drop table "MonthlyLM052Ovdu" purge;

create table "MonthlyLM052Ovdu" (
  "YearMonth" decimal(6, 0) default 0 not null,
  "OvduNo" varchar2(1),
  "AcctCode" varchar2(3),
  "LoanBal" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "MonthlyLM052Ovdu" add constraint "MonthlyLM052Ovdu_PK" primary key("YearMonth", "OvduNo", "AcctCode");

comment on table "MonthlyLM052Ovdu" is 'LM052逾期分類表';
comment on column "MonthlyLM052Ovdu"."YearMonth" is '資料年月';
comment on column "MonthlyLM052Ovdu"."OvduNo" is '逾期期數代號';
comment on column "MonthlyLM052Ovdu"."AcctCode" is '業務科目';
comment on column "MonthlyLM052Ovdu"."LoanBal" is '放款餘額';
comment on column "MonthlyLM052Ovdu"."CreateDate" is '建檔日期時間';
comment on column "MonthlyLM052Ovdu"."CreateEmpNo" is '建檔人員';
comment on column "MonthlyLM052Ovdu"."LastUpdate" is '最後更新日期時間';
comment on column "MonthlyLM052Ovdu"."LastUpdateEmpNo" is '最後更新人員';
