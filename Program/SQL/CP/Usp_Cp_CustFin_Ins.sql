CREATE OR REPLACE PROCEDURE "Usp_Cp_CustFin_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustFin" DROP STORAGE';

  INSERT INTO "CustFin" (
    "CustUKey",
    "DataYear",
    "AssetTotal",
    "Cash",
    "ShortInv",
    "AR",
    "Inventory",
    "LongInv",
    "FixedAsset",
    "OtherAsset",
    "LiabTotal",
    "BankLoan",
    "OtherCurrLiab",
    "LongLiab",
    "OtherLiab",
    "NetWorthTotal",
    "Capital",
    "RetainEarning",
    "OpIncome",
    "OpCost",
    "OpProfit",
    "OpExpense",
    "OpRevenue",
    "NopIncome",
    "FinExpense",
    "NopExpense",
    "NetIncome",
    "Accountant",
    "AccountDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustUKey",
    "DataYear",
    "AssetTotal",
    "Cash",
    "ShortInv",
    "AR",
    "Inventory",
    "LongInv",
    "FixedAsset",
    "OtherAsset",
    "LiabTotal",
    "BankLoan",
    "OtherCurrLiab",
    "LongLiab",
    "OtherLiab",
    "NetWorthTotal",
    "Capital",
    "RetainEarning",
    "OpIncome",
    "OpCost",
    "OpProfit",
    "OpExpense",
    "OpRevenue",
    "NopIncome",
    "FinExpense",
    "NopExpense",
    "NetIncome",
    "Accountant",
    "AccountDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CustFin";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CustFin_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;