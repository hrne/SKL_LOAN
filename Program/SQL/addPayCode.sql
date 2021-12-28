
ALTER TABLE "BankRemit" ADD "PayCode" VARCHAR2(1) NULL;
comment on column "BankRemit"."PayCode" is '付款狀況碼';