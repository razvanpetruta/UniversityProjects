#pragma once
#include <vector>
#include "Dog.h"

class Repository
{
	protected:
		std::vector<Dog> elements;

	public:
		virtual ~Repository() {};
		/*
			Return the index in the DynamicVector of a certain id.
			id: the id for which we are searching the index
		*/
		int getIdIndex(int id);

		/*
			Add a dog to the repository.
			e: the dog we intend to add
		*/
		virtual void add(const Dog& e);

		/*
			Remove a certain id from the repository.
			id: the id we want to remove
		*/
		virtual void remove(int id);

		/*
			Update the breed of a dog.
			id: the dog id
			_breed: the new breed
		*/
		virtual void updateBreed(int id, std::string& _breed);

		/*
			Update the name of a dog.
			id: the dog id
			_name: the new name
		*/
		virtual void updateName(int id, std::string& _name);

		/*
			Update the age of a dog.
			id: the dog id
			_age: the new age
		*/
		virtual void updateAge(int id, int _age);

		/*
			Update the photograph link of a dog.
			id: the dog id
			_photograph: the new link to the photograph
		*/
		virtual void updatePhotograph(int id, std::string& _photograph);

		/*
			Return a copy of the dogs.
		*/
		std::vector<Dog> getElements() const;

		// Return the number of elements that the repository is holding.
		int getSize() const;

		virtual void saveRepositoryToFile();

		virtual void initialiseRepositoryFromFile();

		virtual void saveAdoptionListAndOpen() {};
};