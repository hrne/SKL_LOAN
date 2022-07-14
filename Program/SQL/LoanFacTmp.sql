drop table "LoanFacTmp" purge;

create table "LoanFacTmp" (
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "Describe" nvarchar2(100),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanFacTmp" add constraint "LoanFacTmp_PK" primary key("CustNo", "FacmNo");

alter table "LoanFacTmp" add constraint "LoanFacTmp_FacMain_FK1" foreign key ("CustNo", "FacmNo") references "FacMain" ("CustNo", "FacmNo") on delete cascade;

comment on table "LoanFacTmp" is '暫收款指定額度設定檔';
comment on column "LoanFacTmp"."CustNo" is '借款人戶號';
comment on column "LoanFacTmp"."FacmNo" is '額度編號';
comment on column "LoanFacTmp"."Describe" is '設定理由';
comment on column "LoanFacTmp"."CreateDate" is '建檔日期時間';
comment on column "LoanFacTmp"."CreateEmpNo" is '建檔人員';
comment on column "LoanFacTmp"."LastUpdate" is '最後更新日期時間';
comment on column "LoanFacTmp"."LastUpdateEmpNo" is '最後更新人員';
