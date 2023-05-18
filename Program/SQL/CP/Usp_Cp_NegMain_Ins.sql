CREATE OR REPLACE PROCEDURE "Usp_Cp_NegMain_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "NegMain" DROP STORAGE';

  INSERT INTO "NegMain" (
    "CustNo",
    "CaseSeq",
    "CaseKindCode",
    "Status",
    "CustLoanKind",
    "PayerCustNo",
    "DeferYMStart",
    "DeferYMEnd",
    "ApplDate",
    "DueAmt",
    "TotalPeriod",
    "IntRate",
    "FirstDueDate",
    "LastDueDate",
    "IsMainFin",
    "TotalContrAmt",
    "MainFinCode",
    "PrincipalBal",
    "AccuTempAmt",
    "AccuOverAmt",
    "AccuDueAmt",
    "AccuSklShareAmt",
    "RepaidPeriod",
    "TwoStepCode",
    "ChgCondDate",
    "NextPayDate",
    "PayIntDate",
    "RepayPrincipal",
    "RepayInterest",
    "StatusDate",
    "CourtCode",
    "ThisAcDate",
    "ThisTitaTlrNo",
    "ThisTitaTxtNo",
    "LastAcDate",
    "LastTitaTlrNo",
    "LastTitaTxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "CaseSeq",
    "CaseKindCode",
    "Status",
    "CustLoanKind",
    "PayerCustNo",
    "DeferYMStart",
    "DeferYMEnd",
    "ApplDate",
    "DueAmt",
    "TotalPeriod",
    "IntRate",
    "FirstDueDate",
    "LastDueDate",
    "IsMainFin",
    "TotalContrAmt",
    "MainFinCode",
    "PrincipalBal",
    "AccuTempAmt",
    "AccuOverAmt",
    "AccuDueAmt",
    "AccuSklShareAmt",
    "RepaidPeriod",
    "TwoStepCode",
    "ChgCondDate",
    "NextPayDate",
    "PayIntDate",
    "RepayPrincipal",
    "RepayInterest",
    "StatusDate",
    "CourtCode",
    "ThisAcDate",
    "ThisTitaTlrNo",
    "ThisTitaTxtNo",
    "LastAcDate",
    "LastTitaTlrNo",
    "LastTitaTxtNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."NegMain";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_NegMain_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;