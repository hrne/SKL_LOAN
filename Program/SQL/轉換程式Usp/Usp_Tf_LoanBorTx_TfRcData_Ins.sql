CREATE OR REPLACE PROCEDURE "Usp_Tf_LoanBorTx_TfRcData_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "TfRcData" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "TfRcData" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "TfRcData" ENABLE PRIMARY KEY'; 
  
    -- 寫入資料 
    INSERT INTO "TfRcData" ( 
           "LMSACN" 
          ,"LMSAPN" 
          ,"LMSASQ" 
          ,"TRXISD" 
          ,"FitRate" 
    ) 
    WITH IsdData AS (
      SELECT DISTINCT 
            "LMSACN" 
           ,"LMSAPN" 
           ,"LMSASQ" 
           ,"TRXISD" 
      FROM "LA$TRXP" 
      WHERE "TRXISD" > 0 
    )
    , rcData AS (
      SELECT "LMSACN" 
            ,"LMSAPN" 
            ,"LMSASQ" 
            ,"TRXISD" 
            ,NVL(RC."FitRate",LBM."StoreRate") AS "FitRate" -- 適用利率 
            ,ROW_NUMBER() OVER (PARTITION BY TX."LMSACN"  
                                            ,TX."LMSAPN" 
                                            ,TX."LMSASQ" 
                                            ,TX."TRXISD" 
                                ORDER BY NVL(RC."EffectDate",0) DESC) AS "Seq" 
      FROM IsdData TX 
      LEFT JOIN "LoanRateChange" RC ON RC."CustNo" = TX."LMSACN" 
                                   AND RC."FacmNo" = TX."LMSAPN" 
                                   AND RC."BormNo" = TX."LMSASQ" 
                                   AND RC."EffectDate" <= TX."TRXISD" 
      LEFT JOIN "LoanBorMain" LBM ON LBM."CustNo" = TX."LMSACN" 
                                 AND LBM."FacmNo" = TX."LMSAPN" 
                                 AND LBM."BormNo" = TX."LMSASQ" 
      WHERE NVL(RC."FitRate",LBM."StoreRate") > 0 
    )
    SELECT "LMSACN" 
          ,"LMSAPN" 
          ,"LMSASQ" 
          ,"TRXISD" 
          ,"FitRate"
    FROM rcData
    WHERE "Seq" = 1

    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 
  
    -- 記錄程式結束時間 
    JOB_END_TIME := SYSTIMESTAMP; 
 
    commit; 

    -- 例外處理 
    Exception 
    WHEN OTHERS THEN 
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace; 
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_LoanBorTx_RcData_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
