--------------------------------------------------------
--  DDL for Procedure Usp_Tf_EmpDeductMedia_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_EmpDeductMedia_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "EmpDeductMedia" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "EmpDeductMedia" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "EmpDeductMedia" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "EmpDeductMedia" (
        "MediaDate"           -- decimal 8 0
      , "MediaKind"           -- varchar2 1
      , "MediaSeq"            -- decimal 6 0
      , "CustNo"              -- 戶號 DECIMAL 7 0
      , "RepayCode"           -- 入帳扣款別 VARCHAR2 1 0
      , "PerfRepayCode"       -- 扣款代碼 VARCHAR2 1 0
      , "RepayAmt"            -- 應扣金額 DECIMAL 14 0
      , "PerfMonth"           -- 業績年月 DECIMAL 6 0
      , "FlowCode"            -- 流程別 VARCHAR2 1 0
      , "UnitCode"            -- 單位別 VARCHAR2 6 0
      , "CustId"              -- 統一編號 VARCHAR2 10 0
      , "EntryDate"           -- 入帳日期 DECIMAL 8 0
      , "TxAmt"               -- 交易金額 DECIMAL 14 0
      , "ErrorCode"           -- 失敗原因 NVARCHAR2 20 0
      , "AcctCode"            -- 科目 VARCHAR2 12 0
      , "AcDate"              -- 會計日期 DECIMAL 8 0
      , "BatchNo"             -- 批次號碼 VARCHAR2 6 0
      , "DetailSeq"           -- 明細序號 DECIMAL 6 0
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT S1."TRXIDT"                    AS "MediaDate"           -- decimal 8 0
          ,CASE
             WHEN S1."FLWCOD" IN ('4','5')
             THEN '4'
           ELSE '5' END                   AS "MediaKind"           -- varchar2 1
          ,ROW_NUMBER()
           OVER (
             PARTITION BY S1."TRXIDT"
                        , CASE
                            WHEN S1."FLWCOD" IN ('4','5')
                            THEN '4'
                          ELSE '5' END
             ORDER BY S1."TRXNMT"
                    , S1."LMSACN"
                    , CASE S1."MBKTRX"
                        WHEN '1' THEN '5' -- 保險費
                        WHEN '2' THEN '1' -- 期款
                        WHEN '3' THEN '4' -- 帳管費
                        WHEN '4' THEN '6' -- 契變手續費
                      ELSE '0' END
                    , S1."TPAYAMT"
                    , S1."TRXAMT"
                    , S1."TRXDAT"
                    , S1."BSTBTN"
           )                               AS "MediaSeq"            -- decimal 6 0
          ,S1."LMSACN"                    AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,CASE S1."MBKTRX"
             WHEN '1' THEN '5' -- 保險費
             WHEN '2' THEN '1' -- 期款
             WHEN '3' THEN '4' -- 帳管費
             WHEN '4' THEN '6' -- 契變手續費
           ELSE '0' END                   AS "RepayCode"           -- 入帳扣款別 VARCHAR2 1 0
          ,S1."DEDCOD"                    AS "PerfRepayCode"       -- 扣款代碼 VARCHAR2 1 0
          ,S1."TPAYAMT"                   AS "RepayAmt"            -- 應扣金額 DECIMAL 14 0
          ,S1."YGYYMM"                    AS "PerfMonth"           -- 業績年月 DECIMAL 6 0
          ,S1."FLWCOD"                    AS "FlowCode"            -- 流程別 VARCHAR2 1 0
          ,S1."BCMCOD"                    AS "UnitCode"            -- 單位別 VARCHAR2 6 0
          ,S1."CUSID1"                    AS "CustId"              -- 統一編號 VARCHAR2 10 0
          ,S1."TRXIDT"                    AS "EntryDate"           -- 入帳日期 DECIMAL 8 0
          ,S1."TRXAMT"                    AS "TxAmt"               -- 交易金額 DECIMAL 14 0
          ,S1."FALCOD"                    AS "ErrMsg"              -- 失敗原因 NVARCHAR2 20 0
          ,S1."ACTACT"                    AS "AcctCode"            -- 科目 VARCHAR2 12 0
          ,S1."TRXDAT"                    AS "Acdate"              -- 會計日期 DECIMAL 8 0
          ,S1."BSTBTN"                    AS "BatchNo"             -- 批次號碼 VARCHAR2 6 0
          ,ROW_NUMBER() OVER (PARTITION BY S1."TRXDAT"
                                          ,S1."BSTBTN"
                              ORDER BY S1."TRXNMT")
                                          AS "DetailSeq"           -- 明細序號 DECIMAL 6 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LNMSLP" S1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_EmpDeductMedia_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
