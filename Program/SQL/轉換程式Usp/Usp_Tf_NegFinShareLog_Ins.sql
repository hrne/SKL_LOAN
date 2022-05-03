--------------------------------------------------------
--  DDL for Procedure Usp_Tf_NegFinShareLog_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_NegFinShareLog_Ins" 
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
    DELETE FROM "NegFinShareLog";

    -- 寫入資料
    INSERT INTO "NegFinShareLog"
    SELECT "CustNo"              -- 債務人戶號 DECIMAL 7 0
          ,"CaseSeq"             -- 案件序號 DECIMAL 3 0
          ,1 AS "Seq"                 -- 歷程序號 DECIMAL 3
          ,"FinCode"             -- 債權機構 VARCHAR2 8 0
          ,"ContractAmt"         -- 簽約金額 DECIMAL 16 2
          ,"AmtRatio"            -- 債權比例%  DECIMAL 5 2
          ,"DueAmt"              -- 期款 DECIMAL 16 2
          ,"CancelDate"          -- 註銷日期 DecimalD 8 0
          ,"CancelAmt"           -- 註銷本金 DECIMAL 16 2
          ,"CreateDate"          -- 建檔日期時間 DATE 8 0
          ,"CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,"LastUpdate"          -- 最後更新日期時間 DATE 8 0
          ,"LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "NegFinShare"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_NegFinShareLog_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
