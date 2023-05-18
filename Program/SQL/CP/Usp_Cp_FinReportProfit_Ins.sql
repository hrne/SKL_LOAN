CREATE OR REPLACE PROCEDURE "Usp_Cp_FinReportProfit_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FinReportProfit" DROP STORAGE';

  INSERT INTO "FinReportProfit" (
    "CustUKey",
    "Ukey",
    "BusIncome",
    "GrowRate",
    "BusCost",
    "BusGrossProfit",
    "ManageFee",
    "BusLossProfit",
    "BusOtherIncome",
    "Interest",
    "BusOtherFee",
    "BeforeTaxNet",
    "BusTax",
    "HomeLossProfit",
    "OtherComLossProfit",
    "HomeComLossProfit",
    "UncontrolRight",
    "ParentCompanyRight",
    "EPS",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustUKey",
    "Ukey",
    "BusIncome",
    "GrowRate",
    "BusCost",
    "BusGrossProfit",
    "ManageFee",
    "BusLossProfit",
    "BusOtherIncome",
    "Interest",
    "BusOtherFee",
    "BeforeTaxNet",
    "BusTax",
    "HomeLossProfit",
    "OtherComLossProfit",
    "HomeComLossProfit",
    "UncontrolRight",
    "ParentCompanyRight",
    "EPS",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FinReportProfit";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FinReportProfit_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;