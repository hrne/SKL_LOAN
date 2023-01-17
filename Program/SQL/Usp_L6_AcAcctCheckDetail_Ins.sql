CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L6_AcAcctCheckDetail_Ins" 
(    
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    "EmpNo"        IN  VARCHAR2    -- 經辦
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;        -- 新增筆數 
    UPD_CNT        INT;        -- 更新筆數 
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;  -- 記錄程式結束時間    
   --  EOF_YYYYMM     INT;        -- 月底年月     
  BEGIN   
    INS_CNT :=0;
    UPD_CNT :=0;
   --  IF TBSDYF = MFBSDYF THEN
   --     EOF_YYYYMM   :=  MFBSDYF / 100;
   --  ELSE
   --     EOF_YYYYMM   :=  0;
   --  END IF;
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    DELETE FROM "AcAcctCheckDetail"
    WHERE "AcDate" = TBSDYF
    ;

    -- 寫入資料
    INSERT INTO "AcAcctCheckDetail" (
      "AcDate"            -- 會計日期 Decimald 8
      , "BranchNo"        -- 單位別 VARCHAR2 4
      , "CurrencyCode"    -- 幣別 VARCHAR2 3
      , "AcSubBookCode"
      , "AcctCode"        -- 業務科目代號 VARCHAR2 3
      , "AcctItem"        -- 業務科目名稱 NVARCHAR2  20
      , "CustNo"          -- 戶號 DECIMAL 7
      , "FacmNo"          -- 額度號碼 DECIMAL 3
      , "BormNo"          -- 撥款序號 DECIMAL 3
      , "AcBal"           -- 會計帳餘額 DECIMAL 16 2
      , "AcctMasterBal"   -- 業務帳餘額 DECIAML 16 2
      , "DiffBal"         -- 差額 DECIMAL 18 2
      , "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
      , "CreateDate"      -- 建檔日期 DATE 
      , "LastUpdateEmpNo" -- 最後維護人員 VARCHAR2 6
      , "LastUpdate"      -- 最後維護日期 DATE 
    )
    WITH "AcctCodeData" AS (
      SELECT "AcctCode"
            ,"AcctItem"
            ,"AcDate"
            ,"BranchNo"
            ,"CurrencyCode"
            ,"AcSubBookCode"
      FROM "AcAcctCheck"
      WHERE CASE
              WHEN "AcctMasterBal" != "ReceivableBal"
              THEN 1
              WHEN "AcctMasterBal" != "TdBal"
              THEN 1
            ELSE 0 
            END = 1
        AND "AcDate" = TBSDYF
        AND "AcctCode" IN ('310','320','330','340','990') -- xwh 20211124 added 340
    )
    ,"AR" AS (
      SELECT "AcctCode"
           , "BranchNo"
           , "CurrencyCode"
           , "AcSubBookCode"
           , "CustNo"
           , "FacmNo"
           , "RvNo"
           , "RvBal"
      FROM "AcReceivable"
      WHERE "AcctCode" IN ('310','320','330','340','990') -- xwh 20211124 added 340
    )
    ,"Loan" AS (
      SELECT CASE
               WHEN NVL(L2."BormNo",0) > 0 
               THEN '990' 
             ELSE F1."AcctCode" 
             END              AS "AcctCode"
           , AR."AcSubBookCode"
           , 'TWD' AS "CurrencyCode"
           , F1."BranchNo"
           , F1."CustNo"
           , F1."FacmNo"
           , L1."BormNo"
           , CASE 
               WHEN NVL(L2."BormNo",0) > 0 
               THEN L2."OvduBal" 
               ELSE L1."LoanBal" 
             END             AS "LoanBal"
      FROM "FacMain" F1
      LEFT JOIN "LoanBorMain" L1 ON L1."CustNo" = F1."CustNo"
                                AND L1."FacmNo" = F1."FacmNo"
      LEFT JOIN "LoanOverdue" L2 ON L2."CustNo" = L1."CustNo"
                              AND L2."FacmNo" = L1."FacmNo"
                              AND L2."BormNo" = L1."BormNo"
                              AND L2."OvduNo" = L1."LastOvduNo"
                              AND L1."Status" IN (2,6,7)
      LEFT JOIN ( SELECT "CustNo"
                       , "FacmNo"
                       , MAX("AcSubBookCode") AS "AcSubBookCode"
                  FROM "AcReceivable"
                  WHERE "AcctCode" IN ('310','320','330','340','990') -- xwh 20211124 added 340
                  GROUP BY "CustNo"
                         , "FacmNo"
                ) AR ON AR."CustNo" = F1."CustNo"
                    AND AR."FacmNo" = F1."FacmNo"
    )
    ,"Diff" AS (
      SELECT NVL(A."AcctCode",L."AcctCode")           AS "AcctCode"
           , NVL(A."BranchNo",L."BranchNo")           AS "BranchNo"
           , NVL(A."CurrencyCode",L."CurrencyCode")   AS "CurrencyCode"
           , NVL(A."AcSubBookCode",L."AcSubBookCode") AS "AcSubBookCode"
           , NVL(A."CustNo",L."CustNo")               AS "CustNo"          -- 戶號 DECIMAL 7
           , NVL(A."FacmNo",L."FacmNo")               AS "FacmNo"          -- 額度號碼 DECIMAL 3
           , NVL(A."RvNo",L."BormNo")                 AS "BormNo"          -- 撥款序號 DECIMAL 3
           , NVL(A."RvBal",0)                         AS "AcBal"           -- 會計帳餘額 DECIMAL 16 2
           , NVL(L."LoanBal",0)                       AS "AcctMasterBal"   -- 業務帳餘額 DECIAML 16 2
           , NVL(A."RvBal",0)
             - NVL(L."LoanBal",0)                     AS "DiffBal"         -- 差額 DECIMAL 18 2
      FROM "AR" A
      FULL OUTER JOIN "Loan" L ON (
        L."AcctCode" = A."AcctCode"
        AND L."BranchNo" = A."BranchNo"
        AND L."CurrencyCode" = A."CurrencyCode"
        AND L."AcSubBookCode" = A."AcSubBookCode"
        AND L."CustNo" = A."CustNo"
        AND L."FacmNo" = A."FacmNo"
        AND L."BormNo" = A."RvNo"
      )
      WHERE NVL(A."RvBal",0)
            - NVL(L."LoanBal",0)
            != 0
    )
    SELECT S1."AcDate"                      AS "AcDate"          -- 會計日期 Decimald 8
          ,S1."BranchNo"                    AS "BranchNo"        -- 單位別 VARCHAR2 4
          ,S1."CurrencyCode"                AS "CurrencyCode"    -- 幣別 VARCHAR2 3
          ,S1."AcSubBookCode"               AS "AcSubBookCode"
          ,S1."AcctCode"                    AS "AcctCode"        -- 業務科目代號 VARCHAR2 3
          ,S1."AcctItem"                    AS "AcctItem"        -- 業務科目名稱 NVARCHAR2  20
          ,NVL(S2."CustNo",0)               AS "CustNo"          -- 戶號 DECIMAL 7
          ,NVL(S2."FacmNo",0)               AS "FacmNo"          -- 額度號碼 DECIMAL 3
          ,NVL(S2."BormNo",0)               AS "BormNo"          -- 撥款序號 DECIMAL 3
          ,NVL(S2."AcBal",0)                AS "AcBal"           -- 會計帳餘額 DECIMAL 16 2
          ,NVL(S2."AcctMasterBal",0)        AS "AcctMasterBal"   -- 業務帳餘額 DECIAML 16 2
          ,NVL(S2."DiffBal",0)              AS "DiffBal"         -- 差額 DECIMAL 18 2
          ,"EmpNo"                          AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "CreateDate"      -- 建檔日期 DATE 
          ,"EmpNo"                          AS "LastUpdateEmpNo" -- 最後維護人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "LastUpdate"      -- 最後維護日期 DATE 
    FROM "AcctCodeData" S1
    LEFT JOIN "Diff" S2 ON S2."AcctCode" = S1."AcctCode"
                       AND S2."BranchNo" = S1."BranchNo"
                       AND S2."CurrencyCode" = S1."CurrencyCode"
                       AND S2."AcSubBookCode" = S1."AcSubBookCode"
    WHERE NVL(S2."DiffBal",0)
          != 0
    ;

    INS_CNT := INS_CNT + sql%rowcount;


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L6_AcAcctCheckDetail_Ins' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , "EmpNo" -- 發動預存程序的員工編號
    );
  END;
END;

