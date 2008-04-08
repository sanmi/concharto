alter table Notification add toUsername varchar(32);
alter table Notification add fromUsername varchar(32);

UPDATE Notification n, User u SET n.fromUsername=u.username
WHERE n.fromUser_id = u.id

UPDATE Notification n, User u SET n.toUsername=u.username
WHERE n.toUser_id = u.id
  

alter table Notification dop toUser_id;        
alter table Notification dop fromUser_id;        
