import random
import psycopg2
from faker import Faker
from constants import HOST, PORT, DATABASE, USER, PASSWORD


def insert_data_users():
    conn = psycopg2.connect(
        host=HOST,
        port=PORT,
        database=DATABASE,
        user=USER,
        password=PASSWORD
    )

    try:
        with open("./queries/insert_users.sql", "w", encoding="utf-8") as f:
            fake = Faker()

            usernames = set()
            password = "$2a$10$RhfHEL4/JO57rXBzTBg/Ou4SkvkPpRra/kFzMzAKFWw5rZxIYSTza"
            while len(usernames) < 1000:
                username = fake.user_name()
                usernames.add(username)
            usernames = list(usernames)

            with conn.cursor() as cursor:
                insert_query = "INSERT INTO users (username, password, user_profile_id) VALUES "
                values = []
                for i in range(1000):
                    values.append(f"('{usernames[i]}', '{password}', {i + 1})")
                    if len(values) == 100:
                        f.write(insert_query + ", ".join(values) + ";\n")
                        values = []
    except Exception as error:
        print(error)
    finally:
        if conn:
            cursor.close()
            conn.close()
