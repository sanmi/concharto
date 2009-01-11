-- edit my.ini or my.cbf and add or modify it so it says the following
[mysql]
default-character-set=utf8
[mysqld]
default-character-set=utf8
default-storage-engine=INNODB


-- --- backup and dump for switching
-- mysqldump -p -u root  tsm_test > tsm_test.sql
-- mysqldump -p --default_character-set=latin1 -u root --skip-set-charset tsm_test > tsm_test_utf8.sql
-- --- edit the file and replace all instances of 'latin1' with 'utf8'

DROP DATABASE tsm_test
CREATE DATABASE tsm_test CHARACTER SET utf8 COLLATE utf8_general_ci;

-- mysql -u root -p --default-character-set=utf8 --max_allowed_packet=64M tsm_test < tsm_test_utf8.sql

-- --- backup and dump for switching
-- mysqldump -p -u root  tsm_prod > tsm_prod.sql
-- mysqldump -p --default_character-set=latin1 -u root --skip-set-charset tsm_prod > tsm_prod_utf8.sql
-- --- edit the file and replace all instances of 'latin1' with 'utf8'

DROP DATABASE tsm_prod
CREATE DATABASE tsm_prod CHARACTER SET utf8 COLLATE utf8_general_ci;

-- mysql -u root -p --default-character-set=utf8 --max_allowed_packet=64M tsm_prod < tsm_prod_utf8.sql
