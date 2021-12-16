drop table "TxAmlNotice" purge;

create table "TxAmlNotice" (
  "DataDt" decimal(8, 0) default 0 not null,
  "CustKey" varchar2(10),
  "ProcessSno" decimal(2, 0) default 0 not null,
  "ReviewType" varchar2(1),
  "ProcessType" varchar2(1),
  "ProcessBrNo" varchar2(4),
  "ProcessGroupNo" varchar2(1),
  "ProcessTlrNo" varchar2(6),
  "ProcessDate" decimal(8, 0) default 0 not null,
  "ProcessMobile" varchar2(10),
  "ProcessAddress" nvarchar2(100),
  "ProcessName" nvarchar2(20),
  "ProcessNote" nvarchar2(100),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxAmlNotice" add constraint "TxAmlNotice_PK" primary key("DataDt", "CustKey", "ProcessSno");

comment on table "TxAmlNotice" is 'AML定審通知紀錄檔';
comment on column "TxAmlNotice"."DataDt" is '定審日期';
comment on column "TxAmlNotice"."CustKey" is '身分證字號';
comment on column "TxAmlNotice"."ProcessSno" is '通知序號';
comment on column "TxAmlNotice"."ReviewType" is '評級';
comment on column "TxAmlNotice"."ProcessType" is '通知類別';
comment on column "TxAmlNotice"."ProcessBrNo" is '通知單位';
comment on column "TxAmlNotice"."ProcessGroupNo" is '最後通知科組別';
comment on column "TxAmlNotice"."ProcessTlrNo" is '通知經辦';
comment on column "TxAmlNotice"."ProcessDate" is '通知日期';
comment on column "TxAmlNotice"."ProcessMobile" is '最後通知簡訊電話';
comment on column "TxAmlNotice"."ProcessAddress" is '最後通知郵寄地址';
comment on column "TxAmlNotice"."ProcessName" is '最後通知郵寄名稱';
comment on column "TxAmlNotice"."ProcessNote" is '通知備註';
comment on column "TxAmlNotice"."CreateDate" is '建檔日期時間';
comment on column "TxAmlNotice"."CreateEmpNo" is '建檔人員';
comment on column "TxAmlNotice"."LastUpdate" is '最後更新日期時間';
comment on column "TxAmlNotice"."LastUpdateEmpNo" is '最後更新人員';
