drop table "ClOwnerRelation" purge;

create table "ClOwnerRelation" (
  "CreditSysNo" decimal(7, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "OwnerCustUKey" varchar2(32),
  "OwnerRelCode" varchar2(2),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClOwnerRelation" add constraint "ClOwnerRelation_PK" primary key("CreditSysNo", "CustNo", "OwnerCustUKey");

comment on table "ClOwnerRelation" is '擔保品所有權人與授信戶關係檔';
comment on column "ClOwnerRelation"."CreditSysNo" is '案件編號';
comment on column "ClOwnerRelation"."CustNo" is '借款人戶號';
comment on column "ClOwnerRelation"."OwnerCustUKey" is '客戶識別碼';
comment on column "ClOwnerRelation"."OwnerRelCode" is '與授信戶關係';
comment on column "ClOwnerRelation"."CreateDate" is '建檔日期時間';
comment on column "ClOwnerRelation"."CreateEmpNo" is '建檔人員';
comment on column "ClOwnerRelation"."LastUpdate" is '最後更新日期時間';
comment on column "ClOwnerRelation"."LastUpdateEmpNo" is '最後更新人員';
