--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ443_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ443_Ins" 
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
    DELETE FROM "JcicZ443";

    -- 寫入資料
    INSERT INTO "JcicZ443"
    SELECT "TBJCICZ443"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ443"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ443"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ443"."APPLYDATE"       AS "ApplyDate"           -- 款項統一收付申請日 Decimald 8 0
          ,"TBJCICZ443"."BANK_ID"         AS "BankId"              -- 異動債權金機構代號 VARCHAR2 3 0
          ,"TBJCICZ443"."MAX_MAIN_CODE"   AS "MaxMainCode"         -- 最大債權金融機構代號 VARCHAR2 3 0
          ,"TBJCICZ443"."ISMAXMAIN"       AS "IsMaxMain"           -- 是否為最大債權金融機構報送 VARCHAR2 1 0
          ,"TBJCICZ443"."ACCOUNT"         AS "Account"             -- 帳號 VARCHAR2 20 0
          ,"TBJCICZ443"."GUARANTYTYPE"    AS "GuarantyType"        -- 擔保品類別 VARCHAR2 2 0
          ,"TBJCICZ443"."LOANAMT"         AS "LoanAmt"             -- 原借款金額 Decimal 12 0
          ,"TBJCICZ443"."CREDITAMT"       AS "CreditAmt"           -- 授信餘額 Decimal 12 0
          ,"TBJCICZ443"."PRINCIPAL"       AS "Principal"           -- 本金 Decimal 12 0
          ,"TBJCICZ443"."INTEREST"        AS "Interest"            -- 利息 Decimal 10 0
          ,"TBJCICZ443"."PENALTY"         AS "Penalty"             -- 違約金 Decimal 10 0
          ,"TBJCICZ443"."OTHER"           AS "Other"               -- 其他費用 Decimal 10 0
          ,"TBJCICZ443"."TERMINALPAYAMT"  AS "TerminalPayAmt"      -- 每期應付金額 Decimal 10 0
          ,"TBJCICZ443"."LATESTPAYAMT"    AS "LatestPayAmt"        -- 最近一期繳款金額 Decimal 10 0
          ,"TBJCICZ443"."FINALPAYDAY"     AS "FinalPayDay"         -- 最後繳息日 Decimald 8 0
          ,"TBJCICZ443"."NOTYETACQUIT"    AS "NotyetacQuit"        -- 已到期尚未清償金額 Decimal 10 0
          ,"TBJCICZ443"."MOTHPAYDAY"      AS "MothPayDay"          -- 每月應還款日 VARCHAR2 2 0
          ,"TBJCICZ443"."BEGINDATE"       AS "BeginDate"           -- 契約起始年月 VARCHAR2 6 0
          ,"TBJCICZ443"."ENDDATE"         AS "EndDate"             -- 契約截止年月 VARCHAR2 6 0
          ,"TBJCICZ443"."JCICEXPORTDATE"  AS "OutJcicTxtDate"      -- 轉JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          ,0 AS "ActualFilingDate"
          ,NULL AS "ActualFilingMark"
    FROM "TBJCICZ443"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ443_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
