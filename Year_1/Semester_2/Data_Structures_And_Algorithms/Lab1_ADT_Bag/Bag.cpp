#include "Bag.h"
#include "BagIterator.h"
#include <exception>
#include <iostream>
using namespace std;


void Bag::resizeElementsArray()
{
	this->elementsCapacity *= 2;
	TElem* copy = new TElem[this->elementsCapacity];

	for (int i = 0; i < this->elementsSize; i++)
	{
		copy[i] = this->elements[i];
	}

	delete[] this->elements;

	this->elements = copy;
}
// Theta(elementsSize)

void Bag::resizePositionsArray()
{
	this->positionsCapacity *= 2;
	TElem* copy = new TElem[this->positionsCapacity];

	for (int i = 0; i < this->positionsSize; i++)
	{
		copy[i] = this->positions[i];
	}

	delete[] this->positions;

	this->positions = copy;
}
// Theta(positionsSize)

int Bag::getIndex(TElem elem) const
{
	int foundPos = -1, index = 0;
	while (foundPos == -1 && index < this->elementsSize)
	{
		if (this->elements[index] == elem)
		{
			foundPos = index;
		}
		index++;
	}

	return foundPos;
}
// Best Case: Theta(1) | Worst Case: Theta(elementsSize) -> Total Complexity: O(elementsSize)

Bag::Bag() 
{
	this->elementsCapacity = 1;
	this->elementsSize = 0;
	this->elements = new TElem[this->elementsCapacity];

	this->positionsCapacity = 1;
	this->positionsSize = 0;
	this->positions = new TElem[this->positionsCapacity];
}
// Theta(1)


void Bag::add(TElem elem) 
{
	// get the position of the element if it exists already
	int foundPos = this->getIndex(elem);

	// case 1: the element already exists
	if (foundPos != -1)
	{
		if (this->positionsSize == this->positionsCapacity)
		{
			this->resizePositionsArray();
		}
		this->positions[this->positionsSize] = foundPos;
		this->positionsSize++;
	}
	else
	// case 2: the element is new
	{
		if (this->elementsSize == this->elementsCapacity)
		{
			this->resizeElementsArray();
		}
		this->elements[this->elementsSize] = elem;
		this->elementsSize++;

		if (this->positionsSize == this->positionsCapacity)
		{
			this->resizePositionsArray();
		}
		this->positions[this->positionsSize] = this->elementsSize - 1;
		this->positionsSize++;
	}
}
// Best Case: Theta(1) | Worst Case: Theta(2 * elementsSize + positionsSize) -> Total Complexity: O(2 * elementsSize + positionsSize)


bool Bag::remove(TElem elem) 
{
	int foundPos = this->getIndex(elem);

	// case 1: the element is not in the bag
	if (foundPos == -1)
	{
		return false;
	}
	else
	// case 2: the element is in the bag
	{
		int lastPosition = -1, nrOfOccurrences = 0;
		// find the number of occurrences and the last position
		for (int index = 0; index < this->positionsSize; index++)
		{
			if (this->positions[index] == foundPos)
			{
				lastPosition = index;
				nrOfOccurrences++;
			}
		}

		// case 2.1: the element occur only once
		if (nrOfOccurrences == 1)
		{
			// move the last element on the position of the deleted one
			this->elements[foundPos] = this->elements[this->elementsSize - 1];
			this->elements[this->elementsSize - 1] = NULL_TELEM;
			this->elementsSize--;

			// update the positions of the moved last element
			for (int index = 0; index < this->positionsSize; index++)
			{
				if (this->positions[index] == this->elementsSize)
				{
					this->positions[index] = foundPos;
				}
			}

			// move the occurrence of last element in the positions array on the removed occurrence
			this->positions[lastPosition] = this->positions[this->positionsSize - 1];
			this->positions[this->positionsSize - 1] = NULL_TELEM;
			this->positionsSize--;
		}
		else
		// remove just one occurrence
		{
			this->positions[lastPosition] = this->positions[this->positionsSize - 1];
			this->positions[this->positionsSize - 1] = NULL_TELEM;
			this->positionsSize--;
		}
	}

	return true;
}
// Best Case: Theta(1) | Worst Case: Theta(2 * positionsSize + elementsSize) -> Total Complexity: O(2 * positionsSize + elementsSize)


bool Bag::search(TElem elem) const 
{
	for (int i = 0; i < this->elementsSize; i++)
	{
		if (this->elements[i] == elem)
		{
			return true;
		}
	}

	return false; 
}
// Best Case: Theta(1) | Worst Case: Theta(elementsSize) -> Total Complexity: O(elementsSize)

int Bag::nrOccurrences(TElem elem) const 
{
	// check if the element is in the bag
	int foundPos = this->getIndex(elem);

	// case 1: the element is not in bag
	if (foundPos == -1)
	{
		return 0;
	}
	else
	// case 2: the element is in the bag
	{
		int counter = 0;
		for (int index = 0; index < this->positionsSize; index++)
		{
			if (this->positions[index] == foundPos)
			{
				counter++;
			}
		}
		
		return counter;
	}
}
// Best Case: Theta(elementsSize) | Worst Case: Theta(elementsSize + positionsSize) -> Total Complexity: O(elementsSize + positionsSize)


int Bag::size() const 
{
	return this->positionsSize;
}
// Theta(1)


bool Bag::isEmpty() const 
{
	return this->elementsSize == 0;
}
// Theta(1)

BagIterator Bag::iterator()
{
	return BagIterator(*this);
}
// Theta(1)


Bag::~Bag() 
{
	delete[] this->elements;
	delete[] this->positions;
}
// Theta(1)