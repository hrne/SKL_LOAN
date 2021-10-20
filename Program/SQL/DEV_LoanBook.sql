drop table "LoanBook" purge;

create table "LoanBook" (
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "BookDate" decimal(8, 0) default 0 not null,
  "ActualDate" decimal(8, 0) default 0 not null,
  "Status" decimal(1, 0) default 0 not null,
  "CurrencyCode" varchar2(3),
  "IncludeIntFlag" varchar2(1),
  "UnpaidIntFlag" varchar2(1),
  "BookAmt" decimal(16, 2) default 0 not null,
  "RepayAmt" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LoanBook" add constraint "LoanBook_PK" primary key("CustNo", "FacmNo", "BormNo", "BookDate");

alter table "LoanBook" add constraint "LoanBook_LoanBorMain_FK1" foreign key ("CustNo", "FacmNo", "BormNo") references "LoanBorMain" ("CustNo", "FacmNo", "BormNo") on delete cascade;

comment on table "LoanBook" is '放款約定還本檔';
comment on column "LoanBook"."CustNo" is '借款人戶號';
comment on column "LoanBook"."FacmNo" is '額度編號';
comment on column "LoanBook"."BormNo" is '撥款序號';
comment on column "LoanBook"."BookDate" is '約定還本日期';
comment on column "LoanBook"."ActualDate" is '實際還本日期';
comment on column "LoanBook"."Status" is '狀態';
comment on column "LoanBook"."CurrencyCode" is '幣別';
comment on column "LoanBook"."IncludeIntFlag" is '是否內含利息';
comment on column "LoanBook"."UnpaidIntFlag" is '利息是否可欠繳';
comment on column "LoanBook"."BookAmt" is '約定還本金額';
comment on column "LoanBook"."RepayAmt" is '實際還本金額';
comment on column "LoanBook"."CreateDate" is '建檔日期時間';
comment on column "LoanBook"."CreateEmpNo" is '建檔人員';
comment on column "LoanBook"."LastUpdate" is '最後更新日期時間';
comment on column "LoanBook"."LastUpdateEmpNo" is '最後更新人員';
