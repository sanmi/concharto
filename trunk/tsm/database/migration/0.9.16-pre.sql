--TSM-165 - forgot password, remember me 
ALTER TABLE User  ADD  userNote_id bigint;
ALTER TABLE User MODIFY password varchar(64);
ALTER TABLE User MODIFY username varchar(32);
ALTER TABLE User MODIFY email varchar(128);

create table UserNote (
    id bigint not null auto_increment,
    passwordRetrievalKey varchar(128),
    rememberMeKey varchar(128),
    primary key (id)
);

alter table User 
    add index FK_USER_USERNOTE (userNote_id), 
    add constraint FK_USER_USERNOTE 
    foreign key (userNote_id) 
    references UserNote (id);