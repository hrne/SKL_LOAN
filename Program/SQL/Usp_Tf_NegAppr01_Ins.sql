--------------------------------------------------------
--  DDL for Procedure Usp_Tf_NegAppr01_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_NegAppr01_Ins" 
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
    DELETE FROM "NegAppr01";

    -- 寫入資料
    INSERT INTO "NegAppr01" (
        "AcDate"              -- 會計日期 DecimalD 8 0
      , "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
      , "TitaTxtNo"           -- 交易序號 DECIMAL 8 0
      , "FinCode"             -- 債權機構代號 VARCHAR2 10 0
      , "CustNo"              -- 戶號 DECIMAL 7 0
      , "CaseSeq"             -- 案件序號 DECIMAL 3 0
      , "CaseKindCode"        -- 案件種類 VARCHAR2 1 0
      , "ApprAmt"             -- 撥付金額 DECIMAL 16 2
      , "AccuApprAmt"         -- 累計撥付金額 DECIMAL 16 2
      , "AmtRatio"            -- 撥付比例 DECIMAL 5 2
      , "ExportDate"          -- 製檔日期 DecimalD 8 0
      , "ApprDate"            -- 撥付日期 DecimalD 8 0
      , "BringUpDate"         -- 提兌日 DecimalD 8 0
      , "RemitBank"           -- 匯款銀行 VARCHAR2 7 0
      , "RemitAcct"           -- 匯款帳號 VARCHAR2 16 0
      , "DataSendUnit"        -- 資料傳送單位 VARCHAR2 8 0
      , "ApprAcDate"          -- 撥付傳票日 DecimalD 8 0
      , "ReplyCode"           -- 回應代碼 VARCHAR2 4 0
      , "BatchTxtNo"           -- Batch交易序號 VARCHAR2 10 0
      , "CreateDate"          -- 建檔日期時間 DATE 8 0
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
      , "LastUpdate"          -- 最後更新日期時間 DATE 8 0
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    )
    SELECT T."AcDate"                     AS "AcDate"              -- 會計日期 DecimalD 8 0
          ,T."TitaTlrNo"                  AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
          ,T."TitaTxtNo"                  AS "TitaTxtNo"           -- 交易序號 DECIMAL 8 0
          ,NVL(AMTSHARE.CREDIT_CODE,' ')
                                          AS "FinCode"             -- 債權機構代號 VARCHAR2 10 0
          ,M."CustNo"                     AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,M."CaseSeq"                    AS "CaseSeq"             -- 案件序號 DECIMAL 3 0
          ,M."CaseKindCode"               AS "CaseKindCode"        -- 案件種類 VARCHAR2 1 0
          ,NVL(AMTSHARE.SHARE_AMT,0)      AS "ApprAmt"             -- 撥付金額 DECIMAL 16 2
           -- 累計撥付金額在下段程式加總後更新
          ,0                              AS "AccuApprAmt"         -- 累計撥付金額 DECIMAL 16 2
          ,NVL(FS."AmtRatio",0)           AS "AmtRatio"            -- 撥付比例 DECIMAL 5 2
          ,NVL(AMTSHARE.ExportDate,0)     AS "ExportDate"          -- 製檔日期 DecimalD 8 0
          ,NVL(APPR.APPR_DATE,0)          AS "ApprDate"            -- 撥付日期 DecimalD 8 0
          ,NVL(APPR.BRINGUP_DATE,0)       AS "BringUpDate"         -- 提兌日 DecimalD 8 0
          ,APPR.REMIT_BANK                AS "RemitBank"           -- 匯款銀行 VARCHAR2 7 0
          ,APPR.REMIT_ACCOUNT             AS "RemitAcct"           -- 匯款帳號 VARCHAR2 16 0
          ,APPR.DATA_SEND_UNIT            AS "DataSendUnit"        -- 資料傳送單位 VARCHAR2 8 0
          ,NVL(APPR.ACCOUNT_DATE,0)       AS "ApprAcDate"          -- 撥付傳票日 DecimalD 8 0
          ,APPR.REPLY_CODE                AS "ReplyCode"           -- 回應代碼 VARCHAR2 4 0
          ,NULL                           AS "BatchTxtNo"           -- Batch交易序號 VARCHAR2 10 0
          ,NVL(APPR.LASTUPDATEDATE,JOB_START_TIME)
                                          AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,NVL(APPR.MODIFYUSERID,'999999')
                                          AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,NVL(APPR.LASTUPDATEDATE,JOB_START_TIME)
                                          AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,NVL(APPR.MODIFYUSERID,'999999')
                                          AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "NegTranNoMapping" TNM
    LEFT JOIN "NegTrans" T ON T."AcDate"    = TNM."AcDate"
                          AND T."TitaTlrNo" = TNM."TitaTlrNo"
                          AND T."TitaTxtNo" = TNM."TitaTxtNo"
    LEFT JOIN "NegMain" M ON M."CustNo"  = T."CustNo"
                         AND M."CaseSeq" = T."CaseSeq"
    LEFT JOIN REMIN_TBJCICAPPR APPR ON APPR.CustIDN      = TNM."CustIDN"
                                   AND APPR.RC_DATE      = TNM."RC_DATE"
    LEFT JOIN REMIN_TBJCICAMTSHARE AMTSHARE ON AMTSHARE.CustIDN       = TNM."CustIDN"
                                           AND AMTSHARE.RC_DATE       = TNM."RC_DATE"
                                           AND AMTSHARE.ACCOUNT_DATE  = TNM."ACCOUNT_DATE"
                                           AND AMTSHARE.BUSINESS_CODE = TNM."BUSINESS_CODE"
                                           AND AMTSHARE.BRINGUP_DATE = APPR.BRINGUP_DAT
                                           AND AMTSHARE.CREDIT_CODE = APPR.CREDIT_CODE
    LEFT JOIN "NegFinShare" FS ON FS."CustNo"  = T."CustNo"
                              AND FS."CaseSeq" = T."CaseSeq"
                              AND FS."FinCode" = AMTSHARE.CREDIT_CODE
    WHERE M."CustNo" IS NOT NULL
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_NegAppr01_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
