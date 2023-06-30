import random
import psycopg2
from faker import Faker
from constants import HOST, PORT, DATABASE, USER, PASSWORD


def insert_data_user_roles():
    conn = psycopg2.connect(
        host=HOST,
        port=PORT,
        database=DATABASE,
        user=USER,
        password=PASSWORD
    )

    try:
        with open("./queries/insert_user_roles.sql", "w", encoding="utf-8") as f:
            fake = Faker()

            with conn.cursor() as cursor:
                insert_query = "INSERT INTO user_roles (user_id, role_id) VALUES "
                values = []
                for i in range(1000):
                    values.append(f"({i + 1}, {1})")
                    if len(values) == 100:
                        f.write(insert_query + ", ".join(values) + ";\n")
                        values = []
    except Exception as error:
        print(error)
    finally:
        if conn:
            cursor.close()
            conn.close()
