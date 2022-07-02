#include "SortedBag.h"
#include "SortedBagIterator.h"
#include <iostream>
#include "ShortTest.h"
#include "ExtendedTest.h"
#include <assert.h>

using namespace std;

bool relationExtra(TComp e1, TComp e2) 
{
	return e1 <= e2;
}

void testPreviousIterator()
{
	SortedBag sb(relationExtra);
	sb.add(2);
	sb.add(1);
	sb.add(3);

	SortedBagIterator it = sb.iterator();
	assert(it.valid() == true);

	try
	{
		it.previous();
		assert(it.valid() == false);
		it.previous();
		assert(false);
	}
	catch (...)
	{
		assert(true);
	}

	it.first();
	it.next();
	it.next();
	
	assert(it.getCurrent() == 3);
	it.previous();
	assert(it.getCurrent() == 2);
	it.next();
	assert(it.getCurrent() == 3);
	it.previous();
	it.previous();
	assert(it.getCurrent() == 1);

	cout << "Extra functionality passed\n";
}

int main() {
	testAll();
	testAllExtended();
	testPreviousIterator();
	
	cout << "Test over" << endl;
	//system("pause");
}
