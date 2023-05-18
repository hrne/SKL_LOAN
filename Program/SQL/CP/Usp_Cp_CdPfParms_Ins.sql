CREATE OR REPLACE PROCEDURE "Usp_Cp_CdPfParms_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdPfParms" DROP STORAGE';

  INSERT INTO "CdPfParms" (
    "ConditionCode1",
    "ConditionCode2",
    "Condition",
    "WorkMonthStart",
    "WorkMonthEnd",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ConditionCode1",
    "ConditionCode2",
    "Condition",
    "WorkMonthStart",
    "WorkMonthEnd",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdPfParms";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdPfParms_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;