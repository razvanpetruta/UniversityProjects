#pragma once
#include "Repository.h"
#include "Observer.h"

class Service : public Subject
{
private:
	Repository& repo;

public:
	Service(Repository& repo);

	std::vector<User> getUsers();

	std::vector<Question> getQuestions();

	std::vector<Answer> getAnswers();

	void addQuestion(std::string text, std::string name);

	void addAnswer(int questionId, std::string userName, std::string text, int votes);

	std::vector<Answer> getAnswersByQuestion(Question q);

	Question getBestQuestion(std::string filter);

	void updateVotes(Answer a, int votes);
};