CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ040Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ040Log" DROP STORAGE';

  INSERT INTO "JcicZ040Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "RbDate",
    "ApplyType",
    "RefBankId",
    "NotBankId1",
    "NotBankId2",
    "NotBankId3",
    "NotBankId4",
    "NotBankId5",
    "NotBankId6",
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
    "RbDate",
    "ApplyType",
    "RefBankId",
    "NotBankId1",
    "NotBankId2",
    "NotBankId3",
    "NotBankId4",
    "NotBankId5",
    "NotBankId6",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ040Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ040Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;