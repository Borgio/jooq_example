CREATE DATABASE jooq_test;
CREATE USER 'test'@'localhost'
  IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON jooq_test.* TO 'test'@'localhost';

