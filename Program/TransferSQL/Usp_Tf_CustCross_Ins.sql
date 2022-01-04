--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CustCross_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_CustCross_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CustCross" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustCross" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CustCross" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CustCross"
    SELECT TMP."CustUKey"                 AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32 
          ,TMP."SubCompanyCode"           AS "SubCompanyCode"      -- 子公司代碼 DECIMAL 2 
          ,TMP."CrossUse"                 AS "CrossUse"            -- 交互運用 VARCHAR2 1
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM (SELECT S2."CustUKey"
                ,1             AS "SubCompanyCode"
                ,CASE
                   WHEN LENGTHB(S1."CUSMKU") = 6 AND SUBSTR(S1."CUSMKU",1,1) = '1' THEN 'Y'
                 ELSE 'N' END  AS "CrossUse"
                ,S1."CUSMKU"
          FROM "CU$CUSP" S1
          LEFT JOIN "CustMain" S2 ON S2."CustId" = S1."CUSID1"
          UNION ALL
          SELECT S2."CustUKey"
                ,2             AS "SubCompanyCode"
                ,CASE
                   WHEN LENGTHB(S1."CUSMKU") = 6 AND SUBSTR(S1."CUSMKU",2,1) = '1' THEN 'Y'
                 ELSE 'N' END  AS "CrossUse"
                ,S1."CUSMKU"
          FROM "CU$CUSP" S1
          LEFT JOIN "CustMain" S2 ON S2."CustId" = S1."CUSID1"
          UNION ALL
          SELECT S2."CustUKey"
                ,3             AS "SubCompanyCode"
                ,CASE
                   WHEN LENGTHB(S1."CUSMKU") = 6 AND SUBSTR(S1."CUSMKU",3,1) = '1' THEN 'Y'
                 ELSE 'N' END  AS "CrossUse"
                ,S1."CUSMKU"
          FROM "CU$CUSP" S1
          LEFT JOIN "CustMain" S2 ON S2."CustId" = S1."CUSID1"
          UNION ALL
          SELECT S2."CustUKey"
                ,4             AS "SubCompanyCode"
                ,CASE
                   WHEN LENGTHB(S1."CUSMKU") = 6 AND SUBSTR(S1."CUSMKU",4,1) = '1' THEN 'Y'
                 ELSE 'N' END  AS "CrossUse"
                ,S1."CUSMKU"
          FROM "CU$CUSP" S1
          LEFT JOIN "CustMain" S2 ON S2."CustId" = S1."CUSID1"
          UNION ALL
          SELECT S2."CustUKey"
                ,5             AS "SubCompanyCode"
                ,CASE
                   WHEN LENGTHB(S1."CUSMKU") = 6 AND SUBSTR(S1."CUSMKU",5,1) = '1' THEN 'Y'
                 ELSE 'N' END  AS "CrossUse"
                ,S1."CUSMKU"
          FROM "CU$CUSP" S1
          LEFT JOIN "CustMain" S2 ON S2."CustId" = S1."CUSID1"
          UNION ALL
          SELECT S2."CustUKey"
                ,6             AS "SubCompanyCode"
                ,CASE
                   WHEN LENGTHB(S1."CUSMKU") = 6 AND SUBSTR(S1."CUSMKU",6,1) = '1' THEN 'Y'
                 ELSE 'N' END  AS "CrossUse"
                ,S1."CUSMKU"
          FROM "CU$CUSP" S1
          LEFT JOIN "CustMain" S2 ON S2."CustId" = S1."CUSID1"
         ) TMP
    WHERE NVL(TMP."CustUKey",' ') <> ' '
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CustCross_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
