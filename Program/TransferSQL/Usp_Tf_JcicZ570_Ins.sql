--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ570_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_JcicZ570_Ins" 
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
    DELETE FROM "JcicZ570";

    -- 寫入資料
    INSERT INTO "JcicZ570"
    SELECT "TBJCICZ064"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ064"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ064"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 NVARCHAR2 3 0
          ,"TBJCICZ064"."APPLYDATE"       AS "ApplyDate"           -- 款項統一收付申請日 Decimald 8 0
          ,"TBJCICZ064"."ADJUDICATEDATE"  AS "AdjudicateDate"      -- 更生方案認可裁定日 Decimald 8 0
          ,"TBJCICZ064"."BANK_COUNT"      AS "BankCount"           -- 更生債權金融機構家數 Decimal 2 0
          ,"TBJCICZ064"."BANK_1"          AS "Bank1"               -- 債權金融機構代號1 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_2"          AS "Bank2"               -- 債權金融機構代號2 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_3"          AS "Bank3"               -- 債權金融機構代號3 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_4"          AS "Bank4"               -- 債權金融機構代號4 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_5"          AS "Bank5"               -- 債權金融機構代號5 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_6"          AS "Bank6"               -- 債權金融機構代號6 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_7"          AS "Bank7"               -- 債權金融機構代號7 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_8"          AS "Bank8"               -- 債權金融機構代號8 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_9"          AS "Bank9"               -- 債權金融機構代號9 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_10"         AS "Bank10"              -- 債權金融機構代號10 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_11"         AS "Bank11"              -- 債權金融機構代號11 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_12"         AS "Bank12"              -- 債權金融機構代號12 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_13"         AS "Bank13"              -- 債權金融機構代號13 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_14"         AS "Bank14"              -- 債權金融機構代號14 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_15"         AS "Bank15"              -- 債權金融機構代號15 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_16"         AS "Bank16"              -- 債權金融機構代號16 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_17"         AS "Bank17"              -- 債權金融機構代號17 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_18"         AS "Bank18"              -- 債權金融機構代號18 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_19"         AS "Bank19"              -- 債權金融機構代號19 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_20"         AS "Bank20"              -- 債權金融機構代號20 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_21"         AS "Bank21"              -- 債權金融機構代號21 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_22"         AS "Bank22"              -- 債權金融機構代號22 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_23"         AS "Bank23"              -- 債權金融機構代號23 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_24"         AS "Bank24"              -- 債權金融機構代號24 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_25"         AS "Bank25"              -- 債權金融機構代號25 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_26"         AS "Bank26"              -- 債權金融機構代號26 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_27"         AS "Bank27"              -- 債權金融機構代號27 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_28"         AS "Bank28"              -- 債權金融機構代號28 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_29"         AS "Bank29"              -- 債權金融機構代號29 NVARCHAR2 3 0
          ,"TBJCICZ064"."BANK_30"         AS "Bank30"              -- 債權金融機構代號30 NVARCHAR2 3 0
          ,"TBJCICZ064"."JCICEXPORTDATE"  AS "OutJcicTxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TBJCICZ064"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ570_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
