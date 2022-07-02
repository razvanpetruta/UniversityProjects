#include "ListIterator.h"
#include "SortedIteratedList.h"
#include <exception>

using namespace std;

ListIterator::ListIterator(const SortedIteratedList& list) : list(list){
	this->current = this->list.head;
}
// ?(1)

void ListIterator::first(){
	this->current = this->list.head;
}
// ?(1)

void ListIterator::next(){
	if (this->current == -1)
		throw exception();

	this->current = this->list.next[current];
}
// ?(1)

bool ListIterator::valid() const{
	return this->current != -1;
}
// ?(1)

TComp ListIterator::getCurrent() const{
	if (!this->valid())
		throw exception();

	return this->list.elements[current];
}
// ?(1)