commit;

-- Unfortunately these steps cannot be performed by the automatic hbm2ddl schema generation
CREATE SPATIAL INDEX sp_addr ON address (addressLocation);

CREATE SPATIAL INDEX sp_geom ON TsGeometry (geometryCollection);
