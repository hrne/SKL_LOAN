CREATE OR REPLACE PROCEDURE "Usp_Cp_UspErrorLog_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "UspErrorLog" DROP STORAGE';

  INSERT INTO "UspErrorLog" (
    "LogUkey",
    "LogDate",
    "LogTime",
    "UspName",
    "ErrorCode",
    "ErrorMessage",
    "ErrorBackTrace",
    "ExecEmpNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "LogUkey",
    "LogDate",
    "LogTime",
    "UspName",
    "ErrorCode",
    "ErrorMessage",
    "ErrorBackTrace",
    "ExecEmpNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."UspErrorLog";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_UspErrorLog_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;