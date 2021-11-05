ALTER TABLE "TxRecord" ADD "ImportFg" varchar2(1);

comment on column "TxRecord"."ImportFg" is '交易重要註記';
