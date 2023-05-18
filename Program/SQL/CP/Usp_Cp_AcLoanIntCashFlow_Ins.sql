CREATE OR REPLACE PROCEDURE "Usp_Cp_AcLoanIntCashFlow_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcLoanIntCashFlow" DROP STORAGE';

  INSERT INTO "AcLoanIntCashFlow" (
    "YearMonth",
    "CustNo",
    "FacmNo",
    "BormNo",
    "TermNo",
    "IntStartDate",
    "IntEndDate",
    "Amount",
    "IntRate",
    "Principal",
    "Interest",
    "DelayInt",
    "BreachAmt",
    "IntCalcCode",
    "AmortizedCode",
    "AcctCode",
    "PayIntDate",
    "LoanBal",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "YearMonth",
    "CustNo",
    "FacmNo",
    "BormNo",
    "TermNo",
    "IntStartDate",
    "IntEndDate",
    "Amount",
    "IntRate",
    "Principal",
    "Interest",
    "DelayInt",
    "BreachAmt",
    "IntCalcCode",
    "AmortizedCode",
    "AcctCode",
    "PayIntDate",
    "LoanBal",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."AcLoanIntCashFlow";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AcLoanIntCashFlow_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;