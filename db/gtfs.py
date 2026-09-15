from pathlib import Path
import pandas as pd

# https://gtfs.org/documentation/schedule/reference/#stop_timestxt

INPUT_DIR = Path("GTFS_Realtime")
OUTPUT_DIR = Path("output")
AGENCY_ID = "1"  # Dublin Bus

COLUMNS_TO_DROP = {
    "routes": ["route_desc", "route_url", "route_color", "route_text_color", "route_type"],
    "stops": ["stop_desc", "zone_id", "stop_url", "location_type", "parent_station"],
    "times": ["stop_headsign"], 
}

FILE_ORDER = [
    (1, "agency", "agency"),
    (2, "calendar", "calendar"),
    (3, "dates", "calendar_dates"),
    (4, "shapes", "shapes"),
    (5, "shape_points", None),
    (6, "routes", "routes"),
    (7, "trips", "trips"),
    (8, "stops", "stops"),
    (9, "times", "stop_times"),
]

def load_feed(input_dir):
    feed = {}
    for _, key, filename in FILE_ORDER:
        if filename is None:
            continue
        feed[key] = pd.read_csv(input_dir / f"{filename}.txt")
    return feed


def review_df(df, name=""):
    print(name)
    df.info()
    print(df.head(20))
    print(df.isnull().sum())
    print(df[df.isnull().any(axis=1)]) #Print all records with a null value


def clean_routes(routes_df, agency_id):
    routes_df = routes_df.drop(columns=COLUMNS_TO_DROP["routes"], errors="ignore")
    return routes_df[
        routes_df["agency_id"] == agency_id
    ].copy()


def clean_trips(trips_df, routes_df):
    return trips_df[
        trips_df["route_id"].isin(routes_df["route_id"])
    ].copy()


def clean_calendar(calendar_df, trips_df):
    calendar_df = calendar_df[
        calendar_df["service_id"].isin(trips_df["service_id"])
    ].copy()

    for column in calendar_df.columns[1:8]:
        calendar_df[column] = calendar_df[column].astype(bool)

    return calendar_df


def clean_calendar_dates(dates_df, trips_df):
    return dates_df[
        dates_df["service_id"].isin(trips_df["service_id"])
    ].copy()


def clean_stop_times(times_df, trips_df):
    times_df = times_df.drop(columns=COLUMNS_TO_DROP["times"], errors="ignore")
    return times_df[
        times_df["trip_id"].isin(trips_df["trip_id"])
    ].copy()


def clean_stops(stops_df, times_df):
    stops_df = stops_df.drop(columns=COLUMNS_TO_DROP["stops"], errors="ignore")
    return stops_df[
        stops_df["stop_id"].isin(times_df["stop_id"])
    ].copy()


def clean_shapes(shapes_df, trips_df):
    shape_points_df = shapes_df[
        shapes_df["shape_id"].isin(trips_df["shape_id"])
    ].copy()
    shapes_df = shape_points_df[["shape_id"]].drop_duplicates().reset_index(drop=True)
    return shapes_df, shape_points_df




def clean_feed(feed, agency_id=AGENCY_ID):
    routes = clean_routes(feed["routes"], agency_id)
    trips = clean_trips(feed["trips"], routes)
    calendar = clean_calendar(feed["calendar"], trips)
    calendar_dates = clean_calendar_dates(feed["calendar_dates"], trips)
    stop_times = clean_stop_times(feed["stop_times"], trips)
    stops = clean_stops(feed["stops"], stop_times)
    shapes, shape_points = clean_shapes(feed["shapes"], trips)

    routes.info()
    trips.info()
    calendar.info()
    calendar_dates.info()
    stop_times.info()
    stops.info()
    shapes.info()
    shape_points.info()

    return {
        "agency": feed["agency"],
        "routes": routes,
        "trips": trips,
        "calendar": calendar,
        "dates": calendar_dates,
        "stops": stops,
        "times": stop_times,
        "shapes": shapes,
        "shape_points": shape_points,
    }


def write_cleaned_files(cleaned, output_dir):
    output_dir.mkdir(parents=True, exist_ok=True)
    for number, name in FILE_ORDER:
        df = cleaned[name]
        filename = f"{number:02d}_{name}_cleaned.txt"
        df.to_csv(output_dir / filename, sep=",", index=False)


def main():
    feed = load_feed(INPUT_DIR)
    cleaned = clean_feed(feed)
    write_cleaned_files(cleaned, OUTPUT_DIR)


if __name__ == "__main__":
    main()