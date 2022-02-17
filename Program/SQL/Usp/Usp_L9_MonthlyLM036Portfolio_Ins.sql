-- 程式功能：維護 MonthlyLM036Portfolio LM036Portfolio
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L9_MonthlyLM036Portfolio_Ins"(20200430,'999999');
--

CREATE OR REPLACE PROCEDURE "Usp_L9_MonthlyLM036Portfolio_Ins"
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
  BEGIN
    INS_CNT :=0;
    UPD_CNT :=0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    --　本月年月
    YYYYMM := TBSDYF / 100;

    -- 刪除舊資料
    DELETE FROM "MonthlyLM036Portfolio"
    WHERE "DataMonth" = YYYYMM
    ;

    -- 寫入資料
    INSERT INTO "MonthlyLM036Portfolio"
    SELECT YYYYMM                          AS "DataMonth" -- 資料年月 DECIMAL 6  
         , 0                               AS "MonthEndDate" -- 月底日期 DECIMAL 8  
         , S0."NaturalPersonLoanBal"
           + S0."LegalPersonLoanBal"
           + S2."AmortizeTotal"
           + S1."OvduExpense"              AS "PortfolioTotal" -- 授信組合餘額 DECIMAL 16 2 自然人放款+法人放款+溢折價與催收費用
         , S0."NaturalPersonLoanBal"       AS "NaturalPersonLoanBal" -- 自然人放款 DECIMAL 16 2 
         , S0."LegalPersonLoanBal"         AS "LegalPersonLoanBal" -- 法人放款 DECIMAL 16 2 
         , S0."SyndLoanBal"                AS "SyndLoanBal" -- 聯貸案 DECIMAL 16 2 法人放款之細項
         , S0."StockLoanBal"               AS "StockLoanBal" -- 股票質押 DECIMAL 16 2 法人放款之細項
         , S0."OtherLoanBal"               AS "OtherLoanbal" -- 一般法人放款 DECIMAL 16 2 法人放款之細項
         , S2."AmortizeTotal"              AS "AmortizeTotal" -- 溢折價 DECIMAL 16 2 
         , S1."OvduExpense"                AS "OvduExpense" -- 催收費用 DECIMAL 16 2 
         , S0."NaturalPersonLargeCounts"   AS "NaturalPersonLargeCounts" -- 自然人大額授信件件數 DECIMAL 16  自然人放款一千萬以上
         , S0."NaturalPersonLargeTotal"    AS "NaturalPersonLargeTotal" -- 自然人大額授信件餘額 DECIMAL 16 2 自然人放款一千萬以上
         , S0."LegalPersonLargeCounts"     AS "LegalPersonLargeCounts" -- 法人大額授信件件數 DECIMAL 16  法人放款三千萬以上
         , S0."LegalPersonLargeTotal"      AS "LegalPersonLargeTotal" -- 法人大額授信件餘額 DECIMAL 16 2 法人放款三千萬以上
         , CASE
             WHEN ( S0."NaturalPersonLoanBal"
                  + S0."LegalPersonLoanBal"
                  + S2."AmortizeTotal"
                  + S1."OvduExpense"
                  ) > 0
             THEN ROUND(S0."NaturalPersonLoanBal" 
                       / ( S0."NaturalPersonLoanBal"
                         + S0."LegalPersonLoanBal"
                         + S2."AmortizeTotal"
                         + S1."OvduExpense"
                         )
                       , 4)
           ELSE 0
           END                             AS "NaturalPersonPercent" -- 自然人放款占比 DECIMAL 6 2 百分比
         , CASE
             WHEN ( S0."NaturalPersonLoanBal"
                  + S0."LegalPersonLoanBal"
                  + S2."AmortizeTotal"
                  + S1."OvduExpense"
                  ) > 0
             THEN ROUND(S0."LegalPersonLoanBal" 
                       / ( S0."NaturalPersonLoanBal"
                         + S0."LegalPersonLoanBal"
                         + S2."AmortizeTotal"
                         + S1."OvduExpense"
                         )
                       , 4)
           ELSE 0
           END                             AS "LegalPersonPercent" -- 法人放款占比 DECIMAL 6 2 百分比
         , 0                               AS "SyndPercent" -- 聯貸案占比 DECIMAL 6 2 百分比
         , CASE
             WHEN ( S0."NaturalPersonLoanBal"
                  + S0."LegalPersonLoanBal"
                  + S2."AmortizeTotal"
                  + S1."OvduExpense"
                  ) > 0
             THEN ROUND(S0."StockLoanBal" 
                       / ( S0."NaturalPersonLoanBal"
                         + S0."LegalPersonLoanBal"
                         + S2."AmortizeTotal"
                         + S1."OvduExpense"
                         )
                       , 4)
           ELSE 0
           END                             AS "StockPercent" -- 股票質押占比 DECIMAL 6 2 百分比
         , 0                               AS "OtherPercent" -- 一般法人放款占比 DECIMAL 6 2 百分比
         , 0                               AS "EntUsedPercent" -- 企業放款動用率 DECIMAL 6 2 百分比
         , 0                               AS "InsuDividendRate" -- 保單分紅利率 DECIMAL 6 2 百分比
         , 0                               AS "NaturalPersonRate" -- 自然人當月平均利率 DECIMAL 6 2 百分比
         , 0                               AS "LegalPersonRate" -- 法人當月平均利率 DECIMAL 6 2 百分比
         , 0                               AS "SyndRate" -- 聯貸案平均利率 DECIMAL 6 2 百分比
         , 0                               AS "StockRate" -- 股票質押平均利率 DECIMAL 6 2 百分比
         , 0                               AS "OtherRate" -- 一般法人放款平均利率 DECIMAL 6 2 百分比
         , 0                               AS "AvgRate" -- 放款平均利率 DECIMAL 6 2 百分比
         , 0                               AS "HouseRateOfReturn" -- 房貸通路當月毛報酬率 DECIMAL 6 2 百分比
         , 0                               AS "EntRateOfReturn" -- 企金通路當月毛報酬率 DECIMAL 6 2 百分比
         , 0                               AS "RateOfReturn" -- 放款毛報酬率 DECIMAL 6 2 百分比
         , JOB_START_TIME                  AS "CreateDate"      -- 建檔日期時間 DATE 
         , SUBSTR(EmpNo,0,6)               AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
         , JOB_START_TIME                  AS "LastUpdate"      -- 最後更新日期時間 DATE 
         , SUBSTR(EmpNo,0,6)               AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
      FROM ( SELECT MLB."YearMonth"
                  , SUM(CASE
                          WHEN MLB."EntCode" != '1' --自然人
                          THEN MLB."LoanBalance"
                        ELSE 0
                        END ) AS "NaturalPersonLoanBal"
                  , SUM(CASE
                          WHEN MLB."EntCode" = '1' -- 法人
                          THEN MLB."LoanBalance"
                        ELSE 0
                        END ) AS "LegalPersonLoanBal"
                  , SUM(CASE
                          WHEN MLB."EntCode" != '1' AND MLB."LoanBalance" >= 10000000 -- 自然人放款一千萬以上
                          THEN 1
                        ELSE 0
                        END ) AS "NaturalPersonLargeCounts"
                  , SUM(CASE
                          WHEN MLB."EntCode" != '1' AND MLB."LoanBalance" >= 10000000 -- 自然人放款一千萬以上
                          THEN MLB."LoanBalance"
                        ELSE 0
                        END ) AS "NaturalPersonLargeTotal"
                  , SUM(CASE
                          WHEN MLB."EntCode" = '1' AND MLB."LoanBalance" >= 30000000 -- 法人放款三千萬以上
                          THEN 1
                        ELSE 0
                        END ) AS "LegalPersonLargeCounts"
                  , SUM(CASE
                          WHEN MLB."EntCode" = '1' AND MLB."LoanBalance" >= 30000000 -- 法人放款三千萬以上
                          THEN MLB."LoanBalance"
                        ELSE 0
                        END ) AS "LegalPersonLargeTotal"
                  , SUM(CASE
                          WHEN MLB."EntCode" = '1' AND MLB."ClCode1" = 3 -- 股票質押貸款
                          THEN MLB."LoanBalance"
                        ELSE 0
                        END ) AS "StockLoanBal"
                  , SUM(CASE
                          WHEN MLB."EntCode" = '1' AND FCA."SyndNo" > 0 -- 有聯貸案序號
                          THEN MLB."LoanBalance"
                        ELSE 0
                        END ) AS "SyndLoanBal"
                  , SUM(CASE
                          WHEN MLB."EntCode" = '1' AND MLB."ClCode1" != 3 AND FCA."SyndNo" = 0 -- 法人其他
                          THEN MLB."LoanBalance"
                        ELSE 0
                        END ) AS "OtherLoanBal"
             FROM "MonthlyLoanBal" MLB
             LEFT JOIN "FacMain" FAC ON FAC."CustNo" = MLB."CustNo"
                                    AND FAC."FacmNo" = MLB."FacmNo"
             LEFT JOIN "FacCaseAppl" FCA ON FCA."ApplNo" = FAC."ApplNo"
             WHERE MLB."YearMonth" = YYYYMM
               AND MLB."AcctCode" != '990'
             GROUP BY MLB."YearMonth"
           ) S0
      LEFT JOIN ( SELECT "MonthEndYm"
                       , SUM(CASE
                               WHEN "AcNoCode" IN ('10601301000','10601302000')
                               THEN "TdBal"
                             ELSE 0
                             END ) AS "OvduExpense"
                  FROM "AcMain"
                  WHERE "MonthEndYm" = YYYYMM
                    AND "AcNoCode" IN ( '10601301000' -- 催收款項-法務
                                      , '10601302000' -- 催收款項-火險
                                      )
                  GROUP BY "MonthEndYm"
                ) S1 ON S1."MonthEndYm" = S0."YearMonth"
      LEFT JOIN ( SELECT YYYYMM AS "DataMonth"
                       , 0 AS "AmortizeTotal"
                  FROM DUAL
                ) S2 ON S2."DataMonth" = S0."YearMonth"
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_MonthlyLM036Portfolio_Ins' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;
