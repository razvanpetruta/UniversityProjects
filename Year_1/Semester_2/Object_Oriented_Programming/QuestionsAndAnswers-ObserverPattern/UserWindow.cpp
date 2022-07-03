#include "UserWindow.h"
#include <QMessageBox>
#include <sstream>

using namespace std;

UserWindow::UserWindow(User u, Service& service, QWidget* parent)
	:u{ u }, service{ service }, QWidget(parent)
{
	ui.setupUi(this);
	this->service.addObserver(this);
	this->setWindowTitle(QString::fromStdString(this->u.getName()));
	this->connectSignalsAndSlots();
	this->update();
}

UserWindow::~UserWindow()
{
	this->service.removeObserver(this);
}

void UserWindow::update()
{
	this->populateList();
}

void UserWindow::connectSignalsAndSlots()
{
	QObject::connect(this->ui.addButton, &QPushButton::clicked, this, &UserWindow::addHandle);
	QObject::connect(this->ui.questionsList, &QListWidget::itemSelectionChanged, this, &UserWindow::handleSelected);
	QObject::connect(this->ui.addAnswerButton, &QPushButton::clicked, this, &UserWindow::addAnswerHandle);
	QObject::connect(this->ui.answersList, &QListWidget::itemSelectionChanged, this, &UserWindow::voteSpinnerHandle);
	QObject::connect(this->ui.voteButton, &QPushButton::clicked, this, &UserWindow::voteHandle);
}

void UserWindow::populateList()
{
	int currentRow = this->ui.questionsList->currentRow();

	this->ui.questionsList->clear();

	for (auto u : this->service.getQuestions())
	{
		auto item = new QListWidgetItem{ u.toString().c_str() };
		this->ui.questionsList->addItem(item);
	}

	if(currentRow != -1)
		this->ui.questionsList->setCurrentRow(currentRow);
}

void UserWindow::addHandle()
{
	string text = this->ui.questionLineEdit->text().toStdString();

	if (text.size() > 0)
	{
		this->service.addQuestion(text, this->u.getName());
		this->ui.questionLineEdit->clear();
	}
	else
	{
		QMessageBox::information(this, "Error", "Empty text", QMessageBox::Ok);
	}
}

void UserWindow::addAnswerHandle()
{
	string text = this->ui.newAnswerEdit->text().toStdString();
	string textQuestion = this->ui.questionsList->currentItem()->text().toStdString();

	if (text.size() > 0)
	{
		Question q;
		stringstream ss{ textQuestion };
		ss >> q;
		this->service.addAnswer(q.getId(), this->u.getName(), text, 0);
		this->ui.newAnswerEdit->clear();
	}
	else
	{
		QMessageBox::information(this, "Error", "Empty text or Question not selected", QMessageBox::Ok);
	}
}

void UserWindow::handleSelected()
{
	this->ui.answersList->clear();

	string text = this->ui.questionsList->currentItem()->text().toStdString();

	Question q;
	stringstream ss{ text };
	ss >> q;
	QBrush brush{ "yellow" };
	for (auto a : this->service.getAnswersByQuestion(q))
	{
		auto item = new QListWidgetItem{ a.toString().c_str() };
		if (a.getUserName() == this->u.getName())
			item->setBackground(brush);
		this->ui.answersList->addItem(item);
	}
}

void UserWindow::voteHandle()
{
	string text = this->ui.answersList->currentItem()->text().toStdString();
	stringstream ss{ text };
	Answer a;
	ss >> a;
	this->service.updateVotes(a, this->ui.spinBox->value());
	this->ui.spinBox->setMinimum(0);
	this->ui.spinBox->setMaximum(100000);
	this->ui.spinBox->setDisabled(true);
	this->ui.spinBox->setValue(0);
	this->ui.voteButton->setDisabled(true);
}

void UserWindow::voteSpinnerHandle()
{
	string text = this->ui.answersList->currentItem()->text().toStdString();
	stringstream ss{ text };
	Answer a;
	ss >> a;
	if (a.getUserName() != this->u.getName())
	{
		this->ui.spinBox->setDisabled(false);
		this->ui.voteButton->setDisabled(false);
		this->ui.spinBox->setValue(a.getNrOfVotes());
		this->ui.spinBox->setMinimum(a.getNrOfVotes() - 1);
		this->ui.spinBox->setMaximum(a.getNrOfVotes() + 1);
	}
	else
	{
		this->ui.spinBox->setMinimum(0);
		this->ui.spinBox->setMaximum(100000);
		this->ui.spinBox->setDisabled(true);
		this->ui.spinBox->setValue(0);
		this->ui.voteButton->setDisabled(true);
	}
}
