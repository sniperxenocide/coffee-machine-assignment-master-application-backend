CREATE TABLE oracle_distributor
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    oracle_code VARCHAR(20)           NOT NULL,
    name        VARCHAR(255)          NOT NULL,
    division    VARCHAR(255)          NOT NULL,
    region      VARCHAR(255)          NOT NULL,
    territory   VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_oracle_distributor PRIMARY KEY (id)
);

ALTER TABLE oracle_distributor
    ADD CONSTRAINT uc_oracle_distributor_oracle_code UNIQUE (oracle_code);