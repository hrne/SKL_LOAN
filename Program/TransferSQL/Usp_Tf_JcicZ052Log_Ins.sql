--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ052Log_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ052Log_Ins" 
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
    DELETE FROM "JcicZ052Log";

    -- 寫入資料
    INSERT INTO "JcicZ052Log"
    SELECT "Ukey"              AS "Ukey"                 -- 流水號 VARCHAR2 32 0
          ,LPAD('1', 18, '0')  AS "TxSeq"                -- 交易序號 VARCHAR2 18 0
          ,"TranKey"           AS "TranKey"              -- 交易代碼 VARCHAR2 1 0
          ,"BankCode1"         AS "BankCode1"            -- 補報送債權機構代號1 NVARCHAR2 3 0
          ,"DataCode1"         AS "DataCode1"            -- 補報送檔案格式資料別1 NVARCHAR2 2 0
          ,"BankCode2"         AS "BankCode2"            -- 補報送債權機構代號2 NVARCHAR2 3 0
          ,"DataCode2"         AS "DataCode2"            -- 補報送檔案格式資料別2 NVARCHAR2 2 0
          ,"BankCode3"         AS "BankCode3"            -- 補報送債權機構代號3 NVARCHAR2 3 0
          ,"DataCode3"         AS "DataCode3"            -- 補報送檔案格式資料別3 NVARCHAR2 2 0
          ,"BankCode4"         AS "BankCode4"            -- 補報送債權機構代號4 NVARCHAR2 3 0
          ,"DataCode4"         AS "DataCode4"            -- 補報送檔案格式資料別4 NVARCHAR2 2 0
          ,"BankCode5"         AS "BankCode5"            -- 補報送債權機構代號5 NVARCHAR2 3 0
          ,"DataCode5"         AS "DataCode5"            -- 補報送檔案格式資料別5 NVARCHAR2 2 0
          ,"ChangePayDate"     AS "ChangePayDate"        -- 申請變更還款條件日 Decimald 8 0
          ,"OutJcicTxtDate"    AS "OutJcicTxtDate"       -- 轉出JCIC文字檔日期 Decimald 8 0
          ,JOB_START_TIME      AS "CreateDate"           -- 建檔日期時間 DATE 8 0
          ,'999999'            AS "CreateEmpNo"          -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME      AS "LastUpdate"           -- 最後更新日期時間 DATE 8 0
          ,'999999'            AS "LastUpdateEmpNo"      -- 最後更新人員 VARCHAR2 6 0
    FROM "JcicZ052"
    WHERE "OutJcicTxtDate" > 0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ052Log_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
