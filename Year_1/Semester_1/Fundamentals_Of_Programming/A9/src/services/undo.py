from dataclasses import dataclass


class UndoException(Exception):
    pass


@dataclass
class UndoOperation:
    target_object: object
    handler: object
    args: tuple


class UndoManager:
    __undo_operations = []

    @staticmethod
    def register_operation(target_object, handler, *args):
        UndoManager.__undo_operations.append(UndoOperation(target_object, handler, *args))

    @staticmethod
    def undo():
        if len(UndoManager.__undo_operations) == 0:
            raise UndoException("Cannot undo")
        undo_operation = UndoManager.__undo_operations.pop()
        undo_operation.handler(undo_operation.target_object, *undo_operation.args)

    @staticmethod
    def delete_last_register():
        UndoManager.__undo_operations.pop()

    @staticmethod
    def empty_for_testing():
        UndoManager.__undo_operations.clear()
