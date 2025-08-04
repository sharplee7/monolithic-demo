-- PostgreSQL Database Creation Script
-- Run this script as postgres superuser to create the monolithicbank database

-- Create database
CREATE DATABASE monolithicbank
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE monolithicbank TO postgres;

-- Connect to the database and create schema (optional)
\c monolithicbank;

-- Create schema if needed (optional - can use public schema)
-- CREATE SCHEMA IF NOT EXISTS monolithicbank;
-- GRANT ALL ON SCHEMA monolithicbank TO postgres;