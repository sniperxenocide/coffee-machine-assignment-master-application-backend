CREATE TABLE contract_history
(
    id                         BIGINT AUTO_INCREMENT  NOT NULL,
    contract_id                BIGINT                 NULL,
    machine_id                 BIGINT                 NULL,
    shop_id                    BIGINT                 NULL,
    machine_allotment_price    DOUBLE                 NULL,
    down_payment               DOUBLE                 NULL,
    installment_type_id        BIGINT                 NULL,
    number_of_installment      INT                    NULL,
    per_installment_amount     DOUBLE                 NULL,
    daily_target_volume_kg     DOUBLE                 NULL,
    hand_over_date             date                   NULL,
    service_warranty_month     INT                    NULL,
    security_money             DOUBLE                 NULL,
    security_money_return_date date                   NULL,
    installment_start_date     date                   NULL,
    payment_term_id            BIGINT                 NULL,
    contract_created_by        BIGINT                 NULL,
    contract_creation_time     datetime               NULL,
    contract_end_time          datetime               NULL,
    created_by                 BIGINT                 NULL,
    creation_time              datetime DEFAULT current_timestamp NULL,
    CONSTRAINT pk_contract_history PRIMARY KEY (id)
);

ALTER TABLE contract_history
    ADD CONSTRAINT FK_CONTRACT_HISTORY_ON_CONTRACT FOREIGN KEY (contract_id) REFERENCES contract (id);

ALTER TABLE contract_history
    ADD CONSTRAINT FK_CONTRACT_HISTORY_ON_CONTRACT_CREATED_BY FOREIGN KEY (contract_created_by) REFERENCES user (id);

ALTER TABLE contract_history
    ADD CONSTRAINT FK_CONTRACT_HISTORY_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES user (id);

ALTER TABLE contract_history
    ADD CONSTRAINT FK_CONTRACT_HISTORY_ON_INSTALLMENT_TYPE FOREIGN KEY (installment_type_id) REFERENCES installment_type (id);

ALTER TABLE contract_history
    ADD CONSTRAINT FK_CONTRACT_HISTORY_ON_MACHINE FOREIGN KEY (machine_id) REFERENCES machine (id);

ALTER TABLE contract_history
    ADD CONSTRAINT FK_CONTRACT_HISTORY_ON_PAYMENT_TERM FOREIGN KEY (payment_term_id) REFERENCES payment_term (id);

ALTER TABLE contract_history
    ADD CONSTRAINT FK_CONTRACT_HISTORY_ON_SHOP FOREIGN KEY (shop_id) REFERENCES shop (id);