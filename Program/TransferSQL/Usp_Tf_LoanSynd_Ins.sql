--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanSynd_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_LoanSynd_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanSynd" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanSynd" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "LoanSynd" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "LoanSynd"
    SELECT ROW_NUMBER() OVER (PARTITION BY S1."LMSACN" ORDER BY S1."LMSACN")
                                          AS "SyndNo"              -- 聯貸案序號 DECIMAL 3 0
          ,''                             AS "LeadingBank"         -- 主辦行 VARCHAR2 7 0
          ,''                             AS "AgentBank"           -- 代理行 VARCHAR2 7 0
          ,0                              AS "SigningDate"         -- 簽約日 DECIMALD 8 0
          ,''                             AS "SyndTypeCodeFlag"    -- 國內或國際聯貸 VARCHAR2 1
          ,0                              AS "PartRate"            -- 參貸費率 DECIMAL 16 2
          ,''                             AS "CurrencyCode"        -- 幣別 VARCHAR2 3 0
          ,0                              AS "SyndAmt"             -- 聯貸總金額 DECIMAL 16 2
          ,0                              AS "PartAmt"             -- 參貸金額 DECIMAL 16 2
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "TB$ENTP" S1
    LEFT JOIN "CustMain" S2 ON S2."CustNo" = S1."LMSACN"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanSynd_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
