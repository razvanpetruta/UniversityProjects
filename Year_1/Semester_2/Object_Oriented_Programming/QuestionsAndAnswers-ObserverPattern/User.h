#pragma once
#include <string>

class User
{
private:
	std::string name;

public:
	User(std::string name = "");

	std::string getName();

	void setName(std::string name);

	std::string toString();

	friend std::istream& operator>>(std::istream& is, User& u);

	friend std::ostream& operator<<(std::ostream& os, User& u);
};