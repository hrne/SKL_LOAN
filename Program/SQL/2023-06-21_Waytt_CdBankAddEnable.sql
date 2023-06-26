ALTER TABLE "CdBank" 
ADD (
    "Enable" VARCHAR2(1) 
);

COMMENT ON COLUMN "CdBank"."Enable" IS '啟用記號';

