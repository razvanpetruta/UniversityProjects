#include <iostream>
#include <assert.h>
#include "ShortTest.h"
#include "ExtendedTest.h"
#include "SortedIteratedList.h"

using namespace std;

bool condition1(TComp e)
{
    return e > 3;
}

bool condition2(TComp e)
{
	return e % 2 == 0;
}

bool condition3(TComp e)
{
	return e < 0;
}

bool relationCustom(TComp e1, TComp e2) 
{
	if (e1 <= e2) {
		return true;
	}
	else {
		return false;
	}
}

void testNewFunctionality()
{
	SortedIteratedList list = SortedIteratedList(relationCustom);
	assert(list.size() == 0);
	list.add(1);
	list.add(2);
	list.add(3);
	list.add(4);
	list.add(5);
	list.add(6);
	list.add(7);
	assert(list.size() == 7);
	list.filter(condition1);
	assert(list.size() == 4);
	list.filter(condition2);
	assert(list.size() == 2);
	list.filter(condition3);
	assert(list.size() == 0);
	cout << "New Functionality passed!\n";
}

int main(){
    testAll();
    testAllExtended();
	testNewFunctionality();
    std::cout<<"Finished IL Tests!"<<std::endl;
	// system("pause");
}