#include "Idea.h"
#include <sstream>
#include "Utils.h"

using namespace std;

Idea::Idea(std::string description, std::string status, std::string creator, int act) :
	description{ description }, status{ status }, creator{ creator }, act{ act }
{
}

std::string Idea::getDescription()
{
	return this->description;
}

std::string Idea::getStatus()
{
	return this->status;
}

std::string Idea::getCreator()
{
	return this->creator;
}

int Idea::getAct()
{
	return this->act;
}

void Idea::setDescription(std::string description)
{
	this->description = description;
}

void Idea::setStatus(std::string status)
{
	this->status = status;
}

void Idea::setCreator(std::string creator)
{
	this->creator = creator;
}

void Idea::setAct(int act)
{
	this->act = act;
}

std::string Idea::toString()
{
	stringstream ss;
	ss << this->description << ";" << this->status << ";" << this->creator << ";" << this->act;
	return ss.str();
}

bool Idea::operator==(Idea& i)
{
	return this->description == i.description && this->act == i.act && this->creator == i.creator;
}

std::ostream& operator<<(std::ostream& os, Idea& i)
{
	os << i.toString();
	return os;
}

std::istream& operator>>(std::istream& is, Idea& i)
{
	string line;
	getline(is, line);
	vector<string> tokens = tokenize(line, ';');

	if (tokens.size() != 4)
		return is;

	i.description = tokens[0];
	i.status = tokens[1];
	i.creator = tokens[2];
	i.act = stoi(tokens[3]);

	return is;
}
