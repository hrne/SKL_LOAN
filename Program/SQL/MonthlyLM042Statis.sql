drop table "MonthlyLM042Statis" purge;

create table "MonthlyLM042Statis" (
  "YearMonth" decimal(6, 0) default 0 not null,
  "LoanItem" varchar2(1),
  "RelatedCode" varchar2(1),
  "AssetClass" varchar2(1),
  "LoanBal" decimal(16, 2) default 0 not null,
  "ReserveLossAmt" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "MonthlyLM042Statis" add constraint "MonthlyLM042Statis_PK" primary key("YearMonth", "LoanItem", "RelatedCode", "AssetClass");

comment on table "MonthlyLM042Statis" is 'LM042RBC統計數';
comment on column "MonthlyLM042Statis"."YearMonth" is '資料年月';
comment on column "MonthlyLM042Statis"."LoanItem" is '放款項目';
comment on column "MonthlyLM042Statis"."RelatedCode" is '是否為利害關係人';
comment on column "MonthlyLM042Statis"."AssetClass" is '資產五分類代號';
comment on column "MonthlyLM042Statis"."LoanBal" is '放款餘額';
comment on column "MonthlyLM042Statis"."ReserveLossAmt" is '備呆金額';
comment on column "MonthlyLM042Statis"."CreateDate" is '建檔日期時間';
comment on column "MonthlyLM042Statis"."CreateEmpNo" is '建檔人員';
comment on column "MonthlyLM042Statis"."LastUpdate" is '最後更新日期時間';
comment on column "MonthlyLM042Statis"."LastUpdateEmpNo" is '最後更新人員';
