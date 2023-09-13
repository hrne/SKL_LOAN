ALTER TABLE "MonthlyLM052AssetClass" MODIFY "StorageRate" decimal(6,4);
ALTER TABLE "MonthlyLM042RBC" MODIFY "RiskFactorAmount" decimal(6,4);
ALTER TABLE "MonthlyLM042RBC" ADD "LoanBal" decimal(16,2);
comment on column "MonthlyLM042RBC"."LoanBal" is '放款餘額';
ALTER TABLE "MonthlyLM042RBC" ADD "ReserveLossRate" decimal(6,4);
comment on column "MonthlyLM042RBC"."ReserveLossRate" is '備呆提存比率';
ALTER TABLE "MonthlyLM042RBC" ADD "ReserveLossAmt" decimal(16,2);
comment on column "MonthlyLM042RBC"."ReserveLossAmt" is '備呆金額';
