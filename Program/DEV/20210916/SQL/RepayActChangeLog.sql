drop table "RepayActChangeLog" purge;

drop sequence "RepayActChangeLog_SEQ";

create table "RepayActChangeLog" (
  "LogNo" decimal(11,0) not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "RepayCode" decimal(2, 0) default 0 not null,
  "RepayBank" varchar2(3),
  "PostDepCode" varchar2(1),
  "RepayAcct" varchar2(14),
  "Status" varchar2(1),
  "RelDy" decimal(8, 0) default 0 not null,
  "RelTxseq" varchar2(18),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "RepayActChangeLog" add constraint "RepayActChangeLog_PK" primary key("LogNo");

create sequence "RepayActChangeLog_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "RepayActChangeLog_Index1" on "RepayActChangeLog"("CustNo" asc, "FacmNo" asc);

comment on table "RepayActChangeLog" is '還款帳號變更(含還款方式)紀錄檔';
comment on column "RepayActChangeLog"."LogNo" is '序號';
comment on column "RepayActChangeLog"."CustNo" is '戶號';
comment on column "RepayActChangeLog"."FacmNo" is '額度';
comment on column "RepayActChangeLog"."RepayCode" is '繳款方式';
comment on column "RepayActChangeLog"."RepayBank" is '扣款銀行';
comment on column "RepayActChangeLog"."PostDepCode" is '郵局存款別';
comment on column "RepayActChangeLog"."RepayAcct" is '扣款帳號';
comment on column "RepayActChangeLog"."Status" is '狀態碼';
comment on column "RepayActChangeLog"."RelDy" is '登放日期';
comment on column "RepayActChangeLog"."RelTxseq" is '登放序號';
comment on column "RepayActChangeLog"."CreateDate" is '建檔日期時間';
comment on column "RepayActChangeLog"."CreateEmpNo" is '建檔人員';
comment on column "RepayActChangeLog"."LastUpdate" is '最後更新日期時間';
comment on column "RepayActChangeLog"."LastUpdateEmpNo" is '最後更新人員';
