--------------------------------------------------------
--  DDL for Procedure Usp_Tf_InnFundApl_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_InnFundApl_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "InnFundApl" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "InnFundApl" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "InnFundApl" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "InnFundApl" (
        "AcDate"              -- 日期 DecimalD 8 0
      , "ResrvStndrd"         -- 責任準備金 DECIMAL 14 0
      , "PosbleBorPsn"        -- 可放款比率% DECIMAL 7 4
      , "PosbleBorAmt"        -- 可放款金額 DECIMAL 16 2
      , "AlrdyBorAmt"         -- 已放款金額 DECIMAL 16 2
      , "StockHoldersEqt"     -- 股東權益 DECIMAL 16 2
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT "LA$FDNP"."FDNDAT"             AS "Acdate"              -- 日期 DecimalD 8 0
          ,"LA$FDNP"."FDNAM1"             AS "ResrvStndrd"         -- 責任準備金 DECIMAL 14 0
          ,"LA$FDNP"."FDNPER"             AS "PosbleBorPsn"        -- 可放款比率% DECIMAL 7 4
          ,CASE
             WHEN "LA$FDNP"."FDNAM1" > 0 THEN ROUND(("LA$FDNP"."FDNAM1" * "LA$FDNP"."FDNPER" / 100), 0)
           ELSE 0 END                     AS "PosbleBorAmt"        -- 可放款金額 DECIMAL 16 2
          ,"LA$FDNP"."FDNAM2"             AS "AlrdyBorAmt"         -- 已放款金額 DECIMAL 16 2
          ,0                              AS "StockHoldersEqt"     -- 股東權益 DECIMAL 16 2
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$FDNP"
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_InnFundApl_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
