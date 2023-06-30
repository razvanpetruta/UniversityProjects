import random
import psycopg2
from faker import Faker
from constants import EMAIL_DOMAINS, HOST, PORT, DATABASE, USER, PASSWORD, GENDERS, SPECIAL_CHARS


def insert_data_readers():
    conn = psycopg2.connect(
        host=HOST,
        port=PORT,
        database=DATABASE,
        user=USER,
        password=PASSWORD
    )

    try:
        with open("./queries/insert_readers.sql", "w", encoding="utf-8") as f:
            fake = Faker()
            with conn.cursor() as cursor:
                cursor.execute("SELECT ID from users")
                user_ids = [el[0] for el in cursor.fetchall()]

                insert_query = "INSERT INTO readers (name, email, birth_date, gender, is_student, user_id) VALUES "
                values = []
                for i in range(100000):
                    name = fake.name()
                    name_modified = "".join(c for c in name if c not in SPECIAL_CHARS).lower()
                    email = name_modified + random.choice(EMAIL_DOMAINS)
                    year = random.randint(1940, 2010)
                    month = random.randint(1, 12)
                    day = random.randint(1, 28)
                    date = f"{year}-{'{:02d}'.format(month)}-{'{:02d}'.format(day)}"
                    gender = random.choice(GENDERS)
                    is_student = random.choice([True, False])
                    user_id = random.choice(user_ids)
                    values.append(f"('{name}', '{email}', '{date}', '{gender}', {is_student}, {user_id})")
                    if len(values) == 1000:
                        f.write(insert_query + ", ".join(values) + ";\n")
                        values = []
    except Exception as error:
        print(error)
    finally:
        if conn:
            cursor.close()
            conn.close()
