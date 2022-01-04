--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ062_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ062_Ins" 
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
    DELETE FROM "JcicZ062";

    -- 寫入資料
    INSERT INTO "JcicZ062"
    SELECT "TBJCICZ062"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ062"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ062"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ062"."RC_DATE"         AS "RcDate"              -- 協商申請日 Decimald 8 0
          ,"TBJCICZ062"."CHANGE_PAY_DATE" AS "ChangePayDate"       -- 申請變更還款條件日 Decimald 8 0
          ,"TBJCICZ062"."COMPLIANCE_PERIOD" AS "CompletePeriod"    -- 變更還款條件已履約期數 Decimal 3 0
          ,"TBJCICZ062"."FIRST_PERIOD"    AS "Period"              -- (第一階梯)期數 Decimal 3 0
          ,"TBJCICZ062"."FIRST_RATE"      AS "Rate"                -- (第一階梯)利率 Decimal 5 2
          ,"TBJCICZ062"."RES_CONTRACT_EXP_AMT" AS "ExpBalanceAmt"  -- 信用貸款協商剩餘債務簽約餘額 Decimal 9 0
          ,"TBJCICZ062"."RES_CONTRACT_CASH_AMT" AS "CashBalanceAmt" -- 現金卡協商剩餘債務簽約餘額 Decimal 9 0
          ,"TBJCICZ062"."RES_CONTRACT_CREDIT_AMT" AS "CreditBalanceAmt" -- 信用卡協商剩餘債務簽約餘額 Decimal 9 0
          ,"TBJCICZ062"."CHA_REPAYMENT_AMT" AS "ChaRepayAmt"       -- 變更還款條件簽約總債務金額 Decimal 9 0
          ,"TBJCICZ062"."CHA_REPAY_AGREE_DATE" AS "ChaRepayAgreeDate" -- 變更還款條件協議完成日 Decimald 8 
          ,NVL("TBJCICZ062"."CHA_REPAY_VIEW_DATE",0)
                                          AS "ChaRepayViewDate" -- 變更還款條件面談日期 Decimald 8 
          ,NVL("TBJCICZ062"."CHA_REPAY_END_DATE",0)
                                          AS "ChaRepayEndDate"  -- 變更還款條件簽約完成日期 Decimald 8 
          ,"TBJCICZ062"."CHA_REPAY_FIRST_DATE" AS "ChaRepayFirstDate" -- 變更還款條件首期應繳款日 Decimald 8 
          ,"TBJCICZ062"."PAY_ACCOUNT"     AS "PayAccount"          -- 繳款帳號 NVARCHAR2 40 
          ,"TBJCICZ062"."POST_ADDR"       AS "PostAddr"            -- 最大債權金融機構聲請狀送達地址 NVARCHAR2 304 
          ,"TBJCICZ062"."PAY_AMT"         AS "MonthPayAmt"         -- 月付金 Decimal 9 0
          ,"TBJCICZ062"."GRADETYPE"       AS "GradeType"           -- 屬階梯式還款註記 VARCHAR2 1 0
          ,NVL("TBJCICZ062"."SECOND_PERIOD",0)
                                          AS "Period2"             -- 第二階梯期數 Decimal 3 0
          ,NVL("TBJCICZ062"."SECOND_RATE",0)
                                          AS "Rate2"               -- 第二階梯利率 Decimal 5 2
          ,NVL("TBJCICZ062"."SECOND_PAY_AMT",0)
                                          AS "MonthPayAmt2"        -- 第二階段月付金 Decimal 9 0
          ,"TBJCICZ062"."JCICEXPORTDATE"  AS "OutJcicTxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TBJCICZ062"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ062_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
