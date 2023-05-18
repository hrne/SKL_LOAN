CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ062_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ062" DROP STORAGE';

  INSERT INTO "JcicZ062" (
    "TranKey",
    "CustId",
    "SubmitKey",
    "RcDate",
    "ChangePayDate",
    "CompletePeriod",
    "Period",
    "Rate",
    "ExpBalanceAmt",
    "CashBalanceAmt",
    "CreditBalanceAmt",
    "ChaRepayAmt",
    "ChaRepayAgreeDate",
    "ChaRepayViewDate",
    "ChaRepayEndDate",
    "ChaRepayFirstDate",
    "PayAccount",
    "PostAddr",
    "MonthPayAmt",
    "GradeType",
    "Period2",
    "Rate2",
    "MonthPayAmt2",
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
    "RcDate",
    "ChangePayDate",
    "CompletePeriod",
    "Period",
    "Rate",
    "ExpBalanceAmt",
    "CashBalanceAmt",
    "CreditBalanceAmt",
    "ChaRepayAmt",
    "ChaRepayAgreeDate",
    "ChaRepayViewDate",
    "ChaRepayEndDate",
    "ChaRepayFirstDate",
    "PayAccount",
    "PostAddr",
    "MonthPayAmt",
    "GradeType",
    "Period2",
    "Rate2",
    "MonthPayAmt2",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ062";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ062_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;