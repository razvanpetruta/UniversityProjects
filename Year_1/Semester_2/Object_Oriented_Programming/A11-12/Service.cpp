#include "Service.h"
#include "Exceptions.h"

Service::Service(Repository* _dogsRepo, Repository* _userAdoptionList) : dogsRepo{ _dogsRepo }, userAdoptionList{ _userAdoptionList } {}

void Service::addDog(int id, const std::string& breed, const std::string& name, int age, const std::string& photograph)
{
	Dog d{ id, breed, name, age, photograph };

	DogValidator::validate(d);

	this->dogsRepo->add(d);

	this->saveElementsToFile();
}

void Service::removeDog(int id)
{
	this->dogsRepo->remove(id);

	this->saveElementsToFile();
}

void Service::updateBreed(int id, std::string& _breed)
{
	this->dogsRepo->updateBreed(id, _breed);

	this->saveElementsToFile();
}

void Service::updateName(int id, std::string& _name)
{
	this->dogsRepo->updateName(id, _name);

	this->saveElementsToFile();
}

void Service::updateAge(int id, int _age)
{
	this->dogsRepo->updateAge(id, _age);

	this->saveElementsToFile();
}

void Service::updatePhotograph(int id, std::string& _photograph)
{
	this->dogsRepo->updatePhotograph(id, _photograph);

	this->saveElementsToFile();
}

void Service::adoptDog(int id)
{
	int dogIndex = this->dogsRepo->getIdIndex(id);

	if (dogIndex == -1)
		throw RepositoryException("Dog not found, cannot adopt it\n");

	Dog d{ this->dogsRepo->getElements()[dogIndex]};

	this->userAdoptionList->add(d);

	this->dogsRepo->remove(id);

	this->saveElementsToFile();
}

int Service::getDogsRepoSize() const
{
	return this->dogsRepo->getSize();
}

int Service::getUserAdoptionListSize() const
{
	return this->userAdoptionList->getSize();
}

std::vector<Dog> Service::getDogsRepoElements() const
{
	return this->dogsRepo->getElements();
}

std::vector<Dog> Service::getUserAdpotionElements() const
{
	return this->userAdoptionList->getElements();
}

void Service::saveElementsToFile()
{
	this->dogsRepo->saveRepositoryToFile();
}

void Service::initialiseApplicationFromFile()
{
	this->dogsRepo->initialiseRepositoryFromFile();
}

void Service::saveAdoptionList()
{
	this->userAdoptionList->saveAdoptionListAndOpen();
}

void Service::changeUserAdoptionListType(std::string type)
{
	std::vector<Dog> elements = this->getUserAdpotionElements();

	delete this->userAdoptionList;

	if (type == "csv")
	{
		Repository* repository = new RepositoryCSV{};
		for (auto d : elements)
		{
			repository->add(d);
		}
		this->userAdoptionList = repository;
	}
	else if (type == "html")
	{
		Repository* repository = new RepositoryHTML{};
		for (auto d : elements)
		{
			repository->add(d);
		}
		this->userAdoptionList = repository;
	}
}
