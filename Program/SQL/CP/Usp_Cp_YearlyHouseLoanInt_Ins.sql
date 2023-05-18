CREATE OR REPLACE PROCEDURE "Usp_Cp_YearlyHouseLoanInt_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "YearlyHouseLoanInt" DROP STORAGE';

  INSERT INTO "YearlyHouseLoanInt" (
    "YearMonth",
    "CustNo",
    "FacmNo",
    "UsageCode",
    "AcctCode",
    "RepayCode",
    "LoanAmt",
    "LoanBal",
    "FirstDrawdownDate",
    "MaturityDate",
    "YearlyInt",
    "HouseBuyDate",
    "JsonFields",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "YearMonth",
    "CustNo",
    "FacmNo",
    "UsageCode",
    "AcctCode",
    "RepayCode",
    "LoanAmt",
    "LoanBal",
    "FirstDrawdownDate",
    "MaturityDate",
    "YearlyInt",
    "HouseBuyDate",
    "JsonFields",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."YearlyHouseLoanInt";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_YearlyHouseLoanInt_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;