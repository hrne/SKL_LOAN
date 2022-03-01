drop table "CdBankOld" purge;

create table "CdBankOld" (
  "BankCode" varchar2(3),
  "BranchCode" varchar2(4),
  "BankItem" nvarchar2(50),
  "BranchItem" nvarchar2(50),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdBankOld" add constraint "CdBankOld_PK" primary key("BankCode", "BranchCode");

comment on table "CdBankOld" is '舊行庫資料檔';
comment on column "CdBankOld"."BankCode" is '行庫代號';
comment on column "CdBankOld"."BranchCode" is '分行代號';
comment on column "CdBankOld"."BankItem" is '行庫名稱';
comment on column "CdBankOld"."BranchItem" is '分行名稱';
comment on column "CdBankOld"."CreateDate" is '建檔日期時間';
comment on column "CdBankOld"."CreateEmpNo" is '建檔人員';
comment on column "CdBankOld"."LastUpdate" is '最後更新日期時間';
comment on column "CdBankOld"."LastUpdateEmpNo" is '最後更新人員';
