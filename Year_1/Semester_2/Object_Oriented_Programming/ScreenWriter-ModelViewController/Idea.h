#pragma once
#include <string>

class Idea
{
private:
	std::string description;
	std::string status;
	std::string creator;
	int act;

public:
	Idea(std::string description = "", std::string status = "", std::string creator = "", int act = 0);

	std::string getDescription();

	std::string getStatus();

	std::string getCreator();

	int getAct();

	void setDescription(std::string description);

	void setStatus(std::string status);

	void setCreator(std::string creator);

	void setAct(int act);

	std::string toString();

	friend std::ostream& operator<<(std::ostream& os, Idea& i);

	friend std::istream& operator>>(std::istream& is, Idea& i);

	bool operator==(Idea& i);
};