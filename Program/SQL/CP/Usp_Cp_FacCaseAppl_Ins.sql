CREATE OR REPLACE PROCEDURE "Usp_Cp_FacCaseAppl_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FacCaseAppl" DROP STORAGE';

  INSERT INTO "FacCaseAppl" (
    "ApplNo",
    "CustUKey",
    "ApplDate",
    "CreditSysNo",
    "SyndNo",
    "CurrencyCode",
    "ApplAmt",
    "ProdNo",
    "Estimate",
    "DepartmentCode",
    "PieceCode",
    "CreditOfficer",
    "LoanOfficer",
    "Introducer",
    "Coorgnizer",
    "InterviewerA",
    "InterviewerB",
    "Supervisor",
    "ProcessCode",
    "ApproveDate",
    "GroupUKey",
    "BranchNo",
    "IsLimit",
    "IsRelated",
    "IsLnrelNear",
    "IsSuspected",
    "IsSuspectedCheck",
    "IsSuspectedCheckType",
    "IsDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ApplNo",
    "CustUKey",
    "ApplDate",
    "CreditSysNo",
    "SyndNo",
    "CurrencyCode",
    "ApplAmt",
    "ProdNo",
    "Estimate",
    "DepartmentCode",
    "PieceCode",
    "CreditOfficer",
    "LoanOfficer",
    "Introducer",
    "Coorgnizer",
    "InterviewerA",
    "InterviewerB",
    "Supervisor",
    "ProcessCode",
    "ApproveDate",
    "GroupUKey",
    "BranchNo",
    "IsLimit",
    "IsRelated",
    "IsLnrelNear",
    "IsSuspected",
    "IsSuspectedCheck",
    "IsSuspectedCheckType",
    "IsDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FacCaseAppl";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FacCaseAppl_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;