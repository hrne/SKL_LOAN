drop table "AcLoanIntCashFlow" purge;

create table "AcLoanIntCashFlow" (
  "YearMonth" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "TermNo" decimal(3, 0) default 0 not null,
  "IntStartDate" decimal(8, 0) default 0 not null,
  "IntEndDate" decimal(8, 0) default 0 not null,
  "Amount" decimal(16, 2) default 0 not null,
  "IntRate" decimal(6, 4) default 0 not null,
  "Principal" decimal(16, 2) default 0 not null,
  "Interest" decimal(16, 2) default 0 not null,
  "DelayInt" decimal(16, 2) default 0 not null,
  "BreachAmt" decimal(16, 2) default 0 not null,
  "IntCalcCode" varchar2(1),
  "AmortizedCode" varchar2(1),
  "AcctCode" varchar2(3),
  "PayIntDate" decimal(8, 0) default 0 not null,
  "LoanBal" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "AcLoanIntCashFlow" add constraint "AcLoanIntCashFlow_PK" primary key("YearMonth", "CustNo", "FacmNo", "BormNo", "TermNo");

comment on table "AcLoanIntCashFlow" is '現金流量預估明細檔';
comment on column "AcLoanIntCashFlow"."YearMonth" is '提息年月';
comment on column "AcLoanIntCashFlow"."CustNo" is '借款人戶號';
comment on column "AcLoanIntCashFlow"."FacmNo" is '額度編號';
comment on column "AcLoanIntCashFlow"."BormNo" is '撥款序號';
comment on column "AcLoanIntCashFlow"."TermNo" is '期數編號';
comment on column "AcLoanIntCashFlow"."IntStartDate" is '計息起日';
comment on column "AcLoanIntCashFlow"."IntEndDate" is '計息止日';
comment on column "AcLoanIntCashFlow"."Amount" is '計息本金';
comment on column "AcLoanIntCashFlow"."IntRate" is '計息利率';
comment on column "AcLoanIntCashFlow"."Principal" is '回收本金';
comment on column "AcLoanIntCashFlow"."Interest" is '利息';
comment on column "AcLoanIntCashFlow"."DelayInt" is '延滯息';
comment on column "AcLoanIntCashFlow"."BreachAmt" is '違約金';
comment on column "AcLoanIntCashFlow"."IntCalcCode" is '計息方式';
comment on column "AcLoanIntCashFlow"."AmortizedCode" is '攤還方式';
comment on column "AcLoanIntCashFlow"."AcctCode" is '業務科目代號';
comment on column "AcLoanIntCashFlow"."PayIntDate" is '應繳息日';
comment on column "AcLoanIntCashFlow"."LoanBal" is '放款餘額';
comment on column "AcLoanIntCashFlow"."CreateDate" is '建檔日期時間';
comment on column "AcLoanIntCashFlow"."CreateEmpNo" is '建檔人員';
comment on column "AcLoanIntCashFlow"."LastUpdate" is '最後更新日期時間';
comment on column "AcLoanIntCashFlow"."LastUpdateEmpNo" is '最後更新人員';
