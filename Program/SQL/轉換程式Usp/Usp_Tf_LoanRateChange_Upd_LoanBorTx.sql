--------------------------------------------------------
--  DDL for Procedure Usp_Tf_LoanRateChange_Upd_LoanBorTx
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_LoanRateChange_Upd_LoanBorTx" 
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

    /*  更新 "LoanBorTx"."Rate" = "LoanRateChange"."FitRate"
        取 "LoanRateChange"."EffectDate" <= "LoanBorTx"."IntStartDate"
           且 最接近的一筆
    */
    MERGE INTO "LoanBorTx" T1
    USING (
      SELECT TX."CustNo"                      -- 借款人戶號
            ,TX."FacmNo"                      -- 額度編號
            ,TX."BormNo"                      -- 撥款序號
            ,TX."TitaTxtNo"                   -- 交易序號
            ,NVL(RC."FitRate",LBM."StoreRate") AS "FitRate" -- 適用利率
            ,ROW_NUMBER() OVER (PARTITION BY TX."CustNo" 
                                            ,TX."FacmNo"
                                            ,TX."BormNo"
                                            ,TX."TitaTxtNo"
                                ORDER BY NVL(RC."EffectDate",0) DESC) AS "Seq"
      FROM "LoanBorTx" TX
      LEFT JOIN "LoanRateChange" RC ON RC."CustNo" = TX."CustNo"
                                   AND RC."FacmNo" = TX."FacmNo"
                                   AND RC."BormNo" = TX."BormNo"
                                   AND RC."EffectDate" <= TX."IntStartDate"
      LEFT JOIN "LoanBorMain" LBM ON LBM."CustNo" = TX."CustNo"
                                 AND LBM."FacmNo" = TX."FacmNo"
                                 AND LBM."BormNo" = TX."BormNo"
      WHERE TX."IntStartDate" > 0
        AND NVL(RC."FitRate",LBM."StoreRate")   > 0
    ) S1 ON (    S1."CustNo"    = T1."CustNo"
             AND S1."FacmNo"    = T1."FacmNo"
             AND S1."BormNo"    = T1."BormNo"
             AND S1."TitaTxtNo" = T1."TitaTxtNo"
             AND S1."Seq" = 1)
    WHEN MATCHED THEN UPDATE SET
    T1."Rate"  = S1."FitRate" 
    ;
    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanRateChange_Upd_LoanBorTx',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
