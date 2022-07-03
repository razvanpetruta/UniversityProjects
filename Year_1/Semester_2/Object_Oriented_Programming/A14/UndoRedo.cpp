#include "UndoRedo.h"

UndoRedoAdd::UndoRedoAdd(const Dog& d, Repository* repository) : addedDog{ d }, repository{ repository } {}

void UndoRedoAdd::undo()
{
	this->repository->remove(this->addedDog.getId());
}

void UndoRedoAdd::redo()
{
	this->repository->add(this->addedDog);
}

UndoRedoRemove::UndoRedoRemove(const Dog& d, Repository* repository) : removedDog{ d }, repository{ repository } {}

void UndoRedoRemove::undo()
{
	this->repository->add(this->removedDog);
}

void UndoRedoRemove::redo()
{
	this->repository->remove(this->removedDog.getId());
}

UndoRedoUpdate::UndoRedoUpdate(const Dog& oldDog, const Dog& newDog, Repository* repository) :
	oldDog{ oldDog },
	newDog{ newDog },
	repository{ repository }
{}

void UndoRedoUpdate::undo()
{
	this->repository->updateAge(this->newDog.getId(), this->oldDog.getAge());
	this->repository->updateName(this->newDog.getId(), this->oldDog.getName());
	this->repository->updateBreed(this->newDog.getId(), this->oldDog.getBreed());
	this->repository->updatePhotograph(this->newDog.getId(), this->oldDog.getPhotograph());
}

void UndoRedoUpdate::redo()
{
	this->repository->updateAge(this->oldDog.getId(), this->newDog.getAge());
	this->repository->updateName(this->oldDog.getId(), this->newDog.getName());
	this->repository->updateBreed(this->oldDog.getId(), this->newDog.getBreed());
	this->repository->updatePhotograph(this->oldDog.getId(), this->newDog.getPhotograph());
}
