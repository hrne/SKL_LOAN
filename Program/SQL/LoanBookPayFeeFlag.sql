    ALTER TABLE "LoanBook" ADD "IncludeFeeFlag" VARCHAR2(1) NULL;
    comment on column "LoanBook"."IncludeFeeFlag" is '是否內含費用';
    
    
   