ALTER TABLE "LoanBorMain" ADD "PieceCodeSecond" VARCHAR2(5) NULL;
ALTER TABLE "LoanBorMain" ADD "PieceCodeSecondAmt"  decimal(16, 2) default 0 not null;
comment on column "LoanBorMain"."PieceCodeSecond" is '計件代碼2';
comment on column "LoanBorMain"."PieceCodeSecondAmt" is '計件代碼2金額';
