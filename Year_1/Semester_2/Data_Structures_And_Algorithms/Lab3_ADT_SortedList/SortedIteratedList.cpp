#include "ListIterator.h"
#include "SortedIteratedList.h"
#include <iostream>
using namespace std;
#include <exception>

void SortedIteratedList::internalSearch(TComp el, int& previousNode, int& currentNode) const
{;
	previousNode = -1;
	currentNode = this->head;

	bool flag = false;
	while (currentNode != -1 && this->relation(this->elements[currentNode], el))
	{
		if (this->elements[currentNode] == el)
		{
			flag = true;
			break;
		}

		previousNode = currentNode;
		currentNode = this->next[currentNode];
	}

	if (!flag)
	{
		currentNode = -1;
	}
}
// Best Case: θ(1) | Worst Case: θ(n) | Total Complexity: O(n)
// ... n - the number of nodes

void SortedIteratedList::freePosition(int pos)
{
	this->next[pos] = this->firstEmpty;
	this->firstEmpty = pos;
}
// θ(1)

void SortedIteratedList::resize()
{
	this->capacity = this->capacity * 2;

	TComp* newElements = new TComp[this->capacity];
	TComp* newNext = new int[this->capacity];

	for (int i = 0; i < this->length; i++)
	{
		newElements[i] = this->elements[i];
		newNext[i] = this->next[i];
	}

	delete[] this->elements;
	delete[] this->next;

	this->elements = newElements;
	this->next = newNext;

	for (int i = this->length; i < this->capacity; i++)
	{
		this->next[i] = i + 1;
	}

	this->next[this->capacity - 1] = -1;
	this->firstEmpty = this->length;
}
// θ(n) ... n - the number of elements

SortedIteratedList::SortedIteratedList(Relation r) {
	this->relation = r;
	this->capacity = 10;
	this->head = -1;

	this->elements = new TComp[this->capacity];
	this->next = new int[this->capacity];

	for (int i = 0; i < this->capacity; i++)
	{
		this->next[i] = i + 1;
	}

	this->next[this->capacity - 1] = -1;
	this->firstEmpty = 0;
	this->length = 0;
}
// θ(n) ... n - capacity

int SortedIteratedList::size() const {
	return this->length;
}
// θ(1)

bool SortedIteratedList::isEmpty() const {
	return this->head == -1;
}
// θ(1)

ListIterator SortedIteratedList::first() const {
	return ListIterator(*this);
}
// θ(1)

TComp SortedIteratedList::getElement(ListIterator pos) const {
	if (!pos.valid())
		throw exception();

	return pos.getCurrent();
}
// θ(1)

TComp SortedIteratedList::remove(ListIterator& pos) {
	int position = pos.current;

	int current = this->head;
	int previous = -1;

	while (current != -1 && current != position)
	{
		previous = current;
		current = this->next[current];
	}

	if (current == -1)
	{
		throw exception();
	}

	TComp removedValue = this->elements[position];

	if (position == this->head)
	{
		this->head = this->next[this->head];
	}
	else
	{
		this->next[previous] = this->next[current];
	}

	pos.current = this->next[position];
	this->freePosition(position);
	this->length--;

	return removedValue;
}
// Best Case: θ(1) | Worst Case: θ(n) | Total Complexity: O(n)
// ... n - the number of nodes

ListIterator SortedIteratedList::search(TComp e) const{
	int previous = 0, current = 0;
	this->internalSearch(e, previous, current);

	ListIterator it = ListIterator(*this);
	it.current = current;
	return it;
}
// Best Case: θ(1) | Worst Case: θ(n) | Total Complexity: O(n)
// ... n - the number of nodes

void SortedIteratedList::add(TComp e) {
	if (this->length == this->capacity)
		this->resize();

	int previous = 0, current = 0;
	this->internalSearch(e, previous, current);

	// insert first
	if (previous == -1)
	{
		int position = this->firstEmpty;
		this->firstEmpty = this->next[this->firstEmpty];

		this->elements[position] = e;
		this->next[position] = this->head;
		this->head = position;
	}
	else
	{
		int position = this->firstEmpty;
		this->firstEmpty = this->next[this->firstEmpty];

		this->elements[position] = e;
		this->next[position] = this->next[previous];
		this->next[previous] = position;
	}

	this->length++;
}
// Best Case: θ(1) | Worst Case: θ(n) | Total Complexity: O(n)
// ... n - the number of nodes

SortedIteratedList::~SortedIteratedList() {
	delete[] this->elements;
	delete[] this->next;
}
// θ(1)

void SortedIteratedList::print() {
	int current = this->head;

	while (current != -1)
	{
		cout << this->elements[current] << " ";
		current = this->next[current];
	}

	cout << "\n";
}
// θ(n) ... n - the number of nodes

void SortedIteratedList::filter(Condition cond)
{
	ListIterator it = this->first();
	while (it.valid())
	{
		if (!cond(it.getCurrent()))
			this->remove(it);
		else
			it.next();
	}
}