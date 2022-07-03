#pragma once
typedef struct
{
	char* type;
	char* address;
	int price;
	int surface;
} Estate;


/*
	Create an Estate entity.
	type: char*
	address: char*
	price: int
	surface: int
*/
Estate* createEstate(char* type, char* address, int price, int surface);


/*
	Deallocate the space for a given Estate.
	e: Estate*
*/
void destroyEstate(Estate* e);


/*
	Return the type of a real estate.
	e: Estate*
*/
char* getType(Estate* e);


/*
	Return the address of a real estate.
	e: Estate*
*/
char* getAddress(Estate* e);


/*
	Get the string representation of a real estate.
	e: Estate*
	representation: char* (in which will be stored the string representation.
*/
void toString(Estate* e, char* representation);


/*
	TESTS
*/
void testEstate();