ALTER TABLE ClStock modify  AcMtr NUMBER(5,2) DEFAULT 0 NOT NULL;
COMMENT ON COLUMN  ClStock.AcMtr IS '全戶維持率(%)';