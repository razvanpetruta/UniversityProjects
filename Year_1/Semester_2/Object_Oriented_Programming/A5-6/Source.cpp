#include "DynamicVector.h"
#include <cassert>
#include "Repository.h"
#include "Service.h"
#include "UserInterface.h"
#include "Tests.h"

int main()
{
	Tests::testAll();

	/*{
		Repository dogsRepo;
		Repository userAdoptionList;
		Service serv{ dogsRepo, userAdoptionList };
		UI console{ serv };
		console.run();
	}

	_CrtDumpMemoryLeaks();*/

	return 0;
}