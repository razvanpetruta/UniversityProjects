#pragma once
#include "Repository.h"

class Service
{
	private:
		Repository& dogsRepo;
		Repository& userAdoptionList;

	public:
		// Service constructor
		Service(Repository& _dogsRepo, Repository& _userAdoptionList);

		/*
			Add a dog to the dogsRepo.
		*/
		bool addDog(int id, std::string breed, std::string name, int age, std::string photograph);

		/*
			Remove a dog from the dogsRepo.
		*/
		bool removeDog(int id);

		/*
			Update dog's information.
		*/
		bool updateBreed(int id, std::string& _breed);

		/*
			Update dog's information.
		*/
		bool updateName(int id, std::string& _name);

		/*
			Update dog's information.
		*/
		bool updateAge(int id, int _age);

		/*
			Update dog's information.
		*/
		bool updatePhotograph(int id, std::string& _photograph);

		/*
			Update dog's information.
		*/
		bool adoptDog(int id);

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
		DynamicVector<Dog> getDogsRepoElements();

		/*
			Return a copy of the userAdoptionList.
		*/
		DynamicVector<Dog> getUserAdpotionElements();

		/*
			Filter dogs from dogsRepo by breed, having a maximum age.
		*/
		DynamicVector<Dog> filterDogsByBreedAndAge(std::string& breed, int age);
};