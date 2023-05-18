CREATE OR REPLACE PROCEDURE "Usp_Cp_CdLandOffice_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdLandOffice" DROP STORAGE';

  INSERT INTO "CdLandOffice" (
    "CityCode",
    "LandOfficeCode",
    "RecWord",
    "RecWordItem",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CityCode",
    "LandOfficeCode",
    "RecWord",
    "RecWordItem",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdLandOffice";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdLandOffice_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;