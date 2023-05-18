CREATE OR REPLACE PROCEDURE "Usp_Cp_MonthlyLoanBal_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "MonthlyLoanBal" DROP STORAGE';

  INSERT INTO "MonthlyLoanBal" (
    "YearMonth",
    "CustNo",
    "FacmNo",
    "BormNo",
    "AcctCode",
    "FacAcctCode",
    "CurrencyCode",
    "LoanBalance",
    "MaxLoanBal",
    "StoreRate",
    "IntAmtRcv",
    "IntAmtAcc",
    "UnpaidInt",
    "UnexpiredInt",
    "SumRcvInt",
    "IntAmt",
    "ProdNo",
    "AcBookCode",
    "EntCode",
    "RelsCode",
    "DepartmentCode",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "CityCode",
    "OvduPrinAmt",
    "OvduIntAmt",
    "SumRcvPrin",
    "OvduRcvAmt",
    "BadDebtAmt",
    "PrevPayIntDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "AcSubBookCode"
)
  SELECT
    "YearMonth",
    "CustNo",
    "FacmNo",
    "BormNo",
    "AcctCode",
    "FacAcctCode",
    "CurrencyCode",
    "LoanBalance",
    "MaxLoanBal",
    "StoreRate",
    "IntAmtRcv",
    "IntAmtAcc",
    "UnpaidInt",
    "UnexpiredInt",
    "SumRcvInt",
    "IntAmt",
    "ProdNo",
    "AcBookCode",
    "EntCode",
    "RelsCode",
    "DepartmentCode",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "CityCode",
    "OvduPrinAmt",
    "OvduIntAmt",
    "SumRcvPrin",
    "OvduRcvAmt",
    "BadDebtAmt",
    "PrevPayIntDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "AcSubBookCode"
  FROM ITXADMIN."MonthlyLoanBal";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_MonthlyLoanBal_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;