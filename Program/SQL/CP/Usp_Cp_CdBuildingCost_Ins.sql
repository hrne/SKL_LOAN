CREATE OR REPLACE PROCEDURE "Usp_Cp_CdBuildingCost_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdBuildingCost" DROP STORAGE';

  INSERT INTO "CdBuildingCost" (
    "CityCode",
    "Material",
    "FloorLowerLimit",
    "Cost",
    "VersionDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CityCode",
    "Material",
    "FloorLowerLimit",
    "Cost",
    "VersionDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdBuildingCost";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdBuildingCost_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;