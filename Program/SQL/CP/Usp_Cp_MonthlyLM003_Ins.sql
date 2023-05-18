CREATE OR REPLACE PROCEDURE "Usp_Cp_MonthlyLM003_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "MonthlyLM003" DROP STORAGE';

  INSERT INTO "MonthlyLM003" (
    "EntType",
    "DataYear",
    "DataMonth",
    "DrawdownAmt",
    "CloseLoan",
    "CloseSale",
    "CloseSelfRepay",
    "ExtraRepay",
    "PrincipalAmortize",
    "Collection",
    "LoanBalance",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "EntType",
    "DataYear",
    "DataMonth",
    "DrawdownAmt",
    "CloseLoan",
    "CloseSale",
    "CloseSelfRepay",
    "ExtraRepay",
    "PrincipalAmortize",
    "Collection",
    "LoanBalance",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."MonthlyLM003";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_MonthlyLM003_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;