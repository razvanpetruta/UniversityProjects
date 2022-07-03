#include "Dog.h"
#include <sstream>
#include <vector>
#include <iostream>

Dog::Dog() : id{ 0 }, breed{ "" }, name{ "" }, age{ 0 }, photograph{ "" } {}

Dog::Dog(int _id, const std::string& _breed, const std::string& _name, int _age, const std::string& _photograph)
	: id{ _id }, breed{ _breed }, name{ _name }, age{ _age }, photograph{ _photograph } {}

int Dog::getId() const
{
	return this->id;
}

std::string Dog::getBreed() const
{
	return this->breed;
}

std::string Dog::getName() const
{
	return this->name;
}

int Dog::getAge() const
{
	return this->age;
}

std::string Dog::getPhotograph() const
{
	return this->photograph;
}

void Dog::setBreed(std::string& _breed)
{
	this->breed = _breed;
}

void Dog::setName(std::string& _name)
{
	this->name = _name;
}

void Dog::setAge(int _age)
{
	this->age = _age;
}

void Dog::setPhotograph(std::string& _photograph)
{
	this->photograph = _photograph;
}

std::string Dog::toString() const
{
	std::stringstream representation;

	representation << "id: " << this->id << ", breed: " << this->breed << ", name: " << this->name << ", age: " << this->age;

	return representation.str();
}

std::vector<std::string> tokenize(std::string str, char delimiter)
{
	std::vector <std::string> result;
	std::stringstream ss(str);
	std::string token;
	while (std::getline(ss, token, delimiter))
		result.push_back(token);

	return result;
}

std::istream& operator>>(std::istream& is, Dog& d)
{
	std::string line;
	std::getline(is, line);

	std::vector<std::string> tokens = tokenize(line, '|');

	if (tokens.size() != 5)
		return is;

	d.id = std::stoi(tokens[0]);
	d.breed = tokens[1];
	d.name = tokens[2];
	d.age = std::stoi(tokens[3]);
	d.photograph = tokens[4];

	return is;
}

std::ostream& operator<<(std::ostream& os, const Dog& d)
{
	os << d.getId() << '|' << d.getBreed() << '|' << d.getName() << '|' << d.getAge() << '|' << d.getPhotograph() << '\n';
	return os;
}
