#include "Answer.h"
#include "Utils.h"
#include <sstream>

using namespace std;

Answer::Answer(int id, int questionId, std::string userName, std::string text, int nrOfVotes) :
	id{ id }, questionId{ questionId }, userName{ userName }, text{ text }, nrOfVotes{ nrOfVotes }
{
}

int Answer::getId()
{
	return this->id;
}

void Answer::setId(int id)
{
	this->id = id;
}

int Answer::getQuestionId()
{
	return this->questionId;
}

void Answer::setQuestionId(int id)
{
	this->questionId = id;
}

std::string Answer::getUserName()
{
	return this->userName;
}

void Answer::setUserName(std::string userName)
{
	this->userName = userName;
}

std::string Answer::getText()
{
	return this->text;
}

void Answer::setText(std::string text)
{
	this->text = text;
}

int Answer::getNrOfVotes()
{
	return this->nrOfVotes;
}

void Answer::setNrOfVotes(int nr)
{
	this->nrOfVotes = nr;
}

std::string Answer::toString()
{
	stringstream ss;
	ss << this->id << ";" << this->questionId << ";" << this->text << ";" << this->userName << ";" << this->nrOfVotes;
	return ss.str();
}

bool Answer::operator==(Answer a)
{
	return this->id == a.getId();
}

std::ostream& operator<<(std::ostream& os, Answer& a)
{
	os << a.toString();
	return os;
}

std::istream& operator>>(std::istream& is, Answer& a)
{
	string line;
	getline(is, line);
	vector<string> tokens = tokenize(line, ';');

	if (tokens.size() != 5)
		return is;

	a.id = stoi(tokens[0]);
	a.questionId = stoi(tokens[1]);
	a.text = tokens[2];
	a.userName = tokens[3];
	a.nrOfVotes = stoi(tokens[4]);

	return is;
}
