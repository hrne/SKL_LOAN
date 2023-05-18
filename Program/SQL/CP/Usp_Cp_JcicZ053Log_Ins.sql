CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ053Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ053Log" DROP STORAGE';

  INSERT INTO "JcicZ053Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "AgreeSend",
    "AgreeSendData1",
    "AgreeSendData2",
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
    "AgreeSend",
    "AgreeSendData1",
    "AgreeSendData2",
    "ChangePayDate",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ053Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ053Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;