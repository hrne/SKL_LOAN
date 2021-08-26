drop table "JcicMonthlyLoanData" purge;

create table "JcicMonthlyLoanData" (
  "DataYM" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "CustId" varchar2(10),
  "Status" decimal(2, 0) default 0 not null,
  "EntCode" varchar2(1),
  "SuvId" varchar2(10),
  "OverseasId" varchar2(10),
  "IndustryCode" varchar2(6),
  "AcctCode" varchar2(3),
  "SubAcctCode" varchar2(1),
  "OrigAcctCode" varchar2(3),
  "UtilAmt" decimal(16, 2) default 0 not null,
  "UtilBal" decimal(16, 2) default 0 not null,
  "RecycleCode" varchar2(1),
  "RecycleDeadline" decimal(8, 0) default 0 not null,
  "IrrevocableFlag" varchar2(1),
  "FinCode" varchar2(1),
  "ProjCode" varchar2(2),
  "NonCreditCode" varchar2(1),
  "UsageCode" varchar2(2),
  "ApproveRate" decimal(6, 4) default 0 not null,
  "StoreRate" decimal(6, 4) default 0 not null,
  "DrawdownDate" decimal(8, 0) default 0 not null,
  "MaturityDate" decimal(8, 0) default 0 not null,
  "AmortizedCode" varchar2(1),
  "CurrencyCode" varchar2(3),
  "DrawdownAmt" decimal(16, 2) default 0 not null,
  "LoanBal" decimal(16, 2) default 0 not null,
  "PrevAmt" decimal(16, 2) default 0 not null,
  "IntAmt" decimal(16, 2) default 0 not null,
  "PrevAmtRcv" decimal(16, 2) default 0 not null,
  "IntAmtRcv" decimal(16, 2) default 0 not null,
  "FeeAmtRcv" decimal(16, 2) default 0 not null,
  "PrevPayIntDate" decimal(8, 0) default 0 not null,
  "PrevRepaidDate" decimal(8, 0) default 0 not null,
  "NextPayIntDate" decimal(8, 0) default 0 not null,
  "NextRepayDate" decimal(8, 0) default 0 not null,
  "IntDelayMon" decimal(3, 0) default 0 not null,
  "RepayDelayMon" decimal(3, 0) default 0 not null,
  "RepaidEndMon" decimal(3, 0) default 0 not null,
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "ClTypeCode" varchar2(3),
  "ClType" varchar2(1),
  "EvaAmt" decimal(16, 2) default 0 not null,
  "DispDate" decimal(8, 0) default 0 not null,
  "SyndNo" decimal(3, 0) default 0 not null,
  "SyndCode" varchar2(1),
  "SigningDate" decimal(8, 0) default 0 not null,
  "SyndAmt" decimal(16, 2) default 0 not null,
  "PartAmt" decimal(16, 2) default 0 not null,
  "OvduDate" decimal(8, 0) default 0 not null,
  "BadDebtDate" decimal(8, 0) default 0 not null,
  "BadDebtSkipFg" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "JcicMonthlyLoanData" add constraint "JcicMonthlyLoanData_PK" primary key("DataYM", "CustNo", "FacmNo", "BormNo");

comment on table "JcicMonthlyLoanData" is '聯徵放款月報資料檔';
comment on column "JcicMonthlyLoanData"."DataYM" is '資料年月';
comment on column "JcicMonthlyLoanData"."CustNo" is '戶號';
comment on column "JcicMonthlyLoanData"."FacmNo" is '額度編號';
comment on column "JcicMonthlyLoanData"."BormNo" is '撥款序號';
comment on column "JcicMonthlyLoanData"."CustId" is '借款人ID / 統編';
comment on column "JcicMonthlyLoanData"."Status" is '戶況';
comment on column "JcicMonthlyLoanData"."EntCode" is '企金別';
comment on column "JcicMonthlyLoanData"."SuvId" is '負責人IDN/負責之事業體BAN';
comment on column "JcicMonthlyLoanData"."OverseasId" is '外僑兼具中華民國國籍IDN';
comment on column "JcicMonthlyLoanData"."IndustryCode" is '授信戶行業別';
comment on column "JcicMonthlyLoanData"."AcctCode" is '科目別';
comment on column "JcicMonthlyLoanData"."SubAcctCode" is '科目別註記';
comment on column "JcicMonthlyLoanData"."OrigAcctCode" is '轉催收款(或呆帳)前原科目別';
comment on column "JcicMonthlyLoanData"."UtilAmt" is '(額度)貸出金額(放款餘額)';
comment on column "JcicMonthlyLoanData"."UtilBal" is '(額度)已動用額度餘額';
comment on column "JcicMonthlyLoanData"."RecycleCode" is '循環動用';
comment on column "JcicMonthlyLoanData"."RecycleDeadline" is '循環動用期限';
comment on column "JcicMonthlyLoanData"."IrrevocableFlag" is '不可撤銷';
comment on column "JcicMonthlyLoanData"."FinCode" is '融資分類';
comment on column "JcicMonthlyLoanData"."ProjCode" is '政府專業補助貸款分類';
comment on column "JcicMonthlyLoanData"."NonCreditCode" is '不計入授信項目';
comment on column "JcicMonthlyLoanData"."UsageCode" is '用途別';
comment on column "JcicMonthlyLoanData"."ApproveRate" is '本筆撥款利率';
comment on column "JcicMonthlyLoanData"."StoreRate" is '計息利率';
comment on column "JcicMonthlyLoanData"."DrawdownDate" is '撥款日期';
comment on column "JcicMonthlyLoanData"."MaturityDate" is '到期日';
comment on column "JcicMonthlyLoanData"."AmortizedCode" is '攤還方式';
comment on column "JcicMonthlyLoanData"."CurrencyCode" is '幣別';
comment on column "JcicMonthlyLoanData"."DrawdownAmt" is '撥款金額';
comment on column "JcicMonthlyLoanData"."LoanBal" is '放款餘額';
comment on column "JcicMonthlyLoanData"."PrevAmt" is '本月應收本金';
comment on column "JcicMonthlyLoanData"."IntAmt" is '本月應收利息';
comment on column "JcicMonthlyLoanData"."PrevAmtRcv" is '本月實收本金';
comment on column "JcicMonthlyLoanData"."IntAmtRcv" is '本月實收利息';
comment on column "JcicMonthlyLoanData"."FeeAmtRcv" is '本月收取費用';
comment on column "JcicMonthlyLoanData"."PrevPayIntDate" is '上次繳息日';
comment on column "JcicMonthlyLoanData"."PrevRepaidDate" is '上次還本日';
comment on column "JcicMonthlyLoanData"."NextPayIntDate" is '下次繳息日';
comment on column "JcicMonthlyLoanData"."NextRepayDate" is '下次還本日';
comment on column "JcicMonthlyLoanData"."IntDelayMon" is '利息逾期月數';
comment on column "JcicMonthlyLoanData"."RepayDelayMon" is '本金逾期月數';
comment on column "JcicMonthlyLoanData"."RepaidEndMon" is '本金逾到期日(清償期)月數';
comment on column "JcicMonthlyLoanData"."ClCode1" is '主要擔保品代號1';
comment on column "JcicMonthlyLoanData"."ClCode2" is '主要擔保品代號2';
comment on column "JcicMonthlyLoanData"."ClNo" is '主要擔保品編號';
comment on column "JcicMonthlyLoanData"."ClTypeCode" is '主要擔保品類別代碼';
comment on column "JcicMonthlyLoanData"."ClType" is '擔保品組合型態';
comment on column "JcicMonthlyLoanData"."EvaAmt" is '鑑估總值';
comment on column "JcicMonthlyLoanData"."DispDate" is '擔保品處分日期';
comment on column "JcicMonthlyLoanData"."SyndNo" is '聯貸案序號';
comment on column "JcicMonthlyLoanData"."SyndCode" is '聯貸案類型';
comment on column "JcicMonthlyLoanData"."SigningDate" is '聯貸合約訂定日期';
comment on column "JcicMonthlyLoanData"."SyndAmt" is '聯貸總金額';
comment on column "JcicMonthlyLoanData"."PartAmt" is '參貸金額';
comment on column "JcicMonthlyLoanData"."OvduDate" is '轉催收日期';
comment on column "JcicMonthlyLoanData"."BadDebtDate" is '轉呆帳日期';
comment on column "JcicMonthlyLoanData"."BadDebtSkipFg" is '不報送呆帳記號';
comment on column "JcicMonthlyLoanData"."CreateDate" is '建檔日期時間';
comment on column "JcicMonthlyLoanData"."CreateEmpNo" is '建檔人員';
comment on column "JcicMonthlyLoanData"."LastUpdate" is '最後更新日期時間';
comment on column "JcicMonthlyLoanData"."LastUpdateEmpNo" is '最後更新人員';
