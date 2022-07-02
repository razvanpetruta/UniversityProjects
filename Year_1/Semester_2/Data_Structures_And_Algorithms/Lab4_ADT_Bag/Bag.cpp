#include "Bag.h"
#include "BagIterator.h"
#include <exception>
#include <iostream>
using namespace std;


int Bag::hash(TElem el) const
{
	if (el < 0)
		el *= -1;
	return el % this->capacity;
}
// θ(1)

void Bag::updateFirstFree()
{
	this->firstFree++;
	while (this->firstFree < this->capacity && this->hashTable[this->firstFree] != NULL_TELEM)
		this->firstFree++;
}
// Best Case: θ(1) | Worst Case: θ(capacity) -> Total complexity: O(capacity)

void Bag::rehash()
{
	TElem* oldElements = new TElem[this->noOfElements];

	int currentIndex = 0;
	for (int i = 0; i < this->capacity; i++)
	{
		if (this->hashTable[i] != NULL_TELEM)
		{
			oldElements[currentIndex++] = this->hashTable[i];
		}
	}

	this->capacity *= 2;
	TElem* newHashTable = new TElem[this->capacity];
	int* newNext = new int[this->capacity];
	for (int i = 0; i < this->capacity; i++)
	{
		newHashTable[i] = NULL_TELEM;
		newNext[i] = -1;
	}

	delete[] this->hashTable;
	delete[] this->next;

	this->hashTable = newHashTable;
	this->next = newNext;

	this->noOfElements = 0;
	this->firstFree = 0;
	for (int i = 0; i < currentIndex; i++)
	{
		this->add(oldElements[i]);
	}

	delete[] oldElements;
}
// Best Case: θ(noOfElements) | Worst Case: θ(noOfElements * depth) -> Total Complexity: O(noOfElements * depth)

Bag::Bag() 
{
	this->capacity = 10;
	this->noOfElements = 0;
	this->firstFree = 0;
	this->hashTable = new TElem[this->capacity];
	this->next = new int[this->capacity];
	for (int i = 0; i < this->capacity; i++)
	{
		this->hashTable[i] = NULL_TELEM;
		this->next[i] = -1;
	}
}
// θ(capacity)


void Bag::add(TElem elem) 
{
	// check for rehashing
	if ((1.0 * this->noOfElements) / (1.0 * this->capacity) >= this->loadFactor)
		this->rehash();

	int insertPos = this->hash(elem);	// get the position in hashtable

	if (this->hashTable[insertPos] == NULL_TELEM)  // we have an empty position
	{
		this->hashTable[insertPos] = elem;
		this->next[insertPos] = -1;
		if (this->firstFree == insertPos)	// in case we occupied the first free position
			this->updateFirstFree();
	}
	else
	{	// append to the end
		int currentPos = insertPos;
		while (this->next[currentPos] != -1)
			currentPos = this->next[currentPos];
		this->hashTable[this->firstFree] = elem;
		this->next[this->firstFree] = -1;
		this->next[currentPos] = this->firstFree;
		this->updateFirstFree();
	}

	this->noOfElements++;
}
// Best Case: θ(1) | Worst Case: θ(depth) -> Total Complexity: O(depth)


bool Bag::remove(TElem elem) 
{
	// we need the previous position in order to remove the node
	int currentPos = this->hash(elem);
	int previousPos = -1;

	// find the key to be removed and its previous
	while (currentPos != -1 && this->hashTable[currentPos] != elem)
	{
		previousPos = currentPos;
		currentPos = this->next[currentPos];
	}

	if (currentPos == -1)	// the element is not in the bag
		return false;
	else
	{
		// find another key that hashes to the currentPos
		bool over = false;
		do
		{
			int p = this->next[currentPos];
			int prevP = currentPos;

			while (p != -1 && this->hash(this->hashTable[p]) != currentPos)
			{
				prevP = p;
				p = this->next[p];
			}

			if (p == -1)
				over = true;
			else
			{
				this->hashTable[currentPos] = this->hashTable[p];
				previousPos = prevP;
				currentPos = p;
			}
		} while (over == false);

		// remove key from position currentPos	
		if (previousPos == -1)
		{
			// parse the table to check if currentPos has any previous element
			int index = 0;
			while (index < this->capacity && previousPos == -1)
			{
				if (this->next[index] == currentPos)
					previousPos = index;
				else
					index++;
			}
		}

		if (previousPos != -1)
			this->next[previousPos] = this->next[currentPos];

		this->hashTable[currentPos] = NULL_TELEM;
		this->next[currentPos] = -1;
		if (this->firstFree > currentPos)
			this->firstFree = currentPos;
	}

	this->noOfElements--;
	return true;
}
// Best Case: θ(1) | Worst Case: θ(capacity) -> Total Complexity: O(capacity)

bool Bag::search(TElem elem) const 
{
	int currentPos = this->hash(elem);
	while (currentPos != -1)
	{
		if (this->hashTable[currentPos] == elem)
			return true;
		currentPos = this->next[currentPos];
	}
	return false;
}
// Best Case: θ(1) | Worst Case: θ(depth) | Total Complexity: O(depth)

int Bag::nrOccurrences(TElem elem) const 
{
	int cnt = 0;

	int currentPos = this->hash(elem);
	while (currentPos != -1)
	{
		if (this->hashTable[currentPos] == elem)
			cnt++;
		currentPos = this->next[currentPos];
	}

	return cnt;
}
// θ(depth)

int Bag::size() const 
{
	return this->noOfElements;
}
// θ(1)

bool Bag::isEmpty() const 
{
	return this->noOfElements == 0;
}
// θ(1)

BagIterator Bag::iterator()
{
	return BagIterator(*this);
}
// θ(1)

Bag::~Bag() 
{
	delete[] this->hashTable;
	delete[] this->next;
}
// θ(1)
