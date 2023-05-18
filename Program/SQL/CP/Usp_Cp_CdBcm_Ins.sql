CREATE OR REPLACE PROCEDURE "Usp_Cp_CdBcm_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdBcm" DROP STORAGE';

  INSERT INTO "CdBcm" (
    "UnitCode",
    "UnitItem",
    "DeptCode",
    "DeptItem",
    "DistCode",
    "DistItem",
    "UnitManager",
    "DeptManager",
    "DistManager",
    "ShortDeptItem",
    "ShortDistItem",
    "Enable",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "UnitCode",
    "UnitItem",
    "DeptCode",
    "DeptItem",
    "DistCode",
    "DistItem",
    "UnitManager",
    "DeptManager",
    "DistManager",
    "ShortDeptItem",
    "ShortDistItem",
    "Enable",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdBcm";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdBcm_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;