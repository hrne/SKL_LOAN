--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanBorTx_Upd_LoanBorMain
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_LoanBorTx_Upd_LoanBorMain" 
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

    /* 取最大交易序號更新"LoanBorMain"."LastBorxNo" */
    MERGE INTO "LoanBorMain" T1
    USING (
      SELECT "CustNo"                     -- 借款人戶號 DECIMAL 7 
            ,"FacmNo"                     -- 額度編號 DECIMAL 3 
            ,"BormNo"                     -- 撥款序號 DECIMAL 3 
            ,MAX("BorxNo") AS "MaxBorxNo" -- 最大交易內容檔序號 DECIMAL 4 
      FROM "LoanBorTx" S1
      GROUP BY "CustNo"
              ,"FacmNo"
              ,"BormNo"
    ) S1 ON (    S1."CustNo" = T1."CustNo"
             AND S1."FacmNo" = T1."FacmNo"
             AND S1."BormNo" = T1."BormNo" )
    WHEN MATCHED THEN UPDATE SET
    T1."LastBorxNo" = S1."MaxBorxNo"
    ;

    /* 取最後帳務日更新"LoanBorMain"."AcDate" */
    MERGE INTO "LoanBorMain" T1
    USING (
      SELECT "CustNo"                     -- 借款人戶號 DECIMAL 7 
            ,"FacmNo"                     -- 額度編號 DECIMAL 3 
            ,"BormNo"                     -- 撥款序號 DECIMAL 3 
            ,MAX("AcDate") AS "AcDate"    -- 最後帳務日
      FROM "LoanBorTx" S1
      GROUP BY "CustNo"
              ,"FacmNo"
              ,"BormNo"
    ) S1 ON (    S1."CustNo" = T1."CustNo"
             AND S1."FacmNo" = T1."FacmNo"
             AND S1."BormNo" = T1."BormNo" )
    WHEN MATCHED THEN UPDATE SET
    T1."AcDate" = S1."AcDate"
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanBorTx_Upd_LoanBorMain',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
