--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ049_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ049_Ins" 
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
    DELETE FROM "JcicZ049";

    -- 寫入資料
    INSERT INTO "JcicZ049"
    SELECT Z049."TRANSACTIONSID"          AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,Z049."SUBMITID"                AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,Z049."CUSTIDN"                 AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,Z049."RC_DATE"                 AS "RcDate"              -- 協商申請日 Decimald 8 0
          ,Z049."CLAIM_STATUS"            AS "ClaimStatus"         -- 案件進度 VARCHAR2 1 0
          ,Z049."APPLY_DATE"              AS "ApplyDate"           -- 遞狀日 Decimald 8 0
          ,Z049."COURT_CODE"              AS "CourtCode"           -- 承審法院代碼 VARCHAR2 3 0
          ,NVL(Z049."YEAR",0)             AS "Year"                -- 年度別 VARCHAR2 4 0
          ,Z049."COURT_DIV"               AS "CourtDiv"            -- 法院承審股別 VARCHAR2 10 0
          ,Z049."COURT_CASENO"            AS "CourtCaseNo"         -- 法院案號 VARCHAR2 30 0
          ,Z049."APPROVE"                 AS "Approve"             -- 法院認可與否 VARCHAR2 1 0
          ,NVL(Z049."CLAIM_DATE",0)       AS "ClaimDate"           -- 法院裁定日期 Decimald 8 0
          ,Z049."JCICEXPORTDATE"          AS "OutJcicTxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID                       AS "Ukey"                -- 流水號 VARCHAR2 32
          ,Z049.LASTUPDATEDATE            AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,Z049.MODIFYUSERID              AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,Z049.LASTUPDATEDATE            AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,Z049.MODIFYUSERID              AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          ,0 AS "ActualFilingDate"
          ,NULL AS "ActualFilingMark"
    FROM "TBJCICZ049" Z049
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ049_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
