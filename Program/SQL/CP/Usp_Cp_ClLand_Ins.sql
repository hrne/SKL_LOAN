CREATE OR REPLACE PROCEDURE "Usp_Cp_ClLand_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClLand" DROP STORAGE';

  INSERT INTO "ClLand" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "LandSeq",
    "CityCode",
    "AreaCode",
    "IrCode",
    "LandNo1",
    "LandNo2",
    "LandLocation",
    "LandCode",
    "Area",
    "LandZoningCode",
    "LandUsageType",
    "LandUsageCode",
    "PostedLandValue",
    "PostedLandValueYearMonth",
    "TransferedYear",
    "LastTransferedAmt",
    "LVITax",
    "LVITaxYearMonth",
    "EvaUnitPrice",
    "LandRentStartDate",
    "LandRentEndDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "LandSeq",
    "CityCode",
    "AreaCode",
    "IrCode",
    "LandNo1",
    "LandNo2",
    "LandLocation",
    "LandCode",
    "Area",
    "LandZoningCode",
    "LandUsageType",
    "LandUsageCode",
    "PostedLandValue",
    "PostedLandValueYearMonth",
    "TransferedYear",
    "LastTransferedAmt",
    "LVITax",
    "LVITaxYearMonth",
    "EvaUnitPrice",
    "LandRentStartDate",
    "LandRentEndDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClLand";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClLand_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;