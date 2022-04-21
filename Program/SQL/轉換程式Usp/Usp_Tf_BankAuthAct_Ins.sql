--------------------------------------------------------
--  DDL for Procedure Usp_Tf_BankAuthAct_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_BankAuthAct_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "BankAuthAct" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "BankAuthAct" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "BankAuthAct" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "BankAuthAct"
    WITH haveRecordData AS (
      -- 找出在授權紀錄檔有資料的戶號跟帳號
      SELECT DISTINCT
             "LMSACN"
           , "LMSPCN"
      FROM "AH$ACHP"
      WHERE NVL("ATHCOD",' ') = ' '
      UNION
      SELECT DISTINCT
             "LMSACN"
           , "LMSPCN"
      FROM "AH$ACRP"
      UNION
      SELECT DISTINCT
             "LMSACN"
           , "LMSPCN"
      FROM "PO$AARP"
    )
    SELECT S1."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 
          ,S1."LMSAPN"                    AS "FacmNo"              -- 額度 DECIMAL 3 
          ,CASE
             WHEN S1."LMSPBK" = '3' THEN '01'
           ELSE '00' END                  AS "AuthType"            -- 授權類別 VARCHAR2 2 
          ,CASE
             WHEN S1."LMSPBK" = '1' THEN '812'
             WHEN S1."LMSPBK" = '2' THEN '006'
             WHEN S1."LMSPBK" = '3' THEN '700'
             WHEN S1."LMSPBK" = '4' THEN '103'
           ELSE '000' END                 AS "RepayBank"           -- 扣款銀行 VARCHAR2 3 
          ,S1."POSCDE"                    AS "PostDepCode"         -- 郵局存款別 VARCHAR2 1 
          ,LPAD(S1."LMSPCN",14,'0')       AS "RepayAcct"           -- 扣款帳號 VARCHAR2 14 
          ,CASE
             WHEN NVL(S2."LMSACN",0) = 0 -- 在授權紀錄檔沒資料
             THEN ' ' -- 空白:未授權
           ELSE '0' END                   AS "Status"              -- 狀態碼 VARCHAR2 1 
          ,0                              AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14 
          ,' '                            AS "AcctSeq"             -- 帳號碼 VARCHAR2 2 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$APLP" S1
    LEFT JOIN haveRecordData S2 ON S2."LMSACN" = S1."LMSACN"
                               AND S2."LMSPCN" = S1."LMSPCN"
    WHERE S1."LMSPYS" = 2
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    INSERT INTO "BankAuthAct"
    WITH haveRecordData AS (
      -- 找出在授權紀錄檔有資料的戶號跟帳號
      SELECT DISTINCT
             "LMSACN"
           , "LMSPCN"
      FROM "AH$ACHP"
      WHERE NVL("ATHCOD",' ') = ' '
      UNION
      SELECT DISTINCT
             "LMSACN"
           , "LMSPCN"
      FROM "AH$ACRP"
      UNION
      SELECT DISTINCT
             "LMSACN"
           , "LMSPCN"
      FROM "PO$AARP"
    )
    SELECT S1."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 
          ,S1."LMSAPN"                    AS "FacmNo"              -- 額度 DECIMAL 3 
          ,'02'                           AS "AuthType"            -- 授權類別 VARCHAR2 2 
          ,CASE
             WHEN S1."LMSPBK" = '1' THEN '812'
             WHEN S1."LMSPBK" = '2' THEN '006'
             WHEN S1."LMSPBK" = '3' THEN '700'
             WHEN S1."LMSPBK" = '4' THEN '103'
           ELSE '000' END                 AS "RepayBank"           -- 扣款銀行 VARCHAR2 3 
          ,S1."POSCDE"                    AS "PostDepCode"         -- 郵局存款別 VARCHAR2 1 
          ,LPAD(S1."LMSPCN",14,'0')       AS "RepayAcct"           -- 扣款帳號 VARCHAR2 14 
          ,CASE
             WHEN NVL(S2."LMSACN",0) = 0 -- 在授權紀錄檔沒資料
             THEN ' ' -- 空白:未授權
           ELSE '0' END                   AS "Status"              -- 狀態碼 VARCHAR2 1 
          ,0                              AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14 
          ,' '                            AS "AcctSeq"             -- 帳號碼 VARCHAR2 2 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$APLP" S1
    LEFT JOIN haveRecordData S2 ON S2."LMSACN" = S1."LMSACN"
                               AND S2."LMSPCN" = S1."LMSPCN"
    WHERE S1."LMSPBK" = '3'
      AND S1."LMSPYS" = 2
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    MERGE INTO "BankAuthAct" T
    USING (
      SELECT "LMSACN"
           , "LMSPCN"
           , "POSCDE"
           , ROW_NUMBER()
             OVER (
               PARTITION BY "LMSACN"
                          , "LMSPCN"
               ORDER BY "LMSAPN" DESC
             ) AS "Seq"
      FROM (
        SELECT "LMSACN"
             , "LMSPCN"
             , "POSCDE"
             , "LMSAPN"
        FROM "LA$APLP"
        WHERE "LMSPBK" = '3'
          AND "LMSPYS" = 2
          AND "POSCDE" IS NOT NULL
        UNION
        SELECT "LMSACN"
             , "LMSPCN"
             , "POSCDE"
             , 1 AS "LMSAPN"
        FROM "PO$AARP"
      ) S
    ) S
    ON (
      S."Seq" = 1
      AND T."RepayBank" = '700'
      AND S."LMSACN" = T."CustNo"
      AND LPAD(S."LMSPCN",14,'0') = T."RepayAcct"
    )
    WHEN MATCHED THEN UPDATE SET
    T."PostDepCode" = CASE
                        WHEN NVL(T."PostDepCode",' ') = ' '
                        THEN S."POSCDE"
                      ELSE T."PostDepCode" END
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_BankAuthAct_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
