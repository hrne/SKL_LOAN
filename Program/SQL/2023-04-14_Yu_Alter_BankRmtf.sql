ALTER TABLE "BankRmtf" ADD "TitaEntdy" decimal(8, 0) default 0 not null ;
comment on column "BankRmtf"."TitaEntdy" is '作帳日';