CREATE OR REPLACE PROCEDURE "Usp_Cp_ClBatch_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClBatch" DROP STORAGE';

  INSERT INTO "ClBatch" (
    "GroupNo",
    "Seq",
    "ApplNo",
    "EvaCompany",
    "EvaDate",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "InsertStatus",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "GroupNo",
    "Seq",
    "ApplNo",
    "EvaCompany",
    "EvaDate",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "InsertStatus",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."ClBatch";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_ClBatch_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;