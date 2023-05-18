CREATE OR REPLACE PROCEDURE "Usp_Cp_PfBsOfficer_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfBsOfficer" DROP STORAGE';

  INSERT INTO "PfBsOfficer" (
    "WorkMonth",
    "EmpNo",
    "Fullname",
    "AreaCode",
    "AreaItem",
    "DeptCode",
    "DepItem",
    "DistCode",
    "DistItem",
    "StationName",
    "GoalAmt",
    "SmryGoalAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "WorkMonth",
    "EmpNo",
    "Fullname",
    "AreaCode",
    "AreaItem",
    "DeptCode",
    "DepItem",
    "DistCode",
    "DistItem",
    "StationName",
    "GoalAmt",
    "SmryGoalAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PfBsOfficer";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PfBsOfficer_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;