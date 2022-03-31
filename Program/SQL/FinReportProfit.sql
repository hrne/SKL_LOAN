drop table "FinReportProfit" purge;

create table "FinReportProfit" (
  "CustUKey" varchar2(32),
  "Ukey" varchar2(32),
  "BusIncome" decimal(18, 0) default 0 not null,
  "GrowRate" decimal(18, 2) default 0 not null,
  "BusCost" decimal(18, 0) default 0 not null,
  "BusGrossProfit" decimal(18, 0) default 0 not null,
  "ManageFee" decimal(18, 0) default 0 not null,
  "BusLossProfit" decimal(18, 0) default 0 not null,
  "BusOtherIncome" decimal(18, 0) default 0 not null,
  "Interest" decimal(18, 0) default 0 not null,
  "BusOtherFee" decimal(18, 0) default 0 not null,
  "BeforeTaxNet" decimal(18, 0) default 0 not null,
  "BusTax" decimal(18, 0) default 0 not null,
  "HomeLossProfit" decimal(18, 0) default 0 not null,
  "OtherComLossProfit" decimal(18, 0) default 0 not null,
  "HomeComLossProfit" decimal(18, 0) default 0 not null,
  "UncontrolRight" decimal(18, 0) default 0 not null,
  "ParentCompanyRight" decimal(18, 0) default 0 not null,
  "EPS" decimal(18, 4) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "FinReportProfit" add constraint "FinReportProfit_PK" primary key("CustUKey", "Ukey");

comment on table "FinReportProfit" is '客戶財務報表.損益表';
comment on column "FinReportProfit"."CustUKey" is '客戶識別碼';
comment on column "FinReportProfit"."Ukey" is '識別碼';
comment on column "FinReportProfit"."BusIncome" is '營業收入';
comment on column "FinReportProfit"."GrowRate" is '較去年同期營收成長率';
comment on column "FinReportProfit"."BusCost" is '減︰營業成本';
comment on column "FinReportProfit"."BusGrossProfit" is '營業毛利';
comment on column "FinReportProfit"."ManageFee" is '減:管銷費用';
comment on column "FinReportProfit"."BusLossProfit" is '營業損益';
comment on column "FinReportProfit"."BusOtherIncome" is '加︰營業外收入';
comment on column "FinReportProfit"."Interest" is '減︰利息支出';
comment on column "FinReportProfit"."BusOtherFee" is '營業外費用';
comment on column "FinReportProfit"."BeforeTaxNet" is '稅前淨利';
comment on column "FinReportProfit"."BusTax" is '減︰營利事業所得稅';
comment on column "FinReportProfit"."HomeLossProfit" is '本期損益';
comment on column "FinReportProfit"."OtherComLossProfit" is '其他綜合損益';
comment on column "FinReportProfit"."HomeComLossProfit" is '本期綜合損益總額';
comment on column "FinReportProfit"."UncontrolRight" is '非控制權益';
comment on column "FinReportProfit"."ParentCompanyRight" is '歸屬於母公司之權益';
comment on column "FinReportProfit"."EPS" is '每股盈餘EPS(元)';
comment on column "FinReportProfit"."CreateDate" is '建檔日期時間';
comment on column "FinReportProfit"."CreateEmpNo" is '建檔人員';
comment on column "FinReportProfit"."LastUpdate" is '最後更新日期時間';
comment on column "FinReportProfit"."LastUpdateEmpNo" is '最後更新人員';
