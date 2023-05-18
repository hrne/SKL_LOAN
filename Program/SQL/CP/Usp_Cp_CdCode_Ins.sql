CREATE OR REPLACE PROCEDURE "Usp_Cp_CdCode_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdCode" DROP STORAGE';

  INSERT INTO "CdCode" (
    "DefCode",
    "DefType",
    "Code",
    "Item",
    "Enable",
    "EffectFlag",
    "MinCodeLength",
    "MaxCodeLength",
    "IsNumeric",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DefCode",
    "DefType",
    "Code",
    "Item",
    "Enable",
    "EffectFlag",
    "MinCodeLength",
    "MaxCodeLength",
    "IsNumeric",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdCode";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdCode_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;