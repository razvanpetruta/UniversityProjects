import random


class Generator:
    @staticmethod
    def generate_random_id():
        """
        Generate a random id.
        :return: the random id
        """
        _id = random.randint(1, 50)
        return _id

    @staticmethod
    def generate_random_title():
        """
        Generate a random title.
        :return: the random title
        """
        first = ["Flirty", "Sultry", "Kinky", "Lusty", "Curvy", "Steamy", "Crazy", "Fleshy", "Happy"]
        second = ["Zombies", "Mummies", "Ghosts", "Demons", "Angels", "Sirens", "Dogs", "Cats", "Reapers"]
        title = ""
        title += random.choice(first)
        title += ' '
        title += random.choice(second)
        return title

    @staticmethod
    def generate_random_name():
        """
        Generate a random name.
        :return: the random name
        """
        first = ["Ana", "Martin", "John", "Aura", "Daniel", "Bob", "Arnold", "James", "Chloe", "Lucifer"]
        second = ["Morningstar", "Decker", "Smith", "Johnson", "Brown", "Miller", "Davis", "Lee", "Lopez"]
        name = ""
        name += random.choice(first)
        name += ' '
        name += random.choice(second)
        return name
