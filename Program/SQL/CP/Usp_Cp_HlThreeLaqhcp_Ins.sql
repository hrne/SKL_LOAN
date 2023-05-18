CREATE OR REPLACE PROCEDURE "Usp_Cp_HlThreeLaqhcp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "HlThreeLaqhcp" DROP STORAGE';

  INSERT INTO "HlThreeLaqhcp" (
    "CalDate",
    "EmpNo",
    "UnitCode",
    "DistCode",
    "DeptCode",
    "UnitName",
    "DistName",
    "DeptName",
    "EmpName",
    "DepartOfficer",
    "DirectorCode",
    "GoalNum",
    "GoalAmt",
    "ActNum",
    "ActAmt",
    "ActRate",
    "TGoalNum",
    "TGoalAmt",
    "TActNum",
    "TActAmt",
    "TActRate",
    "UpNo",
    "ProcessDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CalDate",
    "EmpNo",
    "UnitCode",
    "DistCode",
    "DeptCode",
    "UnitName",
    "DistName",
    "DeptName",
    "EmpName",
    "DepartOfficer",
    "DirectorCode",
    "GoalNum",
    "GoalAmt",
    "ActNum",
    "ActAmt",
    "ActRate",
    "TGoalNum",
    "TGoalAmt",
    "TActNum",
    "TActAmt",
    "TActRate",
    "UpNo",
    "ProcessDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."HlThreeLaqhcp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_HlThreeLaqhcp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;