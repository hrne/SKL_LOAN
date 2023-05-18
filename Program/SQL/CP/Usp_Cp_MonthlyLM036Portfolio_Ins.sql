CREATE OR REPLACE PROCEDURE "Usp_Cp_MonthlyLM036Portfolio_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "MonthlyLM036Portfolio" DROP STORAGE';

  INSERT INTO "MonthlyLM036Portfolio" (
    "DataMonth",
    "MonthEndDate",
    "PortfolioTotal",
    "NaturalPersonLoanBal",
    "LegalPersonLoanBal",
    "SyndLoanBal",
    "StockLoanBal",
    "OtherLoanbal",
    "AmortizeTotal",
    "OvduExpense",
    "NaturalPersonLargeCounts",
    "NaturalPersonLargeTotal",
    "LegalPersonLargeCounts",
    "LegalPersonLargeTotal",
    "NaturalPersonPercent",
    "LegalPersonPercent",
    "SyndPercent",
    "StockPercent",
    "OtherPercent",
    "EntUsedPercent",
    "InsuDividendRate",
    "NaturalPersonRate",
    "LegalPersonRate",
    "SyndRate",
    "StockRate",
    "OtherRate",
    "AvgRate",
    "HouseRateOfReturn",
    "EntRateOfReturn",
    "RateOfReturn",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataMonth",
    "MonthEndDate",
    "PortfolioTotal",
    "NaturalPersonLoanBal",
    "LegalPersonLoanBal",
    "SyndLoanBal",
    "StockLoanBal",
    "OtherLoanbal",
    "AmortizeTotal",
    "OvduExpense",
    "NaturalPersonLargeCounts",
    "NaturalPersonLargeTotal",
    "LegalPersonLargeCounts",
    "LegalPersonLargeTotal",
    "NaturalPersonPercent",
    "LegalPersonPercent",
    "SyndPercent",
    "StockPercent",
    "OtherPercent",
    "EntUsedPercent",
    "InsuDividendRate",
    "NaturalPersonRate",
    "LegalPersonRate",
    "SyndRate",
    "StockRate",
    "OtherRate",
    "AvgRate",
    "HouseRateOfReturn",
    "EntRateOfReturn",
    "RateOfReturn",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."MonthlyLM036Portfolio";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_MonthlyLM036Portfolio_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;