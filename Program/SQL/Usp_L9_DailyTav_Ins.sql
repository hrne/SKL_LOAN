create or replace PROCEDURE "Usp_L9_DailyTav_Ins" 
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AS
BEGIN
  -- EXEC "Usp_L9_DailyTav_Ins"(20220914,'001702');
  DECLARE
    INS_CNT        INT;        -- 新增筆數
    UPD_CNT        INT;        -- 更新筆數
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間
  BEGIN
    INS_CNT :=0;
    UPD_CNT :=0;
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    DELETE FROM "DailyTav"
    WHERE "AcDate" = TBSDYF
    ;

    -- 寫入資料
    INSERT INTO "DailyTav" (
        "AcctCode"
      , "AcDate" -- 會計日期
      , "CustNo" -- 借款人戶號
      , "FacmNo" -- 額度編號
      , "SelfUseFlag" -- 額度自用記號
      , "TavBal" -- 暫收款餘額
      , "LatestFlag" -- 最新記號
      , "CreateDate"
      , "CreateEmpNo"
      , "LastUpdate"
      , "LastUpdateEmpNo"
    )
    WITH AR AS (  -- 2022-12-08 Wei 增加 from Lai
      SELECT "AcctCode"             AS "AcctCode"
           , "CustNo"               AS "CustNo"
           , "FacmNo"               AS "FacmNo"
           , SUM("RvBal")           AS "TavBal"
           , MAX("LastUpdate")      AS "LastUpdate"
           , MAX("LastUpdateEmpNo") AS "LastUpdateEmpNo"
      FROM "AcReceivable"
      WHERE "AcctCode" IN ('TAV')
      -- 2023-04-20 Wei 修改 from Lai : 暫收款不分TLD
      GROUP BY "AcctCode"
             , "CustNo"
             , "FacmNo"
    )
    SELECT -- 2022-12-08 Wei 增加 from Lai
           AR."AcctCode"          AS "AcctCode"
         , TBSDYF                 AS "AcDate"
         , AR."CustNo"            AS "CustNo"
         , AR."FacmNo"            AS "FacmNo"
         , CASE
             WHEN NVL(LFT."CustNo",0) != 0
             THEN 'Y'
           ELSE 'N' END           AS "SelfUseFlag"
         , AR."TavBal"            AS "TavBal"
         , 'Y'                    AS "LatestFlag"
         , AR."LastUpdate"        AS "CreateDate"
         , AR."LastUpdateEmpNo"   AS "CreateEmpNo"
         , AR."LastUpdate"        AS "LastUpdate"
         , AR."LastUpdateEmpNo"   AS "LastUpdateEmpNo"
    FROM AR
    LEFT JOIN "DailyTav" DT ON DT."CustNo" = AR."CustNo"
                           AND DT."FacmNo" = AR."FacmNo"
                           AND DT."AcctCode" = AR."AcctCode"
                           AND DT."LatestFlag" = 'Y'
    LEFT JOIN "LoanFacTmp" LFT ON LFT."CustNo" = AR."CustNo"
                              AND LFT."FacmNo" = AR."FacmNo"
    WHERE NVL(DT."TavBal",0) != AR."TavBal"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 將舊資料的最新記號,更新為N
    MERGE INTO "DailyTav" T
    USING (
      SELECT "AcDate"
           , "AcctCode"
           , "CustNo"
           , "FacmNo"
           , ROW_NUMBER ()
             OVER (
               PARTITION BY "CustNo"
                          , "FacmNo"
                          , "AcctCode"
               ORDER BY "AcDate" DESC
             ) AS "Seq"
      FROM "DailyTav"
      WHERE "LatestFlag" = 'Y'
    ) S
    ON (
      S."AcDate" = T."AcDate"
      AND S."AcctCode" = T."AcctCode"
      AND S."CustNo" = T."CustNo"
      AND S."FacmNo" = T."FacmNo"
      AND S."Seq" >= 2
    )
    WHEN MATCHED THEN UPDATE
    SET "LatestFlag" = 'N'
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    commit;

    -- 例外處理
    -- Exception
    -- WHEN OTHERS THEN
    -- "Usp_L9_UspErrorLog_Ins"(
    --     'Usp_L9_DailyTav_Ins' -- UspName 預存程序名稱
    --   , SQLCODE -- Sql Error Code (固定值)
    --   , SQLERRM -- Sql Error Message (固定值)
    --   , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
    --   , EmpNo -- 發動預存程序的員工編號
    -- );
  END;
END;