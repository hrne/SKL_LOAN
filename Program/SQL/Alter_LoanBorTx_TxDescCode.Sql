
    ALTER TABLE "LoanBorTx" ADD "TxDescCode" VARCHAR2(4) NULL;
comment on column "LoanBorTx"."TxDescCode" is '交易別代碼';