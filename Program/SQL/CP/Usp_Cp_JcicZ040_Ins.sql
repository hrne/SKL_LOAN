CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ040_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ040" DROP STORAGE';

  INSERT INTO "JcicZ040" (
    "TranKey",
    "SubmitKey",
    "CustId",
    "RcDate",
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
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark",
    "JcicReportDate"
)
  SELECT
    "TranKey",
    "SubmitKey",
    "CustId",
    "RcDate",
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
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark",
    "JcicReportDate"
  FROM ITXADMIN."JcicZ040";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ040_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;