--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ047_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ047_Ins" 
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
    DELETE FROM "JcicZ047";

    -- 寫入資料
    INSERT INTO "JcicZ047"
    SELECT "TBJCICZ047"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ047"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ047"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ047"."RC_DATE"         AS "RcDate"              -- 協商申請日 Decimald 8 0
          ,NVL("TBJCICZ047"."PERIOD",0)          AS "Period"              -- 期數 Decimal 16 0
          ,NVL("TBJCICZ047"."RATE",0)            AS "Rate"                -- 利率 Decimal 16 4
          ,NVL("TBJCICZ047"."CIVIL323_EXP_AMT",0) AS "Civil323ExpAmt"     -- 依民法第323條計算之信用貸款債務總金額 Decimal 16 0
          ,NVL("TBJCICZ047"."EXP_LOAN_AMT",0)    AS "ExpLoanAmt"          -- 信用貸款債務簽約總金額 Decimal 16 0
          ,NVL("TBJCICZ047"."CIVIL323_CASH_AMT",0) AS "Civil323CashAmt"   -- 依民法第323條計算之現金卡債務總金額 Decimal 16 0
          ,NVL("TBJCICZ047"."CASH_CARD_AMT",0)   AS "CashCardAmt"         -- 現金卡債務簽約總金額 Decimal 16 0
          ,NVL("TBJCICZ047"."CIVIL323_CREDIT_AMT",0) AS "Civil323CreditAmt" -- 依民法第323條計算之信用卡債務總金額 Decimal 16 0
          ,NVL("TBJCICZ047"."CREDIT_CARD_AMT",0) AS "CreditCardAmt"       -- 信用卡債務簽約總金額 Decimal 16 0
          ,NVL("TBJCICZ047"."CIVIL323_AMT",0)    AS "Civil323Amt"         -- 依民法第323條計算之債務總金額 Decimal 16 0
          ,NVL("TBJCICZ047"."TOTAL_AMT",0)       AS "TotalAmt"            -- 簽約總債務金額 Decimal 16 0
          ,NVL("TBJCICZ047"."PASS_DATE",0)       AS "PassDate"            -- 協議完成日 Decimald 8 0
          ,NVL("TBJCICZ047"."INTERVIEW_DATE",0)  AS "InterviewDate"       -- 面談日期 Decimald 8 0
          ,NVL("TBJCICZ047"."SIGN_DATE",0)       AS "SignDate"            -- 簽約完成日期 Decimald 8 0
          ,NVL("TBJCICZ047"."LIMIT_DATE",0)      AS "LimitDate"           -- 前置協商註記訊息揭露期限 Decimald 8 0
          ,NVL("TBJCICZ047"."FIRST_PAY_DATE",0)  AS "FirstPayDate"        -- 首期應繳款日 Decimald 8 0
          ,NVL("TBJCICZ047"."PAY_AMOUNT",0)      AS "MonthPayAmt"         -- 月付金 Decimal 16 0
          ,"TBJCICZ047"."PAY_ACCOUNT"     AS "PayAccount"          -- 繳款帳號 VARCHAR2 80 0
          ,"TBJCICZ047"."POST_ADDR"       AS "PostAddr"            -- 最大債權金融機構聲請狀送達地址 NVARCHAR2 304 0
          ,"TBJCICZ047"."GRADETYPE"       AS "GradeType"           -- 屬二階段還款方案之階段註記 VARCHAR2 4 0
          ,NVL("TBJCICZ047"."PAY_LASTAMT",0)     AS "PayLastAmt"          -- 第一階段最後一期應繳金額 Decimal 16 0
          ,NVL("TBJCICZ047"."PERIOD2",0)         AS "Period2"             -- 第二段期數 Decimal 16 0
          ,NVL("TBJCICZ047"."RATE2",0)           AS "Rate2"               -- 第二階段利率 Decimal 16 4
          ,NVL("TBJCICZ047"."PAY_AMT2",0)        AS "MonthPayAmt2"        -- 第二階段協商方案估計月付金 Decimal 16 0
          ,NVL("TBJCICZ047"."PAY_LASTAMT2",0)    AS "PayLastAmt2"         -- 第二階段最後一期應繳金額 Decimal 16 0
          ,"TBJCICZ047"."JCICEXPORTDATE"  AS "OutJcicTxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          ,0 AS "ActualFilingDate"
          ,NULL AS "ActualFilingMark"
    FROM "TBJCICZ047"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ047_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
