#include "UserInterface.h"
#include <iostream>

UI::UI(Service& _serv): serv{_serv} {}

void UI::printMenu()
{
	std::cout << "\n";
	std::cout << "1. Connect as admin\n";
	std::cout << "2. Connect as normal user\n";
	std::cout << "3. Exit app\n";
	std::cout << "\n";
}

void UI::printAdminMenu()
{
	std::cout << "\n";
	std::cout << "\t1. add a dog\n";
	std::cout << "\t2. remove a dog\n";
	std::cout << "\t3. update a dog's information\n";
	std::cout << "\t4. list all dogs\n";
	std::cout << "\t5. change user mode\n";
	std::cout << "\n";
}

void UI::printUpdateMenu()
{
	std::cout << "\n";
	std::cout << "\t\t1. update breed\n";
	std::cout << "\t\t2. update name\n";
	std::cout << "\t\t3. update age\n";
	std::cout << "\t\t4. update photograph\n";
	std::cout << "\n";
}

void UI::printUserMenu()
{
	std::cout << "\n";
	std::cout << "\t1. see the available dogs\n";
	std::cout << "\t2. search for a certain breed, having a maximum age\n";
	std::cout << "\t3. see your adoption list\n";
	std::cout << "\t4. change user mode\n";
	std::cout << "\n";
}

void UI::printAdoptMenu()
{
	std::cout << "\n";
	std::cout << "\t\t1. adopt\n";
	std::cout << "\t\t2. continue\n";
	std::cout << "\t\t3. stop\n";
	std::cout << "\n";
}

int UI::readCommand()
{
	int command;

	std::cout << "command: ";
	std::cin >> command;

	return command;
}

int UI::readAdminCommand()
{
	std::cout << "\t";
	int command = UI::readCommand();

	while (command != 1 && command != 2 && command != 3 && command != 4 && command != 5)
	{
		std::cout << "\ninvalid command, try again...\n\n";
		std::cout << "\t";
		command = UI::readCommand();
	}

	return command;
}

int UI::readUserCommand()
{
	std::cout << "\t";
	int command = UI::readCommand();

	while (command != 1 && command != 2 && command != 3 && command != 4)
	{
		std::cout << "invalid command, try again...\n";
		std::cout << "\t";
		command = UI::readCommand();
	}

	return command;
}

int UI::readUpdateCommand()
{
	std::cout << "\t\t";
	int command = UI::readCommand();

	while (command != 1 && command != 2 && command != 3 && command != 4)
	{
		std::cout << "invalid command, try again...\n";
		std::cout << "\t\t";
		command = UI::readCommand();
	}

	return command;
}

int UI::readAdoptCommand()
{
	std::cout << "\t\t";
	int command = UI::readCommand();

	while (command != 1 && command != 2 && command != 3)
	{
		std::cout << "invalid command, try again...\n";
		std::cout << "\t\t";
		command = UI::readCommand();
	}

	return command;
}

void UI::addDogFromAdmin()
{
	int id, age;
	std::string breed, name, photograph;

	std::cout << "\tfill in the necessary information:\n";

	std::cout << "\tid: ";
	std::cin >> id;

	std::cin.ignore();

	std::cout << "\tbreed: ";
	std::getline(std::cin, breed);

	std::cout << "\tname: ";
	std::getline(std::cin, name);

	std::cout << "\tage: ";
	std::cin >> age;

	std::cin.ignore();

	std::cout << "\tphotograph: ";
	std::getline(std::cin, photograph);

	bool added = this->serv.addDog(id, breed, name, age, photograph);

	if (added)
	{
		std::cout << "\tSuccessfully added!\n";
	}
	else
	{
		std::cout << "\tDuplicate id found...\n";
	}
}

void UI::removeDogFromAdmin()
{
	int id;

	std::cout << "\tid to be deleted: ";
	std::cin >> id;

	bool deleted = this->serv.removeDog(id);

	if (deleted)
	{
		std::cout << "\tSuccessfully deleted!\n";
	}
	else
	{
		std::cout << "\tId not found...\n";
	}
}

void UI::updateBreedFromAdmin()
{
	std::string breed;
	int id;
	
	std::cout << "\t\tid: ";
	std::cin >> id;

	std::cin.ignore();

	std::cout << "\t\tnew breed: ";
	std::getline(std::cin, breed);

	bool updated = this->serv.updateBreed(id, breed);

	if (updated)
	{
		std::cout << "\t\tSuccessfully updated!\n";
	}
	else
	{
		std::cout << "\t\tId not found...\n";
	}
}

void UI::updateNameFromAdmin()
{
	std::string name;
	int id;

	std::cout << "\t\tid: ";
	std::cin >> id;

	std::cin.ignore();

	std::cout << "\t\tnew name: ";
	std::getline(std::cin, name);

	bool updated = this->serv.updateName(id, name);

	if (updated)
	{
		std::cout << "\t\tSuccessfully updated!\n";
	}
	else
	{
		std::cout << "\t\tId not found...\n";
	}
}

void UI::updateAgeFromAdmin()
{
	int id, age;

	std::cout << "\t\tid: ";
	std::cin >> id;

	std::cout << "\t\tnew age: ";
	std::cin >> age;

	bool updated = this->serv.updateAge(id, age);

	if (updated)
	{
		std::cout << "\t\tSuccessfully updated!\n";
	}
	else
	{
		std::cout << "\t\tId not found...\n";
	}
}

void UI::updatePhotographFromAdmin()
{
	std::string photograph;
	int id;

	std::cout << "\t\tid: ";
	std::cin >> id;

	std::cin.ignore();

	std::cout << "\t\tnew photograph: ";
	std::getline(std::cin, photograph);

	bool updated = this->serv.updateBreed(id, photograph);

	if (updated)
	{
		std::cout << "\t\tSuccessfully updated!\n";
	}
	else
	{
		std::cout << "\t\tId not found...\n";
	}
}

void UI::updateDogFromAdmin()
{
	UI::printUpdateMenu();
	int command = UI::readUpdateCommand();

	switch (command)
	{
		case 1:
		{
			this->updateBreedFromAdmin();
			break;
		}

		case 2:
		{
			this->updateNameFromAdmin();
			break;
		}

		case 3:
		{
			this->updateAgeFromAdmin();
			break;
		}

		case 4:
		{
			this->updatePhotographFromAdmin();
			break;
		}
	}
}

void UI::printListOfDogs()
{
	DynamicVector<Dog> dogs = this->serv.getDogsRepoElements();

	std::cout << "\n";

	for (int i = 0; i < this->serv.getDogsRepoSize(); i++)
	{
		std::cout << "\t" << dogs[i].toString() << "\n";
	}
}

void UI::printListOfAdoptedDogsForUser()
{
	DynamicVector<Dog> dogs = this->serv.getUserAdpotionElements();

	std::cout << "\n";

	for (int i = 0; i < this->serv.getUserAdoptionListSize(); i++)
	{
		std::cout << "\t" << dogs[i].toString() << "\n";
	}
}

void UI::adoptionProcess()
{
	int command, currentIndex = 0;
	DynamicVector<Dog> availableDogs = this->serv.getDogsRepoElements();

	while (true)
	{
		if (this->serv.getDogsRepoSize() == 0)
		{
			std::cout << "\t\tsorry, we are out of soul saviours...\n";
			return;
		}

		if (currentIndex >= availableDogs.getSize())
		{
			currentIndex = 0;
			availableDogs = this->serv.getDogsRepoElements();
		}

		std::cout << "\n\t\t" << "id: " << availableDogs[currentIndex].getId() << "\n";
		std::cout << "\n\t\t" << "breed: " << availableDogs[currentIndex].getBreed() << "\n";
		std::cout << "\n\t\t" << "name: " << availableDogs[currentIndex].getName() << "\n";
		std::cout << "\n\t\t" << "age: " << availableDogs[currentIndex].getAge() << "\n";
		std::cout << "\n\t\t" << "photograph: please check your browser... \n";
		/*std::string op = std::string("start ").append(availableDogs[currentIndex].getPhotograph());
		system(op.c_str());*/

		UI::printAdoptMenu();
		command = UI::readAdoptCommand();

		switch (command)
		{
			case 1:
			{
				this->serv.adoptDog(availableDogs[currentIndex].getId());
				currentIndex++;
				std::cout << "\t\tSuccessfully adopted\n";
				break;
			}

			case 2:
			{
				currentIndex++;
				break;
			}

			case 3:
			{
				return;
			}
		}
	}
}

void UI::filterDogs()
{
	std::string breed;
	int age;

	std::cin.ignore();

	std::cout << "\t\tbreed: ";
	std::getline(std::cin, breed);
	
	std::cout << "\t\tmaximum age: ";
	std::cin >> age;

	DynamicVector<Dog> matches = this->serv.filterDogsByBreedAndAge(breed, age);
	DynamicVector<Dog> currentState = matches;

	int currentIndex = 0, command;
	while (true)
	{
		if (matches.getSize() == 0)
		{
			std::cout << "\t\tsorry, we are out of soul saviours...\n";
			return;
		}

		if (currentIndex >= currentState.getSize())
		{
			currentIndex = 0;
			currentState = matches;
		}
		std::cout << "\n\t\t" << "id: " << currentState[currentIndex].getId() << "\n";
		std::cout << "\n\t\t" << "breed: " << currentState[currentIndex].getBreed() << "\n";
		std::cout << "\n\t\t" << "name: " << currentState[currentIndex].getName() << "\n";
		std::cout << "\n\t\t" << "age: " << currentState[currentIndex].getAge() << "\n";
		std::cout << "\n\t\t" << "photograph: please check your browser... \n";
		/*std::string op = std::string("start ").append(currentState[currentIndex].getPhotograph());
		system(op.c_str());*/

		UI::printAdoptMenu();
		command = UI::readAdoptCommand();

		switch (command)
		{
			case 1:
			{
				matches.remove(currentIndex);
				this->serv.adoptDog(currentState[currentIndex].getId());
				currentIndex++;
				std::cout << "\t\tSuccessfully adopted\n";
				break;
			}

			case 2:
			{
				currentIndex++;
				break;
			}

			case 3:
			{
				return;
			}
		}
	}
}

void UI::solveAdmin()
{
	int command;

	while (true)
	{
		UI::printAdminMenu();
		command = UI::readAdminCommand();

		switch (command)
		{
			case 1:
			{
				this->addDogFromAdmin();
				break;
			}

			case 2:
			{
				this->removeDogFromAdmin();
				break;
			}

			case 3:
			{
				this->updateDogFromAdmin();
				break;
			}

			case 4:
			{
				this->printListOfDogs();
				break;
			}

			case 5:
			{
				return;
			}
		}
	}
}

void UI::solveUser()
{
	int command;

	while (true)
	{
		UI::printUserMenu();
		command = UI::readUserCommand();

		switch (command)
		{
			case 1:
			{
				this->adoptionProcess();
				break;
			}

			case 2:
			{
				this->filterDogs();
				break;
			}

			case 3:
			{
				this->printListOfAdoptedDogsForUser();
				break;
			}

			case 4:
			{
				return;
			}
		}
	}
}

void UI::run()
{
	int command;

	this->serv.addDog(1, "Labrador Retriever", "Spike", 10, "https://www.taramulanimalelor.com/wp-content/uploads/2020/10/Labrador-caracteristici-rasa-caine.jpg");
	this->serv.addDog(2, "French Bulldog", "Charlie", 20, "https://www.akc.org/wp-content/uploads/2017/11/French-Bulldog-standing-outdoors.jpg");
	this->serv.addDog(3, "German Shepher", "Max", 7, "https://www.hospitalveterinariglories.com/wp-content/uploads/2021/08/04-08-21-Conoce-al-pastor-alema%CC%81n.jpg");
	this->serv.addDog(4, "Golden Retriever", "Rex", 6, "https://upload.wikimedia.org/wikipedia/commons/d/d6/Golden_retriever-035.JPG");
	this->serv.addDog(5, "Bulldog", "Leo", 3, "https://rasedecaini.ro/wp-content/uploads/2019/05/Bulldog-Englez.jpg");
	this->serv.addDog(6, "Poodle", "Zeus", 2, "https://a-z-animals.com/media/Poodle-Canis-familiaris-white.jpg");
	this->serv.addDog(7, "Beagle", "Henry", 7, "https://www.zooplus.ro/ghid/wp-content/uploads/2021/07/caine-beagle.jpeg");
	this->serv.addDog(8, "Rottweiler", "Ace", 1, "https://www.animalepierdute.ro/wp-content/uploads/2019/09/Rottweiler.jpg");
	this->serv.addDog(9, "Dachshund", "Coco", 2, "https://media-be.chewy.com/wp-content/uploads/2021/12/22164927/Dachshund-FeaturedImage.jpg");
	this->serv.addDog(10, "German Shorthaired Pointer", "Buddy", 11, "https://rasedecaini.ro/wp-content/uploads/2019/05/rasa-German-Shorthaired-Pointer.jpg");

	while (true)
	{
		UI::printMenu();
		command = UI::readCommand();

		switch (command)
		{
			case 1:
			{
				this->solveAdmin();
				break;
			}

			case 2:
			{
				this->solveUser();
				break;
			}

			case 3:
			{
				return;
			}
		}
	}
}
