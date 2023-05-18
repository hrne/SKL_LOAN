CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ048Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ048Log" DROP STORAGE';

  INSERT INTO "JcicZ048Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "CustRegAddr",
    "CustComAddr",
    "CustRegTelNo",
    "CustComTelNo",
    "CustMobilNo",
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
    "CustRegAddr",
    "CustComAddr",
    "CustRegTelNo",
    "CustComTelNo",
    "CustMobilNo",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ048Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ048Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;