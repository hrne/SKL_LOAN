--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MlaundryRecord_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_MlaundryRecord_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "MlaundryRecord" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "MlaundryRecord" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "MlaundryRecord" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "MlaundryRecord"
    SELECT "MlaundryRecord_SEQ".nextval   AS "LogNo"
          ,IV."IVWDAT"                    AS "RecordDate"          -- 訪談日期 Decimald 8 0
          ,IV."LMSACN"                    AS "CustNo"              -- 戶號 Decimal 7 0
          ,0                              AS "FacmNo"              -- 額度編號 Decimal 3 0
          ,0                              AS "BormNo"              -- 撥款序號 Decimal 3 0
          ,IV."RTNADT"                    AS "RepayDate"           -- 預定還款日期 Decimald 8 0
          ,0                              AS "ActualRepayDate"     -- 實際還款日期 Decimald 8 0
          ,IV."RTNAMT"                    AS "RepayAmt"            -- 還款金額 Decimal 16 2
          ,0                              AS "ActualRepayAmt"      -- 實際還款金額 Decimal 16 2
          ,IV."IVWOCD"                    AS "Career"              -- 職業別 nvarchar2 20 0
          ,IV."AMLYIN"                    AS "Income"              -- 年收入(萬) nvarchar2 30 0
          ,IV."AMLRSN"                    AS "RepaySource"         -- 還款來源 Decimal 2 0
          ,IV."CPSBNK"                    AS "RepayBank"           -- 代償銀行 nvarchar2 10 0
          ,IV."AMLRSO"                    AS "Description"         -- 其他說明 nvarchar2 60 0
          ,CASE
             WHEN IV."IVWDAT" > 0
             THEN TO_DATE(IV."IVWDAT",'YYYYMMDD')
           ELSE JOB_START_TIME
           END                            AS "CreateDate"          -- 建檔日期時間 DATE 8 0
          ,NVL(AEM1."EmpNo",'999999')     AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,CASE
             WHEN IV."CHGDAT" > 0
             THEN TO_DATE(IV."CHGDAT",'YYYYMMDD')
           ELSE JOB_START_TIME
           END                            AS "LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,NVL(AEM2."EmpNo",'999999')     AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LN$IVWP" IV 
    LEFT JOIN "As400EmpNoMapping" AEM1 ON AEM1."As400TellerNo" = IV."CRTEMP"
    LEFT JOIN "As400EmpNoMapping" AEM2 ON AEM2."As400TellerNo" = IV."CHGEMP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MlaundryRecord_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
