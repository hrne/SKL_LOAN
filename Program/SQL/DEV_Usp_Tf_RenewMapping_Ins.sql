--------------------------------------------------------
--  DDL for Procedure Usp_Tf_RenewMapping_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_RenewMapping_Ins" 
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
    DELETE FROM "RenewMapping";

    -- 寫入資料
    INSERT INTO "RenewMapping"
    SELECT T1.TRXDAT AS "AcDate"
         , T1.LMSACN AS "CustNo"
         , T1."CloseFacmNo"
         , T1."CloseBormNo"
         , T1."CloseAmt"
         , T3."CloseTotal"
         , T2."RenewFacmNo"
         , T2."RenewBormNo"
         , T2."RenewAmt"
         , ACN.ADTYMT
         , LM.LMSNEW
    FROM (
        SELECT TRXDAT
             , LMSACN
             , LMSAPN AS "CloseFacmNo"
             , LMSASQ AS "CloseBormNo"
             , TRXNMT
             , SUM(TRXAMT)     AS "CloseAmt"
        FROM LA$TRXP
        WHERE TRXTRN = '3041' -- 結案的交易代號
          AND TRXTCT = 1 -- 結案區分為展期
        GROUP BY TRXDAT
               , LMSACN
               , LMSAPN
               , LMSASQ
               , TRXNMT
    )  T1
    LEFT JOIN (
        SELECT TRXDAT
             , LMSACN
             , LMSAPN AS "RenewFacmNo"
             , LMSASQ AS "RenewBormNo"
             , TRXNMT
             , SUM(TRXAMT) AS "RenewAmt"
        FROM LA$TRXP
        WHERE TRXTRN = '3087' -- 展期的交易代號
        GROUP BY TRXDAT
               , LMSACN
               , LMSAPN
               , LMSASQ
               , TRXNMT
    )  T2 ON T2.TRXDAT = T1.TRXDAT
         AND T2.LMSACN = T1.LMSACN
         AND T2.TRXNMT = T1.TRXNMT
   LEFT JOIN (
        SELECT TRXDAT
             , LMSACN
             , TRXNMT
             , SUM(TRXAMT)     AS "CloseTotal"
        FROM LA$TRXP
        WHERE TRXTRN = '3041' -- 結案的交易代號
          AND TRXTCT = 1 -- 結案區分為展期
        GROUP BY TRXDAT
               , LMSACN
               , TRXNMT
    )  T3 ON T3.TRXDAT = T1.TRXDAT
         AND T3.LMSACN = T1.LMSACN
         AND T3.TRXNMT = T1.TRXNMT
   LEFT JOIN LNACNP ACN ON ACN.LMSACN = T1.LMSACN
                       AND ACN.LMSAPN1 = T1."CloseFacmNo"
                       AND ACN.LMSASQ1 = T1."CloseBormNo"
                       AND ACN.LMSAPN = T2."RenewFacmNo"
                       AND ACN.LMSASQ = T2."RenewBormNo"
   LEFT JOIN LA$LMSP LM ON LM.LMSACN = T1.LMSACN
                       AND LM.LMSAPN = T2."RenewFacmNo"
                       AND LM.LMSASQ = T2."RenewBormNo"
   WHERE T2."RenewAmt" > 0
   ORDER BY T1.LMSACN
          , T1.TRXDAT
          , T1."CloseFacmNo"
          , T1."CloseBormNo"
          , T2."RenewFacmNo"
          , T2."RenewBormNo"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_RenewMapping_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
