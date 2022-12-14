insert ignore into role (id, name)
values  (1, 'Common Role'),
        (4, 'Contract Editor'),
        (2, 'Machine Editor'),
        (5, 'Report Viewer'),
        (3, 'Shop Editor');

insert ignore into privilege (id, name, api, method, description)
values  (1, 'login_page', '/login', 'GET', null),
        (2, 'machine_page', '/machine', 'GET', null),
        (3, 'machine_add_page', '/machine/add', 'GET', null),
        (4, 'machine_edit_page', '/machine/edit', 'GET', null),
        (5, 'machine_create', '/machine/create', 'POST', null),
        (6, 'machine_update', '/machine/update', 'POST', null),
        (7, 'shop_page', '/shop', 'GET', null),
        (8, 'shop_add_page', '/shop/add', 'GET', null),
        (9, 'shop_edit_page', '/shop/edit', 'GET', null),
        (10, 'shop_create', '/shop/create', 'POST', null),
        (11, 'shop_update', '/shop/update', 'POST', null),
        (12, 'contract_add_page', '/contract/add', 'GET', null),
        (13, 'contract_edit_page', '/contract/edit', 'GET', null),
        (14, 'contract_create', '/contract/create', 'POST', null),
        (15, 'contract_update', '/contract/update', 'POST', null),
        (16, 'api_get_oracle_supplier', '/api/supplier', 'GET', null),
        (17, 'api_get_oracle_distributor', '/api/distributor', 'GET', null),
        (18, 'api_get_payment_term_all', '/api/v1/payment_term', 'GET', null),
        (19, 'api_get_payment_term_one', '/api/v1/payment_term/all', 'GET', null),
        (20, 'api_check', '/api/check', 'GET', null),
        (21, 'api_authenticate', '/authenticate', 'POST', null),
        (22, 'api_update_machine_number', '/api/machine/update/machine_number', 'GET', null),
        (23, 'report_page', '/report', 'GET', null),
        (24, 'machine_summary_report', '/report/machine/summary', 'GET', null),
        (25, 'location_wise_shop_summary_report', '/report/shop/summary/location_wise', 'GET', null),
        (26, 'type_wise_shop_summary_report', '/report/shop/summary/type_wise', 'GET', null);

insert ignore into role_privilege (id, role_id, privilege_id)
values  (1, 1, 1),
        (2, 1, 2),
        (3, 1, 3),
        (4, 1, 4),
        (5, 1, 7),
        (6, 1, 8),
        (7, 1, 9),
        (8, 1, 12),
        (9, 1, 13),
        (10, 1, 16),
        (11, 1, 17),
        (12, 1, 18),
        (13, 1, 19),
        (14, 1, 20),
        (15, 1, 21),
        (16, 1, 22),
        (17, 1, 23),
        (18, 2, 5),
        (19, 2, 6),
        (20, 3, 10),
        (21, 3, 11),
        (22, 4, 14),
        (23, 4, 15),
        (24, 5, 24),
        (25, 5, 25),
        (26, 5, 26);