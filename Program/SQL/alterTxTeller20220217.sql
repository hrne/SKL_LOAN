ALTER TABLE "TxTeller" ADD "Station" VARCHAR2(3) NULL;
ALTER TABLE "TxTeller" ADD "MntDate" timestamp NULL;
ALTER TABLE "TxTeller" ADD "MntEmpNo" VARCHAR2(6) NULL;
comment on column "TxTeller"."Station" is '站別';
comment on column "TxTeller"."MntDate" is '最後維護時間';
comment on column "TxTeller"."MntEmpNo" is '最後維護人員';
