drop table "FinReportCashFlow" purge;

create table "FinReportCashFlow" (
  "CustUKey" varchar2(32),
  "UKey" varchar2(32),
  "BusCash" decimal(18, 0) default 0 not null,
  "InvestCash" decimal(18, 0) default 0 not null,
  "FinCash" decimal(18, 0) default 0 not null,
  "AccountItem01" nvarchar2(20),
  "AccountItem02" nvarchar2(20),
  "AccountValue01" decimal(18, 0) default 0 not null,
  "AccountValue02" decimal(18, 0) default 0 not null,
  "EndCash" decimal(18, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "FinReportCashFlow" add constraint "FinReportCashFlow_PK" primary key("CustUKey", "UKey");

comment on table "FinReportCashFlow" is '客戶財務報表.現金流量表';
comment on column "FinReportCashFlow"."CustUKey" is '客戶識別碼';
comment on column "FinReportCashFlow"."UKey" is '識別碼';
comment on column "FinReportCashFlow"."BusCash" is '營業活動淨現金流入(出)';
comment on column "FinReportCashFlow"."InvestCash" is '投資活動淨現金流入(出)';
comment on column "FinReportCashFlow"."FinCash" is '理財活動淨現金流入(出)';
comment on column "FinReportCashFlow"."AccountItem01" is '會計科目01';
comment on column "FinReportCashFlow"."AccountItem02" is '會計科目02';
comment on column "FinReportCashFlow"."AccountValue01" is '會計科目值01';
comment on column "FinReportCashFlow"."AccountValue02" is '會計科目值02';
comment on column "FinReportCashFlow"."EndCash" is '期末現金餘額';
comment on column "FinReportCashFlow"."CreateDate" is '建檔日期時間';
comment on column "FinReportCashFlow"."CreateEmpNo" is '建檔人員';
comment on column "FinReportCashFlow"."LastUpdate" is '最後更新日期時間';
comment on column "FinReportCashFlow"."LastUpdateEmpNo" is '最後更新人員';
