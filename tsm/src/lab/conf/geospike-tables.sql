-- debug
DROP TABLE geom;
CREATE TABLE geom (g GEOMETRY NOT NULL) ENGINE = MYISAM;
ALTER TABLE geom ADD pt POINT;
CREATE SPATIAL INDEX sp_index ON geom (g);

CREATE TABLE geom (g GEOMETRY NOT NULL, SPATIAL INDEX(g));

-- Test Tables
drop table address;
CREATE TABLE address (
  id bigint not null auto_increment,
  address CHAR(80) NOT NULL,
  addressLocation POINT NOT NULL,
  PRIMARY KEY(id),
  SPATIAL KEY(addressLocation)
) ENGINE = MYISAM;

drop table cab;
CREATE TABLE cab (
  cab_id INT AUTO_INCREMENT NOT NULL,
  cab_driver CHAR(80) NOT NULL,
  cab_loc POINT NOT NULL,
  PRIMARY KEY(cab_id),
  SPATIAL KEY(cab_loc)
) ENGINE = MYISAM;

INSERT INTO address VALUES(100, 'Foobar street 12', GeomFromText('POINT(2671 2500)'));
INSERT INTO address VALUES(200, 'Foobar street 56', GeomFromText('POINT(2971 2520)'));
INSERT INTO address VALUES(300, 'Foobar street 78', GeomFromText('POINT(3171 2510)'));
INSERT INTO address VALUES(400, 'Foobar street 97', GeomFromText('POINT(5671 2530)'));
INSERT INTO address VALUES(500, 'Foobar street 99', GeomFromText('POINT(6271 2460)'));
INSERT INTO address VALUES(600, 'Bloggs lane 10', GeomFromText('POINT(5673 3520)'));
INSERT INTO address VALUES(700, 'Bloggs lane 20', GeomFromText('POINT(5665 3550)'));
INSERT INTO address VALUES(800, 'Bloggs lane 45', GeomFromText('POINT(5571 3510)'));

INSERT INTO cab VALUES(100, 'Joe Bloggs', GeomFromText('POINT(2262 2100)'));
INSERT INTO cab VALUES(200, 'Bill Bloggs', GeomFromText('POINT(2441 1980)'));
INSERT INTO cab VALUES(300, 'Sam Spade', GeomFromText('POINT(5400 3200)'));

-- From MySQL 4.1, Example: query for the closest cab to a given location
SELECT
  c.cab_driver,
  ROUND(GLength(LineStringFromWKB(LineString(AsBinary(c.cab_loc),
                                             AsBinary(a.addressLocation)))))
    AS distance
FROM cab c, address a
WHERE a.address = 'Foobar street 99'
ORDER BY distance ASC LIMIT 1;

-- Show all
SELECT c.cab_driver, GLength(LineStringFromWKB(LineString(AsBinary(c.cab_loc),AsBinary(a.addressLocation)))) as distance
FROM cab c, address a
WHERE a.address = 'Foobar street 99'
ORDER BY distance;

-- Find wirhin a bounding rectangle
SELECT a.address, MBRWithin(a.addressLocation,Envelope(GeomFromText('LineString(2000 2000,5000 5000)')))
FROM address a;

SELECT a.address
FROM address a
WHERE MBRWithin(a.addressLocation,Envelope(GeomFromText('LineString(2000 2000,5000 5000)'))) = 1;

SELECT *, AsText(geom) FROM address where id < 1251120;

SELECT *, AsText(geom) FROM address 
WHERE MBRWithin(addressLocation,Envelope(GeomFromText('POLYGON ((300 300, 400 300, 400 400, 300 400, 300 300))')))
select count(*) FROM address WHERE MBRWithin(addressLocation,GeomFromText('POLYGON ((300 300, 400 300, 400 400, 300 400, 300 300))')) = 1;
select count(*) FROM address WHERE MBRWithin(addressLocation,GeomFromText('POLYGON ((300 300, 400 300, 400 400, 300 400, 300 300))'));

select count(*) FROM address WHERE Equals(geom,GeomFromText('GEOMETRYCOLLECTION(POLYGON((330 330,340 330,340 340,330 340,330 330)))'));
select count(*) FROM address WHERE MBREqual(geom,GeomFromText('GEOMETRYCOLLECTION(POLYGON((330 330,340 330,340 340,330 340,330 330)))'));

--this is fast (0 sec for 1,000,000 rows)
select count(*) FROM address WHERE MBRWithin(geom,Envelope(GeomFromText('POLYGON ((300 300, 400 300, 400 400, 300 400, 300 300))')));
--this is slow (1.95 sec for 1,000,000 rows)
select count(*) FROM address WHERE MBRWithin(geom,Envelope(GeomFromText('POLYGON ((300 300, 400 300, 400 400, 300 400, 300 300))'))) = 1;

select count(*) FROM address WHERE MBREqual(geom,Envelope(GeomFromText('GEOMETRYCOLLECTION(POINT(330 330))'))) = 1;
select count(*) FROM address WHERE MBREqual(geom,GeomFromText('GEOMETRYCOLLECTION(POINT(330 330))')) = 1
select count(*) FROM address WHERE Equals(geom,GeomFromText('GEOMETRYCOLLECTION(POINT(330 330))')) = 1

select count(*) FROM address WHERE MBREqual(addressLocation,Envelope(GeomFromText('POLYGON((330 330,340 330,340 340,330 340,330 330))'))) = 1


--DEBUG event
SELECT *, AsText(geom) FROM tsgeometry g 
SELECT * FROM event f, tsgeometry g
WHERE f.tsgeometry_id = g.id
AND MBRWithin(geom,Envelope(GeomFromText('POLYGON ((300 300, 400 300, 400 400, 300 400, 300 300))'))) = 1

SELECT * 
FROM event f, tsgeometry g
WHERE f.geometry_id = g.id 
AND MBRWithin(geom, Envelope(GeomFromText('POLYGON ((300 300, 400 300, 400 400, 300 400, 300 300))'))) = 1

delete from tsgeometry;
drop table tsgeometry;
drop table event;

delete from tsgeometry where id in (select id from event)

ALTER TABLE address CHANGE geom geom GEOMETRYCOLLECTION NOT NULL;

Select * from TimePrimitive where id = (select timeprimitive_id from event where id = 4)

--DEBUG big search queries

--geography and text
SELECT * FROM event f, tsgeometry g, eventsearchtext es
WHERE f.tsgeometry_id = g.id 
AND f.eventsearchtext_id = es.id
AND MBRWithin(geometryCollection, Envelope(GeomFromText('POLYGON ((300 300, 400 300, 400 400, 300 400, 300 300))')))
AND MATCH (es.summary, es.description, es.usertags, es.source) AGAINST ('description')

-- just geography
SELECT * FROM event f,  tsgeometry g
WHERE f.tsgeometry_id = g.id 
AND MBRWithin(geometryCollection, Envelope(GeomFromText('POLYGON ((-77.34169006347656 40.85433181694295, -76.81365966796875 40.85433181694295, -76.81365966796875 41.13884745373145, -77.34169006347656 41.13884745373145, -77.34169006347656 40.85433181694295))')))

--Just time
-- Feb 22, 2005 - Feb 22, 2007
SELECT * FROM event f, timeprimitive t
WHERE f.when_id = t.id
AND (t.begin BETWEEN 1111467600000 AND 1174536000000 )
AND (t.end BETWEEN 1111467600000 AND 1174536000000 );

-- Jan 1, 2007 - Feb 1, 2007
SELECT * FROM event f, timeprimitive t
WHERE f.when_id = t.id
AND (t.begin BETWEEN 1167627600000 AND 1199163600000 )
AND (t.end BETWEEN 1111467600000 AND 1174536000000 );


--just text no join
SELECT count(*) FROM eventsearchtext es
WHERE MATCH (es.summary, es._where, es.usertags, es.description, es.source) AGAINST ('description')

--just text
SELECT count(*) FROM event f, eventsearchtext es
WHERE f.eventsearchtext_id = es.id
AND MATCH (es.summary, es._where, es.usertags, es.description, es.source) AGAINST ('description')

--everything
SELECT * FROM event f, tsgeometry g, eventsearchtext es, timeprimitive t
WHERE f.tsgeometry_id = g.id 
AND f.eventsearchtext_id = es.id
AND f.when_id = t.id
AND MBRWithin(geometryCollection, Envelope(GeomFromText('POLYGON ((-78.34169006347656 40.85433181694295, -76.81365966796875 40.85433181694295, -76.81365966796875 41.13884745373145, -78.34169006347656 41.13884745373145, -78.34169006347656 40.85433181694295))')))
AND MATCH (es.summary, es._where, es.usertags, es.description, es.source) AGAINST ('description')
AND (t.begin BETWEEN DATE('1905-03-22') AND DATE('1907-03-22') )
AND (t.end BETWEEN DATE('1905-03-22') AND DATE('1907-03-22') );

