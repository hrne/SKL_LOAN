drop table "TxInquiry" purge;

drop sequence "TxInquiry_SEQ";

create table "TxInquiry" (
  "LogNo" decimal(11,0) not null,
  "Entdy" decimal(8, 0) default 0 not null,
  "CalDate" decimal(8, 0) default 0 not null,
  "BrNo" varchar2(4),
  "TlrNo" varchar2(6),
  "SupNo" varchar2(6),
  "TranNo" varchar2(5),
  "MrKey" varchar2(20),
  "CustNo" decimal(7, 0) default 0 not null,
  "TxResult" varchar2(1),
  "MsgId" varchar2(5),
  "ErrMsg" nvarchar2(300),
  "TranData" clob,
  "ImportFg" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxInquiry" add constraint "TxInquiry_PK" primary key("LogNo");

create sequence "TxInquiry_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "TxInquiry_Index1" on "TxInquiry"("CalDate" asc);

comment on table "TxInquiry" is '查詢紀錄檔';
comment on column "TxInquiry"."LogNo" is '序號';
comment on column "TxInquiry"."Entdy" is '會計日';
comment on column "TxInquiry"."CalDate" is '交易日期';
comment on column "TxInquiry"."BrNo" is '單位';
comment on column "TxInquiry"."TlrNo" is '使用者編號';
comment on column "TxInquiry"."SupNo" is '主管編號';
comment on column "TxInquiry"."TranNo" is '交易代號';
comment on column "TxInquiry"."MrKey" is '交易編號/帳號';
comment on column "TxInquiry"."CustNo" is '戶號';
comment on column "TxInquiry"."TxResult" is '交易結果';
comment on column "TxInquiry"."MsgId" is '訊息代號';
comment on column "TxInquiry"."ErrMsg" is '錯誤訊息';
comment on column "TxInquiry"."TranData" is '交易完整電文';
comment on column "TxInquiry"."ImportFg" is '交易重要註記';
comment on column "TxInquiry"."CreateDate" is '建檔日期時間';
comment on column "TxInquiry"."CreateEmpNo" is '建檔人員';
comment on column "TxInquiry"."LastUpdate" is '最後更新日期時間';
comment on column "TxInquiry"."LastUpdateEmpNo" is '最後更新人員';
