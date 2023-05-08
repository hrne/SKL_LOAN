--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanCustRmk_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_LoanCustRmk_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanCustRmk" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanCustRmk" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanCustRmk" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "LoanCustRmk" (
        "CustNo" -- 借款人戶號 DECIMAL 7 
      , "AcDate" -- 會計日期 DECIMALD 8
      , "RmkNo" -- 備忘錄序號 DECIMAL 3 
      , "RmkCode" -- 備忘錄代碼 VARCHAR2 3
      , "RmkDesc" -- 備忘錄說明 NVARCHAR2 120 
      , "CreateDate" -- 建檔日期時間 DATE  
      , "CreateEmpNo" -- 建檔人員 VARCHAR2 6 
      , "LastUpdate" -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6 
    )
    SELECT S1."LMSACN"                    AS "CustNo"              -- 借款人戶號 DECIMAL 7 
         , CASE S1."TRXDAT"
             WHEN 50150309 THEN 20150309
             WHEN 39121026 THEN 20011026
             WHEN 30160317 THEN 20160317
             WHEN 30140506 THEN 20140506
             WHEN 30121003 THEN 20121003
             WHEN 30110616 THEN 20210616
             WHEN 29710913 THEN 20170913
             WHEN 29411113 THEN 20141113
             WHEN 29111012 THEN 20111012
             WHEN 28990930 THEN 20090930
             WHEN 25191130 THEN 20041130
           ELSE S1."TRXDAT" END           AS "AcDate"              -- 會計日期 DECIMALD 8
         , S1."DOCSEQ"                    AS "RmkNo"               -- 備忘錄序號 DECIMAL 3 
         , ''                             AS "RmkCode"             -- 備忘錄代碼 VARCHAR2 3
         , S1."DOCTXT"                    AS "RmkDesc"             -- 備忘錄說明 NVARCHAR2 120 
         , CASE
             WHEN S1."TRXDAT" = 50150309
             THEN TO_DATE(20150309,'YYYYMMDD')
             WHEN S1."TRXDAT" = 39121026
             THEN TO_DATE(20011026,'YYYYMMDD')
             WHEN S1."TRXDAT" = 30160317
             THEN TO_DATE(20160317,'YYYYMMDD')
             WHEN S1."TRXDAT" = 30140506
             THEN TO_DATE(20140506,'YYYYMMDD')
             WHEN S1."TRXDAT" = 30121003
             THEN TO_DATE(20121003,'YYYYMMDD')
             WHEN S1."TRXDAT" = 30110616
             THEN TO_DATE(20210616,'YYYYMMDD')
             WHEN S1."TRXDAT" = 29710913
             THEN TO_DATE(20170913,'YYYYMMDD')
             WHEN S1."TRXDAT" = 29411113
             THEN TO_DATE(20141113,'YYYYMMDD')
             WHEN S1."TRXDAT" = 29111012
             THEN TO_DATE(20111012,'YYYYMMDD')
             WHEN S1."TRXDAT" = 28990930
             THEN TO_DATE(20090930,'YYYYMMDD')
             WHEN S1."TRXDAT" = 25191130
             THEN TO_DATE(20041130,'YYYYMMDD')
             WHEN S1."TRXDAT" > 0
             THEN TO_DATE(S1."TRXDAT",'YYYYMMDD')
           ELSE JOB_START_TIME
           END                            AS "CreateDate"          -- 建檔日期時間 DATE  
         , NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
         , CASE
             WHEN S1."TRXDAT" > 0
             THEN TO_DATE(S1."TRXDAT",'YYYYMMDD')
           ELSE JOB_START_TIME
           END                            AS "LastUpdate"          -- 最後更新日期時間 DATE  
         , NVL(AEM1."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "DAT_LNDOCP" S1
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = S1."TRXMEM"
    WHERE S1.TRXDAT NOT IN (
      69980123,
      40130515,
      30120503,
      30110614,
      30080711,
      30080630,
      30080307
    )
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanCustRmk_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
