CREATE OR REPLACE PROCEDURE "Usp_Cp_CdCashFlow_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdCashFlow" DROP STORAGE';

  INSERT INTO "CdCashFlow" (
    "DataYearMonth",
    "InterestIncome",
    "PrincipalAmortizeAmt",
    "PrepaymentAmt",
    "DuePaymentAmt",
    "ExtendAmt",
    "LoanAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYearMonth",
    "InterestIncome",
    "PrincipalAmortizeAmt",
    "PrepaymentAmt",
    "DuePaymentAmt",
    "ExtendAmt",
    "LoanAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdCashFlow";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdCashFlow_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;