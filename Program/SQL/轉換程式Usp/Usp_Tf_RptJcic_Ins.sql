--------------------------------------------------------
--  DDL for Procedure Usp_Tf_RptJcic_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_RptJcic_Ins" 
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
    DELETE FROM "RptJcic";

    -- 寫入資料
    INSERT INTO "RptJcic"
    SELECT "LN$JCICP"."CUSBRH"            AS "BranchNo"            -- 單位別 VARCHAR2 4 0
          ,"LN$JCICP"."LMSACN"            AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,"LN$JCICP"."LMSAPN"            AS "FacmNo"              -- 額度號碼 DECIMAL 3 0
          ,"LN$JCICP"."CUSNA1"            AS "JcicName"            -- Jcic名稱 NVARCHAR2 100 0
          ,"LN$JCICP"."LMSSTS"            AS "JcicStatus"          -- Jcic戶況 DECIMAL 1 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "LN$JCICP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_RptJcic_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
