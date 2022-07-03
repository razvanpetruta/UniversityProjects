#include "Repository.h"
#include <fstream>
#include <algorithm>

using namespace std;

void Repository::loadUsers()
{
	ifstream f{ "users.txt" };

	User u;
	while (f >> u)
	{
		this->users.push_back(u);
	}

	f.close();
}

void Repository::loadQuestions()
{
	ifstream f{ "questions.txt" };

	Question q;
	while (f >> q)
	{
		this->questions.push_back(q);
	}

	f.close();
}

void Repository::loadAnswers()
{
	ifstream f{ "answers.txt" };

	Answer a;
	while (f >> a)
	{
		this->answers.push_back(a);
	}

	f.close();
}

void Repository::saveQuestions()
{
	ofstream f{ "questions.txt" };

	for (auto q : this->questions)
	{
		f << q << endl;
	}

	f.close();
}

void Repository::saveAnswers()
{
	ofstream f{ "answers.txt" };

	for (auto a : this->answers)
	{
		f << a << endl;
	}

	f.close();
}

void Repository::sortQuestions()
{
	vector<Answer> answers = this->answers;
	sort(this->questions.begin(), this->questions.end(), [answers](Question q1, Question q2)
		{
			int q1count = 0, q2count = 0;
			for (auto el : answers)
			{
				if (el.getQuestionId() == q1.getId())
				{
					q1count++;
				}
				if (el.getQuestionId() == q2.getId())
				{
					q2count++;
				}
			}

			return q1count > q2count;
		});
}

Repository::Repository()
{
	this->loadUsers();
	this->loadQuestions();
	this->loadAnswers();
	this->sortQuestions();
}

std::vector<User> Repository::getUsers()
{
	return this->users;
}

std::vector<Question> Repository::getQuestions()
{
	return this->questions;
}

std::vector<Answer> Repository::getAnswers()
{
	return this->answers;
}

void Repository::addQuestion(Question q)
{
	this->questions.push_back(q);
	this->saveQuestions();
}

void Repository::addAnswer(Answer a)
{
	this->answers.push_back(a);
	this->saveAnswers();
}

std::vector<Answer> Repository::getAnswersByQuestion(Question q)
{
	vector<Answer> sol;

	for (auto el : this->answers)
	{
		if (el.getQuestionId() == q.getId())
			sol.push_back(el);
	}

	return sol;
}

Question Repository::getBestQuestion(std::string filter)
{
	Question sol = this->questions[0];
	int maxScore = 0;

	for (auto q : this->questions)
	{
		int score = 0, i = 0;
		for (auto l : q.getText())
		{
			if (i < filter.size() && l == filter[i])
				score++;
			i++;
		}
		if (score > maxScore)
		{
			maxScore = score;
			sol = q;
		}
	}

	return sol;
}

void Repository::updateVotes(Answer a, int votes)
{
	int i = 0;
	for (auto ans : this->answers)
	{
		if (ans.getId() == a.getId())
		{
			this->answers[i].setNrOfVotes(votes);
			break;
		}
		i++;
	}
	this->sortQuestions();
	this->saveAnswers();
}
