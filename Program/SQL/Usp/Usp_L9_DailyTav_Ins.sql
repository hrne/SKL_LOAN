CREATE OR REPLACE PROCEDURE "Usp_L9_DailyTav_Ins" 
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

    -- 寫入資料
    INSERT INTO "DailyTav" (
        "AcDate" -- 會計日期
      , "CustNo" -- 借款人戶號
      , "FacmNo" -- 額度編號
      , "SelfUseFlag" -- 額度自用記號
      , "TavBal" -- 暫收款餘額
      , "LatestFlag" -- 最新記號
      , "AcctCode"
      , "CreateDate"
      , "CreateEmpNo"
      , "LastUpdate"
      , "LastUpdateEmpNo"
    )
    SELECT TBSDYF                 AS "AcDate"
         , AR."CustNo"            AS "CustNo"
         , AR."FacmNo"            AS "FacmNo"
         , CASE
             WHEN NVL(LFT."CustNo",0) != 0
             THEN 'Y'
           ELSE 'N' END           AS "SelfUseFlag"
         , AR."RvBal"             AS "TavBal"
         , 'Y'                    AS "LatestFlag"
         -- 2022-12-08 Wei 增加 from Lai
         , AR."AcctCode"          AS "AcctCode"
         , AR."LastUpdate"        AS "CreateDate"
         , AR."LastUpdateEmpNo"   AS "CreateEmpNo"
         , AR."LastUpdate"        AS "LastUpdate"
         , AR."LastUpdateEmpNo"   AS "LastUpdateEmpNo"
    FROM "AcReceivable" AR
    LEFT JOIN "DailyTav" DT ON DT."CustNo" = AR."CustNo"
                           AND DT."FacmNo" = AR."FacmNo"
                           AND DT."LatestFlag" = 'Y'
    LEFT JOIN "LoanFacTmp" LFT ON LFT."CustNo" = AR."CustNo"
                              AND LFT."FacmNo" = AR."FacmNo"
    WHERE AR."AcctCode" IN ('TAV','T10','TLD')
      AND NVL(DT."TavBal",0) != AR."RvBal"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 將舊資料的最新記號,更新為N
    MERGE INTO "DailyTav" T
    USING (
      SELECT "AcDate"
           , "CustNo"
           , "FacmNo"
           , ROW_NUMBER ()
             OVER (
               PARTITION BY "CustNo"
                          , "FacmNo"
               ORDER BY "AcDate" DESC
             ) AS "Seq"
      FROM "DailyTav"
      WHERE "LatestFlag" = 'Y'
    ) S
    ON (
      S."AcDate" = T."AcDate"
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