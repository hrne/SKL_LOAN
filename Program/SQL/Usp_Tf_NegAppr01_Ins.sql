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
        "LogNo"
      , "AcDate"              -- 會計日期 DecimalD 8 0
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
    WITH AMTSHARE AS (
      SELECT CustIDN
           , RC_DATE
           , CREDIT_CODE
           , BRINGUP_DATE
           , EXPORTDATE
           , SHARE_AMT
           , ACCOUNT_DATE
           , BUSINESS_CODE
           , ROW_NUMBER()
             OVER (
              PARTITION BY CustIDN
                         , RC_DATE
                         , CREDIT_CODE
                         , BRINGUP_DATE
                         , EXPORTDATE
                         , SHARE_AMT
              ORDER BY ACCOUNT_DATE DESC
                     , BUSINESS_CODE DESC
             ) AS "DataSeq"
      FROM REMIN_TBJCICAMTSHARE
    )
    SELECT ROW_NUMBER()
           OVER (
            ORDER BY APPR.LASTUPDATEDATE
           )                              AS "LogNo"
          ,NVL(TNM."AcDate",0)            AS "AcDate"              -- 會計日期 DecimalD 8 0
          ,NVL(TNM."TitaTlrNo",0)         AS "TitaTlrNo"           -- 經辦 VARCHAR2 6 0
          ,NVL(TNM."TitaTxtNo",0)         AS "TitaTxtNo"           -- 交易序號 DECIMAL 8 0
          ,APPR.CREDIT_CODE               AS "FinCode"             -- 債權機構代號 VARCHAR2 10 0
          ,JM.RC_ACCOUNT                  AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,M."CaseSeq"                    AS "CaseSeq"             -- 案件序號 DECIMAL 3 0
          ,M."CaseKindCode"               AS "CaseKindCode"        -- 案件種類 VARCHAR2 1 0
          ,APPR.APPR_AMT                  AS "ApprAmt"             -- 撥付金額 DECIMAL 16 2
           -- 累計撥付金額在下段程式加總後更新
          ,0                              AS "AccuApprAmt"         -- 累計撥付金額 DECIMAL 16 2
          ,NVL(S.CREDIT_RATE,0)           AS "AmtRatio"            -- 撥付比例 DECIMAL 5 2
          ,NVL(AMTSHARE.EXPORTDATE,0)     AS "ExportDate"          -- 製檔日期 DecimalD 8 0
          ,APPR.APPR_DATE                 AS "ApprDate"            -- 撥付日期 DecimalD 8 0
          ,APPR.BRINGUP_DATE              AS "BringUpDate"         -- 提兌日 DecimalD 8 0
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
    FROM REMIN_TBJCICAPPR APPR
    -- 2023-10-12 Wei from SKL IT 盈倩 APPR 串 AMTSHARE 時 , 成功的 APPR 才會串到 AMTSHARE
    LEFT JOIN AMTSHARE ON AMTSHARE.CUSTIDN = APPR.CUSTIDN
                      AND AMTSHARE.RC_DATE = APPR.RC_DATE
                      AND AMTSHARE.CREDIT_CODE = APPR.CREDIT_CODE
                      AND AMTSHARE.BRINGUP_DATE = APPR.BRINGUP_DATE
                      AND AMTSHARE.EXPORTDATE = APPR.APPR_DATE
                      AND AMTSHARE.SHARE_AMT = APPR.APPR_AMT
                      AND AMTSHARE."DataSeq" = 1
    -- 2023-10-12 Wei 從新舊交易序號對照檔找出新的交易序號
    LEFT JOIN "NegTranNoMapping" TNM ON TNM."CustIDN" = AMTSHARE.CUSTIDN
                                    AND TNM.RC_DATE = AMTSHARE.RC_DATE
                                    AND TNM.ACCOUNT_DATE = AMTSHARE.ACCOUNT_DATE
                                    AND TNM.BUSINESS_CODE = AMTSHARE.BUSINESS_CODE
                                    AND NVL(AMTSHARE."DataSeq",0) = 1 -- 有串到AMTSHARE的才串
    LEFT JOIN REMIN_TBJCICMAIN JM ON JM.CustIDN = APPR.CustIDN
                                 AND JM.RC_DATE = APPR.RC_DATE
    LEFT JOIN "NegMain" M ON M."CustNo" = JM.RC_ACCOUNT
                         AND M."ApplDate" = JM.RC_DATE
    LEFT JOIN REMIN_TBJCICAMTSHARE S ON S.CUSTIDN = APPR.CUSTIDN
                                    AND S.RC_DATE = APPR.RC_DATE
                                    AND S.CREDIT_CODE = APPR.CREDIT_CODE
    WHERE APPR.BRINGUP_DATE != 19110000
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
