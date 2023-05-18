CREATE OR REPLACE PROCEDURE "Usp_Cp_ClImmRankDetail_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClImmRankDetail" DROP STORAGE';

  INSERT INTO "ClImmRankDetail" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "SettingSeq",
    "FirstCreditor",
    "FirstAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "SettingSeq",
    "FirstCreditor",
    "FirstAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClImmRankDetail";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClImmRankDetail_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;