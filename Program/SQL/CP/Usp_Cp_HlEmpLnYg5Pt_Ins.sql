CREATE OR REPLACE PROCEDURE "Usp_Cp_HlEmpLnYg5Pt_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "HlEmpLnYg5Pt" DROP STORAGE';

  INSERT INTO "HlEmpLnYg5Pt" (
    "WorkYM",
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
    "HlAppNum",
    "HlAppAmt",
    "ClAppNum",
    "ClAppAmt",
    "ServiceAppNum",
    "ServiceAppAmt",
    "CalDate",
    "UpNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "WorkYM",
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
    "HlAppNum",
    "HlAppAmt",
    "ClAppNum",
    "ClAppAmt",
    "ServiceAppNum",
    "ServiceAppAmt",
    "CalDate",
    "UpNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."HlEmpLnYg5Pt";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_HlEmpLnYg5Pt_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;