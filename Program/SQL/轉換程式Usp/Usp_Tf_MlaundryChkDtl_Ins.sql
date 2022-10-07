--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MlaundryChkDtl_Ins
--------------------------------------------------------
set define off;
CREATE OR REPLACE PROCEDURE "Usp_Tf_MlaundryChkDtl_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "MlaundryChkDtl" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "MlaundryChkDtl" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "MlaundryChkDtl" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "MlaundryChkDtl" (
        "EntryDate"       -- 入帳日期(統計期間迄日)  Decimald  8  
      , "Factor"          -- 交易樣態  DECIMAL  2  
      , "CustNo"          -- 戶號  DECIMAL  7  
      , "DtlSeq"          -- 明細序號  DECIMAL  4  
      , "DtlEntryDate"    -- 明細入帳日期  Decimald  8  
      , "RepayItem"       -- 來源  nvarchar2  10  
      , "DscptCode"       -- 摘要代碼  VARCHAR2  4  
      , "TxAmt"           -- 交易金額  DECIMAL  16  2
      , "TotalCnt"        -- 累積筆數  DECIMAL  3  
      , "TotalAmt"        -- 累積金額    DECIMAL  16  2
      , "StartEntryDate"  -- 統計期間起日  Decimald  8  
      , "CreateDate"      -- 建檔日期時間  DATE  8  
      , "CreateEmpNo"     -- 建檔人員  VARCHAR2  6  
      , "LastUpdate"      -- 最後更新日期時間  DATE  8  
      , "LastUpdateEmpNo" -- 最後更新人員  VARCHAR2  6  
    )
    WITH rawData AS (
      SELECT MLD."EntryDate"
           , MLD."Factor"
           , MLD."CustNo"
           , MLD."TotalAmt"
           , TO_CHAR(SUBSTR(BR."VirtualAcctNo",1,5)) AS "RepayItem"
           , BR."DscptCode"
           , BR."RepayAmt"
      FROM "MlaundryDetail" MLD
      LEFT JOIN "BankRmtf" BR ON BR."CustNo" = MLD."CustNo"
                             AND BR."EntryDate" = MLD."EntryDate"
                             AND NVL(BR."AmlRsp",'9') IN ('0','1','2')
      WHERE NVL(BR."CustNo",0) > 0
      UNION ALL
      SELECT MLD."EntryDate"
           , MLD."Factor"
           , MLD."CustNo"
           , MLD."TotalAmt"
           , CASE
               WHEN NVL(ADM."CustNo",0) > 0
               THEN '銀扣'
             ELSE ' ' END    AS "RepayItem"
           , ''              AS "DscptCode"
           , ADM."RepayAmt"  AS "RepayAmt"
      FROM "MlaundryDetail" MLD
      LEFT JOIN "AchDeductMedia" ADM ON ADM."CustNo" = MLD."CustNo"
                                    AND ADM."AcDate" > 0
                                    AND ADM."ReturnCode" IN ('00')
                                    AND ADM."AcDate" = MLD."EntryDate"
      WHERE NVL(ADM."CustNo",0) > 0
      UNION ALL
      SELECT MLD."EntryDate"
           , MLD."Factor"
           , MLD."CustNo"
           , MLD."TotalAmt"
           , CASE
               WHEN NVL(LC."CustNo",0) > 0
               THEN '支票'
             ELSE ' ' END    AS "RepayItem"
           , ''              AS "DscptCode"
           , LC."ChequeAmt"  AS "RepayAmt"
      FROM "MlaundryDetail" MLD
      LEFT JOIN "LoanCheque" LC ON LC."CustNo" = MLD."CustNo"
                               AND LC."StatusCode" = '1'
                               AND LC."EntryDate" = MLD."EntryDate"
      WHERE NVL(LC."CustNo",0) > 0
    )
    SELECT "EntryDate"        AS "EntryDate"       -- 入帳日期(統計期間迄日)  Decimald  8  
         , "Factor"           AS "Factor"          -- 交易樣態  DECIMAL  2  
         , "CustNo"           AS "CustNo"          -- 戶號  DECIMAL  7  
         , ROW_NUMBER()
           OVER (
            PARTITION BY
                "EntryDate"
              , "Factor"
              , "CustNo"
            ORDER BY 
                "RepayItem"
              , "RepayAmt"
           )                  AS "DtlSeq"          -- 明細序號  DECIMAL  4  
         , "EntryDate"        AS "DtlEntryDate"    -- 明細入帳日期  Decimald  8  
         , "RepayItem"        AS "RepayItem"       -- 來源  nvarchar2  10  
         , "DscptCode"        AS "DscptCode"       -- 摘要代碼  VARCHAR2  4  
         , "RepayAmt"         AS "TxAmt"           -- 交易金額  DECIMAL  16  2
         , 1                  AS "TotalCnt"        -- 累積筆數  DECIMAL  3  
         , "TotalAmt"         AS "TotalAmt"        -- 累積金額    DECIMAL  16  2
         , "EntryDate"        AS "StartEntryDate"  -- 統計期間起日  Decimald  8  
         , JOB_START_TIME     AS "CreateDate"      -- 建檔日期時間  DATE  8  
         , '999999'           AS "CreateEmpNo"     -- 建檔人員  VARCHAR2  6  
         , JOB_START_TIME     AS "LastUpdate"      -- 最後更新日期時間  DATE  8  
         , '999999'           AS "LastUpdateEmpNo" -- 最後更新人員  VARCHAR2  6  
    FROM rawData
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    MERGE IN "MlaundryChkDtl" T
    USING (
      SELECT "EntryDate"
           , "Factor"
           , "CustNo"
           , SUM(1) AS "TotalCnt"
      FROM "MlaundryChkDtl"
      WHERE "CreateEmpNo" = '999999'
      GROUP BY "EntryDate"
             , "Factor"
             , "CustNo"
    ) S
    ON (
      S."EntryDate" = T."EntryDate"
      AND S."Factor" = T."Factor"
      AND S."CustNo" = T."CustNo"
    )
    WHEN MATCHED THEN UPDATE
    SET "TotalCnt" = S."TotalCnt"
    ;

    MERGE IN "MlaundryDetail" T
    USING (
      SELECT DISTINCT
             "EntryDate"
           , "Factor"
           , "CustNo"
           , "TotalCnt"
      FROM "MlaundryChkDtl"
      WHERE "CreateEmpNo" = '999999'
    ) S
    ON (
      S."EntryDate" = T."EntryDate"
      AND S."Factor" = T."Factor"
      AND S."CustNo" = T."CustNo"
    )
    WHEN MATCHED THEN UPDATE
    SET "TotalCnt" = S."TotalCnt"
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
END;
/