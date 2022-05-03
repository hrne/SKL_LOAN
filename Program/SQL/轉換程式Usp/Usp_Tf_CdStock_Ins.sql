--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdStock_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CdStock_Ins" 
(
    -- 參數
    JOB_START_TIME OUT TIMESTAMP, --程式起始時間
    JOB_END_TIME   OUT TIMESTAMP, --程式結束時間
    INS_CNT        OUT INT,       --新增資料筆數
    ERROR_MSG      OUT VARCHAR2   --錯誤訊息
)
AS
BEGIN
    -- 筆數預設0
    INS_CNT:=0;
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "CdStock" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdStock" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdStock" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CdStock"
    SELECT NVL(S1.STOCK_NO,S0.ID)     AS "StockCode"        -- 股票代號 VARCHAR2 10 
         , NVL(S1.STOCK_NAME,S0.NAME) AS "StockItem"        -- 股票簡稱 NVARCHAR2 40
         , S0.CNAME                   AS "StockCompanyName" -- 股票公司名稱 NVARCHAR2 50
         , 'TWD'                      AS "Currency"         -- 幣別 VARCHAR2 3
         , NVL(S1.CLOSE_PRICE,0)      AS "YdClosePrice"     -- 前日收盤價 DECIMAL 16 2
         , 0                          AS "MonthlyAvg"       -- 一個月平均價 DECIMAL 16 2
         , 0                          AS "ThreeMonthAvg"    -- 三個月平均價 DECIMAL 16 2
         , NVL(S0.LCOMP,0)            AS "StockType"        -- 上市上櫃記號 DECIMAL 1
         , JOB_START_TIME             AS "CreateDate"       -- 建檔日期時間 DATE 0 
         , '999999'                   AS "CreateEmpNo"      -- 建檔人員 VARCHAR2 6 
         , JOB_START_TIME             AS "LastUpdate"       -- 最後更新日期時間 DATE 0 
         , '999999'                   AS "LastUpdateEmpNo"  -- 最後更新人員 VARCHAR2 6 
    FROM TPCMONEYCON S0
    LEFT JOIN (
        SELECT STOCK_NO
             , STOCK_NAME
             , CLOSE_PRICE
             , ROW_NUMBER() OVER (PARTITION BY STOCK_NO
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
    INSERT INTO "CdStock"
    SELECT S0."NewStockNo"            AS "StockCode"        -- 股票代號 VARCHAR2 10 
         , S0."NewStockName"          AS "StockItem"        -- 股票簡稱 NVARCHAR2 40
         , S0."NewStockName"          AS "StockCompanyName" -- 股票公司名稱 NVARCHAR2 50
         , 'TWD'                      AS "Currency"         -- 幣別 VARCHAR2 3
         , 0                          AS "YdClosePrice"     -- 前日收盤價 DECIMAL 16 2
         , 0                          AS "MonthlyAvg"       -- 一個月平均價 DECIMAL 16 2
         , 0                          AS "ThreeMonthAvg"    -- 三個月平均價 DECIMAL 16 2
         , NVL(S1.LCOMP,0)            AS "StockType"        -- 上市上櫃記號 DECIMAL 1
         , JOB_START_TIME             AS "CreateDate"       -- 建檔日期時間 DATE 0 
         , '999999'                   AS "CreateEmpNo"      -- 建檔人員 VARCHAR2 6 
         , JOB_START_TIME             AS "LastUpdate"       -- 最後更新日期時間 DATE 0 
         , '999999'                   AS "LastUpdateEmpNo"  -- 最後更新人員 VARCHAR2 6 
    FROM "TempCdStockMapping" S0
    LEFT JOIN TPCMONEYCON S1 ON S1.ID = S0."NewStockNo"
    WHERE NVL(S1.ID,' ') = ' '
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;
    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
END;





/
