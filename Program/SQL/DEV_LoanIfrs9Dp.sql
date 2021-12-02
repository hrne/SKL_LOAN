drop table "LoanIfrs9Dp" purge;

create table "LoanIfrs9Dp" (
  "DataYM" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "CustId" varchar2(10),
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "DataFg" decimal(1, 0) default 0 not null,
  "AcCode" varchar2(11),
  "Status" decimal(1, 0) default 0 not null,
  "FirstDrawdownDate" decimal(8, 0) default 0 not null,
  "DrawdownDate" decimal(8, 0) default 0 not null,
  "MaturityDate" decimal(8, 0) default 0 not null,
  "LineAmt" decimal(16, 2) default 0 not null,
  "DrawdownAmt" decimal(16, 2) default 0 not null,
  "LoanBal" decimal(16, 2) default 0 not null,
  "IntAmt" decimal(16, 2) default 0 not null,
  "Fee" decimal(16, 2) default 0 not null,
  "OvduDays" decimal(3, 0) default 0 not null,
  "OvduDate" decimal(8, 0) default 0 not null,
  "BadDebtDate" decimal(8, 0) default 0 not null,
  "BadDebtAmt" decimal(16, 2) default 0 not null,
  "DerDate" decimal(8, 0) default 0 not null,
  "DerRate" decimal(6, 4) default 0 not null,
  "DerLoanBal" decimal(16, 2) default 0 not null,
  "DerIntAmt" decimal(16, 2) default 0 not null,
  "DerFee" decimal(16, 2) default 0 not null,
  "DerY1Amt" decimal(16, 2) default 0 not null,
  "DerY2Amt" decimal(16, 2) default 0 not null,
  "DerY3Amt" decimal(16, 2) default 0 not null,
  "DerY4Amt" decimal(16, 2) default 0 not null,
  "DerY5Amt" decimal(16, 2) default 0 not null,
  "DerY1Int" decimal(16, 2) default 0 not null,
  "DerY2Int" decimal(16, 2) default 0 not null,
  "DerY3Int" decimal(16, 2) default 0 not null,
  "DerY4Int" decimal(16, 2) default 0 not null,
  "DerY5Int" decimal(16, 2) default 0 not null,
  "DerY1Fee" decimal(16, 2) default 0 not null,
  "DerY2Fee" decimal(16, 2) default 0 not null,
  "DerY3Fee" decimal(16, 2) default 0 not null,
  "DerY4Fee" decimal(16, 2) default 0 not null,
  "DerY5Fee" decimal(16, 2) default 0 not null,
  "IndustryCode" varchar2(6),
  "ClTypeJCIC" varchar2(2),
  "AreaCode" varchar2(3),
  "ProdCode" varchar2(5),
  "CustKind" decimal(1, 0) default 0 not null,
  "Ifrs9ProdCode" varchar2(2),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanIfrs9Dp" add constraint "LoanIfrs9Dp_PK" primary key("DataYM", "CustNo", "FacmNo", "BormNo");

comment on table "LoanIfrs9Dp" is 'IFRS9欄位清單4';
comment on column "LoanIfrs9Dp"."DataYM" is '年月份';
comment on column "LoanIfrs9Dp"."CustNo" is '戶號';
comment on column "LoanIfrs9Dp"."CustId" is '借款人ID / 統編';
comment on column "LoanIfrs9Dp"."FacmNo" is '額度編號';
comment on column "LoanIfrs9Dp"."BormNo" is '撥款序號';
comment on column "LoanIfrs9Dp"."DataFg" is '資料類別';
comment on column "LoanIfrs9Dp"."AcCode" is '會計科目';
comment on column "LoanIfrs9Dp"."Status" is '案件狀態';
comment on column "LoanIfrs9Dp"."FirstDrawdownDate" is '初貸日期';
comment on column "LoanIfrs9Dp"."DrawdownDate" is '貸放日期';
comment on column "LoanIfrs9Dp"."MaturityDate" is '到期日';
comment on column "LoanIfrs9Dp"."LineAmt" is '核准金額(台幣)';
comment on column "LoanIfrs9Dp"."DrawdownAmt" is '撥款金額(台幣)';
comment on column "LoanIfrs9Dp"."LoanBal" is '本金餘額(撥款)(台幣)';
comment on column "LoanIfrs9Dp"."IntAmt" is '應收利息(台幣)';
comment on column "LoanIfrs9Dp"."Fee" is '法拍及火險費用(台幣)';
comment on column "LoanIfrs9Dp"."OvduDays" is '逾期繳款天數';
comment on column "LoanIfrs9Dp"."OvduDate" is '轉催收款日期';
comment on column "LoanIfrs9Dp"."BadDebtDate" is '轉銷呆帳日期';
comment on column "LoanIfrs9Dp"."BadDebtAmt" is '轉銷呆帳金額';
comment on column "LoanIfrs9Dp"."DerDate" is 'stage3發生日期';
comment on column "LoanIfrs9Dp"."DerRate" is '上述發生日期前之最近一次利率';
comment on column "LoanIfrs9Dp"."DerLoanBal" is '上述發生日期時之本金餘額(台幣)';
comment on column "LoanIfrs9Dp"."DerIntAmt" is '上述發生日期時之應收利息(台幣)';
comment on column "LoanIfrs9Dp"."DerFee" is '上述發生日期時之法拍及火險費用(台幣)';
comment on column "LoanIfrs9Dp"."DerY1Amt" is 'stage3發生後第一年本金回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY2Amt" is 'stage3發生後第二年本金回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY3Amt" is 'stage3發生後第三年本金回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY4Amt" is 'stage3發生後第四年本金回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY5Amt" is 'stage3發生後第五年本金回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY1Int" is 'stage3發生後第一年應收利息回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY2Int" is 'stage3發生後第二年應收利息回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY3Int" is 'stage3發生後第三年應收利息回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY4Int" is 'stage3發生後第四年應收利息回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY5Int" is 'stage3發生後第五年應收利息回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY1Fee" is 'stage3發生後第一年法拍及火險費用回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY2Fee" is 'stage3發生後第二年法拍及火險費用回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY3Fee" is 'stage3發生後第三年法拍及火險費用回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY4Fee" is 'stage3發生後第四年法拍及火險費用回收金額(台幣)';
comment on column "LoanIfrs9Dp"."DerY5Fee" is 'stage3發生後第五年法拍及火險費用回收金額(台幣)';
comment on column "LoanIfrs9Dp"."IndustryCode" is '授信行業別';
comment on column "LoanIfrs9Dp"."ClTypeJCIC" is '擔保品類別';
comment on column "LoanIfrs9Dp"."AreaCode" is '擔保品地區別';
comment on column "LoanIfrs9Dp"."ProdCode" is '商品利率代碼';
comment on column "LoanIfrs9Dp"."CustKind" is '企業戶/個人戶';
comment on column "LoanIfrs9Dp"."Ifrs9ProdCode" is '產品別';
comment on column "LoanIfrs9Dp"."CreateDate" is '建檔日期時間';
comment on column "LoanIfrs9Dp"."CreateEmpNo" is '建檔人員';
comment on column "LoanIfrs9Dp"."LastUpdate" is '最後更新日期時間';
comment on column "LoanIfrs9Dp"."LastUpdateEmpNo" is '最後更新人員';
