--------------------------------------------------------
--  DDL for Procedure Usp_Tf_RenewMapping_Upd_LoanBorMain
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_RenewMapping_Upd_LoanBorMain" 
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

    -- 寫入資料
    MERGE INTO "LoanBorMain" T0
    USING (SELECT DISTINCT
                  "CustNo"
                , "RenewFacmNo"
                , "RenewBormNo"
           FROM "RenewMapping"
           WHERE "CloseTotal" = "RenewAmt"
          ) S0
   ON (S0."CustNo" = T0."CustNo"
       AND S0."RenewFacmNo" = T0."FacmNo"
       AND S0."RenewBormNo" = T0."BormNo"
      )
   WHEN MATCHED THEN UPDATE
   SET T0."RenewFlag" = CASE
                          WHEN T0."RenewFlag" = '0'
                          THEN '1'
                        ELSE T0."RenewFlag" END
   ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_RenewMapping_Upd_LoanBorMain',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
