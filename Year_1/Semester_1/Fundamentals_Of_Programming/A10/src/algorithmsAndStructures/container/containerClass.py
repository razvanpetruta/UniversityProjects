class Container:
    def __init__(self):
        self.__elements = {}

    def __setitem__(self, key, value):
        self.__elements[key] = value

    def __getitem__(self, item):
        if item not in self.__elements:
            raise IndexError("Value does not exist")
        return self.__elements[item]

    def __iter__(self):
        return iter(self.__elements)

    def __delitem__(self, key):
        if key not in self.__elements:
            raise IndexError("Value does not exist")
        return self.__elements.pop(key)

    def __len__(self):
        return len(self.__elements)
