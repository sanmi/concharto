--TSM-302 - support for multiple catalogs (cruising, boating, hiking, fishing, hunting, fiction, personal)
ALTER TABLE Event  MODIFY catalog  varchar(64);

 create index catalogindex on Event (catalog);

 update Event set catalog='www';
  
ALTER TABLE Spotlight ADD catalog varchar(64);
 update Spotlight  set catalog='www';
 
update Flag f, User u set f.username = u.username
where f.user_id = u.id

alter table Flag drop foreign key FK_FLAG_USER; 
alter table Flag drop user_id;

