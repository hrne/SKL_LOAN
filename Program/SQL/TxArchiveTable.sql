drop table "TxArchiveTable" purge;

create table "TxArchiveTable" (
  "Type" varchar2(4),
  "TableName" varchar2(30),
  "Enabled" decimal(1, 0) default 0 not null,
  "Conditions" decimal(1, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxArchiveTable" add constraint "TxArchiveTable_PK" primary key("Type", "TableName");

comment on table "TxArchiveTable" is '歷史封存表設定檔';
comment on column "TxArchiveTable"."Type" is '分類';
comment on column "TxArchiveTable"."TableName" is '資料表名稱';
comment on column "TxArchiveTable"."Enabled" is '啟用記號';
comment on column "TxArchiveTable"."Conditions" is '搬運條件';
comment on column "TxArchiveTable"."CreateDate" is '建檔日期時間';
comment on column "TxArchiveTable"."CreateEmpNo" is '建檔人員';
comment on column "TxArchiveTable"."LastUpdate" is '最後更新日期時間';
comment on column "TxArchiveTable"."LastUpdateEmpNo" is '最後更新人員';
