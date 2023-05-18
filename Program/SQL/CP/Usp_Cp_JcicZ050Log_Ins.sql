CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ050Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ050Log" DROP STORAGE';

  INSERT INTO "JcicZ050Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "PayAmt",
    "SumRepayActualAmt",
    "SumRepayShouldAmt",
    "Status",
    "OutJcicTxtDate",
    "SecondRepayYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "Ukey",
    "TxSeq",
    "TranKey",
    "PayAmt",
    "SumRepayActualAmt",
    "SumRepayShouldAmt",
    "Status",
    "OutJcicTxtDate",
    "SecondRepayYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ050Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ050Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;