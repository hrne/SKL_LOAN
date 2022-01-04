--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdInsurer_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_CdInsurer_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CdInsurer" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdInsurer" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdInsurer" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CdInsurer"
    SELECT "TB$ISRP"."ISRTYP"             AS "InsurerType"         -- 公司種類 VARCHAR2 1 
          ,"TB$ISRP"."ISRIID"             AS "InsurerCode"         -- 公司代號 VARCHAR2 2 
          ,''                             AS "InsurerId"           -- 公司統編 VARCHAR2 10
          ,"TB$ISRP"."CUSNA1"             AS "InsurerItem"         -- 公司名稱 NVARCHAR2 40 
          ,"TB$ISRP"."ISRINA"             AS "InsurerShort"        -- 公司簡稱 NVARCHAR2 10 
          ,CASE
             WHEN LENGTH(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-',''))) > 8 
             THEN CASE
                    WHEN SUBSTR(TRIM("TB$ISRP"."GRTTEL"),0,4) IN ('0800','0809','0826','0836')
                    THEN SUBSTR(TRIM("TB$ISRP"."GRTTEL"),0,4)
                    WHEN SUBSTR(TRIM("TB$ISRP"."GRTTEL"),0,3) IN ('082','089','049','037')
                    THEN SUBSTR(TRIM("TB$ISRP"."GRTTEL"),0,3)
                    WHEN SUBSTR(TRIM("TB$ISRP"."GRTTEL"),0,2) IN ('02','03','04','05','06','07','08')
                    THEN SUBSTR(TRIM("TB$ISRP"."GRTTEL"),0,2)
                    WHEN SUBSTR(TRIM("TB$ISRP"."GRTTEL"),0,2) = '09'
                    THEN SUBSTR(TRIM("TB$ISRP"."GRTTEL"),0,4)
                  ELSE '' END
           ELSE '' END                    AS "TelArea"             -- 連絡電話區碼 VARCHAR2 5 
          ,CASE
             WHEN LENGTH(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-',''))) > 8 
             THEN CASE
                    WHEN SUBSTR(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-','')),0,4) IN ('0800','0809','0826','0836')
                    THEN SUBSTR(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-','')),5,LENGTH(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-',''))))
                    WHEN SUBSTR(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-','')),0,3) IN ('082','089','049','037')
                    THEN SUBSTR(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-','')),4,LENGTH(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-',''))))
                    WHEN SUBSTR(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-','')),0,2) IN ('02','03','04','05','06','07','08')
                    THEN SUBSTR(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-','')),3,LENGTH(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-',''))))
                    WHEN SUBSTR(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-','')),0,2) = '09'
                    THEN SUBSTR(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-','')),5,LENGTH(TRIM(REPLACE("TB$ISRP"."GRTTEL",'-',''))))
                  ELSE TRIM(REPLACE("TB$ISRP"."GRTTEL",'-','')) END
           ELSE '' END
                                          AS "TelNo"               -- 連絡電話號碼 VARCHAR2 10 
          ,''                             AS "TelExt"              -- 連絡電話分機號碼 VARCHAR2 5 
          ,'Y'                            AS "Enable"              -- 啟用記號 VARCHAR2 1 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "TB$ISRP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdInsurer_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
