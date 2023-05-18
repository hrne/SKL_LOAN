CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ443_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ443" DROP STORAGE';

  INSERT INTO "JcicZ443" (
    "TranKey",
    "CustId",
    "SubmitKey",
    "ApplyDate",
    "CourtCode",
    "MaxMainCode",
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
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ443";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ443_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;