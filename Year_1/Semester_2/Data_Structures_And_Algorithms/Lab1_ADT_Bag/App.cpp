#include "Bag.h"
#include "ShortTest.h"
#include "ExtendedTest.h"
#include <iostream>
#include <chrono>
#include <assert.h>
#include "BagIterator.h"

using namespace std::chrono;

using namespace std;

void testNewFunctionality()
{
	Bag b;

	b.add(1);
	b.add(2);
	b.add(1);
	b.add(3);

	assert(b.size() == 4);

	BagIterator it = b.iterator();

	it.first();
	assert(it.getCurrent() == 1);

	int deleted = it.remove();
	std::cout << deleted << '\n';
	assert(deleted == 1);

	assert(it.getCurrent() == 2);

	assert(b.size() == 3);

	deleted = it.remove();
	std::cout << deleted << '\n';

	assert(b.size() == 2);
}

int main() {

	auto start = high_resolution_clock::now();

	testAll();
	cout << "Short tests over" << endl;

	testAllExtended();
	cout << "All test over" << endl;

	testNewFunctionality();
	cout << "New functionality over" << endl;

	auto stop = high_resolution_clock::now();

	auto duration = duration_cast<seconds>(stop - start);

	cout << duration.count() << " seconds" << endl;
}