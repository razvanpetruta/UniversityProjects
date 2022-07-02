from domain.clientClass import Client
from domain.validators import LibraryException
from services.generators import Generator
from services.handlers import UndoHandlers
from services.undo import UndoManager

from src.algorithmsAndStructures.sortingAlgorithm.sorting import Sorting


class ClientServiceException(LibraryException):
    pass


class ClientService:
    def __init__(self, repo):
        """
        The functions used to modify the clients repository.
        :param repo: the repository of clients
        """
        self.__repo = repo

    def search_by_id(self, client_id):
        """
        Search a client by its id.
        :param client_id: the id we are looking for
        :return: the client object
        """
        client = self.__repo.find_by_id(client_id)
        if client is None:
            raise ClientServiceException(f"the client id: {client_id} does not exist")
        return client

    def search_by_name(self, name):
        """
        Search a client by its name.
        :param name: the name
        :return: a list of matchings
        """
        def custom_sort(client1, client2):
            return client1.name.lower().index(name) <= client2.name.lower().index(name)

        matchings = self.__repo.find_by_name(name)
        if len(matchings) == 0:
            raise ClientServiceException("No matchings found")
        Sorting.sort(matchings, custom_sort)
        return matchings

    def add(self, client_id, name):
        """
        Create a client object and add it to the repository.
        :param client_id: the client id
        :param name: the client name
        """
        new_client = Client(client_id, name)
        self.__repo.save(new_client)
        UndoManager.register_operation(self, UndoHandlers.ADD_CLIENT, (new_client.client_id, ))

    def update(self, client_id, name):
        """
        Update a client's information by its id.
        :param client_id: the id that needs to be modified
        :param name: the new name
        """
        old_stage = self.__repo.update_by_id(client_id, name)
        UndoManager.register_operation(self, UndoHandlers.UPDATE_CLIENT, (old_stage.client_id, old_stage.name))

    def remove(self, client_id):
        """
        Remove a client from the repository by its id.
        :param client_id: the id that needs to be removed
        """
        deleted_client = self.__repo.delete_by_id(client_id)
        UndoManager.register_operation(self, UndoHandlers.REMOVE_CLIENT, (deleted_client.client_id,
                                                                          deleted_client.name))

    def list_of_clients(self):
        """
        :return: a list of client objects
        """
        return self.__repo.list_of_clients

    def populate(self):
        """
        Populate the repository with 5 randomly generated entries, using Generator class.
        """
        while len(self.__repo) != 5:
            client_id = Generator.generate_random_id()
            while client_id in self.__repo.clients:
                client_id = Generator.generate_random_id()
            name = Generator.generate_random_name()
            self.__repo.save(Client(client_id, name))
