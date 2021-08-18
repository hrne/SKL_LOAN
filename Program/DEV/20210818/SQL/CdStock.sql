drop table "CdStock" purge;

create table "CdStock" (
  "StockCode" varchar2(10),
  "StockItem" nvarchar2(20),
  "StockCompanyName" nvarchar2(50),
  "Currency" varchar2(3),
  "YdClosePrice" decimal(16, 2) default 0 not null,
  "MonthlyAvg" decimal(16, 2) default 0 not null,
  "ThreeMonthAvg" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdStock" add constraint "CdStock_PK" primary key("StockCode");

comment on table "CdStock" is '股票代號檔';
comment on column "CdStock"."StockCode" is '股票代號';
comment on column "CdStock"."StockItem" is '股票簡稱';
comment on column "CdStock"."StockCompanyName" is '股票公司名稱';
comment on column "CdStock"."Currency" is '幣別';
comment on column "CdStock"."YdClosePrice" is '前日收盤價';
comment on column "CdStock"."MonthlyAvg" is '一個月平均價';
comment on column "CdStock"."ThreeMonthAvg" is '三個月平均價';
comment on column "CdStock"."CreateDate" is '建檔日期時間';
comment on column "CdStock"."CreateEmpNo" is '建檔人員';
comment on column "CdStock"."LastUpdate" is '最後更新日期時間';
comment on column "CdStock"."LastUpdateEmpNo" is '最後更新人員';
