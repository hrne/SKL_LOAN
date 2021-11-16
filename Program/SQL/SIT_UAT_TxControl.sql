drop table "TxControl" purge;

create table "TxControl" (
  "Code" varchar2(50),
  "Desc" varchar2(50),
  "CreateEmpNo" varchar2(6),
  "CreateDate" timestamp,
  "LastUpdateEmpNo" varchar2(6),
  "LastUpdate" timestamp
);

alter table "TxControl" add constraint "TxControl_PK" primary key("Code");

comment on table "TxControl" is '作業流程控制檔';
comment on column "TxControl"."Code" is '控制項目';
comment on column "TxControl"."Desc" is '控制內容';
comment on column "TxControl"."CreateEmpNo" is '建立者櫃員編號';
comment on column "TxControl"."CreateDate" is '建立日期時間';
comment on column "TxControl"."LastUpdateEmpNo" is '修改者櫃員編號';
comment on column "TxControl"."LastUpdate" is '修改日期時間';
