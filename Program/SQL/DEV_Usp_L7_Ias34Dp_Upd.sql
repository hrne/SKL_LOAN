-- 程式功能：維護 Ias34Dp 每月IAS34資料欄位清單D檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias34Dp_Upd"(20201231,'System',0);
--


CREATE OR REPLACE PROCEDURE "Usp_L7_Ias34Dp_Upd"
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    NewAcFg        IN  INT         -- 0=使用舊會計科目(8碼) 1=使用新會計科目(11碼)
)
AS
BEGIN
	"Usp_L7_Ias34Dp_Upd_Prear"(); -- Work_Ias34DP 資料清檔
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

    --  本月年月
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


-- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE Ias34Dp');

    DELETE FROM "Ias34Dp"
    WHERE "DataYM" = YYYYMM
    ;

-- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Dp');
    INS_CNT := 0;

    INSERT INTO "Ias34Dp"
    SELECT
           YYYYMM                                    AS "DataYM"            -- 資料年月
         , FF."CustNo"                               AS "CustNo"            -- 戶號
         , NVL(M."CustId",' ')                       AS "CustId"            -- 借款人ID / 統編
         , FF."FacmNo"                               AS "FacmNo"            -- 額度編號
         , M."BormNo"                                AS "BormNo"            -- 撥款序號
         , CASE WHEN NewAcFg = 0 THEN RPAD(NVL("CdAcCode"."AcNoCodeOld",' '),8,' ')   -- 舊
                ELSE                  RPAD(NVL("CdAcCode"."AcNoCode",' '),11,' ')     -- 新
           END                                       AS "AcCode"            -- 會計科目
         , CASE WHEN M."Status" IN (2)      THEN 2   -- 催收
                WHEN M."Status" IN (7)      THEN 3   -- 呆帳 (部份轉呆)
                ELSE  1                              -- 正常
           END                                       AS "Status"             -- 案件狀態
         , NVL(F."FirstDrawdownDate",0)              AS "FirstDrawdownDate"  -- 初貸日期
         , NVL(M."DrawdownDate",0)                   AS "DrawdownDate"       -- 貸放日期
         , NVL(M."MaturityDate",0)                   AS "MaturityDate"       -- 到期日
         , NVL(F."LineAmt",0)                        AS "LineAmt"            -- 核准金額  --每額度編號項下之放款帳號皆同
         , NVL(M."DrawdownAmt",0)                    AS "DrawdownAmt"        -- 撥款金額
         , NVL(M."LoanBal",0)                        AS "LoanBal"            -- 本金餘額(撥款)
         , NVL(M."IntAmt",0)                         AS "IntAmt"             -- 應收利息
         , NVL(MF."FireFee",0) + NVL(MF."LawFee",0)  AS "Fee"                -- 法拍及火險費用
         , CASE WHEN M."NextPayIntDate" IS NULL  THEN 0
                WHEN ( TO_DATE(FF."FinishedDate",'yyyy-mm-dd') - TO_DATE(M."NextPayIntDate",'yyyy-mm-dd') ) > 999 THEN 999
                ELSE ( TO_DATE(FF."FinishedDate",'yyyy-mm-dd') - TO_DATE(M."NextPayIntDate",'yyyy-mm-dd') )
           END                                       AS "OvduDays"           -- 逾期繳款天數  -- 應繳日～拍定完成日
         , NVL(M."OvduDate", 0)                      AS "OvduDate"           -- 轉催收款日期
         , NVL(M."BadDebtDate", 0)                   AS "BadDebtDate"        -- 轉銷呆帳日期  -- 最早之轉銷呆帳日期
         , NVL(OD."BadDebtAmt", 0)                   AS "BadDebtAmt"         -- 轉銷呆帳金額
         , CASE WHEN LOS."CustNo" IS NOT NULL THEN  LOS."MarkDate"   -- 特殊減損件
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
         , 0      AS "DerRate"            -- 上述發生日期前之最近一次利率
         , 0      AS "DerLoanBal"         -- 上述發生日期時之本金餘額
         , 0      AS "DerIntAmt"          -- 上述發生日期時之應收利息
         , 0      AS "DerFee"             -- 上述發生日期時之法拍及火險費用
         , 0      AS "DerY1Amt"           -- 個案減損客觀證據發生後第一年本金回收金額
         , 0      AS "DerY2Amt"           -- 個案減損客觀證據發生後第二年本金回收金額
         , 0      AS "DerY3Amt"           -- 個案減損客觀證據發生後第三年本金回收金額
         , 0      AS "DerY4Amt"           -- 個案減損客觀證據發生後第四年本金回收金額
         , 0      AS "DerY5Amt"           -- 個案減損客觀證據發生後第五年本金回收金額
         , 0      AS "DerY1Int"           -- 個案減損客觀證據發生後第一年應收利息回收金額
         , 0      AS "DerY2Int"           -- 個案減損客觀證據發生後第二年應收利息回收金額
         , 0      AS "DerY3Int"           -- 個案減損客觀證據發生後第三年應收利息回收金額
         , 0      AS "DerY4Int"           -- 個案減損客觀證據發生後第四年應收利息回收金額
         , 0      AS "DerY5Int"           -- 個案減損客觀證據發生後第五年應收利息回收金額
         , 0      AS "DerY1Fee"           -- 個案減損客觀證據發生後第一年法拍及火險費用回收金額
         , 0      AS "DerY2Fee"           -- 個案減損客觀證據發生後第二年法拍及火險費用回收金額
         , 0      AS "DerY3Fee"           -- 個案減損客觀證據發生後第三年法拍及火險費用回收金額
         , 0      AS "DerY4Fee"           -- 個案減損客觀證據發生後第四年法拍及火險費用回收金額
         , 0      AS "DerY5Fee"           -- 個案減損客觀證據發生後第五年法拍及火險費用回收金額
         , CASE WHEN TRIM(NVL("CustMain"."IndustryCode", ' ')) = '' THEN ' '
                ELSE SUBSTR('000000' || TRIM("CustMain"."IndustryCode"), -6)
           END                                       AS "IndustryCode"       -- 授信行業別
         , NVL(M."ClTypeCode",' ')                   AS "ClTypeJCIC"         -- 擔保品類別
         , NVL("CdArea"."Zip3", ' ')                 AS "Zip3"               -- 擔保品地區別  -- 郵遞區號
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
    FROM   "ForeclosureFinished" FF
        LEFT JOIN "JcicMonthlyLoanData" M  ON M."DataYM"    = YYYYMM
                                          AND M."CustNo"    = FF."CustNo"
                                          AND M."FacmNo"    = FF."FacmNo"
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
        LEFT JOIN "CdArea"      ON "CdArea"."CityCode"   = "ClMain"."CityCode"
                               AND "CdArea"."AreaCode"   = "ClMain"."AreaCode"
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
      WHERE  TRUNC(NVL(FF."FinishedDate",0) / 100) <= YYYYMM        -- 法拍完成日 <= 會計日
        AND  TRUNC(NVL(FF."FinishedDate",0) / 100) >  Last2YearsYM  -- 法拍完成日 >  會計2年前月底日
        AND  M."Status" IN (2, 7)    -- 2: 催收戶 7: 部分轉呆戶
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Dp END');
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Dp INS_CNT=' || INS_CNT);


-- 寫入 Work_Ias34DP
    DBMS_OUTPUT.PUT_LINE('INSERT Work_Ias34DP');
    UPD_CNT := 0;

    INSERT INTO "Work_Ias34DP"
    SELECT M."CustNo"                      AS  "CustNo"          -- 戶號
         , M."FacmNo"                      AS  "FacmNo"          -- 額度編號
         , M."BormNo"                      AS  "BormNo"          -- 撥款序號
         , NVL(M1."TotalLoanBal", 0)       AS  "TotalLoanBal"    -- 同額度本金餘額合計
         , NVL(ML."StoreRate",0)           AS  "StoreRate"       -- 減損發生日月底 計息利率
         , NVL(ML."LoanBalance",0)         AS  "LoanBalance"     -- 減損發生日月底 放款餘額
         , NVL(ML."IntAmt",0)              AS  "IntAmt"          -- 減損發生日月底 應收利息
         , CASE WHEN NVL(M1."TotalLoanBal", 0) = 0 THEN 0
                ELSE ROUND((NVL(MF."FireFee",0) + NVL(MF."LawFee",0)) *
                            NVL(ML."LoanBalance",0) / M1."TotalLoanBal", 0)
           END                             AS  "Fee"             -- 減損發生日月底 費用 (火險+法務)
         , CASE WHEN ML."LoanBalance" IS NULL OR ML1."LoanBalance" IS NULL THEN 0
                WHEN ML."LoanBalance" <  ML1."LoanBalance"                 THEN 0
                ELSE ML."LoanBalance" -  ML1."LoanBalance"
           END                             AS  "DerY1Amt"        -- 個案減損客觀證據發生後第一年本金回收金額
         , CASE WHEN ML1."LoanBalance" IS NULL OR ML2."LoanBalance" IS NULL THEN 0
                WHEN ML1."LoanBalance" <  ML2."LoanBalance"                 THEN 0
                ELSE ML1."LoanBalance" -  ML2."LoanBalance"
           END                             AS  "DerY2Amt"        -- 個案減損客觀證據發生後第二年本金回收金額
         , CASE WHEN ML2."LoanBalance" IS NULL OR ML3."LoanBalance" IS NULL THEN 0
                WHEN ML2."LoanBalance" <  ML3."LoanBalance"                 THEN 0
                ELSE ML2."LoanBalance" -  ML3."LoanBalance"
           END                             AS  "DerY3Amt"        -- 個案減損客觀證據發生後第三年本金回收金額
         , CASE WHEN ML3."LoanBalance" IS NULL OR ML4."LoanBalance" IS NULL THEN 0
                WHEN ML3."LoanBalance" <  ML4."LoanBalance"                 THEN 0
                ELSE ML3."LoanBalance" -  ML4."LoanBalance"
           END                             AS  "DerY4Amt"        -- 個案減損客觀證據發生後第四年本金回收金額
         , CASE WHEN ML4."LoanBalance" IS NULL OR ML5."LoanBalance" IS NULL THEN 0
                WHEN ML4."LoanBalance" <  ML5."LoanBalance"                 THEN 0
                ELSE ML4."LoanBalance" -  ML5."LoanBalance"
           END                             AS  "DerY5Amt"        -- 個案減損客觀證據發生後第五年本金回收金額
         , NVL(INT1."IntAmtRcv",0)         AS  "DerY1Int"        -- 個案減損客觀證據發生後第一年應收利息回收金額
         , NVL(INT2."IntAmtRcv",0)         AS  "DerY2Int"        -- 個案減損客觀證據發生後第二年應收利息回收金額
         , NVL(INT3."IntAmtRcv",0)         AS  "DerY3Int"        -- 個案減損客觀證據發生後第三年應收利息回收金額
         , NVL(INT4."IntAmtRcv",0)         AS  "DerY4Int"        -- 個案減損客觀證據發生後第四年應收利息回收金額
         , NVL(INT5."IntAmtRcv",0)         AS  "DerY5Int"        -- 個案減損客觀證據發生後第五年應收利息回收金額
         , CASE WHEN NVL(M1."TotalLoanBal", 0) = 0 THEN 0
                ELSE ROUND((NVL(FEE1."FireFee",0) + NVL(FEE1."LawFee",0)) *
                            NVL(ML."LoanBalance",0) / M1."TotalLoanBal", 0)
           END                             AS  "DerY1Fee"        -- 個案減損客觀證據發生後第一年法拍及火險費用回收金額
         , CASE WHEN NVL(M1."TotalLoanBal", 0) = 0 THEN 0
                ELSE ROUND((NVL(FEE2."FireFee",0) + NVL(FEE2."LawFee",0)) *
                            NVL(ML."LoanBalance",0) / M1."TotalLoanBal", 0)
           END                             AS  "DerY2Fee"        -- 個案減損客觀證據發生後第二年法拍及火險費用回收金額
         , CASE WHEN NVL(M1."TotalLoanBal", 0) = 0 THEN 0
                ELSE ROUND((NVL(FEE3."FireFee",0) + NVL(FEE3."LawFee",0)) *
                            NVL(ML."LoanBalance",0) / M1."TotalLoanBal", 0)
           END                             AS  "DerY3Fee"        -- 個案減損客觀證據發生後第三年法拍及火險費用回收金額
         , CASE WHEN NVL(M1."TotalLoanBal", 0) = 0 THEN 0
                ELSE ROUND((NVL(FEE4."FireFee",0) + NVL(FEE4."LawFee",0)) *
                            NVL(ML."LoanBalance",0) / M1."TotalLoanBal", 0)
           END                             AS  "DerY4Fee"        -- 個案減損客觀證據發生後第四年法拍及火險費用回收金額
         , CASE WHEN NVL(M1."TotalLoanBal", 0) = 0 THEN 0
                ELSE ROUND((NVL(FEE5."FireFee",0) + NVL(FEE5."LawFee",0)) *
                            NVL(ML."LoanBalance",0) / M1."TotalLoanBal", 0)
           END                             AS  "DerY5Fee"        -- 個案減損客觀證據發生後第五年法拍及火險費用回收金額
    FROM   "Ias34Dp" M
      -- 同額度本金餘額合計
      LEFT JOIN ( SELECT M."DataYM"                 AS "DataYM"
                       , M."CustNo"                 AS "CustNo"
                       , M."FacmNo"                 AS "FacmNo"
                       , SUM(M."LoanBal")           AS "TotalLoanBal"
                  FROM     "Ias34Dp" M
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
      -- 減損發生日第一年實收利息 (放款)
      LEFT JOIN ( SELECT M."CustNo"                    AS  "CustNo"
                       , M."FacmNo"                    AS  "FacmNo"
                       , M."BormNo"                    AS  "BormNo"
                       , SUM(NVL(ML."IntAmtRcv",0))    AS  "IntAmtRcv"
                  FROM   "Ias34Dp" M
                    LEFT JOIN "MonthlyLoanBal" ML
                           ON ML."YearMonth"   >= TRUNC(M."DerDate" / 100)
                          AND ML."YearMonth"   <  TRUNC(M."DerDate" / 100) + 100
                          AND ML."CustNo"      =  M."CustNo"
                          AND ML."FacmNo"      =  M."FacmNo"
                          AND ML."BormNo"      =  M."BormNo"
                  WHERE  M."DataYM"      = YYYYMM
                  GROUP BY  M."CustNo", M."FacmNo", M."BormNo"
                ) INT1  ON  INT1."CustNo"     = M."CustNo"
                       AND  INT1."FacmNo"     = M."FacmNo"
                       AND  INT1."BormNo"     = M."BormNo"
      -- 減損發生日第二年實收利息 (放款)
      LEFT JOIN ( SELECT M."CustNo"                    AS  "CustNo"
                       , M."FacmNo"                    AS  "FacmNo"
                       , M."BormNo"                    AS  "BormNo"
                       , SUM(NVL(ML."IntAmtRcv",0))    AS  "IntAmtRcv"
                  FROM   "Ias34Dp" M
                    LEFT JOIN "MonthlyLoanBal" ML
                           ON ML."YearMonth"   >= TRUNC(M."DerDate" / 100) + 100
                          AND ML."YearMonth"   <  TRUNC(M."DerDate" / 100) + 200
                          AND ML."CustNo"      =  M."CustNo"
                          AND ML."FacmNo"      =  M."FacmNo"
                          AND ML."BormNo"      =  M."BormNo"
                  WHERE  M."DataYM"      = YYYYMM
                  GROUP BY  M."CustNo", M."FacmNo", M."BormNo"
                ) INT2  ON  INT2."CustNo"     = M."CustNo"
                       AND  INT2."FacmNo"     = M."FacmNo"
                       AND  INT2."BormNo"     = M."BormNo"
      -- 減損發生日第三年實收利息 (放款)
      LEFT JOIN ( SELECT M."CustNo"                    AS  "CustNo"
                       , M."FacmNo"                    AS  "FacmNo"
                       , M."BormNo"                    AS  "BormNo"
                       , SUM(NVL(ML."IntAmtRcv",0))    AS  "IntAmtRcv"
                  FROM   "Ias34Dp" M
                    LEFT JOIN "MonthlyLoanBal" ML
                           ON ML."YearMonth"   >= TRUNC(M."DerDate" / 100) + 200
                          AND ML."YearMonth"   <  TRUNC(M."DerDate" / 100) + 300
                          AND ML."CustNo"      =  M."CustNo"
                          AND ML."FacmNo"      =  M."FacmNo"
                          AND ML."BormNo"      =  M."BormNo"
                  WHERE  M."DataYM"      = YYYYMM
                  GROUP BY  M."CustNo", M."FacmNo", M."BormNo"
                ) INT3  ON  INT3."CustNo"     = M."CustNo"
                       AND  INT3."FacmNo"     = M."FacmNo"
                       AND  INT3."BormNo"     = M."BormNo"
      -- 減損發生日第四年實收利息 (放款)
      LEFT JOIN ( SELECT M."CustNo"                    AS  "CustNo"
                       , M."FacmNo"                    AS  "FacmNo"
                       , M."BormNo"                    AS  "BormNo"
                       , SUM(NVL(ML."IntAmtRcv",0))    AS  "IntAmtRcv"
                  FROM   "Ias34Dp" M
                    LEFT JOIN "MonthlyLoanBal" ML
                           ON ML."YearMonth"   >= TRUNC(M."DerDate" / 100) + 300
                          AND ML."YearMonth"   <  TRUNC(M."DerDate" / 100) + 400
                          AND ML."CustNo"      =  M."CustNo"
                          AND ML."FacmNo"      =  M."FacmNo"
                          AND ML."BormNo"      =  M."BormNo"
                  WHERE  M."DataYM"      = YYYYMM
                  GROUP BY  M."CustNo", M."FacmNo", M."BormNo"
                ) INT4  ON  INT4."CustNo"     = M."CustNo"
                       AND  INT4."FacmNo"     = M."FacmNo"
                       AND  INT4."BormNo"     = M."BormNo"
      -- 減損發生日第五年實收利息 (放款)
      LEFT JOIN ( SELECT M."CustNo"                    AS  "CustNo"
                       , M."FacmNo"                    AS  "FacmNo"
                       , M."BormNo"                    AS  "BormNo"
                       , SUM(NVL(ML."IntAmtRcv",0))    AS  "IntAmtRcv"
                  FROM   "Ias34Dp" M
                    LEFT JOIN "MonthlyLoanBal" ML
                           ON ML."YearMonth"   >= TRUNC(M."DerDate" / 100) + 400
                          AND ML."YearMonth"   <  TRUNC(M."DerDate" / 100) + 500
                          AND ML."CustNo"      =  M."CustNo"
                          AND ML."FacmNo"      =  M."FacmNo"
                          AND ML."BormNo"      =  M."BormNo"
                  WHERE  M."DataYM"      = YYYYMM
                  GROUP BY  M."CustNo", M."FacmNo", M."BormNo"
                ) INT5  ON  INT5."CustNo"     = M."CustNo"
                       AND  INT5."FacmNo"     = M."FacmNo"
                       AND  INT5."BormNo"     = M."BormNo"
      -- 減損發生日第一年法拍及火險費用回收金額 (額度)
      LEFT JOIN ( SELECT M."CustNo"                 AS  "CustNo"
                       , M."FacmNo"                 AS  "FacmNo"
                       , SUM(NVL(MF."FireFee",0))   AS  "FireFee"
                       , SUM(NVL(MF."LawFee",0))    AS  "LawFee"
                  FROM   "Ias34Dp" M
                    LEFT JOIN "MonthlyFacBal" MF
                           ON MF."YearMonth"  >= TRUNC(M."DerDate" / 100)
                          AND MF."YearMonth"  <  TRUNC(M."DerDate" / 100) + 100
                          AND MF."CustNo"     =  M."CustNo"
                          AND MF."FacmNo"     =  M."FacmNo"
                  WHERE  M."DataYM"      = YYYYMM
                  GROUP BY  M."CustNo", M."FacmNo"
                ) FEE1  ON  FEE1."CustNo"     = M."CustNo"
                       AND  FEE1."FacmNo"     = M."FacmNo"
      -- 減損發生日第二年法拍及火險費用回收金額 (額度)
      LEFT JOIN ( SELECT M."CustNo"                 AS  "CustNo"
                       , M."FacmNo"                 AS  "FacmNo"
                       , SUM(NVL(MF."FireFee",0))   AS  "FireFee"
                       , SUM(NVL(MF."LawFee",0))    AS  "LawFee"
                  FROM   "Ias34Dp" M
                    LEFT JOIN "MonthlyFacBal" MF
                           ON MF."YearMonth"  >= TRUNC(M."DerDate" / 100) + 100
                          AND MF."YearMonth"  <  TRUNC(M."DerDate" / 100) + 200
                          AND MF."CustNo"     =  M."CustNo"
                          AND MF."FacmNo"     =  M."FacmNo"
                  WHERE  M."DataYM"      = YYYYMM
                  GROUP BY  M."CustNo", M."FacmNo"
                ) FEE2  ON  FEE2."CustNo"     = M."CustNo"
                       AND  FEE2."FacmNo"     = M."FacmNo"
      -- 減損發生日第三年法拍及火險費用回收金額 (額度)
      LEFT JOIN ( SELECT M."CustNo"                 AS  "CustNo"
                       , M."FacmNo"                 AS  "FacmNo"
                       , SUM(NVL(MF."FireFee",0))   AS  "FireFee"
                       , SUM(NVL(MF."LawFee",0))    AS  "LawFee"
                  FROM   "Ias34Dp" M
                    LEFT JOIN "MonthlyFacBal" MF
                           ON MF."YearMonth"  >= TRUNC(M."DerDate" / 100) + 200
                          AND MF."YearMonth"  <  TRUNC(M."DerDate" / 100) + 300
                          AND MF."CustNo"     =  M."CustNo"
                          AND MF."FacmNo"     =  M."FacmNo"
                  WHERE  M."DataYM"      = YYYYMM
                  GROUP BY  M."CustNo", M."FacmNo"
                ) FEE3  ON  FEE3."CustNo"     = M."CustNo"
                       AND  FEE3."FacmNo"     = M."FacmNo"
      -- 減損發生日第四年法拍及火險費用回收金額 (額度)
      LEFT JOIN ( SELECT M."CustNo"                 AS  "CustNo"
                       , M."FacmNo"                 AS  "FacmNo"
                       , SUM(NVL(MF."FireFee",0))   AS  "FireFee"
                       , SUM(NVL(MF."LawFee",0))    AS  "LawFee"
                  FROM   "Ias34Dp" M
                    LEFT JOIN "MonthlyFacBal" MF
                           ON MF."YearMonth"  >= TRUNC(M."DerDate" / 100) + 300
                          AND MF."YearMonth"  <  TRUNC(M."DerDate" / 100) + 400
                          AND MF."CustNo"     =  M."CustNo"
                          AND MF."FacmNo"     =  M."FacmNo"
                  WHERE  M."DataYM"      = YYYYMM
                  GROUP BY  M."CustNo", M."FacmNo"
                ) FEE4  ON  FEE4."CustNo"     = M."CustNo"
                       AND  FEE4."FacmNo"     = M."FacmNo"
      -- 減損發生日第五年法拍及火險費用回收金額 (額度)
      LEFT JOIN ( SELECT M."CustNo"                 AS  "CustNo"
                       , M."FacmNo"                 AS  "FacmNo"
                       , SUM(NVL(MF."FireFee",0))   AS  "FireFee"
                       , SUM(NVL(MF."LawFee",0))    AS  "LawFee"
                  FROM   "Ias34Dp" M
                    LEFT JOIN "MonthlyFacBal" MF
                           ON MF."YearMonth"  >= TRUNC(M."DerDate" / 100) + 400
                          AND MF."YearMonth"  <  TRUNC(M."DerDate" / 100) + 500
                          AND MF."CustNo"     =  M."CustNo"
                          AND MF."FacmNo"     =  M."FacmNo"
                  WHERE  M."DataYM"      = YYYYMM
                  GROUP BY  M."CustNo", M."FacmNo"
                ) FEE5  ON  FEE5."CustNo"     = M."CustNo"
                       AND  FEE5."FacmNo"     = M."FacmNo"
    WHERE    M."DataYM"          =  YYYYMM
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Work_Ias34DP END: UPD_CNT=' || UPD_CNT);


-- 更新  減損發生日時之月底各放款資料
    DBMS_OUTPUT.PUT_LINE('UPDATE 減損發生日時之月底各資料');
    UPD_CNT := 0;

    MERGE INTO "Ias34Dp" M
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
            FROM  "Work_Ias34DP" W
          ) T
    ON (    M."DataYM"   = YYYYMM
        AND M."CustNo"   = T."CustNo"
        AND M."FacmNo"   = T."FacmNo"
        AND M."BormNo"   = T."BormNo"
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

--    , ' '    AS "AcCode"             -- 會計科目
--    , 0      AS "Status"             -- 案件狀態
--    , 0      AS "FirstDrawdownDate"  -- 初貸日期
--    , 0      AS "DrawdownDate"       -- 貸放日期
--    , 0      AS "MaturityDate"       -- 到期日
--    , 0      AS "LineAmt"            -- 核准金額         --每額度編號項下之放款帳號皆同
--    , 0      AS "DrawdownAmt"        -- 撥款金額
--    , 0      AS "LoanBal"            -- 本金餘額(撥款)
--    , 0      AS "IntAmt"             -- 應收利息
--    , 0      AS "Fee"                -- 法拍及火險費用
--    , 0      AS "OvduDays"           -- 逾期繳款天數
--    , 0      AS "OvduDate"           -- 轉催收款日期
--    , 0      AS "BadDebtDate"        -- 轉銷呆帳日期     --最早之轉銷呆帳日期
--    , 0      AS "BadDebtAmt"         -- 轉銷呆帳金額
--    , 0      AS "DerDate"            -- 個案減損客觀證據發生日期
--    , 0      AS "DerRate"            -- 上述發生日期前之最近一次利率
--    , 0      AS "DerLoanBal"         -- 上述發生日期時之本金餘額
--    , 0      AS "DerIntAmt"          -- 上述發生日期時之應收利息
--    , 0      AS "DerFee"             -- 上述發生日期時之法拍及火險費用
--    , 0      AS "DerY1Amt"           -- 個案減損客觀證據發生後第一年本金回收金額
--    , 0      AS "DerY2Amt"           -- 個案減損客觀證據發生後第二年本金回收金額
--    , 0      AS "DerY3Amt"           -- 個案減損客觀證據發生後第三年本金回收金額
--    , 0      AS "DerY4Amt"           -- 個案減損客觀證據發生後第四年本金回收金額
--    , 0      AS "DerY5Amt"           -- 個案減損客觀證據發生後第五年本金回收金額
--    , 0      AS "DerY1Int"           -- 個案減損客觀證據發生後第一年應收利息回收金額
--    , 0      AS "DerY2Int"           -- 個案減損客觀證據發生後第二年應收利息回收金額
--    , 0      AS "DerY3Int"           -- 個案減損客觀證據發生後第三年應收利息回收金額
--    , 0      AS "DerY4Int"           -- 個案減損客觀證據發生後第四年應收利息回收金額
--    , 0      AS "DerY5Int"           -- 個案減損客觀證據發生後第五年應收利息回收金額
--    , 0      AS "DerY1Fee"           -- 個案減損客觀證據發生後第一年法拍及火險費用回收金額
--    , 0      AS "DerY2Fee"           -- 個案減損客觀證據發生後第二年法拍及火險費用回收金額
--    , 0      AS "DerY3Fee"           -- 個案減損客觀證據發生後第三年法拍及火險費用回收金額
--    , 0      AS "DerY4Fee"           -- 個案減損客觀證據發生後第四年法拍及火險費用回收金額
--    , 0      AS "DerY5Fee"           -- 個案減損客觀證據發生後第五年法拍及火險費用回收金額
--    , ' '    AS "IndustryCode"       -- 授信行業別
--    , ' '    AS "ClTypeJCIC"         -- 擔保品類別
--    , ' '    AS "Zip3"               -- 擔保品地區別
--    , ' '    AS "ProdCode"           -- 商品利率代碼
--    , 0      AS "CustKind"           -- 企業戶/個人戶  -- 1=企業戶；2=個人戶
--    , ' '    AS "Ifrs9ProdCode"      -- 產品別


-- Work_Ias34DP
--    , 0      AS  "TotalLoanBal"    -- 同額度本金餘額合計
--    , 0      AS  "StoreRate"       -- 減損發生日月底 計息利率
--    , 0      AS  "LoanBalance"     -- 減損發生日月底 放款餘額
--    , 0      AS  "IntAmt"          -- 減損發生日月底 應收利息
--    , 0      AS  "Fee"             -- 減損發生日月底 費用 (火險+法務)
--    , 0      AS  "DerY1Amt"        -- 個案減損客觀證據發生後第一年本金回收金額
--    , 0      AS  "DerY2Amt"        -- 個案減損客觀證據發生後第二年本金回收金額
--    , 0      AS  "DerY3Amt"        -- 個案減損客觀證據發生後第三年本金回收金額
--    , 0      AS  "DerY4Amt"        -- 個案減損客觀證據發生後第四年本金回收金額
--    , 0      AS  "DerY5Amt"        -- 個案減損客觀證據發生後第五年本金回收金額
--    , 0      AS  "DerY1Int"        -- 個案減損客觀證據發生後第一年應收利息回收金額
--    , 0      AS  "DerY2Int"        -- 個案減損客觀證據發生後第二年應收利息回收金額
--    , 0      AS  "DerY3Int"        -- 個案減損客觀證據發生後第三年應收利息回收金額
--    , 0      AS  "DerY4Int"        -- 個案減損客觀證據發生後第四年應收利息回收金額
--    , 0      AS  "DerY5Int"        -- 個案減損客觀證據發生後第五年應收利息回收金額
--    , 0      AS  "DerY1Fee"        -- 個案減損客觀證據發生後第一年法拍及火險費用回收金額
--    , 0      AS  "DerY2Fee"        -- 個案減損客觀證據發生後第二年法拍及火險費用回收金額
--    , 0      AS  "DerY3Fee"        -- 個案減損客觀證據發生後第三年法拍及火險費用回收金額
--    , 0      AS  "DerY4Fee"        -- 個案減損客觀證據發生後第四年法拍及火險費用回收金額
--    , 0      AS  "DerY5Fee"        -- 個案減損客觀證據發生後第五年法拍及火險費用回收金額
