CREATE OR REPLACE PROCEDURE "Usp_Cp_JobMain_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JobMain" DROP STORAGE';

  INSERT INTO "JobMain" (
    "TxSeq",
    "ExecDate",
    "JobCode",
    "StartTime",
    "EndTime",
    "Status",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
)
  SELECT
    "TxSeq",
    "ExecDate",
    "JobCode",
    "StartTime",
    "EndTime",
    "Status",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
  FROM ITXADMIN."JobMain";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JobMain_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;