#include "User.h"
#include <sstream>

using namespace std;

User::User(std::string name) : name{ name }
{
}

std::string User::getName()
{
	return this->name;
}

void User::setName(std::string name)
{
	this->name = name;
}

std::string User::toString()
{
	stringstream ss;
	ss << this->name;
	return ss.str();
}

std::istream& operator>>(std::istream& is, User& u)
{
	string line;
	getline(is, line);	
	u.name = line;
	return is;
}

std::ostream& operator<<(std::ostream& os, User& u)
{
	os << u.toString();
	return os;
}
