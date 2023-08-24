ALTER TABLE "MonthlyFacBal" ADD "LawAssetClass" NVARCHAR2(100) NULL;
comment on column "MonthlyFacBal"."LawAssetClass" is '無擔保資產分類代號';