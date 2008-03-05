--TSM-237 - Add User Page and User Talk Page
    create table Notification (
        id bigint not null auto_increment,
        created datetime,
        lastModified datetime,
        version bigint,
        description text,
        title varchar(512),
        type varchar(255),
        toUser_id bigint,
        fromUser_id bigint,
        primary key (id)
    );
    
    
    alter table wikitext modify text mediumtext;
    alter table wikitext add title varchar(512);
    
    alter table Notification 
        add index FK_NOTIF_FROMUSER (fromUser_id), 
        add constraint FK_NOTIF_FROMUSER 
        foreign key (fromUser_id) 
        references User (id);

    alter table Notification 
        add index FK_NOTIF_TOUSER (toUser_id), 
        add constraint FK_NOTIF_TOUSER 
        foreign key (toUser_id) 
        references User (id);    