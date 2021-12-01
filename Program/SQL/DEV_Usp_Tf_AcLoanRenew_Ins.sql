--------------------------------------------------------
--  DDL for Procedure Usp_Tf_AcLoanRenew_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_AcLoanRenew_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "AcLoanRenew" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcLoanRenew" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "AcLoanRenew" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "AcLoanRenew"
    SELECT S1."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 3
          ,S1."LMSAPN"                    AS "NewFacmNo"           -- 新額度編號 DECIMAL 3
          ,S1."LMSASQ"                    AS "NewBormNo"           -- 新撥款序號 DECIMAL 3
          ,S1."LMSAPN1"                   AS "OldFacmNo"           -- 舊額度編號 DECIMAL 6
          ,S1."LMSASQ1"                   AS "OldBormNo"           -- 舊撥款序號 DECIMAL 6
          ,CASE
             WHEN NVL(S2."LMSACN",0) <> 0 THEN '2'  -- 協議
           ELSE '1' -- 一般
           END                            AS "ReNewCode"           -- 展期記號 VARCHAR2 1 (1:一般 2:協議)
          ,CASE
             WHEN S1."Seq" = 1 -- 新撥款對到舊撥款 最早的一筆 為Y
             THEN 'Y'
           ELSE 'N' END                   AS "MainFlag"            -- 主要記號 VARCHAR2 1 (Y:新撥款對到舊撥款最早的一筆 )
          ,NVL(S3."LMSLLD",0)             AS "AcDate"              -- 會計日期 DECIMAL 8
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
    FROM (SELECT "LMSACN"
                ,"LMSAPN"
                ,"LMSASQ"
                ,"LMSAPN1"
                ,"LMSASQ1"
                ,ROW_NUMBER() OVER (PARTITION BY "LMSACN","LMSAPN","LMSASQ"
                                    ORDER BY "LMSAPN1","LMSASQ1") AS "Seq"
          FROM "LNACNP"
          GROUP BY "LMSACN"
                  ,"LMSAPN"
                  ,"LMSASQ"
                  ,"LMSAPN1"
                  ,"LMSASQ1") S1
    LEFT JOIN (SELECT "LMSACN"
                     ,"LMSAPN"
                     ,"LMSASQ"
               FROM "LN$NODP"
               GROUP BY "LMSACN"
                       ,"LMSAPN"
                       ,"LMSASQ") S2 ON S2."LMSACN" = S1."LMSACN"
                                    AND S2."LMSAPN" = S1."LMSAPN"
                                    AND S2."LMSASQ" = S1."LMSASQ"
    LEFT JOIN "LA$LMSP" S3 ON S3."LMSACN" = S1."LMSACN"
                          AND S3."LMSAPN" = S1."LMSAPN"
                          AND S3."LMSASQ" = S1."LMSASQ"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_AcLoanRenew_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
