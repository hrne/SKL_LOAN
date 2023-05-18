CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicB093_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicB093" DROP STORAGE';

  INSERT INTO "JcicB093" (
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
    "InsuFg",
    "Filler19",
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
    "InsuFg",
    "Filler19",
    "JcicDataYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicB093";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicB093_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;