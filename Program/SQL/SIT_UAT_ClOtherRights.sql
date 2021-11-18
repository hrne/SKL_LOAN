drop table "ClOtherRights" purge;

create table "ClOtherRights" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "Seq" varchar2(8),
  "City" varchar2(2),
  "OtherCity" varchar2(40),
  "LandAdm" varchar2(2),
  "OtherLandAdm" varchar2(40),
  "RecYear" decimal(3, 0) default 0 not null,
  "RecWord" varchar2(3),
  "OtherRecWord" varchar2(40),
  "RecNumber" varchar2(6),
  "RightsNote" varchar2(2),
  "SecuredTotal" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClOtherRights" add constraint "ClOtherRights_PK" primary key("ClCode1", "ClCode2", "ClNo", "Seq");

alter table "ClOtherRights" add constraint "ClOtherRights_ClMain_FK1" foreign key ("ClCode1", "ClCode2", "ClNo") references "ClMain" ("ClCode1", "ClCode2", "ClNo") on delete cascade;

create index "ClOtherRights_Index1" on "ClOtherRights"("ClCode1" asc);

create index "ClOtherRights_Index2" on "ClOtherRights"("ClCode1" asc, "ClCode2" asc);

comment on table "ClOtherRights" is '擔保品他項權利檔';
comment on column "ClOtherRights"."ClCode1" is '擔保品代號1';
comment on column "ClOtherRights"."ClCode2" is '擔保品代號2';
comment on column "ClOtherRights"."ClNo" is '擔保品編號';
comment on column "ClOtherRights"."Seq" is '他項權利序號';
comment on column "ClOtherRights"."City" is '縣市';
comment on column "ClOtherRights"."OtherCity" is '其他縣市';
comment on column "ClOtherRights"."LandAdm" is '地政';
comment on column "ClOtherRights"."OtherLandAdm" is '其他地政';
comment on column "ClOtherRights"."RecYear" is '收件年';
comment on column "ClOtherRights"."RecWord" is '收件字';
comment on column "ClOtherRights"."OtherRecWord" is '其他收件字';
comment on column "ClOtherRights"."RecNumber" is '收件號';
comment on column "ClOtherRights"."RightsNote" is '權利價值說明';
comment on column "ClOtherRights"."SecuredTotal" is '擔保債權總金額';
comment on column "ClOtherRights"."CreateDate" is '建檔日期時間';
comment on column "ClOtherRights"."CreateEmpNo" is '建檔人員';
comment on column "ClOtherRights"."LastUpdate" is '最後更新日期時間';
comment on column "ClOtherRights"."LastUpdateEmpNo" is '最後更新人員';
