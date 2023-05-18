CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicReFile_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicReFile" DROP STORAGE';

  INSERT INTO "JcicReFile" (
    "SubmitKey",
    "JcicDate",
    "ReportTotal",
    "CorrectCount",
    "MistakeCount",
    "NoBackFileCount",
    "NoBackFileDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "SubmitKey",
    "JcicDate",
    "ReportTotal",
    "CorrectCount",
    "MistakeCount",
    "NoBackFileCount",
    "NoBackFileDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicReFile";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicReFile_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;