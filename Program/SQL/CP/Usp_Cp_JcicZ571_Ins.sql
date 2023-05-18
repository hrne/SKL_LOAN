CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ571_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ571" DROP STORAGE';

  INSERT INTO "JcicZ571" (
    "TranKey",
    "CustId",
    "SubmitKey",
    "BankId",
    "ApplyDate",
    "OwnerYn",
    "PayYn",
    "OwnerAmt",
    "AllotAmt",
    "UnallotAmt",
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
    "BankId",
    "ApplyDate",
    "OwnerYn",
    "PayYn",
    "OwnerAmt",
    "AllotAmt",
    "UnallotAmt",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ571";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ571_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;