--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CustDataCtrl_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CustDataCtrl_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CustDataCtrl" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustDataCtrl" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "CustDataCtrl" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    -- INSERT INTO "CustDataCtrl" 
    -- SELECT "CU$MRKP"."LMSACN"             AS "CustNo"              -- 借款人戶號 DECIMAL 7  
    --       ,"CustMain"."CustUKey"          AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32  
    --       ,'Y'                            AS "Enable"              -- 啟用記號 VARCHAR2 1  
    --       ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE   
    --       ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
    --       ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE   
    --       ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
    -- FROM "CU$MRKP" 
    -- LEFT JOIN "CustMain" on "CustMain"."CustNo" = "CU$MRKP"."LMSACN" 
    -- ; 
 
    -- 寫入資料 
    INSERT INTO "CustDataCtrl" 
    SELECT "CustMain"."CustNo"            AS "CustNo"              -- 借款人戶號 DECIMAL 7  
          ,"CustMain"."CustUKey"          AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32  
          ,0                              AS "ApplMark"            -- 申請記號 DECIMAL 1  
          ,''                             AS "Reason"              -- 解除原因 VARCHAR2 50   
          ,CASE 
             WHEN PDCP."CRTDTM" > 0 
             THEN TO_DATE(PDCP."CRTDTM",'YYYYMMDDHH24MISS') 
           ELSE JOB_START_TIME 
           END                            AS "CreateDate"          -- 建檔日期時間 DATE   
          ,NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
          ,CASE 
             WHEN PDCP."CRTDTM" > 0 
             THEN TO_DATE(PDCP."CRTDTM",'YYYYMMDDHH24MISS') 
           ELSE JOB_START_TIME 
           END                            AS "LastUpdate"          -- 最後更新日期時間 DATE   
          ,NVL(AEM1."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
          ,''                             AS "CustId" 
          ,''                             AS "CustName"  
          ,''                             AS "SetDate"  
          ,''                             AS "SetEmpNo"  
          ,''                             AS "ReSetDate"  
          ,''                             AS "ReSetEmpNo"  
    FROM "LN$PDCP" PDCP 
    LEFT JOIN "CustMain" on "CustMain"."CustId" = PDCP."CUSID1" 
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = PDCP."CRTEMP" 
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CustDataCtrl_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
