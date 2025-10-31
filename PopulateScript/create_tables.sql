CREATE TABLE med_data (
    id SERIAL PRIMARY KEY,
    -- name VARCHAR(50),
    age varchar(20),
    -- address VARCHAR(100),
    -- email VARCHAR(50),
    gender VARCHAR(20),
    postal_code VARCHAR(20),
    diagnosis VARCHAR(20)
);

CREATE TABLE work_data (
    id SERIAL PRIMARY KEY,
    f_name VARCHAR(50),
    l_name VARCHAR(50),
    postal_code VARCHAR(20),
    gender VARCHAR(20),
    education VARCHAR(100),
    workplace VARCHAR(100),
    department VARCHAR(50)
);

CREATE TABLE med_data_og (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    age INTEGER,
    address VARCHAR(100),
    email VARCHAR(50),
    gender VARCHAR(20),
    postal_code VARCHAR(20),
    diagnosis VARCHAR(20)
    
);

