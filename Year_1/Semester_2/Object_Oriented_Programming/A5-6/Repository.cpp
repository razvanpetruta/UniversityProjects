#include "Repository.h"

int Repository::getIdIndex(int id)
{
	int foundPos = -1, i = 0;
	
	while (i < this->elements.getSize() && foundPos == -1)
	{
		if (this->elements[i].getId() == id)
		{
			foundPos = i;
		}
		i++;
	}

	return foundPos;
}

bool Repository::add(const Dog& e)
{
	if (this->getIdIndex(e.getId()) != -1)
		return false;

	this->elements.add(e);

	return true;
}

bool Repository::remove(int id)
{
	int index = this->getIdIndex(id);

	if (index == -1)
		return false;

	this->elements.remove(index);

	return true;
}

bool Repository::updateBreed(int id, std::string& _breed)
{
	int index = this->getIdIndex(id);

	if (index == -1)
		return false;

	this->elements[index].setBreed(_breed);

	return true;
}

bool Repository::updateName(int id, std::string& _name)
{
	int index = this->getIdIndex(id);

	if (index == -1)
		return false;

	this->elements[index].setName(_name);

	return true;
}

bool Repository::updateAge(int id, int _age)
{
	int index = this->getIdIndex(id);

	if (index == -1)
		return false;

	this->elements[index].setAge(_age);

	return true;
}

bool Repository::updatePhotograph(int id, std::string& _photograph)
{
	int index = this->getIdIndex(id);

	if (index == -1)
		return false;

	this->elements[index].setPhotograph(_photograph);

	return true;
}

DynamicVector<Dog> Repository::getElements()
{
	DynamicVector<Dog> dogs = this->elements;

	return dogs;
}

int Repository::getSize() const
{
	return this->elements.getSize();
}
