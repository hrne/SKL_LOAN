CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ442Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ442Log" DROP STORAGE';

  INSERT INTO "JcicZ442Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "IsMaxMain",
    "IsClaims",
    "GuarLoanCnt",
    "Civil323ExpAmt",
    "Civil323CashAmt",
    "Civil323CreditAmt",
    "Civil323GuarAmt",
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
    "GuarObliPrin",
    "GuarObliInte",
    "GuarObliPena",
    "GuarObliOther",
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
    "IsMaxMain",
    "IsClaims",
    "GuarLoanCnt",
    "Civil323ExpAmt",
    "Civil323CashAmt",
    "Civil323CreditAmt",
    "Civil323GuarAmt",
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
    "GuarObliPrin",
    "GuarObliInte",
    "GuarObliPena",
    "GuarObliOther",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ442Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ442Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;