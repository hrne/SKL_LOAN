--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ055_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ055_Ins" 
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
    DELETE FROM "JcicZ055";

    -- 寫入資料
    INSERT INTO "JcicZ055"
    SELECT S0."TRANSACTIONSID"            AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,S0."CUSTIDN"                   AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,S0."SUBMITID"                  AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,S0."CASE_STATUS"               AS "CaseStatus"          -- 案件狀態 VARCHAR2 1 0
          ,S0."CLAIM_DATE"                AS "ClaimDate"           -- 裁定日期 Decimald 8 0
          ,S0."COURT_CODE"                AS "CourtCode"           -- 承審法院代碼 VARCHAR2 12 0
          ,S0."YEAR"                      AS "Year"                -- 年度別 VARCHAR2 4 0
          ,S0."COURT_DIV"                 AS "CourtDiv"            -- 法院承審股別 NVARCHAR2 16 0
          ,S0."COURT_CASENO"              AS "CourtCaseNo"         -- 法院案號 NVARCHAR2 160 0
          ,NVL(S0."PAY_DATE",0)           AS "PayDate"             -- 更生方案首期應繳款日 Decimald 8 0
          ,NVL(S0."PAY_END_DATE",0)       AS "PayEndDate"          -- 更生方案末期應繳款日 Decimald 8 0
          ,NVL(S0."PERIOD",0)             AS "Period"              -- 更生條件(期數) Decimal 5 0
          ,NVL(S0."RATE",0)               AS "Rate"                -- 更生條件(利率) Decimal 9 2
          ,NVL(S0."MATURITYAMT",0)        AS "OutstandAmt"         -- 原始債權金額 Decimal 9 0
          ,NVL(S0."SUB_AMT",0)            AS "SubAmt"              -- 更生損失金額 Decimal 9 0
          ,S0."CLAIM_STATUS1"             AS "ClaimStatus1"        -- 法院裁定保全處分 VARCHAR2 1 0
          ,NVL(S0."SAVE_DATE",0)          AS "SaveDate"            -- 保全處分起始日 Decimald 8 0
          ,S0."CLAIM_STATUS2"             AS "ClaimStatus2"        -- 法院裁定撤銷保全處分 VARCHAR2 1 0
          ,NVL(S0."SAVE_END_DATE",0)      AS "SaveEndDate"         -- 保全處分撤銷日 Decimald 8 0
          ,S0."ISIMPLEMENT"               AS "IsImplement"         -- 是否依更生條件履行 VARCHAR2 1 0
          ,S0."INSPECTNAME"               AS "InspectName"         -- 監督人姓名 NVARCHAR2 40 0
          ,NVL(S0."JCICEXPORTDATE",0)     AS "OutJcicTxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TBJCICZ055" S0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ055_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
