alter table "BankDeductDtl" drop constraint "BankDeductDtl_PK";
alter table "BankDeductDtl" drop column "BormNo";
alter table "BankDeductDtl" add constraint "BankDeductDtl_PK" primary key("EntryDate", "CustNo", "FacmNo", "RepayType", "PayIntDate");
commit;