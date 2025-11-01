from sqlalchemy import create_engine, MetaData, select

import csv
from random import randint
from datetime import timedelta

import uuid

def random_datetime(begin, end):
    delta = end-begin
    random_seconds = randint(1, int(delta.total_seconds()))
    return begin+timedelta(seconds=random_seconds)

def main():
    engine = create_engine("postgresql://postgresql:password@localhost:5432/pds_proj_1")
    CONN = engine.connect()
    META_DATA = MetaData()
    META_DATA.reflect(bind=engine)
    MED_TABLE = META_DATA.tables["med_data"]
    MED_DATA_DEIDENTIFIED = META_DATA.tables["med_data_deidentified"]

    rows = CONN.execute(select(MED_TABLE)).mappings().all()

    for line in rows:
        # randomize id
        rand_id = str(uuid.uuid4())
        
        # generalize age
        age = int(line["age"])
        range_low = int(age/5) * 5
        range_high = 4 + range_low
        age_range = f'{range_low} - {range_high}'

        # suppress postal code by "*"
        supp_range = 2
        post_code_supped = list(line["postal_code"])
        post_code_supped[-supp_range:] = ["*"]*supp_range
        post_code_supped = ''.join(post_code_supped)

        row = MED_DATA_DEIDENTIFIED.insert().values(id=rand_id, 
                                        age=age_range,
                                        gender=line["gender"], 
                                        postal_code=post_code_supped, 
                                        diagnosis=line["diagnosis"])
        CONN.execute(row)
    CONN.commit()

if __name__ == '__main__':
    main()
