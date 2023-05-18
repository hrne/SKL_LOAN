CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanIfrs9Ap_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanIfrs9Ap" DROP STORAGE';

  INSERT INTO "LoanIfrs9Ap" (
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "ApplNo",
    "BormNo",
    "AcCode",
    "Status",
    "FirstDrawdownDate",
    "DrawdownDate",
    "FacLineDate",
    "MaturityDate",
    "LineAmt",
    "DrawdownAmt",
    "AcctFee",
    "LoanBal",
    "IntAmt",
    "Fee",
    "Rate",
    "OvduDays",
    "OvduDate",
    "BadDebtDate",
    "BadDebtAmt",
    "GracePeriod",
    "ApproveRate",
    "AmortizedCode",
    "RateCode",
    "RepayFreq",
    "PayIntFreq",
    "IndustryCode",
    "ClTypeJCIC",
    "CityCode",
    "ProdNo",
    "CustKind",
    "AssetClass",
    "Ifrs9ProdCode",
    "EvaAmt",
    "FirstDueDate",
    "TotalPeriod",
    "AvblBal",
    "RecycleCode",
    "IrrevocableFlag",
    "TempAmt",
    "AcCurcd",
    "AcBookCode",
    "CurrencyCode",
    "ExchangeRate",
    "LineAmtCurr",
    "DrawdownAmtCurr",
    "AcctFeeCurr",
    "LoanBalCurr",
    "IntAmtCurr",
    "FeeCurr",
    "AvblBalCurr",
    "TempAmtCurr",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "ApplNo",
    "BormNo",
    "AcCode",
    "Status",
    "FirstDrawdownDate",
    "DrawdownDate",
    "FacLineDate",
    "MaturityDate",
    "LineAmt",
    "DrawdownAmt",
    "AcctFee",
    "LoanBal",
    "IntAmt",
    "Fee",
    "Rate",
    "OvduDays",
    "OvduDate",
    "BadDebtDate",
    "BadDebtAmt",
    "GracePeriod",
    "ApproveRate",
    "AmortizedCode",
    "RateCode",
    "RepayFreq",
    "PayIntFreq",
    "IndustryCode",
    "ClTypeJCIC",
    "CityCode",
    "ProdNo",
    "CustKind",
    "AssetClass",
    "Ifrs9ProdCode",
    "EvaAmt",
    "FirstDueDate",
    "TotalPeriod",
    "AvblBal",
    "RecycleCode",
    "IrrevocableFlag",
    "TempAmt",
    "AcCurcd",
    "AcBookCode",
    "CurrencyCode",
    "ExchangeRate",
    "LineAmtCurr",
    "DrawdownAmtCurr",
    "AcctFeeCurr",
    "LoanBalCurr",
    "IntAmtCurr",
    "FeeCurr",
    "AvblBalCurr",
    "TempAmtCurr",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanIfrs9Ap";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanIfrs9Ap_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;