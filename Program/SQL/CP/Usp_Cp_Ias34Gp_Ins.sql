CREATE OR REPLACE PROCEDURE "Usp_Cp_Ias34Gp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias34Gp" DROP STORAGE';

  INSERT INTO "Ias34Gp" (
    "DataYM",
    "CustNo",
    "CustId",
    "AgreeNo",
    "AgreeFg",
    "FacmNo",
    "BormNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "CustNo",
    "CustId",
    "AgreeNo",
    "AgreeFg",
    "FacmNo",
    "BormNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."Ias34Gp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Ias34Gp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;