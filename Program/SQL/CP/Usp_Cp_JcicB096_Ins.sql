CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicB096_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicB096" DROP STORAGE';

  INSERT INTO "JcicB096" (
    "DataYM",
    "DataType",
    "BankItem",
    "BranchItem",
    "Filler4",
    "ClActNo",
    "LandSeq",
    "OwnerId",
    "CityJCICCode",
    "AreaJCICCode",
    "IrCode",
    "LandNo1",
    "LandNo2",
    "LandCode",
    "Area",
    "LandZoningCode",
    "LandUsageType",
    "PostedLandValue",
    "PostedLandValueYearMonth",
    "Filler18",
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
    "LandSeq",
    "OwnerId",
    "CityJCICCode",
    "AreaJCICCode",
    "IrCode",
    "LandNo1",
    "LandNo2",
    "LandCode",
    "Area",
    "LandZoningCode",
    "LandUsageType",
    "PostedLandValue",
    "PostedLandValueYearMonth",
    "Filler18",
    "JcicDataYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicB096";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicB096_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;