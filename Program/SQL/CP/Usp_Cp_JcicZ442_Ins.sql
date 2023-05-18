CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ442_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ442" DROP STORAGE';

  INSERT INTO "JcicZ442" (
    "TranKey",
    "CustId",
    "SubmitKey",
    "ApplyDate",
    "CourtCode",
    "MaxMainCode",
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
    "ApplyDate",
    "CourtCode",
    "MaxMainCode",
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
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ442";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ442_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;