--TSM-135 event discussion 
ALTER TABLE Event ADD  discussion_id bigint;

create table WikiText (
    id bigint not null auto_increment,
    text text,
    primary key (id)
);
    
alter table Event 
    add index FK_EVENT_DISCUSS (discussion_id), 
    add constraint FK_EVENT_DISCUSS 
    foreign key (discussion_id) 
    references WikiText (id);
