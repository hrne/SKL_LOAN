--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ040_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ040_Ins" 
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
    DELETE FROM "JcicZ040";

    -- 寫入資料
    INSERT INTO "JcicZ040"
    SELECT "TBJCICZ040"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ040"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 NVARCHAR2 10 0
          ,"TBJCICZ040"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ040"."RC_DATE"         AS "RcDate"              -- 協商申請日 Decimal 8 0
          ,"TBJCICZ040"."RB_DATE"         AS "RbDate"              -- 止息基準日 Decimald 8 0
          ,"TBJCICZ040"."APPLYTYPE"       AS "ApplyType"           -- 受理方式 VARCHAR2 1 0
          ,"TBJCICZ040"."REFBANKID"       AS "RefBankId"           -- 轉借金融機構代號 NVARCHAR2 3 0
          ,"TBJCICZ040"."NOTBANKID1"      AS "NotBankId1"          -- 未揭露債權機構代號1 NVARCHAR2 3 0
          ,"TBJCICZ040"."NOTBANKID2"      AS "NotBankId2"          -- 未揭露債權機構代號2 NVARCHAR2 3 0
          ,"TBJCICZ040"."NOTBANKID3"      AS "NotBankId3"          -- 未揭露債權機構代號3 NVARCHAR2 3 0
          ,NULL                           AS "NotBankId4"          -- 未揭露債權機構代號4 NVARCHAR2 3 0
          ,NULL                           AS "NotBankId5"          -- 未揭露債權機構代號5 NVARCHAR2 3 0
          ,NULL                           AS "NotBankId6"          -- 未揭露債權機構代號6 NVARCHAR2 3 0
          ,"TBJCICZ040"."JCICEXPORTDATE"  AS "OutJcictxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TBJCICZ040"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ040_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
