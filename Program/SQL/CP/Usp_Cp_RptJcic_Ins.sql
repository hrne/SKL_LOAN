CREATE OR REPLACE PROCEDURE "Usp_Cp_RptJcic_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "RptJcic" DROP STORAGE';

  INSERT INTO "RptJcic" (
    "BranchNo",
    "CustNo",
    "FacmNo",
    "JcicName",
    "JcicStatus",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "BranchNo",
    "CustNo",
    "FacmNo",
    "JcicName",
    "JcicStatus",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."RptJcic";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_RptJcic_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;