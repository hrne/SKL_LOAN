drop table "AcAcctCheck" purge;

create table "AcAcctCheck" (
  "AcDate" decimal(8, 0) default 0 not null,
  "BranchNo" varchar2(4),
  "CurrencyCode" varchar2(3),
  "AcctCode" varchar2(3),
  "AcctItem" nvarchar2(20),
  "TdBal" decimal(18, 2) default 0 not null,
  "TdCnt" decimal(8, 0) default 0 not null,
  "TdNewCnt" decimal(8, 0) default 0 not null,
  "TdClsCnt" decimal(8, 0) default 0 not null,
  "TdExtCnt" decimal(8, 0) default 0 not null,
  "TdExtAmt" decimal(18, 2) default 0 not null,
  "ReceivableBal" decimal(18, 2) default 0 not null,
  "AcctMasterBal" decimal(18, 2) default 0 not null,
  "CreateEmpNo" varchar2(6),
  "CreateDate" timestamp,
  "LastUpdateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "AcSubBookCode" varchar2(3)
);

alter table "AcAcctCheck" add constraint "AcAcctCheck_PK" primary key("AcDate", "BranchNo", "CurrencyCode", "AcctCode", "AcSubBookCode");

comment on table "AcAcctCheck" is '會計業務檢核檔';
comment on column "AcAcctCheck"."AcDate" is '會計日期';
comment on column "AcAcctCheck"."BranchNo" is '單位別';
comment on column "AcAcctCheck"."CurrencyCode" is '幣別';
comment on column "AcAcctCheck"."AcctCode" is '業務科目代號';
comment on column "AcAcctCheck"."AcctItem" is '業務科目名稱';
comment on column "AcAcctCheck"."TdBal" is '本日餘額';
comment on column "AcAcctCheck"."TdCnt" is '本日件數';
comment on column "AcAcctCheck"."TdNewCnt" is '本日開戶件數';
comment on column "AcAcctCheck"."TdClsCnt" is '本日結清件數';
comment on column "AcAcctCheck"."TdExtCnt" is '本日展期件數';
comment on column "AcAcctCheck"."TdExtAmt" is '本日展期金額';
comment on column "AcAcctCheck"."ReceivableBal" is '銷帳檔餘額';
comment on column "AcAcctCheck"."AcctMasterBal" is '業務檔餘額';
comment on column "AcAcctCheck"."CreateEmpNo" is '建檔人員';
comment on column "AcAcctCheck"."CreateDate" is '建檔日期';
comment on column "AcAcctCheck"."LastUpdateEmpNo" is '最後維護人員';
comment on column "AcAcctCheck"."LastUpdate" is '最後維護日期';
comment on column "AcAcctCheck"."AcSubBookCode" is '區隔帳冊';
