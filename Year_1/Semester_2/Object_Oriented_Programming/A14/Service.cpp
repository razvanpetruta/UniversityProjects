#include "Service.h"
#include "Exceptions.h"

Service::Service(Repository* _dogsRepo, Repository* _userAdoptionList) : dogsRepo{ _dogsRepo }, userAdoptionList{ _userAdoptionList } {}

void Service::addDog(int id, const std::string& breed, const std::string& name, int age, const std::string& photograph)
{
	Dog d{ id, breed, name, age, photograph };

	DogValidator::validate(d);

	this->dogsRepo->add(d);

	std::shared_ptr<UndoRedoAction> action = std::make_shared<UndoRedoAdd>(d, this->dogsRepo);

	this->undoAdmin.push_back(action);
	this->clearRedo();

	this->saveElementsToFile();
}

void Service::removeDog(int id)
{
	Dog d = this->dogsRepo->getElements()[this->dogsRepo->getIdIndex(id)];

	this->dogsRepo->remove(id);

	std::shared_ptr<UndoRedoAction> action = std::make_shared<UndoRedoRemove>(d, this->dogsRepo);

	this->undoAdmin.push_back(action);
	this->clearRedo();

	this->saveElementsToFile();
}

void Service::updateBreed(int id, std::string& _breed)
{
	Dog oldDog = this->dogsRepo->getElements()[this->dogsRepo->getIdIndex(id)];

	this->dogsRepo->updateBreed(id, _breed);

	Dog newDog = this->dogsRepo->getElements()[this->dogsRepo->getIdIndex(id)];

	std::shared_ptr<UndoRedoAction> action = std::make_shared<UndoRedoUpdate>(oldDog, newDog, this->dogsRepo);

	this->undoAdmin.push_back(action);
	this->clearRedo();

	this->saveElementsToFile();
}

void Service::updateName(int id, std::string& _name)
{
	Dog oldDog = this->dogsRepo->getElements()[this->dogsRepo->getIdIndex(id)];

	this->dogsRepo->updateName(id, _name);

	Dog newDog = this->dogsRepo->getElements()[this->dogsRepo->getIdIndex(id)];

	std::shared_ptr<UndoRedoAction> action = std::make_shared<UndoRedoUpdate>(oldDog, newDog, this->dogsRepo);

	this->undoAdmin.push_back(action);
	this->clearRedo();

	this->saveElementsToFile();
}

void Service::updateAge(int id, int _age)
{
	Dog oldDog = this->dogsRepo->getElements()[this->dogsRepo->getIdIndex(id)];

	this->dogsRepo->updateAge(id, _age);

	Dog newDog = this->dogsRepo->getElements()[this->dogsRepo->getIdIndex(id)];

	std::shared_ptr<UndoRedoAction> action = std::make_shared<UndoRedoUpdate>(oldDog, newDog, this->dogsRepo);

	this->undoAdmin.push_back(action);
	this->clearRedo();

	this->saveElementsToFile();
}

void Service::updatePhotograph(int id, std::string& _photograph)
{
	Dog oldDog = this->dogsRepo->getElements()[this->dogsRepo->getIdIndex(id)];

	this->dogsRepo->updatePhotograph(id, _photograph);

	Dog newDog = this->dogsRepo->getElements()[this->dogsRepo->getIdIndex(id)];

	std::shared_ptr<UndoRedoAction> action = std::make_shared<UndoRedoUpdate>(oldDog, newDog, this->dogsRepo);

	this->undoAdmin.push_back(action);
	this->clearRedo();

	this->saveElementsToFile();
}

void Service::adoptDog(int id)
{
	int dogIndex = this->dogsRepo->getIdIndex(id);

	if (dogIndex == -1)
		throw RepositoryException("Dog not found, cannot adopt it\n");

	Dog d{ this->dogsRepo->getElements()[dogIndex]};

	this->userAdoptionList->add(d);

	this->dogsRepo->remove(id);

	this->saveElementsToFile();
}

int Service::getDogsRepoSize() const
{
	return this->dogsRepo->getSize();
}

int Service::getUserAdoptionListSize() const
{
	return this->userAdoptionList->getSize();
}

std::vector<Dog> Service::getDogsRepoElements() const
{
	return this->dogsRepo->getElements();
}

std::vector<Dog> Service::getUserAdpotionElements() const
{
	return this->userAdoptionList->getElements();
}

void Service::saveElementsToFile()
{
	this->dogsRepo->saveRepositoryToFile();
}

void Service::initialiseApplicationFromFile()
{
	this->dogsRepo->initialiseRepositoryFromFile();
}

void Service::saveAdoptionList()
{
	this->userAdoptionList->saveAdoptionListAndOpen();
}

void Service::changeUserAdoptionListType(std::string type)
{
	std::vector<Dog> elements = this->getUserAdpotionElements();

	delete this->userAdoptionList;

	if (type == "csv")
	{
		Repository* repository = new RepositoryCSV{};
		for (auto d : elements)
		{
			repository->add(d);
		}
		this->userAdoptionList = repository;
	}
	else if (type == "html")
	{
		Repository* repository = new RepositoryHTML{};
		for (auto d : elements)
		{
			repository->add(d);
		}
		this->userAdoptionList = repository;
	}
}

void Service::undoLastAction()
{
	if (this->undoAdmin.empty())
	{
		std::string error;
		error += std::string("Cannot undo!");
		if (!error.empty())
			throw RepositoryException(error);
	}
	this->undoAdmin.back()->undo();
	this->redoAdmin.push_back(this->undoAdmin.back());
	this->undoAdmin.pop_back();
}

void Service::redoLastAction()
{
	if (this->redoAdmin.empty()) 
	{
		std::string error;
		error += std::string("Cannot redo!");
		if (!error.empty())
			throw RepositoryException(error);
	}
	this->redoAdmin.back()->redo();
	this->undoAdmin.push_back(this->redoAdmin.back());
	this->redoAdmin.pop_back();
}

void Service::clearRedo()
{
	this->redoAdmin.clear();
}

void Service::clearUndo()
{
	this->undoAdmin.clear();
}
