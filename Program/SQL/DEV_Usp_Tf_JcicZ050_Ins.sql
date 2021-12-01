--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ050_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ050_Ins" 
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
    DELETE FROM "JcicZ050";

    -- 寫入資料
    INSERT INTO "JcicZ050"
    SELECT Z050."TRANSACTIONSID"          AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,Z050."SUBMITID"                AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,Z050."CUSTIDN"                 AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,NVL(Z050."RC_DATE",0)          AS "RcDate"              -- 協商申請日 Decimald 8 0
          ,NVL(Z050."PAY_DATE",0)         AS "PayDate"             -- 繳款日期 Decimald 8 0
          ,NVL(Z050."PAY_AMT",0)          AS "PayAmt"              -- 本次繳款金額 Decimal 16 0
          ,NVL(Z050."PAYAMT_1",0)         AS "SumRepayActualAmt"   -- 累計實際還款金額 Decimal 16 0
          ,NVL(Z050."PAYAMT_2",0)         AS "SumRepayShouldAmt"   -- 累計應還款金額 Decimal 16 0
          ,Z050."PAY_STATUS"              AS "Status"              -- 債權結案註記 VARCHAR2 1 0
          ,NVL(Z050."JCICEXPORTDATE",0)   AS "OutJcicTxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,NVL(Z050."SECOND_REPAY_YM",0)  AS "SecondRepayYM"       -- 進入第二階梯還款年月 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TBJCICZ050" Z050
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ050_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
