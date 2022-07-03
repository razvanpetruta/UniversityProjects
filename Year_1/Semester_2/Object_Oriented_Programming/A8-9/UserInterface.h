#pragma once
#include "Service.h"

class UI
{
	private:
		Service* serv;

		/*
			Print the general menu.
		*/
		static void printMenu();

		/*
			Print the admin menu.
		*/
		static void printAdminMenu();

		/*
			Print the update menu.
		*/
		static void printUpdateMenu();

		/*
			Print the user menu.
		*/
		static void printUserMenu();

		/*
			Print the adopt menu.
		*/
		static void printAdoptMenu();

		/*
			Read a command.
		*/
		static int readCommand();

		/*
			Read and validate the admin command.
		*/
		static int readAdminCommand();

		/*
			Read and validate the user command.
		*/
		static int readUserCommand();

		/*
			Read and validate the update command.
		*/
		static int readUpdateCommand();

		/*
			Read and validate the adopt command.
		*/
		static int readAdoptCommand();

		/*
			Add a dog in admin mode.
		*/
		void addDogFromAdmin();

		/*
			Remove a dog in user mode.
		*/
		void removeDogFromAdmin();

		/*
			Update the breed in admin mode.
		*/
		void updateBreedFromAdmin();

		/*
			Update the name in admin mode.
		*/
		void updateNameFromAdmin();

		/*
			Update the age in admin mode.
		*/
		void updateAgeFromAdmin();

		/*
			Update the photograph in admin mode.
		*/
		void updatePhotographFromAdmin();

		/*
			Update dog information in admin mode.
		*/
		void updateDogFromAdmin();

		/*
			Print the available dogs.
		*/
		void printListOfDogs();

		/*
			Print the list of adopted dogs.
		*/
		void printListOfAdoptedDogsForUser();

		/*
			Adopt a dog from the user.
		*/
		void adoptionProcess();

		/*
			Filter the dogs for adoption.
		*/
		void filterDogs();

		/*
			Solve the admin's tasks.
		*/
		void solveAdmin();

		/*
			Solve the user's necessities
		*/
		void solveUser();

	public:
		// UI constructor
		UI(Service* _serv);

		/*
			The main menu from where our application starts.
		*/
		void run();
};