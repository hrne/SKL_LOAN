--------------------------------------------------------
--  DDL for Procedure Usp_Tf_EmpDeductSchedule_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_EmpDeductSchedule_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "EmpDeductSchedule" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "EmpDeductSchedule" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "EmpDeductSchedule" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "EmpDeductSchedule"
    SELECT S1."YGYYMM"                    AS "WorkMonth"           -- 工作年月 DECIMAL 6 0
          ,S1."FLWCOD"                    AS "AgType1"             -- 流程/制度別 VARCHAR2 1 0
          ,S1."TRXIDT"                    AS "EntryDate"           -- 入賬日期 DECIMAL 8 0
          ,S1."TRXIDT"                    AS "MediaDate"           -- 媒體日期 DECIMAL 8 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM (SELECT "YGYYMM"
                ,"FLWCOD"
                ,"TRXIDT"
                ,"YGEPDT"
                ,ROW_NUMBER() OVER (PARTITION BY "YGYYMM","FLWCOD" ORDER BY "TRXIDT" DESC) AS "Seq"
          FROM "TBYGYMP") S1
    WHERE S1."Seq" = 1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_EmpDeductSchedule_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
