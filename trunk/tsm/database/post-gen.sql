-- Unfortunately these steps cannot be performed by the automatic hbm2ddl schema generation
alter table ADDRESS drop INDEX sp_addr;      
CREATE SPATIAL INDEX sp_addr ON address (addressLocation);
