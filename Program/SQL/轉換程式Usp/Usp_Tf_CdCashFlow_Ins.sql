--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdCashFlow_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_CdCashFlow_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CdCashFlow" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdCashFlow" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CdCashFlow" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CdCashFlow" (
        "BranchNo"             -- 單位別 VARCHAR2 4
      , "DataYearMonth"        -- 年月份 DECIMAL 6 
      , "TenDayPeriods"        -- 旬別 DECIMAL 1
      , "InterestIncome"       -- 利息收入 DECIMAL 16 2
      , "PrincipalAmortizeAmt" -- 本金攤還金額 DECIMAL 16 2
      , "PrepaymentAmt"        -- 提前還款金額 DECIMAL 16 2
      , "DuePaymentAmt"        -- 到期清償金額 DECIMAL 16 2
      , "ExtendAmt"            -- 展期金額 DECIMAL 16 2
      , "LoanAmt"              -- 貸放金額 DECIMAL 16 2
      , "CreateDate"           -- 建檔日期時間 DATE 0 
      , "CreateEmpNo"          -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"           -- 最後更新日期時間 DATE 0 
      , "LastUpdateEmpNo"      -- 最後更新人員 VARCHAR2 6 
    )
    SELECT LPAD(C.CUSBRH,4,'0')           AS "BranchNo"             -- 單位別 VARCHAR2 4
         , C.ADTYMT                       AS "DataYearMonth"        -- 年月份 DECIMAL 6 
         , C.CSTPRD                       AS "TenDayPeriods"        -- 旬別 DECIMAL 1
         , C.CSTINS                       AS "InterestIncome"       -- 利息收入 DECIMAL 16 2
         , C.CSTRA1                       AS "PrincipalAmortizeAmt" -- 本金攤還金額 DECIMAL 16 2
         , C.CSTRA2                       AS "PrepaymentAmt"        -- 提前還款金額 DECIMAL 16 2
         , C.CSTRA3                       AS "DuePaymentAmt"        -- 到期清償金額 DECIMAL 16 2
         , C.CSTETA                       AS "ExtendAmt"            -- 展期金額 DECIMAL 16 2
         , C.CSTLAM                       AS "LoanAmt"              -- 貸放金額 DECIMAL 16 2
         , JOB_START_TIME                 AS "CreateDate"           -- 建檔日期時間 DATE 0 
         , '999999'                       AS "CreateEmpNo"          -- 建檔人員 VARCHAR2 6 
         , JOB_START_TIME                 AS "LastUpdate"           -- 最後更新日期時間 DATE 0 
         , '999999'                       AS "LastUpdateEmpNo"      -- 最後更新人員 VARCHAR2 6 
    FROM LA$CSTP C 
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdCashFlow_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
