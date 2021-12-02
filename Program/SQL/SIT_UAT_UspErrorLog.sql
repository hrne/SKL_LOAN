drop table "UspErrorLog" purge;

create table "UspErrorLog" (
  "LogUkey" varchar2(32),
  "LogDate" decimal(8, 0) default 0 not null,
  "LogTime" decimal(6, 0) default 0 not null,
  "UspName" varchar2(100),
  "ErrorCode" decimal(14, 0) default 0 not null,
  "ErrorMessage" varchar2(1500),
  "ErrorBackTrace" varchar2(1500),
  "ExecEmpNo" varchar2(6),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "UspErrorLog" add constraint "UspErrorLog_PK" primary key("LogUkey");

create index "UspErrorLog_Index1" on "UspErrorLog"("LogDate DESC" asc, "LogTime DESC" asc, "UspName DESC" asc);

comment on table "UspErrorLog" is '預存程序錯誤記錄檔';
comment on column "UspErrorLog"."LogUkey" is '記錄識別碼';
comment on column "UspErrorLog"."LogDate" is '記錄日期';
comment on column "UspErrorLog"."LogTime" is '記錄時間';
comment on column "UspErrorLog"."UspName" is '預存程序名稱';
comment on column "UspErrorLog"."ErrorCode" is '錯誤代碼';
comment on column "UspErrorLog"."ErrorMessage" is '錯誤訊息';
comment on column "UspErrorLog"."ErrorBackTrace" is 'ErrorBackTrace';
comment on column "UspErrorLog"."ExecEmpNo" is '啟動人員員編';
comment on column "UspErrorLog"."CreateDate" is '建檔日期時間';
comment on column "UspErrorLog"."CreateEmpNo" is '建檔人員';
comment on column "UspErrorLog"."LastUpdate" is '最後更新日期時間';
comment on column "UspErrorLog"."LastUpdateEmpNo" is '最後更新人員';
