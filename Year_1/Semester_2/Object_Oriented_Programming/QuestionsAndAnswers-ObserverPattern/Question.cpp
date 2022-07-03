#include "Question.h"
#include <sstream>
#include "Utils.h"

using namespace std;

Question::Question(int id, std::string text, std::string userName) :
	id{ id }, text{ text }, userName{ userName }
{
}

int Question::getId()
{
	return this->id;
}

std::string Question::getText()
{
	return this->text;
}

std::string Question::getUserName()
{
	return this->userName;
}

void Question::setId(int id)
{
	this->id = id;
}

void Question::setText(std::string text)
{
	this->text = text;
}

void Question::setUserName(std::string userName)
{
	this->userName = userName;
}

std::string Question::toString()
{
	stringstream ss;
	ss << this->id << ";" << this->text << ";" << this->userName;
	return ss.str();
}

std::ostream& operator<<(std::ostream& os, Question& q)
{
	os << q.toString();
	return os;
}

std::istream& operator>>(std::istream& is, Question& q)
{
	string line;
	getline(is, line);
	vector<string> tokens = tokenize(line, ';');

	if (tokens.size() != 3)
		return is;

	q.id = stoi(tokens[0]);
	q.text = tokens[1];
	q.userName = tokens[2];

	return is;
}
