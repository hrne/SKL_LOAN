--------------------------------------------------------
--  DDL for Procedure Usp_L6_AcAcctCheckDetail_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_L6_AcAcctCheckDetail_Ins" 
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
    INSERT INTO "AcAcctCheckDetail"
    SELECT S1."AcDate"                      AS "AcDate"          -- 會計日期 Decimald 8
          ,S1."BranchNo"                    AS "BranchNo"        -- 單位別 VARCHAR2 4
          ,S1."CurrencyCode"                AS "CurrencyCode"    -- 幣別 VARCHAR2 3
          ,S1."AcctCode"                    AS "AcctCode"        -- 業務科目代號 VARCHAR2 3
          ,S1."AcctItem"                    AS "AcctItem"        -- 業務科目名稱 NVARCHAR2  20
          ,NVL(S2."CustNo",NVL(S3."CustNo",S4."CustNo"))
                                            AS "CustNo"          -- 戶號 DECIMAL 7
          ,NVL(S2."FacmNo",NVL(S3."FacmNo",S4."FacmNo"))
                                            AS "FacmNo"          -- 額度號碼 DECIMAL 3
          ,NVL(S2."RvNo",NVL(S3."BormNo",S4."BormNo"))
                                            AS "BormNo"          -- 撥款序號 DECIMAL 3
          ,NVL(S2."RvBal",0)                AS "AcBal"           -- 會計帳餘額 DECIMAL 16 2
          ,CASE
             WHEN S1."AcctCode" IN ('310','320','330')
             THEN NVL(S3."AcctMasterBal",0)
             WHEN S1."AcctCode" = '990'
             THEN NVL(S4."AcctMasterBal",0)
           ELSE 0 END                       AS "AcctMasterBal"   -- 業務帳餘額 DECIAML 16 2
          ,NVL(S2."RvBal",0)
          - CASE
              WHEN S1."AcctCode" IN ('310','320','330')
              THEN NVL(S3."AcctMasterBal",0)
              WHEN S1."AcctCode" = '990'
              THEN NVL(S4."AcctMasterBal",0)
            ELSE 0 END                      AS "DiffBal"         -- 差額 DECIMAL 18 2
          ,"EmpNo"                          AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "CreateDate"      -- 建檔日期 DATE 
          ,"EmpNo"                          AS "LastUpdateEmpNo" -- 最後維護人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "LastUpdate"      -- 最後維護日期 DATE 
          ,S1."AcSubBookCode"               AS "AcSubBookCode"
    FROM (SELECT "AcctCode"
                ,"AcctItem"
                ,"AcDate"
                ,"BranchNo"
                ,"CurrencyCode"
                ,"AcSubBookCode"
          FROM "AcAcctCheck"
          WHERE "AcctMasterBal" <> "ReceivableBal"
            AND "AcDate" = TBSDYF
            AND "AcctCode" IN ('310','320','330','990')
         ) S1
    LEFT JOIN "AcReceivable" S2 ON S2."AcctCode" = S1."AcctCode"
                               AND S2."AcctFlag" = 1 -- 篩選 業務科目記號 1: 資負明細科目
                               AND S2."BranchNo" = S1."BranchNo"
                               AND S2."CurrencyCode" = S1."CurrencyCode"
                               AND S2."AcSubBookCode" = S1."AcSubBookCode"
    LEFT JOIN (SELECT F1."AcctCode"
                     ,F1."CustNo"
                     ,F1."FacmNo"
                     ,L1."BormNo"
                     ,AR."AcSubBookCode"
                     ,L1."LoanBal" AS "AcctMasterBal"
               FROM "FacMain" F1
               LEFT JOIN "LoanBorMain" L1 ON L1."CustNo" = F1."CustNo"
                                         AND L1."FacmNo" = F1."FacmNo"
               LEFT JOIN ( SELECT "CustNo"
                                , "FacmNo"
                                , MAX("AcSubBookCode") AS "AcSubBookCode"
                           FROM "AcReceivable"
                           WHERE "AcctCode" IN ('310','320','330','990')
                           GROUP BY "CustNo"
                                  , "FacmNo"
                         ) AR ON AR."CustNo" = F1."CustNo"
                             AND AR."FacmNo" = F1."FacmNo"
               WHERE "AcctCode" IN ('310','320','330')
              ) S3 ON S3."AcctCode" = S1."AcctCode"
                  AND S3."CustNo" = S2."CustNo"
                  AND S3."FacmNo" = S2."FacmNo"
                  AND S3."BormNo" = S2."RvNo"
                  AND S3."AcSubBookCode" = S1."AcSubBookCode"
    LEFT JOIN (SELECT O1."AcctCode"
                     ,O1."CustNo"
                     ,O1."FacmNo"
                     ,O1."BormNo"
                     ,AR."AcSubBookCode"
                     ,O1."OvduBal" AS "AcctMasterBal"
               FROM "LoanOverdue" O1
               LEFT JOIN ( SELECT "CustNo"
                                , "FacmNo"
                                , MAX("AcSubBookCode") AS "AcSubBookCode"
                           FROM "AcReceivable"
                           WHERE "AcctCode" IN ('310','320','330','990')
                           GROUP BY "CustNo"
                                  , "FacmNo"
                         ) AR ON AR."CustNo" = O1."CustNo"
                             AND AR."FacmNo" = O1."FacmNo"
               WHERE "AcctCode" = '990'
              ) S4 ON S4."AcctCode" = S1."AcctCode"
                  AND S4."CustNo" = S2."CustNo"
                  AND S4."FacmNo" = S2."FacmNo"
                  AND S4."BormNo" = S2."RvNo"
                  AND S4."AcSubBookCode" = S1."AcSubBookCode"
    WHERE NVL(S2."RvBal",0)
          - CASE
              WHEN S1."AcctCode" IN ('310','320','330')
              THEN NVL(S3."AcctMasterBal",0)
              WHEN S1."AcctCode" = '990'
              THEN NVL(S4."AcctMasterBal",0)
            ELSE 0 END <> 0
    ;

    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_DailyLoanBa_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;


/
