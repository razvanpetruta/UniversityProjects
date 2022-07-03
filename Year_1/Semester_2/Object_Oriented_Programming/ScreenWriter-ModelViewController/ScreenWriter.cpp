#include "ScreenWriter.h"
#include <sstream>
#include "Utils.h"

using namespace std;

ScreenWriter::ScreenWriter(std::string name, std::string expertise) : name{ name }, expertise{ expertise }
{
}

std::string ScreenWriter::getName()
{
	return this->name;
}

std::string ScreenWriter::getExpertise()
{
	return this->expertise;
}

std::string ScreenWriter::toString()
{
	stringstream ss;
	ss << this->name << ";" << this->expertise;
	return ss.str();
}

void ScreenWriter::setName(std::string name)
{
	this->name = name;
}

void ScreenWriter::setExpertise(std::string expertise)
{
	this->expertise = expertise;
}

std::ostream& operator<<(std::ostream& os, ScreenWriter& sw)
{
	os << sw.toString();
	return os;
}

std::istream& operator>>(std::istream& is, ScreenWriter& sw)
{
	string line;
	getline(is, line);
	vector<string> tokens = tokenize(line, ';');

	if (tokens.size() != 2)
		return is;

	sw.name = tokens[0];
	sw.expertise = tokens[1];

	return is;
}
