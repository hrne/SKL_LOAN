CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicB095_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicB095" DROP STORAGE';

  INSERT INTO "JcicB095" (
    "DataYM",
    "DataType",
    "BankItem",
    "BranchItem",
    "Filler4",
    "ClActNo",
    "OwnerId",
    "CityJCICCode",
    "AreaJCICCode",
    "IrCode",
    "BdNo1",
    "BdNo2",
    "CityName",
    "AreaName",
    "Addr",
    "BdMainUseCode",
    "BdMtrlCode",
    "BdSubUsageCode",
    "TotalFloor",
    "FloorNo",
    "BdDate",
    "TotalArea",
    "FloorArea",
    "BdSubArea",
    "PublicArea",
    "Filler33",
    "JcicDataYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "DataType",
    "BankItem",
    "BranchItem",
    "Filler4",
    "ClActNo",
    "OwnerId",
    "CityJCICCode",
    "AreaJCICCode",
    "IrCode",
    "BdNo1",
    "BdNo2",
    "CityName",
    "AreaName",
    "Addr",
    "BdMainUseCode",
    "BdMtrlCode",
    "BdSubUsageCode",
    "TotalFloor",
    "FloorNo",
    "BdDate",
    "TotalArea",
    "FloorArea",
    "BdSubArea",
    "PublicArea",
    "Filler33",
    "JcicDataYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicB095";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicB095_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;