alter table "UspErrorLog" add "JobTxSeq" varchar2(20);
comment on column "UspErrorLog"."JobTxSeq" is '啟動批次的交易序號';