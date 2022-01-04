--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdIndustry_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_CdIndustry_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CdIndustry" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdIndustry" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdIndustry" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CdIndustry"
    SELECT LPAD(OCP."OCPCOD",6,'0')       AS "IndustryCode"        -- 行業代號 VARCHAR2 6 0 -- 2021-07-22修改: 位數不足6碼者，前補零
          ,OCP."OCPNAM"                   AS "IndustryItem"        -- 行業說明 NVARCHAR2 50 0
          ,OCP."OCPTYP"                   AS "MainType"            -- 主計處大類 VARCHAR2 1 0
          ,''                             AS "IndustryRating"      -- 企金放款產業評等 VARCHAR2 1 0
          ,'Y'                            AS "Enable"              -- 啟用記號 VARCHAR2 1 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TB$OCPP" OCP
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 2021-07-22新增:來源檔缺060000:個人
    -- 寫入資料
    INSERT INTO "CdIndustry"
    SELECT '060000'                       AS "IndustryCode"        -- 行業代號 VARCHAR2 6 0 -- 2021-07-22修改: 位數不足6碼者，前補零
          ,'個人'                         AS "IndustryItem"        -- 行業說明 NVARCHAR2 50 0
          ,'1'                            AS "MainType"            -- 主計處大類 VARCHAR2 1 0
          ,''                             AS "IndustryRating"      -- 企金放款產業評等 VARCHAR2 1 0
          ,'Y'                            AS "Enable"              -- 啟用記號 VARCHAR2 1 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM DUAL
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdIndustry_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
