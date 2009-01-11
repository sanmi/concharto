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
    
 --TSM-168 - rotating spotlight
 create table Spotlight (
    id bigint not null auto_increment,
    created datetime,
    lastModified datetime,
    version bigint,
    label varchar(255),
    link varchar(255),
    visible bit,
    addedByUser_id bigint,
    primary key (id)
);

alter table Spotlight 
    add index FK_SPOTLIGHT_USER (addedByUser_id), 
    add constraint FK_SPOTLIGHT_USER 
    foreign key (addedByUser_id) 
    references User (id);
