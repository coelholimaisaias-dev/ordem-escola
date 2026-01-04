CREATE DATABASE ordem_escola;

CREATE USER ordem_escola_user WITH ENCRYPTED PASSWORD 'ordem_escola_pass';

GRANT ALL PRIVILEGES ON DATABASE ordem_escola TO ordem_escola_user;