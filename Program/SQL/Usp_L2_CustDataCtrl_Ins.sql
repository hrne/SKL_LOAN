--------------------------------------------------------
--  DDL for Procedure Usp_L2_CustDataCtrl_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_L2_CustDataCtrl_Ins" 
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統會計日期(西元)
    EmpNo          IN  VARCHAR2    -- 經辦

)
AS
BEGIN
  -- EXEC "Usp_L2_CustDataCtrl_Ins"(20211208,'999999');
  DECLARE
    INS_CNT        INT;         -- 新增筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間

  BEGIN
    INS_CNT :=0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 筆數預設0
    INS_CNT:=0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 寫入資料
    MERGE INTO "CustDataCtrl" CDC
    USING(
        WITH "ClosedCust" AS (
            -- 取得在撥款主檔結清的最後會計日期
            SELECT "CustNo"
                 , MAX("AcDate") AS "ClosedDate"
            FROM "LoanBorMain" L
            WHERE "Status" IN (1,3,5,8,9,97,98)
            GROUP BY "CustNo"
        )
        , "NotClosedCust" as (
            -- 取得未結清戶號
            SELECT "CustNo"
            FROM "LoanBorMain" L
            WHERE "Status" NOT IN (1,3,5,8,9,97,98)
            GROUP BY"CustNo"
        )
        , "ClosedOver5YearCust" AS (
            -- 取得整戶已結清滿五年的戶號
            SELECT CC."CustNo"
            FROM "ClosedCust" CC
            LEFT JOIN "NotClosedCust" NCC ON NCC."CustNo" = CC."CustNo"
            WHERE NVL(NCC."CustNo",0) = 0
            AND ROUND(MONTHS_BETWEEN(TO_DATE(TBSDYF,'yyyymmdd'),TO_DATE(CC."ClosedDate",'yyyymmdd')),0) > 60
        )
        -- 取得在CustDataCtrl的申請記號不是1跟2的資料
        SELECT CO5C."CustNo"
             , CM."CustUKey"
        FROM "ClosedOver5YearCust" CO5C
        LEFT JOIN "CustMain" CM ON CM."CustNo" = CO5C."CustNo"
        LEFT JOIN "CustDataCtrl" CDC ON CDC."CustNo" = CO5C."CustNo"
        WHERE NVL(CDC."ApplMark",0) NOT IN (1,2)
    ) CO
    ON (
        CO."CustNo" = CDC."CustNo"
        AND CO."CustUKey" = CDC."CustUKey"
    )
    -- 若該戶號已存在且申請記號不是1跟2,更新為2:滿五年自動寫入,並更新最後更新日期時間及最後更新人員
    WHEN MATCHED THEN UPDATE
    SET "ApplMark" = 2
      , "LastUpdate" = SYSTIMESTAMP
      , "LastUpdateEmpNo" = '999999'
    -- 資料不存在,寫入
    WHEN NOT MATCHED THEN INSERT (
        "CustNo"
        , "CustUKey"
        , "ApplMark"
        , "CreateDate"
        , "CreateEmpNo"
        , "LastUpdate"
        , "LastUpdateEmpNo"
    ) VALUES (
        CO."CustNo"
        , CO."CustUKey"
        , 2
        , SYSTIMESTAMP
        , '999999'
        , SYSTIMESTAMP
        , '999999'
    )
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L2_CustDataCtrl_Ins' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;


