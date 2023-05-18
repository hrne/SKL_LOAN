CREATE OR REPLACE PROCEDURE "Usp_Cp_Ias34Ap_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias34Ap" DROP STORAGE';

  INSERT INTO "Ias34Ap" (
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
    "DerCode",
    "GracePeriod",
    "ApproveRate",
    "AmortizedCode",
    "RateCode",
    "RepayFreq",
    "PayIntFreq",
    "IndustryCode",
    "ClTypeJCIC",
    "Zip3",
    "ProdNo",
    "CustKind",
    "AssetClass",
    "Ifrs9ProdCode",
    "EvaAmt",
    "FirstDueDate",
    "TotalPeriod",
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
    "DerCode",
    "GracePeriod",
    "ApproveRate",
    "AmortizedCode",
    "RateCode",
    "RepayFreq",
    "PayIntFreq",
    "IndustryCode",
    "ClTypeJCIC",
    "Zip3",
    "ProdNo",
    "CustKind",
    "AssetClass",
    "Ifrs9ProdCode",
    "EvaAmt",
    "FirstDueDate",
    "TotalPeriod",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."Ias34Ap";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Ias34Ap_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;