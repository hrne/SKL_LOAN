--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ443Log_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ443Log_Ins" 
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
    DELETE FROM "JcicZ443Log";

    -- 寫入資料
    INSERT INTO "JcicZ443Log"
    SELECT "Ukey"              AS "Ukey"                 -- 流水號 VARCHAR2 32 0
          ,LPAD('1', 18, '0')  AS "TxSeq"                -- 交易序號 VARCHAR2 18 0
          ,"TranKey"           AS "TranKey"              -- 交易代碼 VARCHAR2 1 0
          ,"IsMaxMain"         AS "IsMaxMain"            -- 是否為最大債權金融機構報送 VARCHAR2 1 0
          ,"Account"           AS "Account"              -- 帳號 NVARCHAR2 50 0
          ,"GuarantyType"      AS "GuarantyType"         -- 擔保品類別 NVARCHAR2 2 0
          ,"LoanAmt"           AS "LoanAmt"              -- 原借款金額 Decimal 12 0
          ,"CreditAmt"         AS "CreditAmt"            -- 授信餘額 Decimal 12 0
          ,"Principal"         AS "Principal"            -- 本金 Decimal 10 0
          ,"Interest"          AS "Interest"             -- 利息 Decimal 10 0
          ,"Penalty"           AS "Penalty"              -- 違約金 Decimal 10 0
          ,"Other"             AS "Other"                -- 其他費用 Decimal 10 0
          ,"TerminalPayAmt"    AS "TerminalPayAmt"       -- 每期應付金額 Decimal 10 0
          ,"LatestPayAmt"      AS "LatestPayAmt"         -- 最近一期繳款金額 Decimal 10 0
          ,"FinalPayDay"       AS "FinalPayDay"          -- 最後繳息日 Decimald 8 0
          ,"NotyetacQuit"      AS "NotyetacQuit"         -- 已到期尚未清償金額 Decimal 10 0
          ,"MothPayDay"        AS "MothPayDay"           -- 每月應還款日 Decimal 2 0
          ,"BeginDate"         AS "BeginDate"            -- 契約起始年月 Decimal 6 0
          ,"EndDate"           AS "EndDate"              -- 契約截止年月 Decimal 6 0
          ,"OutJcicTxtDate"    AS "OutJcicTxtDate"       -- 轉JCIC文字檔日期 Decimald 8 0
          ,JOB_START_TIME      AS "CreateDate"           -- 建檔日期時間 DATE 6 0
          ,'999999'            AS "CreateEmpNo"          -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME      AS "LastUpdate"           -- 最後更新日期時間 DATE 8 0
          ,'999999'            AS "LastUpdateEmpNo"      -- 最後更新人員 VARCHAR2 6 0
    FROM "JcicZ443"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ443Log_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
