
ALTER TABLE "LoanCheque" ADD "BankItem" NVARCHAR2(50) NULL;
ALTER TABLE "LoanCheque" ADD "BranchItem" NVARCHAR2(50) NULL;
comment on column "LoanCheque"."BankItem" is '支票銀行';
comment on column "LoanCheque"."BranchItem" is '支票分行';