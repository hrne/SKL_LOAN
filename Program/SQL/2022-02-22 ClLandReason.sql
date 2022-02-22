drop table "ClLandReason" purge;

create table "ClLandReason" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "Reason" decimal(1, 0) default 0 not null,
  "OtherReason" nvarchar2(60),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClLandReason" add constraint "ClLandReason_PK" primary key("ClCode1", "ClCode2", "ClNo");

create unique index "ClLandReason_Index1" on "ClLandReason"("ClCode1" asc, "ClCode2" asc, "ClNo" asc);

comment on table "ClLandReason" is '擔保品-土地修改原因檔';
comment on column "ClLandReason"."ClCode1" is '擔保品-代號1';
comment on column "ClLandReason"."ClCode2" is '擔保品-代號2';
comment on column "ClLandReason"."ClNo" is '擔保品編號';
comment on column "ClLandReason"."Reason" is '修改原因';
comment on column "ClLandReason"."OtherReason" is '其他原因';
comment on column "ClLandReason"."CreateDate" is '建檔日期時間';
comment on column "ClLandReason"."CreateEmpNo" is '建檔人員';
comment on column "ClLandReason"."LastUpdate" is '最後更新日期時間';
comment on column "ClLandReason"."LastUpdateEmpNo" is '最後更新人員';
