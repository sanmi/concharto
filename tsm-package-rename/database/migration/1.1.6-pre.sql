--TSM-302 - support for multiple catalogs (cruising, boating, hiking, fishing, hunting, fiction, personal)
ALTER TABLE Event  MODIFY catalog  varchar(64);

create index catalogindex on Event (catalog);

update Event set catalog='www';
  
ALTER TABLE Spotlight ADD catalog varchar(64);
update Spotlight  set catalog='www';

--TSM-304 - Flag - new disposition and action to remove flags that are spam 
alter table Flag ADD username varchar(32);

update Flag f, User u set f.username = u.username
where f.user_id = u.id

alter table Flag drop foreign key FK_FLAG_USER; 

alter table Flag drop user_id;

