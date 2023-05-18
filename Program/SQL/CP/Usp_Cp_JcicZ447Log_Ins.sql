CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ447Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ447Log" DROP STORAGE';

  INSERT INTO "JcicZ447Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "Civil323Amt",
    "TotalAmt",
    "SignDate",
    "FirstPayDate",
    "Period",
    "Rate",
    "MonthPayAmt",
    "PayAccount",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "Ukey",
    "TxSeq",
    "TranKey",
    "Civil323Amt",
    "TotalAmt",
    "SignDate",
    "FirstPayDate",
    "Period",
    "Rate",
    "MonthPayAmt",
    "PayAccount",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ447Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ447Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;