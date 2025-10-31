from sqlalchemy import create_engine, MetaData, Table, Column, Numeric, Integer, VARCHAR, text
from sqlalchemy.orm import scoped_session, sessionmaker

import csv
from random import randint, choice
from datetime import datetime, timedelta

def random_datetime(begin, end):
    delta = end-begin
    random_seconds = randint(1, int(delta.total_seconds()))
    return begin+timedelta(seconds=random_seconds)

def main():
    engine = create_engine("postgresql://postgres:password@localhost:5432/pds_proj_1")
    CONN = engine.connect()
    META_DATA = MetaData()
    META_DATA.reflect(bind=engine)
    MED_TABLE = META_DATA.tables["med_data"]
    WORK_TABLE = META_DATA.tables["work_data"]

    with open("./Data/med.csv", 'r') as med_file:
        med_csv = csv.reader(med_file)

        for line in med_csv:
            # generalize id???
            
            # generalize age
            age = int(line[2])
            range_low = int(age/5) * 5
            range_high = 4 + range_low
            age_range = f'{range_low} - {range_high}'

            # suppress postal code by "*"
            supp_range = 2
            post_code_supped = list(line[6])
            post_code_supped[-supp_range:] = ["*"]*supp_range
            post_code_supped = ''.join(post_code_supped)

            row = MED_TABLE.insert().values(id=line[0], age=age_range,
                gender=line[5], postal_code=post_code_supped, diagnosis=line[7])
            CONN.execute(row)
        CONN.commit()


    # with open("./Data/work.csv", 'r') as work_file:
    #     work_csv = csv.reader(work_file)

    #     for line in work_csv:
    #         row = WORK_TABLE.insert().values(id=line[0], f_name=line[1],
    #             l_name=line[2], postal_code=line[3], gender=line[4],
    #             education=line[5], workplace=line[6], department=line[7])
    #         CONN.execute(row)
    #     CONN.commit()

if __name__ == '__main__':
    main()
