CREATE OR REPLACE PROCEDURE "Usp_Cp_CdArea_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdArea" DROP STORAGE';

  INSERT INTO "CdArea" (
    "CityCode",
    "AreaCode",
    "AreaItem",
    "CityShort",
    "AreaShort",
    "JcicCityCode",
    "JcicAreaCode",
    "CityType",
    "Zip3",
    "DepartCode",
    "CityGroup",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CityCode",
    "AreaCode",
    "AreaItem",
    "CityShort",
    "AreaShort",
    "JcicCityCode",
    "JcicAreaCode",
    "CityType",
    "Zip3",
    "DepartCode",
    "CityGroup",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdArea";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdArea_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;