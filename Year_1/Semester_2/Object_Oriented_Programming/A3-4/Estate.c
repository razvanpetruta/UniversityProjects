#include "Estate.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>

Estate* createEstate(char* type, char* address, int price, int surface)
{
	Estate* e = (Estate*)malloc(sizeof(Estate));

	if (e == NULL)
		return NULL;

	e->type = (char*)malloc((strlen(type) + 1) * sizeof(char));
	if (e->type != NULL)
		strcpy(e->type, type);

	e->address = (char*)malloc((strlen(address) + 1) * sizeof(char));
	if (e->address != NULL)
		strcpy(e->address, address);

	e->price = price;

	e->surface = surface;

	return e;
}

void destroyEstate(Estate* e)
{
	if (e == NULL)
		return;

	free(e->type);
	free(e->address);

	free(e);
}

char* getType(Estate* e)
{
	if (e == NULL)
		return NULL;
	return e->type;
}

char* getAddress(Estate* e)
{
	if (e == NULL)
		return NULL;
	return e->address;
}

void toString(Estate* e, char* representation)
{
	if (e == NULL)
		return;
	sprintf(representation, "Estate info: type - %s, address - %s, price - %d, surface - %d", e->type, e->address, e->price, e->surface);
}

// TESTS
void testEstate()
{
	Estate* e = createEstate("house", "110A", 100, 240);

	assert(strcmp(e->type, "house") == 0);
	assert(strcmp(e->address, "110A") == 0);
	assert(e->price == 100);
	assert(e->surface == 240);

	destroyEstate(e);
}