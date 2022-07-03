#pragma once
#include "Idea.h"
#include "ScreenWriter.h"
#include <vector>

class Repository
{
private:
	std::vector<ScreenWriter> writers;
	std::vector<Idea> ideas;

	void loadWriters();

	void loadIdeas();

	void sortIdeas();

	void saveIdeas();

public:
	Repository();

	std::vector<ScreenWriter> getWriters();

	std::vector<Idea> getIdeas();

	void addIdea(Idea i);

	void reviseIdea(int index);

	void setIdeaAct(Idea i, int value);
};