CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ574Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ574Log" DROP STORAGE';

  INSERT INTO "JcicZ574Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "CloseDate",
    "CloseMark",
    "PhoneNo",
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
    "CloseDate",
    "CloseMark",
    "PhoneNo",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ574Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ574Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;