CREATE OR REPLACE PROCEDURE "Usp_Cp_MlaundryParas_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "MlaundryParas" DROP STORAGE';

  INSERT INTO "MlaundryParas" (
    "BusinessType",
    "Factor1TotLimit",
    "Factor2Count",
    "Factor2AmtStart",
    "Factor2AmtEnd",
    "Factor3TotLimit",
    "FactorDays",
    "FactorDays3",
    "FactorDays2",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "BusinessType",
    "Factor1TotLimit",
    "Factor2Count",
    "Factor2AmtStart",
    "Factor2AmtEnd",
    "Factor3TotLimit",
    "FactorDays",
    "FactorDays3",
    "FactorDays2",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."MlaundryParas";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_MlaundryParas_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;