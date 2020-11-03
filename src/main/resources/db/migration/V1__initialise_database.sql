DROP SCHEMA IF EXISTS mcve CASCADE;

CREATE SCHEMA mcve;

CREATE TABLE mcve.feature (
  id     SERIAL,
  value INT,

  CONSTRAINT pk_feature PRIMARY KEY (id)
);

CREATE TABLE mcve.bug (
  id     SERIAL,
  value INT UNIQUE ,
  feature_id INT,

  CONSTRAINT pk_bug PRIMARY KEY (id),
  CONSTRAINT fk_feature FOREIGN KEY (feature_id) REFERENCES mcve.feature(id)
);
