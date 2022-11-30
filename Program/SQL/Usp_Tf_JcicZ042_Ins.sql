--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ042_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ042_Ins" 
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
    DELETE FROM "JcicZ042";

    -- 寫入資料
    INSERT INTO "JcicZ042"
    SELECT "TBJCICZ042"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ042"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ042"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ042"."RC_DATE"         AS "RcDate"              -- 協商申請日 Decimald 8 0
          ,"TBJCICZ042"."MAX_MAIN_CODE"   AS "MaxMainCode"         -- 最大債權金融機構代號 VARCHAR2 10 0
          ,"TBJCICZ042"."ISCLAIMS"        AS "IsClaims"            -- 是否為本金融機構債務人 VARCHAR2 1 0
          ,NVL("TBJCICZ042"."GUAR_LOAN_CNT",0)   AS "GuarLoanCnt"         -- 本金融機構有擔保債權筆數 Decimal 22 0
          ,NVL("TBJCICZ042"."EXP_LOAN_AMT",0)    AS "ExpLoanAmt"          -- 信用貸款對內本息餘額 Decimal 22 0
          ,NVL("TBJCICZ042"."CIVIL323_EXP_AMT",0) AS "Civil323ExpAmt"     -- 依民法第323條計算之信用貸款本息餘額 Decimal 22 0
          ,NVL("TBJCICZ042"."RECE_EXP_AMT",0)    AS "ReceExpAmt"          -- 信用貸款最近一期繳款金額 Decimal 22 0
          ,NVL("TBJCICZ042"."CASH_CARD_AMT",0)   AS "CashCardAmt"         -- 現金卡放款對內本息餘額 Decimal 22 0
          ,NVL("TBJCICZ042"."CIVIL323_CASH_AMT",0) AS "Civil323CashAmt"   -- 依民法第323條計算之現金卡放款本息餘額 Decimal 22 0
          ,NVL("TBJCICZ042"."RECE_CASH_AMT",0)   AS "ReceCashAmt"         -- 現金卡最近一期繳款金額 Decimal 22 0
          ,NVL("TBJCICZ042"."CREDIT_CARD_AMT",0) AS "CreditCardAmt"       -- 信用卡對內本息餘額 Decimal 22 0
          ,NVL("TBJCICZ042"."CIVIL323_CREDIT_AMT",0) AS "Civil323CreditAmt" -- 依民法第323條計算之信用卡本息餘額 Decimal 22 0
          ,NVL("TBJCICZ042"."RECE_CREDIT_AMT",0) AS "ReceCreditAmt"       -- 信用卡最近一期繳款金額 Decimal 22 0
          ,NVL("TBJCICZ042"."RECE_EXP_PRIN",0)   AS "ReceExpPrin"         -- 信用貸款本金 Decimal 22 0
          ,NVL("TBJCICZ042"."RECE_EXP_INTE",0)   AS "ReceExpInte"         -- 信用貸款利息 Decimal 22 0
          ,NVL("TBJCICZ042"."RECE_EXP_PENA",0)   AS "ReceExpPena"         -- 信用貸款違約金 Decimal 22 0
          ,NVL("TBJCICZ042"."RECE_EXP_OTHER",0)  AS "ReceExpOther"        -- 信用貸款其他費用 Decimal 22 0
          ,NVL("TBJCICZ042"."CASH_CARD_PRIN",0)  AS "CashCardPrin"        -- 現金卡本金 Decimal 22 0
          ,NVL("TBJCICZ042"."CASH_CARD_INTE",0)  AS "CashCardInte"        -- 信金卡利息 Decimal 22 0
          ,NVL("TBJCICZ042"."CASH_CARD_PENA",0)  AS "CashCardPena"        -- 信金卡違約金 Decimal 22 0
          ,NVL("TBJCICZ042"."CASH_CARD_OTHER",0) AS "CashCardOther"       -- 現金卡其他費用 Decimal 22 0
          ,NVL("TBJCICZ042"."CREDIT_CARD_PRIN",0) AS "CreditCardPrin"     -- 信用卡本金 Decimal 22 0
          ,NVL("TBJCICZ042"."CREDIT_CARD_INTE",0) AS "CreditCardInte"     -- 信用卡利息 Decimal 22 0
          ,NVL("TBJCICZ042"."CREDIT_CARD_PENA",0) AS "CreditCardPena"     -- 信用卡違約金 Decimal 22 0
          ,NVL("TBJCICZ042"."CREDIT_CARD_OTHER",0) AS "CreditCardOther"   -- 信用卡其他費用 Decimal 22 0
          ,NVL("TBJCICZ042"."JCICEXPORTDATE",0)  AS "OutJcictxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          ,0 AS "ActualFilingDate"
          ,NULL AS "ActualFilingMark"
    FROM "TBJCICZ042"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ042_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
