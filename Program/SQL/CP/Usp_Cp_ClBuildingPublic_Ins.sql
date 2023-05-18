CREATE OR REPLACE PROCEDURE "Usp_Cp_ClBuildingPublic_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClBuildingPublic" DROP STORAGE';

  INSERT INTO "ClBuildingPublic" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "PublicSeq",
    "PublicBdNo1",
    "PublicBdNo2",
    "Area",
    "OwnerId",
    "OwnerName",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "PublicSeq",
    "PublicBdNo1",
    "PublicBdNo2",
    "Area",
    "OwnerId",
    "OwnerName",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClBuildingPublic";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClBuildingPublic_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;