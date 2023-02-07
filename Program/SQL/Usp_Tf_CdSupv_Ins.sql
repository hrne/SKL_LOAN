--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdSupv_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CdSupv_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CdSupv" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdSupv" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdSupv" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CdSupv" (
        "SupvReasonCode"      -- 理由代碼 VARCHAR2 4 
      , "SupvReasonItem"      -- 理由說明 NVARCHAR2 40 
      , "SupvReasonLevel"     -- 理由階層 VARCHAR2 1 
      , "Enable"              -- 啟用記號 VARCHAR2 1 
      , "CreateDate"          -- 建檔日期時間 DATE 0 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE 0 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT LPAD("TB$SRNP"."SRNCOD",4,0)   AS "SupvReasonCode"      -- 理由代碼 VARCHAR2 4 
          ,"TB$SRNP"."SRNDSC"             AS "SupvReasonItem"      -- 理由說明 NVARCHAR2 40 
          ,CASE
             WHEN "TB$SRNP"."SRNLEL" = '2' THEN '1'
             WHEN "TB$SRNP"."SRNLEL" = '5' THEN '2'
           ELSE "TB$SRNP"."SRNLEL" END    AS "SupvReasonLevel"     -- 理由階層 VARCHAR2 1 
          ,'Y'                            AS "Enable"              -- 啟用記號 VARCHAR2 1 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "TB$SRNP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdSupv_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
