drop table "AcAcctCheckDetail" purge;

create table "AcAcctCheckDetail" (
  "AcDate" decimal(8, 0) default 0 not null,
  "BranchNo" varchar2(4),
  "CurrencyCode" varchar2(3),
  "AcctCode" varchar2(3),
  "AcctItem" nvarchar2(20),
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "AcBal" decimal(16, 2) default 0 not null,
  "AcctMasterBal" decimal(16, 2) default 0 not null,
  "DiffBal" decimal(16, 2) default 0 not null,
  "CreateEmpNo" varchar2(6),
  "CreateDate" timestamp,
  "LastUpdateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "AcSubBookCode" varchar2(3)
);

alter table "AcAcctCheckDetail" add constraint "AcAcctCheckDetail_PK" primary key("AcDate", "BranchNo", "CurrencyCode", "AcctCode", "CustNo", "FacmNo", "BormNo", "AcSubBookCode");

comment on table "AcAcctCheckDetail" is '會計業務檢核明細檔';
comment on column "AcAcctCheckDetail"."AcDate" is '會計日期';
comment on column "AcAcctCheckDetail"."BranchNo" is '單位別';
comment on column "AcAcctCheckDetail"."CurrencyCode" is '幣別';
comment on column "AcAcctCheckDetail"."AcctCode" is '業務科目代號';
comment on column "AcAcctCheckDetail"."AcctItem" is '業務科目名稱';
comment on column "AcAcctCheckDetail"."CustNo" is '戶號';
comment on column "AcAcctCheckDetail"."FacmNo" is '額度號碼';
comment on column "AcAcctCheckDetail"."BormNo" is '撥款序號';
comment on column "AcAcctCheckDetail"."AcBal" is '會計帳餘額';
comment on column "AcAcctCheckDetail"."AcctMasterBal" is '業務帳餘額';
comment on column "AcAcctCheckDetail"."DiffBal" is '差額';
comment on column "AcAcctCheckDetail"."CreateEmpNo" is '建檔人員';
comment on column "AcAcctCheckDetail"."CreateDate" is '建檔日期';
comment on column "AcAcctCheckDetail"."LastUpdateEmpNo" is '最後維護人員';
comment on column "AcAcctCheckDetail"."LastUpdate" is '最後維護日期';
comment on column "AcAcctCheckDetail"."AcSubBookCode" is '區隔帳冊';
