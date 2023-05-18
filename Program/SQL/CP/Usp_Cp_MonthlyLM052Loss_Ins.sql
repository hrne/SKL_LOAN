CREATE OR REPLACE PROCEDURE "Usp_Cp_MonthlyLM052Loss_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "MonthlyLM052Loss" DROP STORAGE';

  INSERT INTO "MonthlyLM052Loss" (
    "YearMonth",
    "AssetEvaTotal",
    "LegalLoss",
    "ApprovedLoss",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "YearMonth",
    "AssetEvaTotal",
    "LegalLoss",
    "ApprovedLoss",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."MonthlyLM052Loss";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_MonthlyLM052Loss_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;