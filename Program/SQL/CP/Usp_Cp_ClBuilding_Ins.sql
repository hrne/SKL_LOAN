CREATE OR REPLACE PROCEDURE "Usp_Cp_ClBuilding_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClBuilding" DROP STORAGE';

  INSERT INTO "ClBuilding" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "CityCode",
    "AreaCode",
    "IrCode",
    "Road",
    "Section",
    "Alley",
    "Lane",
    "Num",
    "NumDash",
    "Floor",
    "FloorDash",
    "BdNo1",
    "BdNo2",
    "BdLocation",
    "BdMainUseCode",
    "BdUsageCode",
    "BdMtrlCode",
    "BdTypeCode",
    "TotalFloor",
    "FloorNo",
    "FloorArea",
    "EvaUnitPrice",
    "RoofStructureCode",
    "BdDate",
    "BdSubUsageCode",
    "BdSubArea",
    "SellerId",
    "SellerName",
    "ContractPrice",
    "ContractDate",
    "ParkingTypeCode",
    "ParkingArea",
    "ParkingProperty",
    "HouseTaxNo",
    "HouseBuyDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "CityCode",
    "AreaCode",
    "IrCode",
    "Road",
    "Section",
    "Alley",
    "Lane",
    "Num",
    "NumDash",
    "Floor",
    "FloorDash",
    "BdNo1",
    "BdNo2",
    "BdLocation",
    "BdMainUseCode",
    "BdUsageCode",
    "BdMtrlCode",
    "BdTypeCode",
    "TotalFloor",
    "FloorNo",
    "FloorArea",
    "EvaUnitPrice",
    "RoofStructureCode",
    "BdDate",
    "BdSubUsageCode",
    "BdSubArea",
    "SellerId",
    "SellerName",
    "ContractPrice",
    "ContractDate",
    "ParkingTypeCode",
    "ParkingArea",
    "ParkingProperty",
    "HouseTaxNo",
    "HouseBuyDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClBuilding";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClBuilding_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;