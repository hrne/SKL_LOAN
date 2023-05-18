CREATE OR REPLACE PROCEDURE "Usp_Cp_ClFac_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClFac" DROP STORAGE';

  INSERT INTO "ClFac" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "ApproveNo",
    "CustNo",
    "FacmNo",
    "MainFlag",
    "FacShareFlag",
    "ShareAmt",
    "OriSettingAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ClCode1",
    "ClCode2",
    "ClNo",
    "ApproveNo",
    "CustNo",
    "FacmNo",
    "MainFlag",
    "FacShareFlag",
    "ShareAmt",
    "OriSettingAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClFac";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClFac_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;