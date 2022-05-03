--------------------------------------------------------
--  DDL for Procedure Usp_Tf_FacProdStepRate_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_FacProdStepRate_Ins" 
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
    DELETE FROM "TmpLA$ASCP";

    INSERT INTO "TmpLA$ASCP"
    SELECT LPAD(LA."LMSACN",7,'0') AS "LMSACN" -- VARCHAR2(7)  戶號 N 7 
          ,LPAD(LA."LMSAPN",3,'0') AS "LMSAPN" -- VARCHAR2(3)  額度號碼 N 3 
          ,LA."ASCADT"                         -- DECIMAL(8),  加碼生效日期 N 8 
          ,LA."ASCRAT"                         -- DECIMAL(6,4) 加碼利率 D 6 4
          ,ROW_NUMBER () OVER (PARTITION BY LA."LMSACN"
                                           ,LA."LMSAPN"
                               ORDER BY LA."ASCADT") 
                                   AS "Seq"    -- DECIMAL(5)   順序
    FROM "LA$ASCP" LA
    WHERE LA."LMSASQ" = 1
    ;

    -- 刪除舊資料
    DELETE FROM "FacProdStepRate";

    -- 寫入資料
    INSERT INTO "FacProdStepRate"
    SELECT A1."LMSACN" || A1."LMSAPN"     AS "ProdNo"              -- 商品代碼或戶號+額度編號 VARCHAR2 10 
          -- 串額度檔計算"ASCADT"與首撥日APLFSD計算月差
          ,ROUND(MONTHS_BETWEEN(TO_DATE(A1."ASCADT",'YYYYMMDD')
                               ,TO_DATE(AP."APLFSD",'YYYYMMDD')
           ),0) + 1                       AS "MonthStart"          -- 月數(含)以上 DECIMAL 3 
          ,CASE
             WHEN NVL(A2."ASCADT",0) > 0
             THEN ROUND(MONTHS_BETWEEN(TO_DATE(A2."ASCADT",'YYYYMMDD')
                                      ,TO_DATE(AP."APLFSD",'YYYYMMDD')
                  ),0)
           ELSE 999 END                   AS "MonthEnd"            -- 月數(含)以下 DECIMAL 3 
          -- 1:固定利率 2:加碼利率
          ,'2'                            AS "RateType"            -- 利率型態 VARCHAR2 1 
          ,A1."ASCRAT"                    AS "RateIncr"            -- 加碼利率 DECIMAL 6 4
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "TmpLA$ASCP" A1
    LEFT JOIN "LA$APLP" AP ON AP."LMSACN" = TO_NUMBER(A1."LMSACN")
                          AND AP."LMSAPN" = TO_NUMBER(A1."LMSAPN")
    LEFT JOIN "TmpLA$ASCP" A2 ON A2."LMSACN" = A1."LMSACN"
                             AND A2."LMSAPN" = A1."LMSAPN"
                             AND A2."Seq"    = A1."Seq" + 1
    WHERE NVL(AP."APLFSD",0) > 0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_FacProdStepRate_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
