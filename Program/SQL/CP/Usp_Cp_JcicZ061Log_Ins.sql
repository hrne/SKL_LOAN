CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ061Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ061Log" DROP STORAGE';

  INSERT INTO "JcicZ061Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "ExpBalanceAmt",
    "CashBalanceAmt",
    "CreditBalanceAmt",
    "MaxMainNote",
    "IsGuarantor",
    "IsChangePayment",
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
    "ExpBalanceAmt",
    "CashBalanceAmt",
    "CreditBalanceAmt",
    "MaxMainNote",
    "IsGuarantor",
    "IsChangePayment",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ061Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ061Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;