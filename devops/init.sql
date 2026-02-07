CREATE DATABASE pocwebframeworks_keycloak_db WITH ENCODING = 'UTF8';
-- Change connection to pocwebframeworks_keycloak_db database
CREATE USER pocwebframeworks_keycloak_user WITH ENCRYPTED PASSWORD 'pocwebframeworks_keycloak_password';
CREATE SCHEMA IF NOT EXISTS pocwebframeworks_keycloak_schema;
GRANT ALL PRIVILEGES ON DATABASE pocwebframeworks_keycloak_db to pocwebframeworks_keycloak_user;
GRANT ALL PRIVILEGES ON SCHEMA pocwebframeworks_keycloak_schema to pocwebframeworks_keycloak_user;

CREATE DATABASE pocwebframeworks_db WITH ENCODING = 'UTF8';
-- Change connection to pocwebframeworks_db database
CREATE USER pocwebframeworks_user WITH ENCRYPTED PASSWORD 'pocwebframeworks_password';
CREATE SCHEMA IF NOT EXISTS pocwebframeworks_schema;
GRANT ALL PRIVILEGES ON DATABASE pocwebframeworks_db to pocwebframeworks_user;
GRANT ALL PRIVILEGES ON SCHEMA pocwebframeworks_schema to pocwebframeworks_user;
