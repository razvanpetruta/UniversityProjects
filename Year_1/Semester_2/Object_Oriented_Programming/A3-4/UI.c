#include "UI.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "UndoRedo.h"

UI* createUI(Service* serv)
{
	UI* ui = (UI*)malloc(sizeof(UI));

	if (ui == NULL)
		return NULL;

	ui->serv = serv;

	return ui;
}

void destroyUI(UI* ui)
{
	if (ui == NULL)
		return;

	destroyService(ui->serv);

	free(ui);
}

void printMenu()
{
	printf("\n\n***************************************************************\n");
	printf("*  1. add an estate.                                          *\n");
	printf("*  2. delete an estate by address.                            *\n");
	printf("*  3. update an estate by address.                            *\n");
	printf("*  4. filter estates by address.                              *\n");
	printf("*  5. filter estates by type, having a minimum surface.       *\n");
	printf("*  6. filter estates having a maximum price.                  *\n");
	printf("*  7. undo.                                                   *\n");
	printf("*  8. redo.                                                   *\n");
	printf("*  0. exit                                                    *\n");
	printf("***************************************************************\n\n");
}

/*
	Read the command from console.
*/
int readCommand()
{
	int command, scanfResult;
	printf("\tcommand: ");
	scanfResult = scanf("%d", &command);
	return command;
}

/*
	Read the information about an Estate for adding it to the application.
*/
int addEstateFromUser(UI* ui, UndoRedo* ur)
{
	char type[50], address[50];
	int price = 0, surface = 0, scanfResult, res;

	fgetc(stdin); // clear the buffer

	printf("\tpossible types: house, apartment or penthouse\n");
	printf("\ttype: ");
	fgets(type, 49, stdin);
	type[strlen(type) - 1] = '\0';

	while (strcmp(type, "house") != 0 && strcmp(type, "apartment") != 0 && strcmp(type, "penthouse") != 0)
	{
		printf("\tpossible types: house, apartment or penthouse\n");
		printf("\ttype: ");
		fgets(type, 49, stdin);
		type[strlen(type) - 1] = '\0';
	}

	printf("\taddress: ");
	fgets(address, 49, stdin);
	address[strlen(address) - 1] = '\0';

	printf("\tprice: ");
	scanfResult = scanf("%d", &price);

	printf("\tsurface: ");
	scanfResult = scanf("%d", &surface);

	Vector* copy = makeCopy(ui->serv->repo->v);

	res = addEstate(ui->serv, type, address, price, surface);

	if (res == 1)
	{
		addStateToUndo(ur, copy);
		clearRedoList(ur);
	}
	else
	{
		destroyVector(copy);
	}

	return res;
}

/*
	Read the necessary information for deleting an Estate from the application.
*/
int deleteEstateFromUser(UI* ui, UndoRedo* ur)
{
	char address[50];
	int res;

	printf("\taddress: ");
	fgetc(stdin); // clear the buffer
	fgets(address, 49, stdin);
	address[strlen(address) - 1] = '\0';

	Vector* copy = makeCopy(ui->serv->repo->v);

	res = deleteEstate(ui->serv, address);

	if (res == 1)
	{
		addStateToUndo(ur, copy);
		clearRedoList(ur);
	}
	else
	{
		destroyVector(copy);
	}

	return res;
}

/*
	Read the information for updating an Estate from the application.
*/
int updateEstateFromUser(UI* ui, UndoRedo* ur)
{
	char address[50], newType[50];
	int newPrice, newSurface, scanfResult, res;

	printf("\taddress: ");
	fgetc(stdin); // clear the buffer
	fgets(address, 49, stdin);
	address[strlen(address) - 1] = '\0';

	printf("\tpossible types: house, apartment or penthouse\n");
	printf("\ttype: ");
	fgets(newType, 49, stdin);
	newType[strlen(newType) - 1] = '\0';

	while (strcmp(newType, "house") != 0 && strcmp(newType, "apartment") != 0 && strcmp(newType, "penthouse") != 0)
	{
		printf("\tpossible types: house, apartment or penthouse\n");
		printf("\ttype: ");
		fgets(newType, 49, stdin);
		newType[strlen(newType) - 1] = '\0';
	}

	printf("\tnew price: ");
	scanfResult = scanf("%d", &newPrice);

	printf("\tnew surface: ");
	scanfResult = scanf("%d", &newSurface);

	Vector* copy = makeCopy(ui->serv->repo->v);

	res = updateEstate(ui->serv, address, newType, newPrice, newSurface);

	if (res == 1)
	{
		addStateToUndo(ur, copy);
		clearRedoList(ur);
	}
	else
	{
		destroyVector(copy);
	}

	return res;
}

/*
	Print the elements of a Vector instance.
*/
int printVector(Vector* result)
{
	printf("\n\n");
	int i;
	for (i = 0; i < result->size; i++)
	{
		char representation[200];
		Estate* e = (Estate*)malloc(sizeof(Estate));
		if (e == NULL)
			return 0;

		e = result->data[i];
		toString(e, representation);
		if (e != NULL)
			printf("%s\n", representation);
		else
			printf("no estate found...\n");
	}

	return 1;
}

typedef int (*customSort)(Estate* e1, Estate* e2);

int sortingAscendingByPrice(Estate* e1, Estate* e2)
{
	if (e1->price <= e2->price)
		return 1;

	return 0;
}

int sortingDesscendingByPrice(Estate* e1, Estate* e2)
{
	if (e1->price >= e2->price)
		return 1;

	return 0;
}

/*
	Filter the application for finding the Estates with a specific address.
*/
int filterEstatesByAddress(UI* ui)
{
	printf("\t1. Sort ascending by price.\n");
	printf("\t2. Sort desscending by price.\n");

	char filter[50];
	int filtered, printed, sorting, scanfResult;

	printf("\tsorting option: ");
	scanfResult = scanf("%d", &sorting);

	while (sorting != 1 && sorting != 2)
	{
		printf("\tsorting option: ");
		scanfResult = scanf("%d", &sorting);
	}

	printf("\tfilter: ");
	fgetc(stdin); // clear the buffer
	fgets(filter, 49, stdin);
	filter[strlen(filter) - 1] = '\0';

	Vector* result = createVector(getLength(ui->serv->repo));
	if (result == NULL)
		return 0;

	filtered = filterByAddress(ui->serv, filter, result);
	if (result->size == 0 || filtered == 0)
		return 0;

	customSort comparator;
	if (sorting == 1)
	{
		comparator = &sortingAscendingByPrice;
		sortVector(result, comparator);
	}
	else
	{
		comparator = &sortingDesscendingByPrice;
		sortVector(result, comparator);
	}

	printed = printVector(result);
	if (printed == 0)
		return 0;

	destroyVector(result);

	return 1;
}

/*
	Search for the Estates that have a certain type and a minimum surface.
*/
int searchByTypeAndSurface(UI* ui)
{
	if (ui == NULL)
		return 0;

	char type[50];
	int surface, scanfResult, searched, printed;

	fgetc(stdin); // clear the buffer

	printf("\tpossible types: house, apartment or penthouse\n");
	printf("\ttype: ");
	fgets(type, 49, stdin);
	type[strlen(type) - 1] = '\0';

	while (strcmp(type, "house") != 0 && strcmp(type, "apartment") != 0 && strcmp(type, "penthouse") != 0)
	{
		printf("\tpossible types: house, apartment or penthouse\n");
		printf("\ttype: ");
		fgets(type, 49, stdin);
		type[strlen(type) - 1] = '\0';
	}

	printf("\tminimum surface: ");
	scanfResult = scanf("%d", &surface);

	Vector* result = createVector(getLength(ui->serv->repo));
	if (result == NULL)
		return 0;

	searched = searchEstateByTypeAndSurface(ui->serv, type, surface, result);
	if (result->size == 0 || searched == 0)
		return 0;

	printed = printVector(result);
	if (printed == 0)
		return 0;

	destroyVector(result);

	return 1;
}

typedef int (*customFilter)(Estate* e, int price);

int filterMaximumPrice(Estate* e, int price)
{
	if (e->price <= price)
		return 1;

	return 0;
}

int filteringMaximumPrice(UI* ui)
{
	fgetc(stdin); // clear the buffer

	int price, scanfResult, searched, printed;

	printf("\tmaximum price: ");
	scanfResult = scanf("%d", &price);

	Vector* result = createVector(getLength(ui->serv->repo));
	if (result == NULL)
		return 0;

	customFilter filter = &filterMaximumPrice;

	searched = searchEstateByPrice(ui->serv, price, filter, result);
	if (result->size == 0 || searched == 0)
		return 0;

	printed = printVector(result);
	if (printed == 0)
		return 0;

	destroyVector(result);

	return 1;
}

/*
	Start the application.
*/
void startUI(UI* ui)
{
	int command, res;
	UndoRedo* ur = createUndoRedo();

	while (1)
	{
		printMenu();
		command = readCommand();
		switch (command)
		{
			case 0:
			{
				destroyUndoRedo(ur);
				return;
			}

			case 1:
			{
				res = addEstateFromUser(ui, ur);
				if (res == 1)
					printf("Successfully added!\n");
				else
					printf("There has been an error, possible duplicates!\n");
				break;
			}

			case 2:
			{
				res = deleteEstateFromUser(ui, ur);
				if (res == 1)
					printf("Successfully deleted!\n");
				else
					printf("There has been an error, possible address not found!\n");
				break;
			}

			case 3:
			{
				res = updateEstateFromUser(ui, ur);
				if (res == 1)
					printf("Successfully updated!\n");
				else
					printf("There has been an error, possible address not found!\n");
				break;			
			}

			case 4:
			{
				res = filterEstatesByAddress(ui);
				if (res == 0)
					printf("There has been an error, possible no matches!\n");
				break;
			}

			case 5:
			{
				res = searchByTypeAndSurface(ui);
				if (res == 0)
					printf("There has been an error, possible not matches!\n");
				break;
			}

			case 6:
			{
				res = filteringMaximumPrice(ui);
				if (res == 0)
					printf("There has been an error, possible not matches!\n");
				break;
			}

			case 7:
			{
				res = undo(ur, ui->serv->repo);
				if (res == 0)
					printf("Cannot undo\n");
				else
					printf("Undo performed\n");
				break;
			}

			case 8:
			{
				res = redo(ur, ui->serv->repo);
				if (res == 0)
					printf("Cannot redo\n");
				else
					printf("Redo performed\n");
				break;
			}

			default:
			{
				printf("There was an error...\n");
				break;
			}
		}
	}
}
