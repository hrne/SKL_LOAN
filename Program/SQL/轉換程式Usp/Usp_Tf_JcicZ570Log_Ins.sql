--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ570Log_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ570Log_Ins" 
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
    DELETE FROM "JcicZ570Log";

    -- 寫入資料
    INSERT INTO "JcicZ570Log"
    SELECT "Ukey"              AS "Ukey"                 -- 流水號 VARCHAR2 32 0
          ,LPAD('1', 18, '0')  AS "TxSeq"                -- 交易序號 VARCHAR2 18 0
          ,"TranKey"           AS "TranKey"              -- 交易代碼 VARCHAR2 1 0
          ,"AdjudicateDate"    AS "AdjudicateDate"       -- 更生方案認可裁定日 Decimald 8 0
          ,"BankCount"         AS "BankCount"            -- 更生債權金融機構家數 Decimal 2 0
          ,"Bank1"             AS "Bank1"                -- 債權金融機構代號1 NVARCHAR2 3 0
          ,"Bank2"             AS "Bank2"                -- 債權金融機構代號2 NVARCHAR2 3 0
          ,"Bank3"             AS "Bank3"                -- 債權金融機構代號3 NVARCHAR2 3 0
          ,"Bank4"             AS "Bank4"                -- 債權金融機構代號4 NVARCHAR2 3 0
          ,"Bank5"             AS "Bank5"                -- 債權金融機構代號5 NVARCHAR2 3 0
          ,"Bank6"             AS "Bank6"                -- 債權金融機構代號6 NVARCHAR2 3 0
          ,"Bank7"             AS "Bank7"                -- 債權金融機構代號7 NVARCHAR2 3 0
          ,"Bank8"             AS "Bank8"                -- 債權金融機構代號8 NVARCHAR2 3 0
          ,"Bank9"             AS "Bank9"                -- 債權金融機構代號9 NVARCHAR2 3 0
          ,"Bank10"            AS "Bank10"               -- 債權金融機構代號10 NVARCHAR2 3 0
          ,"Bank11"            AS "Bank11"               -- 債權金融機構代號11 NVARCHAR2 3 0
          ,"Bank12"            AS "Bank12"               -- 債權金融機構代號12 NVARCHAR2 3 0
          ,"Bank13"            AS "Bank13"               -- 債權金融機構代號13 NVARCHAR2 3 0
          ,"Bank14"            AS "Bank14"               -- 債權金融機構代號14 NVARCHAR2 3 0
          ,"Bank15"            AS "Bank15"               -- 債權金融機構代號15 NVARCHAR2 3 0
          ,"Bank16"            AS "Bank16"               -- 債權金融機構代號16 NVARCHAR2 3 0
          ,"Bank17"            AS "Bank17"               -- 債權金融機構代號17 NVARCHAR2 3 0
          ,"Bank18"            AS "Bank18"               -- 債權金融機構代號18 NVARCHAR2 3 0
          ,"Bank19"            AS "Bank19"               -- 債權金融機構代號19 NVARCHAR2 3 0
          ,"Bank20"            AS "Bank20"               -- 債權金融機構代號20 NVARCHAR2 3 0
          ,"Bank21"            AS "Bank21"               -- 債權金融機構代號21 NVARCHAR2 3 0
          ,"Bank22"            AS "Bank22"               -- 債權金融機構代號22 NVARCHAR2 3 0
          ,"Bank23"            AS "Bank23"               -- 債權金融機構代號23 NVARCHAR2 3 0
          ,"Bank24"            AS "Bank24"               -- 債權金融機構代號24 NVARCHAR2 3 0
          ,"Bank25"            AS "Bank25"               -- 債權金融機構代號25 NVARCHAR2 3 0
          ,"Bank26"            AS "Bank26"               -- 債權金融機構代號26 NVARCHAR2 3 0
          ,"Bank27"            AS "Bank27"               -- 債權金融機構代號27 NVARCHAR2 3 0
          ,"Bank28"            AS "Bank28"               -- 債權金融機構代號28 NVARCHAR2 3 0
          ,"Bank29"            AS "Bank29"               -- 債權金融機構代號29 NVARCHAR2 3 0
          ,"Bank30"            AS "Bank30"               -- 債權金融機構代號30 NVARCHAR2 3 0
          ,"OutJcicTxtDate"    AS "OutJcicTxtDate"       -- 轉出JCIC文字檔日期 Decimald 8 0
          ,JOB_START_TIME      AS "CreateDate"           -- 建檔日期時間 DATE 8 0
          ,'999999'            AS "CreateEmpNo"          -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME      AS "LastUpdate"           -- 最後更新日期時間 DATE 8 0
          ,'999999'            AS "LastUpdateEmpNo"      -- 最後更新人員 VARCHAR2 6 0
    FROM "JcicZ570"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ570Log_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
