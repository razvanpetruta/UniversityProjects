class Settings:
    def __init__(self):
        self.__repository = ""
        self.__books_path = ""
        self.__clients_path = ""
        self.__rentals_path = ""
        self.__load_data()

    def __load_data(self):
        with open("configuration/settings.properties") as file_pointer:
            line = file_pointer.readline()
            tokens = line.split("=")
            self.__repository = tokens[1].strip()
            line = file_pointer.readline()
            tokens = line.split("=")
            self.__books_path = tokens[1].strip().strip('"')
            line = file_pointer.readline()
            tokens = line.split("=")
            self.__clients_path = tokens[1].strip().strip('"')
            line = file_pointer.readline()
            tokens = line.split("=")
            self.__rentals_path = tokens[1].strip().strip('"')

    @property
    def repository(self):
        return self.__repository

    @property
    def books_path(self):
        return self.__books_path

    @property
    def clients_path(self):
        return self.__clients_path

    @property
    def rentals_path(self):
        return self.__rentals_path
