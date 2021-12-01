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
--     INSERT INTO "BankAuthAct"
--     SELECT S1."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 
--           ,S1."LMSAPN"                    AS "FacmNo"              -- 額度 DECIMAL 3 
--           ,'01'                           AS "AuthType"            -- 授權類別 VARCHAR2 2 
--           ,CASE
--              WHEN S2."LMSPBK" = '1' THEN '812'
--              WHEN S2."LMSPBK" = '2' THEN '006'
--              WHEN S2."LMSPBK" = '3' THEN '700'
--              WHEN S2."LMSPBK" = '4' THEN '103'
--            ELSE '000' END                 AS "RepayBank"           -- 扣款銀行 VARCHAR2 3 
--           ,''                             AS "PostDepCode"         -- 郵局存款別 VARCHAR2 1 
--           ,S2."LMSPCN"                    AS "RepayAcct"           -- 扣款帳號 VARCHAR2 14 
--           ,CASE
--              WHEN NVL(S2."ACHSTS",' ') = ' ' THEN '0'
--              WHEN NVL(S2."ACHSTS",' ') = 'D' THEN '1'
--            ELSE '9' END                   AS "Status"              -- 狀態碼 VARCHAR2 1 
--           ,S2."ACHLAMT"                   AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14 
--           ,LPAD(TO_CHAR(ROW_NUMBER() OVER (PARTITION BY S1."LMSACN"
--                                            ORDER BY S1."LMSAPN")),2,'0')
--                                           AS "AcctSeq"             -- 帳號碼 VARCHAR2 2 
--           ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
--           ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
--           ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
--           ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
--     FROM (SELECT "AH$ACHP"."LMSACN"
--                 ,"AH$ACHP"."LMSAPN"
--                 ,MAX("AH$ACHP"."CRTDTM") AS "CRTDTM"
--           FROM "AH$ACHP"
--           GROUP BY "AH$ACHP"."LMSACN"
--                   ,"AH$ACHP"."LMSAPN" ) S1
--     LEFT JOIN "AH$ACHP" S2 ON S2."LMSACN" = S1."LMSACN"
--                           AND S2."LMSAPN" = S1."LMSAPN"
--                           AND S2."CRTDTM" = S1."CRTDTM"
--     ;

    -- 寫入資料
--     INSERT INTO "BankAuthAct"
--     SELECT S1."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 
--           ,0                              AS "FacmNo"              -- 額度 DECIMAL 3 
--           ,S1."POATYP"                    AS "AuthType"            -- 授權類別 VARCHAR2 2 
--           ,'700'                          AS "RepayBank"           -- 扣款銀行 VARCHAR2 3 
--           ,S2."POSCDE"                    AS "PostDepCode"         -- 郵局存款別 VARCHAR2 1 
--           ,S1."LMSPCN"                    AS "RepayAcct"           -- 扣款帳號 VARCHAR2 14 
--           ,CASE
--              WHEN NVL(S2."POASTS",' ') = ' ' THEN '0' -- 成功
--              WHEN NVL(S2."POASTS",' ') = 'D' THEN '1' -- 刪除
--            ELSE '9' END                   AS "Status"              -- 狀態碼 VARCHAR2 1 
--           ,S2."POLAMT"                    AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14 
--           ,LPAD(TO_CHAR(ROW_NUMBER() OVER (PARTITION BY S1."LMSACN"
--                                            ORDER BY S1."LMSPCN")),2,'0')
--                                           AS "AcctSeq"             -- 帳號碼 VARCHAR2 2 
--           ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
--           ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
--           ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
--           ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
--     FROM (SELECT "PO$AADP"."LMSACN"
--                 ,"PO$AADP"."POATYP"
--                 ,MIN("PO$AADP"."LMSPCN") AS "LMSPCN"
--           FROM "PO$AADP"
--           GROUP BY "PO$AADP"."LMSACN"
--                   ,"PO$AADP"."POATYP") S1
--     LEFT JOIN "PO$AADP" S2 ON S2."LMSACN" = S1."LMSACN"
--                           AND S2."POATYP" = S1."POATYP"
--                           AND S2."LMSPCN" = S1."LMSPCN"
--     ;

    INSERT INTO "BankAuthAct"
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
          ,'0'                            AS "Status"              -- 狀態碼 VARCHAR2 1 
          ,0                              AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14 
          ,' '                            AS "AcctSeq"             -- 帳號碼 VARCHAR2 2 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$APLP" S1
    WHERE S1."LMSPYS" = 2
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    INSERT INTO "BankAuthAct"
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
          ,'0'                            AS "Status"              -- 狀態碼 VARCHAR2 1 
          ,0                              AS "LimitAmt"            -- 每筆扣款限額 DECIMAL 14 
          ,' '                            AS "AcctSeq"             -- 帳號碼 VARCHAR2 2 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$APLP" S1
    WHERE S1."LMSPBK" = '3'
      AND S1."LMSPYS" = 2
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_BankAuthAct_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
