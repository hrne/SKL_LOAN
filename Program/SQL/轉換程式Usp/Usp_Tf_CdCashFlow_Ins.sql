--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CdCashFlow_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_CdCashFlow_Ins" 
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
    INSERT INTO "CdCashFlow"
    SELECT S1."ADTYMT"                    AS "DataYearMonth"       -- 年月份 DECIMAL 6 
          ,S2."CSTINS"                    AS "InterestIncome"      -- 利息收入 DECIMAL 16 2
          ,S2."CSTRA1"                    AS "PrincipalAmortizeAmt" -- 本金攤還金額 DECIMAL 16 2
          ,S2."CSTRA2"                    AS "PrepaymentAmt"       -- 提前還款金額 DECIMAL 16 2
          ,S2."CSTRA3"                    AS "DuePaymentAmt"       -- 到期清償金額 DECIMAL 16 2
          ,S2."CSTETA"                    AS "ExtendAmt"           -- 展期金額 DECIMAL 16 2
          ,S2."CSTLAM"                    AS "LoanAmt"             -- 貸放金額 DECIMAL 16 2
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM (SELECT "LA$CSTP"."ADTYMT"
                ,"LA$CSTP"."CSTPRD"
                ,ROW_NUMBER() OVER (PARTITION BY 0 ORDER BY "LA$CSTP"."ADTYMT" DESC,"LA$CSTP"."CSTPRD" DESC) AS "Seq"
          FROM "LA$CSTP"
         ) S1
    LEFT JOIN "LA$CSTP" S2 ON S2."ADTYMT" = S1."ADTYMT"
                          AND S2."CSTPRD" = S1."CSTPRD"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CdCashFlow_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
