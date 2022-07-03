#include "AnswersWindow.h"

using namespace std;

AnswersWindow::AnswersWindow(Service& service, QWidget *parent)
	: service{ service }, QWidget(parent)
{
	ui.setupUi(this);
	QObject::connect(this->ui.searchEdit, &QLineEdit::textChanged, this, &AnswersWindow::searchHandle);
}

AnswersWindow::~AnswersWindow()
{
}

void AnswersWindow::searchHandle()
{
	string text = this->ui.searchEdit->text().toStdString();
	this->ui.questionEdit->clear();
	this->ui.listWidget->clear();

	if (text.size() > 0)
	{
		Question q = this->service.getBestQuestion(text);
		this->ui.questionEdit->setText(QString::fromStdString(q.toString()));
		int i = 0;
		vector<Answer> sol = this->service.getAnswersByQuestion(q);
		sort(sol.begin(), sol.end(), [](Answer a1, Answer a2)
			{
				return a1.getNrOfVotes() > a2.getNrOfVotes();
			});
		for (auto a : sol)
		{
			if (i < 3)
			{
				auto item = new QListWidgetItem{ a.toString().c_str() };
				this->ui.listWidget->addItem(item);
			}
			i++;
		}
	}
	else
	{
		this->ui.questionEdit->clear();
		this->ui.listWidget->clear();
	}
}
