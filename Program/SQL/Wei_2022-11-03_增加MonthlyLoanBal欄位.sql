ALTER TABLE "MonthlyLoanBal" ADD "SumRcvPrin" decimal(16, 2) default 0 not null;
ALTER TABLE "MonthlyLoanBal" ADD "OvduRcvAmt" decimal(16, 2) default 0 not null;
ALTER TABLE "MonthlyLoanBal" ADD "BadDebtAmt" decimal(16, 2) default 0 not null;
ALTER TABLE "MonthlyLoanBal" ADD "PrevPayIntDate" decimal(8, 0) default 0 not null;