CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ042Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ042Log" DROP STORAGE';

  INSERT INTO "JcicZ042Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
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
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "Ukey",
    "TxSeq",
    "TranKey",
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
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ042Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ042Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;