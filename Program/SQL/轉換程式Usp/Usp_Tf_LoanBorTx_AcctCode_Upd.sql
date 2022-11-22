create or replace PROCEDURE "Usp_Tf_LoanBorTx_AcctCode_Upd" 
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

    -- 更新資料 
    MERGE INTO "LoanBorTx" T
    USING (
        WITH rawData AS (
            SELECT DISTINCT
                   "AcDate"
                 , "TitaTlrNo"
                 , "TitaTxtNo"
                 , "AcctCode"
            FROM "AcDetail"
            WHERE "AcctCode" IN ('T11','T12','T13','T21','T22','T23')
        )
        , filterData as (
            SELECT "AcDate","TitaTlrNo","TitaTxtNo",count(*) as "count"
            FROM rawData
            GROUP BY "AcDate","TitaTlrNo","TitaTxtNo"
        )
        , acData AS (
            SELECT rawData."AcDate"
                 , rawData."TitaTlrNo"
                 , rawData."TitaTxtNo"
                 , rawData."AcctCode"
                 , filterData."count"
            FROM filterData
            LEFT JOIN rawData
                ON rawData."AcDate" = filterData."AcDate"
                AND rawData."TitaTlrNo" = filterData."TitaTlrNo"
                AND rawData."TitaTxtNo" = filterData."TitaTxtNo"
        )
        SELECT TX."CustNo"
             , TX."FacmNo"
             , TX."BormNo"
             , TX."BorxNo"
             , TX."AcDate"
             , TX."TitaTlrNo"
             , TX."TitaTxtNo"
             , TX."TitaTxCd"
             , TX."Desc"
             , TX."AcctCode" AS "OriAcctCode"
             , AC."AcctCode"
        FROM "LoanBorTx" TX
        LEFT JOIN acData AC
            ON AC."AcDate" = TX."AcDate"
            AND AC."TitaTlrNo" = TX."TitaTlrNo"
            AND AC."TitaTxtNo" = TX."TitaTxtNo"
        WHERE AC."AcctCode" IS NOT NULL
    ) S
    ON (
        S."CustNo" = T."CustNo"
        AND S."FacmNo" = T."FacmNo"
        AND S."BormNo" = T."BormNo"
        AND S."BorxNo" = T."BorxNo"
    )
    WHEN MATCHED THEN UPDATE SET
    T."AcctCode" = S."AcctCode"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanBorTx_AcctCode_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;