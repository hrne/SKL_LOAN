

ALTER TABLE "LoanBorTx" ADD "AcSeq" DECIMAL(4, 0)default 0 not null;
  
update "LoanBorTx" SET "AcSeq" = NVL(JSON_VALUE(ln3."OtherFields", '$.AcSeq'),0) ;