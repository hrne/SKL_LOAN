--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ062Log_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ062Log_Ins" 
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
    DELETE FROM "JcicZ062Log";

    -- 寫入資料
    INSERT INTO "JcicZ062Log"
    SELECT "Ukey"               AS "Ukey"                   -- 流水號 VARCHAR2 32 0
          ,LPAD('1', 18, '0')   AS "TxSeq"                  -- 交易序號 VARCHAR2 18 0
          ,"TranKey"            AS "TranKey"                -- 交易代碼 VARCHAR2 1 0
          ,"CompletePeriod"     AS "CompletePeriod"         -- 變更還款條件已履約期數 Decimal 3 0
          ,"Period"             AS "Period"                 -- (第一階梯)期數 Decimal 3 0
          ,"Rate"               AS "Rate"                   -- (第一階梯)利率 Decimal 5 2
          ,"ExpBalanceAmt"      AS "ExpBalanceAmt"          -- 信用貸款協商剩餘債務簽約餘額 Decimal 9 0
          ,"CashBalanceAmt"     AS "CashBalanceAmt"         -- 現金卡協商剩餘債務簽約餘額 Decimal 9 0
          ,"CreditBalanceAmt"   AS "CreditBalanceAmt"       -- 信用卡協商剩餘債務簽約餘額 Decimal 9 0
          ,"ChaRepayAmt"        AS "ChaRepayAmt"            -- 變更還款條件簽約總債務金額 Decimal 10 0
          ,"ChaRepayAgreeDate"  AS "ChaRepayAgreeDate"      -- 變更還款條件協議完成日 Decimald 8 0
          ,"ChaRepayViewDate"   AS "ChaRepayViewDate"       -- 變更還款條件面談日期 Decimald 8 0
          ,"ChaRepayEndDate"    AS "ChaRepayEndDate"        -- 變更還款條件簽約完成日期 Decimald 8 0
          ,"ChaRepayFirstDate"  AS "ChaRepayFirstDate"      -- 變更還款條件首期應繳款日 Decimald 8 0
          ,"PayAccount"         AS "PayAccount"             -- 繳款帳號 NVARCHAR2 20 0
          ,"PostAddr"           AS "PostAddr"               -- 最大債權金融機構聲請狀送達地址 NVARCHAR2 76 0
          ,"MonthPayAmt"        AS "MonthPayAmt"            -- 月付金 Decimal 9 0
          ,"GradeType"          AS "GradeType"              -- 屬階梯式還款註記 VARCHAR2 1 0
          ,"Period2"            AS "Period2"                -- 第二階梯期數 Decimal 3 0
          ,"Rate2"              AS "Rate2"                  -- 第二階梯利率 Decimal 5 2
          ,"MonthPayAmt2"       AS "MonthPayAmt2"           -- 第二階段月付金 Decimal 9 0
          ,"OutJcicTxtDate"     AS "OutJcicTxtDate"         -- 轉出JCIC文字檔日期 Decimald 8 0
          ,JOB_START_TIME       AS "CreateDate"             -- 建檔日期時間 DATE 8 0
          ,'999999'             AS "CreateEmpNo"            -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME       AS "LastUpdate"             -- 最後更新日期時間 DATE 8 0
          ,'999999'             AS "LastUpdateEmpNo"        -- 最後更新人員 VARCHAR2 6 0
    FROM "JcicZ062"
    WHERE "OutJcicTxtDate" > 0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ062Log_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
