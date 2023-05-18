CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicAtomMain_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicAtomMain" DROP STORAGE';

  INSERT INTO "JcicAtomMain" (
    "FunctionCode",
    "DataType",
    "Remark",
    "SearchPoint",
    "FunctionKey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "FunctionCode",
    "DataType",
    "Remark",
    "SearchPoint",
    "FunctionKey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicAtomMain";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicAtomMain_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;