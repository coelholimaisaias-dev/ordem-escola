CREATE DATABASE ordem_escola;

CREATE USER ordem_escola_user WITH ENCRYPTED PASSWORD 'ordem_escola_pass';

GRANT ALL PRIVILEGES ON DATABASE ordem_escola TO ordem_escola_user;

-- Ensure Flyway and app user can create tables in the default 'public' schema
\connect ordem_escola
GRANT USAGE ON SCHEMA public TO ordem_escola_user;
GRANT CREATE ON SCHEMA public TO ordem_escola_user;