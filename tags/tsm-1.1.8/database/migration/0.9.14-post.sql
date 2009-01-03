--TSM-76 - search - doesn't find words that have apostrophes (e.g. doesn't)
--First edit my.ini / my.cnf to add the following under the [mysqld] section:
-- #Free text word size
-- ft_min_word_len=3

alter table EventSearchText drop index ft_event; 
CREATE FULLTEXT INDEX ft_event ON EventSearchText (summary, _where, usertags, description, source);
