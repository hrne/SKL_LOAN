--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyLM014A_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_MonthlyLM014A_Ins" 
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
    DELETE FROM "MonthlyLM014A";

    -- 寫入資料
    INSERT INTO "MonthlyLM014A"
    SELECT "LA$DSTP"."ADTYMT"             AS "DataYM"              -- 資料年月 DECIMAL 6 0
          ,"LA$DSTP"."ACTACT"             AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
          ,"LA$DSTP"."ACNTYP"             AS "AccountType"         -- 帳戶類別 DECIMAL 1 0
          ,CASE
             WHEN "LA$DSTP"."ACTFSC" = 'A'           THEN '201' -- 利變A
             WHEN NVL("LA$DSTP"."ACTFSC",' ') <> ' ' THEN "LA$DSTP"."ACTFSC"
           ELSE '   ' END                 AS "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
          ,"LA$DSTP"."DSTINS"             AS "MonthLoanIns"        -- 本月利息收入 DECIMAL 16 2
          ,"LA$DSTP"."DSTLBL"             AS "MonthLoanBal"        -- 本月月底餘額 DECIMAL 16 2
          ,"LA$DSTP"."DSTIN2"             AS "YearLoanIns"         -- 累計利息收入 DECIMAL 16 2
          ,"LA$DSTP"."DSTLB2"             AS "YearLoanBal"         -- 每月平均放款餘額 DECIMAL 16 2
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LA$DSTP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MonthlyLM014A_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
