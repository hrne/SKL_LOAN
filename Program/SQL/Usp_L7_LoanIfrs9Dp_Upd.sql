CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L7_LoanIfrs9Dp_Upd"
(
-- 程式功能：維護 LoanIfrs9Dp 每月IFRS9欄位清單D檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_LoanIfrs9Dp_Upd"(20211230,'999999',1);
--

    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    NewAcFg        IN  INT         -- 0=使用舊會計科目(8碼) 1=使用新會計科目(11碼)
)
AUTHID CURRENT_USER
AS
BEGIN
	"Usp_L7_LoanIfrs9Dp_Upd_Prear1"();
	"Usp_L7_LoanIfrs9Dp_Upd_Prear2"();
  DECLARE
    INS_CNT        INT;         -- 新增筆數
    UPD_CNT        INT;         -- 更新筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間
    YYYYMM         INT;         -- 本月年月
    LYYYYMM        INT;         -- 上月年月
    MM             INT;         -- 本月月份
    YYYY           INT;         -- 本月年度
    LastYMD        DATE;        -- 本月最後一天
    Last2YearsYM   INT;         -- 2年前年月
  BEGIN
    INS_CNT := 0;
    UPD_CNT := 0;

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


-- 本月最後一天
    SELECT LAST_DAY( to_date(to_char(TBSDYF), 'YYYYMMDD') )
           INTO LastYMD
           FROM DUAL ;

-- 2年前年月
    Last2YearsYM :=  YYYYMM - 200 ;


-- 撈符合條件資料，寫入 "Work_DP_Data" 暫存檔
    INSERT INTO "Work_DP_Data"
    SELECT
           W."CustNo"                           AS "CustNo"             -- 戶號
         , W."FacmNo"                           AS "FacmNo"             -- 額度編號
         , W."BormNo"                           AS "BormNo"             -- 撥款序號
         , W."DataFg"                           AS "DataFg"             -- 資料類別
         , W."FinishedDate"                     AS "FinishedDate"       -- 拍定完成日
    FROM ( -- 取Ias34Dp 已完成撈取該月法拍戶資料from[法拍完成資料檔(ForeclosureFinished)]
           SELECT IA."CustNo"                   AS "CustNo"
                , IA."FacmNo"                   AS "FacmNo"
                , IA."BormNo"                   AS "BormNo"
                , 1                             AS "DataFg"
                , NVL(FF."FinishedDate",0)      AS "FinishedDate"
           FROM   "Ias34Dp"  IA
           LEFT JOIN "ForeclosureFinished" FF ON IA."CustNo" = FF."CustNo"
                                             AND IA."FacmNo" = FF."FacmNo"
                                             AND IA."DataYM" = YYYYMM
           WHERE  IA."DataYM" = YYYYMM 
           --  AND  TRUNC(NVL(FF."FinishedDate",0) / 100) <= YYYYMM        -- 法拍完成日 <= 會計日
           --  AND  TRUNC(NVL(FF."FinishedDate",0) / 100) >  Last2YearsYM  -- 法拍完成日 >  會計2年前月底日
           --  AND  M."Status" IN (2 ,5 ,6 ,7 ,8 ,9 )  --2:催收戶 5:催收結案戶 6:呆帳戶 7: 部分轉呆戶 8:債權轉讓戶 9:呆帳結案戶
           -- 逾期 90 天
           UNION
           SELECT M."CustNo"                    AS "CustNo"
                , M."FacmNo"                    AS "FacmNo"
                , M."BormNo"                    AS "BormNo"
                , 2                             AS "DataFg"
                , 0                             AS "FinishedDate"
           FROM   "JcicMonthlyLoanData" M
             LEFT JOIN "ForeclosureFinished" FF  ON FF."CustNo"    = M."CustNo"
                                                AND FF."FacmNo"    = M."FacmNo"
           WHERE  M."DataYM"    = YYYYMM
             AND M."Status" IN (0)
             AND CASE
                   WHEN M."MaturityDate" < M."NextPayIntDate"  -- 已到期
                        AND ( LastYMD - TO_DATE(M."MaturityDate",'yyyy-mm-dd') ) >= 90
                     THEN 1 
                   WHEN ( LastYMD - TO_DATE(M."NextPayIntDate",'yyyy-mm-dd') ) >= 90
                     THEN 1
                   ELSE 0
                 END = 1  
         ) W
      ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE LoanIfrs9Dp');

    DELETE FROM "LoanIfrs9Dp"
    WHERE "DataYM" = YYYYMM
      ;


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Dp');
    INS_CNT := 0;

    INSERT INTO "LoanIfrs9Dp"
    WITH Total AS (
      -- 各戶號放款餘額加總
      SELECT JM."DataYM"                 AS "DataYM"
           , JM."CustNo"                 AS "CustNo"
           , SUM(JM."LoanBal")           AS "LoanBalTotal"
      FROM "JcicMonthlyLoanData"  JM
      LEFT JOIN "Work_DP_Data" WK  ON WK."CustNo" = JM."CustNo"
                                  AND WK."FacmNo" = JM."FacmNo"
                                  AND WK."BormNo" = JM."BormNo"
      WHERE JM."DataYM" =  YYYYMM
        AND WK."CustNo" > 0
      GROUP BY JM."DataYM"
             , JM."CustNo"
    )
    , FacTotal AS (
      -- 各額度放款餘額加總
      SELECT JM."DataYM"                 AS "DataYM"
           , JM."CustNo"                 AS "CustNo"
           , JM."FacmNo"                 AS "FacmNo"
           , SUM(JM."LoanBal")           AS "LoanBalTotal"
      FROM "JcicMonthlyLoanData" JM
      LEFT JOIN "Work_DP_Data" WK  ON WK."CustNo" = JM."CustNo"
                                  AND WK."FacmNo" = JM."FacmNo"
                                  AND WK."BormNo" = JM."BormNo"
      WHERE JM."DataYM" =  YYYYMM
        AND WK."CustNo" > 0
      GROUP BY JM."DataYM"
             , JM."CustNo"
             , JM."FacmNo"
    )
    , Law AS (
      -- 把本月各戶號法拍費用餘額撈出來
      SELECT "CustNo"
           , SUM("Fee") AS "Fee"
      FROM "ForeclosureFee"
      WHERE TRUNC("OpenAcDate" / 100) <= YYYYMM
        AND "Fee" > 0
        AND CASE
              WHEN "CloseDate" = 0 -- 未銷直接計入
              THEN 1
              WHEN TRUNC("CloseDate" / 100) > YYYYMM -- 銷帳日期大於本月,計入
              THEN 1
            ELSE 0
            END = 1
      GROUP BY "CustNo"
    )
    , Insu AS (
      -- 把本月各額度火險費餘額撈出來
      SELECT "CustNo"
           , "FacmNo"
           , SUM("TotInsuPrem") AS "Fee"
      FROM "InsuRenew"
      WHERE "TotInsuPrem" > 0
        AND "RenewCode" = 2 
        AND "InsuYearMonth" <= YYYYMM
        AND
            CASE
              WHEN "AcDate" = 0 -- 未銷直接計入
              THEN 1
              WHEN TRUNC("AcDate" / 100) > YYYYMM -- 銷帳日期大於本月,計入
              THEN 1
            ELSE 0
            END = 1
      GROUP BY "CustNo"
             , "FacmNo"
    )
    , FeeData AS (
      SELECT M."DataYM"
           , M."CustNo"
           , M."FacmNo"
           , M."BormNo"
           , M."LoanBal"
           , T."LoanBalTotal"
           , FT."LoanBalTotal" AS "FacLoanBalTotal"
           , NVL(L."Fee",0) AS "LawFee"
           , NVL(I."Fee",0) AS "InsuFee"
      FROM "JcicMonthlyLoanData" M
      LEFT JOIN "Work_DP_Data" WK  ON WK."CustNo" = M."CustNo"
                                  AND WK."FacmNo" = M."FacmNo"
                                  AND WK."BormNo" = M."BormNo"
      LEFT JOIN Total T ON T."DataYM" = M."DataYM"
                       AND T."CustNo" = M."CustNo"
      LEFT JOIN FacTotal FT ON FT."DataYM" = M."DataYM"
                           AND FT."CustNo" = M."CustNo"
                           AND FT."FacmNo" = M."FacmNo"
      LEFT JOIN Law L ON L."CustNo" = M."CustNo"
      LEFT JOIN Insu I ON I."CustNo" = M."CustNo"
                      AND I."FacmNo" = M."FacmNo"
      WHERE M."DataYM" = YYYYMM
    )
    , AvgFeeDataBase AS (
      -- 以撥款層的放款餘額與佔總戶號放款餘額比例
      -- 分配法拍及火險費用
      SELECT "DataYM"
           , "CustNo"
           , "FacmNo"
           , "BormNo"
           , "LoanBal"
           , "LoanBalTotal"
           , "FacLoanBalTotal"
           , "LawFee"
           , "InsuFee"
           , ROUND("LawFee" * "LoanBal" / "LoanBalTotal",0) AS "AvgLawFee"
           , ROUND("InsuFee" * "LoanBal" / "FacLoanBalTotal",0) AS "AvgInsuFee"
           , ROW_NUMBER()
             OVER (
               PARTITION BY "DataYM"
                          , "CustNo"
               ORDER BY "FacmNo"
                      , "BormNo"
             )                                  AS "Seq"
           , ROW_NUMBER()
             OVER (
               PARTITION BY "DataYM"
                          , "CustNo"
                          , "FacmNo"
               ORDER BY "BormNo"
             )                                  AS "FacSeq"
      FROM FeeData
      WHERE "LawFee" + "InsuFee" > 0
        AND "LoanBal" > 0
        AND "LoanBalTotal" > 0
        AND "FacLoanBalTotal" > 0
    )
    , GetMaxSeq AS (
      SELECT "DataYM"
           , "CustNo"
           , MAX("Seq") AS "MaxSeq"
      FROM AvgFeeDataBase
      GROUP BY "DataYM"
             , "CustNo"
    )
    , GetFacMaxSeq AS (
      SELECT "DataYM"
           , "CustNo"
           , "FacmNo"
           , MAX("FacSeq") AS "MaxSeq"
      FROM AvgFeeDataBase
      GROUP BY "DataYM"
             , "CustNo"
             , "FacmNo"
    )
    , AvgFeeDataFinal AS (
      -- 最後一筆用總費用減去其他筆費用
      SELECT B."DataYM"
           , B."CustNo"
           , B."FacmNo"
           , B."BormNo"
           , CASE
               WHEN B."Seq" = G1."MaxSeq"
               THEN B."LawFee" - NVL(O1."OtherLawFee",0)
             ELSE B."AvgLawFee"
             END                                AS "AvgLawFee"
           , CASE
               WHEN B."FacSeq" = G2."MaxSeq"
               THEN B."InsuFee" - NVL(O2."OtherInsuFee",0)
             ELSE B."AvgInsuFee"
             END                                AS "AvgInsuFee"
      FROM AvgFeeDataBase B
      LEFT JOIN (
        SELECT B."DataYM"
             , B."CustNo"
             , SUM(B."AvgLawFee") AS "OtherLawFee"
        FROM AvgFeeDataBase B
        LEFT JOIN GetMaxSeq G ON G."DataYM" = B."DataYM"
                             AND G."CustNo" = B."CustNo"
        WHERE B."Seq" < G."MaxSeq"
        GROUP BY B."DataYM"
               , B."CustNo"
      ) O1 ON O1."DataYM" = B."DataYM"
          AND O1."CustNo" = B."CustNo"
      LEFT JOIN (
        SELECT B."DataYM"
             , B."CustNo"
             , B."FacmNo"
             , SUM(B."AvgInsuFee") AS "OtherInsuFee"
        FROM AvgFeeDataBase B
        LEFT JOIN GetFacMaxSeq G ON G."DataYM" = B."DataYM"
                                AND G."CustNo" = B."CustNo"
                                AND G."FacmNo" = B."FacmNo"
        WHERE B."FacSeq" < G."MaxSeq"
        GROUP BY B."DataYM"
               , B."CustNo"
               , B."FacmNo"
      ) O2 ON O2."DataYM" = B."DataYM"
          AND O2."CustNo" = B."CustNo"
          AND O2."FacmNo" = B."FacmNo"
      LEFT JOIN GetMaxSeq G1 ON G1."DataYM" = B."DataYM"
                            AND G1."CustNo" = B."CustNo"
      LEFT JOIN GetFacMaxSeq G2 ON G2."DataYM" = B."DataYM"
                               AND G2."CustNo" = B."CustNo"
                               AND G2."FacmNo" = B."FacmNo"
    )   
    SELECT
           YYYYMM                                    AS "DataYM"            -- 資料年月
         , WK."CustNo"                               AS "CustNo"            -- 戶號
         , NVL("CustMain"."CustId",' ')              AS "CustId"            -- 借款人ID / 統編
         , WK."FacmNo"                               AS "FacmNo"            -- 額度編號
         , WK."BormNo"                               AS "BormNo"            -- 撥款序號
         , WK."DataFg"                               AS "DataFg"            -- 資料類別
                                                                               -- 1=法拍完成資料檔
                                                                               -- 2=逾期天數>=90天且未在法拍完成資料檔中
         , CASE WHEN NewAcFg = 0 THEN RPAD(NVL("CdAcCode"."AcNoCodeOld",' '),8,' ')   -- 舊
                ELSE                  RPAD(NVL("CdAcCode"."AcNoCode",' '),11,' ')     -- 新
           END                                       AS "AcCode"            -- 會計科目
         , CASE WHEN M."Status" IN (0)      THEN 1   -- 正常
                WHEN M."Status" IN (6,7,9)  THEN 3   -- 呆帳 (部份轉呆)
                ELSE  2                              -- 催收
           END                                       AS "Status"             -- 案件狀態
         , NVL(F."FirstDrawdownDate",0)              AS "FirstDrawdownDate"  -- 初貸日期
         , NVL(M."DrawdownDate",0)                   AS "DrawdownDate"       -- 貸放日期
         , NVL(M."MaturityDate",0)                   AS "MaturityDate"       -- 到期日
         , NVL(F."LineAmt",0)                        AS "LineAmt"            -- 核准金額  --每額度編號項下之放款帳號皆同
         , NVL(M."DrawdownAmt",0)                    AS "DrawdownAmt"        -- 撥款金額
         , NVL(M."LoanBal",0)                        AS "LoanBal"            -- 本金餘額(撥款)
         , NVL(M."IntAmt",0)                         AS "IntAmt"             -- 應收利息
         , CASE WHEN WK."DataFg" = 1 THEN IA."Fee"
                ELSE NVL(AF."AvgLawFee",0) + NVL(AF."AvgInsuFee",0)
           END                                       AS "Fee"                -- 法拍及火險費用
         , CASE WHEN WK."DataFg" = 1 THEN IA."OvduDays"
                WHEN M."NextPayIntDate" IS NULL  THEN 0
                WHEN M."MaturityDate" < M."NextPayIntDate"
                     THEN ( LastYMD - TO_DATE(M."MaturityDate",'yyyy-mm-dd') )
                ELSE ( LastYMD - TO_DATE(M."NextPayIntDate",'yyyy-mm-dd'))
           END                                       AS "OvduDays"           -- 逾期繳款天數
         , CASE WHEN WK."DataFg" = 1 THEN IA."OvduDate"
                ELSE NVL(M."OvduDate", 0) 
           END                                       AS "OvduDate"           -- 轉催收款日期
         , CASE WHEN WK."DataFg" = 1 THEN IA."BadDebtDate"
                ELSE NVL(M."BadDebtDate", 0)
           END                                       AS "BadDebtDate"        -- 轉銷呆帳日期  -- 最早之轉銷呆帳日期
         , CASE WHEN WK."DataFg" = 1 THEN IA."BadDebtAmt"
                ELSE NVL(OD."BadDebtAmt", 0)
           END                                       AS "BadDebtAmt"         -- 轉銷呆帳金額
         , CASE WHEN WK."DataFg" = 1 THEN IA."DerDate"
                WHEN LOS."CustNo" IS NOT NULL THEN  LOS."MarkDate"   -- 特殊減損件
                WHEN NVL("FacProd"."AgreementFg",' ') = 'Y' THEN     -- 協議件: 60~62, MIN(撥款日期, 繳息迄日+120)
                  CASE WHEN NVL(M."PrevPayIntDate", 0) = 0  THEN  M."DrawdownDate"
                       WHEN to_date(M."DrawdownDate",'yyyy-mm-dd')
                               <= ( to_date(M."PrevPayIntDate",'yyyy-mm-dd') + 120 )
                            THEN  M."DrawdownDate"
                       ELSE to_number(to_char((to_date(M."PrevPayIntDate",'yyyy-mm-dd') + 120 ),'yyyymmdd'))
                  END
                WHEN NVL(M."PrevPayIntDate", 0) = 0                  -- 非協議件: 無繳息迄日, 使用 撥款日期+120
                     THEN  to_number(to_char((to_date(M."DrawdownDate",'yyyy-mm-dd') + 120 ),'yyyymmdd'))
                ELSE to_number(to_char((to_date(M."PrevPayIntDate",'yyyy-mm-dd') + 120 ),'yyyymmdd'))  -- 繳息迄日+120
           END                                       AS "DerDate"            -- 個案減損客觀證據發生日期
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerRate"
             ELSE 0
           END    AS "DerRate"            -- 上述發生日期前之最近一次利率
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerLoanBal"
             ELSE 0
           END    AS "DerLoanBal"         -- 上述發生日期時之本金餘額
         , CASE 
             WHEN WK."DataFg" = 1 
             THEN IA."DerIntAmt"
             ELSE 0
           END    AS "DerIntAmt"          -- 上述發生日期時之應收利息
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerFee"
             ELSE 0
           END    AS "DerFee"             -- 上述發生日期時之法拍及火險費用
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY1Amt"
             ELSE 0
           END    AS "DerY1Amt"           -- 個案減損客觀證據發生後第一年本金回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY2Amt"
             ELSE 0
           END    AS "DerY2Amt"           -- 個案減損客觀證據發生後第二年本金回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY3Amt"
             ELSE 0
           END    AS "DerY3Amt"           -- 個案減損客觀證據發生後第三年本金回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY4Amt"
             ELSE 0
           END    AS "DerY4Amt"           -- 個案減損客觀證據發生後第四年本金回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY5Amt"
             ELSE 0
           END    AS "DerY5Amt"           -- 個案減損客觀證據發生後第五年本金回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY1Int"
             ELSE 0
           END    AS "DerY1Int"           -- 個案減損客觀證據發生後第一年應收利息回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY2Int"
             ELSE 0
           END    AS "DerY2Int"           -- 個案減損客觀證據發生後第二年應收利息回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY3Int"
             ELSE 0
           END    AS "DerY3Int"           -- 個案減損客觀證據發生後第三年應收利息回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY4Int"
             ELSE 0
           END    AS "DerY4Int"           -- 個案減損客觀證據發生後第四年應收利息回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY5Int"
             ELSE 0
           END    AS "DerY5Int"           -- 個案減損客觀證據發生後第五年應收利息回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY1Fee"
             ELSE 0
           END    AS "DerY1Fee"           -- 個案減損客觀證據發生後第一年法拍及火險費用回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY2Fee"
             ELSE 0
           END    AS "DerY2Fee"           -- 個案減損客觀證據發生後第二年法拍及火險費用回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY3Fee"
             ELSE 0
           END    AS "DerY3Fee"           -- 個案減損客觀證據發生後第三年法拍及火險費用回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY4Fee"
             ELSE 0
           END    AS "DerY4Fee"           -- 個案減損客觀證據發生後第四年法拍及火險費用回收金額
         , CASE 
             WHEN WK."DataFg" = 1 THEN IA."DerY5Fee"
             ELSE 0
           END    AS "DerY5Fee"           -- 個案減損客觀證據發生後第五年法拍及火險費用回收金額
         , CASE WHEN TRIM(NVL("CustMain"."IndustryCode", ' ')) = '' THEN ' '
                ELSE SUBSTR('000000' || TRIM("CustMain"."IndustryCode"), -6)
           END                                       AS "IndustryCode"       -- 授信行業別
         , NVL(M."ClTypeCode",' ')                   AS "ClTypeJCIC"         -- 擔保品類別
         , CASE 
             WHEN NVL("ClMain"."CityCode", ' ') = '05' THEN 'A' 
             WHEN NVL("ClMain"."CityCode", ' ') = '10' THEN 'B' 
             WHEN NVL("ClMain"."CityCode", ' ') = '15' THEN 'C' 
             WHEN NVL("ClMain"."CityCode", ' ') = '35' THEN 'D' 
             WHEN NVL("ClMain"."CityCode", ' ') = '65' THEN 'E' 
             WHEN NVL("ClMain"."CityCode", ' ') = '70' THEN 'F' 
             ELSE 'G'
           END                                       AS "AreaCode"           -- 擔保品地區別 A~G
         , NVL(F."ProdNo", ' ')                      AS "ProdNo"             -- 商品利率代碼
         , CASE
             WHEN "CustMain"."EntCode" IN ('1') THEN 1  -- 企金
             ELSE 2                                     -- 個人戶, 企金自然人
           END                                       AS "CustKind"           -- 企業戶/個人戶 (1=企業戶 2=個人戶)
         , NVL("FacProd"."Ifrs9ProdCode", ' ')       AS "Ifrs9ProdCode"      -- 產品別
         , JOB_START_TIME                            AS "CreateDate"         -- 建檔日期時間
         , EmpNo                                     AS "CreateEmpNo"        -- 建檔人員
         , JOB_START_TIME                            AS "LastUpdate"         -- 最後更新日期時間
         , EmpNo                                     AS "LastUpdateEmpNo"    -- 最後更新人員
    FROM   "Work_DP_Data" WK
        LEFT JOIN "JcicMonthlyLoanData" M  ON M."DataYM"    = YYYYMM
                                          AND M."CustNo"    = WK."CustNo"
                                          AND M."FacmNo"    = WK."FacmNo"
                                          AND M."BormNo"    = WK."BormNo"
        LEFT JOIN "CustMain"  ON "CustMain"."CustNo"    =  M."CustNo"
        LEFT JOIN "CdAcCode"  ON "CdAcCode"."AcctCode"  =  CASE WHEN M."Status" IN (2,5,6,7,8,9) THEN '990'  -- 催收後
                                                                ELSE M."AcctCode"
                                                           END
        LEFT JOIN "FacMain"      F  ON F."CustNo"    = M."CustNo"
                                   AND F."FacmNo"    = M."FacmNo"
        LEFT JOIN "MonthlyFacBal"  MF  ON MF."YearMonth" = YYYYMM
                                      AND MF."CustNo"    = M."CustNo"
                                      AND MF."FacmNo"    = M."FacmNo"
        LEFT JOIN "ClMain"      ON "ClMain"."ClCode1"  = M."ClCode1"
                               AND "ClMain"."ClCode2"  = M."ClCode2"
                               AND "ClMain"."ClNo"     = M."ClNo"
        LEFT JOIN "CdCity"      ON to_number("CdCity"."CityCode")
                                 = to_number(NVL(trim("ClMain"."CityCode"), 0))
        LEFT JOIN "FacProd"     ON "FacProd"."ProdNo"  = F."ProdNo"
        LEFT JOIN "Ias39Loss" LOS   ON LOS."CustNo"  =  M."CustNo"
                                   AND LOS."FacmNo"  =  M."FacmNo"
                                   AND TRUNC(NVL(LOS."StartDate",0) / 100) <= YYYYMM
                                   AND TRUNC(NVL(LOS."EndDate",99991231) / 100) >= YYYYMM
        LEFT JOIN ( SELECT OD."CustNo"            AS  "CustNo"
                         , OD."FacmNo"            AS  "FacmNo"
                         , OD."BormNo"            AS  "BormNo"
                         , SUM(OD."BadDebtAmt")   AS  "BadDebtAmt"
                    FROM "LoanOverdue"  OD
                    WHERE OD."Status" IN (2, 3)   -- 2=部分轉呆 3=呆帳
                    GROUP BY OD."CustNo", OD."FacmNo", OD."BormNo"
                  ) OD    ON OD."CustNo"  = M."CustNo"
                         AND OD."FacmNo"  = M."FacmNo"
                         AND OD."BormNo"  = M."BormNo"
      -- 法拍件
      LEFT JOIN "Ias34Dp" IA ON IA."DataYM" = YYYYMM 
                            AND IA."CustNo" = WK."CustNo"
                            AND IA."FacmNo" = WK."FacmNo"
                            AND IA."BormNo" = WK."BormNo"
                            AND WK."DataFg" = 1
      LEFT JOIN AvgFeeDataFinal AF ON AF."DataYM" = M."DataYM"
                                  AND AF."CustNo" = M."CustNo"
                                  AND AF."FacmNo" = M."FacmNo"
                                  AND AF."BormNo" = M."BormNo"
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Dp END: INS_CNT=' || INS_CNT);


-- 寫入 Work_DP
    DBMS_OUTPUT.PUT_LINE('INSERT Work_DP');
    UPD_CNT := 0;

    INSERT INTO "Work_DP"
    WITH LR AS (
    	SELECT LR."CustNo"
    	     , LR."FacmNo"
    	     , LR."BormNo"
    	     , LR."FitRate"
    	     , LR."EffectDate"
    	     , ROW_NUMBER()
    	       OVER (
    	       		 PARTITION BY LR."CustNo"
    	                      , LR."FacmNo"
    	                      , LR."BormNo"
    	           ORDER BY LR."EffectDate" DESC
    	       ) AS "Seq"
    	FROM "LoanIfrs9Dp" M
      LEFT JOIN "LoanRateChange" LR ON LR."CustNo"  = M."CustNo"
                                   AND LR."FacmNo"  = M."FacmNo"
                                   AND LR."BormNo"  = M."BormNo"
                                   AND LR."EffectDate" <= M."DerDate"
      WHERE NVL(LR."EffectDate",0) != 0
        AND M."DataYM" = YYYYMM
    )
    , RawData AS (
      SELECT "CustNo"
           , "FacmNo"
           , "BormNo"
           -- 2022-09-28 Wei 修改 from Linda's mail from 舜雯
           -- 計算回收時，會由繳息迄日(LA$MSTP月餘額統計檔/LMSLPD繳息迄日 or LNMDLYP逾期檔/W08LPD逾期天數)
           -- +120天的年月A
           -- 開始往後計算5年(此客戶資料繳息迄日為20220320,120天後=20220718,A=202207)
           -- 1. A+1年，即202207+1=202307
           -- 2. 202307>畫面年月202208，以不超過畫面年月為限，故202307改為202208=B
           -- 3. 火險回收時，先B-1年=202108，以此年月的月底日為條件日期C，此時C=20210831
           -- 4. 讀取火險續保檔(Key值僅設戶號)，挑選會計日>=C(即20210831)，檔案中年月=202109那筆被讀取(如下)，
           --    此筆火險保費=1999，資料寫入媒體檔
           , CASE
               WHEN TRUNC("DerDate" / 100) + 100 > YYYYMM -- 若A+1年>畫面年月
                    THEN YYYYMM - 100 -- 取B-1年=C
               ELSE TRUNC("DerDate" / 100) 
             END  AS "IssueMonth" -- 減損發生年月
      FROM "LoanIfrs9Dp"
      WHERE "DerDate" > 0
        AND "DataYM" = YYYYMM
    )
    , MonthData AS (
      SELECT "CustNo"
           , "FacmNo"
           , "BormNo"
           , "IssueMonth"
           , (TRUNC("IssueMonth" / 100) + 1) * 100 + MOD("IssueMonth", 100) AS "EndMonth1" -- 第一年止月
           , (TRUNC("IssueMonth" / 100) + 2) * 100 + MOD("IssueMonth", 100) AS "EndMonth2" -- 第二年止月
           , (TRUNC("IssueMonth" / 100) + 3) * 100 + MOD("IssueMonth", 100) AS "EndMonth3" -- 第三年止月
           , (TRUNC("IssueMonth" / 100) + 4) * 100 + MOD("IssueMonth", 100) AS "EndMonth4" -- 第四年止月
           , (TRUNC("IssueMonth" / 100) + 5) * 100 + MOD("IssueMonth", 100) AS "EndMonth5" -- 第五年止月
           , ROW_NUMBER()
             OVER (
               PARTITION BY "CustNo"
               ORDER BY "FacmNo"
                      , "BormNo"
             ) AS "Seq"
           , ROW_NUMBER()
             OVER (
               PARTITION BY "CustNo"
                          , "FacmNo"
               ORDER BY "BormNo"
             ) AS "FacSeq"
      FROM RawData
    )
    , Law AS (
      -- 取各戶號每個月實收法拍費用,2022/11/1改為入帳日OpenAcDate年月取貸方
      SELECT "CustNo"
           , TRUNC("OpenAcDate" / 100)  AS "Month"  --  2022/11/1:CloseDate改為OpenAcDate
           , SUM("Fee")                 AS "LawFee"
      FROM "ForeclosureFee"
      WHERE --"CloseDate" > 0  --  2022/11/1點掉
            "Fee" < 0  --  2022/11/1改為貸方,金額小於0  
        --AND "FeeCode" NOT IN ('11','15') -- 排除全額沖銷、催收沖銷資料
      GROUP BY "CustNo"
             , TRUNC("OpenAcDate" / 100)
    )
    , Insu AS (
      -- 取各額度每個月實收火險費用
      SELECT "CustNo"
           , "FacmNo"
           , TRUNC("AcDate" / 100) AS "Month"
           , SUM("TotInsuPrem")    AS "InsuFee"
      FROM "InsuRenew"
      WHERE "AcDate" > 0
        AND "TotInsuPrem" > 0
      GROUP BY "CustNo"
             , "FacmNo"
             , TRUNC("AcDate" / 100)
    )
    , LawData AS (
      SELECT M."CustNo"
           , M."FacmNo"
           , M."BormNo"
           , SUM(
              CASE
                WHEN L."Month" > M."IssueMonth"
                     AND L."Month" <= M."EndMonth1"
                THEN ABS(L."LawFee")    --2022/11/1 累計貸方為負值,取正數
              ELSE 0 END ) AS "LawFee1" -- 第一年法務費用
           , SUM(
              CASE
                WHEN L."Month" > M."EndMonth1"
                     AND L."Month" <= M."EndMonth2"
                THEN ABS(L."LawFee")  
              ELSE 0 END ) AS "LawFee2" -- 第二年法務費用
           , SUM(
              CASE
                WHEN L."Month" > M."EndMonth2"
                     AND L."Month" <= M."EndMonth3"
                THEN ABS(L."LawFee")    
              ELSE 0 END ) AS "LawFee3" -- 第三年法務費用
           , SUM(
              CASE
                WHEN L."Month" > M."EndMonth3"
                     AND L."Month" <= M."EndMonth4"
                THEN ABS(L."LawFee")    
              ELSE 0 END ) AS "LawFee4" -- 第四年法務費用
           , SUM(
              CASE
                WHEN L."Month" > M."EndMonth4"
                     AND L."Month" <= M."EndMonth5"
                THEN ABS(L."LawFee")    
              ELSE 0 END ) AS "LawFee5" -- 第五年法務費用
      FROM MonthData M
      LEFT JOIN Law L ON L."CustNo" = M."CustNo"
      GROUP BY M."CustNo"
             , M."FacmNo"
             , M."BormNo"
    )
    , InsuData AS (
      SELECT M."CustNo"
           , M."FacmNo"
           , M."BormNo"
           , SUM(
              CASE
                WHEN I."Month" > M."IssueMonth"
                     AND I."Month" <= M."EndMonth1"
                THEN I."InsuFee"
              ELSE 0 END ) AS "InsuFee1" -- 第一年火險費用
           , SUM(
              CASE
                WHEN I."Month" > M."EndMonth1"
                     AND I."Month" <= M."EndMonth2"
                THEN I."InsuFee"
              ELSE 0 END ) AS "InsuFee2" -- 第二年火險費用
           , SUM(
              CASE
                WHEN I."Month" > M."EndMonth2"
                     AND I."Month" <= M."EndMonth3"
                THEN I."InsuFee"
              ELSE 0 END ) AS "InsuFee3" -- 第三年火險費用
           , SUM(
              CASE
                WHEN I."Month" > M."EndMonth3"
                     AND I."Month" <= M."EndMonth4"
                THEN I."InsuFee"
              ELSE 0 END ) AS "InsuFee4" -- 第四年火險費用
           , SUM(
              CASE
                WHEN I."Month" > M."EndMonth4"
                     AND I."Month" <= M."EndMonth5"
                THEN I."InsuFee"
              ELSE 0 END ) AS "InsuFee5" -- 第五年火險費用
      FROM MonthData M
      LEFT JOIN Insu I ON I."CustNo" = M."CustNo"
                      AND I."FacmNo" = M."FacmNo"
      GROUP BY M."CustNo"
             , M."FacmNo"
             , M."BormNo"
    )
    , LoanData AS (
      SELECT M."CustNo"
           , M."FacmNo"
           , M."BormNo"
           , SUM (
               CASE
                 WHEN MLB."YearMonth" = M."EndMonth1"
                 THEN MLB."LoanBalance"
               ELSE 0 END
             )                       AS "LoanBal1" -- 發生日後第一年餘額
           , SUM (
               CASE
                 WHEN MLB."YearMonth" = M."EndMonth2"
                 THEN MLB."LoanBalance"
               ELSE 0 END
             )                       AS "LoanBal2" -- 發生日後第二年餘額
           , SUM (
               CASE
                 WHEN MLB."YearMonth" = M."EndMonth3"
                 THEN MLB."LoanBalance"
               ELSE 0 END
             )                       AS "LoanBal3" -- 發生日後第三年餘額
           , SUM (
               CASE
                 WHEN MLB."YearMonth" = M."EndMonth4"
                 THEN MLB."LoanBalance"
               ELSE 0 END
             )                       AS "LoanBal4" -- 發生日後第四年餘額
           , SUM (
               CASE
                 WHEN MLB."YearMonth" = M."EndMonth5"
                 THEN MLB."LoanBalance"
               ELSE 0 END
             )                       AS "LoanBal5" -- 發生日後第五年餘額
           , M."Seq"
           , M."FacSeq"
      FROM MonthData M
      LEFT JOIN "MonthlyLoanBal" MLB ON MLB."CustNo" = M."CustNo"
                                    AND MLB."FacmNo" = M."FacmNo"
                                    AND MLB."BormNo" = M."FacmNo"
                                    AND MLB."YearMonth" IN (
                                            M."EndMonth1"
                                          , M."EndMonth2"
                                          , M."EndMonth3"
                                          , M."EndMonth4"
                                          , M."EndMonth5"
                                        )
      GROUP BY M."CustNo"
             , M."FacmNo"
             , M."BormNo"
             , M."Seq"
             , M."FacSeq"
    )
    , CustTotal AS (
      SELECT "CustNo"
           , SUM("LoanBal1")              AS "CustTotal1" -- 發生日後第一年戶號合計餘額
           , SUM("LoanBal2")              AS "CustTotal2" -- 發生日後第二年戶號合計餘額
           , SUM("LoanBal3")              AS "CustTotal3" -- 發生日後第三年戶號合計餘額
           , SUM("LoanBal4")              AS "CustTotal4" -- 發生日後第四年戶號合計餘額
           , SUM("LoanBal5")              AS "CustTotal5" -- 發生日後第五年戶號合計餘額
      FROM LoanData
      GROUP BY "CustNo"
    )
    , FacTotal AS (
      SELECT "CustNo"
           , "FacmNo"
           , SUM("LoanBal1")              AS "FacTotal1" -- 發生日後第一年額度合計餘額
           , SUM("LoanBal2")              AS "FacTotal2" -- 發生日後第二年額度合計餘額
           , SUM("LoanBal3")              AS "FacTotal3" -- 發生日後第三年額度合計餘額
           , SUM("LoanBal4")              AS "FacTotal4" -- 發生日後第四年額度合計餘額
           , SUM("LoanBal5")              AS "FacTotal5" -- 發生日後第五年額度合計餘額
      FROM LoanData 
      GROUP BY "CustNo"
             , "FacmNo"
    )
    , GetMaxSeq AS (
      SELECT "CustNo"
           , MAX("Seq") AS "MaxSeq"
      FROM MonthData
      GROUP BY "CustNo"
    )
    , GetFacMaxSeq AS (
      SELECT "CustNo"
           , "FacmNo"
           , MAX("Seq") AS "MaxSeq"
      FROM MonthData
      GROUP BY "CustNo"
             , "FacmNo"
    )
    , AvgFeeDataBase AS (
      SELECT L."CustNo"
           , L."FacmNo"
           , L."BormNo" 
           , L."Seq"
           , L."FacSeq"
           , LD."LawFee1" -- 第一年法務費用
           , LD."LawFee2" -- 第二年法務費用
           , LD."LawFee3" -- 第三年法務費用
           , LD."LawFee4" -- 第四年法務費用
           , LD."LawFee5" -- 第五年法務費用
           , ID."InsuFee1" -- 第一年火險費用
           , ID."InsuFee2" -- 第二年火險費用
           , ID."InsuFee3" -- 第三年火險費用
           , ID."InsuFee4" -- 第四年火險費用
           , ID."InsuFee5" -- 第五年火險費用
           , CT."CustTotal1"  --  發生日後第一年戶號合計餘額
           , CT."CustTotal2"  --  發生日後第二年戶號合計餘額
           , CT."CustTotal3"  --  發生日後第三年戶號合計餘額
           , CT."CustTotal4"  --  發生日後第四年戶號合計餘額
           , CT."CustTotal5"  --  發生日後第五年戶號合計餘額
           , FT."FacTotal1"   --  發生日後第一年額度合計餘額
           , FT."FacTotal2"   --  發生日後第二年額度合計餘額
           , FT."FacTotal3"   --  發生日後第三年額度合計餘額
           , FT."FacTotal4"   --  發生日後第四年額度合計餘額
           , FT."FacTotal5"   --  發生日後第五年額度合計餘額
           , CASE
               WHEN CT."CustTotal1" > 0
               THEN ROUND(LD."LawFee1" * L."LoanBal1" / CT."CustTotal1",0)
              ELSE 0 END      AS "AvgLawFee1"
           , CASE
               WHEN CT."CustTotal2" > 0
               THEN ROUND(LD."LawFee2" * L."LoanBal2" / CT."CustTotal2",0)
              ELSE 0 END      AS "AvgLawFee2"
           , CASE
               WHEN CT."CustTotal3" > 0
               THEN ROUND(LD."LawFee3" * L."LoanBal3" / CT."CustTotal3",0)
              ELSE 0 END      AS "AvgLawFee3"
           , CASE
               WHEN CT."CustTotal4" > 0
               THEN ROUND(LD."LawFee4" * L."LoanBal4" / CT."CustTotal4",0)
              ELSE 0 END      AS "AvgLawFee4"
           , CASE
               WHEN CT."CustTotal5" > 0
               THEN ROUND(LD."LawFee5" * L."LoanBal5" / CT."CustTotal5",0)
              ELSE 0 END      AS "AvgLawFee5"
           , CASE
               WHEN FT."FacTotal1" > 0
               THEN ROUND(ID."InsuFee1" * L."LoanBal1" / FT."FacTotal1",0)
             ELSE 0 END       AS "AvgInsuFee1"
           , CASE
               WHEN FT."FacTotal2" > 0
               THEN ROUND(ID."InsuFee2" * L."LoanBal2" / FT."FacTotal2",0)
             ELSE 0 END       AS "AvgInsuFee2"
           , CASE
               WHEN FT."FacTotal3" > 0
               THEN ROUND(ID."InsuFee3" * L."LoanBal3" / FT."FacTotal3",0)
             ELSE 0 END       AS "AvgInsuFee3"
           , CASE
               WHEN FT."FacTotal4" > 0
               THEN ROUND(ID."InsuFee4" * L."LoanBal4" / FT."FacTotal4",0)
             ELSE 0 END       AS "AvgInsuFee4"
           , CASE
               WHEN FT."FacTotal5" > 0
               THEN ROUND(ID."InsuFee5" * L."LoanBal5" / FT."FacTotal5",0)
             ELSE 0 END       AS "AvgInsuFee5"
      FROM LoanData L
      LEFT JOIN CustTotal CT ON CT."CustNo" = L."CustNo"
      LEFT JOIN FacTotal FT ON FT."CustNo" = L."CustNo"
                           AND FT."FacmNo" = L."FacmNo"
      LEFT JOIN LawData LD ON LD."CustNo" = L."CustNo"
                          AND LD."FacmNo" = L."FacmNo"
                          AND LD."BormNo" = L."BormNo"
      LEFT JOIN InsuData ID ON ID."CustNo" = L."CustNo"
                           AND ID."FacmNo" = L."FacmNo"
                           AND ID."BormNo" = L."BormNo"

    )
    , AvgFeeDataFinal AS (
      
      -- 最後一筆用總費用減去其他筆費用
      SELECT B."CustNo"
           , B."FacmNo"
           , B."BormNo"
           , SUM(
             CASE
               WHEN B."Seq" = G1."MaxSeq"
               THEN B."LawFee1" - NVL(O1."OtherLawFee1",0)
             ELSE B."AvgLawFee1"
             END)                               AS "AvgLawFee1"
           , SUM(
             CASE
               WHEN B."Seq" = G1."MaxSeq"
               THEN B."LawFee2" - NVL(O1."OtherLawFee2",0)
             ELSE B."AvgLawFee2"
             END)                               AS "AvgLawFee2"
           , SUM(
             CASE
               WHEN B."Seq" = G1."MaxSeq"
               THEN B."LawFee3" - NVL(O1."OtherLawFee3",0)
             ELSE B."AvgLawFee3"
             END)                               AS "AvgLawFee3"
           , SUM(
             CASE
               WHEN B."Seq" = G1."MaxSeq"
               THEN B."LawFee4" - NVL(O1."OtherLawFee4",0)
             ELSE B."AvgLawFee4"
             END)                               AS "AvgLawFee4"
           , SUM(
             CASE
               WHEN B."Seq" = G1."MaxSeq"
               THEN B."LawFee5" - NVL(O1."OtherLawFee5",0)
             ELSE B."AvgLawFee5"
             END)                               AS "AvgLawFee5"
           , SUM(
             CASE
               WHEN B."FacSeq" = G2."MaxSeq"
               THEN B."InsuFee1" - NVL(O2."OtherInsuFee1",0)
             ELSE B."AvgInsuFee1"
             END)                               AS "AvgInsuFee1"
           , SUM(
             CASE
               WHEN B."FacSeq" = G2."MaxSeq"
               THEN B."InsuFee2" - NVL(O2."OtherInsuFee2",0)
             ELSE B."AvgInsuFee2"
             END)                               AS "AvgInsuFee2"
           , SUM(
             CASE
               WHEN B."FacSeq" = G2."MaxSeq"
               THEN B."InsuFee3" - NVL(O2."OtherInsuFee3",0)
             ELSE B."AvgInsuFee3"
             END)                               AS "AvgInsuFee3"
           , SUM(
             CASE
               WHEN B."FacSeq" = G2."MaxSeq"
               THEN B."InsuFee4" - NVL(O2."OtherInsuFee4",0)
             ELSE B."AvgInsuFee4"
             END)                               AS "AvgInsuFee4"
           , SUM(
             CASE
               WHEN B."FacSeq" = G2."MaxSeq"
               THEN B."InsuFee5" - NVL(O2."OtherInsuFee5",0)
             ELSE B."AvgInsuFee5"
             END)                               AS "AvgInsuFee5"
      FROM AvgFeeDataBase B
      LEFT JOIN (
        SELECT B."CustNo"
             , SUM(B."AvgLawFee1") AS "OtherLawFee1"
             , SUM(B."AvgLawFee2") AS "OtherLawFee2"
             , SUM(B."AvgLawFee3") AS "OtherLawFee3"
             , SUM(B."AvgLawFee4") AS "OtherLawFee4"
             , SUM(B."AvgLawFee5") AS "OtherLawFee5"
        FROM AvgFeeDataBase B
        LEFT JOIN GetMaxSeq G ON G."CustNo" = B."CustNo"
        WHERE B."Seq" < G."MaxSeq"
        GROUP BY B."CustNo"
      ) O1 ON O1."CustNo" = B."CustNo"
      LEFT JOIN (
        SELECT B."CustNo"
             , B."FacmNo"
             , SUM(B."AvgInsuFee1") AS "OtherInsuFee1"
             , SUM(B."AvgInsuFee2") AS "OtherInsuFee2"
             , SUM(B."AvgInsuFee3") AS "OtherInsuFee3"
             , SUM(B."AvgInsuFee4") AS "OtherInsuFee4"
             , SUM(B."AvgInsuFee5") AS "OtherInsuFee5"
        FROM AvgFeeDataBase B
        LEFT JOIN GetFacMaxSeq G ON G."CustNo" = B."CustNo"
                                AND G."FacmNo" = B."FacmNo"
        WHERE B."FacSeq" < G."MaxSeq"
        GROUP BY B."CustNo"
               , B."FacmNo"
      ) O2 ON O2."CustNo" = B."CustNo"
          AND O2."FacmNo" = B."FacmNo"
      LEFT JOIN GetMaxSeq G1 ON G1."CustNo" = B."CustNo"
      LEFT JOIN GetFacMaxSeq G2 ON G2."CustNo" = B."CustNo"
                               AND G2."FacmNo" = B."FacmNo"
      GROUP BY B."CustNo"
             , B."FacmNo"
             , B."BormNo"
    )
    SELECT M."CustNo"                      AS  "CustNo"          -- 戶號
         , M."FacmNo"                      AS  "FacmNo"          -- 額度編號
         , M."BormNo"                      AS  "BormNo"          -- 撥款序號
         , NVL(M1."TotalLoanBal", 0)       AS  "TotalLoanBal"    -- 同額度本金餘額合計
         , NVL(LR."FitRate",0)             AS  "StoreRate"       -- 減損發生日月底 計息利率
         , NVL(ML."LoanBalance",0)         AS  "LoanBalance"     -- 減損發生日月底 放款餘額
         -- 2022-7-12 Wei from Linda
         -- 上述發生日期時之應收利息(台幣)=繳息迄日~發生日期間的利息
         -- 若此區間無新利率則利息=發生日時餘額*發生日時利率*120/360/100
         -- 若此區間有不同利率則以分段計算
         , CASE
             WHEN M."DerDate" != 0 -- 減損發生日
             THEN "Fn_CalculateDerogationInterest"(M."CustNo",M."FacmNo",M."BormNo",NVL(ML."LoanBalance",0),NVL(LR."FitRate",0),JML."PrevPayIntDate",M."DerDate")
           ELSE 0 END                       AS  "IntAmt"          -- 減損發生日月底 應收利息
         , NVL("Fn_GetUnpaidInsuFee"(M."CustNo", M."FacmNo", M."BormNo", M."DerDate") , 0)
           + NVL("Fn_GetUnpaidForeclosureFee"(M."CustNo", M."FacmNo", M."BormNo", M."DerDate") , 0)
                                            AS  "Fee"             -- 減損發生日月底 費用 (火險+法務)
         , CASE WHEN TRUNC(M."DerDate" / 100) >  YYYYMM THEN 0   -- 減損發生日該月大於本月年月
                WHEN ML."LoanBalance" IS NULL AND ML1."LoanBalance" IS NULL THEN
                     NVL(M."DrawdownAmt",0) - NVL(MLE."LoanBalance",0) -- 減損發生日該月與第一年該月無資料改用撥款金額減當月餘額
                WHEN ML."LoanBalance" IS NULL THEN 
                     NVL(M."DrawdownAmt",0) - NVL(ML1."LoanBalance",0) -- 減損發生日該月無資料改用撥款金額減第一年餘額
                WHEN ML1."LoanBalance" IS NULL THEN
                     NVL(ML."LoanBalance",0) -  NVL(MLE."LoanBalance",0) -- 減損發生日後無第一年該月資料改用發生日月底餘額減當月餘額
                ELSE NVL(ML."LoanBalance",0) -  NVL(ML1."LoanBalance",0)
           END                             AS  "DerY1Amt"        -- 個案減損客觀證據發生後第一年本金回收金額
         , CASE WHEN ML1."LoanBalance" IS NULL  THEN 0
                WHEN ML2."LoanBalance" IS NULL  THEN 
                     NVL(ML1."LoanBalance",0) - NVL(MLE."LoanBalance",0) -- 減損發生日後無第二年該月資料改用第一年餘額減當月餘額
                ELSE NVL(ML1."LoanBalance",0) - NVL(ML2."LoanBalance",0)
           END                             AS  "DerY2Amt"        -- 個案減損客觀證據發生後第二年本金回收金額
         , CASE WHEN ML2."LoanBalance" IS NULL  THEN 0
                WHEN ML3."LoanBalance" IS NULL  THEN 
                     NVL(ML2."LoanBalance",0) - NVL(MLE."LoanBalance",0) -- 減損發生日後無第三年該月資料改用第二年餘額減當月餘額
                ELSE NVL(ML2."LoanBalance",0) - NVL(ML3."LoanBalance",0)
           END                             AS  "DerY3Amt"        -- 個案減損客觀證據發生後第三年本金回收金額
         , CASE WHEN ML3."LoanBalance" IS NULL  THEN 0
                WHEN ML4."LoanBalance" IS NULL  THEN 
                     NVL(ML3."LoanBalance",0) - NVL(MLE."LoanBalance",0) -- 減損發生日後無第四年該月資料改用第三年餘額減當月餘額
                ELSE NVL(ML3."LoanBalance",0) - NVL(ML4."LoanBalance",0)
           END                             AS  "DerY4Amt"        -- 個案減損客觀證據發生後第四年本金回收金額
         , CASE WHEN ML4."LoanBalance" IS NULL  THEN 0
                WHEN ML5."LoanBalance" IS NULL  THEN 
                     NVL(ML4."LoanBalance",0) - NVL(MLE."LoanBalance",0) -- 減損發生日後無第五年該月資料改用第四年餘額減當月餘額
                ELSE NVL(ML4."LoanBalance",0) -  NVL(ML5."LoanBalance",0)
           END                             AS  "DerY5Amt"        -- 個案減損客觀證據發生後第五年本金回收金額
         , NVL(INT1."IntAmtRcv",0)         AS  "DerY1Int"        -- 個案減損客觀證據發生後第一年應收利息回收金額
         , NVL(INT2."IntAmtRcv",0)         AS  "DerY2Int"        -- 個案減損客觀證據發生後第二年應收利息回收金額
         , NVL(INT3."IntAmtRcv",0)         AS  "DerY3Int"        -- 個案減損客觀證據發生後第三年應收利息回收金額
         , NVL(INT4."IntAmtRcv",0)         AS  "DerY4Int"        -- 個案減損客觀證據發生後第四年應收利息回收金額
         , NVL(INT5."IntAmtRcv",0)         AS  "DerY5Int"        -- 個案減損客觀證據發生後第五年應收利息回收金額
         , CASE WHEN TRUNC(M."DerDate" / 100) >=  YYYYMM THEN 0  -- 若發生日與本月底日是同年月則不計入
                ELSE NVL(AF."AvgLawFee1",0) + NVL(AF."AvgInsuFee1",0)
           END                             AS  "DerY1Fee"        -- 個案減損客觀證據發生後第一年法拍及火險費用回收金額
         , NVL(AF."AvgLawFee2",0)
           + NVL(AF."AvgInsuFee2",0)       AS  "DerY2Fee"        -- 個案減損客觀證據發生後第二年法拍及火險費用回收金額
         , NVL(AF."AvgLawFee3",0)
           + NVL(AF."AvgInsuFee3",0)       AS  "DerY3Fee"        -- 個案減損客觀證據發生後第三年法拍及火險費用回收金額
         , NVL(AF."AvgLawFee4",0)
           + NVL(AF."AvgInsuFee4",0)       AS  "DerY4Fee"        -- 個案減損客觀證據發生後第四年法拍及火險費用回收金額
         , NVL(AF."AvgLawFee5",0)
           + NVL(AF."AvgInsuFee5",0)       AS  "DerY5Fee"        -- 個案減損客觀證據發生後第五年法拍及火險費用回收金額
    FROM   "LoanIfrs9Dp" M
        LEFT JOIN "JcicMonthlyLoanData" JML ON JML."DataYM" = YYYYMM
                                            AND JML."CustNo" = M."CustNo"
                                            AND JML."FacmNo" = M."FacmNo"
                                            AND JML."BormNo" = M."BormNo"
      -- 同額度本金餘額合計
      LEFT JOIN ( SELECT M."DataYM"                 AS "DataYM"
                       , M."CustNo"                 AS "CustNo"
                       , M."FacmNo"                 AS "FacmNo"
                       , SUM(M."LoanBal")           AS "TotalLoanBal"
                  FROM     "LoanIfrs9Dp" M
                  WHERE    M."DataYM"          =  YYYYMM
                  GROUP BY M."DataYM", M."CustNo", M."FacmNo"
                ) M1    ON M1."DataYM"   =  M."DataYM"
                       AND M1."CustNo"   =  M."CustNo"
                       AND M1."FacmNo"   =  M."FacmNo"
      -- 減損發生日時之月底 (額度)   -- 火險費用 法務費用
      LEFT JOIN "MonthlyFacBal"  MF  ON  MF."YearMonth"  = TRUNC(M."DerDate" / 100)
                                    AND  MF."CustNo"     = M."CustNo"
                                    AND  MF."FacmNo"     = M."FacmNo"
      -- 減損發生日時之月底 (放款)
      LEFT JOIN "MonthlyLoanBal" ML  ON  ML."YearMonth"  = TRUNC(M."DerDate" / 100)
                                    AND  ML."CustNo"     = M."CustNo"
                                    AND  ML."FacmNo"     = M."FacmNo"
                                    AND  ML."BormNo"     = M."BormNo"
      -- 減損發生日第一年 (放款)
      LEFT JOIN "MonthlyLoanBal" ML1 ON  ML1."YearMonth" = TRUNC(M."DerDate" / 100) + 100
                                    AND  ML1."CustNo"    = M."CustNo"
                                    AND  ML1."FacmNo"    = M."FacmNo"
                                    AND  ML1."BormNo"    = M."BormNo"
      -- 減損發生日第二年 (放款)
      LEFT JOIN "MonthlyLoanBal" ML2 ON  ML2."YearMonth" = TRUNC(M."DerDate" / 100) + 200
                                    AND  ML2."CustNo"    = M."CustNo"
                                    AND  ML2."FacmNo"    = M."FacmNo"
                                    AND  ML2."BormNo"    = M."BormNo"
      -- 減損發生日第三年 (放款)
      LEFT JOIN "MonthlyLoanBal" ML3 ON  ML3."YearMonth" = TRUNC(M."DerDate" / 100) + 300
                                    AND  ML3."CustNo"    = M."CustNo"
                                    AND  ML3."FacmNo"    = M."FacmNo"
                                    AND  ML3."BormNo"    = M."BormNo"
      -- 減損發生日第四年 (放款)
      LEFT JOIN "MonthlyLoanBal" ML4 ON  ML4."YearMonth" = TRUNC(M."DerDate" / 100) + 400
                                    AND  ML4."CustNo"    = M."CustNo"
                                    AND  ML4."FacmNo"    = M."FacmNo"
                                    AND  ML4."BormNo"    = M."BormNo"
      -- 減損發生日第五年 (放款)
      LEFT JOIN "MonthlyLoanBal" ML5 ON  ML5."YearMonth" = TRUNC(M."DerDate" / 100) + 500
                                    AND  ML5."CustNo"    = M."CustNo"
                                    AND  ML5."FacmNo"    = M."FacmNo"
                                    AND  ML5."BormNo"    = M."BormNo"
      -- 減損發生日後本月月底 (放款)
      LEFT JOIN "MonthlyLoanBal" MLE ON  MLE."YearMonth" = YYYYMM
                                    AND  MLE."CustNo"    = M."CustNo"
                                    AND  MLE."FacmNo"    = M."FacmNo"
                                    AND  MLE."BormNo"    = M."BormNo"
      -- 減損發生日第一年實收利息 (放款)
      LEFT JOIN ( SELECT M."CustNo"                    AS  "CustNo"
                       , M."FacmNo"                    AS  "FacmNo"
                       , M."BormNo"                    AS  "BormNo"
                       , SUM(NVL(ML."IntAmtRcv",0))    AS  "IntAmtRcv"
                  FROM   "LoanIfrs9Dp" M
                    LEFT JOIN "MonthlyLoanBal" ML
                           ON ML."YearMonth"   >  TRUNC(M."DerDate" / 100)
                          AND ML."YearMonth"   <=  TRUNC(M."DerDate" / 100) + 100
                          AND ML."CustNo"      =  M."CustNo"
                          AND ML."FacmNo"      =  M."FacmNo"
                          AND ML."BormNo"      =  M."BormNo"
                  WHERE  M."DataYM"      = YYYYMM
                    AND  M."DataFg"      =  2
                  GROUP BY  M."CustNo", M."FacmNo", M."BormNo"
                ) INT1  ON  INT1."CustNo"     = M."CustNo"
                       AND  INT1."FacmNo"     = M."FacmNo"
                       AND  INT1."BormNo"     = M."BormNo"
      -- 減損發生日第二年實收利息 (放款)
      LEFT JOIN ( SELECT M."CustNo"                    AS  "CustNo"
                       , M."FacmNo"                    AS  "FacmNo"
                       , M."BormNo"                    AS  "BormNo"
                       , SUM(NVL(ML."IntAmtRcv",0))    AS  "IntAmtRcv"
                  FROM   "LoanIfrs9Dp" M
                    LEFT JOIN "MonthlyLoanBal" ML
                           ON ML."YearMonth"   >  TRUNC(M."DerDate" / 100) + 100
                          AND ML."YearMonth"   <= TRUNC(M."DerDate" / 100) + 200
                          AND ML."CustNo"      =  M."CustNo"
                          AND ML."FacmNo"      =  M."FacmNo"
                          AND ML."BormNo"      =  M."BormNo"
                  WHERE  M."DataYM"      = YYYYMM
                    AND  M."DataFg"      =  2
                 GROUP BY  M."CustNo", M."FacmNo", M."BormNo"
                ) INT2  ON  INT2."CustNo"     = M."CustNo"
                       AND  INT2."FacmNo"     = M."FacmNo"
                       AND  INT2."BormNo"     = M."BormNo"
      -- 減損發生日第三年實收利息 (放款)
      LEFT JOIN ( SELECT M."CustNo"                    AS  "CustNo"
                       , M."FacmNo"                    AS  "FacmNo"
                       , M."BormNo"                    AS  "BormNo"
                       , SUM(NVL(ML."IntAmtRcv",0))    AS  "IntAmtRcv"
                  FROM   "LoanIfrs9Dp" M
                    LEFT JOIN "MonthlyLoanBal" ML
                           ON ML."YearMonth"   >  TRUNC(M."DerDate" / 100) + 200
                          AND ML."YearMonth"   <= TRUNC(M."DerDate" / 100) + 300
                          AND ML."CustNo"      =  M."CustNo"
                          AND ML."FacmNo"      =  M."FacmNo"
                          AND ML."BormNo"      =  M."BormNo"
                  WHERE  M."DataYM"      = YYYYMM
                    AND  M."DataFg"      =  2
                  GROUP BY  M."CustNo", M."FacmNo", M."BormNo"
                ) INT3  ON  INT3."CustNo"     = M."CustNo"
                       AND  INT3."FacmNo"     = M."FacmNo"
                       AND  INT3."BormNo"     = M."BormNo"
      -- 減損發生日第四年實收利息 (放款)
      LEFT JOIN ( SELECT M."CustNo"                    AS  "CustNo"
                       , M."FacmNo"                    AS  "FacmNo"
                       , M."BormNo"                    AS  "BormNo"
                       , SUM(NVL(ML."IntAmtRcv",0))    AS  "IntAmtRcv"
                  FROM   "LoanIfrs9Dp" M
                    LEFT JOIN "MonthlyLoanBal" ML
                           ON ML."YearMonth"   >  TRUNC(M."DerDate" / 100) + 300
                          AND ML."YearMonth"   <= TRUNC(M."DerDate" / 100) + 400
                          AND ML."CustNo"      =  M."CustNo"
                          AND ML."FacmNo"      =  M."FacmNo"
                          AND ML."BormNo"      =  M."BormNo"
                  WHERE  M."DataYM"      = YYYYMM
                    AND  M."DataFg"      =  2
                  GROUP BY  M."CustNo", M."FacmNo", M."BormNo"
                ) INT4  ON  INT4."CustNo"     = M."CustNo"
                       AND  INT4."FacmNo"     = M."FacmNo"
                       AND  INT4."BormNo"     = M."BormNo"
      -- 減損發生日第五年實收利息 (放款)
      LEFT JOIN ( SELECT M."CustNo"                    AS  "CustNo"
                       , M."FacmNo"                    AS  "FacmNo"
                       , M."BormNo"                    AS  "BormNo"
                       , SUM(NVL(ML."IntAmtRcv",0))    AS  "IntAmtRcv"
                  FROM   "LoanIfrs9Dp" M
                    LEFT JOIN "MonthlyLoanBal" ML
                           ON ML."YearMonth"   >  TRUNC(M."DerDate" / 100) + 400
                          AND ML."YearMonth"   <= TRUNC(M."DerDate" / 100) + 500
                          AND ML."CustNo"      =  M."CustNo"
                          AND ML."FacmNo"      =  M."FacmNo"
                          AND ML."BormNo"      =  M."BormNo"
                  WHERE  M."DataYM"      = YYYYMM
                    AND  M."DataFg"      =  2
                  GROUP BY  M."CustNo", M."FacmNo", M."BormNo"
                ) INT5  ON  INT5."CustNo"     = M."CustNo"
                       AND  INT5."FacmNo"     = M."FacmNo"
                       AND  INT5."BormNo"     = M."BormNo"
      -- 減損發生日時之計息利率
      LEFT JOIN LR ON LR."CustNo"  = M."CustNo"
                  AND LR."FacmNo"  = M."FacmNo"
                  AND LR."BormNo"  = M."BormNo"
                  AND LR."Seq" = 1
      LEFT JOIN AvgFeeDataFinal AF ON AF."CustNo" = M."CustNo"
                                  AND AF."FacmNo" = M."FacmNo"
                                  AND AF."BormNo" = M."BormNo"
                                   
    WHERE    M."DataYM"          =  YYYYMM
      AND    M."DataFg"          =  2 
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Work_DP END: UPD_CNT=' || UPD_CNT);


-- 更新  減損發生日時之月底各放款資料
    DBMS_OUTPUT.PUT_LINE('UPDATE 減損發生日時之月底各資料');
    UPD_CNT := 0;

    MERGE INTO "LoanIfrs9Dp" M
    USING ( SELECT W."CustNo"               AS  "CustNo"          -- 戶號
                 , W."FacmNo"               AS  "FacmNo"          -- 額度編號
                 , W."BormNo"               AS  "BormNo"          -- 撥款序號
                 , W."TotalLoanBal"         AS  "TotalLoanBal"    -- 同額度本金餘額合計
                 , W."StoreRate"            AS  "StoreRate"       -- 減損發生日月底 計息利率
                 , W."LoanBalance"          AS  "LoanBalance"     -- 減損發生日月底 放款餘額
                 , W."IntAmt"               AS  "IntAmt"          -- 減損發生日月底 應收利息
                 , W."Fee"                  AS  "Fee"             -- 減損發生日月底 費用
                 , W."DerY1Amt"             AS  "DerY1Amt"        -- 個案減損客觀證據發生後第一年本金回收金額
                 , W."DerY2Amt"             AS  "DerY2Amt"        -- 個案減損客觀證據發生後第二年本金回收金額
                 , W."DerY3Amt"             AS  "DerY3Amt"        -- 個案減損客觀證據發生後第三年本金回收金額
                 , W."DerY4Amt"             AS  "DerY4Amt"        -- 個案減損客觀證據發生後第四年本金回收金額
                 , W."DerY5Amt"             AS  "DerY5Amt"        -- 個案減損客觀證據發生後第五年本金回收金額
                 , W."DerY1Int"             AS  "DerY1Int"        -- 個案減損客觀證據發生後第一年應收利息回收金額
                 , W."DerY2Int"             AS  "DerY2Int"        -- 個案減損客觀證據發生後第二年應收利息回收金額
                 , W."DerY3Int"             AS  "DerY3Int"        -- 個案減損客觀證據發生後第三年應收利息回收金額
                 , W."DerY4Int"             AS  "DerY4Int"        -- 個案減損客觀證據發生後第四年應收利息回收金額
                 , W."DerY5Int"             AS  "DerY5Int"        -- 個案減損客觀證據發生後第五年應收利息回收金額
                 , W."DerY1Fee"             AS  "DerY1Fee"        -- 個案減損客觀證據發生後第一年法拍及火險費用回收金額
                 , W."DerY2Fee"             AS  "DerY2Fee"        -- 個案減損客觀證據發生後第二年法拍及火險費用回收金額
                 , W."DerY3Fee"             AS  "DerY3Fee"        -- 個案減損客觀證據發生後第三年法拍及火險費用回收金額
                 , W."DerY4Fee"             AS  "DerY4Fee"        -- 個案減損客觀證據發生後第四年法拍及火險費用回收金額
                 , W."DerY5Fee"             AS  "DerY5Fee"        -- 個案減損客觀證據發生後第五年法拍及火險費用回收金額
            FROM  "Work_DP" W
          ) T
    ON (    M."DataYM"   = YYYYMM
        AND M."CustNo"   = T."CustNo"
        AND M."FacmNo"   = T."FacmNo"
        AND M."BormNo"   = T."BormNo"
        AND M."DataFg"   =  2
       )
    WHEN MATCHED THEN UPDATE SET
         M."DerRate"    =  T."StoreRate" / 100
       , M."DerLoanBal" =  T."LoanBalance"
       , M."DerIntAmt"  =  T."IntAmt"
       , M."DerFee"     =  T."Fee"
       , M."DerY1Amt"   =  T."DerY1Amt"
       , M."DerY2Amt"   =  T."DerY2Amt"
       , M."DerY3Amt"   =  T."DerY3Amt"
       , M."DerY4Amt"   =  T."DerY4Amt"
       , M."DerY5Amt"   =  T."DerY5Amt"
       , M."DerY1Int"   =  T."DerY1Int"
       , M."DerY2Int"   =  T."DerY2Int"
       , M."DerY3Int"   =  T."DerY3Int"
       , M."DerY4Int"   =  T."DerY4Int"
       , M."DerY5Int"   =  T."DerY5Int"
       , M."DerY1Fee"   =  T."DerY1Fee"
       , M."DerY2Fee"   =  T."DerY2Fee"
       , M."DerY3Fee"   =  T."DerY3Fee"
       , M."DerY4Fee"   =  T."DerY4Fee"
       , M."DerY5Fee"   =  T."DerY5Fee"
    ;


    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE 減損發生日時之月底各放款資料 End');


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;