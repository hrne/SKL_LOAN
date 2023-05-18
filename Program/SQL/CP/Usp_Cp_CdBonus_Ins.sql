CREATE OR REPLACE PROCEDURE "Usp_Cp_CdBonus_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdBonus" DROP STORAGE';

  INSERT INTO "CdBonus" (
    "WorkMonth",
    "ConditionCode",
    "Condition",
    "AmtStartRange",
    "AmtEndRange",
    "Bonus",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "WorkMonth",
    "ConditionCode",
    "Condition",
    "AmtStartRange",
    "AmtEndRange",
    "Bonus",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdBonus";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdBonus_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;