drop table "TxArchiveTableLog" purge;

create table "TxArchiveTableLog" (
  "Type" varchar2(4),
  "DataFrom" varchar2(7),
  "DataTo" varchar2(7),
  "ExecuteDate" decimal(8, 0) default 0 not null,
  "TableName" varchar2(30),
  "BatchNo" decimal(3, 0) default 0 not null,
  "Result" decimal(1, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "Description" nvarchar2(200),
  "Records" decimal(7, 0) default 0 not null,
  "IsDeleted" decimal(1, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxArchiveTableLog" add constraint "TxArchiveTableLog_PK" primary key("Type", "DataFrom", "DataTo", "ExecuteDate", "TableName", "BatchNo", "CustNo", "FacmNo", "BormNo");

comment on table "TxArchiveTableLog" is '歷史封存表紀錄檔';
comment on column "TxArchiveTableLog"."Type" is '分類';
comment on column "TxArchiveTableLog"."DataFrom" is '搬運來源環境';
comment on column "TxArchiveTableLog"."DataTo" is '搬運目標環境';
comment on column "TxArchiveTableLog"."ExecuteDate" is '執行日期';
comment on column "TxArchiveTableLog"."TableName" is '資料表名稱';
comment on column "TxArchiveTableLog"."BatchNo" is '執行批次';
comment on column "TxArchiveTableLog"."Result" is '是否成功';
comment on column "TxArchiveTableLog"."CustNo" is '戶號';
comment on column "TxArchiveTableLog"."FacmNo" is '額度';
comment on column "TxArchiveTableLog"."BormNo" is '撥款序號';
comment on column "TxArchiveTableLog"."Description" is '執行結果說明';
comment on column "TxArchiveTableLog"."Records" is '搬運資料筆數';
comment on column "TxArchiveTableLog"."IsDeleted" is '已自連線環境刪除';
comment on column "TxArchiveTableLog"."CreateDate" is '建檔日期時間';
comment on column "TxArchiveTableLog"."CreateEmpNo" is '建檔人員';
comment on column "TxArchiveTableLog"."LastUpdate" is '最後更新日期時間';
comment on column "TxArchiveTableLog"."LastUpdateEmpNo" is '最後更新人員';
