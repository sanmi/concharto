-- TSM-148 positional accuracy
create table PositionalAccuracy (
    id bigint not null auto_increment,
    name varchar(255),
    visible bit,
    primary key (id)
);

ALTER TABLE Event ADD  positionalAccuracy_id bigint;

alter table Event 
    add index FK_EVENT_POSACCURACY (positionalAccuracy_id), 
    add constraint FK_EVENT_POSACCURACY 
    foreign key (positionalAccuracy_id) 
    references PositionalAccuracy (id);

INSERT INTO PositionalAccuracy (id, name, visible) VALUES
(1, 'UNSPECIFIED', true),
(2, 'REGION', true),
(3, 'CITY', true),
(4, 'NEIGHBORHOOD', true),
(5, 'ADDRESS', true),
(6, 'PINPOINT', true);
