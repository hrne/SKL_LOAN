ALTER TABLE "FacProdAcctFee" DROP PRIMARY KEY;
ALTER TABLE "FacProdAcctFee" ADD "FeeType" varchar2(1) ;
comment on column "FacProdAcctFee"."FeeType" is '費用類別';
alter table "FacProdAcctFee" add constraint "FacProdAcctFee_PK" primary key("ProdNo", "FeeType", "LoanLow");

UPDATE "FacProdAcctFee" SET "FeeType" ='1' WHERE "FeeType" IS NULL;

