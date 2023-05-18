CREATE OR REPLACE PROCEDURE "Usp_Cp_ClParking_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClParking" DROP STORAGE';

  INSERT INTO "ClParking" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "ParkingSeqNo",
    "ParkingNo",
    "ParkingQty",
    "ParkingTypeCode",
    "OwnerPart",
    "OwnerTotal",
    "CityCode",
    "AreaCode",
    "IrCode",
    "BdNo1",
    "BdNo2",
    "LandNo1",
    "LandNo2",
    "ParkingArea",
    "Amount",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "ParkingSeqNo",
    "ParkingNo",
    "ParkingQty",
    "ParkingTypeCode",
    "OwnerPart",
    "OwnerTotal",
    "CityCode",
    "AreaCode",
    "IrCode",
    "BdNo1",
    "BdNo2",
    "LandNo1",
    "LandNo2",
    "ParkingArea",
    "Amount",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClParking";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClParking_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;