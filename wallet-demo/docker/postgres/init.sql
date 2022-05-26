-- WALLET COMMAND DB AND USERS
CREATE DATABASE wallet_demo_db;

CREATE USER wallet_demo_user WITH ENCRYPTED PASSWORD 'password';
GRANT CONNECT ON DATABASE wallet_demo_db TO wallet_demo_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO wallet_demo_user;
