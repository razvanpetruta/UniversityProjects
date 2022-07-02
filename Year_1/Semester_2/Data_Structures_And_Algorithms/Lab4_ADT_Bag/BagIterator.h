#include "Bag.h"

class BagIterator
{
	//DO NOT CHANGE THIS PART
	friend class Bag;
	
private:
	Bag& bag;
	int currentIndex;

	BagIterator(Bag& c);
public:
	void first();
	void next();
	TElem getCurrent() const;
	bool valid() const;
	TElem remove();
};
