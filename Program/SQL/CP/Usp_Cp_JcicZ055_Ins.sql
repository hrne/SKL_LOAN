CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ055_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ055" DROP STORAGE';

  INSERT INTO "JcicZ055" (
    "TranKey",
    "CustId",
    "SubmitKey",
    "CaseStatus",
    "ClaimDate",
    "CourtCode",
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
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
)
  SELECT
    "TranKey",
    "CustId",
    "SubmitKey",
    "CaseStatus",
    "ClaimDate",
    "CourtCode",
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
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ055";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ055_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;