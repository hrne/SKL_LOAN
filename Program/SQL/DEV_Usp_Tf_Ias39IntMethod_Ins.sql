--------------------------------------------------------
--  DDL for Procedure Usp_Tf_Ias39IntMethod_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_Ias39IntMethod_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "Ias39IntMethod" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "Ias39IntMethod" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "Ias39IntMethod" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "Ias39IntMethod"
    SELECT "LN$LBVP"."ADTYMT"             AS "YearMonth"           -- 資料年月 DECIMAL 6 0
          ,"LN$LBVP"."LMSACN"             AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,"LN$LBVP"."LMSAPN"             AS "FacmNo"              -- 額度編號 DECIMAL 3 0
          ,"LN$LBVP"."LMSASQ"             AS "BormNo"              -- 撥款序號 DECIMAL 3 0
          ,"LN$LBVP"."TRMLBL"             AS "Principal"           -- 本期本金餘額 DECIMAL 16 2
          ,"LN$LBVP"."TRMLBV"             AS "BookValue"           -- 本期帳面價值 DECIMAL 16 2
          ,"LN$LBVP"."TRMASV"             AS "AccumDPAmortized"    -- 本期累應攤銷折溢價 DECIMAL 16 2
          ,"LN$LBVP"."TRMANV"             AS "AccumDPunAmortized"  -- 本期累未攤銷折溢價 DECIMAL 16 2
          ,"LN$LBVP"."TRMUSV"             AS "DPAmortized"         -- 本期折溢價攤銷數 DECIMAL 16 2
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LN$LBVP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_Ias39IntMethod_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
