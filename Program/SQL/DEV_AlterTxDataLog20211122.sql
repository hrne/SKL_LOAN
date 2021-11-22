ALTER TABLE "TxDataLog" ADD "MrKey" VARCHAR2(50) null;
comment on column "TxDataLog"."MrKey" is '交易編號/帳號';
