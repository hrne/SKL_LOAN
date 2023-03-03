drop table "TxApLog" purge;

drop sequence "TxApLog_SEQ";

create table "TxApLog" (
  "AutoSeq" decimal(11,0) not null,
  "UserID" varchar2(8),
  "IDNumber" varchar2(12),
  "IDName" nvarchar2(24),
  "ActionEvent" decimal(1, 0) default 0 not null,
  "UserIP" varchar2(50),
  "SystemName" nvarchar2(20),
  "OperationName" nvarchar2(50),
  "ProgramName" varchar2(50),
  "MethodName" varchar2(50),
  "ServerName" varchar2(60),
  "ServerIP" varchar2(50),
  "InputDataforXMLorJson" clob,
  "OutputDataforXMLorJson" clob,
  "EnforcementResult" decimal(1, 0) default 0 not null,
  "Message" nvarchar2(200),
  "Entdy" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxApLog" add constraint "TxApLog_PK" primary key("AutoSeq");

create sequence "TxApLog_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "TxApLog_Index1" on "TxApLog"("Entdy" asc, "UserID" asc);

comment on table "TxApLog" is 'ApLog敏感資料查詢紀錄檔';
comment on column "TxApLog"."AutoSeq" is '序號';
comment on column "TxApLog"."UserID" is '使用者員編';
comment on column "TxApLog"."IDNumber" is '使用者身份證字號';
comment on column "TxApLog"."IDName" is '使用者姓名';
comment on column "TxApLog"."ActionEvent" is 'Action事件';
comment on column "TxApLog"."UserIP" is '使用者IP';
comment on column "TxApLog"."SystemName" is '系統名稱';
comment on column "TxApLog"."OperationName" is '作業名稱';
comment on column "TxApLog"."ProgramName" is '程式名稱';
comment on column "TxApLog"."MethodName" is '方法名稱';
comment on column "TxApLog"."ServerName" is '伺服器名稱';
comment on column "TxApLog"."ServerIP" is '伺服器IP';
comment on column "TxApLog"."InputDataforXMLorJson" is '輸入的參數';
comment on column "TxApLog"."OutputDataforXMLorJson" is '輸出的結果';
comment on column "TxApLog"."EnforcementResult" is '事件執行結果';
comment on column "TxApLog"."Message" is '備註';
comment on column "TxApLog"."Entdy" is '交易日期';
comment on column "TxApLog"."CreateDate" is '建檔日期時間';
comment on column "TxApLog"."CreateEmpNo" is '建檔人員';
comment on column "TxApLog"."LastUpdate" is '最後更新日期時間';
comment on column "TxApLog"."LastUpdateEmpNo" is '最後更新人員';
