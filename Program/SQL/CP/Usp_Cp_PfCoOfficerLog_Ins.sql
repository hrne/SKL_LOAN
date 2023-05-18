CREATE OR REPLACE PROCEDURE "Usp_Cp_PfCoOfficerLog_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfCoOfficerLog" DROP STORAGE';

  INSERT INTO "PfCoOfficerLog" (
    "EmpNo",
    "EffectiveDate",
    "IneffectiveDate",
    "AreaCode",
    "DistCode",
    "DeptCode",
    "AreaItem",
    "DistItem",
    "DeptItem",
    "EmpClass",
    "ClassPass",
    "SerialNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "EmpNo",
    "EffectiveDate",
    "IneffectiveDate",
    "AreaCode",
    "DistCode",
    "DeptCode",
    "AreaItem",
    "DistItem",
    "DeptItem",
    "EmpClass",
    "ClassPass",
    "SerialNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PfCoOfficerLog";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PfCoOfficerLog_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;