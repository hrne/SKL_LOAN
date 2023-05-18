CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ447_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ447" DROP STORAGE';

  INSERT INTO "JcicZ447" (
    "TranKey",
    "CustId",
    "SubmitKey",
    "ApplyDate",
    "CourtCode",
    "Civil323Amt",
    "TotalAmt",
    "SignDate",
    "FirstPayDate",
    "Period",
    "Rate",
    "MonthPayAmt",
    "PayAccount",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
)
  SELECT
    "TranKey",
    "CustId",
    "SubmitKey",
    "ApplyDate",
    "CourtCode",
    "Civil323Amt",
    "TotalAmt",
    "SignDate",
    "FirstPayDate",
    "Period",
    "Rate",
    "MonthPayAmt",
    "PayAccount",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ447";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ447_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;