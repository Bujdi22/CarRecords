ALTER TABLE maintenance_record ADD COLUMN odometer INTEGER;
UPDATE maintenance_record SET odometer = 0;
ALTER TABLE maintenance_record ALTER COLUMN odometer SET NOT NULL;

ALTER TABLE maintenance_record_aud ADD COLUMN odometer INTEGER;
UPDATE maintenance_record_aud SET odometer = 0;
ALTER TABLE maintenance_record_aud ALTER COLUMN odometer SET NOT NULL;