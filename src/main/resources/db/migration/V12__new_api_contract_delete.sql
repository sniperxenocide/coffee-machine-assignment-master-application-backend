insert into privilege(id, name, api, method, description) VALUE
(28,'contract_delete','/contract/delete','POST','');

insert into role_privilege(id, role_id, privilege_id) value
(28,4,28);