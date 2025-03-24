CREATE USER 'planner'@'localhost' IDENTIFIED BY 'planner';

CREATE DATABASE IF NOT EXISTS planner;

GRANT ALL PRIVILEGES ON planner.* TO 'planner'@'localhost';