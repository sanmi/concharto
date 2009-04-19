ALTER TABLE UserTag ADD created datetime;
ALTER TABLE UserTag ADD lastModified datetime;
ALTER TABLE UserTag ADD version bigint;

UPDATE  UserTag AS t
RIGHT JOIN Event_UserTag as eut ON t.id = eut.userTags_id
RIGHT JOIN Event as e ON e.id = eut.Event_id
SET t.created = e.created;

-- a few tags have a null created date
UPDATE UserTag SET created = adddate(curdate(), -61) where created is null;

UPDATE UserTag SET lastModified = curdate();

UPDATE UserTag SET version = 0;