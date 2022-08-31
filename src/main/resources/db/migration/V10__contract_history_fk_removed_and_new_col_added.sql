ALTER TABLE contract_history
    ADD division VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD installment_type VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD machine_brand VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD machine_model VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD machine_number VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD maos_phone VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD mso_name VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD payment_term BIGINT NULL;

ALTER TABLE contract_history
    ADD region VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD shop_address VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD shop_code VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD shop_name VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD shop_owner_name VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD shop_owner_phone VARCHAR(255) NULL;

ALTER TABLE contract_history
    ADD territory VARCHAR(255) NULL;

ALTER TABLE contract_history
    DROP FOREIGN KEY FK_CONTRACT_HISTORY_ON_CONTRACT;

ALTER TABLE contract_history
    DROP FOREIGN KEY FK_CONTRACT_HISTORY_ON_INSTALLMENT_TYPE;

ALTER TABLE contract_history
    DROP FOREIGN KEY FK_CONTRACT_HISTORY_ON_MACHINE;

ALTER TABLE contract_history
    DROP FOREIGN KEY FK_CONTRACT_HISTORY_ON_PAYMENT_TERM;

ALTER TABLE contract_history
    DROP FOREIGN KEY FK_CONTRACT_HISTORY_ON_SHOP;