
    ALTER TABLE "LoanBorMain" ADD "HandlingFee" DECIMAL(16,2) DEFAULT 0 NOT NULL ;
comment on column "LoanBorMain"."HandlingFee" is '手續費';