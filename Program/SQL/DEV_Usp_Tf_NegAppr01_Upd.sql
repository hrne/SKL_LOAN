--------------------------------------------------------
--  DDL for Procedure Usp_Tf_NegAppr01_Upd
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_NegAppr01_Upd" 
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

    -- 計算並更新 累計撥付金額 依照戶號&案件序號&債權機構代號 排序加總
    MERGE INTO "NegAppr01" T1
    USING (WITH N AS (
              SELECT "AcDate"
                   , "TitaTlrNo"
                   , "TitaTxtNo"
                   , "CustNo"
                   , "CaseSeq"
                   , "FinCode"
                   , "ApprAmt"
                   , ROW_NUMBER() OVER (PARTITION BY "CustNo","CaseSeq","FinCode"
                                        ORDER BY "AcDate","TitaTxtNo") AS "Seq"
              FROM "NegAppr01"
           )
           SELECT N1."AcDate"
                 ,N1."TitaTlrNo"
                 ,N1."TitaTxtNo"
                 ,N1."FinCode"
                 ,SUM(N2."ApprAmt") AS "AccuApprAmt"
           FROM N N1
           LEFT JOIN N N2 ON N2."CustNo" = N1."CustNo"
                                           AND N2."CaseSeq" = N1."CaseSeq"
                                           AND N2."FinCode" = N1."FinCode"
                                           AND N2."Seq" <= N1."Seq"
           GROUP BY N1."AcDate"
                   ,N1."TitaTlrNo"
                   ,N1."TitaTxtNo"
                   ,N1."FinCode"
           ORDER BY "AcDate"
                   ,"TitaTlrNo"
                   ,"TitaTxtNo"
                   ,"FinCode"
        ) S1
    ON (    S1."AcDate"    = T1."AcDate"
        AND S1."TitaTlrNo" = T1."TitaTlrNo"
        AND S1."TitaTxtNo" = T1."TitaTxtNo"
        AND S1."FinCode"   = T1."FinCode"
    )
    WHEN MATCHED THEN UPDATE SET
    T1."AccuApprAmt" = S1."AccuApprAmt"
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_NegAppr01_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
