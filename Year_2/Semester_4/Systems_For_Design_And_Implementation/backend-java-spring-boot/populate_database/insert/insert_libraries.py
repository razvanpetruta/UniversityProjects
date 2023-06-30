import random
import psycopg2
from faker import Faker
from constants import SPECIAL_CHARS, EMAIL_DOMAINS, TLDS, HOST, PORT, DATABASE, USER, PASSWORD


def insert_data_libraries():
    conn = psycopg2.connect(
        host=HOST,
        port=PORT,
        database=DATABASE,
        user=USER,
        password=PASSWORD
    )

    try:
        with open("./queries/insert_libraries.sql", "w", encoding="utf-8") as f:
            fake = Faker()
            with conn.cursor() as cursor:
                cursor.execute("SELECT ID from users")
                user_ids = [el[0] for el in cursor.fetchall()]

                insert_query = "INSERT INTO libraries (name, address, email, website, year_of_construction, user_id) VALUES "
                values = []
                for i in range(100000):
                    name = fake.company()
                    name_modified = "".join(c for c in name if c not in SPECIAL_CHARS).lower()
                    address_ = fake.country() + ", " + fake.city()
                    address = "".join(c for c in address_ if c not in ["'", "\n"])
                    email = name_modified + random.choice(EMAIL_DOMAINS)
                    website = name_modified + random.choice(TLDS)
                    year_of_construction = random.randint(1850, 2022)
                    user_id = random.choice(user_ids)
                    values.append(f"('{name}', '{address}', '{email}', '{website}', {year_of_construction}, {user_id})")
                    if len(values) == 1000:
                        f.write(insert_query + ", ".join(values) + ";\n")
                        values = []
    except Exception as error:
        print(error)
    finally:
        if conn:
            cursor.close()
            conn.close()
