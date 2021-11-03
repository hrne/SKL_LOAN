drop table "MonthlyLM052LoanAsset" purge;

create table "MonthlyLM052LoanAsset" (
  "YearMonth" decimal(6, 0) default 0 not null,
  "LoanAssetCode" varchar2(3),
  "LoanBal" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "MonthlyLM052LoanAsset" add constraint "MonthlyLM052LoanAsset_PK" primary key("YearMonth", "LoanAssetCode");

comment on table "MonthlyLM052LoanAsset" is 'LM052放款資產表';
comment on column "MonthlyLM052LoanAsset"."YearMonth" is '資料年月';
comment on column "MonthlyLM052LoanAsset"."LoanAssetCode" is '放款資產項目';
comment on column "MonthlyLM052LoanAsset"."LoanBal" is '放款餘額';
comment on column "MonthlyLM052LoanAsset"."CreateDate" is '建檔日期時間';
comment on column "MonthlyLM052LoanAsset"."CreateEmpNo" is '建檔人員';
comment on column "MonthlyLM052LoanAsset"."LastUpdate" is '最後更新日期時間';
comment on column "MonthlyLM052LoanAsset"."LastUpdateEmpNo" is '最後更新人員';
