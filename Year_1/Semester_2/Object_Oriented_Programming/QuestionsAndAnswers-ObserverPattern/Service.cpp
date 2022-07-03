#include "Service.h"

Service::Service(Repository& repo) : repo{ repo }
{
}

std::vector<User> Service::getUsers()
{
	return this->repo.getUsers();
}

std::vector<Question> Service::getQuestions()
{
	return this->repo.getQuestions();
}

std::vector<Answer> Service::getAnswers()
{
	return this->repo.getAnswers();
}

void Service::addQuestion(std::string text, std::string name)
{
	int size = this->getQuestions().size();
	Question q{ size + 1, text, name };
	this->repo.addQuestion(q);
	this->notify();
}

void Service::addAnswer(int questionId, std::string userName, std::string text, int votes)
{
	int size = this->repo.getAnswers().size();
	Answer a{ size + 1, questionId, userName, text, votes };
	this->repo.addAnswer(a);
	this->notify();
}

std::vector<Answer> Service::getAnswersByQuestion(Question q)
{
	return this->repo.getAnswersByQuestion(q);
}

Question Service::getBestQuestion(std::string filter)
{
	return this->repo.getBestQuestion(filter);
}

void Service::updateVotes(Answer a, int votes)
{
	this->repo.updateVotes(a, votes);
	this->notify();
}
