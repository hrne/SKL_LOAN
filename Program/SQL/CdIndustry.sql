drop table "CdIndustry" purge;

create table "CdIndustry" (
  "IndustryCode" varchar2(6),
  "IndustryItem" nvarchar2(50),
  "MainType" varchar2(1),
  "IndustryRating" varchar2(1),
  "Enable" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdIndustry" add constraint "CdIndustry_PK" primary key("IndustryCode");

create index "CdIndustry_Index1" on "CdIndustry"("MainType" asc);

comment on table "CdIndustry" is '行業別代號檔';
comment on column "CdIndustry"."IndustryCode" is '行業代號';
comment on column "CdIndustry"."IndustryItem" is '行業說明';
comment on column "CdIndustry"."MainType" is '主計處大類';
comment on column "CdIndustry"."IndustryRating" is '企金放款產業評等';
comment on column "CdIndustry"."Enable" is '啟用記號';
comment on column "CdIndustry"."CreateDate" is '建檔日期時間';
comment on column "CdIndustry"."CreateEmpNo" is '建檔人員';
comment on column "CdIndustry"."LastUpdate" is '最後更新日期時間';
comment on column "CdIndustry"."LastUpdateEmpNo" is '最後更新人員';
