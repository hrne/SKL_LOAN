CREATE OR REPLACE PROCEDURE "Usp_Cp_ClLandOwner_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClLandOwner" DROP STORAGE';

  INSERT INTO "ClLandOwner" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "LandSeq",
    "OwnerCustUKey",
    "OwnerRelCode",
    "OwnerPart",
    "OwnerTotal",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "LandSeq",
    "OwnerCustUKey",
    "OwnerRelCode",
    "OwnerPart",
    "OwnerTotal",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClLandOwner";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClLandOwner_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;