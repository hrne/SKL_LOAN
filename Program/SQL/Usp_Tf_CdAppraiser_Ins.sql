--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdAppraiser_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CdAppraiser_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CdAppraiser" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdAppraiser" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdAppraiser" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CdAppraiser" (
        "AppraiserCode"       -- 估價人員代號 VARCHAR2 6 0
      , "AppraiserItem"       -- 估價人員姓名 NVARCHAR2 100 0
      , "Company"             -- 公司名稱 NVARCHAR2 100 0
      , "CreateDate"          -- 建檔日期時間 DATE 0 0
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
      , "LastUpdate"          -- 最後更新日期時間 DATE 0 0
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    )
    SELECT "TB$EM6P"."CUSEM6"             AS "AppraiserCode"       -- 估價人員代號 VARCHAR2 6 0
          ,"TB$EM6P"."EMPNA6"             AS "AppraiserItem"       -- 估價人員姓名 NVARCHAR2 100 0
          ,"TB$EM6P"."CUSNA1"             AS "Company"             -- 公司名稱 NVARCHAR2 100 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TB$EM6P"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdAppraiser_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
