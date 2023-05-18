CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ575Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ575Log" DROP STORAGE';

  INSERT INTO "JcicZ575Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "ModifyType",
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
    "ModifyType",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ575Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ575Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;