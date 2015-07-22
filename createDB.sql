-- DROP DATABASE javaee6demos;
-- DROP USER 'demouser'@'%';

-- source C:\...\JavaEE6Demos\createDB.sql

CREATE DATABASE javaee6demos;
USE javaee6demos;

CREATE USER 'demouser'@'%' IDENTIFIED BY 'demopass';
GRANT ALL PRIVILEGES ON javaee6demos.* TO 'demouser'@'%';
FLUSH PRIVILEGES;