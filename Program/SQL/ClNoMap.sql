drop table "ClNoMap" purge;

create table "ClNoMap" (
  "GdrId1" decimal(1, 0) default 0 not null,
  "GdrId2" decimal(2, 0) default 0 not null,
  "GdrNum" decimal(7, 0) default 0 not null,
  "LgtSeq" decimal(2, 0) default 0 not null,
  "MainGdrId1" decimal(1, 0) default 0 not null,
  "MainGdrId2" decimal(2, 0) default 0 not null,
  "MainGdrNum" decimal(7, 0) default 0 not null,
  "MainLgtSeq" decimal(2, 0) default 0 not null,
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "TfStatus" decimal(1, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClNoMap" add constraint "ClNoMap_PK" primary key("GdrId1", "GdrId2", "GdrNum", "LgtSeq");

create index "ClNoMap_Index1" on "ClNoMap"("MainGdrId1" asc, "MainGdrId2" asc, "MainGdrNum" asc, "MainLgtSeq" asc);

create index "ClNoMap_Index2" on "ClNoMap"("ClCode1" asc, "ClCode2" asc, "ClNo" asc);

comment on table "ClNoMap" is '擔保品編號新舊對照檔';
comment on column "ClNoMap"."GdrId1" is '原擔保品代號1';
comment on column "ClNoMap"."GdrId2" is '原擔保品代號2';
comment on column "ClNoMap"."GdrNum" is '原擔保品編號';
comment on column "ClNoMap"."LgtSeq" is '原擔保品序號';
comment on column "ClNoMap"."MainGdrId1" is '最新擔保品代號1';
comment on column "ClNoMap"."MainGdrId2" is '最新擔保品代號2';
comment on column "ClNoMap"."MainGdrNum" is '最新擔保品編號';
comment on column "ClNoMap"."MainLgtSeq" is '最新擔保品序號';
comment on column "ClNoMap"."ClCode1" is '新擔保品代號1';
comment on column "ClNoMap"."ClCode2" is '新擔保品代號2';
comment on column "ClNoMap"."ClNo" is '新擔保品編號';
comment on column "ClNoMap"."TfStatus" is '轉換結果';
comment on column "ClNoMap"."CreateDate" is '建檔日期時間';
comment on column "ClNoMap"."CreateEmpNo" is '建檔人員';
comment on column "ClNoMap"."LastUpdate" is '最後更新日期時間';
comment on column "ClNoMap"."LastUpdateEmpNo" is '最後更新人員';
