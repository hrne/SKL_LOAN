ALTER TABLE "EmpDeductDtl" ADD "JsonFields" NVARCHAR2(300);
comment on column "EmpDeductDtl"."JsonFields" is 'jason格式紀錄欄';
ALTER TABLE "EmpDeductDtl" DROP CONSTRAINT "EmpDeductDtl_PK" CASCADE;
alter table "EmpDeductDtl" add constraint "EmpDeductDtl_PK" primary key("EntryDate", "CustNo", "AchRepayCode", "PerfMonth", "ProcCode", "RepayCode", "AcctCode", "FacmNo", "BormNo");
