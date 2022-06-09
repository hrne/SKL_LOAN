CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L2_ForeclosureFinished_Upd" 
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統會計日期(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AS
BEGIN
  -- EXEC "Usp_L2_ForeclosureFinished_Upd"(20211208,'999999');
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
    -- ITX 每月更新法拍完成檔ForeclosureFinished
    -- 根據AS400 原程式 LNSP66A-LN6622 開發
    -- 2022-05-12 ST1 Wei 新增
    -- 需求為ST1 Linda提出
    -- SKL 清河 提供原程式名稱
    -- ST1 Wei 根據原程式編寫
    -- 發動時機:固定每月底,此為Linda詢問清河的回覆
    MERGE INTO "ForeclosureFinished" T
    USING (
        WITH rawData AS (
            SELECT "CustNo"
                 , "FacmNo"
                 , "RecordDate"
                 , "LegalProg"
                 , "CreateDate"
                 , "CreateEmpNo"
                 , "LastUpdate"
                 , "LastUpdateEmpNo"
                 , ROW_NUMBER()
                   OVER (
                       PARTITION BY "CustNo"
                                   , "FacmNo"
                       ORDER BY "RecordDate" DESC -- 取最新的法拍完成日
                   ) AS "Seq"
            FROM "CollLaw"
            WHERE "LegalProg" = '060'
        )
        SELECT "CustNo"
             , "FacmNo"
             , "RecordDate"
             , "LegalProg"
             , "CreateDate"
             , "CreateEmpNo"
             , "LastUpdate"
             , "LastUpdateEmpNo"
        FROM rawData
        WHERE "Seq" = 1
    ) S
    ON (
        T."CustNo" = S."CustNo"
        AND T."FacmNo" = S."FacmNo"
    )
    WHEN MATCHED THEN UPDATE
    SET T."FinishedDate" = CASE
                             WHEN S."RecordDate" > T."FinishedDate"
                             THEN S."RecordDate"
                           ELSE T."FinishedDate" END
      , T."LastUpdate" = CASE
                           WHEN S."RecordDate" > T."FinishedDate"
                           THEN S."LastUpdate"
                         ELSE T."LastUpdate" END
      , T."LastUpdateEmpNo" = CASE
                                WHEN S."RecordDate" > T."FinishedDate"
                                THEN S."LastUpdateEmpNo"
                              ELSE T."LastUpdateEmpNo" END
    WHEN NOT MATCHED THEN INSERT
    (
        "CustNo"
      , "FacmNo"
      , "FinishedDate"
      , "CreateDate"
      , "CreateEmpNo"
      , "LastUpdate"
      , "LastUpdateEmpNo"
    ) VALUES (
        S."CustNo"
      , S."FacmNo"
      , S."RecordDate"
      , S."CreateDate"
      , S."CreateEmpNo"
      , S."LastUpdate"
      , S."LastUpdateEmpNo"
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
        'Usp_L2_ForeclosureFinished_Upd' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;


