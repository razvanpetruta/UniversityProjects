class Client:
    def __init__(self, client_id, name):
        """
        Initialise a client object.
        :param client_id: the client id, it's unique
        :param name: the client name
        """
        self.__client_id = client_id
        self.__name = name

    @property
    def client_id(self):
        return self.__client_id

    @client_id.setter
    def client_id(self, value):
        self.__client_id = value

    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, value):
        self.__name = value

    def __str__(self):
        return f"\tid: {self.client_id}, name: {self.name}"
