CREATE OR REPLACE PROCEDURE "Usp_Cp_Ias39LoanCommit_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias39LoanCommit" DROP STORAGE';

  INSERT INTO "Ias39LoanCommit" (
    "DataYm",
    "CustNo",
    "FacmNo",
    "ApplNo",
    "ApproveDate",
    "FirstDrawdownDate",
    "MaturityDate",
    "LoanTermYy",
    "LoanTermMm",
    "LoanTermDd",
    "UtilDeadline",
    "RecycleDeadline",
    "LineAmt",
    "UtilBal",
    "AvblBal",
    "RecycleCode",
    "IrrevocableFlag",
    "AcBookCode",
    "AcSubBookCode",
    "Ccf",
    "ExpLimitAmt",
    "DbAcNoCode",
    "CrAcNoCode",
    "DrawdownFg",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYm",
    "CustNo",
    "FacmNo",
    "ApplNo",
    "ApproveDate",
    "FirstDrawdownDate",
    "MaturityDate",
    "LoanTermYy",
    "LoanTermMm",
    "LoanTermDd",
    "UtilDeadline",
    "RecycleDeadline",
    "LineAmt",
    "UtilBal",
    "AvblBal",
    "RecycleCode",
    "IrrevocableFlag",
    "AcBookCode",
    "AcSubBookCode",
    "Ccf",
    "ExpLimitAmt",
    "DbAcNoCode",
    "CrAcNoCode",
    "DrawdownFg",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."Ias39LoanCommit";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Ias39LoanCommit_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;