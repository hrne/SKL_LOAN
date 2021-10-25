drop table "MonthlyLM036Portfolio" purge;

create table "MonthlyLM036Portfolio" (
  "DataMonth" decimal(6, 0) default 0 not null,
  "MonthEndDate" decimal(8, 0) default 0 not null,
  "PortfolioTotal" decimal(16, 2) default 0 not null,
  "NaturalPersonLoanBal" decimal(16, 2) default 0 not null,
  "LegalPersonLoanBal" decimal(16, 2) default 0 not null,
  "SyndLoanBal" decimal(16, 2) default 0 not null,
  "StockLoanBal" decimal(16, 2) default 0 not null,
  "OtherLoanbal" decimal(16, 2) default 0 not null,
  "AmortizeTotal" decimal(16, 2) default 0 not null,
  "OvduExpense" decimal(16, 2) default 0 not null,
  "NaturalPersonLargeCounts" decimal(16, 0) default 0 not null,
  "NaturalPersonLargeTotal" decimal(16, 2) default 0 not null,
  "LegalPersonLargeCounts" decimal(16, 0) default 0 not null,
  "LegalPersonLargeTotal" decimal(16, 2) default 0 not null,
  "NaturalPersonPercent" decimal(6, 4) default 0 not null,
  "LegalPersonPercent" decimal(6, 4) default 0 not null,
  "SyndPercent" decimal(6, 4) default 0 not null,
  "StockPercent" decimal(6, 4) default 0 not null,
  "OtherPercent" decimal(6, 4) default 0 not null,
  "EntUsedPercent" decimal(6, 4) default 0 not null,
  "InsuDividendRate" decimal(6, 4) default 0 not null,
  "NaturalPersonRate" decimal(6, 4) default 0 not null,
  "LegalPersonRate" decimal(6, 4) default 0 not null,
  "SyndRate" decimal(6, 4) default 0 not null,
  "StockRate" decimal(6, 4) default 0 not null,
  "OtherRate" decimal(6, 4) default 0 not null,
  "AvgRate" decimal(6, 4) default 0 not null,
  "HouseRateOfReturn" decimal(6, 4) default 0 not null,
  "EntRateOfReturn" decimal(6, 4) default 0 not null,
  "RateOfReturn" decimal(6, 4) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "MonthlyLM036Portfolio" add constraint "MonthlyLM036Portfolio_PK" primary key("DataMonth");

comment on table "MonthlyLM036Portfolio" is 'LM036Portfolio';
comment on column "MonthlyLM036Portfolio"."DataMonth" is '資料年月';
comment on column "MonthlyLM036Portfolio"."MonthEndDate" is '月底日期';
comment on column "MonthlyLM036Portfolio"."PortfolioTotal" is '授信組合餘額';
comment on column "MonthlyLM036Portfolio"."NaturalPersonLoanBal" is '自然人放款';
comment on column "MonthlyLM036Portfolio"."LegalPersonLoanBal" is '法人放款';
comment on column "MonthlyLM036Portfolio"."SyndLoanBal" is '聯貸案';
comment on column "MonthlyLM036Portfolio"."StockLoanBal" is '股票質押';
comment on column "MonthlyLM036Portfolio"."OtherLoanbal" is '一般法人放款';
comment on column "MonthlyLM036Portfolio"."AmortizeTotal" is '溢折價';
comment on column "MonthlyLM036Portfolio"."OvduExpense" is '催收費用';
comment on column "MonthlyLM036Portfolio"."NaturalPersonLargeCounts" is '自然人大額授信件件數';
comment on column "MonthlyLM036Portfolio"."NaturalPersonLargeTotal" is '自然人大額授信件餘額';
comment on column "MonthlyLM036Portfolio"."LegalPersonLargeCounts" is '法人大額授信件件數';
comment on column "MonthlyLM036Portfolio"."LegalPersonLargeTotal" is '法人大額授信件餘額';
comment on column "MonthlyLM036Portfolio"."NaturalPersonPercent" is '自然人放款占比';
comment on column "MonthlyLM036Portfolio"."LegalPersonPercent" is '法人放款占比';
comment on column "MonthlyLM036Portfolio"."SyndPercent" is '聯貸案占比';
comment on column "MonthlyLM036Portfolio"."StockPercent" is '股票質押占比';
comment on column "MonthlyLM036Portfolio"."OtherPercent" is '一般法人放款占比';
comment on column "MonthlyLM036Portfolio"."EntUsedPercent" is '企業放款動用率';
comment on column "MonthlyLM036Portfolio"."InsuDividendRate" is '保單分紅利率';
comment on column "MonthlyLM036Portfolio"."NaturalPersonRate" is '自然人當月平均利率';
comment on column "MonthlyLM036Portfolio"."LegalPersonRate" is '法人當月平均利率';
comment on column "MonthlyLM036Portfolio"."SyndRate" is '聯貸案平均利率';
comment on column "MonthlyLM036Portfolio"."StockRate" is '股票質押平均利率';
comment on column "MonthlyLM036Portfolio"."OtherRate" is '一般法人放款平均利率';
comment on column "MonthlyLM036Portfolio"."AvgRate" is '放款平均利率';
comment on column "MonthlyLM036Portfolio"."HouseRateOfReturn" is '房貸通路當月毛報酬率';
comment on column "MonthlyLM036Portfolio"."EntRateOfReturn" is '企金通路當月毛報酬率';
comment on column "MonthlyLM036Portfolio"."RateOfReturn" is '放款毛報酬率';
comment on column "MonthlyLM036Portfolio"."CreateDate" is '建檔日期時間';
comment on column "MonthlyLM036Portfolio"."CreateEmpNo" is '建檔人員';
comment on column "MonthlyLM036Portfolio"."LastUpdate" is '最後更新日期時間';
comment on column "MonthlyLM036Portfolio"."LastUpdateEmpNo" is '最後更新人員';
