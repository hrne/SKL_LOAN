create or replace NONEDITIONABLE PROCEDURE "Usp_L7_L7206Manager_Ins" 
( 
    -- 參數 
    "EmpNo" IN VARCHAR2   --執行人員員編 
) 
AS 
BEGIN  
-- exec "Usp_L7_L7206Manager_Ins"('001702'); 

  -- 刪除舊資料 
  EXECUTE IMMEDIATE 'ALTER TABLE "L7206Manager" DISABLE PRIMARY KEY CASCADE'; 
  EXECUTE IMMEDIATE 'TRUNCATE TABLE "L7206Manager" DROP STORAGE'; 
  EXECUTE IMMEDIATE 'ALTER TABLE "L7206Manager" ENABLE PRIMARY KEY'; 
 
  -- 寫入資料 
  INSERT INTO "L7206Manager" (
      "LogNo" -- 序號 AUTO 
    , "CustId" -- 身分證/統一編號 VARCHAR2 10
    , "CustName" -- 姓名 NVARCHAR2 42
    , "ManagerId" -- 負責人身分證/統一編號 VARCHAR2 10
    , "CustNo" -- 戶號 Decimal 7
    , "FacmNo" -- 額度號碼 Decimal 3
    , "DataMonth" -- 資料年月 Decimal 6
    , "AvgLineAmt" -- 平均核准額度 Decimal 15
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
         , SUBSTR(C."SpouseId",1,10) AS "ManagerId"
         , M."CustNo"
         , M."FacmNo"
         , M."YearMonth"    AS "DataMonth"
         , F."LineAmt"      AS "AvgLineAmt"
         , SUM(M."LoanBalance") AS "SumLoanBal"
    FROM monthData d
    LEFT JOIN "MonthlyLoanBal" M ON M."YearMonth" = d."MaxYearMonth"
    LEFT JOIN "CustMain" C ON C."CustNo" = M."CustNo"
    LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                         AND F."FacmNo" = M."FacmNo"
    WHERE M."LoanBalance" > 0
    GROUP BY SUBSTR(C."CustId",1,10)
           , SUBSTR(C."CustName",1,21)
           , SUBSTR(C."SpouseId",1,10)
           , M."CustNo"
           , M."FacmNo"
           , M."YearMonth"
           , F."LineAmt"
  )
  SELECT "L7206Manager_SEQ".nextval  AS "LogNo" -- 序號 AUTO 
       , S."CustId"               AS "CustId" -- 身分證/統一編號 VARCHAR2 10
       , S."CustName"             AS "CustName" -- 姓名 NVARCHAR2 42
       , S."CustNo"               AS "CustNo" -- 戶號 Decimal 7
       , S."ManagerId"            AS "ManagerId" -- 負責人身分證/統一編號 VARCHAR2 10
       , S."FacmNo"               AS "FacmNo" -- 額度號碼 Decimal 3
       , S."DataMonth"            AS "DataMonth" -- 資料年月 Decimal 6
       , S."AvgLineAmt"           AS "AvgLineAmt" -- 平均核准額度 Decimal 15
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
      'Usp_L7_L7206Manager_Ins' -- UspName 預存程序名稱
    , SQLCODE -- Sql Error Code (固定值)
    , SQLERRM -- Sql Error Message (固定值)
    , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
    , "EmpNo" -- 發動預存程序的員工編號
    , null -- 啟動批次的交易序號
  );
  COMMIT;
  RAISE;
END;