#include <exception>
#include "BagIterator.h"
#include "Bag.h"

using namespace std;


BagIterator::BagIterator(Bag& c): bag(c)
{
	this->currentIndex = 0;
	while (this->currentIndex < this->bag.capacity && this->bag.hashTable[this->currentIndex] == NULL_TELEM)
		this->currentIndex++;
}
// Best Case: ?(1) | Worst Case: ?(capacity) -> Total complexity: O(capacity)

void BagIterator::first() 
{
	this->currentIndex = 0;
	while (this->currentIndex < this->bag.capacity && this->bag.hashTable[this->currentIndex] == NULL_TELEM)
		this->currentIndex++;
}
// Best Case: ?(1) | Worst Case: ?(capacity) -> Total complexity: O(capacity)

void BagIterator::next() 
{
	if (!this->valid())
		throw exception();
	this->currentIndex++;
	while (this->currentIndex < this->bag.capacity && this->bag.hashTable[this->currentIndex] == NULL_TELEM)
		this->currentIndex++;
}
// Best Case: ?(1) | Worst Case: ?(capacity) -> Total complexity: O(capacity)

bool BagIterator::valid() const 
{
	return this->currentIndex < this->bag.capacity;
}
// ?(1)

TElem BagIterator::remove()
{
	if (!this->valid())
		throw exception();

	TElem deletedElem = this->getCurrent();
	this->bag.remove(deletedElem);
	this->next();

	return deletedElem;
}
// ?(1)

TElem BagIterator::getCurrent() const
{
	if (!this->valid())
		throw exception();
	return this->bag.hashTable[currentIndex];
}
// ?(1)