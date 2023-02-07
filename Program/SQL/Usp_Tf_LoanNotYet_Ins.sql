--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanNotYet_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_LoanNotYet_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanNotYet" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanNotYet" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanNotYet" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "LoanNotYet" (
        "CustNo"              -- 借款人戶號 DECIMAL 7 
      , "FacmNo"              -- 額度編號 DECIMAL 3 
      , "NotYetCode"          -- 未齊件代碼 VARCHAR2 2 
      , "YetDate"             -- 齊件日期 DECIMALD 8 
      , "CloseDate"           -- 銷號日期 DECIMALD 8 
      , "ReMark"              -- 備註 NVARCHAR2 80
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT "LA$SDOP"."LMSACN"             AS "CustNo"              -- 借款人戶號 DECIMAL 7 
          ,"LA$SDOP"."LMSAPN"             AS "FacmNo"              -- 額度編號 DECIMAL 3 
          ,LPAD("LA$SDOP"."DOTSID",2,'0') AS "NotYetCode"          -- 未齊件代碼 VARCHAR2 2 
          ,0                              AS "YetDate"             -- 齊件日期 DECIMALD 8 
          ,0                              AS "CloseDate"           -- 銷號日期 DECIMALD 8 
          ,''                             AS "ReMark"              -- 備註 NVARCHAR2 80
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$SDOP"
    LEFT JOIN "TB$DOTP" ON "TB$DOTP"."DOTSID" = "LA$SDOP"."DOTSID"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanNotYet_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
