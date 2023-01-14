insert into user (username,name,pwd,role,team,create_dt,update_dt)
select * from (select 'Admin' as username, 'Admin' as name, '$2a$10$cODMN3R9ZmiQQTV3yoPab.g2EBclXAnzFWiEDwEYIgBttVt0r3Rme' as pwd, 'ROLE_ADMIN' as role, 'RF.Spartans' as team, current_timestamp as create_dt, current_timestamp as updated_dt) as tmp
where not exists (select username from user where username='Admin') LIMIT 1;