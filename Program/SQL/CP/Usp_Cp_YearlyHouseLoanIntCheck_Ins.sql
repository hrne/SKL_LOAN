CREATE OR REPLACE PROCEDURE "Usp_Cp_YearlyHouseLoanIntCheck_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "YearlyHouseLoanIntCheck" DROP STORAGE';

  INSERT INTO "YearlyHouseLoanIntCheck" (
    "YearMonth",
    "CustNo",
    "FacmNo",
    "UsageCode",
    "R1",
    "R2",
    "R3",
    "R4",
    "R5",
    "R6",
    "R7",
    "R8",
    "R10",
    "R11",
    "R12",
    "C1",
    "C2",
    "C3",
    "C4",
    "C5",
    "JsonFields",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "YearMonth",
    "CustNo",
    "FacmNo",
    "UsageCode",
    "R1",
    "R2",
    "R3",
    "R4",
    "R5",
    "R6",
    "R7",
    "R8",
    "R10",
    "R11",
    "R12",
    "C1",
    "C2",
    "C3",
    "C4",
    "C5",
    "JsonFields",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."YearlyHouseLoanIntCheck";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_YearlyHouseLoanIntCheck_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;