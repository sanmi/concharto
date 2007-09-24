-- Unfortunately these steps cannot be performed by the automatic hbm2ddl schema generation
-- Also, we cannot use the foreign key constraint from event to geometry, since MySQL doesn't 
-- support foreign key constraints from innodb to myisam tables.
alter table TsEvent drop foreign key FK_EVENT_GEOM;
ALTER TABLE TsGeometry ENGINE = MyISAM;
CREATE SPATIAL INDEX sp_geom ON TsGeometry (geometryCollection);

ALTER TABLE address ENGINE = MyISAM;
CREATE SPATIAL INDEX sp_addr ON address (addressLocation);

