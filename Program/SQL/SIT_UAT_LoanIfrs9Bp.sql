drop table "LoanIfrs9Bp" purge;

create table "LoanIfrs9Bp" (
  "DataYM" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "CustId" varchar2(10),
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "LoanRate" decimal(8, 6) default 0 not null,
  "RateCode" decimal(1, 0) default 0 not null,
  "EffectDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanIfrs9Bp" add constraint "LoanIfrs9Bp_PK" primary key("DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate");

comment on table "LoanIfrs9Bp" is 'IFRS9欄位清單B檔';
comment on column "LoanIfrs9Bp"."DataYM" is '年月份';
comment on column "LoanIfrs9Bp"."CustNo" is '戶號';
comment on column "LoanIfrs9Bp"."CustId" is '借款人ID / 統編';
comment on column "LoanIfrs9Bp"."FacmNo" is '額度編號';
comment on column "LoanIfrs9Bp"."BormNo" is '撥款序號';
comment on column "LoanIfrs9Bp"."LoanRate" is '貸放利率';
comment on column "LoanIfrs9Bp"."RateCode" is '利率調整方式';
comment on column "LoanIfrs9Bp"."EffectDate" is '利率欄位生效日';
comment on column "LoanIfrs9Bp"."CreateDate" is '建檔日期時間';
comment on column "LoanIfrs9Bp"."CreateEmpNo" is '建檔人員';
comment on column "LoanIfrs9Bp"."LastUpdate" is '最後更新日期時間';
comment on column "LoanIfrs9Bp"."LastUpdateEmpNo" is '最後更新人員';
