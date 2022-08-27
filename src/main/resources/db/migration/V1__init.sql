create table if not exists chamber_option
(
    id          bigint auto_increment
        primary key,
    name        varchar(100) not null,
    description varchar(200) null
);

create table if not exists installment_type
(
    id          bigint auto_increment
        primary key,
    name        varchar(100) not null,
    description varchar(200) null
);

create table if not exists machine_brand
(
    id   bigint auto_increment
        primary key,
    name varchar(100) not null
);

create table if not exists origin_country
(
    id   bigint auto_increment
        primary key,
    name varchar(100) not null
);

create table if not exists payment_term
(
    id                      bigint auto_increment
        primary key,
    name                    varchar(100) not null,
    machine_allotment_price double       null,
    down_payment            double       null,
    installment_type_id     bigint       null,
    number_of_installment   int          null,
    per_installment_amount  double       null,
    constraint payment_term_installment_type_id_fk
        foreign key (installment_type_id) references installment_type (id)
);

create table if not exists privilege
(
    id          bigint auto_increment
        primary key,
    name        varchar(100)              not null,
    api         varchar(500)              not null,
    method      varchar(20) default 'GET' not null,
    description varchar(1000)             null,
    constraint privilege_api_uindex
        unique (api),
    constraint privilege_name_uindex
        unique (name)
);

create table if not exists role
(
    id   bigint auto_increment
        primary key,
    name varchar(50) not null,
    constraint role_name_uindex
        unique (name)
);

create table if not exists role_privilege
(
    id           bigint auto_increment
        primary key,
    role_id      bigint not null,
    privilege_id bigint not null,
    constraint role_privilege_privilege_id_fk
        foreign key (privilege_id) references privilege (id),
    constraint role_privilege_role_id_fk
        foreign key (role_id) references role (id)
);

create table if not exists shop_grade
(
    id          bigint auto_increment
        primary key,
    name        varchar(20)  not null,
    description varchar(100) null
);

create table if not exists shop_type
(
    id          bigint auto_increment
        primary key,
    name        varchar(100) not null,
    description varchar(200) null
);

create table if not exists user
(
    id       bigint auto_increment
        primary key,
    username varchar(45) not null,
    password varchar(45) not null,
    constraint user_username_uindex
        unique (username)
);

create table if not exists machine
(
    id                     bigint auto_increment
        primary key,
    machine_number         varchar(100)                       null,
    machine_code           varchar(100)                       null,
    origin_id              bigint                             null,
    brand_id               bigint                             null,
    option_id              bigint                             null,
    supplier_name          varchar(200)                       null,
    supplier_oracle_code   varchar(100)                       null,
    machine_receiving_date date                               null,
    warranty_period_month  int                                null,
    machine_po_price       double                             null,
    machine_landed_cost    double                             null,
    po_number              varchar(100)                       null,
    lc_number              varchar(100)                       null,
    created_by             bigint                             null,
    creation_time          datetime default CURRENT_TIMESTAMP not null,
    lot_number             varchar(100)                       null,
    model_number           varchar(100)                       null,
    oracle_item_code       varchar(200)                       null,
    constraint machine_chamber_option_id_fk
        foreign key (option_id) references chamber_option (id)
            on delete set null,
    constraint machine_machine_brand_id_fk
        foreign key (brand_id) references machine_brand (id)
            on delete set null,
    constraint machine_origin_country_id_fk
        foreign key (origin_id) references origin_country (id)
            on delete set null,
    constraint machine_user_id_fk
        foreign key (created_by) references user (id)
            on delete set null
);

create table if not exists shop
(
    id                      bigint auto_increment
        primary key,
    shop_name               varchar(200)                       not null,
    shop_code               varchar(100)                       not null,
    shop_type_id            bigint                             null,
    proprietor_name         varchar(100)                       not null,
    shop_grade_id           bigint                             null,
    proprietor_nid          varchar(100)                       null,
    address                 varchar(1000)                      not null,
    division                varchar(100)                       not null,
    region                  varchar(100)                       not null,
    territory               varchar(100)                       not null,
    responsible_officer     varchar(100)                       not null,
    officer_phone           varchar(50)                        not null,
    distributor_name        varchar(200)                       null,
    distributor_oracle_code varchar(50)                        null,
    created_by              bigint                             null,
    creation_time           datetime default CURRENT_TIMESTAMP not null,
    proprietor_phone        varchar(50)                        not null,
    constraint shop_shop_code_uindex
        unique (shop_code),
    constraint shop_shop_grade_id_fk
        foreign key (shop_grade_id) references shop_grade (id)
            on delete set null,
    constraint shop_shop_type_id_fk
        foreign key (shop_type_id) references shop_type (id)
            on delete set null,
    constraint shop_user_id_fk
        foreign key (created_by) references user (id)
);

create table if not exists contract
(
    id                         bigint auto_increment
        primary key,
    machine_id                 bigint                             not null,
    shop_id                    bigint                             not null,
    machine_allotment_price    double                             not null,
    down_payment               double                             not null,
    installment_type_id        bigint                             null,
    number_of_installment      int                                not null,
    per_installment_amount     double                             not null,
    daily_target_volume_kg     double                             not null,
    hand_over_date             date                               not null,
    payment_term_id            bigint                             null,
    created_by                 bigint                             null,
    creation_time              datetime default CURRENT_TIMESTAMP not null,
    service_warranty_month     int                                null,
    security_money             double                             null,
    security_money_return_date date                               null,
    installment_start_date     date                               null,
    constraint contract_installment_type_id_fk
        foreign key (installment_type_id) references installment_type (id)
            on delete set null,
    constraint contract_machine_id_fk
        foreign key (machine_id) references machine (id)
            on delete cascade,
    constraint contract_payment_term_id_fk
        foreign key (payment_term_id) references payment_term (id)
            on delete set null,
    constraint contract_shop_id_fk
        foreign key (shop_id) references shop (id)
            on delete cascade,
    constraint contract_user_id_fk
        foreign key (created_by) references user (id)
);

create table if not exists contract_history
(
    id                         bigint auto_increment
        primary key,
    machine_id                 bigint                             not null,
    shop_id                    bigint                             not null,
    machine_allotment_price    double                             not null,
    down_payment               double                             not null,
    installment_type_id        bigint                             null,
    number_of_installment      int                                not null,
    per_installment_amount     double                             not null,
    daily_target_volume_kg     double                             not null,
    hand_over_date             date                               not null,
    payment_term_id            bigint                             null,
    created_by                 bigint                             null,
    creation_time              datetime                           not null,
    deleted_by                 bigint                             null,
    delete_time                datetime default CURRENT_TIMESTAMP not null,
    service_warranty_month     int                                null,
    security_money             double                             null,
    security_money_return_date date                               null,
    installment_start_date     date                               null,
    constraint contract_hst_deleted_user_id_fk
        foreign key (deleted_by) references user (id),
    constraint contract_hst_installment_type_id_fk
        foreign key (installment_type_id) references installment_type (id)
            on delete set null,
    constraint contract_hst_machine_id_fk
        foreign key (machine_id) references machine (id)
            on delete cascade,
    constraint contract_hst_payment_term_id_fk
        foreign key (payment_term_id) references payment_term (id)
            on delete set null,
    constraint contract_hst_shop_id_fk
        foreign key (shop_id) references shop (id)
            on delete cascade,
    constraint contract_hst_user_id_fk
        foreign key (created_by) references user (id)
);

create table if not exists user_role
(
    id      bigint auto_increment
        primary key,
    user_id bigint not null,
    role_id bigint not null,
    constraint user_role_role_id_fk
        foreign key (role_id) references role (id),
    constraint user_role_user_id_fk
        foreign key (user_id) references user (id)
);


