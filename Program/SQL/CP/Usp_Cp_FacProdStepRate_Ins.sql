CREATE OR REPLACE PROCEDURE "Usp_Cp_FacProdStepRate_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FacProdStepRate" DROP STORAGE';

  INSERT INTO "FacProdStepRate" (
    "ProdNo",
    "MonthStart",
    "MonthEnd",
    "RateType",
    "RateIncr",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ProdNo",
    "MonthStart",
    "MonthEnd",
    "RateType",
    "RateIncr",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FacProdStepRate";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FacProdStepRate_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;