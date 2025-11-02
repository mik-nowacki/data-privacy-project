from sqlalchemy import create_engine, MetaData, select
import csv
from statistics import median
import uuid
from sqlalchemy import Table, Column, String


K = 3

QI_ATTRIBUTES = ["age", "gender", "postal_code"]  # quasi-identifiers


def load_data(CONN, MED_TABLE):
    rows = CONN.execute(select(MED_TABLE)).mappings().all()
    data = []
    for row in rows:
        data.append({
            "id": row["id"],
            "age": int(row["age"]),
            "gender": row["gender"],
            "postal_code": row["postal_code"],
            "diagnosis": row["diagnosis"]
        })
    return data


def anonymize_partition(partition, k):
    if len(partition) < 2 * k:
        return [partition]  # can't split further safely

    # Choose dimension to split
    dim = choose_dimension(partition)

    # Find split value (median)
    values = [record[dim] for record in partition]
    if dim == "age":
        split_val = median(values)
    elif dim == "postal_code":
        split_val = median([int(v[:3]) for v in values])  # use first digits
    else:
        # For categorical (like gender)
        return [partition]

    # Split data into two halves
    lhs = [r for r in partition if get_value(r, dim) <= split_val]
    rhs = [r for r in partition if get_value(r, dim) > split_val]

    if len(lhs) < k or len(rhs) < k:
        return [partition]  # stop splitting if not enough per side

    return anonymize_partition(lhs, k) + anonymize_partition(rhs, k)


def get_value(record, dim):
    if dim == "postal_code":
        return int(record["postal_code"][:3])
    return record[dim]


def choose_dimension(partition):
    # Choose attribute with largest range
    ranges = {}
    for attr in QI_ATTRIBUTES:
        values = [get_value(r, attr) for r in partition]
        if all(isinstance(v, (int, float)) for v in values):
            ranges[attr] = max(values) - min(values)
        else:
            ranges[attr] = 0
    return max(ranges, key=ranges.get)


def generalize_group(group):
    # Compute generalized value for each QI
    ages = [r["age"] for r in group]
    postal_codes = [r["postal_code"] for r in group]
    genders = [r["gender"] for r in group]

    age_range = f"{min(ages)}-{max(ages)}"
    postal_prefix = common_prefix(postal_codes)
    gender_gen = genders[0] if len(set(genders)) == 1 else "*"

    for r in group:
        r["age"] = age_range
        r["postal_code"] = postal_prefix + "*" * (len(r["postal_code"]) - len(postal_prefix))
        r["gender"] = gender_gen
    return group


def common_prefix(strings):
    if not strings:
        return ""
    prefix = strings[0]
    for s in strings[1:]:
        while not s.startswith(prefix):
            prefix = prefix[:-1]
            if not prefix:
                break
    return prefix


def main():
    engine = create_engine("postgresql://postgres:password@localhost:5432/pds_proj_1")
    CONN = engine.connect()
    META = MetaData()
    META.reflect(bind=engine)

    MED_TABLE = META.tables["med_data"]

    if "med_data_k_anonymous" not in META.tables:
        MED_DATA_KANON = Table(
            "med_data_k_anonymous", META,
            Column("id", String, primary_key=True),
            Column("age", String),
            Column("gender", String),
            Column("postal_code", String),
            Column("diagnosis", String)
        )
        META.create_all(engine)
    else:
        MED_DATA_KANON = META.tables["med_data_k_anonymous"]
        
    data = load_data(CONN, MED_TABLE)

    partitions = anonymize_partition(data, K)
    generalized_data = []
    for group in partitions:
        generalized_data += generalize_group(group)

    CONN.execute(MED_DATA_KANON.delete())  # clears table

    for row in generalized_data:
        rand_id = str(uuid.uuid4())
        ins = MED_DATA_KANON.insert().values(
            id=rand_id,
            age=row["age"],
            gender=row["gender"],
            postal_code=row["postal_code"],
            diagnosis=row["diagnosis"]
        )
        CONN.execute(ins)
    CONN.commit()


if __name__ == "__main__":
    main()
