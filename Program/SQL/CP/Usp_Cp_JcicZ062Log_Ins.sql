CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ062Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ062Log" DROP STORAGE';

  INSERT INTO "JcicZ062Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
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
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "Ukey",
    "TxSeq",
    "TranKey",
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
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ062Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ062Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;