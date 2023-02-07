--------------------------------------------------------
--  DDL for Procedure Usp_Tf_BatxOthers_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_BatxOthers_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "BatxOthers" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "BatxOthers" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "BatxOthers" ENABLE PRIMARY KEY';

    -- 寫入資料
    -- 法院扣薪
    INSERT INTO "BatxOthers"
    SELECT CTS."TRXDAT"                   AS "AcDate"              -- 會計日期 Decimald 8 0
          ,'BATX01'                       AS "BatchNo"             -- 整批批號 VARCHAR2 6 0
          ,ROW_NUMBER() OVER (PARTITION BY CTS."TRXDAT"
                              ORDER BY CTS."TRXNMT")
                                          AS "DetailSeq"           -- 明細序號 DECIMAL 6 0
          ,5                              AS "RepayCode"           -- 來源 DECIMAL 2 0
          ,9                              AS "RepayType"           -- 還款類別 DECIMAL 2 0 ??? 不確定
          ,''                             AS "RepayAcCode"         -- 來源會計科目 VARCHAR2 15 0
          ,CTS."TRXDAT"                   AS "EntryDate"           -- 入帳日期 Decimald 8 0
          ,CTS."METAMT"                   AS "RepayAmt"            -- 金額 DECIMAL 14 0
          ,CTS."CUSID1"                   AS "RepayId"             -- 來源統編 VARCHAR2 10 0
          ,CTS."CUSNAJ"                   AS "RepayName"           -- 來源戶名 NVARCHAR2 100 0
          ,CTS."LMSACN"                   AS "CustNo"              -- 借款人戶號 DECIMAL 7 0
          ,0                              AS "FacmNo"              -- 額度號碼 DECIMAL 3 0
          ,CTS."CUSNAE"                   AS "CustNm"              -- 借款人戶名 NVARCHAR2 100 0
          ,''                             AS "RvNo"                -- 銷帳碼 VARCHAR2 12 0
          ,CTS."T05"                      AS "Note"                -- 摘要 NVARCHAR2 60 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LN$CTSP" CTS
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    DECLARE
        "d_MaxRecordNo" DECIMAL(7);         --記錄號碼
    BEGIN

    SELECT MAX("RecordNo") INTO "d_MaxRecordNo" FROM "ForeclosureFee";

    -- 寫入資料
    -- 安心居理賠累積檔
    INSERT INTO "BatxOthers" (
        "AcDate"              -- 會計日期 Decimald 8 0
      , "BatchNo"             -- 整批批號 VARCHAR2 6 0
      , "DetailSeq"           -- 明細序號 DECIMAL 6 0
      , "RepayCode"           -- 來源 DECIMAL 2 0
      , "RepayType"           -- 還款類別 DECIMAL 2 0 ??? 不確定
      , "RepayAcCode"         -- 來源會計科目 VARCHAR2 15 0
      , "EntryDate"              -- 入帳日期 Decimald 8 0
      , "RepayAmt"            -- 金額 DECIMAL 14 0
      , "RepayId"             -- 來源統編 VARCHAR2 10 0
      , "RepayName"           -- 來源戶名 NVARCHAR2 100 0
      , "CustNo"              -- 借款人戶號 DECIMAL 7 0
      , "FacmNo"              -- 額度號碼 DECIMAL 3 0
      , "CustNm"              -- 借款人戶名 NVARCHAR2 100 0
      , "RvNo"                -- 銷帳碼 VARCHAR2 12 0
      , "Note"                -- 摘要 NVARCHAR2 60 0
      , "CreateDate"          -- 建檔日期時間 DATE 0 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE 0 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    SELECT KCP."TRXDAT"                   AS "AcDate"              -- 會計日期 Decimald 8 0
          ,'BATX01'                       AS "BatchNo"             -- 整批批號 VARCHAR2 6 0
          ,"d_MaxRecordNo" + ROW_NUMBER() OVER (PARTITION BY KCP."TRXDAT"
                                                ORDER BY KCP."TRXNMT")
                                          AS "DetailSeq"           -- 明細序號 DECIMAL 6 0
          ,6                              AS "RepayCode"           -- 來源 DECIMAL 2 0
          ,9                              AS "RepayType"           -- 還款類別 DECIMAL 2 0 ??? 不確定
          ,''                             AS "RepayAcCode"         -- 來源會計科目 VARCHAR2 15 0
          ,KCP."TRXDAT"                   AS "EntryDate"              -- 入帳日期 Decimald 8 0
          ,KCP."KCPAMT"                   AS "RepayAmt"            -- 金額 DECIMAL 14 0
          ,KCP."CUSID1"                   AS "RepayId"             -- 來源統編 VARCHAR2 10 0
          ,KCP."CUSNAJ"                   AS "RepayName"           -- 來源戶名 NVARCHAR2 100 0
          ,KCP."LMSACN"                   AS "CustNo"              -- 借款人戶號 DECIMAL 7 0
          ,0                              AS "FacmNo"              -- 額度號碼 DECIMAL 3 0
          ,KCP."CUSNAJ"                   AS "CustNm"              -- 借款人戶名 NVARCHAR2 100 0
          ,''                             AS "RvNo"                -- 銷帳碼 VARCHAR2 12 0
          ,KCP."KCPPLY"                   AS "Note"                -- 摘要 NVARCHAR2 60 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LN$KCPP" KCP
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    END;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_BatxOthers_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;



/
