CREATE OR REPLACE PROCEDURE "Usp_Cp_ClOtherRightsFac_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClOtherRightsFac" DROP STORAGE';

  INSERT INTO "ClOtherRightsFac" (
    "ClCode1",
    "ClCode2",
    "ClNo",
    "Seq",
    "ApproveNo",
    "CustNo",
    "FacmNo",
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
    "ApproveNo",
    "CustNo",
    "FacmNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClOtherRightsFac";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClOtherRightsFac_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;