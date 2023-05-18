CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ571Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ571Log" DROP STORAGE';

  INSERT INTO "JcicZ571Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "OwnerYn",
    "PayYn",
    "OwnerAmt",
    "AllotAmt",
    "UnallotAmt",
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
    "OwnerYn",
    "PayYn",
    "OwnerAmt",
    "AllotAmt",
    "UnallotAmt",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ571Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ571Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;