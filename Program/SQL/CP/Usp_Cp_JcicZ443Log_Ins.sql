CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ443Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ443Log" DROP STORAGE';

  INSERT INTO "JcicZ443Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "IsMaxMain",
    "Account",
    "GuarantyType",
    "LoanAmt",
    "CreditAmt",
    "Principal",
    "Interest",
    "Penalty",
    "Other",
    "TerminalPayAmt",
    "LatestPayAmt",
    "FinalPayDay",
    "NotyetacQuit",
    "MothPayDay",
    "BeginDate",
    "EndDate",
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
    "Account",
    "GuarantyType",
    "LoanAmt",
    "CreditAmt",
    "Principal",
    "Interest",
    "Penalty",
    "Other",
    "TerminalPayAmt",
    "LatestPayAmt",
    "FinalPayDay",
    "NotyetacQuit",
    "MothPayDay",
    "BeginDate",
    "EndDate",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ443Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ443Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;