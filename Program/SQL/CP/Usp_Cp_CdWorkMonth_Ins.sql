CREATE OR REPLACE PROCEDURE "Usp_Cp_CdWorkMonth_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdWorkMonth" DROP STORAGE';

  INSERT INTO "CdWorkMonth" (
    "Year",
    "Month",
    "StartDate",
    "EndDate",
    "BonusDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "Year",
    "Month",
    "StartDate",
    "EndDate",
    "BonusDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdWorkMonth";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdWorkMonth_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;