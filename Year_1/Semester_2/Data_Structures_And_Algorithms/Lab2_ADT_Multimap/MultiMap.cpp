#include "MultiMap.h"
#include "MultiMapIterator.h"
#include <exception>
#include <iostream>

using namespace std;


NodeKey* MultiMap::checkKey(TKey key) const
{
	NodeKey* current = this->head;
	NodeKey* sol = NULL;

	while (current != NULL)
	{
		if (current->info == key)
		{
			sol = current;
			return sol;
		}
		current = current->next;
	}

	return sol;
}
// Best Case: θ(1) | Worst Case: θ(n) | Total Complexity: O(n) ... n - the number of keys

void MultiMap::addNewKey(TKey k)
{
	NodeKey* node = new NodeKey;
	node->info = k;
	node->headValues = NULL;

	// insert at the beginning
	node->next = this->head;
	this->head = node;
}
// θ(1)

void MultiMap::addValueToKey(NodeKey* keyNode, TValue v)
{
	NodeValue* nodeVal = new NodeValue;
	nodeVal->info = v;
	nodeVal->next = NULL;

	if (keyNode->headValues == NULL)
	{
		keyNode->headValues = nodeVal;
		return;
	}

	// insert at the beginning
	nodeVal->next = keyNode->headValues;
	keyNode->headValues = nodeVal;
}
// θ(1)

void MultiMap::freeValues(NodeValue* head)
{
	while (head != NULL)
	{
		NodeValue* tmp = head;
		head = head->next;
		delete tmp;
	}
}
// θ(n) ... n - the number of values associated to a key

MultiMap::MultiMap() {
	this->head = NULL;
	this->length = 0;
}
// θ(1)

void MultiMap::add(TKey c, TValue v) {
	NodeKey* key = this->checkKey(c);

	// if the key is not in the multimap
	if (key == NULL)
	{
		this->addNewKey(c);
		this->addValueToKey(this->head, v);
		this->length++;
		return;
	}

	// the key is in the multimap
	this->addValueToKey(key, v);
	this->length++;
}
// Best Case: θ(1) | Worst Case: θ(n) | Total Complexity: O(n) ... n - the number of keys


bool MultiMap::remove(TKey c, TValue v) {
	NodeKey* node = this->head;
	NodeKey* prev = NULL;

	while (node != NULL && node->info != c)
	{
		prev = node;
		node = node->next;
	}

	// if the key does not exist
	if (node == NULL)
	{
		return false;
	}

	// the info is at the beginning
	if (node->headValues->info == v)
	{
		NodeValue* tmp = node->headValues;
		node->headValues = node->headValues->next;
		delete tmp;

		// check if we need to remove the key also
		if (node->headValues == NULL)
		{
			// if the node is at the beginning of the linked list
			if (prev == NULL)
			{
				NodeKey* tmp = this->head;
				this->head = this->head->next;
				delete tmp;

				this->length--;
				return true;
			}

			NodeKey* tmp = node;
			prev->next = node->next;
			delete tmp;
		}
	}
	else
	// look for the info in the linked list
	{
		NodeValue* currentNodeValue = node->headValues;

		while (currentNodeValue->next != NULL && currentNodeValue->next->info != v)
		{
			currentNodeValue = currentNodeValue->next;
		}

		// if the value to be removed is not associated to the key
		if (currentNodeValue->next == NULL)
		{
			return false;
		}

		NodeValue* tmp = currentNodeValue->next;
		currentNodeValue->next = currentNodeValue->next->next;
		delete tmp;
	}

	this->length--;
	return true;
}
// Best Case: θ(1) | Worst Case: θ(n) | Total Complexity: O(n) ... n - the number of keys

vector<TValue> MultiMap::search(TKey c) const {
	vector<TValue> sol;

	NodeKey* node = this->checkKey(c);

	if (node == NULL)
	{
		return sol;
	}

	NodeValue* currentValue = node->headValues;

	while (currentValue != NULL)
	{
		sol.push_back(currentValue->info);
		currentValue = currentValue->next;
	}

	return sol;
}
// Best Case: θ(min(n, m)) | Worst Case: θ(n + m) | Total complexity: O(n + m)
// ... n - number of keys
// ... m - number of values associated to the key

int MultiMap::size() const {
	return this->length;
}
// θ(1)


bool MultiMap::isEmpty() const {
	return this->length == 0;
}
// θ(1)

MultiMapIterator MultiMap::iterator() const {
	return MultiMapIterator(*this);
}
// θ(1)


MultiMap::~MultiMap() {
	while (this->head != NULL)
	{
		NodeKey* tmp = this->head;
		this->freeValues(this->head->headValues);
		this->head = this->head->next;
		delete tmp;
	}
}
// θ(n) ... n - the total number of elements

int MultiMap::addIfNotPresent(MultiMap& m)
{
	int addedCount = 0;

	MultiMapIterator it = m.iterator();

	while (it.valid())
	{
		std::pair<TKey, TValue> el = it.getCurrent();

		std::vector<TValue> thisValues = this->search(el.first);
		if (thisValues.size() == 0)
		{
			std::vector<TValue> mValues = m.search(el.first);
			for (int i = 0; i < mValues.size(); i++)
			{
				this->add(el.first, mValues[i]);
				addedCount++;
				it.next();
			}
		}
		else
		{
			it.next();
		}
	}

	return addedCount;
}
// θ(n) ... n - the number of elements in the multi