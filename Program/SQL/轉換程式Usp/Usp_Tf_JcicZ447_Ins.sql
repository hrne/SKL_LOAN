--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ447_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ447_Ins" 
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
    DELETE FROM "JcicZ447";

    -- 寫入資料
    INSERT INTO "JcicZ447"
    SELECT "TBJCICZ447"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ447"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ447"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ447"."APPLYDATE"       AS "ApplyDate"           -- 款項統一收付申請日 Decimald 8 0
          ,"TBJCICZ447"."BANK_ID"         AS "BankId"              -- 異動債權金機構代號 VARCHAR2 3 0
          ,"TBJCICZ447"."CIVIL323_AMT"    AS "Civil323Amt"         -- 依民法第323條計算之債務總金額 Decimal 10 0
          ,"TBJCICZ447"."TOTAL_AMT"       AS "TotalAmt"            -- 簽約總債務金額 Decimal 10 0
          ,"TBJCICZ447"."SIGN_DATE"       AS "SignDate"            -- 簽約完成日期 Decimald 8 0
          ,"TBJCICZ447"."FIRST_PAY_DATE"  AS "FirstPayDate"        -- 首期應繳款日 Decimald 8 0
          ,"TBJCICZ447"."PERIOD"          AS "Period"              -- 期數 Decimal 3 0
          ,"TBJCICZ447"."RATE"            AS "Rate"                -- 利率 Decimal 5 2
          ,"TBJCICZ447"."PAY_AMOUNT"      AS "MonthPayAmt"         -- 月付金 Decimal 10 0
          ,"TBJCICZ447"."PAY_ACCOUNT"     AS "PayAccount"          -- 繳款帳號 VARCHAR2 20 0
          ,"TBJCICZ447"."JCICEXPORTDATE"  AS "OutJcicTxtDate"      -- 轉JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TBJCICZ447"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ447_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
