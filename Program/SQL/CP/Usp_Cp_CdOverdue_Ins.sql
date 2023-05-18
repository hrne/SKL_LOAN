CREATE OR REPLACE PROCEDURE "Usp_Cp_CdOverdue_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdOverdue" DROP STORAGE';

  INSERT INTO "CdOverdue" (
    "OverdueSign",
    "OverdueCode",
    "OverdueItem",
    "Enable",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "OverdueSign",
    "OverdueCode",
    "OverdueItem",
    "Enable",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdOverdue";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdOverdue_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;