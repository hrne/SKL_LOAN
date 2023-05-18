CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ043Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ043Log" DROP STORAGE';

  INSERT INTO "JcicZ043Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "CollateralType",
    "OriginLoanAmt",
    "CreditBalance",
    "PerPeriordAmt",
    "LastPayAmt",
    "LastPayDate",
    "OutstandAmt",
    "RepayPerMonDay",
    "ContractStartYM",
    "ContractEndYM",
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
    "CollateralType",
    "OriginLoanAmt",
    "CreditBalance",
    "PerPeriordAmt",
    "LastPayAmt",
    "LastPayDate",
    "OutstandAmt",
    "RepayPerMonDay",
    "ContractStartYM",
    "ContractEndYM",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ043Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ043Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;