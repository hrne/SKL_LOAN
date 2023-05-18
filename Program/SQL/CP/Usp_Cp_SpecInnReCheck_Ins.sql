CREATE OR REPLACE PROCEDURE "Usp_Cp_SpecInnReCheck_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "SpecInnReCheck" DROP STORAGE';

  INSERT INTO "SpecInnReCheck" (
    "CustNo",
    "FacmNo",
    "Remark",
    "Cycle",
    "ReChkYearMonth",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "FacmNo",
    "Remark",
    "Cycle",
    "ReChkYearMonth",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."SpecInnReCheck";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_SpecInnReCheck_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;