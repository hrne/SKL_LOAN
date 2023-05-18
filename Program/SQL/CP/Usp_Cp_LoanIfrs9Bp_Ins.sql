CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanIfrs9Bp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanIfrs9Bp" DROP STORAGE';

  INSERT INTO "LoanIfrs9Bp" (
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
  FROM ITXADMIN."LoanIfrs9Bp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanIfrs9Bp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;