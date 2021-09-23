drop table "ClParkingType" purge;

create table "ClParkingType" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "ParkingTypeCode" varchar2(1),
  "ParkingQty" decimal(5, 0) default 0 not null,
  "ParkingArea" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClParkingType" add constraint "ClParkingType_PK" primary key("ClCode1", "ClCode2", "ClNo", "ParkingTypeCode");

comment on table "ClParkingType" is '擔保品-停車位型式檔';
comment on column "ClParkingType"."ClCode1" is '擔保品-代號1';
comment on column "ClParkingType"."ClCode2" is '擔保品-代號2';
comment on column "ClParkingType"."ClNo" is '擔保品編號';
comment on column "ClParkingType"."ParkingTypeCode" is '停車位型式';
comment on column "ClParkingType"."ParkingQty" is '車位數量';
comment on column "ClParkingType"."ParkingArea" is '車位面積(坪)';
comment on column "ClParkingType"."CreateDate" is '建檔日期時間';
comment on column "ClParkingType"."CreateEmpNo" is '建檔人員';
comment on column "ClParkingType"."LastUpdate" is '最後更新日期時間';
comment on column "ClParkingType"."LastUpdateEmpNo" is '最後更新人員';
