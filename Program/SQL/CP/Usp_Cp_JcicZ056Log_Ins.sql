CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ056Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ056Log" DROP STORAGE';

  INSERT INTO "JcicZ056Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "Year",
    "CourtDiv",
    "CourtCaseNo",
    "Approve",
    "OutstandAmt",
    "SubAmt",
    "ClaimStatus1",
    "SaveDate",
    "ClaimStatus2",
    "SaveEndDate",
    "AdminName",
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
    "Year",
    "CourtDiv",
    "CourtCaseNo",
    "Approve",
    "OutstandAmt",
    "SubAmt",
    "ClaimStatus1",
    "SaveDate",
    "ClaimStatus2",
    "SaveEndDate",
    "AdminName",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ056Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ056Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;