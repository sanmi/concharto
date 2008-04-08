
    create table AuditEntry (
        id bigint not null auto_increment,
        action integer,
        dateCreated datetime,
        entityClass varchar(255),
        entityId bigint not null,
        user varchar(255),
        version bigint not null,
        primary key (id)
    );

    create table AuditFieldChange (
        id bigint not null auto_increment,
        newValue varchar(3999),
        oldValue varchar(3999),
        propertyName integer,
        auditEntry_id bigint,
        primary key (id)
    );

    create table Event (
        id bigint not null auto_increment,
        created datetime,
        lastModified datetime,
        version bigint,
        catalog integer,
        description varchar(2048),
        hasUnresolvedFlag bit,
        mapType integer,
        snippet varchar(1024),
        source varchar(1024),
        summary varchar(512),
        visible bit,
        negative integer,
        positive integer,
        _where varchar(512),
        zoomLevel integer,
        styleSelector_id bigint,
        discussion_id bigint,
        tsGeometry_id bigint,
        positionalAccuracy_id bigint,
        eventSearchText_id bigint,
        when_id bigint,
        primary key (id)
    );

    create table EventSearchText (
        id bigint not null auto_increment,
        description varchar(2048),
        source varchar(1024),
        summary varchar(512),
        userTags varchar(2048),
        _where varchar(512),
        primary key (id)
    ) comment='ENGINE : MyISAM';

    create table EventSummary (
        id bigint not null auto_increment,
        created datetime,
        lastModified datetime,
        version bigint,
        centroid blob,
        description varchar(255),
        snippet varchar(255),
        summary varchar(255),
        primary key (id)
    );

    create table Event_UserTag (
        Event_id bigint not null,
        userTags_id bigint not null
    );

    create table Flag (
        id bigint not null auto_increment,
        created datetime,
        lastModified datetime,
        version bigint,
        comment varchar(512),
        disposition varchar(32),
        dispositionComment varchar(512),
        reason varchar(32),
        state varchar(255),
        event_id bigint,
        user_id bigint,
        primary key (id)
    );

    create table Notification (
        id bigint not null auto_increment,
        created datetime,
        lastModified datetime,
        version bigint,
        description text,
        fromUsername varchar(32),
        title varchar(512),
        toUsername varchar(32),
        type varchar(255),
        primary key (id)
    );

    create table PositionalAccuracy (
        id bigint not null auto_increment,
        name varchar(255),
        visible bit,
        primary key (id)
    );

    create table Role (
        id bigint not null auto_increment,
        created datetime,
        lastModified datetime,
        version bigint,
        name varchar(255),
        primary key (id)
    );

    create table Spotlight (
        id bigint not null auto_increment,
        created datetime,
        lastModified datetime,
        version bigint,
        label varchar(2000),
        link varchar(2000),
        visible bit,
        addedByUser_id bigint,
        primary key (id)
    );

    create table StyleSelector (
        styleSelectorType varchar(31) not null,
        id bigint not null auto_increment,
        bgColor tinyblob,
        displayMode varchar(255),
        text varchar(255),
        textColor tinyblob,
        heading float,
        XUnits varchar(255),
        YUnits varchar(255),
        x double precision,
        y double precision,
        httpQuery varchar(255),
        url varchar(255),
        iconScale float,
        labelScale float,
        color tinyblob,
        colorMode varchar(255),
        width integer,
        fill bit,
        outline bit,
        primary key (id)
    );

    create table StyleSelector_map (
        StyleSelector_id bigint not null,
        styleUrl varchar(255),
        mapkey varchar(255),
        primary key (StyleSelector_id, mapkey)
    );

    create table TimePrimitive (
        timePrimativeType integer not null,
        id bigint not null auto_increment,
        time bigint,
        begin bigint,
        beginPrecision integer,
        end bigint,
        endPrecision integer,
        primary key (id)
    );

    create table TsGeometry (
        geometrytype varchar(31) not null,
        id bigint not null auto_increment,
        geometryCollection GEOMETRYCOLLECTION not null,
        primary key (id)
    ) comment='ENGINE : MyISAM';

    create table User (
        id bigint not null auto_increment,
        created datetime,
        lastModified datetime,
        version bigint,
        email varchar(255),
        password varchar(64),
        username varchar(32) unique,
        userNote_id bigint,
        primary key (id)
    );

    create table UserNote (
        id bigint not null auto_increment,
        passwordRetrievalKey varchar(128),
        rememberMeKey varchar(128),
        primary key (id)
    );

    create table UserTag (
        id bigint not null auto_increment,
        tag varchar(255),
        primary key (id)
    );

    create table User_EventSummary (
        User_id bigint not null,
        eventSummaries_id bigint not null,
        unique (eventSummaries_id)
    );

    create table User_Role (
        User_id bigint not null,
        roles_id bigint not null
    );

    create table WikiText (
        id bigint not null auto_increment,
        created datetime,
        lastModified datetime,
        version bigint,
        text mediumtext,
        title varchar(512),
        primary key (id)
    );

    alter table AuditFieldChange 
        add index FK_AUDITENTR_AUDITFLD (auditEntry_id), 
        add constraint FK_AUDITENTR_AUDITFLD 
        foreign key (auditEntry_id) 
        references AuditEntry (id);

    alter table Event 
        add index FK_EVENT_EVENTSEARCHTEXT (eventSearchText_id), 
        add constraint FK_EVENT_EVENTSEARCHTEXT 
        foreign key (eventSearchText_id) 
        references EventSearchText (id);

    alter table Event 
        add index FK_EVENT_GEOM (tsGeometry_id), 
        add constraint FK_EVENT_GEOM 
        foreign key (tsGeometry_id) 
        references TsGeometry (id);

    alter table Event 
        add index FK_EVENT_STYLE (styleSelector_id), 
        add constraint FK_EVENT_STYLE 
        foreign key (styleSelector_id) 
        references StyleSelector (id);

    alter table Event 
        add index FK_EVENT_TIMEPR (when_id), 
        add constraint FK_EVENT_TIMEPR 
        foreign key (when_id) 
        references TimePrimitive (id);

    alter table Event 
        add index FK_EVENT_DISCUSS (discussion_id), 
        add constraint FK_EVENT_DISCUSS 
        foreign key (discussion_id) 
        references WikiText (id);

    alter table Event 
        add index FK_EVENT_POSACCURACY (positionalAccuracy_id), 
        add constraint FK_EVENT_POSACCURACY 
        foreign key (positionalAccuracy_id) 
        references PositionalAccuracy (id);

    alter table Event_UserTag 
        add index FK_EVENT_USERTAG (Event_id), 
        add constraint FK_EVENT_USERTAG 
        foreign key (Event_id) 
        references Event (id);

    alter table Event_UserTag 
        add index FK_USERTAG_EVENT (userTags_id), 
        add constraint FK_USERTAG_EVENT 
        foreign key (userTags_id) 
        references UserTag (id);

    alter table Flag 
        add index FK_FLAG_USER (user_id), 
        add constraint FK_FLAG_USER 
        foreign key (user_id) 
        references User (id);

    alter table Flag 
        add index FK_FLAG_EVENT (event_id), 
        add constraint FK_FLAG_EVENT 
        foreign key (event_id) 
        references Event (id);

    alter table Spotlight 
        add index FK_SPOTLIGHT_USER (addedByUser_id), 
        add constraint FK_SPOTLIGHT_USER 
        foreign key (addedByUser_id) 
        references User (id);

    alter table StyleSelector_map 
        add index FK_STYLEMAP (StyleSelector_id), 
        add constraint FK_STYLEMAP 
        foreign key (StyleSelector_id) 
        references StyleSelector (id);

    create index endindex on TimePrimitive (end);

    create index beginindex on TimePrimitive (begin);

    alter table User 
        add index FK_USER_USERNOTE (userNote_id), 
        add constraint FK_USER_USERNOTE 
        foreign key (userNote_id) 
        references UserNote (id);

    alter table User_EventSummary 
        add index FK_USER_EVENTSUMMARY (User_id), 
        add constraint FK_USER_EVENTSUMMARY 
        foreign key (User_id) 
        references User (id);

    alter table User_EventSummary 
        add index FK_EVENTSUMMARY_USER (eventSummaries_id), 
        add constraint FK_EVENTSUMMARY_USER 
        foreign key (eventSummaries_id) 
        references EventSummary (id);

    alter table User_Role 
        add index FK_USER_ROLE (User_id), 
        add constraint FK_USER_ROLE 
        foreign key (User_id) 
        references User (id);

    alter table User_Role 
        add index FK_ROLE_USER (roles_id), 
        add constraint FK_ROLE_USER 
        foreign key (roles_id) 
        references Role (id);
