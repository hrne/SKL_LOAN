drop table "FinReportRate" purge;

create table "FinReportRate" (
  "CustUKey" varchar2(32),
  "UKey" varchar2(32),
  "IsSameTrade" varchar2(1),
  "TradeType" varchar2(20),
  "Flow" decimal(18, 4) default 0 not null,
  "Speed" decimal(18, 4) default 0 not null,
  "RateGuar" decimal(18, 4) default 0 not null,
  "Debt" decimal(18, 4) default 0 not null,
  "Net" decimal(18, 4) default 0 not null,
  "CashFlow" decimal(18, 4) default 0 not null,
  "FixLong" decimal(18, 4) default 0 not null,
  "FinSpend" decimal(18, 4) default 0 not null,
  "GrossProfit" decimal(18, 4) default 0 not null,
  "AfterTaxNet" decimal(18, 4) default 0 not null,
  "NetReward" decimal(18, 4) default 0 not null,
  "TotalAssetReward" decimal(18, 4) default 0 not null,
  "Stock" decimal(18, 4) default 0 not null,
  "ReceiveAccount" decimal(18, 4) default 0 not null,
  "TotalAsset" decimal(18, 4) default 0 not null,
  "PayAccount" decimal(18, 4) default 0 not null,
  "AveTotalAsset" decimal(18, 4) default 0 not null,
  "AveNetBusCycle" decimal(18, 4) default 0 not null,
  "FinLever" decimal(18, 4) default 0 not null,
  "LoanDebtNet" decimal(18, 4) default 0 not null,
  "BusRate" decimal(18, 4) default 0 not null,
  "PayFinLever" decimal(18, 4) default 0 not null,
  "ADE" decimal(18, 4) default 0 not null,
  "CashGuar" decimal(18, 4) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "FinReportRate" add constraint "FinReportRate_PK" primary key("CustUKey", "UKey");

comment on table "FinReportRate" is '客戶財務報表.財務比率表';
comment on column "FinReportRate"."CustUKey" is '客戶識別碼';
comment on column "FinReportRate"."UKey" is '識別碼';
comment on column "FinReportRate"."IsSameTrade" is '是否為同業值';
comment on column "FinReportRate"."TradeType" is '行業別';
comment on column "FinReportRate"."Flow" is '流動比率';
comment on column "FinReportRate"."Speed" is '速動比率';
comment on column "FinReportRate"."RateGuar" is '利息保障倍數';
comment on column "FinReportRate"."Debt" is '負債比率';
comment on column "FinReportRate"."Net" is '淨值比率';
comment on column "FinReportRate"."CashFlow" is '現金流量比率';
comment on column "FinReportRate"."FixLong" is '固定長期適合率';
comment on column "FinReportRate"."FinSpend" is '財務支出率';
comment on column "FinReportRate"."GrossProfit" is '毛利率';
comment on column "FinReportRate"."AfterTaxNet" is '稅後淨利率';
comment on column "FinReportRate"."NetReward" is '淨值報酬率';
comment on column "FinReportRate"."TotalAssetReward" is '總資產報酬率';
comment on column "FinReportRate"."Stock" is '存貨週轉率';
comment on column "FinReportRate"."ReceiveAccount" is '應收帳款週轉率';
comment on column "FinReportRate"."TotalAsset" is '總資產週轉率';
comment on column "FinReportRate"."PayAccount" is '應付帳款週轉率';
comment on column "FinReportRate"."AveTotalAsset" is '平均總資產週轉率：營業收入/((上期總資產+本期總資產)/2)';
comment on column "FinReportRate"."AveNetBusCycle" is '平均淨營業週期：平均存貨週轉天數+平均應收帳款及應收票據週轉天數-平均應付帳款及應付票據週轉天數';
comment on column "FinReportRate"."FinLever" is '財務結構-財務槓桿：總負債/淨值';
comment on column "FinReportRate"."LoanDebtNet" is '長期負債對淨值比：長期負債/淨值';
comment on column "FinReportRate"."BusRate" is '營授比率：總借款/營收';
comment on column "FinReportRate"."PayFinLever" is '償債能力-財務槓桿度：營業利益/(營業利益-利息支出)';
comment on column "FinReportRate"."ADE" is '借款依存度(ADE)：(短期借款+應付短期票券+股東墊款+長期負債+一年內到期之長期負債)/淨值合計';
comment on column "FinReportRate"."CashGuar" is '現金保障倍數：營業活動現金流量/利息支出';
comment on column "FinReportRate"."CreateDate" is '建檔日期時間';
comment on column "FinReportRate"."CreateEmpNo" is '建檔人員';
comment on column "FinReportRate"."LastUpdate" is '最後更新日期時間';
comment on column "FinReportRate"."LastUpdateEmpNo" is '最後更新人員';
