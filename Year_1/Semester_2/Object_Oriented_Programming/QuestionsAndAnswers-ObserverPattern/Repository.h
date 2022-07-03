#pragma once
#include "Answer.h"
#include "Question.h"
#include "User.h"
#include <vector>

class Repository
{
private:
	std::vector<User> users;
	std::vector<Question> questions;
	std::vector<Answer> answers;

	void loadUsers();

	void loadQuestions();

	void loadAnswers();

	void saveQuestions();

	void saveAnswers();

	void sortQuestions();

public:
	Repository();

	std::vector<User> getUsers();

	std::vector<Question> getQuestions();

	std::vector<Answer> getAnswers();

	void addQuestion(Question q);

	void addAnswer(Answer a);

	std::vector<Answer> getAnswersByQuestion(Question q);

	Question getBestQuestion(std::string filter);

	void updateVotes(Answer a, int votes);
};