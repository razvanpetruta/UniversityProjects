#pragma once
#include <string>

class ScreenWriter
{
private:
	std::string name;
	std::string expertise;

public:
	ScreenWriter(std::string name = "", std::string expertise = "");

	std::string getName();

	std::string getExpertise();

	std::string toString();

	void setName(std::string name);

	void setExpertise(std::string expertise);

	friend std::ostream& operator<<(std::ostream& os, ScreenWriter& sw);

	friend std::istream& operator>>(std::istream& is, ScreenWriter& sw);
};