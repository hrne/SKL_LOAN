CREATE OR REPLACE PROCEDURE "Usp_Cp_Ias34Cp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias34Cp" DROP STORAGE';

  INSERT INTO "Ias34Cp" (
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
  FROM ITXADMIN."Ias34Cp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_Ias34Cp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;