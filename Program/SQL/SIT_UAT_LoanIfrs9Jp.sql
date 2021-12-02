drop table "LoanIfrs9Jp" purge;

create table "LoanIfrs9Jp" (
  "DataYM" decimal(6, 0) default 0 not null,
  "AcDateYM" decimal(6, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "NewFacmNo" decimal(3, 0) default 0 not null,
  "NewBormNo" decimal(3, 0) default 0 not null,
  "OldFacmNo" decimal(3, 0) default 0 not null,
  "OldBormNo" decimal(3, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanIfrs9Jp" add constraint "LoanIfrs9Jp_PK" primary key("DataYM", "AcDateYM", "CustNo", "NewFacmNo", "NewBormNo", "OldFacmNo", "OldBormNo");

comment on table "LoanIfrs9Jp" is 'IFRS9欄位清單10';
comment on column "LoanIfrs9Jp"."DataYM" is '年月份';
comment on column "LoanIfrs9Jp"."AcDateYM" is '發生時會計日期年月';
comment on column "LoanIfrs9Jp"."CustNo" is '戶號';
comment on column "LoanIfrs9Jp"."NewFacmNo" is '新額度編號';
comment on column "LoanIfrs9Jp"."NewBormNo" is '新撥款序號';
comment on column "LoanIfrs9Jp"."OldFacmNo" is '舊額度編號';
comment on column "LoanIfrs9Jp"."OldBormNo" is '舊撥款序號';
comment on column "LoanIfrs9Jp"."CreateDate" is '建檔日期時間';
comment on column "LoanIfrs9Jp"."CreateEmpNo" is '建檔人員';
comment on column "LoanIfrs9Jp"."LastUpdate" is '最後更新日期時間';
comment on column "LoanIfrs9Jp"."LastUpdateEmpNo" is '最後更新人員';
