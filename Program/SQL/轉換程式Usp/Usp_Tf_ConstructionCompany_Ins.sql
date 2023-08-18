--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ConstructionCompany_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_ConstructionCompany_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ConstructionCompany" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ConstructionCompany" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ConstructionCompany" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ConstructionCompany" (
        "CustNo"              -- 戶號 DECIMAL 7 0
      , "DeleteFlag"          -- 建商狀況 VARCHAR2 20 0
      , "CreateDate"          -- 建檔日期時間 DATE 0 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE 0 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    )
    SELECT BUDP."LMSACN"                  AS "CustNo"              -- 戶號 DECIMAL 7 0
         , BUDP."LU$STAT"                 AS "DeleteFlag"       -- 刪除碼 VARCHAR2 1 0
         , CASE
             WHEN BUDP."CRTDTM" > 0
             THEN TO_DATE(BUDP."CRTDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                            AS "CreateDate"          -- 建檔日期時間 DATE 0 
         , NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
         , CASE
             WHEN BUDP."CHGDTM" > 0
             THEN TO_DATE(BUDP."CHGDTM",'YYYYMMDDHH24MISS')
           ELSE JOB_START_TIME
           END                            AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
         , NVL(AEM2."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LN$BUDP" BUDP
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = BUDP."CRTEMP"
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = BUDP."CHGEMP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ConstructionCompany_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
