#include <iostream>
#include "MultiMap.h"
#include "ExtendedTest.h"
#include "ShortTest.h"
#include "MultiMapIterator.h"
#include <cassert>

using namespace std;

void testAddIfNotPresent()
{
	MultiMap m1;

	m1.add(1, 1);
	m1.add(1, 2);
	m1.add(1, 3);
	m1.add(1, 4);
	m1.add(2, 5);
	m1.add(3, 6);
	m1.add(4, 7);
	m1.add(4, 8);

	assert(m1.size() == 8);

	MultiMap m2;
	m2.add(1, 100);

	int addedCount = m2.addIfNotPresent(m1);

	assert(addedCount == 4);
	assert(m2.size() == 5);

	addedCount = m2.addIfNotPresent(m1);
	
	assert(addedCount == 0);
	assert(m2.size() == 5);

	m1.add(5, 11);

	addedCount = m2.addIfNotPresent(m1);
	assert(addedCount == 1);
	assert(m2.size() == 6);

	cout << "New requirement passed\n";
}

int main() 
{
	{
		testAddIfNotPresent();
		testAll();
		testAllExtended();
		cout << "End" << endl;
	}
	_CrtDumpMemoryLeaks;
	// system("pause");
}
