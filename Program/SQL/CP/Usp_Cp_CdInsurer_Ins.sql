CREATE OR REPLACE PROCEDURE "Usp_Cp_CdInsurer_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdInsurer" DROP STORAGE';

  INSERT INTO "CdInsurer" (
    "InsurerType",
    "InsurerCode",
    "InsurerId",
    "InsurerItem",
    "InsurerShort",
    "TelArea",
    "TelNo",
    "TelExt",
    "Enable",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "InsurerType",
    "InsurerCode",
    "InsurerId",
    "InsurerItem",
    "InsurerShort",
    "TelArea",
    "TelNo",
    "TelExt",
    "Enable",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdInsurer";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdInsurer_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;