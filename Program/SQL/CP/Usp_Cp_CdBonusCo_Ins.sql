CREATE OR REPLACE PROCEDURE "Usp_Cp_CdBonusCo_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdBonusCo" DROP STORAGE';

  INSERT INTO "CdBonusCo" (
    "WorkMonth",
    "ConditionCode",
    "Condition",
    "ConditionAmt",
    "Bonus",
    "ClassPassBonus",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "WorkMonth",
    "ConditionCode",
    "Condition",
    "ConditionAmt",
    "Bonus",
    "ClassPassBonus",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdBonusCo";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdBonusCo_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;