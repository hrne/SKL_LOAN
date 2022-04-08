--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ572_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ572_Ins" 
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
    DELETE FROM "JcicZ572";

    -- 寫入資料
    INSERT INTO "JcicZ572"
    SELECT "TBJCICZ066"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ066"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 NVARCHAR2 3 0
          ,"TBJCICZ066"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ066"."APPLYDATE"       AS "ApplyDate"           -- 更生款項統一收付申請日 Decimald 8 0
          ,"TBJCICZ066"."STARTDATE"       AS "StartDate"           -- 生效日期 Decimald 8 0
          ,"TBJCICZ066"."PAYDATE"         AS "PayDate"             -- 本分配表首繳日 Decimald 8 0
          ,"TBJCICZ066"."BANK_ID"         AS "BankId"              -- 債權金融機構代號 NVARCHAR2 3 0
          ,"TBJCICZ066"."ALLOTAMT"        AS "AllotAmt"            -- 參與分配債權金額 Decimal 9 0
          ,"TBJCICZ066"."OWNPERCENTAGE"   AS "OwnPercentage"       -- 債權比例 Decimal 6 2
          ,"TBJCICZ066"."JCICEXPORTDATE"  AS "OutJcicTxtDate"      -- 轉JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TBJCICZ066"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ572_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
