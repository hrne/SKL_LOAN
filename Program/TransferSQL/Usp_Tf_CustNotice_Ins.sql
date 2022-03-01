--------------------------------------------------------
--  DDL for Procedure Usp_Tf_CustNotice_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_CustNotice_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "CustNotice" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustNotice" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "CustNotice" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "CustNotice"
    SELECT DISTINCT
           NP."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 
          ,FAC."FacmNo"                   AS "FacmNo"              -- 額度編號 DECIMAL 3 
          ,RCM."NewReportCode"            AS "FormNo"              -- 報表代號 VARCHAR2 10 
          ,NP."ACTUSE"                    AS "PaperNotice"         -- 書面通知與否 VARCHAR2 1 
          ,'Y'                            AS "MsgNotice"           -- 簡訊發送與否 VARCHAR2 1 
          ,'Y'                            AS "EmailNotice"         -- 電子郵件發送與否 VARCHAR2 1 
          ,0                              AS "ApplyDate"           -- 申請日期 decimald 8 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LN$NPTP" NP
    LEFT JOIN "FacMain" FAC ON FAC."CustNo" = NP."LMSACN"
    LEFT JOIN "ReportCodeMapping" RCM ON RCM."OriReportCode" = NP."F23FMT"
    WHERE NVL(FAC."CustNo",0) <> 0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_CustNotice_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
