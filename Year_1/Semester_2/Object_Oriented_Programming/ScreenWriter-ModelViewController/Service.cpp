#include "Service.h"

using namespace std;

Service::Service(Repository& repo) : repo{ repo }
{
}

std::vector<ScreenWriter> Service::getWriters()
{
	return this->repo.getWriters();
}

std::vector<Idea> Service::getIdeas()
{
	return this->repo.getIdeas();
}

bool Service::isDescriptionPresent(std::string description)
{
	for (auto el : this->getIdeas())
		if (el.getDescription() == description)
			return true;
	return false;
}

void Service::addIdea(std::string description, std::string status, std::string creator, int act)
{
	Idea i{ description, status, creator, act };
	this->repo.addIdea(i);
}

void Service::reviseIdea(int index)
{
	this->repo.reviseIdea(index);
}

void Service::setIdeaAct(Idea i, int value)
{
	this->repo.setIdeaAct(i, value);
}
