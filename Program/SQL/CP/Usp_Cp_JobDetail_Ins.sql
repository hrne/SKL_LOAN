CREATE OR REPLACE PROCEDURE "Usp_Cp_JobDetail_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JobDetail" DROP STORAGE';

  INSERT INTO "JobDetail" (
    "TxSeq",
    "ExecDate",
    "JobCode",
    "StepId",
    "BatchType",
    "Status",
    "ErrCode",
    "ErrContent",
    "StepStartTime",
    "StepEndTime",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
)
  SELECT
    "TxSeq",
    "ExecDate",
    "JobCode",
    "StepId",
    "BatchType",
    "Status",
    "ErrCode",
    "ErrContent",
    "StepStartTime",
    "StepEndTime",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
  FROM ITXADMIN."JobDetail";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JobDetail_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;