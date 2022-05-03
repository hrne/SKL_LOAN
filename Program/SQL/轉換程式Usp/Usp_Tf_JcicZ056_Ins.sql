--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ056_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ056_Ins" 
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
    DELETE FROM "JcicZ056";

    -- 寫入資料
    INSERT INTO "JcicZ056"
    SELECT Z056."TRANSACTIONSID"          AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,Z056."CUSTIDN"                 AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,Z056."SUBMITID"                AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 
          ,Z056."CASE_STATUS"             AS "CaseStatus"          -- 案件狀態 VARCHAR2 1 0
          ,Z056."CLAIM_DATE"              AS "ClaimDate"           -- 裁定日期 Decimald 8 0
          ,Z056."COURT_CODE"              AS "CourtCode"           -- 承審法院代碼 VARCHAR2 3 0
          ,Z056."YEAR"                    AS "Year"                -- 年度別 decimal 4 0
          ,Z056."COURT_DIV"               AS "CourtDiv"            -- 法院承審股別 NVARCHAR2 16 0
          ,Z056."COURT_CASENO"            AS "CourtCaseNo"         -- 法院案號 NVARCHAR2 160 0
          ,Z056."APPROVE"                 AS "Approve"             -- 法院裁定免責確定 VARCHAR2 1 0
          ,NVL(Z056."MATURITYAMT",0)      AS "OutstandAmt"         -- 原始債權金額 Decimal 9 0
          ,NVL(Z056."SUB_AMT",0)          AS "SubAmt"              -- 清算損失金額 Decimal 9 0
          ,Z056."CLAIM_STATUS1"           AS "ClaimStatus1"        -- 法院裁定保全處分 VARCHAR2 1 0
          ,NVL(Z056."SAVE_DATE",0)        AS "SaveDate"            -- 保全處分起始日 Decimald 8 0
          ,Z056."CLAIM_STATUS2"           AS "ClaimStatus2"        -- 法院裁定撤銷保全處分 VARCHAR2 1 0
          ,NVL(Z056."SAVE_END_DATE",0)    AS "SaveEndDate"         -- 保全處分撤銷日 Decimald 8 0
          ,Z056."ADMINNAME"               AS "AdminName"           -- 管理人姓名 NVARCHAR2 40 0
          ,Z056."JCICEXPORTDATE"          AS "OutJcicTxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TBJCICZ056" Z056
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ056_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
