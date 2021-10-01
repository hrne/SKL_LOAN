drop table "MonthlyLM052AssetClass" purge;

create table "MonthlyLM052AssetClass" (
  "YearMonth" decimal(6, 0) default 0 not null,
  "AssetClassNo" varchar2(2),
  "AcSubBookCode" varchar2(3),
  "LoanBal" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "MonthlyLM052AssetClass" add constraint "MonthlyLM052AssetClass_PK" primary key("YearMonth", "AssetClassNo", "AcSubBookCode");

comment on table "MonthlyLM052AssetClass" is 'LM052資產分類表';
comment on column "MonthlyLM052AssetClass"."YearMonth" is '資料年月';
comment on column "MonthlyLM052AssetClass"."AssetClassNo" is '資產五分類';
comment on column "MonthlyLM052AssetClass"."AcSubBookCode" is '區隔帳冊';
comment on column "MonthlyLM052AssetClass"."LoanBal" is '放款餘額';
comment on column "MonthlyLM052AssetClass"."CreateDate" is '建檔日期時間';
comment on column "MonthlyLM052AssetClass"."CreateEmpNo" is '建檔人員';
comment on column "MonthlyLM052AssetClass"."LastUpdate" is '最後更新日期時間';
comment on column "MonthlyLM052AssetClass"."LastUpdateEmpNo" is '最後更新人員';
