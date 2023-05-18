CREATE OR REPLACE PROCEDURE "Usp_Cp_ClOther_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClOther" DROP STORAGE';

  INSERT INTO "ClOther" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "PledgeStartDate",
    "PledgeEndDate",
    "PledgeBankCode",
    "PledgeNO",
    "OwnerCustUKey",
    "IssuingId",
    "IssuingCounty",
    "DocNo",
    "LoanToValue",
    "SecuritiesType",
    "Listed",
    "OfferingDate",
    "ExpirationDate",
    "TargetIssuer",
    "SubTargetIssuer",
    "CreditDate",
    "Credit",
    "ExternalCredit",
    "Index",
    "TradingMethod",
    "Compensation",
    "Investment",
    "PublicValue",
    "SettingStat",
    "ClStat",
    "SettingDate",
    "SettingAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "PledgeStartDate",
    "PledgeEndDate",
    "PledgeBankCode",
    "PledgeNO",
    "OwnerCustUKey",
    "IssuingId",
    "IssuingCounty",
    "DocNo",
    "LoanToValue",
    "SecuritiesType",
    "Listed",
    "OfferingDate",
    "ExpirationDate",
    "TargetIssuer",
    "SubTargetIssuer",
    "CreditDate",
    "Credit",
    "ExternalCredit",
    "Index",
    "TradingMethod",
    "Compensation",
    "Investment",
    "PublicValue",
    "SettingStat",
    "ClStat",
    "SettingDate",
    "SettingAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClOther";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClOther_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;