#include "UserInterface.h"
#include <iostream>
#include <algorithm>
#include "Exceptions.h"

UI::UI(Service* _serv): serv{_serv} {}

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
	
	while (!(std::cin >> command))
	{
		std::cout << "\tPlease enter an integer number: ";
		std::cin.clear();
		std::cin.ignore(100, '\n');
	}

	return command;
}

int UI::readAdminCommand()
{
	std::cout << "\t";
	int command = UI::readCommand();

	while (command != 1 && command != 2 && command != 3 && command != 4 && command != 5)
	{
		std::cout << "\tinvalid command, try again...\n\n";
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
		std::cout << "\tinvalid command, try again...\n";
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
		std::cout << "\t\tinvalid command, try again...\n";
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
		std::cout << "\t\tinvalid command, try again...\n";
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

	this->serv->addDog(id, breed, name, age, photograph);

	std::cout << "\tSuccessfully added!\n";
}

void UI::removeDogFromAdmin()
{
	int id;

	std::cout << "\tid to be deleted: ";
	std::cin >> id;

	this->serv->removeDog(id);

	std::cout << "\tSuccessfully deleted!\n";
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

	this->serv->updateBreed(id, breed);

	std::cout << "\tSuccessfully updated!\n";
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

	this->serv->updateName(id, name);

	std::cout << "\tSuccessfully updated!\n";
}

void UI::updateAgeFromAdmin()
{
	int id, age;

	std::cout << "\t\tid: ";
	std::cin >> id;

	std::cout << "\t\tnew age: ";
	std::cin >> age;

	this->serv->updateAge(id, age);
	
	std::cout << "\tSuccessfully updated!\n";
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

	this->serv->updateBreed(id, photograph);

	std::cout << "\tSuccessfully updated!\n";
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
	std::vector<Dog> dogs = this->serv->getDogsRepoElements();

	std::cout << "\n";

	for (auto const& dog: dogs)
	{
		std::cout << "\t" << dog;
	}
}

void UI::printListOfAdoptedDogsForUser()
{
	this->serv->saveAdoptionList();
}

void UI::adoptionProcess()
{
	int command, currentIndex = 0;
	std::vector<Dog> availableDogs = this->serv->getDogsRepoElements();

	while (true)
	{
		if (this->serv->getDogsRepoSize() == 0)
		{
			std::cout << "\t\tsorry, we are out of soul saviours...\n";
			return;
		}

		if (currentIndex >= availableDogs.size())
		{
			currentIndex = 0;
			availableDogs = this->serv->getDogsRepoElements();
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
				this->serv->adoptDog(availableDogs[currentIndex].getId());
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

	std::vector<Dog> elements = this->serv->getDogsRepoElements();
	std::vector<Dog> matches;
	copy_if(elements.begin(), elements.end(), std::back_inserter(matches),
		[breed, age](const Dog& dog)
		{
			return (breed == "" || dog.getBreed().find(breed) != std::string::npos) && dog.getAge() <= age;
		});
	std::vector<Dog> currentState = matches;

	int currentIndex = 0, command;
	while (true)
	{
		if (matches.size() == 0)
		{
			std::cout << "\t\tsorry, we are out of soul saviours...\n";
			return;
		}

		if (currentIndex >= currentState.size())
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
				int id = currentState[currentIndex].getId();
				auto it = find_if(matches.begin(), matches.end(), [id](const Dog& d)
					{
						return d.getId() == id;
					});
				matches.erase(it);
				this->serv->adoptDog(id);
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
		try
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
		catch (DogException& de)
		{
			std::cout << "\t" << de.what();
		}
		catch (RepositoryException& re)
		{
			std::cout << "\t" << re.what();
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

	this->serv->initialiseApplicationFromFile();

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
