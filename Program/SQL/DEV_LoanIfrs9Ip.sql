drop table "LoanIfrs9Ip" purge;

create table "LoanIfrs9Ip" (
  "DataYM" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "CustId" varchar2(10),
  "FacmNo" decimal(3, 0) default 0 not null,
  "ApplNo" decimal(7, 0) default 0 not null,
  "DrawdownFg" decimal(1, 0) default 0 not null,
  "ApproveDate" decimal(8, 0) default 0 not null,
  "FirstDrawdownDate" decimal(8, 0) default 0 not null,
  "LineAmt" decimal(16, 2) default 0 not null,
  "AcctFee" decimal(16, 2) default 0 not null,
  "Fee" decimal(16, 2) default 0 not null,
  "ApproveRate" decimal(8, 6) default 0 not null,
  "GracePeriod" decimal(3, 0) default 0 not null,
  "AmortizedCode" varchar2(1),
  "RateCode" varchar2(1),
  "RepayFreq" decimal(2, 0) default 0 not null,
  "PayIntFreq" decimal(2, 0) default 0 not null,
  "IndustryCode" varchar2(6),
  "ClTypeJCIC" varchar2(2),
  "CityCode" varchar2(3),
  "ProdNo" varchar2(5),
  "CustKind" decimal(1, 0) default 0 not null,
  "Ifrs9ProdCode" varchar2(1),
  "EvaAmt" decimal(16, 2) default 0 not null,
  "AvblBal" decimal(16, 2) default 0 not null,
  "RecycleCode" varchar2(1),
  "IrrevocableFlag" varchar2(1),
  "LoanTerm" varchar2(8),
  "AcCode" varchar2(11),
  "AcCurcd" decimal(1, 0) default 0 not null,
  "AcBookCode" varchar2(1),
  "CurrencyCode" varchar2(4),
  "ExchangeRate" decimal(10, 8) default 0 not null,
  "LineAmtCurr" decimal(16, 2) default 0 not null,
  "AcctFeeCurr" decimal(16, 2) default 0 not null,
  "FeeCurr" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanIfrs9Ip" add constraint "LoanIfrs9Ip_PK" primary key("DataYM", "CustNo", "FacmNo");

comment on table "LoanIfrs9Ip" is 'IFRS9欄位清單9';
comment on column "LoanIfrs9Ip"."DataYM" is '年月份';
comment on column "LoanIfrs9Ip"."CustNo" is '戶號';
comment on column "LoanIfrs9Ip"."CustId" is '借款人ID / 統編';
comment on column "LoanIfrs9Ip"."FacmNo" is '額度編號';
comment on column "LoanIfrs9Ip"."ApplNo" is '核准號碼';
comment on column "LoanIfrs9Ip"."DrawdownFg" is '已核撥記號';
comment on column "LoanIfrs9Ip"."ApproveDate" is '核准日期';
comment on column "LoanIfrs9Ip"."FirstDrawdownDate" is '初貸日期';
comment on column "LoanIfrs9Ip"."LineAmt" is '核准金額';
comment on column "LoanIfrs9Ip"."AcctFee" is '帳管費';
comment on column "LoanIfrs9Ip"."Fee" is '法拍及火險費用';
comment on column "LoanIfrs9Ip"."ApproveRate" is '核准利率';
comment on column "LoanIfrs9Ip"."GracePeriod" is '初貸時約定還本寬限期';
comment on column "LoanIfrs9Ip"."AmortizedCode" is '契約當時還款方式';
comment on column "LoanIfrs9Ip"."RateCode" is '契約當時利率調整方式';
comment on column "LoanIfrs9Ip"."RepayFreq" is '契約約定當時還本週期';
comment on column "LoanIfrs9Ip"."PayIntFreq" is '契約約定當時繳息週期';
comment on column "LoanIfrs9Ip"."IndustryCode" is '授信行業別';
comment on column "LoanIfrs9Ip"."ClTypeJCIC" is '擔保品類別';
comment on column "LoanIfrs9Ip"."CityCode" is '擔保品地區別';
comment on column "LoanIfrs9Ip"."ProdNo" is '商品利率代碼';
comment on column "LoanIfrs9Ip"."CustKind" is '企業戶/個人戶';
comment on column "LoanIfrs9Ip"."Ifrs9ProdCode" is '產品別';
comment on column "LoanIfrs9Ip"."EvaAmt" is '原始鑑價金額';
comment on column "LoanIfrs9Ip"."AvblBal" is '可動用餘額(台幣)';
comment on column "LoanIfrs9Ip"."RecycleCode" is '該筆額度是否可循環動用';
comment on column "LoanIfrs9Ip"."IrrevocableFlag" is '該筆額度是否為不可撤銷';
comment on column "LoanIfrs9Ip"."LoanTerm" is '合約期限';
comment on column "LoanIfrs9Ip"."AcCode" is '備忘分錄會計科目';
comment on column "LoanIfrs9Ip"."AcCurcd" is '記帳幣別';
comment on column "LoanIfrs9Ip"."AcBookCode" is '會計帳冊';
comment on column "LoanIfrs9Ip"."CurrencyCode" is '交易幣別';
comment on column "LoanIfrs9Ip"."ExchangeRate" is '報導日匯率';
comment on column "LoanIfrs9Ip"."LineAmtCurr" is '核准金額(交易幣)';
comment on column "LoanIfrs9Ip"."AcctFeeCurr" is '帳管費(交易幣)';
comment on column "LoanIfrs9Ip"."FeeCurr" is '法拍及火險費用(交易幣)';
comment on column "LoanIfrs9Ip"."CreateDate" is '建檔日期時間';
comment on column "LoanIfrs9Ip"."CreateEmpNo" is '建檔人員';
comment on column "LoanIfrs9Ip"."LastUpdate" is '最後更新日期時間';
comment on column "LoanIfrs9Ip"."LastUpdateEmpNo" is '最後更新人員';
