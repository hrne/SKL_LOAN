CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ049_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ049" DROP STORAGE';

  INSERT INTO "JcicZ049" (
    "TranKey",
    "SubmitKey",
    "CustId",
    "RcDate",
    "ClaimStatus",
    "ApplyDate",
    "CourtCode",
    "Year",
    "CourtDiv",
    "CourtCaseNo",
    "Approve",
    "ClaimDate",
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
    "SubmitKey",
    "CustId",
    "RcDate",
    "ClaimStatus",
    "ApplyDate",
    "CourtCode",
    "Year",
    "CourtDiv",
    "CourtCaseNo",
    "Approve",
    "ClaimDate",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ049";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ049_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;