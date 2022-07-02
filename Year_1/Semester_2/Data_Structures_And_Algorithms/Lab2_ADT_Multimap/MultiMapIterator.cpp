#include "MultiMapIterator.h"
#include "MultiMap.h"


MultiMapIterator::MultiMapIterator(const MultiMap& c): col(c) {
	this->currentKey = this->col.head;
	if (this->currentKey != NULL)
	{
		this->currentValue = this->currentKey->headValues;
	}
	else
	{
		this->currentValue = NULL;
	}
}
// ?(1)

TElem MultiMapIterator::getCurrent() const{
	if (!this->valid())
	{
		throw exception();
	}

	return std::make_pair(this->currentKey->info, this->currentValue->info);
}
// ?(1)

bool MultiMapIterator::valid() const {
	return this->currentKey != NULL;
}
// ?(1)

void MultiMapIterator::next() {
	if (!this->valid())
	{
		throw exception();
	}

	if (this->currentValue->next == NULL)
	{
		this->currentKey = this->currentKey->next;
		if (this->currentKey != NULL)
		{
			this->currentValue = this->currentKey->headValues;
		}
	}
	else
	{
		this->currentValue = this->currentValue->next;
	}
}
// ?(1)

void MultiMapIterator::first() {
	this->currentKey = this->col.head;
	if (this->currentKey != NULL)
	{
		this->currentValue = this->currentKey->headValues;
	}
	else
	{
		this->currentValue = NULL;
	}
}
// ?(1)
