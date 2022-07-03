#pragma once
#include <string>

class Question
{
private:
	int id;
	std::string text;
	std::string userName;

public:
	Question(int id = 0, std::string text = "", std::string userName = "");

	int getId();

	std::string getText();

	std::string getUserName();

	void setId(int id);

	void setText(std::string text);

	void setUserName(std::string userName);

	std::string toString();

	friend std::ostream& operator<<(std::ostream& os, Question& q);

	friend std::istream& operator>>(std::istream& is, Question& q);
};