CREATE OR REPLACE PROCEDURE "Usp_Cp_ClMovables_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClMovables" DROP STORAGE';

  INSERT INTO "ClMovables" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "OwnerCustUKey",
    "ServiceLife",
    "ProductSpec",
    "ProductType",
    "ProductBrand",
    "ProductCC",
    "ProductColor",
    "EngineSN",
    "LicenseNo",
    "LicenseTypeCode",
    "LicenseUsageCode",
    "LiceneIssueDate",
    "MfgYearMonth",
    "VehicleTypeCode",
    "VehicleStyleCode",
    "VehicleOfficeCode",
    "Currency",
    "ExchangeRate",
    "Insurance",
    "LoanToValue",
    "ScrapValue",
    "MtgCode",
    "MtgCheck",
    "MtgLoan",
    "MtgPledge",
    "SettingStat",
    "ClStat",
    "SettingDate",
    "SettingAmt",
    "ReceiptNo",
    "MtgNo",
    "ReceivedDate",
    "MortgageIssueStartDate",
    "MortgageIssueEndDate",
    "Remark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "OwnerCustUKey",
    "ServiceLife",
    "ProductSpec",
    "ProductType",
    "ProductBrand",
    "ProductCC",
    "ProductColor",
    "EngineSN",
    "LicenseNo",
    "LicenseTypeCode",
    "LicenseUsageCode",
    "LiceneIssueDate",
    "MfgYearMonth",
    "VehicleTypeCode",
    "VehicleStyleCode",
    "VehicleOfficeCode",
    "Currency",
    "ExchangeRate",
    "Insurance",
    "LoanToValue",
    "ScrapValue",
    "MtgCode",
    "MtgCheck",
    "MtgLoan",
    "MtgPledge",
    "SettingStat",
    "ClStat",
    "SettingDate",
    "SettingAmt",
    "ReceiptNo",
    "MtgNo",
    "ReceivedDate",
    "MortgageIssueStartDate",
    "MortgageIssueEndDate",
    "Remark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClMovables";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClMovables_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;