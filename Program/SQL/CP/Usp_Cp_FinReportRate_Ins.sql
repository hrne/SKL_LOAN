CREATE OR REPLACE PROCEDURE "Usp_Cp_FinReportRate_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FinReportRate" DROP STORAGE';

  INSERT INTO "FinReportRate" (
    "CustUKey",
    "Ukey",
    "IsSameTrade",
    "TradeType",
    "Flow",
    "Speed",
    "RateGuar",
    "Debt",
    "Net",
    "CashFlow",
    "FixLong",
    "FinSpend",
    "GrossProfit",
    "AfterTaxNet",
    "NetReward",
    "TotalAssetReward",
    "Stock",
    "ReceiveAccount",
    "TotalAsset",
    "PayAccount",
    "AveTotalAsset",
    "AveNetBusCycle",
    "FinLever",
    "LoanDebtNet",
    "BusRate",
    "PayFinLever",
    "ADE",
    "CashGuar",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustUKey",
    "Ukey",
    "IsSameTrade",
    "TradeType",
    "Flow",
    "Speed",
    "RateGuar",
    "Debt",
    "Net",
    "CashFlow",
    "FixLong",
    "FinSpend",
    "GrossProfit",
    "AfterTaxNet",
    "NetReward",
    "TotalAssetReward",
    "Stock",
    "ReceiveAccount",
    "TotalAsset",
    "PayAccount",
    "AveTotalAsset",
    "AveNetBusCycle",
    "FinLever",
    "LoanDebtNet",
    "BusRate",
    "PayFinLever",
    "ADE",
    "CashGuar",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FinReportRate";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FinReportRate_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;