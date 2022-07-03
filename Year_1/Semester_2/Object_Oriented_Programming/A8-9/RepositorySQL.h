#pragma once
#include "Repository.h"

class RepositorySQL : public Repository
{
	private:
		const char* DBpath = "D:\\ubb stuff\\Year 1\\Semester 2\\Object Oriented Programming\\Assignements\\a8-9-915-Petruta-Razvan\\DogShelterV2\\DOGS.db";

		// insert a Dog in the database
		void insertDogSQL(const Dog& d);

		// delete a Dog from the database
		void deleteDogSQL(int id);

		// update a Dog from the database
		void updateBreedSQL(int id, std::string& breed);

		// update a Dog from the database
		void updateNameSQL(int id, std::string& name);

		// update a Dog from the database
		void updateAgeSQL(int id, int age);

		// update a Dog from the database
		void updatePhotographSQL(int id, std::string& photograph);

		// select all the dogs
		void selectDogs();
	public:
		RepositorySQL();

		~RepositorySQL() {};

		void add(const Dog& d) override;

		void remove(int id) override;

		void updateBreed(int id, std::string& _breed) override;

		void updateName(int id, std::string& _name) override;

		void updateAge(int id, int _age) override;

		void updatePhotograph(int id, std::string& _photograph) override;

		void initialiseRepositoryFromFile() override;

		void saveRepositoryToFile() override {};
};