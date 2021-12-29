drop table "JobMain" purge;

create table "JobMain" (
  "TxSeq" varchar2(20),
  "ExecDate" decimal(8, 0) default 0 not null,
  "JobCode" varchar2(10),
  "StartTime" timestamp,
  "EndTime" timestamp,
  "Status" varchar2(1),
  "CreateEmpNo" varchar2(6),
  "CreateDate" timestamp,
  "LastUpdateEmpNo" varchar2(6),
  "LastUpdate" timestamp
);

alter table "JobMain" add constraint "JobMain_PK" primary key("TxSeq", "ExecDate", "JobCode");

comment on table "JobMain" is '批次工作主檔';
comment on column "JobMain"."TxSeq" is '交易序號';
comment on column "JobMain"."ExecDate" is '批次執行日期';
comment on column "JobMain"."JobCode" is '批次代號';
comment on column "JobMain"."StartTime" is '啟動時間';
comment on column "JobMain"."EndTime" is '結束時間';
comment on column "JobMain"."Status" is '狀態記號';
comment on column "JobMain"."CreateEmpNo" is '建檔人員';
comment on column "JobMain"."CreateDate" is '建檔日期';
comment on column "JobMain"."LastUpdateEmpNo" is '最後維護人員';
comment on column "JobMain"."LastUpdate" is '最後維護日期';
