CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L6_AcAcctCheck_Upd" 
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
    -- exec "Usp_L6_AcAcctCheck_Upd"(20200523,'001702');
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
    WITH "AcctCodeData" AS (
      SELECT CAC."AcctCode"
           , CAC."AcctItem"
           , CDC."AcSubBookCode"
           , 'TWD' AS "CurrencyCode"
      FROM ( SELECT "AcctCode"
                  , "AcctItem"
             FROM "CdAcCode"
             WHERE "AcctCode" IN ('310','320','330','340','990') -- xwh 20211124 added 340
             GROUP BY "AcctCode","AcctItem"
           ) CAC
         , ( SELECT "Code" AS "AcSubBookCode"
             FROM "CdCode" 
             WHERE "DefCode" = 'AcSubBookCode'
               AND "Enable" = 'Y'
           ) CDC
    )
    ,"AR" AS (
      SELECT "AcctCode"
           , "AcSubBookCode"
           , "CurrencyCode"
           , SUM(CASE
                   WHEN S2."RvBal" > 0
                   THEN 1
                 ELSE 0 END)                  AS "TdCnt"          -- 本日件數 DECIMAL 8
           -- 若起帳日與系統營業日(西元)相同,計入本日開戶件數
           , SUM(CASE
                   WHEN S2."OpenAcDate" = TBSDYF
                   THEN 1
                 ELSE 0 END)                  AS "TdNewCnt"        -- 本日開戶件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷,計入本日結清件數
           , SUM(CASE
                   WHEN S2."LastAcDate" = TBSDYF AND S2."ClsFlag" = 1
                   THEN 1
                 ELSE 0 END)                  AS "TdClsCnt"        -- 本日結清件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷 且 結案區分為 1:展期-一般 或 2:展期-協議
           -- 計入本日展期件數
           , SUM(CASE
                   WHEN S2."LastAcDate" = TBSDYF
                        AND S2."ClsFlag" = 1
                        AND NVL(JSON_VALUE(S2."JsonFields",'$.CaseCloseCode' RETURNING NUMBER),0) IN (1,2)
                   THEN 1
                 ELSE 0 END)                  AS "TdExtCnt"        -- 本日展期件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷 且 結案區分為 1:展期-一般 或 2:展期-協議
           -- 將結案金額計入本日展期金額
           , SUM(CASE
                   WHEN S2."LastAcDate" = TBSDYF
                        AND S2."ClsFlag" = 1
                        AND NVL(JSON_VALUE(S2."JsonFields",'$.CaseCloseCode' RETURNING NUMBER),0) IN (1,2)
                   THEN NVL(JSON_VALUE(S2."JsonFields",'$.CaseCloseAmt' RETURNING NUMBER),0.00)
                 ELSE 0.00 END)               AS "TdExtAmt"        -- 本日展期金額 DECIMAL 18 2
           , SUM("RvBal") AS "ReceivableBal"
      FROM "AcReceivable" S2
      WHERE S2."AcctFlag" = 1 -- 篩選 業務科目記號 1: 資負明細科目
        AND S2."AcctCode" IN ('310','320','330','340','990') -- xwh 20211124 added 340
      -- 2022-10-25 Wei: 排除預撥
        AND S2."OpenAcDate" <= TBSDYF
      GROUP BY "AcctCode"
             , "AcSubBookCode"
             , "CurrencyCode"
    )
    ,"Loan" AS (
      SELECT CASE
               WHEN NVL(L2."BormNo",0) > 0 
               THEN '990' 
             ELSE F1."AcctCode" 
             END              AS "AcctCode"
           , AR."AcSubBookCode"
           , 'TWD' AS "CurrencyCode"
           , SUM(CASE 
                   WHEN NVL(L2."BormNo",0) > 0 
                   THEN L2."OvduBal" 
                   ELSE L1."LoanBal" 
                 END
                )             AS "LoanBal"
            , SUM(CASE 
                   WHEN NVL(L2."BormNo",0) > 0 
                   THEN L2."OvduAmt" - L2."OvduBal" 
                   ELSE L1."DrawdownAmt" - L1."LoanBal" 
                 END
                )             AS "ClsAmt"
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
      -- 2022-10-25 Wei: 排除預撥
      WHERE L1."DrawdownDate" <= TBSDYF
      GROUP BY CASE WHEN NVL(L2."BormNo",0) > 0 THEN '990' ELSE F1."AcctCode" END
             , AR."AcSubBookCode"
    )
    ,"Ac" AS (
      SELECT "AcctCode"
           , "AcSubBookCode"
           , "CurrencyCode"
           , SUM("DbAmt") AS "DbAmtSum"
           , SUM("CrAmt") AS "CrAmtSum"
           , SUM("CoreDbAmt") AS "CoreDbAmtSum"
           , SUM("CoreCrAmt") AS "CoreCrAmtSum"
           , SUM("YdBal") AS "YdBalSum"
           , SUM("TdBal") AS "TdBalSum"
      FROM "AcMain"
      WHERE "AcctCode" IN ('310','320','330','340','990') -- xwh 20211124 added 340
        AND "AcDate" = TBSDYF
        AND "AcBookCode" = '000'
      GROUP BY "AcctCode"
             , "AcSubBookCode"
             , "CurrencyCode"
    )
    SELECT TBSDYF                           AS "AcDate"          -- 會計日期 Decimald 8
          ,'0000'                           AS "BranchNo"        -- 單位別 VARCHAR2 4
          ,S1."CurrencyCode"                AS "CurrencyCode"    -- 幣別 VARCHAR2 3
          ,S1."AcSubBookCode"               AS "AcSubBookCode"
          ,S1."AcctCode"                    AS "AcctCode"        -- 業務科目代號 VARCHAR2 3
          ,S1."AcctItem"                    AS "AcctItem"        -- 業務科目名稱 NVARCHAR2  20
          ,NVL(S4."TdBalSum",0)             AS "TdBal"           -- 本日餘額 DECIMAL 18 2
          ,NVL(S2."TdCnt",0)                AS "TdCnt"           -- 本日件數 DECIMAL 8
          ,NVL(S2."TdNewCnt",0)             AS "TdNewCnt"        -- 本日開戶件數 DECIMAL 8
          ,NVL(S2."TdClsCnt",0)             AS "TdClsCnt"        -- 本日結清件數 DECIMAL 8
          ,NVL(S2."TdExtCnt",0)             AS "TdExtCnt"        -- 本日展期件數 DECIMAL 8
          ,NVL(S2."TdExtAmt",0)             AS "TdExtAmt"        -- 本日展期金額 DECIMAL 18 2
          ,NVL(S2."ReceivableBal",0)        AS "ReceivableBal"   -- 銷帳檔餘額 DECIMAL 18 2
          ,NVL(S3."LoanBal",0)              AS "AcctMasterBal"   -- 業務檔餘額 DECIMAL 18 2
          ,NVL(S4."YdBalSum",0)             AS "YdBal"           -- 前日餘額 DECIMAL 18 2
          ,NVL(S4."DbAmtSum",0)             AS "DbAmt"           -- 借方金額 DECIMAL 18 2
          ,NVL(S4."CrAmtSum",0)             AS "CrAmt"           -- 貸方金額 DECIMAL 18 2
          ,NVL(S4."CoreDbAmtSum",0)         AS "CoreDbAmt"       -- 核心借方金額 DECIMAL 18 2
          ,NVL(S4."CoreCrAmtSum",0)         AS "CoreCrAmt"       -- 核心貸方金額 DECIMAL 18 2
          ,"EmpNo"                          AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "CreateDate"      -- 建檔日期 DATE 
          ,"EmpNo"                          AS "LastUpdateEmpNo" -- 最後維護人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "LastUpdate"      -- 最後維護日期 DATE 
          ,NVL(S3."ClsAmt",0)               AS "MasterClsAmt"    -- 業務檔已銷金額 DECIMAL 18 2
    FROM "AcctCodeData" S1
    LEFT JOIN "AR" S2 ON S2."AcctCode" = S1."AcctCode"
                     AND S2."AcSubBookCode" = S1."AcSubBookCode"
                     AND S2."CurrencyCode" = S1."CurrencyCode"
    LEFT JOIN "Loan" S3 ON S3."AcctCode" = S1."AcctCode"
                       AND S3."AcSubBookCode" = S1."AcSubBookCode"
                       AND S3."CurrencyCode" = S1."CurrencyCode"
    LEFT JOIN "Ac" S4 ON S4."AcctCode" = S1."AcctCode"
                     AND S4."AcSubBookCode" = S1."AcSubBookCode"
                     AND S4."CurrencyCode" = S1."CurrencyCode"
    ;

    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料-火險費用
    INSERT INTO "AcAcctCheck"
    WITH "AcctCodeData" AS (
      SELECT CAC."AcctCode"
           , CAC."AcctItem"
           , CDC."AcSubBookCode"
           , 'TWD' AS "CurrencyCode"
      FROM ( SELECT "AcctCode"
                  , "AcctItem"
             FROM "CdAcCode"
             WHERE "AcctCode" IN ( 'TMI' -- 火險保費
                                 , 'F09' -- 暫付火險保費
                                 , 'F25' -- 催收款項-火險費用
                                 ) -- Wei 2022-01-27
             GROUP BY "AcctCode","AcctItem"
           ) CAC
         , ( SELECT "Code" AS "AcSubBookCode"
             FROM "CdCode" 
             WHERE "DefCode" = 'AcSubBookCode'
               AND "Enable" = 'Y'
           ) CDC
    )
    ,"AR" AS (
      SELECT "AcctCode"
           , "AcSubBookCode"
           , "CurrencyCode"
           , SUM(CASE
                   WHEN S2."RvBal" > 0
                   THEN 1
                 ELSE 0 END)                  AS "TdCnt"          -- 本日件數 DECIMAL 8
           -- 若起帳日與系統營業日(西元)相同,計入本日開戶件數
           , SUM(CASE
                   WHEN S2."OpenAcDate" = TBSDYF
                   THEN 1
                 ELSE 0 END)                  AS "TdNewCnt"        -- 本日開戶件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷,計入本日結清件數
           , SUM(CASE
                   WHEN S2."LastAcDate" = TBSDYF AND S2."ClsFlag" = 1
                   THEN 1
                 ELSE 0 END)                  AS "TdClsCnt"        -- 本日結清件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷 且 結案區分為 1:展期-一般 或 2:展期-協議
           -- 計入本日展期件數
           , SUM(0)                           AS "TdExtCnt"        -- 本日展期件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷 且 結案區分為 1:展期-一般 或 2:展期-協議
           -- 將結案金額計入本日展期金額
           , SUM(0.00)                        AS "TdExtAmt"        -- 本日展期金額 DECIMAL 18 2
           , SUM("RvBal") AS "ReceivableBal"
      FROM "AcReceivable" S2
      WHERE S2."AcctFlag" = 0
        AND S2."AcctCode" IN ( 'F09' -- 暫付火險保費
                             , 'F25' -- 催收款項-火險費用
                             ) -- Wei 2022-01-27
      GROUP BY "AcctCode"
             , "AcSubBookCode"
             , "CurrencyCode"
    )
    ,"Insu" AS (
      SELECT CASE
               WHEN S1."StatusCode" = 0 THEN 'TMI'
               WHEN S1."StatusCode" = 1 THEN 'F09'
               WHEN S1."StatusCode" = 2 THEN 'F25'
             ELSE ' ' END           AS "AcctCode"
           , '00A'                  AS "AcSubBookCode"
           , 'TWD'                  AS "CurrencyCode"
           , SUM(CASE
                WHEN S1."StatusCode" IN (0) and S1."AcDate" > 0 AND  S1."InsuYearMonth" >= trunc(SP."InsuSettleDate" / 100)
                     THEN  S1."TotInsuPrem"
                WHEN S1."StatusCode" IN (1,2) and S1."AcDate" = 0
                     THEN  S1."TotInsuPrem"
                ELSE 0  END)        AS "InsuBal"
           , SUM(CASE
                WHEN S1."AcDate" > 0 THEN  S1."TotInsuPrem"
                ELSE 0  END)        AS "ClsAmt"
      FROM "InsuRenew" S1
      LEFT JOIN "SystemParas" SP ON SP."BusinessType" = 'LN'
      WHERE S1."RenewCode" = 2
        AND S1."TotInsuPrem" > 0
        AND S1."StatusCode" IN (0,1,2) 
      GROUP BY CASE
                 WHEN S1."StatusCode" = 0 THEN 'TMI'
                 WHEN S1."StatusCode" = 1 THEN 'F09'
                 WHEN S1."StatusCode" = 2 THEN 'F25'
               ELSE ' ' END
    )
    ,"Ac" AS (
      SELECT "AcctCode"
           , "AcSubBookCode"
           , "CurrencyCode"
           , SUM("DbAmt") AS "DbAmtSum"
           , SUM("CrAmt") AS "CrAmtSum"
           , SUM("CoreDbAmt") AS "CoreDbAmtSum"
           , SUM("CoreCrAmt") AS "CoreCrAmtSum"
           , SUM("YdBal") AS "YdBalSum"
           , SUM("TdBal") AS "TdBalSum"
      FROM "AcMain"
      WHERE "AcctCode" IN ('F09','F25','TMI') -- 2022-03-01 Wei
        AND "AcDate" = TBSDYF
        AND "AcBookCode" = '000'
      GROUP BY "AcctCode"
             , "AcSubBookCode"
             , "CurrencyCode"
    )
    SELECT TBSDYF                           AS "AcDate"          -- 會計日期 Decimald 8
          ,'0000'                           AS "BranchNo"        -- 單位別 VARCHAR2 4
          ,S1."CurrencyCode"                AS "CurrencyCode"    -- 幣別 VARCHAR2 3
          ,S1."AcSubBookCode"               AS "AcSubBookCode"
          ,S1."AcctCode"                    AS "AcctCode"        -- 業務科目代號 VARCHAR2 3
          ,S1."AcctItem"                    AS "AcctItem"        -- 業務科目名稱 NVARCHAR2  20
          ,NVL(S4."TdBalSum",0)             AS "TdBal"           -- 本日餘額 DECIMAL 18 2
          ,NVL(S2."TdCnt",0)                AS "TdCnt"           -- 本日件數 DECIMAL 8
          ,NVL(S2."TdNewCnt",0)             AS "TdNewCnt"        -- 本日開戶件數 DECIMAL 8
          ,NVL(S2."TdClsCnt",0)             AS "TdClsCnt"        -- 本日結清件數 DECIMAL 8
          ,NVL(S2."TdExtCnt",0)             AS "TdExtCnt"        -- 本日展期件數 DECIMAL 8
          ,NVL(S2."TdExtAmt",0)             AS "TdExtAmt"        -- 本日展期金額 DECIMAL 18 2
          ,NVL(S2."ReceivableBal",0)        AS "ReceivableBal"   -- 銷帳檔餘額 DECIMAL 18 2
          ,NVL(S3."InsuBal",0)              AS "AcctMasterBal"   -- 業務檔餘額 DECIMAL 18 2
          ,NVL(S4."YdBalSum",0)             AS "YdBal"           -- 前日餘額 DECIMAL 18 2
          ,NVL(S4."DbAmtSum",0)             AS "DbAmt"           -- 借方金額 DECIMAL 18 2
          ,NVL(S4."CrAmtSum",0)             AS "CrAmt"           -- 貸方金額 DECIMAL 18 2
          ,NVL(S4."CoreDbAmtSum",0)         AS "CoreDbAmt"       -- 核心借方金額 DECIMAL 18 2
          ,NVL(S4."CoreCrAmtSum",0)         AS "CoreCrAmt"       -- 核心貸方金額 DECIMAL 18 2
          ,"EmpNo"                          AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "CreateDate"      -- 建檔日期 DATE 
          ,"EmpNo"                          AS "LastUpdateEmpNo" -- 最後維護人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "LastUpdate"      -- 最後維護日期 DATE 
          ,NVL(S3."ClsAmt",0)               AS "MasterClsAmt"    -- 業務檔已銷金額 DECIMAL 18 2
    FROM "AcctCodeData" S1
    LEFT JOIN "AR" S2 ON S2."AcctCode" = S1."AcctCode"
                     AND S2."AcSubBookCode" = S1."AcSubBookCode"
                     AND S2."CurrencyCode" = S1."CurrencyCode"
    LEFT JOIN "Insu" S3 ON S3."AcctCode" = S1."AcctCode"
                       AND S3."AcSubBookCode" = S1."AcSubBookCode"
                       AND S3."CurrencyCode" = S1."CurrencyCode"
    LEFT JOIN "Ac" S4 ON S4."AcctCode" = S1."AcctCode"
                     AND S4."AcSubBookCode" = S1."AcSubBookCode"
                     AND S4."CurrencyCode" = S1."CurrencyCode"
    ;

    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料-法務費
    INSERT INTO "AcAcctCheck"
    WITH "AcctCodeData" AS (
      SELECT CAC."AcctCode"
           , CAC."AcctItem"
           , CDC."AcSubBookCode"
           , 'TWD' AS "CurrencyCode"
      FROM ( SELECT "AcctCode"
                  , "AcctItem"
             FROM "CdAcCode"
             WHERE "AcctCode" IN ( 'F07' -- 暫付法務費
                                 , 'F24' -- 催收款項-法務費用
                                 ) -- Wei 2022-01-27
             GROUP BY "AcctCode","AcctItem"
           ) CAC
         , ( SELECT "Code" AS "AcSubBookCode"
             FROM "CdCode" 
             WHERE "DefCode" = 'AcSubBookCode'
               AND "Enable" = 'Y'
           ) CDC
    )
    ,"AR" AS (
      SELECT "AcctCode"
           , "AcSubBookCode"
           , "CurrencyCode"
           , SUM(CASE
                   WHEN S2."RvBal" > 0
                   THEN 1
                 ELSE 0 END)                  AS "TdCnt"          -- 本日件數 DECIMAL 8
           -- 若起帳日與系統營業日(西元)相同,計入本日開戶件數
           , SUM(CASE
                   WHEN S2."OpenAcDate" = TBSDYF
                   THEN 1
                 ELSE 0 END)                  AS "TdNewCnt"        -- 本日開戶件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷,計入本日結清件數
           , SUM(CASE
                   WHEN S2."LastAcDate" = TBSDYF AND S2."ClsFlag" = 1
                   THEN 1
                 ELSE 0 END)                  AS "TdClsCnt"        -- 本日結清件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷 且 結案區分為 1:展期-一般 或 2:展期-協議
           -- 計入本日展期件數
           , SUM(0)                           AS "TdExtCnt"        -- 本日展期件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷 且 結案區分為 1:展期-一般 或 2:展期-協議
           -- 將結案金額計入本日展期金額
           , SUM(0.00)                        AS "TdExtAmt"        -- 本日展期金額 DECIMAL 18 2
           , SUM("RvBal") AS "ReceivableBal"
      FROM "AcReceivable" S2
      WHERE S2."AcctFlag" = 0
        AND S2."AcctCode" IN ( 'F07' -- 暫付法務費
                             , 'F24' -- 催收款項-法務費用
                             ) -- Wei 2022-01-27
      GROUP BY "AcctCode"
             , "AcSubBookCode"
             , "CurrencyCode"
    )
    ,"Law" AS (
      SELECT CASE
               WHEN S1."OverdueDate" > 0 THEN 'F24'
             ELSE 'F07' END         AS "AcctCode"
           , '00A'                  AS "AcSubBookCode"
           , 'TWD'                  AS "CurrencyCode"
           , SUM(CASE WHEN  S1."CloseDate" = 0 THEN S1."Fee"
                      ELSE 0 END )  AS "LawFeeBal"
           , SUM(CASE WHEN  S1."CloseDate" > 0 THEN S1."Fee"
                      ELSE 0 END )  AS "ClsAmt"
      FROM "ForeclosureFee" S1
      GROUP BY CASE
                 WHEN S1."OverdueDate" > 0 THEN 'F24'
               ELSE 'F07' END
    )
    ,"Ac" AS (
      SELECT "AcctCode"
           , "AcSubBookCode"
           , "CurrencyCode"
           , SUM("DbAmt") AS "DbAmtSum"
           , SUM("CrAmt") AS "CrAmtSum"
           , SUM("CoreDbAmt") AS "CoreDbAmtSum"
           , SUM("CoreCrAmt") AS "CoreCrAmtSum"
           , SUM("YdBal") AS "YdBalSum"
           , SUM("TdBal") AS "TdBalSum"
      FROM "AcMain"
      WHERE "AcctCode" IN ('F07','F24') -- 2022-03-01 Wei
        AND "AcDate" = TBSDYF
        AND "AcBookCode" = '000'
      GROUP BY "AcctCode"
             , "AcSubBookCode"
             , "CurrencyCode"
    )
    SELECT TBSDYF                           AS "AcDate"          -- 會計日期 Decimald 8
          ,'0000'                           AS "BranchNo"        -- 單位別 VARCHAR2 4
          ,S1."CurrencyCode"                AS "CurrencyCode"    -- 幣別 VARCHAR2 3
          ,S1."AcSubBookCode"               AS "AcSubBookCode"
          ,S1."AcctCode"                    AS "AcctCode"        -- 業務科目代號 VARCHAR2 3
          ,S1."AcctItem"                    AS "AcctItem"        -- 業務科目名稱 NVARCHAR2  20
          ,NVL(S4."TdBalSum",0)             AS "TdBal"           -- 本日餘額 DECIMAL 18 2
          ,NVL(S2."TdCnt",0)                AS "TdCnt"           -- 本日件數 DECIMAL 8
          ,NVL(S2."TdNewCnt",0)             AS "TdNewCnt"        -- 本日開戶件數 DECIMAL 8
          ,NVL(S2."TdClsCnt",0)             AS "TdClsCnt"        -- 本日結清件數 DECIMAL 8
          ,NVL(S2."TdExtCnt",0)             AS "TdExtCnt"        -- 本日展期件數 DECIMAL 8
          ,NVL(S2."TdExtAmt",0)             AS "TdExtAmt"        -- 本日展期金額 DECIMAL 18 2
          ,NVL(S2."ReceivableBal",0)        AS "ReceivableBal"   -- 銷帳檔餘額 DECIMAL 18 2
          ,NVL(S3."LawFeeBal",0)            AS "AcctMasterBal"   -- 業務檔餘額 DECIMAL 18 2
          ,NVL(S4."YdBalSum",0)             AS "YdBal"           -- 前日餘額 DECIMAL 18 2
          ,NVL(S4."DbAmtSum",0)             AS "DbAmt"           -- 借方金額 DECIMAL 18 2
          ,NVL(S4."CrAmtSum",0)             AS "CrAmt"           -- 貸方金額 DECIMAL 18 2
          ,NVL(S4."CoreDbAmtSum",0)         AS "CoreDbAmt"       -- 核心借方金額 DECIMAL 18 2
          ,NVL(S4."CoreCrAmtSum",0)         AS "CoreCrAmt"       -- 核心貸方金額 DECIMAL 18 2
          ,"EmpNo"                          AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "CreateDate"      -- 建檔日期 DATE 
          ,"EmpNo"                          AS "LastUpdateEmpNo" -- 最後維護人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "LastUpdate"      -- 最後維護日期 DATE 
          ,NVL(S3."ClsAmt",0)               AS "MasterClsAmt"    -- 業務檔已銷金額 DECIMAL 18 2
    FROM "AcctCodeData" S1
    LEFT JOIN "AR" S2 ON S2."AcctCode" = S1."AcctCode"
                     AND S2."AcSubBookCode" = S1."AcSubBookCode"
                     AND S2."CurrencyCode" = S1."CurrencyCode"
    LEFT JOIN "Law" S3 ON S3."AcctCode" = S1."AcctCode"
                      AND S3."AcSubBookCode" = S1."AcSubBookCode"
                      AND S3."CurrencyCode" = S1."CurrencyCode"
    LEFT JOIN "Ac" S4 ON S4."AcctCode" = S1."AcctCode"
                     AND S4."AcSubBookCode" = S1."AcSubBookCode"
                     AND S4."CurrencyCode" = S1."CurrencyCode"
    ;

    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料-帳管費 & 契變手續費 & 暫收款
    INSERT INTO "AcAcctCheck"
    WITH "AcctCodeData" AS (
      SELECT CAC."AcctCode"
           , CAC."AcctItem"
           , CDC."AcSubBookCode"
           , 'TWD' AS "CurrencyCode"
      FROM ( SELECT "AcctCode"
                  , "AcctItem"
             FROM "CdAcCode"
             WHERE "AcctCode" IN ( 'F10' -- 帳管費
                             , 'F29' -- 契變手續費
                             , 'F12' -- 聯貸件
                             , 'F27' -- 聯貸管理費
                             , 'TAV' -- 暫收款
                             , 'TCK' -- 2023-01-30 Wei from Lai
                             , 'TAM' -- 2023-01-30 Wei from Lai
                             , 'TRO' -- 2023-01-30 Wei from Lai
                             , 'TLD' -- 2023-01-30 Wei from Lai
                             , 'TSL' -- 2023-01-30 Wei from Lai
                             , 'T10' -- 2023-01-30 Wei from Lai
                             , 'T11' -- 2023-01-30 Wei from Lai
                             , 'T12' -- 2023-01-30 Wei from Lai
                             , 'T13' -- 2023-01-30 Wei from Lai
                             ) -- Wei 2022-01-27
             GROUP BY "AcctCode","AcctItem"
           ) CAC
         , ( SELECT "Code" AS "AcSubBookCode"
             FROM "CdCode" 
             WHERE "DefCode" = 'AcSubBookCode'
               AND "Enable" = 'Y'
           ) CDC
    )
    ,"AR" AS (
      SELECT "AcctCode"
           , "AcSubBookCode"
           , "CurrencyCode"
           , SUM(CASE
                   WHEN S2."RvBal" > 0
                   THEN 1
                 ELSE 0 END)                  AS "TdCnt"          -- 本日件數 DECIMAL 8
           -- 若起帳日與系統營業日(西元)相同,計入本日開戶件數
           , SUM(CASE
                   WHEN S2."OpenAcDate" = TBSDYF
                   THEN 1
                 ELSE 0 END)                  AS "TdNewCnt"        -- 本日開戶件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷,計入本日結清件數
           , SUM(CASE
                   WHEN S2."LastAcDate" = TBSDYF AND S2."ClsFlag" = 1
                   THEN 1
                 ELSE 0 END)                  AS "TdClsCnt"        -- 本日結清件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷 且 結案區分為 1:展期-一般 或 2:展期-協議
           -- 計入本日展期件數
           , SUM(0)                           AS "TdExtCnt"        -- 本日展期件數 DECIMAL 8
           -- 若最後作帳日與系統營業日(西元)相同 且 銷帳記號為1: 已銷 且 結案區分為 1:展期-一般 或 2:展期-協議
           -- 將結案金額計入本日展期金額
           , SUM(0.00)                        AS "TdExtAmt"        -- 本日展期金額 DECIMAL 18 2
           , SUM("RvBal") AS "ReceivableBal"
           , SUM(CASE
                   WHEN S2."ClsFlag" = 1
                   THEN S2."RvAmt" 
                 ELSE 0 END)                  AS "ClsAmt"          -- 已銷金額 DECIMAL 18 2
      FROM "AcReceivable" S2
      WHERE S2."AcctFlag" = 0
        AND S2."AcctCode" IN ( 'F10' -- 帳管費
                             , 'F29' -- 契變手續費
                             , 'F12' -- 聯貸件
                             , 'F27' -- 聯貸管理費
                             , 'TAV' -- 暫收款
                             , 'TCK' -- 2023-01-30 Wei from Lai
                             , 'TAM' -- 2023-01-30 Wei from Lai
                             , 'TRO' -- 2023-01-30 Wei from Lai
                             , 'TLD' -- 2023-01-30 Wei from Lai
                             , 'TSL' -- 2023-01-30 Wei from Lai
                             , 'T10' -- 2023-01-30 Wei from Lai
                             , 'T11' -- 2023-01-30 Wei from Lai
                             , 'T12' -- 2023-01-30 Wei from Lai
                             , 'T13' -- 2023-01-30 Wei from Lai
                             ) -- Wei 2022-01-27
      GROUP BY "AcctCode"
             , "AcSubBookCode"
             , "CurrencyCode"
    )
    ,"Ac" AS (
      SELECT "AcctCode"
           , "AcSubBookCode"
           , "CurrencyCode"
           , SUM("DbAmt") AS "DbAmtSum"
           , SUM("CrAmt") AS "CrAmtSum"
           , SUM("CoreDbAmt") AS "CoreDbAmtSum"
           , SUM("CoreCrAmt") AS "CoreCrAmtSum"
           , SUM("YdBal") AS "YdBalSum"
           , SUM("TdBal") AS "TdBalSum"
      FROM "AcMain"
      WHERE "AcctCode" IN ('TAV'
                          ,'TCK' -- 2023-01-30 Wei from Lai
                          ,'TAM' -- 2023-01-30 Wei from Lai
                          ,'TRO' -- 2023-01-30 Wei from Lai
                          ,'TLD' -- 2023-01-30 Wei from Lai
                          ,'TSL' -- 2023-01-30 Wei from Lai
                          ,'T10' -- 2023-01-30 Wei from Lai
                          ,'T11' -- 2023-01-30 Wei from Lai
                          ,'T12' -- 2023-01-30 Wei from Lai
                          ,'T13' -- 2023-01-30 Wei from Lai
                          ) -- 2022-03-01 Wei
        AND "AcDate" = TBSDYF
        AND "AcBookCode" = '000'
      GROUP BY "AcctCode"
             , "AcSubBookCode"
             , "CurrencyCode"
    )
    SELECT TBSDYF                           AS "AcDate"          -- 會計日期 Decimald 8
          ,'0000'                           AS "BranchNo"        -- 單位別 VARCHAR2 4
          ,S1."CurrencyCode"                AS "CurrencyCode"    -- 幣別 VARCHAR2 3
          ,S1."AcSubBookCode"               AS "AcSubBookCode"
          ,S1."AcctCode"                    AS "AcctCode"        -- 業務科目代號 VARCHAR2 3
          ,S1."AcctItem"                    AS "AcctItem"        -- 業務科目名稱 NVARCHAR2  20
          ,NVL(S4."TdBalSum",0)             AS "TdBal"           -- 本日餘額 DECIMAL 18 2
          ,NVL(S2."TdCnt",0)                AS "TdCnt"           -- 本日件數 DECIMAL 8
          ,NVL(S2."TdNewCnt",0)             AS "TdNewCnt"        -- 本日開戶件數 DECIMAL 8
          ,NVL(S2."TdClsCnt",0)             AS "TdClsCnt"        -- 本日結清件數 DECIMAL 8
          ,NVL(S2."TdExtCnt",0)             AS "TdExtCnt"        -- 本日展期件數 DECIMAL 8
          ,NVL(S2."TdExtAmt",0)             AS "TdExtAmt"        -- 本日展期金額 DECIMAL 18 2
          ,NVL(S2."ReceivableBal",0)        AS "ReceivableBal"   -- 銷帳檔餘額 DECIMAL 18 2
          ,NVL(S2."ReceivableBal",0)        AS "AcctMasterBal"   -- 業務檔餘額 DECIMAL 18 2
          ,NVL(S4."YdBalSum",0)             AS "YdBal"           -- 前日餘額 DECIMAL 18 2
          ,NVL(S4."DbAmtSum",0)             AS "DbAmt"           -- 借方金額 DECIMAL 18 2
          ,NVL(S4."CrAmtSum",0)             AS "CrAmt"           -- 貸方金額 DECIMAL 18 2
          ,NVL(S4."CoreDbAmtSum",0)         AS "CoreDbAmt"       -- 核心借方金額 DECIMAL 18 2
          ,NVL(S4."CoreCrAmtSum",0)         AS "CoreCrAmt"       -- 核心貸方金額 DECIMAL 18 2
          ,"EmpNo"                          AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "CreateDate"      -- 建檔日期 DATE 
          ,"EmpNo"                          AS "LastUpdateEmpNo" -- 最後維護人員 VARCHAR2 6
          ,JOB_START_TIME                   AS "LastUpdate"      -- 最後維護日期 DATE 
          ,NVL(S2."ClsAmt",0)               AS "MasterClsAmt"    -- 業務檔已銷金額 DECIMAL 18 2
    FROM "AcctCodeData" S1
    LEFT JOIN "AR" S2 ON S2."AcctCode" = S1."AcctCode"
                     AND S2."AcSubBookCode" = S1."AcSubBookCode"
                     AND S2."CurrencyCode" = S1."CurrencyCode"
    LEFT JOIN "Ac" S4 ON S4."AcctCode" = S1."AcctCode"
                     AND S4."AcSubBookCode" = S1."AcSubBookCode"
                     AND S4."CurrencyCode" = S1."CurrencyCode"
    ;

    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L6_AcAcctCheck_Upd' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , "EmpNo" -- 發動預存程序的員工編號
    );
  END;
END;