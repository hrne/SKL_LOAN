drop table "MonthlyLM042RBC" purge;

create table "MonthlyLM042RBC" (
  "YearMonth" decimal(6, 0) default 0 not null,
  "LoanType" varchar2(1),
  "LoanItem" varchar2(1),
  "RelatedCode" varchar2(1),
  "LoanAmount" decimal(16, 2) default 0 not null,
  "RiskFactor" decimal(6, 4) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "MonthlyLM042RBC" add constraint "MonthlyLM042RBC_PK" primary key("YearMonth", "LoanType", "LoanItem", "RelatedCode");

comment on table "MonthlyLM042RBC" is 'LM042RBC會計報表';
comment on column "MonthlyLM042RBC"."YearMonth" is '資料年月';
comment on column "MonthlyLM042RBC"."LoanType" is '放款種類';
comment on column "MonthlyLM042RBC"."LoanItem" is '放款項目';
comment on column "MonthlyLM042RBC"."RelatedCode" is '對象關係人';
comment on column "MonthlyLM042RBC"."LoanAmount" is '放款金額';
comment on column "MonthlyLM042RBC"."RiskFactor" is '風險係數';
comment on column "MonthlyLM042RBC"."CreateDate" is '建檔日期時間';
comment on column "MonthlyLM042RBC"."CreateEmpNo" is '建檔人員';
comment on column "MonthlyLM042RBC"."LastUpdate" is '最後更新日期時間';
comment on column "MonthlyLM042RBC"."LastUpdateEmpNo" is '最後更新人員';
