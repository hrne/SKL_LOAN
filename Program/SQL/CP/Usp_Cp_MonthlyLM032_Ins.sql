CREATE OR REPLACE PROCEDURE "Usp_Cp_MonthlyLM032_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "MonthlyLM032" DROP STORAGE';

  INSERT INTO "MonthlyLM032" (
    "ADTYMT",
    "GDRID1",
    "W08PPR",
    "LMSACN",
    "LMSAPN",
    "W08LBL",
    "W08DLY",
    "STATUS",
    "ADTYMT01",
    "GDRID101",
    "W08PPR01",
    "LMSACN01",
    "LMSAPN01",
    "W08LBL01",
    "W08DLY01",
    "ACTACT",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ADTYMT",
    "GDRID1",
    "W08PPR",
    "LMSACN",
    "LMSAPN",
    "W08LBL",
    "W08DLY",
    "STATUS",
    "ADTYMT01",
    "GDRID101",
    "W08PPR01",
    "LMSACN01",
    "LMSAPN01",
    "W08LBL01",
    "W08DLY01",
    "ACTACT",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."MonthlyLM032";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_MonthlyLM032_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;