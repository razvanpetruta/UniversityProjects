#pragma once
#include "DynamicVector.h"
#include "Repository.h"

typedef struct
{
	Vector** undoList;
	int undoSize, undoCapacity;
	Vector** redoList;
	int redoSize, redoCapacity;
} UndoRedo;

UndoRedo* createUndoRedo();

void destroyUndoRedo(UndoRedo* ur);

void addStateToUndo(UndoRedo* ur, Vector* state);

int undo(UndoRedo* ur, Repository* repo);

int redo(UndoRedo* ur, Repository* repo);

void clearRedoList(UndoRedo* ur);