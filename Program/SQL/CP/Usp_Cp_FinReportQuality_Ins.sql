CREATE OR REPLACE PROCEDURE "Usp_Cp_FinReportQuality_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FinReportQuality" DROP STORAGE';

  INSERT INTO "FinReportQuality" (
    "CustUKey",
    "Ukey",
    "ReportType",
    "Opinion",
    "IsCheck",
    "IsChange",
    "OfficeType",
    "PunishRecord",
    "ChangeReason",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustUKey",
    "Ukey",
    "ReportType",
    "Opinion",
    "IsCheck",
    "IsChange",
    "OfficeType",
    "PunishRecord",
    "ChangeReason",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FinReportQuality";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FinReportQuality_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;