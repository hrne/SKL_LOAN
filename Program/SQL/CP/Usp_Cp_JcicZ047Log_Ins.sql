CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ047Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ047Log" DROP STORAGE';

  INSERT INTO "JcicZ047Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "Period",
    "Rate",
    "Civil323ExpAmt",
    "ExpLoanAmt",
    "Civil323CashAmt",
    "CashCardAmt",
    "Civil323CreditAmt",
    "CreditCardAmt",
    "Civil323Amt",
    "TotalAmt",
    "PassDate",
    "InterviewDate",
    "SignDate",
    "LimitDate",
    "FirstPayDate",
    "MonthPayAmt",
    "PayAccount",
    "PostAddr",
    "GradeType",
    "PayLastAmt",
    "Period2",
    "Rate2",
    "MonthPayAmt2",
    "PayLastAmt2",
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
    "Period",
    "Rate",
    "Civil323ExpAmt",
    "ExpLoanAmt",
    "Civil323CashAmt",
    "CashCardAmt",
    "Civil323CreditAmt",
    "CreditCardAmt",
    "Civil323Amt",
    "TotalAmt",
    "PassDate",
    "InterviewDate",
    "SignDate",
    "LimitDate",
    "FirstPayDate",
    "MonthPayAmt",
    "PayAccount",
    "PostAddr",
    "GradeType",
    "PayLastAmt",
    "Period2",
    "Rate2",
    "MonthPayAmt2",
    "PayLastAmt2",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ047Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ047Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;