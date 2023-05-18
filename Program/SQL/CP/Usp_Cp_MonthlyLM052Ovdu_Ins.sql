CREATE OR REPLACE PROCEDURE "Usp_Cp_MonthlyLM052Ovdu_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "MonthlyLM052Ovdu" DROP STORAGE';

  INSERT INTO "MonthlyLM052Ovdu" (
    "YearMonth",
    "OvduNo",
    "AcctCode",
    "LoanBal",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "YearMonth",
    "OvduNo",
    "AcctCode",
    "LoanBal",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."MonthlyLM052Ovdu";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_MonthlyLM052Ovdu_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;