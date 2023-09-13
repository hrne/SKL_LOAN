drop table "MonthlyLM055AssetLoss" purge;

create table "MonthlyLM055AssetLoss" (
  "YearMonth" decimal(6, 0) default 0 not null,
  "LoanType" varchar2(1),
  "LoanItem" varchar2(1),
  "LoanBal" decimal(16, 2) default 0 not null,
  "GovProjectAdjustAmt" decimal(16, 2) default 0 not null,
  "LoanAmount1" decimal(16, 2) default 0 not null,
  "LoanAmount2" decimal(16, 2) default 0 not null,
  "LoanAmount3" decimal(16, 2) default 0 not null,
  "LoanAmount4" decimal(16, 2) default 0 not null,
  "LoanAmount5" decimal(16, 2) default 0 not null,
  "LoanAmount6" decimal(16, 2) default 0 not null,
  "LoanAmountNeg0" decimal(16, 2) default 0 not null,
  "LoanAmountNor0" decimal(16, 2) default 0 not null,
  "ReserveLossAmt1" decimal(16, 2) default 0 not null,
  "ReserveLossAmt2" decimal(16, 2) default 0 not null,
  "ReserveLossAmt3" decimal(16, 2) default 0 not null,
  "ReserveLossAmt4" decimal(16, 2) default 0 not null,
  "ReserveLossAmt5" decimal(16, 2) default 0 not null,
  "IFRS9AdjustAmt" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "MonthlyLM055AssetLoss" add constraint "MonthlyLM055AssetLoss_PK" primary key("YearMonth", "LoanType", "LoanItem");

comment on table "MonthlyLM055AssetLoss" is 'LM055重要放款餘額明細表';
comment on column "MonthlyLM055AssetLoss"."YearMonth" is '資料年月';
comment on column "MonthlyLM055AssetLoss"."LoanType" is '放款種類';
comment on column "MonthlyLM055AssetLoss"."LoanItem" is '放款項目';
comment on column "MonthlyLM055AssetLoss"."LoanBal" is '放款餘額';
comment on column "MonthlyLM055AssetLoss"."GovProjectAdjustAmt" is '政策性專案貸款調整數';
comment on column "MonthlyLM055AssetLoss"."LoanAmount1" is '逾期1放款金額';
comment on column "MonthlyLM055AssetLoss"."LoanAmount2" is '逾期2放款金額';
comment on column "MonthlyLM055AssetLoss"."LoanAmount3" is '逾期3放款金額';
comment on column "MonthlyLM055AssetLoss"."LoanAmount4" is '逾期4放款金額';
comment on column "MonthlyLM055AssetLoss"."LoanAmount5" is '逾期5放款金額';
comment on column "MonthlyLM055AssetLoss"."LoanAmount6" is '逾期6放款金額';
comment on column "MonthlyLM055AssetLoss"."LoanAmountNeg0" is '協議逾期數0放款金額';
comment on column "MonthlyLM055AssetLoss"."LoanAmountNor0" is '正常逾期數0放款金額';
comment on column "MonthlyLM055AssetLoss"."ReserveLossAmt1" is '備呆金額五分類1';
comment on column "MonthlyLM055AssetLoss"."ReserveLossAmt2" is '備呆金額五分類2';
comment on column "MonthlyLM055AssetLoss"."ReserveLossAmt3" is '備呆金額五分類3';
comment on column "MonthlyLM055AssetLoss"."ReserveLossAmt4" is '備呆金額五分類4';
comment on column "MonthlyLM055AssetLoss"."ReserveLossAmt5" is '備呆金額五分類5';
comment on column "MonthlyLM055AssetLoss"."IFRS9AdjustAmt" is 'IFRS9增提金額(含應收利息)';
comment on column "MonthlyLM055AssetLoss"."CreateDate" is '建檔日期時間';
comment on column "MonthlyLM055AssetLoss"."CreateEmpNo" is '建檔人員';
comment on column "MonthlyLM055AssetLoss"."LastUpdate" is '最後更新日期時間';
comment on column "MonthlyLM055AssetLoss"."LastUpdateEmpNo" is '最後更新人員';
