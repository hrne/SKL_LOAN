--------------------------------------------------------
--  DDL for Procedure Usp_Tf_NegAppr01_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_NegAppr01_Ins" 
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
    INSERT INTO "NegAppr01"
    SELECT "NegTrans"."AcDate"            AS "AcDate"              -- 會計日期 DecimalD 8 0
          ,"NegTrans"."TitaTlrNo"         AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
          ,"NegTrans"."TitaTxtNo"         AS "TitaTxtNo"           -- 交易序號 DECIMAL 8 0
          ,NVL("tbJCICAmtShare".CREDIT_CODE,' ')
                                          AS "FinCode"             -- 債權機構代號 VARCHAR2 10 0
          ,"NegMain"."CustNo"             AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,"NegMain"."CaseSeq"            AS "CaseSeq"             -- 案件序號 DECIMAL 3 0
          ,"NegMain"."CaseKindCode"       AS "CaseKindCode"        -- 案件種類 VARCHAR2 1 0
          ,NVL("tbJCICAmtShare".SHARE_AMT,0)
                                          AS "ApprAmt"             -- 撥付金額 DECIMAL 16 2
           -- 累計撥付金額在下段程式加總後更新
          ,0                              AS "AccuApprAmt"         -- 累計撥付金額 DECIMAL 16 2
          ,NVL("NegFinShare"."AmtRatio",0)
                                          AS "AmtRatio"            -- 撥付比例 DECIMAL 5 2
          ,NVL("tbJCICAmtShare".ExportDate,0)
                                          AS "ExportDate"          -- 製檔日期 DecimalD 8 0
          ,NVL("tbJCICAppr".APPR_DATE,0)  AS "ApprDate"            -- 撥付日期 DecimalD 8 0
          ,NVL("tbJCICAppr".BRINGUP_DATE,0)
                                          AS "BringUpDate"         -- 提兌日 DecimalD 8 0
          ,"tbJCICAppr".REMIT_BANK        AS "RemitBank"           -- 匯款銀行 VARCHAR2 7 0
          ,"tbJCICAppr".REMIT_ACCOUNT     AS "RemitAcct"           -- 匯款帳號 VARCHAR2 16 0
          ,"tbJCICAppr".DATA_SEND_UNIT    AS "DataSendUnit"        -- 資料傳送單位 VARCHAR2 8 0
          ,NVL("tbJCICAppr".ACCOUNT_DATE,0)
                                          AS "ApprAcDate"          -- 撥付傳票日 DecimalD 8 0
          ,"tbJCICAppr".REPLY_CODE        AS "ReplyCode"           -- 回應代碼 VARCHAR2 4 0
          ,NULL                           AS "BatchTxtNo"           -- Batch交易序號 VARCHAR2 10 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "NegTranNoMapping"
    LEFT JOIN "NegTrans" ON "NegTrans"."AcDate"    = "NegTranNoMapping"."AcDate"
                        AND "NegTrans"."TitaTlrNo" = "NegTranNoMapping"."TitaTlrNo"
                        AND "NegTrans"."TitaTxtNo" = "NegTranNoMapping"."TitaTxtNo"
    LEFT JOIN "NegMain" ON "NegMain"."CustNo"  = "NegTrans"."CustNo"
                       AND "NegMain"."CaseSeq" = "NegTrans"."CaseSeq"
    LEFT JOIN "tbJCICAmtShare" ON "tbJCICAmtShare".CustIDN       = "NegTranNoMapping"."CustIDN"
                              AND "tbJCICAmtShare".RC_DATE       = "NegTranNoMapping"."RC_DATE"
                              AND "tbJCICAmtShare".ACCOUNT_DATE  = "NegTranNoMapping"."ACCOUNT_DATE"
                              AND "tbJCICAmtShare".BUSINESS_CODE = "NegTranNoMapping"."BUSINESS_CODE"
    LEFT JOIN "NegFinShare" ON "NegFinShare"."CustNo"  = "NegTrans"."CustNo"
                           AND "NegFinShare"."CaseSeq" = "NegTrans"."CaseSeq"
                           AND "NegFinShare"."FinCode" = "tbJCICAmtShare".CREDIT_CODE
    LEFT JOIN "tbJCICAppr" ON "tbJCICAppr".CustIDN      = "NegTranNoMapping"."CustIDN"
                          AND "tbJCICAppr".RC_DATE      = "NegTranNoMapping"."RC_DATE"
                          AND "tbJCICAppr".BRINGUP_DATE = "tbJCICAmtShare".BRINGUP_DATE
                          AND "tbJCICAppr".CREDIT_CODE  = "tbJCICAmtShare".CREDIT_CODE
    WHERE "NegMain"."CustNo" IS NOT NULL
      AND NVL("tbJCICAmtShare".CREDIT_CODE,'458') != '458' -- 排除 CREDIT_CODE 串不到及458的資料
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
