ALTER TABLE "MonthlyFacBal" MODIFY "AssetClass" VARCHAR2(1);
comment on column "MonthlyFacBal"."AssetClass" is '資產五分類代號(有擔保部分)';
ALTER TABLE "MonthlyFacBal" ADD "AssetClass2" VARCHAR2(2) ;
comment on column "MonthlyFacBal"."AssetClass2" is '資產五分類代號2(有擔保部分)';
ALTER TABLE "MonthlyFacBal" MODIFY "LawAssetClass" VARCHAR2(1) ;
comment on column "MonthlyFacBal"."LawAssetClass" is '無擔保資產分類代號';
