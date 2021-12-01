--------------------------------------------------------
--  DDL for Procedure Usp_Tf_NegFinAcct_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_NegFinAcct_Ins" 
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
    DELETE FROM "NegFinAcct";

    -- 寫入資料
    INSERT INTO "NegFinAcct"
    SELECT JCIC.CREDIT_CODE               AS "FinCode"          -- 債權機構代號 VARCHAR2 8 0
          ,JCIC.CREDIT_NAME               AS "FinItem"          -- 債權機構名稱 NVARCHAR2 60 0
          ,JCIC.REMIT_BANK                AS "RemitBank"         -- 匯款銀行 VARCHAR2 7 0
          ,JCIC.REMIT_ACCOUNT             AS "RemitAcct"      -- 匯款帳號 VARCHAR2 16 0
          ,JCIC.DATA_SEND_UNIT            AS "DataSendSection" -- 資料傳送單位 VARCHAR2 8 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "tbJCICAccountData" JCIC
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_NegFinAcct_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
