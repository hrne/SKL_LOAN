CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ052Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ052Log" DROP STORAGE';

  INSERT INTO "JcicZ052Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "BankCode1",
    "DataCode1",
    "BankCode2",
    "DataCode2",
    "BankCode3",
    "DataCode3",
    "BankCode4",
    "DataCode4",
    "BankCode5",
    "DataCode5",
    "ChangePayDate",
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
    "BankCode1",
    "DataCode1",
    "BankCode2",
    "DataCode2",
    "BankCode3",
    "DataCode3",
    "BankCode4",
    "DataCode4",
    "BankCode5",
    "DataCode5",
    "ChangePayDate",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ052Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ052Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;