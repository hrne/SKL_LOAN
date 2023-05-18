CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicB094_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicB094" DROP STORAGE';

  INSERT INTO "JcicB094" (
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
    "CompanyId",
    "CompanyCountry",
    "StockCode",
    "StockType",
    "Currency",
    "SettingBalance",
    "LoanBal",
    "InsiderJobTitle",
    "InsiderPosition",
    "LegalPersonId",
    "DispPrice",
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
    "CompanyId",
    "CompanyCountry",
    "StockCode",
    "StockType",
    "Currency",
    "SettingBalance",
    "LoanBal",
    "InsiderJobTitle",
    "InsiderPosition",
    "LegalPersonId",
    "DispPrice",
    "Filler19",
    "JcicDataYM",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicB094";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicB094_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;