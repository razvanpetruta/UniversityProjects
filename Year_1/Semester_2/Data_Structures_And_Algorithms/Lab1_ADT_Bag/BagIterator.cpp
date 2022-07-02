#include <exception>
#include "BagIterator.h"
#include "Bag.h"

using namespace std;


BagIterator::BagIterator(Bag& c): bag(c)
{
	this->current = 0;
}
// Theta(1)


void BagIterator::first() 
{
	this->current = 0;
}
// Theta(1)


void BagIterator::next() 
{
	if (!this->valid())
	{
		throw exception();
	}
	
	this->current++;
}
// Theta(1)


bool BagIterator::valid() const 
{
	if (this->current < this->bag.positionsSize)
	{
		return true;
	}

	return false;
}
// Theta(1)

TElem BagIterator::remove()
{
	if (!this->valid())
		throw exception();

	TElem deletedElem = this->getCurrent();
	this->bag.remove(deletedElem);
	this->next();

	return deletedElem;
}
// Best Case: Theta(1) | Worst Case: Theta(2 * positionsSize + elementsSize) -> Total Complexity: O(2 * positionsSize + elementsSize)


TElem BagIterator::getCurrent() const
{
	if (!this->valid())
	{
		throw exception();
	}
	
	return this->bag.elements[this->bag.positions[this->current]];
}
// Theta(1)
