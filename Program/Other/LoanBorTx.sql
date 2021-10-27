drop table "LoanBorTx" purge;

create table "LoanBorTx" (
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "BorxNo" decimal(4, 0) default 0 not null,
  "TitaCalDy" decimal(8, 0) default 0 not null,
  "TitaCalTm" decimal(8, 0) default 0 not null,
  "TitaKinBr" varchar2(4),
  "TitaTlrNo" varchar2(6),
  "TitaTxtNo" varchar2(8),
  "TitaTxCd" varchar2(5),
  "TitaCrDb" varchar2(1),
  "TitaHCode" varchar2(1),
  "TitaCurCd" varchar2(3),
  "TitaEmpNoS" varchar2(6),
  "RepayCode" decimal(2, 0) default 0 not null,
  "Desc" nvarchar2(15),
  "AcDate" decimal(8, 0) default 0 not null,
  "CorrectSeq" varchar2(26),
  "Displayflag" varchar2(1),
  "EntryDate" decimal(8, 0) default 0 not null,
  "DueDate" decimal(8, 0) default 0 not null,
  "TxAmt" decimal(16, 2) default 0 not null,
  "LoanBal" decimal(16, 2) default 0 not null,
  "IntStartDate" decimal(8, 0) default 0 not null,
  "IntEndDate" decimal(8, 0) default 0 not null,
  "RepaidPeriod" decimal(3, 0) default 0 not null,
  "Rate" decimal(6, 4) default 0 not null,
  "Principal" decimal(16, 2) default 0 not null,
  "Interest" decimal(16, 2) default 0 not null,
  "DelayInt" decimal(16, 2) default 0 not null,
  "BreachAmt" decimal(16, 2) default 0 not null,
  "CloseBreachAmt" decimal(16, 2) default 0 not null,
  "TempAmt" decimal(16, 2) default 0 not null,
  "ExtraRepay" decimal(16, 2) default 0 not null,
  "UnpaidInterest" decimal(16, 2) default 0 not null,
  "UnpaidPrincipal" decimal(16, 2) default 0 not null,
  "UnpaidCloseBreach" decimal(16, 2) default 0 not null,
  "Shortfall" decimal(16, 2) default 0 not null,
  "Overflow" decimal(16, 2) default 0 not null,
  "OtherFields" varchar2(2000),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanBorTx" add constraint "LoanBorTx_PK" primary key("CustNo", "FacmNo", "BormNo", "BorxNo");

comment on table "LoanBorTx" is '放款交易內容檔';
comment on column "LoanBorTx"."CustNo" is '借款人戶號';
comment on column "LoanBorTx"."FacmNo" is '額度編號';
comment on column "LoanBorTx"."BormNo" is '撥款序號';
comment on column "LoanBorTx"."BorxNo" is '交易內容檔序號';
comment on column "LoanBorTx"."TitaCalDy" is '交易日期';
comment on column "LoanBorTx"."TitaCalTm" is '交易時間';
comment on column "LoanBorTx"."TitaKinBr" is '單位別';
comment on column "LoanBorTx"."TitaTlrNo" is '經辦';
comment on column "LoanBorTx"."TitaTxtNo" is '交易序號';
comment on column "LoanBorTx"."TitaTxCd" is '交易代號';
comment on column "LoanBorTx"."TitaCrDb" is '借貸別';
comment on column "LoanBorTx"."TitaHCode" is '訂正別';
comment on column "LoanBorTx"."TitaCurCd" is '幣別';
comment on column "LoanBorTx"."TitaEmpNoS" is '主管編號';
comment on column "LoanBorTx"."RepayCode" is '還款來源';
comment on column "LoanBorTx"."Desc" is '摘要';
comment on column "LoanBorTx"."AcDate" is '會計日期';
comment on column "LoanBorTx"."CorrectSeq" is '更正序號, 原交易序號';
comment on column "LoanBorTx"."Displayflag" is '查詢時顯示否';
comment on column "LoanBorTx"."EntryDate" is '入帳日期';
comment on column "LoanBorTx"."DueDate" is '應繳日期';
comment on column "LoanBorTx"."TxAmt" is '交易金額';
comment on column "LoanBorTx"."LoanBal" is '放款餘額';
comment on column "LoanBorTx"."IntStartDate" is '計息起日';
comment on column "LoanBorTx"."IntEndDate" is '計息迄日';
comment on column "LoanBorTx"."RepaidPeriod" is '回收期數';
comment on column "LoanBorTx"."Rate" is '利率';
comment on column "LoanBorTx"."Principal" is '實收本金';
comment on column "LoanBorTx"."Interest" is '實收利息';
comment on column "LoanBorTx"."DelayInt" is '實收延滯息';
comment on column "LoanBorTx"."BreachAmt" is '實收違約金';
comment on column "LoanBorTx"."CloseBreachAmt" is '實收清償違約金';
comment on column "LoanBorTx"."TempAmt" is '暫收款金額';
comment on column "LoanBorTx"."ExtraRepay" is '提前償還本金';
comment on column "LoanBorTx"."UnpaidInterest" is '短繳利息';
comment on column "LoanBorTx"."UnpaidPrincipal" is '短繳本金';
comment on column "LoanBorTx"."UnpaidCloseBreach" is '短繳清償違約金';
comment on column "LoanBorTx"."Shortfall" is '短收金額';
comment on column "LoanBorTx"."Overflow" is '溢收金額';
comment on column "LoanBorTx"."OtherFields" is '其他欄位';
comment on column "LoanBorTx"."CreateDate" is '建檔日期時間';
comment on column "LoanBorTx"."CreateEmpNo" is '建檔人員';
comment on column "LoanBorTx"."LastUpdate" is '最後更新日期時間';
comment on column "LoanBorTx"."LastUpdateEmpNo" is '最後更新人員';
