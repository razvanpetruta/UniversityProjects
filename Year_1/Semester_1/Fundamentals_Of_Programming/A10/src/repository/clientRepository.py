from domain.clientClass import Client
from repository.repoException import RepositoryException

from src.algorithmsAndStructures.container.containerClass import Container
from src.algorithmsAndStructures.filterAlgorithm.genericFilter import GenericFilter


class ClientRepository:
    def __init__(self, validator_class):
        """
        This repository is used for managing the clients. We will store the clients as dictionaries,
        key: client_object, in order to find the clients by their id.
        :param validator_class: the validator class used to validate the clients
        """
        self.__validator = validator_class
        self.__clients = Container()

    def find_by_id(self, client_id):
        """
        Look of in the clients for the one with the id = client_id.
        :param client_id: the id we are looking for
        :return: the object or None (if we did not find the id)
        """
        if client_id in self.__clients:
            return self.__clients[client_id]
        return None

    def find_by_name(self, info):
        """
        Search clients by name from a given info.
        :param info: the info containing the name.
        :return: the list of matchings
        """

        def custom_filter(client):
            return info in client.name.lower()

        if info == "":
            raise RepositoryException("Please provide relevant information")
        matchings = GenericFilter(self.list_of_clients, custom_filter).filter()
        return matchings

    def save(self, client):
        """
        Save a client to the repository, if it doesn't already exists.
        :param client: the client object
        """
        self.__validator.validate(client)
        if self.find_by_id(client.client_id) is not None:
            raise RepositoryException(f"duplicate id {client.client_id}")
        self.__clients[client.client_id] = client

    def delete_by_id(self, client_id):
        """
        Delete a client by its id.
        :param client_id:
        """
        if self.find_by_id(client_id) is None:
            raise RepositoryException("The id of the client does not exist")
        deleted_client = self.find_by_id(client_id)
        del self.__clients[client_id]
        return deleted_client

    def update_by_id(self, client_id, new_name):
        """
        Update a client information by its id.
        :param client_id: the client id
        :param new_name: the new name
        """
        victim = self.find_by_id(client_id)
        if victim is None:
            raise RepositoryException("The id of the client does not exist")
        old_name = victim.name
        victim.name = new_name
        return Client(victim.client_id, old_name)

    @property
    def clients(self):
        return self.__clients

    def __len__(self):
        return len(self.__clients)

    @property
    def list_of_clients(self):
        clients_list = []
        for key in self.__clients:
            clients_list.append(self.__clients[key])
        return clients_list
