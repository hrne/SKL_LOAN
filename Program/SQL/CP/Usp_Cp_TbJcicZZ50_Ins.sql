CREATE OR REPLACE PROCEDURE "Usp_Cp_TbJcicZZ50_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "TbJcicZZ50" DROP STORAGE';

  INSERT INTO "TbJcicZZ50" (
    "QryStartDate",
    "QryEndDate",
    "OutJcictxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "QryStartDate",
    "QryEndDate",
    "OutJcictxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."TbJcicZZ50";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_TbJcicZZ50_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;