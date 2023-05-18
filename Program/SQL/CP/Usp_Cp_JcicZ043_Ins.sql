CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ043_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ043" DROP STORAGE';

  INSERT INTO "JcicZ043" (
    "TranKey",
    "SubmitKey",
    "CustId",
    "RcDate",
    "MaxMainCode",
    "Account",
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
    "Account",
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
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ043";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ043_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;