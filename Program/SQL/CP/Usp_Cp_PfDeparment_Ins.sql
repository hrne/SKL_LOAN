CREATE OR REPLACE PROCEDURE "Usp_Cp_PfDeparment_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfDeparment" DROP STORAGE';

  INSERT INTO "PfDeparment" (
    "UnitCode",
    "DistCode",
    "DeptCode",
    "EmpNo",
    "UnitItem",
    "DistItem",
    "DeptItem",
    "DirectorCode",
    "EmpName",
    "DepartOfficer",
    "GoalCnt",
    "SumGoalCnt",
    "GoalAmt",
    "SumGoalAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "UnitCode",
    "DistCode",
    "DeptCode",
    "EmpNo",
    "UnitItem",
    "DistItem",
    "DeptItem",
    "DirectorCode",
    "EmpName",
    "DepartOfficer",
    "GoalCnt",
    "SumGoalCnt",
    "GoalAmt",
    "SumGoalAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PfDeparment";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PfDeparment_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;