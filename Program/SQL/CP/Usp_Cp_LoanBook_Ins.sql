CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanBook_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanBook" DROP STORAGE';

  INSERT INTO "LoanBook" (
    "CustNo",
    "FacmNo",
    "BormNo",
    "BookDate",
    "ActualDate",
    "Status",
    "CurrencyCode",
    "IncludeIntFlag",
    "UnpaidIntFlag",
    "IncludeFeeFlag",
    "BookAmt",
    "RepayAmt",
    "PayMethod",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "FacmNo",
    "BormNo",
    "BookDate",
    "ActualDate",
    "Status",
    "CurrencyCode",
    "IncludeIntFlag",
    "UnpaidIntFlag",
    "IncludeFeeFlag",
    "BookAmt",
    "RepayAmt",
    "PayMethod",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanBook";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanBook_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;