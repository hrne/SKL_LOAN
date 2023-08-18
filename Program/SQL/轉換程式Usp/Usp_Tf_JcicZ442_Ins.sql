--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ442_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ442_Ins" 
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
    DELETE FROM "JcicZ442";

    -- 寫入資料
    INSERT INTO "JcicZ442"
    SELECT Z442."TRANSACTIONSID"             AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,Z442."CUSTIDN"                    AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,Z442."SUBMITID"                   AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,Z442."APPLYDATE"                  AS "ApplyDate"           -- 款項統一收付申請日 Decimald 8 0
          ,Z442."BANK_ID"                    AS "BankId"              -- 異動債權金機構代號 VARCHAR2 3 0
          ,Z442."MAX_MAIN_CODE"              AS "MaxMainCode"         -- 最大債權金融機構代號 VARCHAR2 3 0
          ,Z442."ISMAXMAIN"                  AS "IsMaxMain"           -- 是否為最大債權金融機構報送 VARCHAR2 1 0
          ,Z442."ISCLAIMS"                   AS "IsClaims"            -- 是否為本金融機構債務人 VARCHAR2 1 0
          ,NVL(Z442."GUAR_LOAN_CNT",0)       AS "GuarLoanCnt"         -- 本金融機構有擔保債權筆數 VARCHAR2 1 0
          ,NVL(Z442."CIVIL323_EXP_AMT",0)    AS "Civil323ExpAmt"     -- 依民法第323條計算之信用放款本息餘額 DECIMAL 9 0
          ,NVL(Z442."CIVIL323_CASH_AMT",0)   AS "Civil323CashAmt"   -- 依民法第323條計算之現金卡放款本息餘額 DECIMAL 9 0
          ,NVL(Z442."CIVIL323_CREDIT_AMT",0) AS "Civil323CreditAmt" -- 依民法第323條計算之信用卡本息餘額 DECIMAL 9 0
          ,NVL(Z442."CIVIL323_GUAR_AMT",0)   AS "Civil323GuarAmt"   -- 依民法第323條計算之保證債權本息餘額 DECIMAL 9 0
          ,NVL(Z442."RECE_EXP_PRIN",0)       AS "ReceExpPrin"         -- 信用放款本金 DECIMAL 9 0
          ,NVL(Z442."RECE_EXP_INTE",0)       AS "ReceExpInte"         -- 信用放款利息 DECIMAL 9 0
          ,NVL(Z442."RECE_EXP_PENA",0)       AS "ReceExpPena"         -- 信用放款違約金 DECIMAL 9 0
          ,NVL(Z442."RECE_EXP_OTHER",0)      AS "ReceExpOther"        -- 信用放款其他費用 DECIMAL 9 0
          ,NVL(Z442."CASH_CARD_PRIN",0)      AS "CashCardPrin"        -- 現金卡本金 DECIMAL 9 0
          ,NVL(Z442."CASH_CARD_INTE",0)      AS "CashCardInte"        -- 現金卡利息 DECIMAL 9 0
          ,NVL(Z442."CASH_CARD_PENA",0)      AS "CashCardPena"        -- 現金卡違約金 DECIMAL 9 0
          ,NVL(Z442."CASH_CARD_OTHER",0)     AS "CashCardOther"       -- 現金卡其他費用 DECIMAL 9 0
          ,NVL(Z442."CREDIT_CARD_PRIN",0)    AS "CreditCardPrin"     -- 信用卡本金 DECIMAL 9 0
          ,NVL(Z442."CREDIT_CARD_INTE",0)    AS "CreditCardInte"     -- 信用卡利息 DECIMAL 9 0
          ,NVL(Z442."CREDIT_CARD_PENA",0)    AS "CreditCardPena"     -- 信用卡違約金 DECIMAL 9 0
          ,NVL(Z442."CREDIT_CARD_OTHER",0)   AS "CreditCardOther"   -- 信用卡其他費用 DECIMAL 9 0
          ,NVL(Z442."GUAR_OBLI_PRIN",0)      AS "GuarObliPrin"        -- 保證債權本金 DECIMAL 9 0
          ,NVL(Z442."GUAR_OBLI_INTE",0)      AS "GuarObliInte"        -- 保證債權利息 DECIMAL 9 0
          ,NVL(Z442."GUAR_OBLI_PENA",0)      AS "GuarObliPena"        -- 保證債權違約金 DECIMAL 9 0
          ,NVL(Z442."GUAR_OBLI_OTHER",0)     AS "GuarObliOther"       -- 保證債權其他費用 DECIMAL 9 0
          ,NVL(Z442."JCICEXPORTDATE",0)      AS "OutJcicTxtDate"      -- 轉JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                        AS "Ukey"                -- 流水號 VARCHAR2 32
          ,Z442.LASTUPDATEDATE               AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,Z442.MODIFYUSERID                 AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,Z442.LASTUPDATEDATE               AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,Z442.MODIFYUSERID                 AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          ,0 AS "ActualFilingDate"
          ,NULL AS "ActualFilingMark"
    FROM "TBJCICZ442" Z442
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ442_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
