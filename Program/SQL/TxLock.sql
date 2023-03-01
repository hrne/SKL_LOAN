drop table "TxLock" purge;

drop sequence "TxLock_SEQ";

create table "TxLock" (
  "LockNo" decimal(11,0) not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "TranNo" varchar2(5),
  "BrNo" varchar2(4),
  "TlrNo" varchar2(6),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxLock" add constraint "TxLock_PK" primary key("LockNo");

create sequence "TxLock_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create unique index "TxLock_Index1" on "TxLock"("CustNo" asc);

comment on table "TxLock" is '鎖定控制檔';
comment on column "TxLock"."LockNo" is '鎖定序號';
comment on column "TxLock"."CustNo" is '戶號';
comment on column "TxLock"."TranNo" is '交易代號';
comment on column "TxLock"."BrNo" is '鎖定單位';
comment on column "TxLock"."TlrNo" is '鎖定櫃員';
comment on column "TxLock"."CreateDate" is '建檔日期時間';
comment on column "TxLock"."CreateEmpNo" is '建檔人員';
comment on column "TxLock"."LastUpdate" is '最後更新日期時間';
comment on column "TxLock"."LastUpdateEmpNo" is '最後更新人員';
