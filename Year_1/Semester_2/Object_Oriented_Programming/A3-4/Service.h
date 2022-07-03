#pragma once
#include "Repository.h"

typedef struct
{
	Repository* repo;
} Service;


/*
	Create a Service instance.
	repo: Repository*
*/
Service* createService(Repository* repo);


/*
	Deallocate the space for a given Service instance.
	s: Service*
*/
void destroyService(Service* s);


/*
	Add an specific Estate to the application.
	serv: Service*
	type: char*
	price: int
	surface: int
	Return value: 1 - success, 0 - failed
*/
int addEstate(Service* serv, char* type, char* address, int price, int surface);


/*
	Return a Repository pointer to the repository of the application.
	s: Service*
*/
Repository* getRepository(Service* s);


/*
	Delete a specific Estate from the application.
	serv: Service*
	address:  char*
	Return value: 1 - success, 0 - failed
*/
int deleteEstate(Service* serv, char* address);


/*
	Update a specific Estate in the application.
	serv: Service
	address: char*
	newType: char*
	newPrice: int
	newSurface: int
	Return value: 1 - success, 0 - failed
*/
int updateEstate(Service* serv, char* address, char* newType, int newPrice, int newSurface);


/*
	Populate the application with 10 entries.
*/
void populateRepo(Service* serv);


/*
	Filter for printing, considering the address as a filter.
	serv: Service*
	filter: char*
	result: Vector*
*/
int filterByAddress(Service* serv, char* filter, Vector* result);


/*
	Filter for printing, considering the type and the surface as filter.
	serv: Service*
	type: char*
	surface: int
	result: Vector*
*/
int searchEstateByTypeAndSurface(Service* serv, char* type, int surface, Vector* result);


int searchEstateByPrice(Service* serv, int price, int (*filter)(Estate* e, int price), Vector* result);


/*
	Tests for service.
*/
void testService();