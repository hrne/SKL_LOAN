drop table "LoanIfrs9Fp" purge;

create table "LoanIfrs9Fp" (
  "DataYM" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "CustId" varchar2(10),
  "AgreeNo" decimal(3, 0) default 0 not null,
  "AgreeFg" varchar2(1),
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanIfrs9Fp" add constraint "LoanIfrs9Fp_PK" primary key("DataYM", "CustNo", "AgreeNo", "AgreeFg", "FacmNo", "BormNo");

comment on table "LoanIfrs9Fp" is 'IFRS9欄位清單6';
comment on column "LoanIfrs9Fp"."DataYM" is '年月份';
comment on column "LoanIfrs9Fp"."CustNo" is '戶號';
comment on column "LoanIfrs9Fp"."CustId" is '借款人ID / 統編';
comment on column "LoanIfrs9Fp"."AgreeNo" is '協議編號';
comment on column "LoanIfrs9Fp"."AgreeFg" is '協議前後';
comment on column "LoanIfrs9Fp"."FacmNo" is '額度編號';
comment on column "LoanIfrs9Fp"."BormNo" is '撥款序號';
comment on column "LoanIfrs9Fp"."CreateDate" is '建檔日期時間';
comment on column "LoanIfrs9Fp"."CreateEmpNo" is '建檔人員';
comment on column "LoanIfrs9Fp"."LastUpdate" is '最後更新日期時間';
comment on column "LoanIfrs9Fp"."LastUpdateEmpNo" is '最後更新人員';
