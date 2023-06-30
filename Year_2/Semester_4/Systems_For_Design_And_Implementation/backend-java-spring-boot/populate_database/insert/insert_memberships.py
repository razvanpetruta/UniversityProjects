import random
import psycopg2
from constants import HOST, PORT, DATABASE, USER, PASSWORD


def insert_data_memberships():
    conn = psycopg2.connect(
        host=HOST,
        port=PORT,
        database=DATABASE,
        user=USER,
        password=PASSWORD
    )

    try:
        with open("./queries/insert_memberships.sql", "w", encoding="utf-8") as f:
            with conn.cursor() as cursor:
                cursor.execute("SELECT ID from libraries")
                library_ids = [el[0] for el in cursor.fetchall()]
                cursor.execute("SELECT ID from readers")
                reader_ids = [el[0] for el in cursor.fetchall()]
                pairs = set()
                insert_query = "INSERT INTO memberships (library_id, reader_id, start_date, end_date) VALUES "
                values = []
                for i in range(500000):
                    library_id = random.choice(library_ids)
                    reader_id = random.choice(reader_ids)
                    while (library_id, reader_id) in pairs:
                        library_id = random.choice(library_ids)
                        reader_id = random.choice(reader_ids)
                    pairs.add((library_id, reader_id))
                    year = random.randint(2010, 2012)
                    month = random.randint(1, 12)
                    day = random.randint(1, 28)
                    start_date = f"{year}-{'{:02d}'.format(month)}-{'{:02d}'.format(day)}"
                    year = random.randint(2013, 2015)
                    month = random.randint(1, 12)
                    day = random.randint(1, 28)
                    end_date = f"{year}-{'{:02d}'.format(month)}-{'{:02d}'.format(day)}"
                    values.append(f"({library_id}, {reader_id}, '{start_date}', '{end_date}')")
                    if len(values) == 1000:
                        f.write(insert_query + ", ".join(values) + ";\n")
                        values = []
    except Exception as error:
        print(error)
    finally:
        if conn:
            cursor.close()
            conn.close()
