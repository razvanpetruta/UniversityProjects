#pragma once
#include <string>

class Answer
{
private:
	int id;
	int questionId;
	std::string userName;
	std::string text;
	int nrOfVotes;

public:
	Answer(int id = 0, int questionId = 0, std::string userName = "", std::string text = "", int nrOfVotes = 0);

	int getId();

	void setId(int id);

	int getQuestionId();

	void setQuestionId(int id);

	std::string getUserName();

	void setUserName(std::string userName);

	std::string getText();

	void setText(std::string text);

	int getNrOfVotes();

	void setNrOfVotes(int nr);

	std::string toString();

	friend std::ostream& operator<<(std::ostream& os, Answer& a);

	friend std::istream& operator>>(std::istream& is, Answer& a);

	bool operator==(Answer a);
};