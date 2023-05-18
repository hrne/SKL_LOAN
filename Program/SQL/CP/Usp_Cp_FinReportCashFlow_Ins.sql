CREATE OR REPLACE PROCEDURE "Usp_Cp_FinReportCashFlow_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FinReportCashFlow" DROP STORAGE';

  INSERT INTO "FinReportCashFlow" (
    "CustUKey",
    "Ukey",
    "BusCash",
    "InvestCash",
    "FinCash",
    "AccountItem01",
    "AccountItem02",
    "AccountValue01",
    "AccountValue02",
    "EndCash",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustUKey",
    "Ukey",
    "BusCash",
    "InvestCash",
    "FinCash",
    "AccountItem01",
    "AccountItem02",
    "AccountValue01",
    "AccountValue02",
    "EndCash",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FinReportCashFlow";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FinReportCashFlow_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;