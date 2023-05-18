CREATE OR REPLACE PROCEDURE "Usp_Cp_SlipEbsRecord_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "SlipEbsRecord" DROP STORAGE';

  INSERT INTO "SlipEbsRecord" (
    "UploadNo",
    "GroupId",
    "RequestData",
    "ResultData",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "UploadNo",
    "GroupId",
    "RequestData",
    "ResultData",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."SlipEbsRecord";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_SlipEbsRecord_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;