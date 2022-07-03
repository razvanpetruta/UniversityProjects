#pragma once
#include "DynamicVector.h"

class Repository
{
	private:
		DynamicVector<Dog> elements;

	public:
		/*
			Return the index in the DynamicVector of a certain id.
			id: the id for which we are searching the index
		*/
		int getIdIndex(int id);

		/*
			Add a dog to the repository.
			e: the dog we intend to add
		*/
		bool add(const Dog& e);

		/*
			Remove a certain id from the repository.
			id: the id we want to remove
		*/
		bool remove(int id);

		/*
			Update the breed of a dog.
			id: the dog id
			_breed: the new breed
		*/
		bool updateBreed(int id, std::string& _breed);

		/*
			Update the name of a dog.
			id: the dog id
			_name: the new name
		*/
		bool updateName(int id, std::string& _name);

		/*
			Update the age of a dog.
			id: the dog id
			_age: the new age
		*/
		bool updateAge(int id, int _age);

		/*
			Update the photograph link of a dog.
			id: the dog id
			_photograph: the new link to the photograph
		*/
		bool updatePhotograph(int id, std::string& _photograph);

		/*
			Return a copy of the dogs.
		*/
		DynamicVector<Dog> getElements();

		// Return the number of elements that the repository is holding.
		int getSize() const;
};