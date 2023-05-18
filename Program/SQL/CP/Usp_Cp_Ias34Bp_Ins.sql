CREATE OR REPLACE PROCEDURE "Usp_Cp_Ias34Bp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias34Bp" DROP STORAGE';

  INSERT INTO "Ias34Bp" (
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "BormNo",
    "LoanRate",
    "RateCode",
    "EffectDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "BormNo",
    "LoanRate",
    "RateCode",
    "EffectDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."Ias34Bp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Ias34Bp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;