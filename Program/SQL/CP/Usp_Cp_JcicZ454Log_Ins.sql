CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ454Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ454Log" DROP STORAGE';

  INSERT INTO "JcicZ454Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "PayOffResult",
    "PayOffDate",
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
    "PayOffResult",
    "PayOffDate",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ454Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ454Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;