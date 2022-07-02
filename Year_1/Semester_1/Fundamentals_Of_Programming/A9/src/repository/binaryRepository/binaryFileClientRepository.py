from repository.clientRepository import ClientRepository
from repository.dataAccessEntity import ClientDataAccess


class BinaryFileClientRepository(ClientRepository):
    """
    Class for client repository specific to a binary file.
    """
    def __init__(self, validator_class, file_path):
        super().__init__(validator_class)
        self.__file_path = file_path
        self.__load_content()

    def __load_content(self):
        data = ClientDataAccess.read_from_binary_file(self.__file_path)
        for key in data:
            super().save(data[key])

    def save(self, client):
        super().save(client)
        ClientDataAccess.write_in_binary_file(self.__file_path, self.clients)

    def delete_by_id(self, client_id):
        deleted_client = super().delete_by_id(client_id)
        ClientDataAccess.write_in_binary_file(self.__file_path, self.clients)
        return deleted_client

    def update_by_id(self, client_id, new_name):
        updated_client = super().update_by_id(client_id, new_name)
        ClientDataAccess.write_in_binary_file(self.__file_path, self.clients)
        return updated_client
