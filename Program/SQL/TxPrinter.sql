drop table "TxPrinter" purge;

create table "TxPrinter" (
  "StanIp" varchar2(15),
  "FileCode" nvarchar2(40),
  "ServerIp" varchar2(15),
  "Printer" nvarchar2(100),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxPrinter" add constraint "TxPrinter_PK" primary key("StanIp", "FileCode");

comment on table "TxPrinter" is '印表機設定檔';
comment on column "TxPrinter"."StanIp" is '工作站IP';
comment on column "TxPrinter"."FileCode" is '檔案編號';
comment on column "TxPrinter"."ServerIp" is '印表機伺服器IP';
comment on column "TxPrinter"."Printer" is '預設印表機';
comment on column "TxPrinter"."CreateDate" is '建檔日期時間';
comment on column "TxPrinter"."CreateEmpNo" is '建檔人員';
comment on column "TxPrinter"."LastUpdate" is '最後更新日期時間';
comment on column "TxPrinter"."LastUpdateEmpNo" is '最後更新人員';
