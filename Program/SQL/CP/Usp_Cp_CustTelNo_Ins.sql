CREATE OR REPLACE PROCEDURE "Usp_Cp_CustTelNo_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustTelNo" DROP STORAGE';

  INSERT INTO "CustTelNo" (
    "TelNoUKey",
    "CustUKey",
    "TelTypeCode",
    "TelArea",
    "TelNo",
    "TelExt",
    "TelChgRsnCode",
    "RelationCode",
    "LiaisonName",
    "Rmk",
    "StopReason",
    "Enable",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "TelNoUKey",
    "CustUKey",
    "TelTypeCode",
    "TelArea",
    "TelNo",
    "TelExt",
    "TelChgRsnCode",
    "RelationCode",
    "LiaisonName",
    "Rmk",
    "StopReason",
    "Enable",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CustTelNo";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CustTelNo_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;