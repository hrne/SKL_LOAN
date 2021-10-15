drop table "CdBuildingCost" purge;

create table "CdBuildingCost" (
  "CityCode" varchar2(2),
  "FloorLowerLimit" decimal(3, 0) default 0 not null,
  "Cost" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdBuildingCost" add constraint "CdBuildingCost_PK" primary key("CityCode", "FloorLowerLimit");

comment on table "CdBuildingCost" is '建築造價參考檔';
comment on column "CdBuildingCost"."CityCode" is '縣市代碼(地區別)';
comment on column "CdBuildingCost"."FloorLowerLimit" is '總樓層數(下限)';
comment on column "CdBuildingCost"."Cost" is '建築造價';
comment on column "CdBuildingCost"."CreateDate" is '建檔日期時間';
comment on column "CdBuildingCost"."CreateEmpNo" is '建檔人員';
comment on column "CdBuildingCost"."LastUpdate" is '最後更新日期時間';
comment on column "CdBuildingCost"."LastUpdateEmpNo" is '最後更新人員';
