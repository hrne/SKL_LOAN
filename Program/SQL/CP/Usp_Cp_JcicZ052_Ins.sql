CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ052_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ052" DROP STORAGE';

  INSERT INTO "JcicZ052" (
    "TranKey",
    "SubmitKey",
    "CustId",
    "RcDate",
    "BankCode1",
    "DataCode1",
    "BankCode2",
    "DataCode2",
    "BankCode3",
    "DataCode3",
    "BankCode4",
    "DataCode4",
    "BankCode5",
    "DataCode5",
    "ChangePayDate",
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
    "BankCode1",
    "DataCode1",
    "BankCode2",
    "DataCode2",
    "BankCode3",
    "DataCode3",
    "BankCode4",
    "DataCode4",
    "BankCode5",
    "DataCode5",
    "ChangePayDate",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ052";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ052_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;