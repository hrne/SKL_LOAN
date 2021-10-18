drop table "FinReportReview" purge;

create table "FinReportReview" (
  "CustUKey" varchar2(32),
  "UKey" varchar2(32),
  "CurrentAsset" decimal(18, 0) default 0 not null,
  "TotalAsset" decimal(18, 0) default 0 not null,
  "PropertyAsset" decimal(18, 0) default 0 not null,
  "Investment" decimal(18, 0) default 0 not null,
  "InvestmentProperty" decimal(18, 0) default 0 not null,
  "Depreciation" decimal(18, 0) default 0 not null,
  "CurrentDebt" decimal(18, 0) default 0 not null,
  "TotalDebt" decimal(18, 0) default 0 not null,
  "TotalEquity" decimal(18, 0) default 0 not null,
  "BondsPayable" decimal(18, 0) default 0 not null,
  "LongTermBorrowings" decimal(18, 0) default 0 not null,
  "NonCurrentLease" decimal(18, 0) default 0 not null,
  "LongTermPayable" decimal(18, 0) default 0 not null,
  "Preference" decimal(18, 0) default 0 not null,
  "OperatingRevenue" decimal(18, 0) default 0 not null,
  "InterestExpense" decimal(18, 0) default 0 not null,
  "ProfitBeforeTax" decimal(18, 0) default 0 not null,
  "ProfitAfterTax" decimal(18, 0) default 0 not null,
  "WorkingCapitalRatio" decimal(18, 4) default 0 not null,
  "InterestCoverageRatio1" decimal(18, 4) default 0 not null,
  "InterestCoverageRatio2" decimal(18, 4) default 0 not null,
  "LeverageRatio" decimal(18, 4) default 0 not null,
  "EquityRatio" decimal(18, 4) default 0 not null,
  "LongFitRatio" decimal(18, 4) default 0 not null,
  "NetProfitRatio" decimal(18, 4) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "FinReportReview" add constraint "FinReportReview_PK" primary key("CustUKey", "UKey");

comment on table "FinReportReview" is '客戶財務報表.覆審比率表';
comment on column "FinReportReview"."CustUKey" is '客戶識別碼';
comment on column "FinReportReview"."UKey" is '識別碼';
comment on column "FinReportReview"."CurrentAsset" is '流動資產';
comment on column "FinReportReview"."TotalAsset" is '資產總額';
comment on column "FinReportReview"."PropertyAsset" is '不動產、廠房及設備淨額';
comment on column "FinReportReview"."Investment" is '權益法之投資';
comment on column "FinReportReview"."InvestmentProperty" is '投資性不動產';
comment on column "FinReportReview"."Depreciation" is '折舊及攤銷';
comment on column "FinReportReview"."CurrentDebt" is '流動負債';
comment on column "FinReportReview"."TotalDebt" is '負債合計';
comment on column "FinReportReview"."TotalEquity" is '權益合計';
comment on column "FinReportReview"."BondsPayable" is '應付公司債';
comment on column "FinReportReview"."LongTermBorrowings" is '長期借款';
comment on column "FinReportReview"."NonCurrentLease" is '應付租賃款-非流動';
comment on column "FinReportReview"."LongTermPayable" is '長期應付票據及款項-關係人';
comment on column "FinReportReview"."Preference" is '特別股負債';
comment on column "FinReportReview"."OperatingRevenue" is '營業收入';
comment on column "FinReportReview"."InterestExpense" is '利息支出';
comment on column "FinReportReview"."ProfitBeforeTax" is '稅前淨利';
comment on column "FinReportReview"."ProfitAfterTax" is '本期淨利(稅後)';
comment on column "FinReportReview"."WorkingCapitalRatio" is '流動比率';
comment on column "FinReportReview"."InterestCoverageRatio1" is '利息保障倍數1';
comment on column "FinReportReview"."InterestCoverageRatio2" is '利息保障倍數2';
comment on column "FinReportReview"."LeverageRatio" is '槓桿比率';
comment on column "FinReportReview"."EquityRatio" is '權益比率';
comment on column "FinReportReview"."LongFitRatio" is '固定長期適合率';
comment on column "FinReportReview"."NetProfitRatio" is '純益率(稅後)';
comment on column "FinReportReview"."CreateDate" is '建檔日期時間';
comment on column "FinReportReview"."CreateEmpNo" is '建檔人員';
comment on column "FinReportReview"."LastUpdate" is '最後更新日期時間';
comment on column "FinReportReview"."LastUpdateEmpNo" is '最後更新人員';
