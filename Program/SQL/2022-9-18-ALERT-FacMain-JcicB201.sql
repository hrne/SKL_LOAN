ALTER TABLE "FacMain" RENAME COLUMN "StarBuildingPeriod" TO "PreStarBuildingYM";
ALTER TABLE "FacMain" MODIFY "PreStarBuildingYM" DECIMAL(5);
comment on column "FacMain"."PreStarBuildingYM" is '約定動工年月';
comment on column "FacMain"."SettingDate" is '額度設定日';
ALTER TABLE "JcicB201" RENAME COLUMN "LandLoanFg" TO "Filler51";
ALTER TABLE "JcicB201" MODIFY "Filler51" VARCHAR2(3);
comment on column "JcicB201"."Filler51" is '空白';
ALTER TABLE "JcicB201" RENAME COLUMN "Filler443" TO "LandLoanFg";
ALTER TABLE "JcicB201" MODIFY "LandLoanFg" VARCHAR2(1);
comment on column "JcicB201"."LandLoanFg" is '購地貸款註記';
ALTER TABLE "JcicB201" ADD "PreStarBuildingYM" VARCHAR2(5) null;
comment on column "JcicB201"."PreStarBuildingYM" is '約定動工年月';
ALTER TABLE "JcicB201" DROP COLUMN "StarBuildingPeriod";
