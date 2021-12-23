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
    WITH TX AS (
      SELECT LMSACN
           , LMSAPN
           , LMSASQ
           , TRXDAT
      FROM LA$TRXP
      WHERE TRXTRN IN ('3025','3087')
        AND TRXCRC = 0
    )
    SELECT S1."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 3
          ,S1."LMSAPN"                    AS "NewFacmNo"           -- 新額度編號 DECIMAL 3
          ,S1."LMSASQ"                    AS "NewBormNo"           -- 新撥款序號 DECIMAL 3
          ,S1."LMSAPN1"                   AS "OldFacmNo"           -- 舊額度編號 DECIMAL 6
          ,S1."LMSASQ1"                   AS "OldBormNo"           -- 舊撥款序號 DECIMAL 6
          ,CASE
             WHEN NVL(S2."LMSACN",0) <> 0 THEN '2'  -- 協議
           ELSE '1' -- 一般
           END                            AS "RenewCode"           -- 展期記號 VARCHAR2 1 (1:一般 2:協議)
          ,CASE
             WHEN S1."Seq" = 1 -- 新撥款對到舊撥款 最早的一筆 為Y
             THEN 'Y'
           ELSE 'N' END                   AS "MainFlag"            -- 主要記號 VARCHAR2 1 (Y:新撥款對到舊撥款最早的一筆 )
          ,NVL(S3."TRXDAT",0)             AS "AcDate"              -- 會計日期 DECIMAL 8 -- 新撥款序號在放款主檔的撥款日期 -- 2021-12-23 綺萍要求改為交易明細檔做撥款的會計日期
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
    LEFT JOIN TX ON S3."LMSACN" = S1."LMSACN"
                AND S3."LMSAPN" = S1."LMSAPN"
                AND S3."LMSASQ" = S1."LMSASQ"
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    MERGE INTO "AcLoanRenew" TARGET_TABLE
    USING (
      WITH NEW_NO AS (
        SELECT LMSACN
             , NEGNUM
             , LMSAPN
             , LMSASQ
        FROM LN$NODP
        WHERE CHGFLG = 'A' -- 新
      )
      , OLD_NO AS (
        SELECT LMSACN
             , NEGNUM
             , LMSAPN
             , LMSASQ
        FROM LN$NODP
        WHERE CHGFLG = 'B' -- 舊
      )
      , S1 AS (
        SELECT NEW_NO.LMSACN
             , NEW_NO.LMSAPN
             , NEW_NO.LMSASQ
             , OLD_NO.LMSAPN AS OLD_LMSAPN
             , OLD_NO.LMSASQ AS OLD_LMSASQ
             , ROW_NUMBER() 
               OVER (
                 PARTITION BY NEW_NO.LMSACN
                            , NEW_NO.LMSAPN
                            , NEW_NO.LMSASQ
                 ORDER BY OLD_NO.LMSAPN
                        , OLD_NO.LMSASQ
               ) AS "Seq"
        FROM NEW_NO
        LEFT JOIN OLD_NO ON OLD_NO.LMSACN = NEW_NO.LMSACN
                        AND OLD_NO.NEGNUM = NEW_NO.NEGNUM
        WHERE NVL(OLD_NO.LMSACN,0) != 0
      )
      SELECT S1."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 3
            ,S1."LMSAPN"                    AS "NewFacmNo"           -- 新額度編號 DECIMAL 3
            ,S1."LMSASQ"                    AS "NewBormNo"           -- 新撥款序號 DECIMAL 3
            ,S1."OLD_LMSAPN"                AS "OldFacmNo"           -- 舊額度編號 DECIMAL 6
            ,S1."OLD_LMSASQ"                AS "OldBormNo"           -- 舊撥款序號 DECIMAL 6
            ,'2'                            AS "RenewCode"           -- 展期記號 VARCHAR2 1 (1:一般 2:協議)
            ,CASE
              WHEN S1."Seq" = 1 -- 新撥款對到舊撥款 最早的一筆 為Y
              THEN 'Y'
            ELSE 'N' END                    AS "MainFlag"            -- 主要記號 VARCHAR2 1 (Y:新撥款對到舊撥款最早的一筆 )
            ,NVL(S3."LMSLLD",0)             AS "AcDate"              -- 會計日期 DECIMAL 8 -- 新撥款序號在放款主檔的撥款日期
            ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
            ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
            ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
            ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
      FROM  S1
      LEFT JOIN LA$LMSP S3 ON S3.LMSACN = S1.LMSACN
                          AND S3.LMSAPN = S1.LMSAPN
                          AND S3.LMSASQ = S1.LMSASQ
    ) SOURCE_TABLE
    ON (
      TARGET_TABLE."CustNo" = SOURCE_TABLE."CustNo"
      AND TARGET_TABLE."NewFacmNo" = SOURCE_TABLE."NewFacmNo"
      AND TARGET_TABLE."NewBormNo" = SOURCE_TABLE."NewBormNo"
      AND TARGET_TABLE."OldFacmNo" = SOURCE_TABLE."OldFacmNo"
      AND TARGET_TABLE."OldBormNo" = SOURCE_TABLE."OldBormNo"
    )
    WHEN MATCHED THEN UPDATE
    SET "RenewCode" = '2'
    WHEN NOT MATCHED THEN INSERT (
        "CustNo"          -- 戶號 DECIMAL 3
      , "NewFacmNo"       -- 新額度編號 DECIMAL 3
      , "NewBormNo"       -- 新撥款序號 DECIMAL 3
      , "OldFacmNo"       -- 舊額度編號 DECIMAL 6
      , "OldBormNo"       -- 舊撥款序號 DECIMAL 6
      , "RenewCode"       -- 展期記號 VARCHAR2 1 (1:一般 2:協議)
      , "MainFlag"        -- 主要記號 VARCHAR2 1 (Y:新撥款對到舊撥款最早的一筆 )
      , "AcDate"          -- 會計日期 DECIMAL 8 -- 新撥款序號在放款主檔的撥款日期
      , "CreateEmpNo"     -- 建檔人員 VARCHAR2 6 
      , "CreateDate"      -- 建檔日期時間 DATE  
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
      , "LastUpdate"      -- 最後更新日期時間 DATE  
    ) VALUES (
        SOURCE_TABLE."CustNo"          -- 戶號 DECIMAL 3
      , SOURCE_TABLE."NewFacmNo"       -- 新額度編號 DECIMAL 3
      , SOURCE_TABLE."NewBormNo"       -- 新撥款序號 DECIMAL 3
      , SOURCE_TABLE."OldFacmNo"       -- 舊額度編號 DECIMAL 6
      , SOURCE_TABLE."OldBormNo"       -- 舊撥款序號 DECIMAL 6
      , SOURCE_TABLE."RenewCode"       -- 展期記號 VARCHAR2 1 (1:一般 2:協議)
      , SOURCE_TABLE."MainFlag"        -- 主要記號 VARCHAR2 1 (Y:新撥款對到舊撥款最早的一筆 )
      , SOURCE_TABLE."AcDate"          -- 會計日期 DECIMAL 8 -- 新撥款序號在放款主檔的撥款日期
      , SOURCE_TABLE."CreateEmpNo"     -- 建檔人員 VARCHAR2 6 
      , SOURCE_TABLE."CreateDate"      -- 建檔日期時間 DATE  
      , SOURCE_TABLE."LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
      , SOURCE_TABLE."LastUpdate"      -- 最後更新日期時間 DATE  
    )
    ;

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
