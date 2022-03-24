drop table "HlAreaData" purge;

create table "HlAreaData" (
  "AreaUnitNo" varchar2(6),
  "AreaName" nvarchar2(20),
  "AreaChiefEmpNo" varchar2(6),
  "AreaChiefName" nvarchar2(15),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "HlAreaData" add constraint "HlAreaData_PK" primary key("AreaUnitNo");

comment on table "HlAreaData" is '區域資料主檔';
comment on column "HlAreaData"."AreaUnitNo" is '區域代碼';
comment on column "HlAreaData"."AreaName" is '區域名稱';
comment on column "HlAreaData"."AreaChiefEmpNo" is '區域主管員編';
comment on column "HlAreaData"."AreaChiefName" is '區域主管名稱';
comment on column "HlAreaData"."CreateDate" is '建檔日期時間';
comment on column "HlAreaData"."CreateEmpNo" is '建檔人員';
comment on column "HlAreaData"."LastUpdate" is '最後更新日期時間';
comment on column "HlAreaData"."LastUpdateEmpNo" is '最後更新人員';
