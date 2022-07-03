#pragma once
#include "Estate.h"
#include "DynamicVector.h"

typedef struct
{
	Vector* v;
} Repository;


/*
	Create a repository of a given capacity.
	capacity: int
*/
Repository* createRepository(int capacity);


/*
	Deallocate the space for a given repository.
	repo: Repository*
*/
void destroyRepository(Repository* repo);


/*
	Look for a specific address in the repository.
	repo: Repository*
	address: char*
	Return value: the position of the Estate that contains the address or -1
*/
int findAddress(Repository* repo, char* address);


/*
	Add an estate to the repository.
	repo: Repository*
	e: Estate*
	Return value: 1 - success, 0 - failed
*/
int addToRepository(Repository* repo, Estate* e);


/*
	Get the number of elements from the repository.
*/
int getLength(Repository* repo);


/*
	Get the Estate instance from the position i in the repository.
	repo: Repository*
	i: int
	Return value: corresponding Estate or NULL
*/
Estate* getEstateOnPos(Repository* repo, int i);


/*
	Delete a certain Estate from the repository using as info its address.
	repo: Repository*
	address: char* 
	Return value: 1 - success, 0 - failed
*/
int deleteFromRepository(Repository* repo, char* address);


/*
	Update the information of an Estate using its address.
	repo: Repository*
	address: char*
	newType: char*
	newPrice: int
	newSurface: int
	Return value: 1 - success, 0 - failed
*/
int updateFromRepository(Repository* repo, char* address, char* newType, int newPrice, int newSurface);


/*
	Tests for repository.
*/
void testRepository();