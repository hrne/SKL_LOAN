--------------------------------------------------------
--  DDL for Procedure Usp_Tf_YearlyHouseLoanInt_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_YearlyHouseLoanInt_Ins" 
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
    DELETE FROM "YearlyHouseLoanInt";

    -- 寫入資料
    INSERT INTO "YearlyHouseLoanInt"
    SELECT "LA$W24P"."ADTYMT"             AS "YearMonth"           -- 資料年月 DECIMAL 6 0
          ,"LA$W24P"."LMSACN"             AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,"LA$W24P"."LMSAPN"             AS "FacmNo"              -- 額度編號 DECIMAL 3 0
          ,"LA$W24P"."W24USG"             AS "UsageCode"           -- 資金用途別 VARCHAR2 2 0
          ,"LA$W24P"."ACTACT"             AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
          ,"LA$W24P"."LMSPYS"             AS "RepayCode"           -- 繳款方式 VARCHAR2 3 0
          ,"LA$W24P"."LMSFLA"             AS "LoanAmt"             -- 撥款金額 VARCHAR2 16 2
          ,"LA$W24P"."LMSLBL"             AS "LoanBal"             -- 放款餘額 DECIMAL 16 2
          ,"LA$W24P"."LMSLLD"             AS "FirstDrawdownDate"   -- 初貸日 DECIMALD 8 0
          ,"LA$W24P"."LMSDLD"             AS "MaturityDate"        -- 到期日 DECIMALD 8 0
          ,"LA$W24P"."TRXAMT"             AS "YearlyInt"           -- 年度繳息金額 DECIMAL 16 2
          ,"LA$W24P"."HGTGTD"             AS "HouseBuyDate"        -- 房屋取得日期 decimald  
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2  
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$W24P"
    WHERE "LA$W24P"."ADTYMT" >= 200701
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_YearlyHouseLoanInt_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
