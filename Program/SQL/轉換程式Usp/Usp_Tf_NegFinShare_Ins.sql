--------------------------------------------------------
--  DDL for Procedure Usp_Tf_NegFinShare_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_NegFinShare_Ins" 
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
    DELETE FROM "NegFinShare";

    -- 寫入資料
    INSERT INTO "NegFinShare"
    SELECT "NegMain"."CustNo"                 AS "CustNo"              -- 債務人戶號 DECIMAL 7 0
          ,"NegMain"."CaseSeq"                AS "CaseSeq"             -- 案件序號 DECIMAL 3 0
          ,NVL("tbJCICShare".CREDIT_CODE,' ') AS "FinCode"             -- 債權機構 VARCHAR2 8 0
          ,NVL("tbJCICShare".RC_AMT,0)        AS "ContractAmt"         -- 簽約金額 DECIMAL 16 2
          ,NVL("tbJCICShare".CREDIT_RATE,0)   AS "AmtRatio"            -- 債權比例%  DECIMAL 5 2
          ,NVL("tbJCICShare".PERIOD_AMT,0)    AS "DueAmt"              -- 期款 DECIMAL 16 2
          ,NVL("tbJCICShare".DEL_DATE,0)      AS "CancelDate"          -- 註銷日期 DecimalD 8 0
          ,"tbJCICShare".DEL_AMT              AS "CancelAmt"           -- 註銷本金 DECIMAL 16 2
          ,JOB_START_TIME                     AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                           AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                     AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                           AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "tbJCICShare"
    LEFT JOIN "tbJCICMain" ON "tbJCICMain".CustIDN = "tbJCICShare".CustIDN
                          AND "tbJCICMain".RC_DATE = "tbJCICShare".RC_DATE
    LEFT JOIN "NegMain" ON "NegMain"."CustNo"   = "tbJCICMain".RC_ACCOUNT
                       AND "NegMain"."ApplDate" = "tbJCICMain".RC_DATE
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_NegFinShare_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
