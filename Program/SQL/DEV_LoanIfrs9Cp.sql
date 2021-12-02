drop table "LoanIfrs9Cp" purge;

create table "LoanIfrs9Cp" (
  "DataYM" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "CustId" varchar2(10),
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "AmortizedCode" varchar2(1),
  "PayIntFreq" decimal(2, 0) default 0 not null,
  "RepayFreq" decimal(2, 0) default 0 not null,
  "EffectDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanIfrs9Cp" add constraint "LoanIfrs9Cp_PK" primary key("DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate");

comment on table "LoanIfrs9Cp" is 'IFRS9資料欄位清單3';
comment on column "LoanIfrs9Cp"."DataYM" is '年月份';
comment on column "LoanIfrs9Cp"."CustNo" is '戶號';
comment on column "LoanIfrs9Cp"."CustId" is '借款人ID / 統編';
comment on column "LoanIfrs9Cp"."FacmNo" is '額度編號';
comment on column "LoanIfrs9Cp"."BormNo" is '撥款序號';
comment on column "LoanIfrs9Cp"."AmortizedCode" is '約定還款方式';
comment on column "LoanIfrs9Cp"."PayIntFreq" is '繳息週期';
comment on column "LoanIfrs9Cp"."RepayFreq" is '還本週期';
comment on column "LoanIfrs9Cp"."EffectDate" is '生效日期';
comment on column "LoanIfrs9Cp"."CreateDate" is '建檔日期時間';
comment on column "LoanIfrs9Cp"."CreateEmpNo" is '建檔人員';
comment on column "LoanIfrs9Cp"."LastUpdate" is '最後更新日期時間';
comment on column "LoanIfrs9Cp"."LastUpdateEmpNo" is '最後更新人員';
