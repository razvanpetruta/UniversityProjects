#include "Service.h"

Service::Service(Repository& _dogsRepo, Repository& _userAdoptionList) : dogsRepo{ _dogsRepo }, userAdoptionList{ _userAdoptionList } {}

bool Service::addDog(int id, std::string breed, std::string name, int age, std::string photograph)
{
	Dog d{ id, breed, name, age, photograph };

	return this->dogsRepo.add(d);
}

bool Service::removeDog(int id)
{
	return this->dogsRepo.remove(id);
}

bool Service::updateBreed(int id, std::string& _breed)
{
	return this->dogsRepo.updateBreed(id, _breed);
}

bool Service::updateName(int id, std::string& _name)
{
	return this->dogsRepo.updateName(id, _name);
}

bool Service::updateAge(int id, int _age)
{
	return this->dogsRepo.updateAge(id, _age);
}

bool Service::updatePhotograph(int id, std::string& _photograph)
{
	return this->dogsRepo.updatePhotograph(id, _photograph);
}

bool Service::adoptDog(int id)
{
	int dogIndex = this->dogsRepo.getIdIndex(id);

	if (dogIndex == -1)
		return false;

	Dog d{ this->dogsRepo.getElements()[dogIndex]};

	this->userAdoptionList.add(d);

	this->dogsRepo.remove(id);

	return true;
}

int Service::getDogsRepoSize() const
{
	return this->dogsRepo.getSize();
}

int Service::getUserAdoptionListSize() const
{
	return this->userAdoptionList.getSize();
}

DynamicVector<Dog> Service::getDogsRepoElements()
{
	return this->dogsRepo.getElements();
}

DynamicVector<Dog> Service::getUserAdpotionElements()
{
	return this->userAdoptionList.getElements();
}

DynamicVector<Dog> Service::filterDogsByBreedAndAge(std::string& breed, int age)
{
	DynamicVector<Dog> sol;
	DynamicVector<Dog> availableDogs = this->getDogsRepoElements();

	for (int i = 0; i < availableDogs.getSize(); i++)
	{
		if ((breed == "" || availableDogs[i].getBreed().find(breed) != std::string::npos) && availableDogs[i].getAge() <= age)
		{
			sol.add(availableDogs[i]);
		}
	}

	return sol;
}
