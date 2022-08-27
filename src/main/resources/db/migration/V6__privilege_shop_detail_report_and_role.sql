insert into privilege (id, name, api, method, description)
values  (27, 'location_wise_shop_machine_detail_report', '/report/shop_machine_detail/location_wise', 'GET', null);

insert into role_privilege (id, role_id, privilege_id)
values  (27, 5, 27);