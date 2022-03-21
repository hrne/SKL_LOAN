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

    --　去年底年月(期初)
    lYYYYMM :=  (( YYYYMM / 100 ) - 1) * 100 + 12;

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
         ,  CASE
             WHEN ( S0."NaturalPersonLoanBal"
                  + S0."LegalPersonLoanBal"
                  + S2."AmortizeTotal"
                  + S1."OvduExpense"
                  ) > 0
             THEN ROUND(S0."SyndLoanBal" 
                       / ( S0."NaturalPersonLoanBal"
                         + S0."LegalPersonLoanBal"
                         + S2."AmortizeTotal"
                         + S1."OvduExpense"
                         )
                       , 4)
           ELSE 0
           END                              AS "SyndPercent" -- 聯貸案占比 DECIMAL 6 2 百分比
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
         , CASE
             WHEN ( S0."NaturalPersonLoanBal"
                  + S0."LegalPersonLoanBal"
                  + S2."AmortizeTotal"
                  + S1."OvduExpense"
                  ) > 0
             THEN ROUND(S0."OtherLoanBal" 
                       / ( S0."NaturalPersonLoanBal"
                         + S0."LegalPersonLoanBal"
                         + S2."AmortizeTotal"
                         + S1."OvduExpense"
                         )
                       , 4)
           ELSE 0
           END                             AS "OtherPercent" -- 一般法人放款占比 DECIMAL 6 2 百分比
         , S3."EntUsedPercent"             AS "EntUsedPercent" -- 企業放款動用率 DECIMAL 6 2 百分比
         , S4."InsuDividendRate"           AS "InsuDividendRate" -- 保單分紅利率 DECIMAL 6 2 百分比
         , S5."NaturalPersonRate"          AS "NaturalPersonRate" -- 自然人當月平均利率 DECIMAL 6 2 百分比
         , S6."LegalPersonRate"            AS "LegalPersonRate" -- 法人當月平均利率 DECIMAL 6 2 百分比
         , S7."SyndRate"                   AS "SyndRate" -- 聯貸案平均利率 DECIMAL 6 2 百分比
         , S8."StockRate"                  AS "StockRate" -- 股票質押平均利率 DECIMAL 6 2 百分比
         , S9."OtherRate"                  AS "OtherRate" -- 一般法人放款平均利率 DECIMAL 6 2 百分比
         , S10."AvgRate"                    AS "AvgRate" -- 放款平均利率 DECIMAL 6 2 百分比
         , S11."GrossRate"                 AS "HouseRateOfReturn" -- 房貸通路當月毛報酬率 DECIMAL 6 2 百分比
         , S12."GrossRate"                 AS "EntRateOfReturn" -- 企金通路當月毛報酬率 DECIMAL 6 2 百分比
         , S13."GrossRate"                 AS "RateOfReturn" -- 放款毛報酬率 DECIMAL 6 2 百分比
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
      --催收費用
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
      --溢折價
      LEFT JOIN ( SELECT "MonthEndYm"
                       , SUM(CASE
                               WHEN "AcNoCode" IN ('10600304000','10601304000')
                               THEN "TdBal"
                             ELSE 0
                             END ) AS "AmortizeTotal"
                  FROM "AcMain"
                  WHERE "MonthEndYm" = YYYYMM
                    AND "AcNoCode" IN ( '10600304000' -- 擔保放款-溢折價
                                      , '10601304000' -- 催收放款-溢折價
                                      )
                  GROUP BY "MonthEndYm"
                ) S2 ON S2."MonthEndYm" = S0."YearMonth"
      --企業放款動用率
      LEFT JOIN ( SELECT YYYYMM AS "YearMonth"
                       , ROUND(
                           SUM(T1."PrinBalance") / 
                          (SUM(T1."PrinBalance") - SUM(T2."UtilAmt") + SUM(T2."LineAmt"))
                       , 4 ) * 100 AS "EntUsedPercent"
                  FROM (
                    SELECT YYYYMM AS "YearMonth"
                          ,SUM("PrinBalance") AS "PrinBalance"
                    FROM "MonthlyFacBal"
                    WHERE "YearMonth" = YYYYMM
                      AND "EntCode" = 1 --企金
                  ) T1
                  LEFT JOIN (
                    SELECT YYYYMM           AS "YearMonth"
                          ,SUM(F."UtilAmt") AS "UtilAmt"
                          ,SUM(F."LineAmt") AS "LineAmt"
                    FROM "MonthlyFacBal" M
                    LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                                         AND F."FacmNo" = M."FacmNo"
                    WHERE M."YearMonth" = YYYYMM
                      AND M."EntCode" = 1 --企金
                      AND F."RecycleCode" = 1 --循環動用
                   ) T2 ON T2."YearMonth" = T1."YearMonth"
                ) S3 ON S3."YearMonth" = S0."YearMonth"
      --保單利率分紅(待確認)
      LEFT JOIN ( SELECT YYYYMM           AS "YearMonth"
                       , ROUND(
                           "BaseRate"
                       , 2 ) AS "InsuDividendRate"
                  FROM "CdBaseRate"
                  WHERE "EffectDate" = (
                    SELECT MAX("EffectDate")
                    FROM "CdBaseRate"
                    WHERE "BaseRateCode" = '02')
                ) S4 ON S4."YearMonth" = S0."YearMonth"
      --自然人 當月平均利率
      LEFT JOIN ( SELECT YYYYMM           AS "YearMonth"
                       , ROUND(
                           SUM("StoreRate") /
                           COUNT("StoreRate")
                       , 2 ) AS "NaturalPersonRate"
                  FROM "MonthlyLoanBal"
                  WHERE "YearMonth" = YYYYMM
                    AND "EntCode" != '1'
                    AND "ClCode1" IN (1,2)
                    AND "LoanBalance" > 0
                ) S5 ON S5."YearMonth" = S0."YearMonth"
      --法人 當月平均利率
      LEFT JOIN ( SELECT YYYYMM           AS "YearMonth"
                       , ROUND(
                           SUM("StoreRate") /
                           COUNT("StoreRate")
                       , 2 ) AS "LegalPersonRate"
                  FROM "MonthlyLoanBal"
                  WHERE "YearMonth" = YYYYMM
                    AND "EntCode" = '1'
                    AND "ClCode1" IN (1,2)
                    AND "LoanBalance" > 0
                ) S6 ON S6."YearMonth" = S0."YearMonth"
      --聯貸案 當月平均利率
      LEFT JOIN ( SELECT YYYYMM           AS "YearMonth"
                       , ROUND(
                           SUM("StoreRate") /
                           COUNT("StoreRate")
                       , 2 ) AS "SyndRate"
                  FROM "MonthlyLoanBal" M
                  LEFT JOIN "FacMain" FAC ON FAC."CustNo" = M."CustNo"
                                         AND FAC."FacmNo" = M."FacmNo"
                  LEFT JOIN "FacCaseAppl" FCA ON FCA."ApplNo" = FAC."ApplNo"
                  WHERE "YearMonth" = YYYYMM
                    AND "EntCode" = '1'
                    AND "ClCode1" IN (1,2)
                    AND "LoanBalance" > 0
                    AND FCA."SyndNo" > 0
                ) S7 ON S7."YearMonth" = S0."YearMonth"
      --股票質押 當月平均利率
      LEFT JOIN ( SELECT YYYYMM           AS "YearMonth"
                       , ROUND(
                           SUM("StoreRate") /
                           COUNT("StoreRate")
                       , 2 ) AS "StockRate"
                  FROM "MonthlyLoanBal"
                  WHERE "YearMonth" = YYYYMM
                    AND "EntCode" = '1'
                    AND "ClCode1" IN (3)
                    AND "LoanBalance" > 0
                ) S8 ON S8."YearMonth" = S0."YearMonth"
      --一般法人 當月平均利率
      LEFT JOIN ( SELECT YYYYMM           AS "YearMonth"
                       , ROUND(
                           SUM("StoreRate") /
                           COUNT("StoreRate")
                       , 2 ) AS "OtherRate"
                  FROM "MonthlyLoanBal" M
                  LEFT JOIN "FacMain" FAC ON FAC."CustNo" = M."CustNo"
                                         AND FAC."FacmNo" = M."FacmNo"
                  LEFT JOIN "FacCaseAppl" FCA ON FCA."ApplNo" = FAC."ApplNo"
                  WHERE "YearMonth" = YYYYMM
                    AND "EntCode" = '1'
                    AND "ClCode1" != 3
                    AND "LoanBalance" > 0
                    AND FCA."SyndNo" = 0
                ) S9 ON S9."YearMonth" = S0."YearMonth"
      --當月 放款平均利率
      LEFT JOIN ( SELECT YYYYMM           AS "YearMonth"
                       , ROUND(
                           SUM("StoreRate") /
                           COUNT("StoreRate")
                       , 2 ) AS "AvgRate"
                  FROM "MonthlyLoanBal"
                  WHERE "YearMonth" = YYYYMM
                    AND "LoanBalance" > 0
                ) S10 ON S10."YearMonth" = S0."YearMonth"
      /*
      毛報酬率 = 息收合計 / 期初期末平均餘額 / 當月份 * 12
      息收合計= 利息收入 + 帳管費 + 呆帳回沖
          利息收入：(放款餘額及財收統計表-利息收入 + 專案補貼息 + 應收息調整數)
              -放款餘額及財收統計表-利息收入：(LoanBorTx 放款交易內容檔)
              -專案補貼息：(AcDetail 會計帳務明細檔)
                  利息收入－九二一貸款戶(F15)
                  利息收入－３２００億專案息(F16)
                  利息收入－88風災貸款戶(F21)
              -應收息調整數：(AcDetail 會計帳務明細檔)
                  應收利息-放款部(ICT) [當年月 減 去年底年月]
          帳管費：(AcDetail 會計帳務明細檔)
              收續費收入－放款帳管費－一般(F10)
          呆帳沖回：(AcDetail 會計帳務明細檔)
              收回呆帳及過期障－放款(F08)
      期初期末平均餘額=(有效戶放款餘額 + 催收款項(990) + 催收費用 + 折溢價) (期末 + 期初) / 2
          有效戶放款餘額：(MonthlyFacBal 額度月報工作檔)
          催收款項：(MonthlyFacBal 額度月報工作檔 AcctCode=990)
          催收費用：(AcDetail 會計帳務明細檔)
              催收款項－法務費用(F24)
              催收款項－火險費用(F25)
          折溢價：(AcDetail 會計帳務明細檔)
              催收款項－折溢價(AIO)，沒有擔保放款－折溢價(AIL)
      */
       --房貸通路 當月毛報酬率
      LEFT JOIN ( SELECT YYYYMM AS "YearMonth"
                        ,T1."EntCode"
                        ,ROUND(
                          (NVL(T1."IntRcv",0) 
                          + NVL(T2."TxAmt",0)  
                          - NVL(T3."lIntRcv",0)) 
                        /(NVL(T4."LoanBal",0)
                         +NVL(T5."LoanBal",0))
                        / 2 / MOD(YYYYMM , 100) * 12 , 5 ) * 100 AS "GrossRate"
                  FROM ( --利息收入
                        SELECT CASE 
                                 WHEN C."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,SUM( "Interest"
                                  + "DelayInt"
                                  + "BreachAmt"
                                  + "CloseBreachAmt" ) AS "IntRcv"
                        FROM "LoanBorTx" LBT
                        LEFT JOIN "CustMain" C ON C."CustNo" = LBT."CustNo"
                        WHERE TRUNC(LBT."AcDate" / 100) = YYYYMM
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END ) T1
                   LEFT JOIN ( 
                        --應收利息－放款部(ICR)
                        SELECT CASE 
                                 WHEN C."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,NVL(
                                SUM(
                                  DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                  ) , 0)  AS "TxAmt"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = YYYYMM
                          AND A."AcctCode" IN (
                            'F15' --利息收入－九二一貸款戶
                           ,'F16' --利息收入－３２００億專案息
                           ,'F21' --利息收入－88風災貸款戶
                           ,'ICR' --應收利息-放款部(ICT)
                           ,'F10' --收續費收入－放款帳管費－一般
                           ,'F08' --收回呆帳及過期障－放款
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                          ) T2 ON T2."EntCode" = T1."EntCode"
                  LEFT JOIN ( 
                        --應收利息－放款部(ICR)
                        SELECT CASE 
                                 WHEN C."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,NVL(
                                SUM(
                                  DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                  ) , 0)  AS "lIntRcv"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = lYYYYMM
                          AND A."AcctCode" IN (
                            'ICR' --應收利息-放款部
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                          ) T3 ON T3."EntCode" = T1."EntCode"
                  LEFT JOIN ( 
                        --當月 放款餘額
                        SELECT R."EntCode"
                              ,SUM(R."TxAmt") AS "LoanBal"
                        FROM ( SELECT CASE 
                                        WHEN C."EntCode" = '1' 
                                        THEN '1'
                                      ELSE '0' END AS "EntCode"
                                     ,NVL(
                                      SUM(
                                        DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                        ) , 0)  AS "TxAmt"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = lYYYYMM
                          AND A."AcctCode" IN (
                            'AIO' --催收款項－折溢價
                           ,'F24' --催收款項－法務費用
                           ,'F25' --催收款項－火險費用
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                        UNION 
                        SELECT CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,SUM("LoanBalance") AS "TxAmt"
                        FROM "MonthlyLoanBal" M
                        WHERE M."YearMonth" = YYYYMM 
                          AND M."LoanBalance" > 0
                        GROUP BY CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                                ELSE '0' END ) R
                        GROUP BY R."EntCode" 
                          ) T4 ON T4."EntCode" = T1."EntCode"  
                  LEFT JOIN ( 
                        --期初 放款餘額
                        SELECT R."EntCode"
                              ,SUM(R."TxAmt") AS "LoanBal"
                        FROM ( SELECT CASE 
                                        WHEN C."EntCode" = '1' 
                                        THEN '1'
                                      ELSE '0' END AS "EntCode"
                                     ,NVL(
                                      SUM(
                                        DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                        ) , 0)  AS "TxAmt"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = lYYYYMM
                          AND A."AcctCode" IN (
                            'AIO' --催收款項－折溢價
                           ,'F24' --催收款項－法務費用
                           ,'F25' --催收款項－火險費用
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                        UNION 
                        SELECT CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,SUM("LoanBalance") AS "TxAmt"
                        FROM "MonthlyLoanBal" M
                        WHERE M."YearMonth" = YYYYMM 
                          AND M."LoanBalance" > 0
                        GROUP BY CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                                ELSE '0' END ) R
                        GROUP BY R."EntCode" 
                          ) T5 ON T5."EntCode" = T1."EntCode"  
                ) S11 ON S11."YearMonth" = S0."YearMonth" 
                     AND S11."EntCode" = '0'
      --企金通路 當月毛報酬率
      LEFT JOIN ( SELECT YYYYMM AS "YearMonth"
                        ,T1."EntCode"
                        ,ROUND(
                          (NVL(T1."IntRcv",0) 
                          + NVL(T2."TxAmt",0)  
                          - NVL(T3."lIntRcv",0)) 
                        /(NVL(T4."LoanBal",0)
                         +NVL(T5."LoanBal",0))
                        / 2 / MOD(YYYYMM , 100) * 12 , 5 ) * 100 AS "GrossRate"
                  FROM ( --利息收入
                        SELECT CASE 
                                 WHEN C."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,SUM( "Interest"
                                  + "DelayInt"
                                  + "BreachAmt"
                                  + "CloseBreachAmt" ) AS "IntRcv"
                        FROM "LoanBorTx" LBT
                        LEFT JOIN "CustMain" C ON C."CustNo" = LBT."CustNo"
                        WHERE TRUNC(LBT."AcDate" / 100) = YYYYMM
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END ) T1
                   LEFT JOIN ( 
                        --應收利息－放款部(ICR)
                        SELECT CASE 
                                 WHEN C."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,NVL(
                                SUM(
                                  DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                  ) , 0)  AS "TxAmt"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = YYYYMM
                          AND A."AcctCode" IN (
                            'F15' --利息收入－九二一貸款戶
                           ,'F16' --利息收入－３２００億專案息
                           ,'F21' --利息收入－88風災貸款戶
                           ,'ICR' --應收利息-放款部(ICT)
                           ,'F10' --收續費收入－放款帳管費－一般
                           ,'F08' --收回呆帳及過期障－放款
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                          ) T2 ON T2."EntCode" = T1."EntCode"
                  LEFT JOIN ( 
                        --應收利息－放款部(ICR)
                        SELECT CASE 
                                 WHEN C."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,NVL(
                                SUM(
                                  DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                  ) , 0)  AS "lIntRcv"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = lYYYYMM
                          AND A."AcctCode" IN (
                            'ICR' --應收利息-放款部
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                          ) T3 ON T3."EntCode" = T1."EntCode"
                  LEFT JOIN ( 
                        --當月 放款餘額
                        SELECT R."EntCode"
                              ,SUM(R."TxAmt") AS "LoanBal"
                        FROM ( SELECT CASE 
                                        WHEN C."EntCode" = '1' 
                                        THEN '1'
                                      ELSE '0' END AS "EntCode"
                                     ,NVL(
                                      SUM(
                                        DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                        ) , 0)  AS "TxAmt"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = lYYYYMM
                          AND A."AcctCode" IN (
                            'AIO' --催收款項－折溢價
                           ,'F24' --催收款項－法務費用
                           ,'F25' --催收款項－火險費用
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                        UNION 
                        SELECT CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,SUM("LoanBalance") AS "TxAmt"
                        FROM "MonthlyLoanBal" M
                        WHERE M."YearMonth" = YYYYMM 
                          AND M."LoanBalance" > 0
                        GROUP BY CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                                ELSE '0' END ) R
                        GROUP BY R."EntCode" 
                          ) T4 ON T4."EntCode" = T1."EntCode"  
                  LEFT JOIN ( 
                        --期初 放款餘額
                        SELECT R."EntCode"
                              ,SUM(R."TxAmt") AS "LoanBal"
                        FROM ( SELECT CASE 
                                        WHEN C."EntCode" = '1' 
                                        THEN '1'
                                      ELSE '0' END AS "EntCode"
                                     ,NVL(
                                      SUM(
                                        DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                        ) , 0)  AS "TxAmt"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = lYYYYMM
                          AND A."AcctCode" IN (
                            'AIO' --催收款項－折溢價
                           ,'F24' --催收款項－法務費用
                           ,'F25' --催收款項－火險費用
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                        UNION 
                        SELECT CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,SUM("LoanBalance") AS "TxAmt"
                        FROM "MonthlyLoanBal" M
                        WHERE M."YearMonth" = YYYYMM 
                          AND M."LoanBalance" > 0
                        GROUP BY CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                                ELSE '0' END ) R
                        GROUP BY R."EntCode" 
                          ) T5 ON T5."EntCode" = T1."EntCode"  
                ) S12 ON S12."YearMonth" = S0."YearMonth" 
                     AND S12."EntCode" = '1'
       --當月毛報酬率
      LEFT JOIN ( SELECT YYYYMM AS "YearMonth"
                        ,ROUND(
                          (SUM(NVL(T1."IntRcv",0) 
                          + NVL(T2."TxAmt",0)  
                          - NVL(T3."lIntRcv",0)))  
                        /SUM((NVL(T4."LoanBal",0)
                         +NVL(T5."LoanBal",0)))
                        / 2 / MOD(YYYYMM , 100) * 12 , 5 ) * 100 AS "GrossRate"
                  FROM ( --利息收入
                        SELECT CASE 
                                 WHEN C."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,SUM( "Interest"
                                  + "DelayInt"
                                  + "BreachAmt"
                                  + "CloseBreachAmt" ) AS "IntRcv"
                        FROM "LoanBorTx" LBT
                        LEFT JOIN "CustMain" C ON C."CustNo" = LBT."CustNo"
                        WHERE TRUNC(LBT."AcDate" / 100) = YYYYMM
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END ) T1
                   LEFT JOIN ( 
                        --應收利息－放款部(ICR)
                        SELECT CASE 
                                 WHEN C."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,NVL(
                                SUM(
                                  DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                  ) , 0)  AS "TxAmt"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = YYYYMM
                          AND A."AcctCode" IN (
                            'F15' --利息收入－九二一貸款戶
                           ,'F16' --利息收入－３２００億專案息
                           ,'F21' --利息收入－88風災貸款戶
                           ,'ICR' --應收利息-放款部(ICT)
                           ,'F10' --收續費收入－放款帳管費－一般
                           ,'F08' --收回呆帳及過期障－放款
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                          ) T2 ON T2."EntCode" = T1."EntCode"
                  LEFT JOIN ( 
                        --應收利息－放款部(ICR)
                        SELECT CASE 
                                 WHEN C."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,NVL(
                                SUM(
                                  DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                  ) , 0)  AS "lIntRcv"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = lYYYYMM
                          AND A."AcctCode" IN (
                            'ICR' --應收利息-放款部
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                          ) T3 ON T3."EntCode" = T1."EntCode"
                  LEFT JOIN ( 
                        --當月 放款餘額
                        SELECT R."EntCode"
                              ,SUM(R."TxAmt") AS "LoanBal"
                        FROM ( SELECT CASE 
                                        WHEN C."EntCode" = '1' 
                                        THEN '1'
                                      ELSE '0' END AS "EntCode"
                                     ,NVL(
                                      SUM(
                                        DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                        ) , 0)  AS "TxAmt"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = lYYYYMM
                          AND A."AcctCode" IN (
                            'AIO' --催收款項－折溢價
                           ,'F24' --催收款項－法務費用
                           ,'F25' --催收款項－火險費用
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                        UNION 
                        SELECT CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,SUM("LoanBalance") AS "TxAmt"
                        FROM "MonthlyLoanBal" M
                        WHERE M."YearMonth" = YYYYMM 
                          AND M."LoanBalance" > 0
                        GROUP BY CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                                ELSE '0' END ) R
                        GROUP BY R."EntCode" 
                          ) T4 ON T4."EntCode" = T1."EntCode"  
                  LEFT JOIN ( 
                        --期初 放款餘額
                        SELECT R."EntCode"
                              ,SUM(R."TxAmt") AS "LoanBal"
                        FROM ( SELECT CASE 
                                        WHEN C."EntCode" = '1' 
                                        THEN '1'
                                      ELSE '0' END AS "EntCode"
                                     ,NVL(
                                      SUM(
                                        DECODE(A."DbCr" , 'C' , A."TxAmt" , -A."TxAmt" )
                                        ) , 0)  AS "TxAmt"
                        FROM "AcDetail" A
                        LEFT JOIN "CustMain" C ON C."CustNo" = A."CustNo"
                                              AND C."CustNo" NOT IN ( 0 )
                        WHERE TRUNC(A."AcDate" / 100) = lYYYYMM
                          AND A."AcctCode" IN (
                            'AIO' --催收款項－折溢價
                           ,'F24' --催收款項－法務費用
                           ,'F25' --催收款項－火險費用
                          )
                        GROUP BY CASE 
                                  WHEN C."EntCode" = '1' 
                                  THEN '1'
                                 ELSE '0' END 
                        UNION 
                        SELECT CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                               ELSE '0' END AS "EntCode"
                              ,SUM("LoanBalance") AS "TxAmt"
                        FROM "MonthlyLoanBal" M
                        WHERE M."YearMonth" = YYYYMM 
                          AND M."LoanBalance" > 0
                        GROUP BY CASE 
                                 WHEN M."EntCode" = '1' 
                                 THEN '1'
                                ELSE '0' END ) R
                        GROUP BY R."EntCode" 
                          ) T5 ON T5."EntCode" = T1."EntCode"  
                ) S13 ON S13."YearMonth" = S0."YearMonth" 
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
