CREATE OR REPLACE PROCEDURE "Usp_Cp_CdVarValue_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdVarValue" DROP STORAGE';

  INSERT INTO "CdVarValue" (
    "YearMonth",
    "AvailableFunds",
    "LoanTotalLmt",
    "NoGurTotalLmt",
    "Totalequity",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "YearMonth",
    "AvailableFunds",
    "LoanTotalLmt",
    "NoGurTotalLmt",
    "Totalequity",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdVarValue";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdVarValue_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;