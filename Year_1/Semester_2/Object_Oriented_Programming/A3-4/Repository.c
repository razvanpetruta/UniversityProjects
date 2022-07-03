#include "Repository.h"
#include "DynamicVector.h"
#include <stdlib.h>
#include <string.h>
#include <assert.h>

Repository* createRepository(int capacity)
{
	Repository* repo = (Repository*)malloc(sizeof(Repository));
	if (repo == NULL)
		return NULL;

	repo->v = createVector(capacity);

	return repo;
}

void destroyRepository(Repository* repo)
{
	if (repo == NULL)
		return;

	destroyVector(repo->v);

	free(repo);
}

int findAddress(Repository* repo, char* address)
{
	int i;
	for (i = 0; i < repo->v->size; i++)
	{
		if (strcmp(repo->v->data[i]->address, address) == 0)
			return i;
	}

	return -1;
}

int addToRepository(Repository* repo, Estate* e)
{
	if (repo == NULL)
		return 0;

	if (e == NULL)
		return 0;

	if (e->address == NULL)
		return 0;

	if (findAddress(repo, e->address) != -1)
		return 0;

	addToVector(repo->v, e);

	return 1;
}

int getLength(Repository* repo)
{
	return repo->v->size;
}

Estate* getEstateOnPos(Repository* repo, int i)
{
	if (repo == NULL)
		return NULL;

	if (i < 0 || i > repo->v->size)
		return NULL;

	return repo->v->data[i];
}

int deleteFromRepository(Repository* repo, char* address)
{
	if (repo == NULL || address == NULL)
		return 0;

	int victimPos = findAddress(repo, address);

	if (victimPos == -1)
		return 0;

	removeFromVector(repo->v, victimPos);

	return 1;
}

int updateFromRepository(Repository* repo, char* address, char* newType, int newPrice, int newSurface)
{
	if (repo == NULL || address == NULL)
		return 0;

	int victimPos = findAddress(repo, address);
	if (victimPos == -1)
		return 0;

	char* aux = realloc(repo->v->data[victimPos]->type, sizeof(char) * (strlen(newType) + 1));
	if (aux == NULL)
		return 0;

	repo->v->data[victimPos]->type = aux;

	strcpy(repo->v->data[victimPos]->type, newType);
	repo->v->data[victimPos]->price = newPrice;
	repo->v->data[victimPos]->surface = newSurface;

	return 1;
}


// TESTS
void testAddToRepository()
{
	Repository* repo = createRepository(10);
	assert(getLength(repo) == 0);

	Estate* e = createEstate("house", "100A", 100, 64);
	assert(addToRepository(repo, e) == 1);
	assert(getLength(repo) == 1);

	assert(addToRepository(repo, e) == 0);

	destroyRepository(repo);
}

void testDeleteFromRepository()
{
	Repository* repo = createRepository(10);

	Estate* e = createEstate("house", "100A", 100, 64);
	assert(addToRepository(repo, e) == 1);
	assert(getLength(repo) == 1);

	assert(deleteFromRepository(repo, "100A") == 1);
	assert(getLength(repo) == 0);

	assert(deleteFromRepository(repo, "100A") == 0);

	destroyRepository(repo);
}

void testUpdateFromRepository()
{
	Repository* repo = createRepository(10);

	Estate* e = createEstate("house", "100A", 100, 64);
	assert(addToRepository(repo, e) == 1);
	assert(getLength(repo) == 1);

	assert(updateFromRepository(repo, "100A", "penthouse", 100, 77) == 1);
	assert(e->surface == 77);

	assert(updateFromRepository(repo, "100B", "penthouse", 100, 77) == 0);

	destroyRepository(repo);
}

void testRepository()
{
	testAddToRepository();
	testDeleteFromRepository();
	testUpdateFromRepository();
}
