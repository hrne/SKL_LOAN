drop table "CdCityRate" purge;

create table "CdCityRate" (
  "EffectYYMM" decimal(6, 0) default 0 not null,
  "CityCode" varchar2(2),
  "IntRateIncr" decimal(6, 4) default 0 not null,
  "IntRateCeiling" decimal(6, 4) default 0 not null,
  "IntRateFloor" decimal(6, 4) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdCityRate" add constraint "CdCityRate_PK" primary key("EffectYYMM", "CityCode");

create index "CdCityRate_Index1" on "CdCityRate"("CityCode" asc);

comment on table "CdCityRate" is '地區利率檔';
comment on column "CdCityRate"."EffectYYMM" is '生效年月';
comment on column "CdCityRate"."CityCode" is '縣市代碼(地區別)';
comment on column "CdCityRate"."IntRateIncr" is '利率加減碼';
comment on column "CdCityRate"."IntRateCeiling" is '利率上限';
comment on column "CdCityRate"."IntRateFloor" is '利率下限';
comment on column "CdCityRate"."CreateDate" is '建檔日期時間';
comment on column "CdCityRate"."CreateEmpNo" is '建檔人員';
comment on column "CdCityRate"."LastUpdate" is '最後更新日期時間';
comment on column "CdCityRate"."LastUpdateEmpNo" is '最後更新人員';
