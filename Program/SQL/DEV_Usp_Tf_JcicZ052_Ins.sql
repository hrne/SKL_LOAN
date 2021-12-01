--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ052_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ052_Ins" 
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
    DELETE FROM "JcicZ052";

    -- 寫入資料
    INSERT INTO "JcicZ052"
    SELECT "TBJCICZ052"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ052"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ052"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ052"."RC_DATE"         AS "RcDate"              -- 協商申請日 Decimald 8 0
          ,"TBJCICZ052"."BANKCODE1"       AS "BankCode1"           -- 同意報送債權機構代號1 VARCHAR2 10 0
          ,"TBJCICZ052"."DATACODE1"       AS "DataCode1"           -- 同意報送檔案格式資料別1 VARCHAR2 2 0
          ,"TBJCICZ052"."BANKCODE2"       AS "BankCode2"           -- 同意報送債權機構代號2 VARCHAR2 10 0
          ,"TBJCICZ052"."DATACODE2"       AS "DataCode2"           -- 同意報送檔案格式資料別2 VARCHAR2 2 0
          ,"TBJCICZ052"."BANKCODE3"       AS "BankCode3"           -- 同意報送債權機構代號3 VARCHAR2 10 0
          ,"TBJCICZ052"."DATACODE3"       AS "DataCode3"           -- 同意報送檔案格式資料別3 VARCHAR2 2 0
          ,"TBJCICZ052"."BANKCODE4"       AS "BankCode4"           -- 同意報送債權機構代號4 VARCHAR2 10 0
          ,"TBJCICZ052"."DATACODE4"       AS "DataCode4"           -- 同意報送檔案格式資料別4 VARCHAR2 2 0
          ,"TBJCICZ052"."BANKCODE5"       AS "BankCode5"           -- 同意報送債權機構代號5 VARCHAR2 10 0
          ,"TBJCICZ052"."DATACODE5"       AS "DataCode5"           -- 同意報送檔案格式資料別5 VARCHAR2 2 0
          ,NVL("TBJCICZ052"."CHANGE_PAY_DATE",0) AS "ChangePayDate"       -- 申請變更還款條件日 Decimald 8 0
          ,NVL("TBJCICZ052"."JCICEXPORTDATE",0)  AS "OutJcicTxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TBJCICZ052"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ052_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
