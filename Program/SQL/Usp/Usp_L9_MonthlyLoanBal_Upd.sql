create or replace NONEDITIONABLE PROCEDURE "Usp_L9_MonthlyLoanBal_Upd" 
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;        -- 新增筆數 
    UPD_CNT        INT;        -- 更新筆數 
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間 
    YYYYMM         INT;         -- 本月年月
    LYYYYMM        INT;         -- 上月年月
    MM             INT;         -- 本月月份
    YYYY           INT;         -- 本月年度   
  BEGIN   
    INS_CNT :=0;       
    UPD_CNT :=0;   

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    --　本月年月
    YYYYMM := TBSDYF / 100;
    --
    MM := MOD(YYYYMM, 100);
    YYYY := TRUNC(YYYYMM / 100);
    IF MM = 1 THEN
       LYYYYMM := (YYYY - 1) * 100 + 12;
    ELSE
       LYYYYMM := YYYYMM - 1;
    END IF;
    --
    -- 刪除舊資料
    -- DBMS_OUTPUT.PUT_LINE('DELETE MonthlyLoanBal');

    DELETE FROM "MonthlyLoanBal"
    WHERE "YearMonth" = YYYYMM
    ; 

    -- 寫入資料
    -- DBMS_OUTPUT.PUT_LINE('INSERT MonthlyLoanBal');

    INSERT INTO "MonthlyLoanBal"
    WITH "DailyData" AS (
        SELECT D."MonthEndYm"             --AS "YearMonth"           -- 資料年月
              ,D."CustNo"                 --AS "CustNo"              -- 戶號 
              ,D."FacmNo"                 --AS "FacmNo"              -- 額度 
              ,D."BormNo"                 --AS "BormNo"              -- 撥款序號 
              ,D."AcctCode"               --AS "AcctCode"            -- 業務科目代號  
              ,D."FacAcctCode"            --AS "FacAcctCode"         -- 額度業務科目
              ,D."CurrencyCode"           --AS "CurrencyCode"        -- 幣別 
              ,D."LoanBalance"            --AS "LoanBalance"         -- 放款餘額
              ,D."StoreRate"              --AS "StoreRate"           -- 計息利率
              ,D."ProdNo"                 --AS "ProdNo"              -- 商品代碼
        FROM "DailyLoanBal" D
        WHERE D."MonthEndYm" = YYYYMM
    ) 
    , "ClData" AS (
        SELECT CF."CustNo"
             , CF."FacmNo"
             , CF."ClCode1"
             , CF."ClCode2"
             , CF."ClNo"
             , CM."CityCode"
             , ROW_NUMBER()
               OVER (
                 PARTITION BY CF."CustNo"
                            , CF."FacmNo"
                 ORDER BY CF."ClCode1"
                        , CF."ClCode2"
                        , CF."ClNo"
               ) AS "ClSeq"
        FROM "ClFac" CF
        LEFT JOIN "ClMain" CM ON CM."ClCode1" = CF."ClCode1"
                             AND CM."ClCode2" = CF."ClCode2"
                             AND CM."ClNo" = CF."ClNo"
        WHERE CF."MainFlag" = 'Y'
    )
    , AR AS (
        SELECT A."AcctCode"
              ,A."CustNo"
              ,A."FacmNo"
              ,A."AcBookCode"  
              ,A."AcSubBookCode"  
             , ROW_NUMBER()
               OVER (
                 PARTITION BY A."AcctCode"
                            , A."CustNo"
                            , A."FacmNo"
                 ORDER BY A."RvNo"
               ) AS "ArSeq"
        FROM "AcReceivable" A
        WHERE A."AcctCode" LIKE '3%'
           OR A."AcctCode" = '990' 
    )
    SELECT D."MonthEndYm"             AS "YearMonth"           -- 資料年月
          ,D."CustNo"                 AS "CustNo"              -- 戶號 
          ,D."FacmNo"                 AS "FacmNo"              -- 額度 
          ,D."BormNo"                 AS "BormNo"              -- 撥款序號 
          ,D."AcctCode"               AS "AcctCode"            -- 業務科目代號  
          ,D."FacAcctCode"            AS "FacAcctCode"         -- 額度業務科目
          ,D."CurrencyCode"           AS "CurrencyCode"        -- 幣別 
          ,D."LoanBalance"            AS "LoanBalance"         -- 放款餘額
          ,0                          AS "MaxLoanBal"          -- 最高放款餘額 DECIMAL 16 2
          ,D."StoreRate"              AS "StoreRate"           -- 計息利率
          ,0                          AS "IntAmtRcv"           -- 實收利息     
          ,0                          AS "IntAmtAcc"           -- 提存利息  
          ,0                          AS "UnpaidInt"           -- 已到期未繳息 DECIMAL 16 2
          ,0                          AS "UnexpiredInt"        -- 未到期應收息 DECIMAL 16 2
          ,0                          AS "SumRcvInt"           -- 累計回收利息 DECIMAL 16 2
          ,0                          AS "IntAmt"              -- 本月利息
          ,D."ProdNo"                 AS "ProdNo"              -- 商品代碼
          ,A."AcBookCode"             AS "AcBookCode"          -- 帳冊別             
          ,C."EntCode"                AS "EntCode"             -- 企金別             
          ,NULL                       AS "RelsCode"            -- (準)利害關係人職稱      
          ,F."DepartmentCode"         AS "DepartmentCode"      -- 案件隸屬單位          
          ,NVL(CL."ClCode1",0)        AS "ClCode1"             -- 擔保品代號1            
          ,NVL(CL."ClCode2",0)        AS "ClCode2"             -- 擔保品代號2  
          ,NVL(CL."ClNo",0)           AS "ClNo"                -- 主要擔保品編號
          ,CL."CityCode"              AS "CityCode"            -- 主要擔保品地區別    
          ,0                          AS "OvduPrinAmt"         -- 轉催收本金 DECIMAL 16 2 
          ,0                          AS "OvduIntAmt"          -- 轉催收利息 DECIMAL 16 2 
          ,JOB_START_TIME             AS "CreateDate"          -- 建檔日期時間  
          ,EmpNo                      AS "CreateEmpNo"         -- 建檔人員 
          ,JOB_START_TIME             AS "LastUpdate"          -- 最後更新日期時間  
          ,EmpNo                      AS "LastUpdateEmpNo"     -- 最後更新人員 
          ,A."AcSubBookCode"          AS "AcSubBookCode"       -- 區隔帳冊       
    FROM "DailyData" D
    LEFT JOIN "FacMain" F ON F."CustNo" = D."CustNo"
                         AND F."FacmNo" = D."FacmNo"
    LEFT JOIN "CustMain" C ON  C."CustNo" = D."CustNo"
    LEFT JOIN "ClData" CL ON CL."CustNo" = D."CustNo"
                         AND CL."FacmNo" = D."FacmNo"
                         AND CL."ClSeq" = 1
    LEFT JOIN AR A ON A."AcctCode" = D."AcctCode"
                  AND A."CustNo"   = D."CustNo"
                  AND A."FacmNo"   = D."FacmNo"
                  AND A."ArSeq"    = 1
    ;

    INS_CNT := INS_CNT + sql%rowcount;

    -- DBMS_OUTPUT.PUT_LINE('UPDATE ClCode1 END');

    MERGE INTO "MonthlyLoanBal" M
    USING (SELECT "CustNo"
                 ,"FacmNo"
                 ,"BormNo" 
                 ,SUM(CASE WHEN "DbCr" = 'C' THEN "TxAmt"
                           ELSE              0 - "TxAmt"
                      END ) AS "IntAmtRcv"
            FROM "AcDetail"             -- 會計帳務明細檔
            WHERE "CustNo"  > 0 
             AND  TRUNC("AcDate"/100)  = YYYYMM
             AND  "AcctCode" IN ('IC1','IC2','IC3','IC4')
            GROUP BY "CustNo", "FacmNo", "BormNo"
           ) T
--IC1	短擔息
--IC2	中擔息
--IC3	長擔息
--IC4	三十年房貸息

    ON (    M."YearMonth" = YYYYMM
        AND M."CustNo"  = T."CustNo"
        AND M."FacmNo"  = T."FacmNo"
        AND M."BormNo"  = T."BormNo" 
       )
    WHEN MATCHED THEN UPDATE SET M."IntAmtRcv" = T."IntAmtRcv" 
    ;      

    MERGE INTO "MonthlyLoanBal" M
    USING (SELECT "CustNo"
                 ,"FacmNo"
                 ,"BormNo" 
                 ,SUM("Interest") AS "IntAmtAcc"
            FROM "AcLoanInt"         --提息明細檔
            WHERE "YearMonth" = YYYYMM  -- 提息年月 = 本月
             AND  "AcctCode" IN ('IC1','IC2','IC3','IC4')
            GROUP BY "CustNo", "FacmNo", "BormNo"
           ) T
    ON (    M."YearMonth" = YYYYMM
        AND M."CustNo"  = T."CustNo"
        AND M."FacmNo"  = T."FacmNo"
        AND M."BormNo"  = T."BormNo" 
       )
    WHEN MATCHED THEN UPDATE SET M."IntAmtAcc" = T."IntAmtAcc"    
    ;       

--  更新 IntAmt	本月利息收入= 本月實收利息+本月提存利息-上月提存利息
    -- DBMS_OUTPUT.PUT_LINE('UPDATE IntAmt');

    MERGE INTO "MonthlyLoanBal" M
    USING (SELECT "CustNo"
                 ,"FacmNo"
                 ,"BormNo"
                 ,SUM("IntAmt") AS "IntAmt"
           FROM ( SELECT D."CustNo"
                        ,D."FacmNo"
                        ,D."BormNo"
                        ,D."IntAmtRcv" + D."IntAmtAcc" AS "IntAmt"
                  FROM "MonthlyLoanBal" D 
                  WHERE D."YearMonth" = YYYYMM
                  UNION ALL
                  SELECT D."CustNo"
                        ,D."FacmNo"
                        ,D."BormNo"
                        ,0 - D."IntAmtAcc" AS "IntAmt" 
                  FROM "MonthlyLoanBal" D 
                  WHERE D."YearMonth" = LYYYYMM
                ) S
          GROUP BY "CustNo", "FacmNo", "BormNo" ) D
    ON (    M."YearMonth" = YYYYMM
        AND M."CustNo"    = D."CustNo"
        AND M."FacmNo"    = D."FacmNo"
        AND M."BormNo"    = D."BormNo" )
    WHEN MATCHED THEN UPDATE SET M."IntAmt" = D."IntAmt"; 

    -- DBMS_OUTPUT.PUT_LINE('UPDATE IntAmt END');

    UPD_CNT := UPD_CNT + sql%rowcount;

    -- 更新最高放款餘額
    MERGE INTO "MonthlyLoanBal" T0
    USING (
      SELECT TRUNC(D."DataDate" / 100)  AS "YearMonth"  -- 資料年月
           , D."CustNo"                 AS "CustNo"     -- 戶號
           , D."FacmNo"                 AS "FacmNo"     -- 額度
           , D."BormNo"                 AS "BormNo"     -- 撥款序號
           , MAX(D."LoanBalance")       AS "MaxLoanBal" -- 最高放款餘額
      FROM "DailyLoanBal" D
      WHERE TRUNC(D."DataDate" / 100) = YYYYMM
        AND D."LoanBalance" > 0
      GROUP BY TRUNC(D."DataDate" / 100)
             , D."CustNo" 
             , D."FacmNo"
             , D."BormNo"
    ) S0
    ON (
      T0."YearMonth" = S0."YearMonth"
      AND T0."CustNo" = S0."CustNo"
      AND T0."FacmNo" = S0."FacmNo"
      AND T0."BormNo" = S0."BormNo"
      AND S0."MaxLoanBal" > 0
    )
    WHEN MATCHED THEN UPDATE SET
    T0."MaxLoanBal" = S0."MaxLoanBal"
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_MonthlyLoanBal_Upd' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;