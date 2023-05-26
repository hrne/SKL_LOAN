drop table "TxFtpUser" purge;

create table "TxFtpUser" (
  "UserId" varchar2(64) not null,
  "UserPassword" varchar2(64) not null,
  "HomeDirectory" varchar2(128) not null,
  "EnableFlag" varchar2(1) default 'Y' not null,
  "WritePermission" varchar2(1) default 'Y' not null,
  "IdleTime" decimal(19, 0) default 0 not null,
  "UploadRate" decimal(19, 0) default 0 not null,
  "DownloadRate" decimal(19, 0) default 0 not null,
  "MaxloginNumber" decimal(19, 0) default 0 not null,
  "MaxloginPerIp" decimal(19, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxFtpUser" add constraint "TxFtpUser_PK" primary key("UserId");

comment on table "TxFtpUser" is 'FTP權限檔';
comment on column "TxFtpUser"."UserId" is '使用者ID';
comment on column "TxFtpUser"."UserPassword" is '使用者密碼';
comment on column "TxFtpUser"."HomeDirectory" is '根目錄';
comment on column "TxFtpUser"."EnableFlag" is '啟用記號';
comment on column "TxFtpUser"."WritePermission" is '寫入權限';
comment on column "TxFtpUser"."IdleTime" is 'Idel時間';
comment on column "TxFtpUser"."UploadRate" is '上傳大小限制';
comment on column "TxFtpUser"."DownloadRate" is '下載大小限制';
comment on column "TxFtpUser"."MaxloginNumber" is '同一使用者同時登入數量';
comment on column "TxFtpUser"."MaxloginPerIp" is '不同IP同時登入限制';
comment on column "TxFtpUser"."CreateDate" is '建檔日期時間';
comment on column "TxFtpUser"."CreateEmpNo" is '建檔人員';
comment on column "TxFtpUser"."LastUpdate" is '最後更新日期時間';
comment on column "TxFtpUser"."LastUpdateEmpNo" is '最後更新人員';
