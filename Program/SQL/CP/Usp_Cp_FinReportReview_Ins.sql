CREATE OR REPLACE PROCEDURE "Usp_Cp_FinReportReview_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FinReportReview" DROP STORAGE';

  INSERT INTO "FinReportReview" (
    "CustUKey",
    "Ukey",
    "CurrentAsset",
    "TotalAsset",
    "PropertyAsset",
    "Investment",
    "InvestmentProperty",
    "Depreciation",
    "CurrentDebt",
    "TotalDebt",
    "TotalEquity",
    "BondsPayable",
    "LongTermBorrowings",
    "NonCurrentLease",
    "LongTermPayable",
    "Preference",
    "OperatingRevenue",
    "InterestExpense",
    "ProfitBeforeTax",
    "ProfitAfterTax",
    "WorkingCapitalRatio",
    "InterestCoverageRatio1",
    "InterestCoverageRatio2",
    "LeverageRatio",
    "EquityRatio",
    "LongFitRatio",
    "NetProfitRatio",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustUKey",
    "Ukey",
    "CurrentAsset",
    "TotalAsset",
    "PropertyAsset",
    "Investment",
    "InvestmentProperty",
    "Depreciation",
    "CurrentDebt",
    "TotalDebt",
    "TotalEquity",
    "BondsPayable",
    "LongTermBorrowings",
    "NonCurrentLease",
    "LongTermPayable",
    "Preference",
    "OperatingRevenue",
    "InterestExpense",
    "ProfitBeforeTax",
    "ProfitAfterTax",
    "WorkingCapitalRatio",
    "InterestCoverageRatio1",
    "InterestCoverageRatio2",
    "LeverageRatio",
    "EquityRatio",
    "LongFitRatio",
    "NetProfitRatio",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FinReportReview";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FinReportReview_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;