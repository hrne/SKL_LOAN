CREATE OR REPLACE PROCEDURE "Usp_Cp_CustMain_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustMain" DROP STORAGE';

  INSERT INTO "CustMain" (
    "CustUKey",
    "CustId",
    "CustNo",
    "BranchNo",
    "CustName",
    "Birthday",
    "Sex",
    "CustTypeCode",
    "IndustryCode",
    "NationalityCode",
    "BussNationalityCode",
    "SpouseId",
    "SpouseName",
    "RegZip3",
    "RegZip2",
    "RegCityCode",
    "RegAreaCode",
    "RegRoad",
    "RegSection",
    "RegAlley",
    "RegLane",
    "RegNum",
    "RegNumDash",
    "RegFloor",
    "RegFloorDash",
    "CurrZip3",
    "CurrZip2",
    "CurrCityCode",
    "CurrAreaCode",
    "CurrRoad",
    "CurrSection",
    "CurrAlley",
    "CurrLane",
    "CurrNum",
    "CurrNumDash",
    "CurrFloor",
    "CurrFloorDash",
    "CuscCd",
    "EntCode",
    "EmpNo",
    "EName",
    "EduCode",
    "OwnedHome",
    "CurrCompName",
    "CurrCompId",
    "CurrCompTel",
    "JobTitle",
    "JobTenure",
    "IncomeOfYearly",
    "IncomeDataDate",
    "PassportNo",
    "AMLJobCode",
    "AMLGroup",
    "IndigenousName",
    "LastFacmNo",
    "LastSyndNo",
    "AllowInquire",
    "Email",
    "ActFg",
    "Introducer",
    "BusinessOfficer",
    "IsSuspected",
    "IsSuspectedCheck",
    "IsSuspectedCheckType",
    "IsLimit",
    "IsRelated",
    "IsLnrelNear",
    "IsDate",
    "DataStatus",
    "TypeCode",
    "Station",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustUKey",
    "CustId",
    "CustNo",
    "BranchNo",
    "CustName",
    "Birthday",
    "Sex",
    "CustTypeCode",
    "IndustryCode",
    "NationalityCode",
    "BussNationalityCode",
    "SpouseId",
    "SpouseName",
    "RegZip3",
    "RegZip2",
    "RegCityCode",
    "RegAreaCode",
    "RegRoad",
    "RegSection",
    "RegAlley",
    "RegLane",
    "RegNum",
    "RegNumDash",
    "RegFloor",
    "RegFloorDash",
    "CurrZip3",
    "CurrZip2",
    "CurrCityCode",
    "CurrAreaCode",
    "CurrRoad",
    "CurrSection",
    "CurrAlley",
    "CurrLane",
    "CurrNum",
    "CurrNumDash",
    "CurrFloor",
    "CurrFloorDash",
    "CuscCd",
    "EntCode",
    "EmpNo",
    "EName",
    "EduCode",
    "OwnedHome",
    "CurrCompName",
    "CurrCompId",
    "CurrCompTel",
    "JobTitle",
    "JobTenure",
    "IncomeOfYearly",
    "IncomeDataDate",
    "PassportNo",
    "AMLJobCode",
    "AMLGroup",
    "IndigenousName",
    "LastFacmNo",
    "LastSyndNo",
    "AllowInquire",
    "Email",
    "ActFg",
    "Introducer",
    "BusinessOfficer",
    "IsSuspected",
    "IsSuspectedCheck",
    "IsSuspectedCheckType",
    "IsLimit",
    "IsRelated",
    "IsLnrelNear",
    "IsDate",
    "DataStatus",
    "TypeCode",
    "Station",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CustMain";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CustMain_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;