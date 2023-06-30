import random
import psycopg2
from faker import Faker
from constants import HOST, PORT, DATABASE, USER, PASSWORD


def insert_data_books():
    conn = psycopg2.connect(
        host=HOST,
        port=PORT,
        database=DATABASE,
        user=USER,
        password=PASSWORD
    )

    try:
        with open("./queries/insert_books.sql", "w", encoding="utf-8") as f:
            fake = Faker()
            with conn.cursor() as cursor:
                cursor.execute("SELECT ID from libraries")
                library_ids = [el[0] for el in cursor.fetchall()]

                cursor.execute("SELECT ID from users")
                user_ids = [el[0] for el in cursor.fetchall()]

                insert_query = "INSERT INTO books (title, author, publisher, price, published_year, description, library_id, user_id) VALUES "
                values = []
                for i in range(100000):
                    title = fake.sentence(nb_words=random.randint(2, 5), variable_nb_words=True,
                                          ext_word_list=None).strip(".")
                    author = fake.name()
                    publisher = fake.company()
                    description = fake.paragraph(nb_sentences=5)
                    while len(description.split()) < 100:
                        description += " " + fake.paragraph(nb_sentences=5)
                    description = "".join(c for c in description if c not in ["'", ",", "-", "/"]).lower()
                    price = round(random.uniform(10, 100), 2)
                    published_year = random.randint(1850, 2022)
                    library_id = random.choice(library_ids)
                    user_id = random.choice(user_ids)
                    values.append(f"('{title}', '{author}', '{publisher}', {price}, {published_year}, '{description}', {library_id}, {user_id})")
                    if len(values) == 1000:
                        f.write(insert_query + ", ".join(values) + ";\n")
                        values = []
    except Exception as error:
        print(error)
    finally:
        if conn:
            cursor.close()
            conn.close()
