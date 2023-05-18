CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanIfrs9Cp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanIfrs9Cp" DROP STORAGE';

  INSERT INTO "LoanIfrs9Cp" (
    "DataYM",
    "CustNo",
    "CustId",
    "FacmNo",
    "BormNo",
    "AmortizedCode",
    "PayIntFreq",
    "RepayFreq",
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
    "AmortizedCode",
    "PayIntFreq",
    "RepayFreq",
    "EffectDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanIfrs9Cp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanIfrs9Cp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;