#pragma once
#include "Estate.h"

typedef Estate TElem;

typedef struct
{
	TElem** data;
	int size, capacity;
} Vector;


/*
	Create a Vector instance.
	return value: Vector pointer
*/
Vector* createVector(int capacity);


/*
	Deallocate the space for a given Vector instance.
*/
void destroyVector(Vector* v);


/*
	Resize the Vector instance in case we don't have enough space.
*/
void resize(Vector* v);


/*
	Add an estate to the Vector instance.
*/
void addToVector(Vector* v, TElem* el);


/*
	Remove an element from a given position from the Vector instance.
	pos: int
*/
void removeFromVector(Vector* v, int pos);


/*
	Make a copy of a Vector instance.
*/
Vector* makeCopy(Vector* v);


void sortVector(Vector* result, int (*comparator)(Estate*, Estate*));


/*
	Tests for Vector structure.
*/
void testVector();