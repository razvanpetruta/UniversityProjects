#include "Bag.h"
#include "ShortTest.h"
#include "ExtendedTest.h"
#include <iostream>
#include <assert.h>
#include "BagIterator.h"

using namespace std;

void testNewFunctionality()
{
	Bag b;

	b.add(1);
	b.add(2);
	b.add(3);
	b.add(4);

	assert(b.size() == 4);

	BagIterator it = b.iterator();

	it.first();

	int deleted = it.remove();
	std::cout << deleted << '\n';
	assert(b.search(deleted) == false);

	assert(b.size() == 3);

	deleted = it.remove();
	std::cout << deleted << '\n';

	assert(b.size() == 2);
}


int main() {

	testNewFunctionality();
	cout << "Tested new functionality\n";
	testAll();
	cout << "Short tests over" << endl;
	testAllExtended();

	cout << "All test over" << endl;
}