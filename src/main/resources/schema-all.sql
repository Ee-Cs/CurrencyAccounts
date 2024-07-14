DROP TABLE owner IF EXISTS;
DROP TABLE sub_account IF EXISTS;

CREATE TABLE owner  (
    pesel VARCHAR(11),
    first_name VARCHAR(25),
    last_name VARCHAR(25),
);
CREATE TABLE sub_account  (
    pesel VARCHAR(11),
    amount DOUBLE,
    currency VARCHAR(3)
);