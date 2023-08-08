CREATE OR REPLACE PROCEDURE "Usp_L6_CdStock_Ins" 
(
    -- 參數 
    "InputEmpNo" IN VARCHAR2,   --執行人員員編 
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
)
AS
BEGIN
  -- 2023-08-08 Wei 新增

  -- 刪除舊資料
  EXECUTE IMMEDIATE 'ALTER TABLE "CdStock" DISABLE PRIMARY KEY CASCADE';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdStock" DROP STORAGE';
  EXECUTE IMMEDIATE 'ALTER TABLE "CdStock" ENABLE PRIMARY KEY';

  -- 寫入資料
  INSERT INTO "CdStock" (
      "StockCode"        -- 股票代號 VARCHAR2 10 
    , "StockItem"        -- 股票簡稱 NVARCHAR2 40
    , "StockCompanyName" -- 股票公司名稱 NVARCHAR2 50
    , "Currency"         -- 幣別 VARCHAR2 3
    , "YdClosePrice"     -- 前日收盤價 DECIMAL 16 2
    , "MonthlyAvg"       -- 一個月平均價 DECIMAL 16 2
    , "ThreeMonthAvg"    -- 三個月平均價 DECIMAL 16 2
    , "StockType"        -- 上市上櫃記號 DECIMAL 1
    , "CreateDate"       -- 建檔日期時間 DATE 0 
    , "CreateEmpNo"      -- 建檔人員 VARCHAR2 6 
    , "LastUpdate"       -- 最後更新日期時間 DATE 0 
    , "LastUpdateEmpNo"  -- 最後更新人員 VARCHAR2 6 
  )
  SELECT NVL(S1.STOCK_NO,S0.ID)     AS "StockCode"        -- 股票代號 VARCHAR2 10 
       , NVL(S1.STOCK_NAME,S0.NAME) AS "StockItem"        -- 股票簡稱 NVARCHAR2 40
       , S0.CNAME                   AS "StockCompanyName" -- 股票公司名稱 NVARCHAR2 50
       , 'TWD'                      AS "Currency"         -- 幣別 VARCHAR2 3
       , NVL(S1.CLOSE_PRICE,0)      AS "YdClosePrice"     -- 前日收盤價 DECIMAL 16 2
       , 0                          AS "MonthlyAvg"       -- 一個月平均價 DECIMAL 16 2
       , 0                          AS "ThreeMonthAvg"    -- 三個月平均價 DECIMAL 16 2
       , NVL(S0.LCOMP,0)            AS "StockType"        -- 上市上櫃記號 DECIMAL 1
       , S0.MODIFYDATE              AS "CreateDate"       -- 建檔日期時間 DATE 0 
       , '999999'                   AS "CreateEmpNo"      -- 建檔人員 VARCHAR2 6 
       , S1.DDATE                   AS "LastUpdate"       -- 最後更新日期時間 DATE 0 
       , '999999'                   AS "LastUpdateEmpNo"  -- 最後更新人員 VARCHAR2 6 
  FROM TPCMONEYCON S0
  LEFT JOIN (
      SELECT STOCK_NO
           , STOCK_NAME
           , CLOSE_PRICE
           , DDATE
           , ROW_NUMBER()
             OVER (
               PARTITION BY STOCK_NO
               ORDER BY DDATE DESC
             )                   AS "Seq"
      FROM TPCMONEYSTOCKCB
  ) S1 ON S1.STOCK_NO = S0.ID
      AND S1."Seq" = 1
  ORDER BY S0.ID
  ;

  -- 記錄寫入筆數
  INS_CNT := INS_CNT + sql%rowcount;

  -- 寫入資料
  INSERT INTO "CdStock" (
      "StockCode"        -- 股票代號 VARCHAR2 10 
    , "StockItem"        -- 股票簡稱 NVARCHAR2 40
    , "StockCompanyName" -- 股票公司名稱 NVARCHAR2 50
    , "Currency"         -- 幣別 VARCHAR2 3
    , "YdClosePrice"     -- 前日收盤價 DECIMAL 16 2
    , "MonthlyAvg"       -- 一個月平均價 DECIMAL 16 2
    , "ThreeMonthAvg"    -- 三個月平均價 DECIMAL 16 2
    , "StockType"        -- 上市上櫃記號 DECIMAL 1
    , "CreateDate"       -- 建檔日期時間 DATE 0 
    , "CreateEmpNo"      -- 建檔人員 VARCHAR2 6 
    , "LastUpdate"       -- 最後更新日期時間 DATE 0 
    , "LastUpdateEmpNo"  -- 最後更新人員 VARCHAR2 6 
  )
  SELECT S0."NewStockNo"            AS "StockCode"        -- 股票代號 VARCHAR2 10 
       , S0."NewStockName"          AS "StockItem"        -- 股票簡稱 NVARCHAR2 40
       , S0."NewStockName"          AS "StockCompanyName" -- 股票公司名稱 NVARCHAR2 50
       , 'TWD'                      AS "Currency"         -- 幣別 VARCHAR2 3
       , 0                          AS "YdClosePrice"     -- 前日收盤價 DECIMAL 16 2
       , 0                          AS "MonthlyAvg"       -- 一個月平均價 DECIMAL 16 2
       , 0                          AS "ThreeMonthAvg"    -- 三個月平均價 DECIMAL 16 2
       , NVL(S1.LCOMP,0)            AS "StockType"        -- 上市上櫃記號 DECIMAL 1
       , S1.MODIFYDATE              AS "CreateDate"       -- 建檔日期時間 DATE 0 
       , '999999'                   AS "CreateEmpNo"      -- 建檔人員 VARCHAR2 6 
       , S1.MODIFYDATE              AS "LastUpdate"       -- 最後更新日期時間 DATE 0 
       , '999999'                   AS "LastUpdateEmpNo"  -- 最後更新人員 VARCHAR2 6 
  FROM "TempCdStockMapping" S0
  LEFT JOIN TPCMONEYCON S1 ON S1.ID = S0."NewStockNo"
  WHERE NVL(S1.ID,' ') = ' '
  ;

  commit;

  -- 例外處理
  Exception
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
      'Usp_L6_CdStock_Ins' -- UspName 預存程序名稱
    , SQLCODE -- Sql Error Code (固定值)
    , SQLERRM -- Sql Error Message (固定值)
    , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
    , "InputEmpNo" -- 發動預存程序的員工編號
    , JobTxSeq -- 啟動批次的交易序號
  );
  COMMIT;
  RAISE;
END;





/
