CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ573_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ573" DROP STORAGE';

  INSERT INTO "JcicZ573" (
    "TranKey",
    "CustId",
    "SubmitKey",
    "ApplyDate",
    "PayDate",
    "PayAmt",
    "TotalPayAmt",
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
    "PayDate",
    "PayAmt",
    "TotalPayAmt",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ573";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ573_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;