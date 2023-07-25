CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tx_TxHoliday_Ins" 
(
    -- 參數
    EmpNo          IN  VARCHAR2,   -- 經辦
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
)
AS
BEGIN
    --- EXEC "Usp_Tx_TxHoliday_Ins"('999999');
    DECLARE
        LOG_START_TIME TIMESTAMP; -- 記錄程式起始時間   
    BEGIN

    LOG_START_TIME := SYSTIMESTAMP;

    INSERT INTO "TxHoliday" T
    SELECT DISTINCT
           'TW'                          AS "Country"
         , TO_NUMBER(TO_CHAR(NBDT_DATE,'YYYYMMDD'))
                                         AS "Holiday"
         , NBDT_TYPE                     AS "TypeCode"
         , LOG_START_TIME                AS "CreateDate"      -- 建檔日期時間 DATE 
         , EmpNo                         AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
         , LOG_START_TIME                AS "LastUpdate"      -- 最後更新日期時間 DATE 
         , EmpNo                         AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
    FROM TPNBDT T
    LEFT JOIN "TxHoliday" TH ON TH."Holiday" = TO_NUMBER(TO_CHAR(NBDT_DATE,'YYYYMMDD'))
                            AND TH."Country" = 'TW'
    WHERE T.NBDT_PLAN_CODE = 'TAIWAN'
      AND NVL(TH."Holiday",0) = 0
      AND T.NBDT_TYPE IN ('1','2')
    ;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_Tx_TxHoliday_Ins' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
      , JobTxSeq -- 啟動批次的交易序號
    );
    COMMIT;
    RAISE;
    END;
END;

