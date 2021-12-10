drop table "RptSubCom" purge;

create table "RptSubCom" (
  "CusSCD" decimal(4, 0) default 0 not null,
  "CusSName" nvarchar2(100),
  "CusSMark" nvarchar2(100),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "RptSubCom" add constraint "RptSubCom_PK" primary key("CusSCD");

comment on table "RptSubCom" is '報表用公司代碼檔';
comment on column "RptSubCom"."CusSCD" is '公司代碼';
comment on column "RptSubCom"."CusSName" is '公司名稱';
comment on column "RptSubCom"."CusSMark" is '備註';
comment on column "RptSubCom"."CreateDate" is '建檔日期時間';
comment on column "RptSubCom"."CreateEmpNo" is '建檔人員';
comment on column "RptSubCom"."LastUpdate" is '最後更新日期時間';
comment on column "RptSubCom"."LastUpdateEmpNo" is '最後更新人員';
