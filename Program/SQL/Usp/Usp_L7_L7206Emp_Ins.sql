create or replace NONEDITIONABLE PROCEDURE "Usp_L7_L7206Emp_Ins" 
( 
    -- 參數 
    "EmpNo" IN VARCHAR2   --執行人員員編 
) 
AS 
BEGIN  
-- exec "Usp_L7_L7206Emp_Ins"('001702'); 

  -- 刪除舊資料 
  EXECUTE IMMEDIATE 'ALTER TABLE "L7206Emp" DISABLE PRIMARY KEY CASCADE'; 
  EXECUTE IMMEDIATE 'TRUNCATE TABLE "L7206Emp" DROP STORAGE'; 
  EXECUTE IMMEDIATE 'ALTER TABLE "L7206Emp" ENABLE PRIMARY KEY'; 
 
  -- 寫入資料 
  INSERT INTO "L7206Emp" (
      "LogNo" -- 序號 AUTO 
    , "CustId" -- 身分證/統一編號 VARCHAR2 10
    , "CustName" -- 姓名 NVARCHAR2 42
    , "CustNo" -- 戶號 Decimal 7
    , "DataMonth" -- 資料年月 Decimal 6
    , "SumLoanBal" -- 合計放款餘額 Decimal 15
    , "CreateDate" -- 建檔日期時間 TIMESTAMP 
    , "CreateEmpNo" -- 建檔人員 VARCHAR2 6
    , "LastUpdate" -- 最後更新日期時間 TIMESTAMP 
    , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
  )
  WITH monthData AS (
    SELECT MAX("YearMonth") AS "MaxYearMonth"
    FROM "MonthlyLoanBal"
  ) 
  , sumData AS (
    SELECT SUBSTR(C."CustId",1,10) AS "CustId"
         , SUBSTR(C."CustName",1,21) AS "CustName"
         , M."CustNo"
         , M."YearMonth"    AS "DataMonth"
         , SUM(M."LoanBalance") AS "SumLoanBal"
    FROM monthData d
    LEFT JOIN "MonthlyLoanBal" M ON M."YearMonth" = d."MaxYearMonth"
    LEFT JOIN "CustMain" C ON C."CustNo" = M."CustNo"
    WHERE M."LoanBalance" > 0
    GROUP BY SUBSTR(C."CustId",1,10)
           , SUBSTR(C."CustName",1,21)
           , M."CustNo"
           , M."YearMonth"
  )
  SELECT "L7206Emp_SEQ".nextval  AS "LogNo" -- 序號 AUTO 
       , S."CustId"               AS "CustId" -- 身分證/統一編號 VARCHAR2 10
       , S."CustName"             AS "CustName" -- 姓名 NVARCHAR2 42
       , S."CustNo"               AS "CustNo" -- 戶號 Decimal 7
       , S."DataMonth"            AS "DataMonth" -- 資料年月 Decimal 6
       , S."SumLoanBal"           AS "SumLoanBal" -- 合計放款餘額 Decimal 15
       , SYSTIMESTAMP             AS "CreateDate"      -- 建檔日期時間  
       , SUBSTR("EmpNo",0,6)      AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
       , SYSTIMESTAMP             AS "LastUpdate"      -- 最後更新日期時間  
       , SUBSTR("EmpNo",0,6)      AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
  FROM sumData S
  ; 
  
  commit;

  -- 例外處理
  Exception
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
      'Usp_L7_L7206Emp_Ins' -- UspName 預存程序名稱
    , SQLCODE -- Sql Error Code (固定值)
    , SQLERRM -- Sql Error Message (固定值)
    , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
    , "EmpNo" -- 發動預存程序的員工編號
    , null -- 啟動批次的交易序號
  );
  COMMIT;
  RAISE;
END;