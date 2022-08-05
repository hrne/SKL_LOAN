ALTER TABLE "FacMain" ADD "StarBuildingPeriod" DECIMAL(2,0) DEFAULT 0 NOT NULL ;
ALTER TABLE "FacMain" ADD "StarBuildingYM" DECIMAL(5,0) DEFAULT 0 NOT NULL ;
comment on column "FacMain"."StarBuildingPeriod" is '約定動工之一定期間';
comment on column "FacMain"."StarBuildingYM" is '實際興建年月';
ALTER TABLE "Ias34Ap" DROP COLUMN "AgreeBefFacmNo";
ALTER TABLE "Ias34Ap" DROP COLUMN "AgreeBefBormNo";