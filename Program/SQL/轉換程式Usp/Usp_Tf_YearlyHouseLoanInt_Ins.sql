--------------------------------------------------------
--  DDL for Procedure Usp_Tf_YearlyHouseLoanInt_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_YearlyHouseLoanInt_Ins" 
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
    DELETE FROM "YearlyHouseLoanInt";

    -- 2022-12-20 Wei修改 from Linda
    -- LA$W24P的資料裡(資料年月+戶號+額度)
    -- 可能同時有
    -- 資金用途別UsageCode=0(官網)跟2(國稅局),AS400寫2筆
    -- 調整為
    -- 若該(資料年月+戶號+額度)有資金用途別UsageCode=2(國稅局)
    -- 則跳掉不要轉資金用途別UsageCode=0(官網)
    -- 產媒體檔時
    -- 國稅局挑用途別2的
    -- 官網是撈全部資料

    -- 寫入資料
    INSERT INTO "YearlyHouseLoanInt"(
        "YearMonth"             -- 資料年月 DECIMAL 6 0
      , "CustNo"              -- 戶號 DECIMAL 7 0
      , "FacmNo"              -- 額度編號 DECIMAL 3 0
      , "UsageCode"           -- 資金用途別 VARCHAR2 2 0
      , "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
      , "RepayCode"           -- 繳款方式 VARCHAR2 3 0
      , "LoanAmt"             -- 撥款金額 VARCHAR2 16 2
      , "LoanBal"             -- 放款餘額 DECIMAL 16 2
      , "FirstDrawdownDate"   -- 初貸日 DECIMALD 8 0
      , "MaturityDate"        -- 到期日 DECIMALD 8 0
      , "YearlyInt"           -- 年度繳息金額 DECIMAL 16 2
      , "HouseBuyDate"        -- 房屋取得日期 decimald  
      , "JsonFields"          -- jason格式紀錄欄 NVARCHAR2 300
      , "CreateDate"          -- 建檔日期時間 DATE 0 
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2  
      , "LastUpdate"          -- 最後更新日期時間 DATE 0 
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    )
    WITH taxData (
      -- 資料年月+戶號+額度
      SELECT DISTINCT
             ADTYMT
           , LMSACN
           , LMSAPN
           , W24USG
      FROM LA$W24P
      WHERE W24USG = 2
    )
    SELECT W.ADTYMT                       AS "YearMonth"           -- 資料年月 DECIMAL 6 0
          ,W.LMSACN                       AS "CustNo"              -- 戶號 DECIMAL 7 0
          ,W.LMSAPN                       AS "FacmNo"              -- 額度編號 DECIMAL 3 0
          ,W.W24USG                       AS "UsageCode"           -- 資金用途別 VARCHAR2 2 0
          ,W.ACTACT                       AS "AcctCode"            -- 業務科目代號 VARCHAR2 3 0
          ,W.LMSPYS                       AS "RepayCode"           -- 繳款方式 VARCHAR2 3 0
          ,W.LMSFLA                       AS "LoanAmt"             -- 撥款金額 VARCHAR2 16 2
          ,W.LMSLBL                       AS "LoanBal"             -- 放款餘額 DECIMAL 16 2
          ,W.LMSLLD                       AS "FirstDrawdownDate"   -- 初貸日 DECIMALD 8 0
          ,W.LMSDLD                       AS "MaturityDate"        -- 到期日 DECIMALD 8 0
          ,W.TRXAMT                       AS "YearlyInt"           -- 年度繳息金額 DECIMAL 16 2
          ,W.HGTGTD                       AS "HouseBuyDate"        -- 房屋取得日期 decimald  
          ,null                           AS "JsonFields"          -- jason格式紀錄欄 NVARCHAR2 300
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2  
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "LA$W24P" W
    LEFT JOIN taxData T ON T.ADTYMT = W.ADTYMT
                       AND T.LMSACN = W.LMSACN
                       AND T.LMSAPN = W.LMSAPN
    WHERE W.ADTYMT >= 200701
      AND CASE
            WHEN NVL(T.W24USG,0) = 2 -- 該(資料年月+戶號+額度)有資金用途別UsageCode=2(國稅局)
                 AND W.W24USG = 0 -- 則跳掉不要轉資金用途別UsageCode=0(官網)
            THEN 0 -- 不轉
          ELSE 1 -- 其他都要轉
          END = 1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_YearlyHouseLoanInt_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
