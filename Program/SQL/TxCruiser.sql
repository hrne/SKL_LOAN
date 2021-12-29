drop table "TxCruiser" purge;

create table "TxCruiser" (
  "TxSeq" varchar2(20),
  "TlrNo" varchar2(6),
  "TxCode" varchar2(10),
  "JobList" varchar2(800),
  "Status" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxCruiser" add constraint "TxCruiser_PK" primary key("TxSeq", "TlrNo");

create index "TxCruiser_Index1" on "TxCruiser"("Status" asc);

comment on table "TxCruiser" is '批次發動交易紀錄';
comment on column "TxCruiser"."TxSeq" is '交易序號';
comment on column "TxCruiser"."TlrNo" is '發動經辦';
comment on column "TxCruiser"."TxCode" is '發動交易';
comment on column "TxCruiser"."JobList" is '批次執行清單';
comment on column "TxCruiser"."Status" is '執行狀態';
comment on column "TxCruiser"."CreateDate" is '建檔日期時間';
comment on column "TxCruiser"."CreateEmpNo" is '建檔人員';
comment on column "TxCruiser"."LastUpdate" is '最後更新日期時間';
comment on column "TxCruiser"."LastUpdateEmpNo" is '最後更新人員';
