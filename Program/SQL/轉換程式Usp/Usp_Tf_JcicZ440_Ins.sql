--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ440_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ440_Ins" 
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
    DELETE FROM "JcicZ440";

    -- 寫入資料
    INSERT INTO "JcicZ440"
    SELECT "TBJCICZ440"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ440"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ440"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ440"."APPLYDATE"       AS "ApplyDate"           -- 款項統一收付申請日 Decimald 8 0
          ,"TBJCICZ440"."BANK_ID"         AS "BankId"              -- 異動債權金機構代號 VARCHAR2 3 0
          ,"TBJCICZ440"."AGREEDATE"       AS "AgreeDate"           -- 同意書取得日期 Decimald 8 0
          ,"TBJCICZ440"."STARTDATE"       AS "StartDate"           -- 首次調解日 Decimald 8 0
          ,"TBJCICZ440"."REMINDATE"       AS "RemindDate"          -- 債權計算基準日 Decimald 8 0
          ,"TBJCICZ440"."APPLYTYPE"       AS "ApplyType"           -- 受理方式 VARCHAR2 1 0
          ,"TBJCICZ440"."REPORTYN"        AS "ReportYn"            -- 協辦行是否需自行回報債權 VARCHAR2 1 0
          ,"TBJCICZ440"."NOTBANKID1"      AS "NotBankId1"          -- 未揭露債權機構代號1 VARCHAR2 3 0
          ,"TBJCICZ440"."NOTBANKID2"      AS "NotBankId2"          -- 未揭露債權機構代號2 VARCHAR2 3 0
          ,"TBJCICZ440"."NOTBANKID3"      AS "NotBankId3"          -- 未揭露債權機構代號3 VARCHAR2 3 0
          ,"TBJCICZ440"."NOTBANKID4"      AS "NotBankId4"          -- 未揭露債權機構代號4 VARCHAR2 3 0
          ,"TBJCICZ440"."NOTBANKID5"      AS "NotBankId5"          -- 未揭露債權機構代號5 VARCHAR2 3 0
          ,"TBJCICZ440"."NOTBANKID6"      AS "NotBankId6"          -- 未揭露債權機構代號6 VARCHAR2 3 0
          ,"TBJCICZ440"."JCICEXPORTDATE"  AS "OutJcicTxtDate"      -- 轉JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,"TBJCICZ440".LASTUPDATEDATE    AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,"TBJCICZ440".MODIFYUSERID      AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,"TBJCICZ440".LASTUPDATEDATE    AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,"TBJCICZ440".MODIFYUSERID      AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          ,0 AS "ActualFilingDate"
          ,NULL AS "ActualFilingMark"
    FROM "TBJCICZ440"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ440_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
