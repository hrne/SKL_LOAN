CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L7_SlipMedia_Upd" 
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統會計日期(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    AcDate         IN  INT,        -- 傳票會計日期(西元)
    BatchNo        IN  INT         -- 傳票批號
)
AS
BEGIN
  -- EXEC "Usp_L7_SlipMedia_Upd"(20220207,'001702',20220207,1);
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
    MERGE INTO "AcDetail" T
    USING (
        WITH SLIP AS (
            SELECT "AcDate"
                 , "BatchNo"
                 , "AcBookCode"
                 , "AcSubBookCode"
                 , "AcNoCode"
                 , "AcSubCode"
                 , "ReceiveCode"
                 , "MediaSlipNo"
            FROM "SlipMedia2022"
            WHERE "AcDate" = AcDate
              AND "BatchNo" = BatchNo
              AND "LatestFlag" = 'Y'
              AND "TransferFlag" = 'Y'
            GROUP BY "AcDate"
                   , "BatchNo"
                   , "AcBookCode"
                   , "AcSubBookCode"
                   , "AcNoCode"
                   , "AcSubCode"
                   , "ReceiveCode"
                   , "MediaSlipNo"
        )
        SELECT AC."RelDy"
             , AC."RelTxseq"
             , AC."AcSeq"
             , SLIP."MediaSlipNo"
        FROM "AcDetail" AC
        LEFT JOIN SLIP ON SLIP."AcDate" = AC."AcDate"
                      AND SLIP."BatchNo" = AC."SlipBatNo"
                      AND SLIP."AcBookCode" = AC."AcBookCode"
                      AND SLIP."AcSubBookCode" = AC."AcSubBookCode"
                      AND SLIP."AcNoCode" = AC."AcNoCode"
                      AND SLIP."AcSubCode" = AC."AcSubCode"
                      AND SLIP."ReceiveCode" = CASE
		                                             WHEN AC."ReceivableFlag" = '8'
		                                             THEN AC."RvNo"
		                                           ELSE ' ' END -- 銷帳碼
        WHERE AC."AcDate" = AcDate
          AND AC."SlipBatNo" = BatchNo
          AND AC."EntAc" != 9 -- 排除入總帳記號為9的資料
          AND AC."EntAc" > 0
          AND NVL(SLIP."MediaSlipNo", ' ') != ' '
    ) S
    ON (
        T."RelDy" = S."RelDy"
        AND T."RelTxseq" = S."RelTxseq"
        AND T."AcSeq" = S."AcSeq"
    )
    WHEN MATCHED THEN UPDATE
    SET T."MediaSlipNo" = S."MediaSlipNo"
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    -- Exception
    -- WHEN OTHERS THEN
    -- "Usp_L9_UspErrorLog_Ins"(
    --     'Usp_L7_SlipMedia_Upd' -- UspName 預存程序名稱
    --   , SQLCODE -- Sql Error Code (固定值)
    --   , SQLERRM -- Sql Error Message (固定值)
    --   , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
    --   , EmpNo -- 發動預存程序的員工編號
    -- );
  END;
END;


