CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ055Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ055Log" DROP STORAGE';

  INSERT INTO "JcicZ055Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "Year",
    "CourtDiv",
    "CourtCaseNo",
    "PayDate",
    "PayEndDate",
    "Period",
    "Rate",
    "OutstandAmt",
    "SubAmt",
    "ClaimStatus1",
    "SaveDate",
    "ClaimStatus2",
    "SaveEndDate",
    "IsImplement",
    "InspectName",
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
    "PayDate",
    "PayEndDate",
    "Period",
    "Rate",
    "OutstandAmt",
    "SubAmt",
    "ClaimStatus1",
    "SaveDate",
    "ClaimStatus2",
    "SaveEndDate",
    "IsImplement",
    "InspectName",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ055Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ055Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;