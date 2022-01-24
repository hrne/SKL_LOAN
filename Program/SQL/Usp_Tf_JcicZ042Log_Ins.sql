--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ042Log_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ042Log_Ins" 
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
    DELETE FROM "JcicZ042Log";

    -- 寫入資料
    INSERT INTO "JcicZ042Log"
    SELECT "Ukey"               AS "Ukey"                   -- 流水號 VARCHAR2 32 0
          ,LPAD('1', 18, '0')   AS "TxSeq"                  -- 交易序號 VARCHAR2 18 0
          ,"TranKey"            AS "TranKey"                -- 交易代碼 VARCHAR2 1 0
          ,"IsClaims"           AS "IsClaims"               -- 是否為本金融機構債務人 VARCHAR2 1 0
          ,"GuarLoanCnt"        AS "GuarLoanCnt"            -- 本金融機構有擔保債權筆數 Decimal 2 0
          ,"ExpLoanAmt"         AS "ExpLoanAmt"             -- 信用貸款對內本息餘額 Decimal 9 0
          ,"Civil323ExpAmt"     AS "Civil323ExpAmt"         -- 依民法第323條計算之信用貸款本息餘額 Decimal 9 0
          ,"ReceExpAmt"         AS "ReceExpAmt"             -- 信用貸款最近一期繳款金額 Decimal 9 0
          ,"CashCardAmt"        AS "CashCardAmt"            -- 現金卡放款對內本息餘額 Decimal 9 0
          ,"Civil323CashAmt"    AS "Civil323CashAmt"        -- 依民法第323條計算之現金卡放款本息餘額 Decimal 9 0
          ,"ReceCashAmt"        AS "ReceCashAmt"            -- 現金卡最近一期繳款金額 Decimal 9 0
          ,"CreditCardAmt"      AS "CreditCardAmt"          -- 信用卡對內本息餘額 Decimal 9 0
          ,"Civil323CreditAmt"  AS "Civil323CreditAmt"      -- 依民法第323條計算之信用卡本息餘額 Decimal 9 0
          ,"ReceCreditAmt"      AS "ReceCreditAmt"          -- 信用卡最近一期繳款金額 Decimal 9 0
          ,"ReceExpPrin"        AS "ReceExpPrin"            -- 信用貸款本金 Decimal 9 0
          ,"ReceExpInte"        AS "ReceExpInte"            -- 信用貸款利息 Decimal 9 0
          ,"ReceExpPena"        AS "ReceExpPena"            -- 信用貸款違約金 Decimal 9 0
          ,"ReceExpOther"       AS "ReceExpOther"           -- 信用貸款其他費用 Decimal 9 0
          ,"CashCardPrin"       AS "CashCardPrin"           -- 現金卡本金 Decimal 9 0
          ,"CashCardInte"       AS "CashCardInte"           -- 信金卡利息 Decimal 9 0
          ,"CashCardPena"       AS "CashCardPena"           -- 信金卡違約金 Decimal 9 0
          ,"CashCardOther"      AS "CashCardOther"          -- 現金卡其他費用 Decimal 9 0
          ,"CreditCardPrin"     AS "CreditCardPrin"         -- 信用卡本金 Decimal 9 0
          ,"CreditCardInte"     AS "CreditCardInte"         -- 信用卡利息 Decimal 9 0
          ,"CreditCardPena"     AS "CreditCardPena"         -- 信用卡違約金 Decimal 9 0
          ,"CreditCardOther"    AS "CreditCardOther"        -- 信用卡其他費用 Decimal 9 0
          ,"OutJcicTxtDate"     AS "OutJcicTxtDate"         -- 轉出JCIC文字檔日期 Decimald 8 0
          ,JOB_START_TIME       AS "CreateDate"             -- 建檔日期時間 DATE 8 0
          ,'999999'             AS "CreateEmpNo"            -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME       AS "LastUpdate"             -- 最後更新日期時間 DATE 8 0
          ,'999999'             AS "LastUpdateEmpNo"        -- 最後更新人員 VARCHAR2 6 0
    FROM "JcicZ042"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ042Log_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
