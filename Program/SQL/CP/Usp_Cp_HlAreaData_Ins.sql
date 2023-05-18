CREATE OR REPLACE PROCEDURE "Usp_Cp_HlAreaData_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "HlAreaData" DROP STORAGE';

  INSERT INTO "HlAreaData" (
    "AreaUnitNo",
    "AreaName",
    "AreaChiefEmpNo",
    "AreaChiefName",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AreaUnitNo",
    "AreaName",
    "AreaChiefEmpNo",
    "AreaChiefName",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."HlAreaData";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_HlAreaData_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;