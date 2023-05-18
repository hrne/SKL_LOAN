CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ061_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ061" DROP STORAGE';

  INSERT INTO "JcicZ061" (
    "TranKey",
    "SubmitKey",
    "CustId",
    "RcDate",
    "ChangePayDate",
    "MaxMainCode",
    "ExpBalanceAmt",
    "CashBalanceAmt",
    "CreditBalanceAmt",
    "MaxMainNote",
    "IsGuarantor",
    "IsChangePayment",
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
    "ChangePayDate",
    "MaxMainCode",
    "ExpBalanceAmt",
    "CashBalanceAmt",
    "CreditBalanceAmt",
    "MaxMainNote",
    "IsGuarantor",
    "IsChangePayment",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ061";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ061_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;