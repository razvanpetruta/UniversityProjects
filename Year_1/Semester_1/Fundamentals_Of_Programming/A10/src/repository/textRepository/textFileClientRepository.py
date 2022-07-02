from repository.clientRepository import ClientRepository
from repository.dataAccessEntity import ClientDataAccess


class TextFileClientRepository(ClientRepository):
    """
    Class for client repository specific to a text file.
    """
    def __init__(self, validator_class, file_path):
        super().__init__(validator_class)
        self.__file_path = file_path
        self.__load_content()

    def __load_content(self):
        with open(self.__file_path) as file_pointer:
            for line in file_pointer:
                client = ClientDataAccess.read_from_text_file(line)
                super().save(client)

    def __upload_content(self):
        with open(self.__file_path, "w") as file_pointer:
            ClientDataAccess.write_in_text_file(self.clients, file_pointer)

    def save(self, client):
        super().save(client)
        self.__upload_content()

    def delete_by_id(self, client_id):
        deleted_client = super().delete_by_id(client_id)
        self.__upload_content()
        return deleted_client

    def update_by_id(self, client_id, new_name):
        updated_client = super().update_by_id(client_id, new_name)
        self.__upload_content()
        return updated_client
