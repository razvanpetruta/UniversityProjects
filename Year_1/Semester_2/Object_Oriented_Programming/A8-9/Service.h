#pragma once
#include "Repository.h"
#include "RepositoryCSV.h"
#include "RepositoryHTML.h"
#include "RepositorySQL.h"
#include "Comparator.h"

class Service
{
	private:
		Repository* dogsRepo;
		Repository* userAdoptionList;

	public:
		// Service constructor
		Service(Repository* _dogsRepo, Repository* _userAdoptionList);

		/*
			Add a dog to the dogsRepo.
		*/
		void addDog(int id, const std::string& breed, const std::string& name, int age, const std::string& photograph);

		/*
			Remove a dog from the dogsRepo.
		*/
		void removeDog(int id);

		/*
			Update dog's information.
		*/
		void updateBreed(int id, std::string& _breed);

		/*
			Update dog's information.
		*/
		void updateName(int id, std::string& _name);

		/*
			Update dog's information.
		*/
		void updateAge(int id, int _age);

		/*
			Update dog's information.
		*/
		void updatePhotograph(int id, std::string& _photograph);

		/*
			Update dog's information.
		*/
		void adoptDog(int id);

		/*
			Get the length of the dogsRepo.
		*/
		int getDogsRepoSize() const;

		/*
			Get the length of the userAdoptionList.
		*/
		int getUserAdoptionListSize() const;

		/*
			Return a copy of the dogsRepo.
		*/
		std::vector<Dog> getDogsRepoElements() const;

		/*
			Return a copy of the userAdoptionList.
		*/
		std::vector<Dog> getUserAdpotionElements() const;

		void saveElementsToFile();

		void initialiseApplicationFromFile();

		void saveAdoptionList();
};

template <typename T>
void customSort(std::vector<T>& v, Comparator<T>* c)
{
	for (int i = 0; i < v.size() - 1; i++)
		for (int j = i + 1; j < v.size(); j++)
			if (!c->compare(v[i], v[j]))
			{
				Dog aux = v[i];
				v[i] = v[j];
				v[j] = aux;
			}
}