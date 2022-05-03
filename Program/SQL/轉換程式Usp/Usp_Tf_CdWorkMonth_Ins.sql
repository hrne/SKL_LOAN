--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdWorkMonth_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CdWorkMonth_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CdWorkMonth" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdWorkMonth" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdWorkMonth" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CdWorkMonth"
    SELECT SUBSTR(TO_CHAR(S1."YGYYMM"),0,4)
                                          AS "Year"                -- 業績年度 DECIMAL 4
          ,SUBSTR(TO_CHAR(S1."YGYYMM"),-2)
                                          AS "Month"               -- 工作月份 DECIMAL 2
          ,S1."DATES"                     AS "StartDate"           -- 開始日期 DECIMALD 8 
          ,S1."DATEE"                     AS "EndDate"             -- 終止日期 DECIMALD 8
          ,0                              AS "BonusDate"           -- 獎金發放日 DECIMALD 8
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "TB$WKMP" S1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdWorkMonth_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
