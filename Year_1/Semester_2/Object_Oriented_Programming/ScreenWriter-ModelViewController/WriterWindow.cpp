#include "WriterWindow.h"
#include <QMessageBox>
#include <fstream>

using namespace std;

WriterWindow::WriterWindow(ScreenWriter writer, IdeasModel* model, QWidget* parent)
	: writer{ writer }, model{ model }, QWidget(parent)
{
	ui.setupUi(this);
	this->setWindowTitle(QString::fromStdString(this->writer.getName()));
	this->ui.ideasView->setModel(model);

	if (this->writer.getExpertise() != "Senior")
		this->ui.reviseButton->setDisabled(true);

	this->handleDevelopButton();
	this->ui.ideasView->resizeRowsToContents();

	QObject::connect(this->ui.addButton, &QPushButton::clicked, this, &WriterWindow::addHandle);
	QObject::connect(this->ui.reviseButton, &QPushButton::clicked, this, &WriterWindow::reviseHandle);
	QObject::connect(this->ui.developButton, &QPushButton::clicked, this, &WriterWindow::handleDevelop);
}

WriterWindow::~WriterWindow()
{
}

int WriterWindow::getSelectedIndex()
{
	QModelIndexList selectedIndexes = this->ui.ideasView->selectionModel()->selectedIndexes();
	if (selectedIndexes.isEmpty())
	{
		return -1;
	}
	int selectedIndex = selectedIndexes.at(0).row();
	return selectedIndex;
}

void WriterWindow::reviseHandle()
{
	int index = this->getSelectedIndex();
	if (index == -1) {
		QMessageBox::critical(this, "Error", "Please select the idea to revise!");
		return;
	}

	try
	{
		this->model->reviseIdea(index);
		this->handleDevelopButton();
	}
	catch (exception& e)
	{
		QMessageBox::information(this, "Error", e.what(), QMessageBox::Ok);
	}
}

void WriterWindow::handleDevelop()
{
	for (auto el : this->saveButtons)
		delete el;
	for (auto el : this->textEdits)
		delete el;
	this->saveButtons.clear();
	this->textEdits.clear();

	for (auto idea : this->model->getIdeaas())
	{
		if (idea.getCreator() == this->writer.getName() && idea.getStatus() == "approved")
		{
			QTextEdit* textEdit = new QTextEdit{};
			textEdit->setText(QString::fromStdString(idea.getDescription()));
			this->textEdits.push_back(textEdit);
			QPushButton* button = new QPushButton{ "Save" };
			string name = this->writer.getName();
			QObject::connect(button, &QPushButton::clicked, this, [name, textEdit]()
				{
					string s = "develop" + name + ".txt";
					ofstream f{ s };
					string description = textEdit->toPlainText().toStdString();
					f << description;
					f.close();
				});
			this->saveButtons.push_back(button);
		}
	};
	QWidget* w = new QWidget{};
	QVBoxLayout* l = new QVBoxLayout{ w };
	for (int i = 0; i < this->saveButtons.size(); i++)
	{
		QHBoxLayout* layout = new QHBoxLayout{};
		l->addLayout(layout);
		layout->addWidget(this->textEdits[i]);
		layout->addWidget(this->saveButtons[i]);
	}
	w->show();
}

void WriterWindow::handleDevelopButton()
{
	int cnt = 0;
	for (auto idea : this->model->getIdeaas())
	{
		if (idea.getCreator() == this->writer.getName() && idea.getStatus() == "approved")
			cnt++;
	}

	if (cnt > 0)
		this->ui.developButton->setDisabled(false);
	else
		this->ui.developButton->setDisabled(true);
}

void WriterWindow::addHandle()
{
	string description = this->ui.descriptionLineEdit->text().toStdString();
	string actS = this->ui.actLineEdit->text().toStdString();
	
	if (description.size() > 0 && actS.size() > 0)
	{
		int act = stoi(actS);
		try
		{
			this->model->addIdea(description, "proposed", this->writer.getName(), act);
		}
		catch (exception& e)
		{
			QMessageBox::information(this, "Error", e.what(), QMessageBox::Ok);
		}
	}
	else
	{
		QMessageBox::information(this, "Error", "Description or Act empty", QMessageBox::Ok);
	}
}