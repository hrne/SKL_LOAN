create or replace PROCEDURE "Usp_Tf_LoanBorTx_OtherFields_ReconCode_Upd" 
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
                 , "ReconCode"
            FROM "BankRmtf"
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
                 , rawData."ReconCode"
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
             , SUBSTR(TX."OtherFields",0,INSTR(TX."OtherFields",'}')-1) AS "OtherFields"
             , ',"ReconCode":"' || AC."ReconCode" || '"}' AS "ReconCode"
        FROM "LoanBorTx" TX
        LEFT JOIN acData AC
            ON AC."AcDate" = TX."AcDate"
            AND AC."TitaTlrNo" = TX."TitaTlrNo"
            AND AC."TitaTxtNo" = TX."TitaTxtNo"
        WHERE AC."ReconCode" IS NOT NULL
    ) S
    ON (
        S."CustNo" = T."CustNo"
        AND S."FacmNo" = T."FacmNo"
        AND S."BormNo" = T."BormNo"
        AND S."BorxNo" = T."BorxNo"
    )
    WHEN MATCHED THEN UPDATE SET
    T."OtherFields" = S."OtherFields" 
                      || CASE  
                           WHEN T."OtherFields" NOT LIKE '%TempReasonCode%' -- 若未有暫收原因,補上暫收原因
                           THEN ',"TempReasonCode":"00"' 
                         ELSE '' 
                         END 
                      || S."ReconCode"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanBorTx_OtherFields_ReconCode_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;