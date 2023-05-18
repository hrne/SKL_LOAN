CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanIfrs9Ip_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanIfrs9Ip" DROP STORAGE';

  INSERT INTO "LoanIfrs9Ip" (
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "ApplNo",
    "DrawdownFg",
    "ApproveDate",
    "FirstDrawdownDate",
    "LineAmt",
    "AcctFee",
    "Fee",
    "ApproveRate",
    "GracePeriod",
    "AmortizedCode",
    "RateCode",
    "RepayFreq",
    "PayIntFreq",
    "IndustryCode",
    "ClTypeJCIC",
    "CityCode",
    "ProdNo",
    "CustKind",
    "Ifrs9ProdCode",
    "EvaAmt",
    "AvblBal",
    "RecycleCode",
    "IrrevocableFlag",
    "LoanTerm",
    "AcCode",
    "AcCurcd",
    "AcBookCode",
    "CurrencyCode",
    "ExchangeRate",
    "LineAmtCurr",
    "AcctFeeCurr",
    "FeeCurr",
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
    "DrawdownFg",
    "ApproveDate",
    "FirstDrawdownDate",
    "LineAmt",
    "AcctFee",
    "Fee",
    "ApproveRate",
    "GracePeriod",
    "AmortizedCode",
    "RateCode",
    "RepayFreq",
    "PayIntFreq",
    "IndustryCode",
    "ClTypeJCIC",
    "CityCode",
    "ProdNo",
    "CustKind",
    "Ifrs9ProdCode",
    "EvaAmt",
    "AvblBal",
    "RecycleCode",
    "IrrevocableFlag",
    "LoanTerm",
    "AcCode",
    "AcCurcd",
    "AcBookCode",
    "CurrencyCode",
    "ExchangeRate",
    "LineAmtCurr",
    "AcctFeeCurr",
    "FeeCurr",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanIfrs9Ip";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanIfrs9Ip_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;