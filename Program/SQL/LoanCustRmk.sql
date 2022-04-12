drop table "LoanCustRmk" purge;

create table "LoanCustRmk" (
  "CustNo" decimal(7, 0) default 0 not null,
  "AcDate" decimal(8, 0) default 0 not null,
  "RmkNo" decimal(3, 0) default 0 not null,
  "CustUKey" varchar2(32),
  "RmkDesc" nvarchar2(120),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanCustRmk" add constraint "LoanCustRmk_PK" primary key("CustNo", "AcDate", "RmkNo");

alter table "LoanCustRmk" add constraint "LoanCustRmk_CustMain_FK1" foreign key ("CustUKey") references "CustMain" ("CustUKey") on delete cascade;

create index "LoanCustRmk_Index1" on "LoanCustRmk"("CustNo" asc);

create index "LoanCustRmk_Index2" on "LoanCustRmk"("RmkCode" asc);

comment on table "LoanCustRmk" is '帳務備忘錄明細檔';
comment on column "LoanCustRmk"."CustNo" is '借款人戶號';
comment on column "LoanCustRmk"."AcDate" is '會計日期';
comment on column "LoanCustRmk"."RmkNo" is '備忘錄序號';
comment on column "LoanCustRmk"."CustUKey" is '客戶識別碼';
comment on column "LoanCustRmk"."RmkDesc" is '備忘錄說明';
comment on column "LoanCustRmk"."CreateDate" is '建檔日期時間';
comment on column "LoanCustRmk"."CreateEmpNo" is '建檔人員';
comment on column "LoanCustRmk"."LastUpdate" is '最後更新日期時間';
comment on column "LoanCustRmk"."LastUpdateEmpNo" is '最後更新人員';

