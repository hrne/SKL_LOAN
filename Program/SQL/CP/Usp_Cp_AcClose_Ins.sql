CREATE OR REPLACE PROCEDURE "Usp_Cp_AcClose_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcClose" DROP STORAGE';

  INSERT INTO "AcClose" (
    "AcDate",
    "BranchNo",
    "SecNo",
    "ClsFg",
    "BatNo",
    "ClsNo",
    "SlipNo",
    "CoreSeqNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcDate",
    "BranchNo",
    "SecNo",
    "ClsFg",
    "BatNo",
    "ClsNo",
    "SlipNo",
    "CoreSeqNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."AcClose";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AcClose_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;