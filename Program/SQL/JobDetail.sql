drop table "JobDetail" purge;

create table "JobDetail" (
  "TxSeq" varchar2(20),
  "ExecDate" decimal(8, 0) default 0 not null,
  "JobCode" varchar2(10),
  "StepId" varchar2(30),
  "BatchType" varchar2(1),
  "Status" varchar2(1),
  "ErrCode" varchar2(15),
  "ErrContent" clob,
  "StepStartTime" timestamp,
  "StepEndTime" timestamp,
  "CreateEmpNo" varchar2(6),
  "CreateDate" timestamp,
  "LastUpdateEmpNo" varchar2(6),
  "LastUpdate" timestamp
);

alter table "JobDetail" add constraint "JobDetail_PK" primary key("TxSeq", "ExecDate", "JobCode", "StepId");

alter table "JobDetail" add constraint "JobDetail_JobMain_FK1" foreign key ("TxSeq", "ExecDate", "JobCode") references "JobMain" ("TxSeq", "ExecDate", "JobCode") on delete cascade;

comment on table "JobDetail" is '批次工作明細檔';
comment on column "JobDetail"."TxSeq" is '交易序號';
comment on column "JobDetail"."ExecDate" is '批次執行日期';
comment on column "JobDetail"."JobCode" is '批次代號';
comment on column "JobDetail"."StepId" is 'StepId';
comment on column "JobDetail"."BatchType" is '類別';
comment on column "JobDetail"."Status" is '執行結果';
comment on column "JobDetail"."ErrCode" is '錯誤碼';
comment on column "JobDetail"."ErrContent" is '錯誤內容';
comment on column "JobDetail"."StepStartTime" is '啟動時間';
comment on column "JobDetail"."StepEndTime" is '結束時間';
comment on column "JobDetail"."CreateEmpNo" is '建檔人員';
comment on column "JobDetail"."CreateDate" is '建檔日期';
comment on column "JobDetail"."LastUpdateEmpNo" is '最後維護人員';
comment on column "JobDetail"."LastUpdate" is '最後維護日期';
