CREATE OR REPLACE PROCEDURE "Usp_Cp_CdCityRate_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdCityRate" DROP STORAGE';

  INSERT INTO "CdCityRate" (
    "EffectYYMM",
    "CityCode",
    "IntRateIncr",
    "IntRateCeiling",
    "IntRateFloor",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "EffectYYMM",
    "CityCode",
    "IntRateIncr",
    "IntRateCeiling",
    "IntRateFloor",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdCityRate";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdCityRate_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;