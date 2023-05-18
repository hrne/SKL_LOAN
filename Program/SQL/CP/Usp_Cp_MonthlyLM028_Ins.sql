CREATE OR REPLACE PROCEDURE "Usp_Cp_MonthlyLM028_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "MonthlyLM028" DROP STORAGE';

  INSERT INTO "MonthlyLM028" (
    "DataMonth",
    "Status",
    "EntCode",
    "BranchNo",
    "CustNo",
    "FacmNo",
    "BormNo",
    "StoreRate",
    "PayIntFreq",
    "MaturityYear",
    "MaturityMonth",
    "MaturityDay",
    "LoanBal",
    "RateCode",
    "PostDepCode",
    "SpecificDd",
    "FirstRateAdjFreq",
    "ProdNo",
    "FitRate1",
    "FitRate2",
    "FitRate3",
    "FitRate4",
    "FitRate5",
    "ClCode1",
    "ClCode2",
    "DrawdownYear",
    "DrawdownMonth",
    "DrawdownDay",
    "W08Code",
    "IsRelation",
    "AgType1",
    "AcctSource",
    "LastestRate",
    "LastestRateStartDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataMonth",
    "Status",
    "EntCode",
    "BranchNo",
    "CustNo",
    "FacmNo",
    "BormNo",
    "StoreRate",
    "PayIntFreq",
    "MaturityYear",
    "MaturityMonth",
    "MaturityDay",
    "LoanBal",
    "RateCode",
    "PostDepCode",
    "SpecificDd",
    "FirstRateAdjFreq",
    "ProdNo",
    "FitRate1",
    "FitRate2",
    "FitRate3",
    "FitRate4",
    "FitRate5",
    "ClCode1",
    "ClCode2",
    "DrawdownYear",
    "DrawdownMonth",
    "DrawdownDay",
    "W08Code",
    "IsRelation",
    "AgType1",
    "AcctSource",
    "LastestRate",
    "LastestRateStartDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."MonthlyLM028";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_MonthlyLM028_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;