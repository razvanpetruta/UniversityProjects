#pragma once
#include "Dog.h"

template <typename T> class Comparator
{
	public:
		virtual bool compare(T first, T second) = 0;
};

class ComparatorAscendingByName : public Comparator<Dog>
{
	bool compare(Dog first, Dog second) override;
};

class ComparatorAscendingByAge : public Comparator<Dog>
{
	bool compare(Dog first, Dog second) override;
};