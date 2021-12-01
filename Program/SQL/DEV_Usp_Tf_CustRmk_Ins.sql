--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CustRmk_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_CustRmk_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CustRmk" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustRmk" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CustRmk" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CustRmk"
    SELECT S1."LMSACN"                    AS "CustNo"              -- 借款人戶號 DECIMAL 7 
          ,ROW_NUMBER() OVER (PARTITION BY S1."LMSACN" ORDER BY S1."TRXTDT")
                                          AS "RmkNo"               -- 備忘錄序號 DECIMAL 3 
          ,"CustMain"."CustUKey"          AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32 
          ,''                             AS "RmkCode"             -- 備忘錄代碼 VARCHAR2 2 
          ,S1."DOCTXT"                    AS "RmkDesc"             -- 備忘錄說明 NVARCHAR2 120 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM (  SELECT "LMSACN"
                  ,"TRXTDT"
                  ,"DOCSEQ"
                  ,TO_CHAR("TRXTDT") || "DOCTXT" AS "DOCTXT"
            FROM "LNREMP"
            UNION ALL 
            SELECT "LMSACN"
                  ,"TRXDAT" AS "TRXTDT"
                  ,"DOCSEQ"
                  ,TO_CHAR("TRXDAT") || "DOCTXT" AS "DOCTXT"
            FROM "DAT_LNDOCP"
        ) S1
    LEFT JOIN "CustMain" on "CustMain"."CustNo" = S1."LMSACN"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CustRmk_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
