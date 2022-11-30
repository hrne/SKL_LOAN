--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ061_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ061_Ins" 
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
    DELETE FROM "JcicZ061";

    -- 寫入資料
    INSERT INTO "JcicZ061"
    SELECT "TBJCICZ061"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ061"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ061"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ061"."RC_DATE"         AS "RcDate"              -- 協商申請日 Decimald 8 0
          ,"TBJCICZ061"."CHANGE_PAY_DATE" AS "ChangePayDate"       -- 申請變更還款條件日 Decimald 8 0
          ,"TBJCICZ061"."MAX_MAIN_CODE"   AS "MaxMainCode"         -- 最大債權金融機構代號 VARCHAR2 10 0
          ,"TBJCICZ061"."RES_EXP_AMT"     AS "ExpBalanceAmt"       -- 信用貸款協商剩餘債權餘額 Decimal 9 0
          ,"TBJCICZ061"."RES_CASH_AMT"    AS "CashBalanceAmt"      -- 現金卡協商剩餘債權餘額 Decimal 9 0
          ,"TBJCICZ061"."RES_CREDIT_AMT"  AS "CreditBalanceAmt"    -- 信用卡協商剩餘債權餘額 Decimal 9 0
          ,"TBJCICZ061"."MAX_MAIN_NOTE"   AS "MaxMainNote"         -- 最大債權金融機構報送註記 VARCHAR2 1 0
          ,"TBJCICZ061"."ISGUARANTOR"     AS "IsGuarantor"         -- 是否有保證人 VARCHAR2 1 0
          ,"TBJCICZ061"."ISCHANGEPAYMENT" AS "IsChangePayment"     -- 是否同意債務人申請變更還款條件方案 VARCHAR2 1 0
          ,"TBJCICZ061"."JCICEXPORTDATE"  AS "OutJcicTxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          ,0 AS "ActualFilingDate"
          ,NULL AS "ActualFilingMark"
    FROM "TBJCICZ061"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ061_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
