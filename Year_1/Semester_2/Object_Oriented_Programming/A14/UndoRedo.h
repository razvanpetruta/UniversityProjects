#pragma once
#include "Dog.h"
#include "Repository.h"

class UndoRedoAction
{
	public:
		virtual void undo() = 0;
		virtual void redo() = 0;
		virtual ~UndoRedoAction() = default;
};

class UndoRedoAdd : public UndoRedoAction
{
	private:
		Dog addedDog;
		Repository* repository;

	public:
		UndoRedoAdd(const Dog& d, Repository* repository);

		void undo() override;

		void redo() override;

		~UndoRedoAdd() override = default;
};

class UndoRedoRemove : public UndoRedoAction
{
	private:
		Dog removedDog;
		Repository* repository;

	public:
		UndoRedoRemove(const Dog& d, Repository* repository);

		void undo() override;

		void redo() override;

		~UndoRedoRemove() override = default;
};

class UndoRedoUpdate : public UndoRedoAction
{
	private:
		Dog oldDog;
		Dog newDog;
		Repository* repository;

	public:
		UndoRedoUpdate(const Dog& oldDog, const Dog& newDog, Repository* repository);

		void undo() override;

		void redo() override;

		~UndoRedoUpdate() override = default;
};