drop table "MonthlyLM052Loss" purge;

create table "MonthlyLM052Loss" (
  "YearMonth" decimal(6, 0) default 0 not null,
  "AssetEvaTotal" decimal(16, 2) default 0 not null,
  "LegalLoss" decimal(16, 2) default 0 not null,
  "ApprovedLoss" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "MonthlyLM052Loss" add constraint "MonthlyLM052Loss_PK" primary key("YearMonth");

comment on table "MonthlyLM052Loss" is 'LM052備抵損失資料檔';
comment on column "MonthlyLM052Loss"."YearMonth" is '資料年月';
comment on column "MonthlyLM052Loss"."AssetEvaTotal" is '五類資產評估合計';
comment on column "MonthlyLM052Loss"."LegalLoss" is '法定備抵損失提撥';
comment on column "MonthlyLM052Loss"."ApprovedLoss" is '會計部核定備抵損失';
comment on column "MonthlyLM052Loss"."CreateDate" is '建檔日期時間';
comment on column "MonthlyLM052Loss"."CreateEmpNo" is '建檔人員';
comment on column "MonthlyLM052Loss"."LastUpdate" is '最後更新日期時間';
comment on column "MonthlyLM052Loss"."LastUpdateEmpNo" is '最後更新人員';
