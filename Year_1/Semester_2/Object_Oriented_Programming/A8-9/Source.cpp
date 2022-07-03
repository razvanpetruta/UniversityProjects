#include <cassert>
#include "Repository.h"
#include "Service.h"
#include "UserInterface.h"
#include "Tests.h"
#include <iostream>

int main()
{
	Tests::testAll();

	{
		std::string adoptionListTypeFile, dogsRepoType;
		std::cout << "How do you want to work with your app? [memory / database]\n";
		std::cout << ">>> ";
		std::cin >> dogsRepoType;

		Repository* dogsRepo;
		if (dogsRepoType == "memory")
			dogsRepo = new Repository;
		else
			dogsRepo = new RepositorySQL;

		Repository* userAdoptionList;
		std::cout << "How do you want to save the adoption list? [csv / html]\n";
		std::cout << ">>> ";
		std::cin >> adoptionListTypeFile;
		if (adoptionListTypeFile == "csv")
			userAdoptionList = new RepositoryCSV;
		else
			userAdoptionList = new RepositoryHTML;

		Service* serv = new Service{ dogsRepo, userAdoptionList };
		UI* console = new UI{ serv };
		console->run();
	}

	_CrtDumpMemoryLeaks();

	return 0;
}