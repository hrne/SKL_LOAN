--------------------------------------------------------
--  DDL for Procedure Usp_Tf_GraceCondition_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_GraceCondition_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "GraceCondition" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "GraceCondition" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "GraceCondition" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "GraceCondition" (
        "CustNo"              -- 借款人戶號 DECIMAL 7 0 
      , "FacmNo"              -- 額度編號 DECIMAL 3 0 
      , "ActUse"              -- 使用碼 VARCHAR2 1 0 
      , "CreateDate"          -- 建檔日期時間 DATE 0 0 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
      , "LastUpdate"          -- 最後更新日期時間 DATE 0 0 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
    )
    SELECT GRPP."LMSACN"                  AS "CustNo"              -- 借款人戶號 DECIMAL 7 0 
          ,GRPP."LMSAPN"                  AS "FacmNo"              -- 額度編號 DECIMAL 3 0 
          ,GRPP."ACTUSE"                  AS "ActUse"              -- 使用碼 VARCHAR2 1 0 
          ,CASE 
             WHEN GRPP."CRTDTM" > 0 
             THEN TO_DATE(GRPP."CRTDTM",'YYYYMMDDHH24MISS') 
           ELSE JOB_START_TIME 
           END                            AS "CreateDate"          -- 建檔日期時間 DATE 0 0 
          ,NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0 
          ,CASE 
             WHEN GRPP."CHGDTM" > 0 
             THEN TO_DATE(GRPP."CHGDTM",'YYYYMMDDHH24MISS') 
           ELSE JOB_START_TIME 
           END                            AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0 
          ,NVL(AEM2."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0 
    FROM "LN$GRPP" GRPP 
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = GRPP."CRTEMP" 
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = GRPP."CHGEMP" 
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_GraceCondition_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
