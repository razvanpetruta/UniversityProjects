#include "Repository.h"
#include <fstream>
#include <algorithm>

using namespace std;

void Repository::loadWriters()
{
	ifstream f{ "writers.txt" };

	ScreenWriter sw{};
	while (f >> sw)
	{
		this->writers.push_back(sw);
	}

	f.close();
}

void Repository::loadIdeas()
{
	ifstream f{ "ideas.txt" };

	Idea i{};
	while (f >> i)
	{
		this->ideas.push_back(i);
	}

	f.close();
}

void Repository::sortIdeas()
{
	sort(this->ideas.begin(), this->ideas.end(), [](Idea i1, Idea i2)
		{
			return i1.getAct() < i2.getAct();
		});
}

void Repository::saveIdeas()
{
	ofstream f{ "ideas.txt" };

	for (auto el : this->ideas)
		f << el << "\n";

	f.close();
}

Repository::Repository()
{
	this->loadWriters();
	this->loadIdeas();
	this->sortIdeas();
}

std::vector<ScreenWriter> Repository::getWriters()
{
	return this->writers;
}

std::vector<Idea> Repository::getIdeas()
{
	return this->ideas;
}

void Repository::addIdea(Idea i)
{
	this->ideas.push_back(i);
	this->sortIdeas();
	this->saveIdeas();
}

void Repository::reviseIdea(int index)
{
	this->ideas[index].setStatus("approved");
	this->saveIdeas();
}

void Repository::setIdeaAct(Idea i, int value)
{
	int index = 0;
	for (auto el : this->ideas)
	{
		if (el == i)
		{
			this->ideas[index].setAct(value);
			this->sortIdeas();
			this->saveIdeas();
			break;
		}
		index++;
	}
}
