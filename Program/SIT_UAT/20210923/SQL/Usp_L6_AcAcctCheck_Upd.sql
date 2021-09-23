--------------------------------------------------------
--  DDL for Procedure Usp_L6_AcAcctCheck_Upd
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_L6_AcAcctCheck_Upd" 
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
    DELETE FROM "AcAcctCheck"
    WHERE "AcDate" = TBSDYF
    ;

    -- 寫入資料
    INSERT INTO "AcAcctCheck"
    SELECT TBSDYF                           AS "AcDate"          -- 會計日期 Decimald 8
          ,'0000'                           AS "BranchNo"        -- 單位別 VARCHAR2 4
          ,NVL(NVL(S2."CurrencyCode",S5."CurrencyCode"),'TWD')
                                            AS "CurrencyCode"    -- 幣別 VARCHAR2 3
          ,S1."AcctCode"                    AS "AcctCode"        -- 業務科目代號 VARCHAR2 3
          ,S1."AcctItem"                    AS "AcctItem"        -- 業務科目名稱 NVARCHAR2  20
          ,MAX(NVL(S5."TdBalSum",0))        AS "TdBal"           -- 本日餘額 DECIMAL 18 2
          ,SUM(CASE WHEN NVL(S5."TdBalSum",0) = 0 THEN 0 ELSE 1 END)
                                            AS "TdCnt"           -- 本日件數 DECIMAL 8
          -- 若起帳日與系統營業日(西元)相同,計入本日開戶件數
          ,SUM(CASE
                 WHEN S2."OpenAcDate" = TBSDYF
                 THEN 1
               ELSE 0 END)                  AS "TdNewCnt"        -- 本日開戶件數 DECIMAL 8
          -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷,計入本日結清件數
          ,SUM(CASE
                 WHEN S2."LastAcDate" = TBSDYF AND S2."ClsFlag" = 1
                 THEN 1
               ELSE 0 END)                  AS "TdClsCnt"        -- 本日結清件數 DECIMAL 8
          -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷 且 結案區分為 1:展期-一般 或 2:展期-協議
          -- 計入本日展期件數
          ,SUM(CASE
                 WHEN S2."LastAcDate" = TBSDYF
                      AND S2."ClsFlag" = 1
                      AND NVL(JSON_VALUE(S2."JsonFields",'$.CaseCloseCode' RETURNING NUMBER),0) IN (1,2)
                 THEN 1
               ELSE 0 END)                  AS "TdExtCnt"        -- 本日展期件數 DECIMAL 8
          -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷 且 結案區分為 1:展期-一般 或 2:展期-協議
          -- 將結案金額計入本日展期金額
          ,SUM(CASE
                 WHEN S2."LastAcDate" = TBSDYF
                      AND S2."ClsFlag" = 1
                      AND NVL(JSON_VALUE(S2."JsonFields",'$.CaseCloseCode' RETURNING NUMBER),0) IN (1,2)
                 THEN NVL(JSON_VALUE(S2."JsonFields",'$.CaseCloseAmt' RETURNING NUMBER),0.00)
               ELSE 0.00 END)               AS "TdExtAmt"        -- 本日展期金額 DECIMAL 18 2
          ,SUM(NVL(S2."RvBal",0))           AS "ReceivableBal"   -- 銷帳檔餘額 DECIMAL 18 2
          ,MAX(NVL(S3."AcctMasterBal",0))   AS "AcctMasterBal"   -- 業務檔餘額 DECIMAL 18 2
          ,"EmpNo"                          AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "CreateDate"      -- 建檔日期 DATE 
          ,"EmpNo"                          AS "LastUpdateEmpNo" -- 最後維護人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "LastUpdate"      -- 最後維護日期 DATE 
          ,S1."AcSubBookCode"               AS "AcSubBookCode"
    FROM (SELECT CAC."AcctCode"
               , CAC."AcctItem"
               , CDC."AcSubBookCode"
          FROM ( SELECT "AcctCode"
                      , "AcctItem"
                 FROM "CdAcCode"
                 WHERE "AcctCode" IN ('310','320','330','990')
                 GROUP BY "AcctCode","AcctItem"
               ) CAC
             , ( SELECT "Code" AS "AcSubBookCode"
                 FROM "CdCode" 
                 WHERE "DefCode" = 'AcSubBookCode'
                   AND "Enable" = 'Y'
               ) CDC
         ) S1
    LEFT JOIN "AcReceivable" S2 ON S2."AcctCode" = S1."AcctCode"
                               AND S2."AcctFlag" = 1 -- 篩選 業務科目記號 1: 資負明細科目
                               AND S2."AcctCode" IN ('310','320','330','990')
                               AND S2."AcSubBookCode" = S1."AcSubBookCode"
    LEFT JOIN (SELECT CASE
                        WHEN NVL(L2."BormNo",0) > 0 
                        THEN '990' 
                      ELSE F1."AcctCode" 
                      END              AS "AcctCode"
                    , AR."AcSubBookCode"
                    , SUM(CASE 
                            WHEN NVL(L2."BormNo",0) > 0 
                            THEN L2."OvduBal" 
                            ELSE L1."LoanBal" 
                          END
                         )             AS "AcctMasterBal"
               FROM "FacMain" F1
               LEFT JOIN "LoanBorMain" L1 ON L1."CustNo" = F1."CustNo"
                                         AND L1."FacmNo" = F1."FacmNo"
               LEFT JOIN "LoanOverdue" L2 ON L2."CustNo" = L1."CustNo"
                                       AND L2."FacmNo" = L1."FacmNo"
                                       AND L2."BormNo" = L1."BormNo"
                                       AND L1."Status" IN (2,6,7)
               LEFT JOIN ( SELECT "CustNo"
                                , "FacmNo"
                                , MAX("AcSubBookCode") AS "AcSubBookCode"
                           FROM "AcReceivable"
                           WHERE "AcctCode" IN ('310','320','330','990')
                           GROUP BY "CustNo"
                                  , "FacmNo"
                         ) AR ON AR."CustNo" = F1."CustNo"
                             AND AR."FacmNo" = F1."FacmNo"
               GROUP BY CASE WHEN NVL(L2."BormNo",0) > 0 THEN '990' ELSE F1."AcctCode" END
                      , AR."AcSubBookCode"
              ) S3 ON S3."AcctCode" = S1."AcctCode"
                  AND S3."AcSubBookCode" = S1."AcSubBookCode"
    LEFT JOIN (SELECT "AcctCode"
                    , "AcSubBookCode"
                    , "CurrencyCode"
                    , SUM("TdBal") AS "TdBalSum"
               FROM "AcMain"
               WHERE "AcctCode" IN ('310','320','330','990')
                 AND "AcDate" = TBSDYF
                 AND "AcBookCode" = '000'
               GROUP BY "AcctCode"
                      , "AcSubBookCode"
                      , "CurrencyCode"
              ) S5 ON S5."AcctCode" = S1."AcctCode"
                  AND S5."AcSubBookCode" = S1."AcSubBookCode"
    GROUP BY S1."AcctCode"
           , S1."AcSubBookCode"
           , NVL(S2."CurrencyCode",S5."CurrencyCode")
           , S1."AcctItem"
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
