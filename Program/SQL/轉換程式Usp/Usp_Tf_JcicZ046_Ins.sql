--------------------------------------------------------
--  DDL for Procedure Usp_Tf_JcicZ046_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_JcicZ046_Ins" 
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
    DELETE FROM "JcicZ046";

    -- 寫入資料
    INSERT INTO "JcicZ046"
    SELECT "TBJCICZ046"."TRANSACTIONSID"  AS "TranKey"             -- 交易代碼 VARCHAR2 1 0
          ,"TBJCICZ046"."SUBMITID"        AS "SubmitKey"           -- 報送單位代號 VARCHAR2 10 0
          ,"TBJCICZ046"."CUSTIDN"         AS "CustId"              -- 債務人IDN VARCHAR2 10 0
          ,"TBJCICZ046"."RC_DATE"         AS "RcDate"              -- 協商申請日 Decimald 8 0
          ,"TBJCICZ046"."CLOSE_CODE"      AS "CloseCode"           -- 結案原因代號 VARCHAR2 2 0
          ,"TBJCICZ046"."BREAK_CODE"      AS "BreakCode"           -- 毀諾原因代號 VARCHAR2 2 0
          ,"TBJCICZ046"."CLOSE_DATE"      AS "CloseDate"           -- 結案日期 Decimald 8 0
          ,"TBJCICZ046"."JCICEXPORTDATE"  AS "OutJcicTxtDate"      -- 轉出JCIC文字檔日期 Decimald 8 0
          ,SYS_GUID()                     AS "Ukey"                -- 流水號 VARCHAR2 32
          ,"TBJCICZ046".LASTUPDATEDATE    AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,"TBJCICZ046".MODIFYUSERID      AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,"TBJCICZ046".LASTUPDATEDATE    AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,"TBJCICZ046".MODIFYUSERID      AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
          ,0 AS "ActualFilingDate"
          ,NULL AS "ActualFilingMark"
    FROM "TBJCICZ046"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_JcicZ046_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
