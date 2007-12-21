mysqldump -p --default_character-set=latin1 -u root --skip-set-charset tsm_test > tsm_test_utf8.sql
--edit the file and replace all instances of 'latin1' with 'utf8'

DROP DATABASE tsm_test
CREATE DATABASE tsm_test CHARACTER SET utf8 COLLATE utf8_general_ci;

mysql -u root -p --default-character-set=utf8 --max_allowed_packet=64M tsm_test < tsm_test_utf8.sql
