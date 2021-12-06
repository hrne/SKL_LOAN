drop table "FacProdAcctFee" purge;

create table "FacProdAcctFee" (
  "ProdNo" varchar2(9),
  "FeeType" varchar2(1),
  "LoanLow" decimal(16, 2) default 0 not null,
  "LoanHigh" decimal(16, 2) default 0 not null,
  "AcctFee" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "FacProdAcctFee" add constraint "FacProdAcctFee_PK" primary key("ProdNo", "FeeType", "LoanLow");

alter table "FacProdAcctFee" add constraint "FacProdAcctFee_FacProd_FK1" foreign key ("ProdNo") references "FacProd" ("ProdNo") on delete cascade;

comment on table "FacProdAcctFee" is '商品參數副檔帳管費';
comment on column "FacProdAcctFee"."ProdNo" is '商品代碼';
comment on column "FacProdAcctFee"."FeeType" is '費用類別';
comment on column "FacProdAcctFee"."LoanLow" is '貸款金額(含)以上';
comment on column "FacProdAcctFee"."LoanHigh" is '貸款金額(含)以下';
comment on column "FacProdAcctFee"."AcctFee" is '帳管費';
comment on column "FacProdAcctFee"."CreateDate" is '建檔日期時間';
comment on column "FacProdAcctFee"."CreateEmpNo" is '建檔人員';
comment on column "FacProdAcctFee"."LastUpdate" is '最後更新日期時間';
comment on column "FacProdAcctFee"."LastUpdateEmpNo" is '最後更新人員';
