ALTER TABLE "JcicB201" RENAME COLUMN "Filler51" TO "LandLoanFg";
ALTER TABLE "JcicB201" RENAME COLUMN "Filler52" TO "StarBuildingYM";
ALTER TABLE "JcicB201" MODIFY "LandLoanFg" VARCHAR2(1);
ALTER TABLE "JcicB201" ADD "StarBuildingPeriod" VARCHAR2(2);
ALTER TABLE "JcicB201" MODIFY "StarBuildingYM" VARCHAR2(5);
comment on column "JcicB201"."LandLoanFg" is '購地貸款註記';
comment on column "JcicB201"."StarBuildingPeriod" is '約定動工之一定期間';
comment on column "JcicB201"."StarBuildingYM" is '實際興建年月';