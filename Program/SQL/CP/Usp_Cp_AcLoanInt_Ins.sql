CREATE OR REPLACE PROCEDURE "Usp_Cp_AcLoanInt_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcLoanInt" DROP STORAGE';

  INSERT INTO "AcLoanInt" (
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
    "RateIncr",
    "IndividualIncr",
    "AcctCode",
    "PayIntDate",
    "LoanBal",
    "Aging",
    "AcBookCode",
    "AcSubBookCode",
    "BranchNo",
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
    "RateIncr",
    "IndividualIncr",
    "AcctCode",
    "PayIntDate",
    "LoanBal",
    "Aging",
    "AcBookCode",
    "AcSubBookCode",
    "BranchNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."AcLoanInt";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AcLoanInt_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;