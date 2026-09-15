from pathlib import Path

import psycopg2
from dotenv import load_dotenv
import os

#https://www.postgresql.org/docs/current/populate.html

OUTPUT_DIR = Path("output")

FILE_ORDER = [
    ("agency", "01_agency_cleaned.txt"),
    ("calendar", "02_calendar_cleaned.txt"),
    ("dates", "03_dates_cleaned.txt"),
    ("shapes", "04_shapes_cleaned.txt"),
    ("shape_points", "05_shape_points_cleaned.txt"),
    ("routes", "06_routes_cleaned.txt"),
    ("trips", "07_trips_cleaned.txt"),
    ("stops", "08_stops_cleaned.txt"),
    ("times", "09_times_cleaned.txt"),
]

COPY_OPTIONS = "FORMAT csv, DELIMITER ',', HEADER, ENCODING 'UTF8', QUOTE '\"', ESCAPE '\"'"

load_dotenv()

conn = psycopg2.connect(
    host=os.environ["myPGHOST"],
    port=os.environ["myPGPORT"],
    dbname=os.environ["myPGDATABASE"],
    user=os.environ["myPGUSER"],
    password=os.environ["myPGPASSWORD"],
)

try:
    with conn:
        with conn.cursor() as cur:
            for table_name, filename in FILE_ORDER:
                file_path = OUTPUT_DIR / filename
                print(f"Loading {filename}")
                with open(file_path, "r", encoding="utf-8") as f:
                    cur.copy_expert(
                        f"COPY public.{table_name} FROM STDIN WITH ({COPY_OPTIONS})",
                        f,
                    )
except Exception as exc:
    print(f"Import failed: {exc}")
finally:
    conn.close()