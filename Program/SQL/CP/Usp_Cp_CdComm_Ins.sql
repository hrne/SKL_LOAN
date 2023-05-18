CREATE OR REPLACE PROCEDURE "Usp_Cp_CdComm_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdComm" DROP STORAGE';

  INSERT INTO "CdComm" (
    "CdType",
    "CdItem",
    "EffectDate",
    "Enable",
    "Remark",
    "JsonFields",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CdType",
    "CdItem",
    "EffectDate",
    "Enable",
    "Remark",
    "JsonFields",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdComm";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdComm_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;