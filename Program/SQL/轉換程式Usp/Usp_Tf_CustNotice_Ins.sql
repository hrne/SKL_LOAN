--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CustNotice_Ins
--------------------------------------------------------
set define off;

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CustNotice_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CustNotice" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustNotice" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "CustNotice" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "CustNotice" 
    WITH orderedData AS (
        SELECT NP."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7  
              ,FAC."FacmNo"                   AS "FacmNo"              -- 額度編號 DECIMAL 3  
              ,RCM."NewReportCode"            AS "FormNo"              -- 報表代號 VARCHAR2 10  
              ,NP."ACTUSE"                    AS "PaperNotice"         -- 書面通知與否 VARCHAR2 1  
              ,'Y'                            AS "MsgNotice"           -- 簡訊發送與否 VARCHAR2 1  
              ,'Y'                            AS "EmailNotice"         -- 電子郵件發送與否 VARCHAR2 1  
              ,0                              AS "ApplyDate"           -- 申請日期 decimald 8  
              ,CASE 
                 WHEN NP."CRTDTM" > 0 
                 THEN TO_DATE(NP."CRTDTM",'YYYYMMDDHH24MISS') 
               ELSE JOB_START_TIME 
               END                            AS "CreateDate"          -- 建檔日期時間 DATE   
              ,NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
              ,CASE 
                 WHEN NP."CHGDTM" > 0 
                 THEN TO_DATE(NP."CHGDTM",'YYYYMMDDHH24MISS') 
               ELSE JOB_START_TIME 
               END                            AS "LastUpdate"          -- 最後更新日期時間 DATE   
              ,NVL(AEM2."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
              ,ROW_NUMBER()
               OVER (
                PARTITION BY NP."LMSACN"
                           , FAC."FacmNo"
                           , RCM."NewReportCode"
                ORDER BY NP."CHGDTM" DESC
                       , NP."CRTDTM" DESC
               ) AS "Seq"
        FROM "LN$NPTP" NP 
        LEFT JOIN "FacMain" FAC ON FAC."CustNo" = NP."LMSACN" 
        LEFT JOIN "ReportCodeMapping" RCM ON RCM."OriReportCode" = NP."F23FMT" 
        LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = NP."CRTEMP" 
        LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = NP."CHGEMP" 
        WHERE NVL(FAC."CustNo",0) <> 0 
    )
    SELECT "CustNo"              -- 戶號 DECIMAL 7  
          ,"FacmNo"              -- 額度編號 DECIMAL 3  
          ,"FormNo"              -- 報表代號 VARCHAR2 10  
          ,"PaperNotice"         -- 書面通知與否 VARCHAR2 1  
          ,"MsgNotice"           -- 簡訊發送與否 VARCHAR2 1  
          ,"EmailNotice"         -- 電子郵件發送與否 VARCHAR2 1  
          ,"ApplyDate"           -- 申請日期 decimald 8  
          ,"CreateDate"          -- 建檔日期時間 DATE   
          ,"CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
          ,"LastUpdate"          -- 最後更新日期時間 DATE   
          ,"LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
    FROM orderedData
    WHERE "Seq" = 1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CustNotice_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
