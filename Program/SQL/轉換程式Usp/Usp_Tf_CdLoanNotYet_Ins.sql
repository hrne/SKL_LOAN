--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdLoanNotYet_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CdLoanNotYet_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CdLoanNotYet" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdLoanNotYet" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdLoanNotYet" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CdLoanNotYet"
    SELECT LPAD(S1."DOTSID",2,'0')        AS "NotYetCode"          -- 未齊件代碼 VARCHAR2 2
          ,S1."DOTDSC"                    AS "NotYetItem"          -- 未齊件說明 NVARCHAR2 40
          ,8                              AS "YetDays"             -- 齊件日期計算日 DECIMAL 3
          ,'Y'                            AS "Enable"              -- 啟用記號 VARCHAR2 1
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "TB$DOTP" S1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdLoanNotYet_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;





/
