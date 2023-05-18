CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ042_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ042" DROP STORAGE';

  INSERT INTO "JcicZ042" (
    "TranKey",
    "SubmitKey",
    "CustId",
    "RcDate",
    "MaxMainCode",
    "IsClaims",
    "GuarLoanCnt",
    "ExpLoanAmt",
    "Civil323ExpAmt",
    "ReceExpAmt",
    "CashCardAmt",
    "Civil323CashAmt",
    "ReceCashAmt",
    "CreditCardAmt",
    "Civil323CreditAmt",
    "ReceCreditAmt",
    "ReceExpPrin",
    "ReceExpInte",
    "ReceExpPena",
    "ReceExpOther",
    "CashCardPrin",
    "CashCardInte",
    "CashCardPena",
    "CashCardOther",
    "CreditCardPrin",
    "CreditCardInte",
    "CreditCardPena",
    "CreditCardOther",
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
    "MaxMainCode",
    "IsClaims",
    "GuarLoanCnt",
    "ExpLoanAmt",
    "Civil323ExpAmt",
    "ReceExpAmt",
    "CashCardAmt",
    "Civil323CashAmt",
    "ReceCashAmt",
    "CreditCardAmt",
    "Civil323CreditAmt",
    "ReceCreditAmt",
    "ReceExpPrin",
    "ReceExpInte",
    "ReceExpPena",
    "ReceExpOther",
    "CashCardPrin",
    "CashCardInte",
    "CashCardPena",
    "CashCardOther",
    "CreditCardPrin",
    "CreditCardInte",
    "CreditCardPena",
    "CreditCardOther",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ042";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ042_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;