#pragma once
#include "Dog.h"

template <typename TElement>
class DynamicVector
{
	private:
		int size;
		int capacity;
		TElement* elems;

		/*
			Resize the current DynamicVector, multiplying its capacity by a given factor.
			factor: int
		*/
		void resize(int factor = 2);

	public:
		// default constructor for a DynamicVector
		DynamicVector();

		// constructor with parameters
		DynamicVector(int capacity);

		// copy constructor for a DynamicVector
		DynamicVector(const DynamicVector& v);

		// destructor
		~DynamicVector();

		/*
			Add an element to the DynamicVector.
			e: the element
		*/
		void add(TElement e);

		/*
			Remove a certain index from the DynamicVector.
			pos: the position to be deleted
		*/
		void remove(int pos);

		/*
			Overloading the subscript operator.
			Input: pos - a valid position within the vector.
			Output: a reference to the element on position pos.
		*/
		TElement& operator[](int pos);

		// assignment operator for a DynamicVector.
		DynamicVector<TElement>& operator=(const DynamicVector<TElement>& v);

		/*
			Return the number of elements from the DynamicVector.
		*/
		int getSize() const;
};

#include "DynamicVector.h"

template <typename TElement>
void DynamicVector<TElement>::resize(int factor)
{
	this->capacity *= factor;

	TElement* aux = new TElement[this->capacity];
	for (int i = 0; i < this->size; i++)
	{
		aux[i] = this->elems[i];
	}

	delete[] this->elems;
	this->elems = aux;
}

template <typename TElement>
DynamicVector<TElement>::DynamicVector()
{
	this->size = 0;
	this->capacity = 10;
	this->elems = new TElement[capacity];
}

template <typename TElement>
DynamicVector<TElement>::DynamicVector(int capacity)
{
	this->size = 0;
	this->capacity = capacity;
	this->elems = new TElement[capacity];
}

template <typename TElement>
DynamicVector<TElement>::DynamicVector(const DynamicVector<TElement>& v)
{
	this->size = v.size;
	this->capacity = v.capacity;
	this->elems = new TElement[this->capacity];
	for (int i = 0; i < this->size; i++)
	{
		this->elems[i] = v.elems[i];
	}
}

template <typename TElement>
DynamicVector<TElement>::~DynamicVector()
{
	delete[] this->elems;
}

template <typename TElement>
void DynamicVector<TElement>::add(TElement e)
{
	if (this->size == this->capacity)
	{
		this->resize();
	}

	this->elems[this->size] = e;
	this->size++;
}

template <typename TElement>
void DynamicVector<TElement>::remove(int pos)
{
	for (int i = pos; i < this->size - 1; i++)
	{
		this->elems[i] = this->elems[i + 1];
	}

	this->size--;
}

template <typename TElement>
TElement& DynamicVector<TElement>::operator[](int pos)
{
	return this->elems[pos];
}

template <typename TElement>
DynamicVector<TElement>& DynamicVector<TElement>::operator=(const DynamicVector<TElement>& v)
{
	if (this == &v)
		return *this;

	this->size = v.size;
	this->capacity = v.capacity;

	delete[] this->elems;
	this->elems = new TElement[this->capacity];
	for (int i = 0; i < this->size; i++)
		this->elems[i] = v.elems[i];

	return *this;
}

template <typename TElement>
int DynamicVector<TElement>::getSize() const
{
	return this->size;
}
