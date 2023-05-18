CREATE OR REPLACE PROCEDURE "Usp_Cp_ClMain_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClMain" DROP STORAGE';

  INSERT INTO "ClMain" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "CustUKey",
    "ClTypeCode",
    "CityCode",
    "AreaCode",
    "ClStatus",
    "EvaDate",
    "EvaAmt",
    "ShareTotal",
    "Synd",
    "SyndCode",
    "DispPrice",
    "DispDate",
    "NewNote",
    "LastClOtherSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "CustUKey",
    "ClTypeCode",
    "CityCode",
    "AreaCode",
    "ClStatus",
    "EvaDate",
    "EvaAmt",
    "ShareTotal",
    "Synd",
    "SyndCode",
    "DispPrice",
    "DispDate",
    "NewNote",
    "LastClOtherSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClMain";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClMain_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;