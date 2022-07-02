#pragma once
#include "SortedBag.h"

class Stack 
{
private:
    BSTNode** elements;
    int nrElements;
    int capacity;
public:
    // constructor
    Stack();

    // get the number of the elements on the stack
    int getNrElements() const;

    // compute the capacity of the stack
    int getCapacity() const;

    // push a node on the top of the stack
    void push(BSTNode* element);

    // resize the stack
    void resize();

    // get the element on the top of the stack
    BSTNode* top();

    // delete the top of the stack
    void pop();

    // check if the stack is empty
    bool empty();

    // [] override
    TElem operator[](int position) const;

    // destructor
    ~Stack();
};

class SortedBag;

class SortedBagIterator
{
	friend class SortedBag;

private:
	const SortedBag& bag;
	SortedBagIterator(const SortedBag& b);

	BSTNode* currentNode;
	Stack stack{};
    Stack stackPrevious{};

public:
	TComp getCurrent();
	bool valid();
	void next();
    void previous();
	void first();
};