drop table "LoanIfrs9Ap" purge;

create table "LoanIfrs9Ap" (
  "DataYM" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "CustId" varchar2(10),
  "FacmNo" decimal(3, 0) default 0 not null,
  "ApplNo" decimal(7, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "AcCode" varchar2(11),
  "Status" decimal(1, 0) default 0 not null,
  "FirstDrawdownDate" decimal(8, 0) default 0 not null,
  "DrawdownDate" decimal(8, 0) default 0 not null,
  "FacLineDate" decimal(8, 0) default 0 not null,
  "MaturityDate" decimal(8, 0) default 0 not null,
  "LineAmt" decimal(16, 2) default 0 not null,
  "DrawdownAmt" decimal(16, 2) default 0 not null,
  "AcctFee" decimal(16, 2) default 0 not null,
  "LoanBal" decimal(16, 2) default 0 not null,
  "IntAmt" decimal(16, 2) default 0 not null,
  "Fee" decimal(16, 2) default 0 not null,
  "Rate" decimal(8, 6) default 0 not null,
  "OvduDays" decimal(3, 0) default 0 not null,
  "OvduDate" decimal(8, 0) default 0 not null,
  "BadDebtDate" decimal(8, 0) default 0 not null,
  "BadDebtAmt" decimal(16, 2) default 0 not null,
  "GracePeriod" decimal(3, 0) default 0 not null,
  "ApproveRate" decimal(8, 6) default 0 not null,
  "AmortizedCode" varchar2(1),
  "RateCode" varchar2(1),
  "RepayFreq" decimal(2, 0) default 0 not null,
  "PayIntFreq" decimal(2, 0) default 0 not null,
  "IndustryCode" varchar2(6),
  "ClTypeJCIC" varchar2(2),
  "CityCode" varchar2(3),
  "ProdNo" varchar2(5),
  "CustKind" decimal(1, 0) default 0 not null,
  "AssetClass" decimal(1, 0) default 0 not null,
  "Ifrs9ProdCode" varchar2(2),
  "EvaAmt" decimal(16, 2) default 0 not null,
  "FirstDueDate" decimal(8, 0) default 0 not null,
  "TotalPeriod" decimal(3, 0) default 0 not null,
  "AvblBal" decimal(16, 2) default 0 not null,
  "RecycleCode" varchar2(1),
  "IrrevocableFlag" varchar2(1),
  "TempAmt" decimal(16, 2) default 0 not null,
  "AcCurcd" decimal(1, 0) default 0 not null,
  "AcBookCode" varchar2(1),
  "CurrencyCode" varchar2(4),
  "ExchangeRate" decimal(8, 5) default 0 not null,
  "LineAmtCurr" decimal(16, 2) default 0 not null,
  "DrawdownAmtCurr" decimal(16, 2) default 0 not null,
  "AcctFeeCurr" decimal(16, 2) default 0 not null,
  "LoanBalCurr" decimal(16, 2) default 0 not null,
  "IntAmtCurr" decimal(16, 2) default 0 not null,
  "FeeCurr" decimal(16, 2) default 0 not null,
  "AvblBalCurr" decimal(16, 2) default 0 not null,
  "TempAmtCurr" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanIfrs9Ap" add constraint "LoanIfrs9Ap_PK" primary key("DataYM", "CustNo", "FacmNo", "BormNo");

comment on table "LoanIfrs9Ap" is 'IFRS9欄位清單1';
comment on column "LoanIfrs9Ap"."DataYM" is '年月份';
comment on column "LoanIfrs9Ap"."CustNo" is '戶號';
comment on column "LoanIfrs9Ap"."CustId" is '借款人ID / 統編';
comment on column "LoanIfrs9Ap"."FacmNo" is '額度編號';
comment on column "LoanIfrs9Ap"."ApplNo" is '核准號碼';
comment on column "LoanIfrs9Ap"."BormNo" is '撥款序號';
comment on column "LoanIfrs9Ap"."AcCode" is '會計科目';
comment on column "LoanIfrs9Ap"."Status" is '戶況';
comment on column "LoanIfrs9Ap"."FirstDrawdownDate" is '初貸日期';
comment on column "LoanIfrs9Ap"."DrawdownDate" is '撥款日期';
comment on column "LoanIfrs9Ap"."FacLineDate" is '到期日(額度)';
comment on column "LoanIfrs9Ap"."MaturityDate" is '到期日(撥款)';
comment on column "LoanIfrs9Ap"."LineAmt" is '核准金額';
comment on column "LoanIfrs9Ap"."DrawdownAmt" is '撥款金額';
comment on column "LoanIfrs9Ap"."AcctFee" is '帳管費';
comment on column "LoanIfrs9Ap"."LoanBal" is '本金餘額(撥款)';
comment on column "LoanIfrs9Ap"."IntAmt" is '應收利息';
comment on column "LoanIfrs9Ap"."Fee" is '法拍及火險費用';
comment on column "LoanIfrs9Ap"."Rate" is '利率(撥款)';
comment on column "LoanIfrs9Ap"."OvduDays" is '逾期繳款天數';
comment on column "LoanIfrs9Ap"."OvduDate" is '轉催收款日期';
comment on column "LoanIfrs9Ap"."BadDebtDate" is '轉銷呆帳日期';
comment on column "LoanIfrs9Ap"."BadDebtAmt" is '轉銷呆帳金額';
comment on column "LoanIfrs9Ap"."GracePeriod" is '初貸時約定還本寬限期';
comment on column "LoanIfrs9Ap"."ApproveRate" is '核准利率';
comment on column "LoanIfrs9Ap"."AmortizedCode" is '契約當時還款方式';
comment on column "LoanIfrs9Ap"."RateCode" is '契約當時利率調整方式';
comment on column "LoanIfrs9Ap"."RepayFreq" is '契約約定當時還本週期';
comment on column "LoanIfrs9Ap"."PayIntFreq" is '契約約定當時繳息週期';
comment on column "LoanIfrs9Ap"."IndustryCode" is '授信行業別';
comment on column "LoanIfrs9Ap"."ClTypeJCIC" is '擔保品類別';
comment on column "LoanIfrs9Ap"."CityCode" is '擔保品地區別';
comment on column "LoanIfrs9Ap"."ProdNo" is '商品利率代碼';
comment on column "LoanIfrs9Ap"."CustKind" is '企業戶/個人戶';
comment on column "LoanIfrs9Ap"."AssetClass" is '五類資產分類';
comment on column "LoanIfrs9Ap"."Ifrs9ProdCode" is '產品別';
comment on column "LoanIfrs9Ap"."EvaAmt" is '原始鑑價金額';
comment on column "LoanIfrs9Ap"."FirstDueDate" is '首次應繳日';
comment on column "LoanIfrs9Ap"."TotalPeriod" is '總期數';
comment on column "LoanIfrs9Ap"."AvblBal" is '可動用餘額(台幣)';
comment on column "LoanIfrs9Ap"."RecycleCode" is '該筆額度是否可循環動用';
comment on column "LoanIfrs9Ap"."IrrevocableFlag" is '該筆額度是否為不可徹銷';
comment on column "LoanIfrs9Ap"."TempAmt" is '暫收款金額(台幣)';
comment on column "LoanIfrs9Ap"."AcCurcd" is '記帳幣別';
comment on column "LoanIfrs9Ap"."AcBookCode" is '會計帳冊';
comment on column "LoanIfrs9Ap"."CurrencyCode" is '交易幣別';
comment on column "LoanIfrs9Ap"."ExchangeRate" is '報導日匯率';
comment on column "LoanIfrs9Ap"."LineAmtCurr" is '核准金額(交易幣)';
comment on column "LoanIfrs9Ap"."DrawdownAmtCurr" is '撥款金額(交易幣)';
comment on column "LoanIfrs9Ap"."AcctFeeCurr" is '帳管費(交易幣)';
comment on column "LoanIfrs9Ap"."LoanBalCurr" is '本金餘額(撥款)(交易幣)';
comment on column "LoanIfrs9Ap"."IntAmtCurr" is '應收利息(交易幣)';
comment on column "LoanIfrs9Ap"."FeeCurr" is '法拍及火險費用(交易幣)';
comment on column "LoanIfrs9Ap"."AvblBalCurr" is '可動用餘額(交易幣)';
comment on column "LoanIfrs9Ap"."TempAmtCurr" is '暫收款金額(交易幣)';
comment on column "LoanIfrs9Ap"."CreateDate" is '建檔日期時間';
comment on column "LoanIfrs9Ap"."CreateEmpNo" is '建檔人員';
comment on column "LoanIfrs9Ap"."LastUpdate" is '最後更新日期時間';
comment on column "LoanIfrs9Ap"."LastUpdateEmpNo" is '最後更新人員';
