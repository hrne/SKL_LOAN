CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanIfrs9Fp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanIfrs9Fp" DROP STORAGE';

  INSERT INTO "LoanIfrs9Fp" (
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
  FROM ITXADMIN."LoanIfrs9Fp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanIfrs9Fp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;