drop table "CdLandOffice" purge;

create table "CdLandOffice" (
  "LandOfficeCode" varchar2(2),
  "RecWord" varchar2(3),
  "RecWordItem" nvarchar2(30),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdLandOffice" add constraint "CdLandOffice_PK" primary key("LandOfficeCode", "RecWord");

comment on table "CdLandOffice" is '地政收件字檔';
comment on column "CdLandOffice"."LandOfficeCode" is '地政所代碼';
comment on column "CdLandOffice"."RecWord" is '收件字代碼';
comment on column "CdLandOffice"."RecWordItem" is '收件字說明';
comment on column "CdLandOffice"."CreateDate" is '建檔日期時間';
comment on column "CdLandOffice"."CreateEmpNo" is '建檔人員';
comment on column "CdLandOffice"."LastUpdate" is '最後更新日期時間';
comment on column "CdLandOffice"."LastUpdateEmpNo" is '最後更新人員';
