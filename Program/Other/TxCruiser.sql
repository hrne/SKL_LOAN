drop table itxadmin."TxCruiser" purge;

create table itxadmin."TxCruiser" (
  "TxSeq" varchar2(20),
  "TlrNo" varchar2(6),
  "TxCode" varchar2(10),
  "JobList" varchar2(800),
  "Parameter" clob,
  "Status" varchar2(1),
  "SendMSgChainOff" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table itxadmin."TxCruiser" add constraint "TxCruiser_PK" primary key("TxSeq", "TlrNo");

create index "TxCruiser_Index1" on itxadmin."TxCruiser"("Status" asc);

comment on table itxadmin."TxCruiser" is '批次發動交易紀錄';
comment on column itxadmin."TxCruiser"."TxSeq" is '交易序號';
comment on column itxadmin."TxCruiser"."TlrNo" is '發動經辦';
comment on column itxadmin."TxCruiser"."TxCode" is '發動交易';
comment on column itxadmin."TxCruiser"."JobList" is '批次執行清單';
comment on column itxadmin."TxCruiser"."Parameter" is '參數';
comment on column itxadmin."TxCruiser"."Status" is '執行狀態';
comment on column itxadmin."TxCruiser"."SendMSgChainOff" is '關閉訊息通知交易記號';
comment on column itxadmin."TxCruiser"."CreateDate" is '建檔日期時間';
comment on column itxadmin."TxCruiser"."CreateEmpNo" is '建檔人員';
comment on column itxadmin."TxCruiser"."LastUpdate" is '最後更新日期時間';
comment on column itxadmin."TxCruiser"."LastUpdateEmpNo" is '最後更新人員';
