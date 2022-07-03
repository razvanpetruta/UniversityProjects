#include "Repository.h"
#include "Exceptions.h"
#include <fstream>

int Repository::getIdIndex(int id)
{
	int foundPos = -1, i = 0;
	
	while (i < this->elements.size() && foundPos == -1)
	{
		if (this->elements[i].getId() == id)
		{
			foundPos = i;
		}
		i++;
	}

	return foundPos;
}

void Repository::add(const Dog& e)
{
	if (this->getIdIndex(e.getId()) != -1)
		throw RepositoryException("Duplicate id found\n");

	this->elements.push_back(e);
}

void Repository::remove(int id)
{
	int index = this->getIdIndex(id);

	if (index == -1)
		throw RepositoryException("Id not found, cannot remove it\n");

	this->elements.erase(this->elements.begin() + index);
}

void Repository::updateBreed(int id, std::string _breed)
{
	int index = this->getIdIndex(id);

	if (index == -1)
		throw RepositoryException("Id not found, cannot update it\n");

	this->elements[index].setBreed(_breed);
}

void Repository::updateName(int id, std::string _name)
{
	int index = this->getIdIndex(id);

	if (index == -1)
		throw RepositoryException("Id not found, cannot update it\n");

	this->elements[index].setName(_name);
}

void Repository::updateAge(int id, int _age)
{
	int index = this->getIdIndex(id);

	if (index == -1)
		throw RepositoryException("Id not found, cannot update it\n");

	this->elements[index].setAge(_age);
}

void Repository::updatePhotograph(int id, std::string _photograph)
{
	int index = this->getIdIndex(id);

	if (index == -1)
		throw RepositoryException("Id not found, cannot update it\n");

	this->elements[index].setPhotograph(_photograph);
}

std::vector<Dog> Repository::getElements() const
{
	std::vector<Dog> dogs = this->elements;

	return dogs;
}

int Repository::getSize() const
{
	return this->elements.size();
}

void Repository::saveRepositoryToFile()
{
	std::ofstream f("Repository.txt");

	if (!f.is_open())
		throw RepositoryException("Couldn't open file to save the elements\n");

	for (const auto& d : this->elements)
	{
		f << d;
	}

	f.close();
}

void Repository::initialiseRepositoryFromFile()
{
	std::ifstream f("Repository.txt");

	if (!f.is_open())
		throw RepositoryException("Couldn't open the file to read data\n");

	Dog d{};
	while (f >> d)
	{
		this->add(d);
	}

	f.close();
}
