

ALTER TABLE "LoanBorTx" ADD "AcctCode" VARCHAR2(3) NULL;
comment on column "LoanBorTx"."AcctCode" is '業務科目';

UPDATE "LoanBorTx" SET "AcctCode" = JSON_VALUE("OtherFields", '$.AcctCode');

