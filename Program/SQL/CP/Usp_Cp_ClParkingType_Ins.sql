CREATE OR REPLACE PROCEDURE "Usp_Cp_ClParkingType_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClParkingType" DROP STORAGE';

  INSERT INTO "ClParkingType" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "ParkingTypeCode",
    "ParkingQty",
    "ParkingArea",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "ParkingTypeCode",
    "ParkingQty",
    "ParkingArea",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClParkingType";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClParkingType_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;