--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyLM014B_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_MonthlyLM014B_Ins" 
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
    DELETE FROM "MonthlyLM014B";

    -- 寫入資料
    INSERT INTO "MonthlyLM014B"
    SELECT "LA$GSTP"."ADTYMT"             AS "DataYM"              -- 資料年月 DECIMAL 6 0
          ,"LA$GSTP"."ACTACT"             AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
          ,"LA$GSTP"."ACNTYP"             AS "AccountType"         -- 帳戶類別 DECIMAL 1 0
          ,CASE
             WHEN "LA$GSTP"."ACTFSC" = 'A' THEN '201' -- 利變A
             WHEN NVL("LA$GSTP"."ACTFSC",' ') <> ' ' THEN "LA$GSTP"."ACTFSC"
           ELSE '   ' END             AS "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
          ,"LA$GSTP"."ACNTY4"             AS "EntCode"             -- 企金別 VARCHAR2 1 0
          ,"LA$GSTP"."ACNTY2"             AS "RelsFlag"            -- 關係人別 VARCHAR2 1 0
          ,"LA$GSTP"."ACNTY3"             AS "ClFlag"              -- 抵押品別 VARCHAR2 1 0
          ,"LA$GSTP"."GSTINS"             AS "MonthLoanIns"        -- 本月利息收入 DECIMAL 16 2
          ,"LA$GSTP"."GSTLBL"             AS "MonthLoanBal"        -- 本月月底餘額 DECIMAL 16 2
          ,"LA$GSTP"."GSTIN2"             AS "YearLoanIns"         -- 累計利息收入 DECIMAL 16 2
          ,"LA$GSTP"."GSTLB2"             AS "YearLoanBal"         -- 每月平均放款餘額 DECIMAL 16 2
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$GSTP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MonthlyLM014B_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
