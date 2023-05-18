CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicB092_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicB092" DROP STORAGE';

  INSERT INTO "JcicB092" (
    "DataYM",
    "DataType",
    "BankItem",
    "BranchItem",
    "Filler4",
    "ClActNo",
    "ClTypeJCIC",
    "OwnerId",
    "EvaAmt",
    "EvaDate",
    "LoanLimitAmt",
    "SettingDate",
    "MonthSettingAmt",
    "SettingSeq",
    "SettingAmt",
    "PreSettingAmt",
    "DispPrice",
    "IssueEndDate",
    "CityJCICCode",
    "AreaJCICCode",
    "IrCode",
    "LandNo1",
    "LandNo2",
    "BdNo1",
    "BdNo2",
    "Zip",
    "InsuFg",
    "LVITax",
    "LVITaxYearMonth",
    "ContractPrice",
    "ContractDate",
    "ParkingTypeCode",
    "Area",
    "LandOwnedArea",
    "BdTypeCode",
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
    "ClTypeJCIC",
    "OwnerId",
    "EvaAmt",
    "EvaDate",
    "LoanLimitAmt",
    "SettingDate",
    "MonthSettingAmt",
    "SettingSeq",
    "SettingAmt",
    "PreSettingAmt",
    "DispPrice",
    "IssueEndDate",
    "CityJCICCode",
    "AreaJCICCode",
    "IrCode",
    "LandNo1",
    "LandNo2",
    "BdNo1",
    "BdNo2",
    "Zip",
    "InsuFg",
    "LVITax",
    "LVITaxYearMonth",
    "ContractPrice",
    "ContractDate",
    "ParkingTypeCode",
    "Area",
    "LandOwnedArea",
    "BdTypeCode",
    "Filler33",
    "JcicDataYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicB092";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicB092_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;