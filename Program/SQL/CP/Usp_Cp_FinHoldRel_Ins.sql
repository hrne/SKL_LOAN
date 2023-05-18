CREATE OR REPLACE PROCEDURE "Usp_Cp_FinHoldRel_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FinHoldRel" DROP STORAGE';

  INSERT INTO "FinHoldRel" (
    "CompanyName",
    "Id",
    "Name",
    "BusTitle",
    "LineAmt",
    "LoanBalance",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CompanyName",
    "Id",
    "Name",
    "BusTitle",
    "LineAmt",
    "LoanBalance",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FinHoldRel";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FinHoldRel_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;