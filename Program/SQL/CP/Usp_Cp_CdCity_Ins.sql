CREATE OR REPLACE PROCEDURE "Usp_Cp_CdCity_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdCity" DROP STORAGE';

  INSERT INTO "CdCity" (
    "CityCode",
    "CityItem",
    "UnitCode",
    "AccCollPsn",
    "AccTelArea",
    "AccTelNo",
    "AccTelExt",
    "LegalPsn",
    "LegalArea",
    "LegalNo",
    "LegalExt",
    "IntRateIncr",
    "IntRateCeiling",
    "IntRateFloor",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CityCode",
    "CityItem",
    "UnitCode",
    "AccCollPsn",
    "AccTelArea",
    "AccTelNo",
    "AccTelExt",
    "LegalPsn",
    "LegalArea",
    "LegalNo",
    "LegalExt",
    "IntRateIncr",
    "IntRateCeiling",
    "IntRateFloor",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdCity";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdCity_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;