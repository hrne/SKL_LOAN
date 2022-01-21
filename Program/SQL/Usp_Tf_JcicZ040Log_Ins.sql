--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ040Log_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ040Log_Ins" 
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
    DELETE FROM "JcicZ040Log";

    -- 寫入資料
    INSERT INTO "JcicZ040Log"
    SELECT "Ukey"                -- 流水號 VARCHAR2 32
          ,1          AS "TxSeq" -- 交易序號 VARCHAR2 18
          ,"TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"RbDate"              -- 止息基準日 Decimald 8 0
          ,"ApplyType"           -- 受理方式 VARCHAR2 1 0
          ,"RefBankId"           -- 轉借金融機構代號 NVARCHAR2 3 0
          ,"NotBankId1"          -- 未揭露債權機構代號1 NVARCHAR2 3 0
          ,"NotBankId2"          -- 未揭露債權機構代號2 NVARCHAR2 3 0
          ,"NotBankId3"          -- 未揭露債權機構代號3 NVARCHAR2 3 0
          ,"NotBankId4"          -- 未揭露債權機構代號4 NVARCHAR2 3 0
          ,"NotBankId5"          -- 未揭露債權機構代號5 NVARCHAR2 3 0
          ,"NotBankId6"          -- 未揭露債權機構代號6 NVARCHAR2 3 0
          ,"OutJcictxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,"CreateDate"          -- 建檔日期時間 DATE 8 0
          ,"CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,"LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,"LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "JcicZ040"
    WHERE "OutJcictxtDate" > 0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ040Log_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
