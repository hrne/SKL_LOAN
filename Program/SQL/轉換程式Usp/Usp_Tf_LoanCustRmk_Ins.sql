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
    INSERT INTO "LoanCustRmk"
    SELECT S1."LMSACN"                    AS "CustNo"              -- 借款人戶號 DECIMAL 7 
         , S1."TRXDAT"                    AS "AcDate"              -- 會計日期 DECIMALD 8
          ,S1."DOCSEQ"                    AS "RmkNo"               -- 備忘錄序號 DECIMAL 3 
          ,"CustMain"."CustUKey"          AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32 
          ,S1."DOCTXT"                    AS "RmkDesc"             -- 備忘錄說明 NVARCHAR2 120 
          ,CASE
             WHEN S1."TRXDAT" > 0
             THEN TO_DATE(S1."TRXDAT",'YYYYMMDD')
           ELSE JOB_START_TIME
           END                            AS "CreateDate"          -- 建檔日期時間 DATE  
          ,NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,CASE
             WHEN S1."TRXDAT" > 0
             THEN TO_DATE(S1."TRXDAT",'YYYYMMDD')
           ELSE JOB_START_TIME
           END                            AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,NVL(AEM1."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "DAT_LNDOCP" S1
    LEFT JOIN "CustMain" on "CustMain"."CustNo" = S1."LMSACN"
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = S1."TRXMEM"
    WHERE S1."LMSACN" > 0
      AND NVL("CustMain"."CustUKey",' ') <> ' '
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
