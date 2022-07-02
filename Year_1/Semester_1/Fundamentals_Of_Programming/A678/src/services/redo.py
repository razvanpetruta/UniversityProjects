from dataclasses import dataclass


class RedoException(Exception):
    pass


@dataclass
class RedoOperation:
    target_object: object
    handler: object
    args: tuple


class RedoManager:
    __redo_operations = []

    @staticmethod
    def register_operation(target_object, handler, *args):
        RedoManager.__redo_operations.append(RedoOperation(target_object, handler, *args))

    @staticmethod
    def redo():
        if len(RedoManager.__redo_operations) == 0:
            raise RedoException("Cannot redo")
        redo_operation = RedoManager.__redo_operations.pop()
        redo_operation.handler(redo_operation.target_object, *redo_operation.args)

    @staticmethod
    def empty_for_testing():
        RedoManager.__redo_operations.clear()

    @staticmethod
    def empty_history():
        RedoManager.__redo_operations.clear()
