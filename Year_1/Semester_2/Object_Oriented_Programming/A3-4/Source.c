#include <stdio.h>
#include "UI.h"
#include <crtdbg.h>

int main()
{
	testRepository();
	testService();
	testVector();
	testEstate();

	Repository* repo = createRepository(10);
	Service* serv = createService(repo);

	populateRepo(serv);

	UI* ui = createUI(serv);

	startUI(ui);

	destroyUI(ui);

	_CrtDumpMemoryLeaks();

	return 0;
}