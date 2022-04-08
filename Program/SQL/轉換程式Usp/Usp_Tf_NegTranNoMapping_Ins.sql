--------------------------------------------------------
--  DDL for Procedure Usp_Tf_NegTranNoMapping_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_NegTranNoMapping_Ins" 
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
    -- EXECUTE IMMEDIATE 'ALTER TABLE "NegTranNoMapping" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "NegTranNoMapping" DROP STORAGE';
    -- EXECUTE IMMEDIATE 'ALTER TABLE "NegTranNoMapping" ENABLE PRIMARY KEY';

    -- 寫入資料 (債務協商交易序號對照檔)
    INSERT INTO "NegTranNoMapping"
    SELECT BS.ACCOUNT_DATE                         AS "AcDate"         -- 會計日期 decimal(8, 0) default 0 not null,
          ,NVL(DTYP.CUSEMP,SUBSTR(BS.ModifyUserID,0,6))
                                                   AS "TitaTlrNo"      -- 經辦 varchar2(6),
          ,ROW_NUMBER() OVER (PARTITION BY BS.ACCOUNT_DATE
                                         , NVL(DTYP.CUSEMP,SUBSTR(BS.ModifyUserID,0,6))
                              ORDER BY BS.ACCOUNT_DATE
                                     , BS.CustIDN
                                     , BS.RC_DATE
                                     , BS.BUSINESS_CODE)
                                                   AS "TitaTxtNo"      -- 交易序號 decimal(8, 0) default 0 not null,
          ,BS.CustIDN                              AS "CustIDN"        -- 債務人IDN varchar2(10),
          ,BS.RC_DATE                              AS "RC_DATE"        -- 協商申請日 decimal(8, 0) default 0 not null,
          ,BS.ACCOUNT_DATE                         AS "ACCOUNT_DATE"   -- 會計日期 decimal(8, 0) default 0 not null,
          ,BS.BUSINESS_CODE                        AS "BUSINESS_CODE"  -- 交易序號 decimal(7, 0) default 0 not null
    FROM "tbJCICBusiness" BS
    LEFT JOIN LN$DTYP DTYP ON DTYP.CUSID1 = BS.ModifyUserID
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_NegTranNoMapping_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
