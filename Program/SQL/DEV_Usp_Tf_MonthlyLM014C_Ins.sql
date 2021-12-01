--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyLM014C_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_MonthlyLM014C_Ins" 
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
    DELETE FROM "MonthlyLM014C";

    -- 寫入資料
    INSERT INTO "MonthlyLM014C"
    SELECT "LN$USTP"."ADTYMT"             AS "DataYM"              -- 資料年月 DECIMAL 6 0
          ,"LN$USTP"."ACTACT"             AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
          ,"LN$USTP"."ACNTYP"             AS "AccountType"         -- 帳戶類別 DECIMAL 1 0
          ,CASE
             WHEN "LN$USTP"."ACTFSC" = 'A' THEN '201' -- 利變A
             WHEN NVL("LN$USTP"."ACTFSC",' ') <> ' ' THEN "LN$USTP"."ACTFSC"
           ELSE '   ' END              AS "AcBookCode"          -- 帳冊別 VARCHAR2 3 0
          ,"LN$USTP"."ACNTY2"             AS "RelsFlag"            -- 關係人別 VARCHAR2 1 0
          ,"LN$USTP"."ACNTY3"             AS "ClFlag"              -- 抵押品別 VARCHAR2 1 0
          ,NVL("LN$USTP"."ACNTY5",' ')    AS "DepartmentCode"      -- 案件隸屬單位 VARCHAR2 1 0
          ,"LN$USTP"."GSTINS"             AS "MonthLoanIns"        -- 本月利息收入 DECIMAL 16 2
          ,"LN$USTP"."GSTLBL"             AS "MonthLoanBal"        -- 本月月底餘額 DECIMAL 16 2
          ,"LN$USTP"."GSTIN2"             AS "YearLoanIns"         -- 累計利息收入 DECIMAL 16 2
          ,"LN$USTP"."GSTLB2"             AS "YearLoanBal"         -- 每月平均放款餘額 DECIMAL 16 2
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LN$USTP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MonthlyLM014C_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
