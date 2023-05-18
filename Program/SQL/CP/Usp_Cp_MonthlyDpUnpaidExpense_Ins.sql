CREATE OR REPLACE PROCEDURE "Usp_Cp_MonthlyDpUnpaidExpense_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "MonthlyDpUnpaidExpense" DROP STORAGE';

  INSERT INTO "MonthlyDpUnpaidExpense" (
    "YearMonth",
    "CustNo",
    "FacmNo",
    "BormNo",
    "DataFlag",
    "CustTotal",
    "FacTotal",
    "ForeclosureShare",
    "InsuShare",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "YearMonth",
    "CustNo",
    "FacmNo",
    "BormNo",
    "DataFlag",
    "CustTotal",
    "FacTotal",
    "ForeclosureShare",
    "InsuShare",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."MonthlyDpUnpaidExpense";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_MonthlyDpUnpaidExpense_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;