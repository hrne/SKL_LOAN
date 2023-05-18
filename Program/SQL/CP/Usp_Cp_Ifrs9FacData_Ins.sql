CREATE OR REPLACE PROCEDURE "Usp_Cp_Ifrs9FacData_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ifrs9FacData" DROP STORAGE';

  INSERT INTO "Ifrs9FacData" (
    "DataYM",
    "CustNo",
    "FacmNo",
    "ApplNo",
    "CustId",
    "DrawdownFg",
    "ApproveDate",
    "UtilDeadline",
    "FirstDrawdownDate",
    "MaturityDate",
    "LineAmt",
    "AcctFee",
    "LawFee",
    "FireFee",
    "GracePeriod",
    "AmortizedCode",
    "RateCode",
    "RepayFreq",
    "PayIntFreq",
    "Ifrs9StepProdCode",
    "IndustryCode",
    "ClTypeJCIC",
    "CityCode",
    "AreaCode",
    "Zip3",
    "ProdNo",
    "AgreementFg",
    "EntCode",
    "AssetClass",
    "Ifrs9ProdCode",
    "EvaAmt",
    "UtilAmt",
    "UtilBal",
    "TotalLoanBal",
    "RecycleCode",
    "IrrevocableFlag",
    "TempAmt",
    "AcBookCode",
    "AcSubBookCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "CustNo",
    "FacmNo",
    "ApplNo",
    "CustId",
    "DrawdownFg",
    "ApproveDate",
    "UtilDeadline",
    "FirstDrawdownDate",
    "MaturityDate",
    "LineAmt",
    "AcctFee",
    "LawFee",
    "FireFee",
    "GracePeriod",
    "AmortizedCode",
    "RateCode",
    "RepayFreq",
    "PayIntFreq",
    "Ifrs9StepProdCode",
    "IndustryCode",
    "ClTypeJCIC",
    "CityCode",
    "AreaCode",
    "Zip3",
    "ProdNo",
    "AgreementFg",
    "EntCode",
    "AssetClass",
    "Ifrs9ProdCode",
    "EvaAmt",
    "UtilAmt",
    "UtilBal",
    "TotalLoanBal",
    "RecycleCode",
    "IrrevocableFlag",
    "TempAmt",
    "AcBookCode",
    "AcSubBookCode",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."Ifrs9FacData";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Ifrs9FacData_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;