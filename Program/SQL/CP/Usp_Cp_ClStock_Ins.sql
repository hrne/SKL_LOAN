CREATE OR REPLACE PROCEDURE "Usp_Cp_ClStock_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClStock" DROP STORAGE';

  INSERT INTO "ClStock" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "StockCode",
    "ListingType",
    "StockType",
    "CompanyId",
    "DataYear",
    "IssuedShares",
    "NetWorth",
    "EvaStandard",
    "ParValue",
    "MonthlyAvg",
    "YdClosingPrice",
    "ThreeMonthAvg",
    "EvaUnitPrice",
    "OwnerCustUKey",
    "InsiderJobTitle",
    "InsiderPosition",
    "LegalPersonId",
    "LoanToValue",
    "ClMtr",
    "NoticeMtr",
    "ImplementMtr",
    "AcMtr",
    "PledgeNo",
    "ComputeMTR",
    "SettingStat",
    "ClStat",
    "SettingDate",
    "SettingBalance",
    "MtgDate",
    "CustodyNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "StockCode",
    "ListingType",
    "StockType",
    "CompanyId",
    "DataYear",
    "IssuedShares",
    "NetWorth",
    "EvaStandard",
    "ParValue",
    "MonthlyAvg",
    "YdClosingPrice",
    "ThreeMonthAvg",
    "EvaUnitPrice",
    "OwnerCustUKey",
    "InsiderJobTitle",
    "InsiderPosition",
    "LegalPersonId",
    "LoanToValue",
    "ClMtr",
    "NoticeMtr",
    "ImplementMtr",
    "AcMtr",
    "PledgeNo",
    "ComputeMTR",
    "SettingStat",
    "ClStat",
    "SettingDate",
    "SettingBalance",
    "MtgDate",
    "CustodyNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClStock";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClStock_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;