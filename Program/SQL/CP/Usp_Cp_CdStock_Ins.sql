CREATE OR REPLACE PROCEDURE "Usp_Cp_CdStock_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdStock" DROP STORAGE';

  INSERT INTO "CdStock" (
    "StockCode",
    "StockItem",
    "StockCompanyName",
    "Currency",
    "YdClosePrice",
    "MonthlyAvg",
    "ThreeMonthAvg",
    "StockType",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "StockCode",
    "StockItem",
    "StockCompanyName",
    "Currency",
    "YdClosePrice",
    "MonthlyAvg",
    "ThreeMonthAvg",
    "StockType",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdStock";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdStock_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;