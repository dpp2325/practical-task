DROP TABLE IF EXISTS filedata;

CREATE TABLE filedata (
  PRIMARY_KEY VARCHAR(50) PRIMARY KEY,
  NAME VARCHAR(100) NOT NULL,
  DESCRIPTION VARCHAR(250) NOT NULL,
  UPDATED_TIMESTAMP TIMESTAMP DEFAULT NULL
);