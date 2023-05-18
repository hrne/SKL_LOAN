CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ440_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ440" DROP STORAGE';

  INSERT INTO "JcicZ440" (
    "TranKey",
    "CustId",
    "SubmitKey",
    "ApplyDate",
    "CourtCode",
    "AgreeDate",
    "StartDate",
    "RemindDate",
    "ApplyType",
    "ReportYn",
    "NotBankId1",
    "NotBankId2",
    "NotBankId3",
    "NotBankId4",
    "NotBankId5",
    "NotBankId6",
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
    "ApplyDate",
    "CourtCode",
    "AgreeDate",
    "StartDate",
    "RemindDate",
    "ApplyType",
    "ReportYn",
    "NotBankId1",
    "NotBankId2",
    "NotBankId3",
    "NotBankId4",
    "NotBankId5",
    "NotBankId6",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ440";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ440_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;