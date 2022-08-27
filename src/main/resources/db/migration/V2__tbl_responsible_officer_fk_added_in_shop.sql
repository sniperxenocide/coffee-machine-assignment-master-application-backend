CREATE TABLE responsible_officer
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    name  VARCHAR(50)           NULL,
    phone VARCHAR(100)          NOT NULL,
    CONSTRAINT pk_responsible_officer PRIMARY KEY (id)
);

ALTER TABLE shop
    ADD responsible_officer_id BIGINT NULL;

ALTER TABLE responsible_officer
    ADD CONSTRAINT uc_responsible_officer_phone UNIQUE (phone);

ALTER TABLE shop
    ADD CONSTRAINT FK_SHOP_ON_RESPONSIBLE_OFFICER FOREIGN KEY (responsible_officer_id) REFERENCES responsible_officer (id);