--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ048_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ048_Ins" 
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
    DELETE FROM "JcicZ048";

    -- 寫入資料
    INSERT INTO "JcicZ048"
    SELECT "TBJCICZ048"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ048"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ048"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ048"."RC_DATE"         AS "RcDate"              -- 協商申請日 Decimald 8 0
          ,"TBJCICZ048"."REG_ADDR"        AS "CustRegAddr"         -- 債務人戶籍之郵遞區號及地址 VARCHAR2 304 0
          ,"TBJCICZ048"."COM_ADDR"        AS "CustComAddr"         -- 債務人通訊地之郵遞區號及地址 VARCHAR2 304 0
          ,"TBJCICZ048"."REG_TELNO"       AS "CustRegTelNo"        -- 債務人戶籍電話 VARCHAR2 16 0
          ,"TBJCICZ048"."COM_TELNO"       AS "CustComTelNo"        -- 債務人通訊電話 VARCHAR2 16 0
          ,"TBJCICZ048"."MOBIL_NO"        AS "CustMobilNo"         -- 債務人行動電話 VARCHAR2 16 0
          ,"TBJCICZ048"."JCICEXPORTDATE"  AS "OutJcicTxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID                       AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TBJCICZ048"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ048_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
