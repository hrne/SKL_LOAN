ALTER TABLE "MonthlyLM052LoanAsset" ADD "StorageRate" decimal(6,4);
comment on column "MonthlyLM052LoanAsset"."StorageRate" is '提存比率';
ALTER TABLE "MonthlyLM052LoanAsset" ADD "StorageAmt" decimal(16,2);
comment on column "MonthlyLM052LoanAsset"."StorageAmt" is '提存金額';
