ALTER TABLE "MonthlyLM052AssetClass" ADD "StorageRate" decimal(2,4);
comment on column "MonthlyLM052AssetClass"."StorageRate" is '提存比率';
ALTER TABLE "MonthlyLM052AssetClass" ADD "StorageAmt" decimal(16,2);
comment on column "MonthlyLM052AssetClass"."StorageAmt" is '提存金額';
ALTER TABLE "MonthlyFacBal" ADD "BankRelationFlag" VARCHAR2(1);
comment on column "MonthlyFacBal"."BankRelationFlag" is '是否為利害關係人';
ALTER TABLE "MonthlyFacBal" ADD "GovProjectFlag" VARCHAR2(1);
comment on column "MonthlyFacBal"."GovProjectFlag" is '政策性專案貸款';
ALTER TABLE "MonthlyFacBal" ADD "BuildingFlag" VARCHAR2(1);
comment on column "MonthlyFacBal"."BuildingFlag" is '建築貸款記號';
ALTER TABLE "MonthlyFacBal" ADD "SpecialAssetFlag" VARCHAR2(1);
comment on column "MonthlyFacBal"."SpecialAssetFlag" is '特定資產記號';
ALTER TABLE "MonthlyLM042RBC" ADD "RiskFactorAmount" decimal(16,2);
comment on column "MonthlyLM042RBC"."RiskFactorAmount" is '風險量金額';

