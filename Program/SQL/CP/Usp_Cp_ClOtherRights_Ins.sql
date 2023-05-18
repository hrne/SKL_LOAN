CREATE OR REPLACE PROCEDURE "Usp_Cp_ClOtherRights_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClOtherRights" DROP STORAGE';

  INSERT INTO "ClOtherRights" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "Seq",
    "City",
    "OtherCity",
    "LandAdm",
    "OtherLandAdm",
    "RecYear",
    "RecWord",
    "OtherRecWord",
    "RecNumber",
    "RightsNote",
    "SecuredTotal",
    "ReceiveFg",
    "ChoiceDate",
    "ReceiveCustNo",
    "CloseNo",
    "SecuredDate",
    "Location",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "Seq",
    "City",
    "OtherCity",
    "LandAdm",
    "OtherLandAdm",
    "RecYear",
    "RecWord",
    "OtherRecWord",
    "RecNumber",
    "RightsNote",
    "SecuredTotal",
    "ReceiveFg",
    "ChoiceDate",
    "ReceiveCustNo",
    "CloseNo",
    "SecuredDate",
    "Location",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClOtherRights";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClOtherRights_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;