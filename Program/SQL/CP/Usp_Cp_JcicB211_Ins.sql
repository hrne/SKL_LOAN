CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicB211_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicB211" DROP STORAGE';

  INSERT INTO "JcicB211" (
    "DataYMD",
    "BankItem",
    "BranchItem",
    "TranCode",
    "CustId",
    "SubTranCode",
    "AcDate",
    "AcctNo",
    "BorxNo",
    "TxAmt",
    "LoanBal",
    "RepayCode",
    "NegStatus",
    "AcctCode",
    "SubAcctCode",
    "BadDebtDate",
    "ConsumeFg",
    "FinCode",
    "UsageCode",
    "Filler18",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYMD",
    "BankItem",
    "BranchItem",
    "TranCode",
    "CustId",
    "SubTranCode",
    "AcDate",
    "AcctNo",
    "BorxNo",
    "TxAmt",
    "LoanBal",
    "RepayCode",
    "NegStatus",
    "AcctCode",
    "SubAcctCode",
    "BadDebtDate",
    "ConsumeFg",
    "FinCode",
    "UsageCode",
    "Filler18",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicB211";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicB211_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;