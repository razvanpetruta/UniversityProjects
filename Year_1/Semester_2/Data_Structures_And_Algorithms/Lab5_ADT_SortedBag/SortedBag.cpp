#include "SortedBag.h"
#include "SortedBagIterator.h"

SortedBag::SortedBag(Relation r) 
{
	this->root = nullptr;
	this->relation = r;
	this->noOfElements = 0;
}
// ?(1)

void SortedBag::add(TComp e) 
{
	BSTNode* currentNode = this->root;
	BSTNode* parentNode = nullptr;

	while (currentNode != nullptr)
	{
		parentNode = currentNode;
		if (this->relation(e, currentNode->info))
			currentNode = currentNode->left;
		else
			currentNode = currentNode->right;
	}

	BSTNode* newNode = new BSTNode;
	newNode->info = e;
	newNode->left = nullptr;
	newNode->right = nullptr;

	if (this->root == nullptr)
	{
		this->root = newNode;
	}
	else
	{
		if (this->relation(e, parentNode->info))
			parentNode->left = newNode;
		else
			parentNode->right = newNode;
	}

	this->noOfElements++;
}
// Best Case: ?(1) | Worst Case: ?(n) - Total Complexity: O(n)

bool SortedBag::remove(TComp e) 
{
	BSTNode* currentNode = this->root;
	BSTNode* parentNode = nullptr;

	while (currentNode != nullptr)
	{
		if (currentNode->info == e)
			break;
		parentNode = currentNode;
		if (this->relation(e, currentNode->info))
			currentNode = currentNode->left;
		else
			currentNode = currentNode->right;
	}

	// the element does not exist
	if (currentNode == nullptr)
		return false;

	// the node does not have any descendents
	if (currentNode->left == nullptr && currentNode->right == nullptr)
	{
		// if it is the root
		if (currentNode == this->root)
			this->root = nullptr;
		else
		{
			// check which side to delete
			if (this->relation(e, parentNode->info))
				parentNode->left = nullptr;
			else
				parentNode->right = nullptr;
		}
		delete currentNode;
	}
	else if (currentNode->right == nullptr)	// it has only the left descendant
	{
		// if it is the root
		if (currentNode == this->root)
			this->root = this->root->left;
		else
		{
			if (this->relation(e, parentNode->info))
				parentNode->left = currentNode->left;
			else
				parentNode->right = currentNode->left;
		}
		delete currentNode;
	}
	else if (currentNode->left == nullptr) // it has only the right descendant
	{
		// if it is the root
		if (currentNode == this->root)
			this->root = this->root->right;
		else
		{
			if (this->relation(e, parentNode->info))
				parentNode->left = currentNode->right;
			else
				parentNode->right = currentNode->right;
		}
		delete currentNode;
	}
	else // both descendants - it will be replaced by the maximum of the left subtree
	{
		currentNode->left->parent = currentNode;
		BSTNode* replacementNode = this->getMaximum(currentNode->left);

		// the replacement node can have at most the left descendant
		if (replacementNode->left == nullptr)
		{
			// if it has no descendant
			currentNode->info = replacementNode->info;
			if (replacementNode->parent != currentNode)
				replacementNode->parent->right = nullptr;
			else
				currentNode->left = nullptr;
		}
		else
		{
			BSTNode* childNode = replacementNode->left;
			if (replacementNode->parent != currentNode)
				replacementNode->parent->right = childNode;
			else
				currentNode->left = childNode;
			currentNode->info = replacementNode->info;
		}
		delete replacementNode;
	}
	this->noOfElements--;
	return true;
}
// Best Case: ?(1) | Worst Case: ?(n) - Total Complexity: O(n)

bool SortedBag::search(TComp elem) const 
{
	BSTNode* currentNode = this->root;

	while (currentNode != nullptr)
	{
		if (currentNode->info == elem)
			return true;
		if (this->relation(elem, currentNode->info))
			currentNode = currentNode->left;
		else
			currentNode = currentNode->right;
	}

	return false;
}
// Best Case: ?(1) | Worst Case: ?(n) - Total Complexity: O(n)

int SortedBag::nrOccurrences(TComp elem) const 
{
	int count = 0;
	BSTNode* currentNode = this->root;

	while (currentNode != nullptr)
	{
		if (currentNode->info == elem)
			count++;
		if (this->relation(elem, currentNode->info))
			currentNode = currentNode->left;
		else
			currentNode = currentNode->right;
	}

	return count;
}
// ?(n)

int SortedBag::size() const 
{
	return this->noOfElements;
}
// ?(1)

bool SortedBag::isEmpty() const 
{
	return this->root == nullptr;
}
// ?(1)

SortedBagIterator SortedBag::iterator() const 
{
	return SortedBagIterator(*this);
}
// ?(1)

SortedBag::~SortedBag() 
{
	this->deleteBSTNode(this->root);
}
// ?(n)

void SortedBag::deleteBSTNode(BSTNode* node)
{
	if (node != nullptr)
	{
		this->deleteBSTNode(node->left);
		this->deleteBSTNode(node->right);
		delete node;
	}
}
// ?(n)

BSTNode* SortedBag::getMaximum(BSTNode* startNode)
{
	BSTNode* currentNode = startNode;
	BSTNode* parentNode = startNode->parent;

	while (currentNode->right != nullptr)
	{
		parentNode = currentNode;
		currentNode = currentNode->right;
	}

	if (parentNode != nullptr)
		currentNode->parent = parentNode;

	return currentNode;
}
// ?(n)