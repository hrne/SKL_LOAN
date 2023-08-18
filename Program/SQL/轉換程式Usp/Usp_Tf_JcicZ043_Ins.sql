--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ043_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ043_Ins" 
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
    DELETE FROM "JcicZ043";

    -- 寫入資料
    INSERT INTO "JcicZ043"
    SELECT "TBJCICZ043"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ043"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ043"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ043"."RC_DATE"         AS "RcDate"              -- 協商申請日 Decimald 8 0
          ,"TBJCICZ043"."MAX_MAIN_CODE"   AS "MaxMainCode"         -- 最大債權金融機構代號 VARCHAR2 10 0
          ,"TBJCICZ043"."ACCOUNTKEY"      AS "Account"             -- 帳號 VARCHAR2 50 0
          ,"TBJCICZ043"."COLLATERAL_TYPE" AS "CollateralType"      -- 擔保品類別 VARCHAR2 2 0
          ,"TBJCICZ043"."LMSFLA"          AS "OriginLoanAmt"       -- 原借款金額 Decimal 22 0
          ,"TBJCICZ043"."LMSLBL"          AS "CreditBalance"       -- 授信餘額 Decimal 22 0
          ,"TBJCICZ043"."LMSPPA"          AS "PerPeriordAmt"       -- 每期應付金額 Decimal 22 0
          ,"TBJCICZ043"."RECE_PAY_AMT"    AS "LastPayAmt"          -- 最近一起繳款金額 Decimal 22 0
          ,"TBJCICZ043"."LMSLPD"          AS "LastPayDate"         -- 最後繳息日 Decimald 8 0
          ,"TBJCICZ043"."MATURITYAMT"     AS "OutstandAmt"         -- 已到期尚未償還金額 Decimal 22 0
          ,"TBJCICZ043"."LMSPDY"          AS "RepayPerMonDay"      -- 每月應還款日 Decimal 2 0
          ,"TBJCICZ043"."LMSLLD"          AS "ContractStartYM"     -- 契約起始年月 Decimal 6 0
          ,"TBJCICZ043"."LMSDLD"          AS "ContractEndYM"       -- 契約截止年月 Decimal 6 0
          ,"TBJCICZ043"."JCICEXPORTDATE"  AS "OutJcictxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 00
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,"TBJCICZ043".LASTUPDATEDATE    AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,"TBJCICZ043".MODIFYUSERID      AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,"TBJCICZ043".LASTUPDATEDATE    AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,"TBJCICZ043".MODIFYUSERID      AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          ,0 AS "ActualFilingDate"
          ,NULL AS "ActualFilingMark"
    FROM "TBJCICZ043"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ043_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
