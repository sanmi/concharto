--TSM-60 time parsing - Dec 7, 1940 10am - Jan, 1941 yeilds confusing results
ALTER TABLE TimePrimitive ADD beginPrecision integer;
ALTER TABLE TimePrimitive ADD endPrecision integer;
ALTER TABLE TimePrimitive  MODIFY time bigint;