#pragma once
#include "Repository.h"

class Service
{
private:
	Repository& repo;

public:
	Service(Repository& repo);

	std::vector<ScreenWriter> getWriters();

	std::vector<Idea> getIdeas();

	bool isDescriptionPresent(std::string description);

	void addIdea(std::string description, std::string status, std::string creator, int act);

	void reviseIdea(int index);

	void setIdeaAct(Idea i, int value);
};