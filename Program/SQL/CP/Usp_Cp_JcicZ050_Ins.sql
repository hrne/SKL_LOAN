CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ050_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ050" DROP STORAGE';

  INSERT INTO "JcicZ050" (
    "TranKey",
    "SubmitKey",
    "CustId",
    "RcDate",
    "PayDate",
    "PayAmt",
    "SumRepayActualAmt",
    "SumRepayShouldAmt",
    "Status",
    "OutJcicTxtDate",
    "Ukey",
    "SecondRepayYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
)
  SELECT
    "TranKey",
    "SubmitKey",
    "CustId",
    "RcDate",
    "PayDate",
    "PayAmt",
    "SumRepayActualAmt",
    "SumRepayShouldAmt",
    "Status",
    "OutJcicTxtDate",
    "Ukey",
    "SecondRepayYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ050";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ050_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;