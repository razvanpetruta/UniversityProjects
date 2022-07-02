#include "SortedBagIterator.h"
#include "SortedBag.h"
#include <exception>

using namespace std;

SortedBagIterator::SortedBagIterator(const SortedBag& b) : bag(b) 
{
	BSTNode* node = this->bag.root;

	while (node != nullptr)
	{
		this->stack.push(node);
		node = node->left;
	}

	// this currentNode will point to the left most element
	if (!this->stack.empty())
		this->currentNode = this->stack.top();
	else
		this->currentNode = nullptr;
}
// ?(n)

TComp SortedBagIterator::getCurrent() 
{
	if (!this->valid())
		throw exception();

	return this->currentNode->info;
}
// ?(1)

bool SortedBagIterator::valid() 
{
	return this->currentNode != nullptr;
}
// ?(1)

void SortedBagIterator::next() 
{
	if (!this->valid())
		throw exception();

	BSTNode* node = this->stack.top();
	this->stackPrevious.push(node);
	this->stack.pop();

	if (node->right != nullptr)
	{
		node = node->right;

		while (node != nullptr)
		{
			this->stack.push(node);
			node = node->left;
		}
	}

	if (!this->stack.empty())
		this->currentNode = this->stack.top();
	else
		this->currentNode = nullptr;
}
// ?(n)

void SortedBagIterator::previous()
{
	if (!this->valid())
		throw exception();

	if (this->stackPrevious.empty())
	{
		this->currentNode = nullptr;
		return;
	}

	BSTNode* node = this->stackPrevious.top();
	this->stackPrevious.pop();
	while (this->stack.top() != node)
		this->stack.pop();
	this->stack.push(node);
	this->currentNode = node;
}
// it does not work properly

void SortedBagIterator::first() 
{
	BSTNode* node = this->bag.root;
	while (!this->stack.empty())
		this->stack.pop();

	while (node != nullptr)
	{
		this->stack.push(node);
		node = node->left;
	}

	// this currentNode will point to the left most element
	if (!this->stack.empty())
		this->currentNode = this->stack.top();
	else
		this->currentNode = nullptr;
}
// ?(n)

Stack::Stack() : nrElements{ 0 }, capacity{ 10 } 
{
	this->elements = new BSTNode * [capacity];
}
// ?(1)

int Stack::getNrElements() const 
{
	return this->nrElements;
}
// ?(1)

int Stack::getCapacity() const 
{
	return this->capacity;
}
// ?(1)

void Stack::push(BSTNode* element) 
{
	if (this->nrElements == this->capacity)
		this->resize();
	this->elements[this->nrElements] = element;
	this->nrElements++;
}
// ?(1)

void Stack::resize() 
{
	auto** newElements = new BSTNode * [this->capacity * 2];
	int index;
	for (index = 0; index < this->nrElements; ++index) {
		newElements[index] = this->elements[index];
	}
	this->capacity = this->capacity * 2;
	delete[] this->elements;
	this->elements = newElements;
}
// ?(n)

BSTNode* Stack::top() 
{
	return this->elements[this->nrElements - 1];
}
// ?(1)

void Stack::pop()
{
	this->nrElements--;
}
// ?(1)

bool Stack::empty()
{
	return this->nrElements == 0;
}
// ?(1)

TElem Stack::operator[](int position) const 
{
	return this->elements[position]->info;
}
// ?(1)

Stack::~Stack() 
{
	delete[] this->elements;
}
// ?(1)