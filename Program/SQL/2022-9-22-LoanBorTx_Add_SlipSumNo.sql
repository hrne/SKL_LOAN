ALTER TABLE "LoanBorTx" 
ADD (
    "SlipSumNo" decimal(2, 0) default 0 not null
);
comment on column "LoanBorTx"."SlipSumNo" is '彙總傳票批號';