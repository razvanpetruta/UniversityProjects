import random
import psycopg2
from faker import Faker
from constants import HOST, PORT, DATABASE, USER, PASSWORD, GENDERS, SPECIAL_CHARS


def insert_data_user_profiles():
    conn = psycopg2.connect(
        host=HOST,
        port=PORT,
        database=DATABASE,
        user=USER,
        password=PASSWORD
    )

    try:
        with open("./queries/insert_user_profiles.sql", "w", encoding="utf-8") as f:
            fake = Faker()
            with conn.cursor() as cursor:
                insert_query = "INSERT INTO user_profiles (bio, birth_date, gender, location, marital_status) VALUES "
                values = []
                for i in range(1000):
                    bio = fake.paragraph(nb_sentences=1)
                    year = random.randint(1940, 2010)
                    month = random.randint(1, 12)
                    day = random.randint(1, 28)
                    date = f"{year}-{'{:02d}'.format(month)}-{'{:02d}'.format(day)}"
                    gender = random.choice(GENDERS)
                    location = fake.city()
                    marital_status = fake.random_element(
                        elements=("single", "married", "divorced", "widowed", "separated"))
                    values.append(f"('{bio}', '{date}', '{gender}', '{location}', '{marital_status}')")
                    if len(values) == 100:
                        f.write(insert_query + ", ".join(values) + ";\n")
                        values = []
    except Exception as error:
        print(error)
    finally:
        if conn:
            cursor.close()
            conn.close()
