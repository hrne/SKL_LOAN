drop table "ClParking" purge;

create table "ClParking" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "ParkingSeqNo" decimal(5, 0) default 0 not null,
  "ParkingNo" nvarchar2(20),
  "ParkingQty" decimal(5, 0) default 0 not null,
  "ParkingTypeCode" varchar2(1),
  "OwnerPart" decimal(10, 0) default 0 not null,
  "OwnerTotal" decimal(10, 0) default 0 not null,
  "CityCode" varchar2(2),
  "AreaCode" varchar2(3),
  "IrCode" varchar2(5),
  "BdNo1" varchar2(5),
  "BdNo2" varchar2(3),
  "LandNo1" varchar2(4),
  "LandNo2" varchar2(4),
  "ParkingArea" decimal(16, 2) default 0 not null,
  "Amount" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClParking" add constraint "ClParking_PK" primary key("ClCode1", "ClCode2", "ClNo", "ParkingSeqNo");

comment on table "ClParking" is '擔保品-車位資料檔';
comment on column "ClParking"."ClCode1" is '擔保品-代號1';
comment on column "ClParking"."ClCode2" is '擔保品-代號2';
comment on column "ClParking"."ClNo" is '擔保品編號';
comment on column "ClParking"."ParkingSeqNo" is '車位資料序號';
comment on column "ClParking"."ParkingNo" is '車位編號';
comment on column "ClParking"."ParkingQty" is '車位數量';
comment on column "ClParking"."ParkingTypeCode" is '停車位型式';
comment on column "ClParking"."OwnerPart" is '持份比率(分子)';
comment on column "ClParking"."OwnerTotal" is '持份比率(分母)';
comment on column "ClParking"."CityCode" is '縣市';
comment on column "ClParking"."AreaCode" is '鄉鎮市區';
comment on column "ClParking"."IrCode" is '段小段代碼';
comment on column "ClParking"."BdNo1" is '建號';
comment on column "ClParking"."BdNo2" is '建號(子號)';
comment on column "ClParking"."LandNo1" is '地號';
comment on column "ClParking"."LandNo2" is '地號(子號)';
comment on column "ClParking"."ParkingArea" is '車位面積(坪)';
comment on column "ClParking"."Amount" is '價格(元)';
comment on column "ClParking"."CreateDate" is '建檔日期時間';
comment on column "ClParking"."CreateEmpNo" is '建檔人員';
comment on column "ClParking"."LastUpdate" is '最後更新日期時間';
comment on column "ClParking"."LastUpdateEmpNo" is '最後更新人員';
