drop table "FinReportQuality" purge;

create table "FinReportQuality" (
  "CustUKey" varchar2(32),
  "UKey" varchar2(32),
  "ReportType" varchar2(1),
  "Opinion" varchar2(1),
  "IsCheck" varchar2(1),
  "IsChange" varchar2(1),
  "OfficeType" varchar2(1),
  "PunishRecord" varchar2(1),
  "ChangeReason" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "FinReportQuality" add constraint "FinReportQuality_PK" primary key("CustUKey", "UKey");

comment on table "FinReportQuality" is '客戶財務報表.財報品質';
comment on column "FinReportQuality"."CustUKey" is '客戶識別碼';
comment on column "FinReportQuality"."UKey" is '識別碼';
comment on column "FinReportQuality"."ReportType" is '年度財務報表類型';
comment on column "FinReportQuality"."Opinion" is '會計師查核意見';
comment on column "FinReportQuality"."IsCheck" is '是否經會計師查核';
comment on column "FinReportQuality"."IsChange" is '近兩年是否曾換會計師';
comment on column "FinReportQuality"."OfficeType" is '會計師事務所類型';
comment on column "FinReportQuality"."PunishRecord" is '會計師懲戒紀錄';
comment on column "FinReportQuality"."ChangeReason" is '更換會計師原因';
comment on column "FinReportQuality"."CreateDate" is '建檔日期時間';
comment on column "FinReportQuality"."CreateEmpNo" is '建檔人員';
comment on column "FinReportQuality"."LastUpdate" is '最後更新日期時間';
comment on column "FinReportQuality"."LastUpdateEmpNo" is '最後更新人員';
