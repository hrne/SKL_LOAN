drop table "TxAmlCredit" purge;

create table "TxAmlCredit" (
  "DataDt" decimal(8, 0) default 0 not null,
  "CustKey" varchar2(10),
  "RRSeq" decimal(20, 0) default 0 not null,
  "ReviewType" varchar2(1),
  "Unit" varchar2(6),
  "IsStatus" decimal(2, 0) default 0 not null,
  "WlfConfirmStatus" varchar2(1),
  "ProcessType" varchar2(1),
  "ProcessCount" decimal(2, 0) default 0 not null,
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

alter table "TxAmlCredit" add constraint "TxAmlCredit_PK" primary key("DataDt", "CustKey");

create index "TxAmlCredit_Index1" on "TxAmlCredit"("ReviewType" asc, "DataDt" asc);

comment on table "TxAmlCredit" is 'AML定審資料';
comment on column "TxAmlCredit"."DataDt" is '定審日期';
comment on column "TxAmlCredit"."CustKey" is '身分證字號';
comment on column "TxAmlCredit"."RRSeq" is 'AML流水號';
comment on column "TxAmlCredit"."ReviewType" is '評級';
comment on column "TxAmlCredit"."Unit" is '單位代號';
comment on column "TxAmlCredit"."IsStatus" is '定審狀態';
comment on column "TxAmlCredit"."WlfConfirmStatus" is '名單類型';
comment on column "TxAmlCredit"."ProcessType" is '通知類別';
comment on column "TxAmlCredit"."ProcessCount" is '通知次數';
comment on column "TxAmlCredit"."ProcessBrNo" is '最後通知單位';
comment on column "TxAmlCredit"."ProcessGroupNo" is '最後通知科組別';
comment on column "TxAmlCredit"."ProcessTlrNo" is '最後通知經辦';
comment on column "TxAmlCredit"."ProcessDate" is '最後通知日期';
comment on column "TxAmlCredit"."ProcessMobile" is '最後通知簡訊電話';
comment on column "TxAmlCredit"."ProcessAddress" is '最後通知郵寄地址';
comment on column "TxAmlCredit"."ProcessName" is '最後通知郵寄名稱';
comment on column "TxAmlCredit"."ProcessNote" is '最後通知備註';
comment on column "TxAmlCredit"."CreateDate" is '建檔日期時間';
comment on column "TxAmlCredit"."CreateEmpNo" is '建檔人員';
comment on column "TxAmlCredit"."LastUpdate" is '最後更新日期時間';
comment on column "TxAmlCredit"."LastUpdateEmpNo" is '最後更新人員';
