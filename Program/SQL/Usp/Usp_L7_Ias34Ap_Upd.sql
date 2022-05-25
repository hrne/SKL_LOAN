CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L7_Ias34Ap_Upd"
(
-- 程式功能：維護 Ias34Ap 每月IAS34資料欄位清單A檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias34Ap_Upd"(20211230,'999999',1);
--
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    NewAcFg        IN  INT         -- 0=使用舊會計科目(8碼) 1=使用新會計科目(11碼)
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;         -- 新增筆數
    UPD_CNT        INT;         -- 更新筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間
    YYYYMM         INT;         -- 本月年月
    LYYYYMM        INT;         -- 上月年月
    MM             INT;         -- 本月月份
    YYYY           INT;         -- 本月年度
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


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE Ias34Ap');

    DELETE FROM "Ias34Ap"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Ap');
    INS_CNT := 0;

    INSERT INTO "Ias34Ap"
    WITH Total AS (
      -- 各戶號放款餘額加總
      SELECT "DataYM"                 AS "DataYM"
           , "CustNo"                 AS "CustNo"
           , SUM("LoanBal")           AS "LoanBalTotal"
      FROM "JcicMonthlyLoanData"
      WHERE "DataYM" =  YYYYMM
      GROUP BY "DataYM"
             , "CustNo"
    )
    , FacTotal AS (
      -- 各額度放款餘額加總
      SELECT "DataYM"                 AS "DataYM"
           , "CustNo"                 AS "CustNo"
           , "FacmNo"                 AS "FacmNo"
           , SUM("LoanBal")           AS "LoanBalTotal"
      FROM "JcicMonthlyLoanData"
      WHERE "DataYM" =  YYYYMM
      GROUP BY "DataYM"
             , "CustNo"
             , "FacmNo"
    )
    , Law AS (
      -- 把本月各戶號法拍費用餘額撈出來
      SELECT "CustNo"
           , SUM("Fee") AS "Fee"
      FROM "ForeclosureFee"
      WHERE TRUNC("OpenAcDate" / 100) <= YYYYMM
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
      WHERE 
            CASE
              WHEN "AcDate" = 0 AND "RenewCode" = 2 
                   AND  "InsuYearMonth" <= YYYYMM -- 續保未銷直接計入
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
    , AcctFeeData AS (
      SELECT "CustNo"           -- 戶號
           , "FacmNo"           -- 額度編號
           , "RvNo"             -- 銷帳編號
           , SUM("RvAmt") AS "AcctFee"
      FROM "AcReceivable"
      WHERE "AcctCode" = 'F10'
        AND "RvAmt" > 0
      GROUP BY "CustNo"
             , "FacmNo"
             , "RvNo"
    )
    , rawData AS (
        SELECT FSL."CustNo"
             , FSL."FacmNo"
             , FSL."MainApplNo"
             , FSL."KeyinSeq"
             , FSL."LineAmt" AS "LimitLineAmt"
             , FM."UtilBal"
        FROM "FacShareLimit" FSL
        LEFT JOIN "FacMain" FM ON FM."CustNo" = FSL."CustNo"
                              AND FM."FacmNo" = FSL."FacmNo"
    )
    , maxSeqData AS (
        SELECT "MainApplNo"
             , MAX("KeyinSeq") AS "MaxSeq"
        FROM rawData
        GROUP BY "MainApplNo"
    )
    , lastSeqData AS (
        SELECT r1."MainApplNo"
             , r1."CustNo"
             , r1."FacmNo"
             , r1."LimitLineAmt" - SUM(r2."UtilBal") AS "LimitLineAmt"
        FROM rawData r1
        LEFT JOIN rawData r2 ON r2."MainApplNo" = r1."MainApplNo"
                            AND r2."KeyinSeq" < r1."KeyinSeq"
        LEFT JOIN maxSeqData m ON m."MainApplNo" = r1."MainApplNo"
        WHERE r1."KeyinSeq" = m."MaxSeq" 
        GROUP BY r1."MainApplNo"
               , r1."CustNo"
               , r1."FacmNo"
               , r1."LimitLineAmt"
    )
    , shareFacData AS (
        SELECT r."CustNo"
             , r."FacmNo"
             , MAX(NVL(l."LimitLineAmt",r."UtilBal")) AS "ShareLineAmt" 
        FROM rawData r
        LEFT JOIN lastSeqData l ON l."MainApplNo" = r."MainApplNo"
                               AND l."CustNo" = r."CustNo"
                               AND l."FacmNo" = r."FacmNo"
        GROUP BY r."CustNo"
               , r."FacmNo"
    )
    SELECT
           YYYYMM                                    AS "DataYM"            -- 資料年月
         , M."CustNo"                                AS "CustNo"            -- 戶號
         , NVL(M."CustId",' ')                       AS "CustId"            -- 借款人ID / 統編
         , M."FacmNo"                                AS "FacmNo"            -- 額度編號
         , NVL(F."ApplNo",0)                         AS "ApplNo"            -- 核准號碼
         , M."BormNo"                                AS "BormNo"            -- 撥款序號
         , CASE WHEN NewAcFg = 0 THEN RPAD(NVL(M."AcCodeOld",' '),8,' ') -- 舊
                ELSE                  RPAD(NVL(M."AcCode",' '),11,' ')   -- 新
           END                                       AS "AcCode"            -- 會計科目
         , CASE WHEN M."Status" IN (0) THEN 1
                ELSE 2
           END                                       AS "Status"            -- 戶況 (1=正常 2=催收)
         , NVL(F."FirstDrawdownDate",0)              AS "FirstDrawdownDate" -- 初貸日期
         , NVL(M."DrawdownDate", 0)                  AS "DrawdownDate"      -- 撥款日期
         , NVL(F."MaturityDate",0)                   AS "FacLineDate"       -- 到期日(額度)
         , NVL(M."MaturityDate", 0)                  AS "MaturityDate"      -- 到期日(撥款)
         , NVL(NVL(sfd."ShareLineAmt",F."LineAmt"),0) AS "LineAmt"           -- 核准金額
         , NVL(M."DrawdownAmt", 0)                   AS "DrawdownAmt"       -- 撥款金額
         , NVL(ACF."AcctFee", 0)                     AS "AcctFee"           -- 帳管費
         , NVL(M."LoanBal", 0)                       AS "LoanBal"           -- 本金餘額(撥款)
         , CASE WHEN M."Status" IN (0) THEN NVL(M."IntAmt", 0)
                ELSE 0
           END                                       AS "IntAmt"            -- 應收利息          --計算至每月月底之撥款應收利息
         , NVL(AF."AvgLawFee",0)
           + NVL(AF."AvgInsuFee",0)                  AS "Fee"               -- 法拍及火險費用
         , ROUND(NVL(M."Rate", 0) / 100, 6)          AS "Rate"              -- 利率(撥款)        --抓取月底時適用利率
         , NVL(M."OvduDays", 0)                      AS "OvduDays"          -- 逾期繳款天數      --抓取月底日資料，並以天數表示
         , NVL(M."OvduDate", 0)                      AS "OvduDate"          -- 轉催收款日期
         , NVL(M."BadDebtDate", 0)                   AS "BadDebtDate"       -- 轉銷呆帳日期
         , NVL(M."BadDebtAmt", 0)                    AS "BadDebtAmt"        -- 轉銷呆帳金額
         , CASE WHEN LOS."LosCod" IS NOT NULL  THEN LOS."LosCod"            -- 符合特殊客觀減損狀況檔
--                WHEN NVL(F."AgreementFg",' ') = 'Y' AND F."EntCode" IN ('1')     THEN 3  -- 協議件 法人
--                WHEN NVL(F."AgreementFg",' ') = 'Y' AND F."EntCode" NOT IN ('1') THEN 4  -- 協議件 自然人
                WHEN NVL(F."ProdNo",' ') IN '60'    AND F."EntCode" IN ('1')     THEN 3  -- 協議件 法人
                WHEN NVL(F."ProdNo",' ') IN '60'    AND F."EntCode" NOT IN ('1') THEN 4  -- 協議件 自然人
                WHEN M."Status" NOT IN (0)          AND F."EntCode" IN ('1')     THEN 3  -- 非正常戶 法人
                WHEN M."Status" NOT IN (0)          AND F."EntCode" NOT IN ('1') THEN 4  -- 非正常戶 自然人
                WHEN NVL(M."OvduDays", 0) >= 90     AND F."EntCode" IN ('1')     THEN 1  -- 逾期>=90 法人
                WHEN NVL(M."OvduDays", 0) >= 90     AND F."EntCode" NOT IN ('1') THEN 2  -- 逾期>=90 自然人
                ELSE 0
           END                                       AS "DerCode"           -- 符合減損客觀證據之條件
         , CASE 
             WHEN NVL(LM."GraceDate", 0) = 0  THEN 0
--             ELSE TRUNC( MONTHS_BETWEEN(
--                                  TO_DATE(NVL(LM."GraceDate", 0),'yyyymmdd'),
--                                  TO_DATE(NVL(LM."DrawdownDate", 0),'yyyymmdd') 
--                                )
             WHEN ( MOD( NVL(LM."GraceDate", 0) , 100) - MOD( NVL(LM."DrawdownDate", 0) , 100) ) >= 15 THEN
                    ( TRUNC(NVL(LM."GraceDate", 0) / 10000 ) - TRUNC(NVL(LM."DrawdownDate", 0) / 10000 ) ) *12 +         
                    ( MOD( TRUNC(NVL(LM."GraceDate", 0) / 100 ),100) - 
                      MOD( TRUNC(NVL(LM."DrawdownDate", 0) / 100 ),100)   )   + 1
             WHEN ( MOD( NVL(LM."GraceDate", 0) , 100) - MOD( NVL(LM."DrawdownDate", 0) , 100) ) < (-15) THEN
                    ( TRUNC(NVL(LM."GraceDate", 0) / 10000 ) - TRUNC(NVL(LM."DrawdownDate", 0) / 10000 ) ) *12 +         
                    ( MOD( TRUNC(NVL(LM."GraceDate", 0) / 100 ),100) - 
                      MOD( TRUNC(NVL(LM."DrawdownDate", 0) / 100 ),100)   )   - 1
             ELSE
                  ( TRUNC(NVL(LM."GraceDate", 0) / 10000 ) - TRUNC(NVL(LM."DrawdownDate", 0) / 10000 ) ) *12 +         
                  ( MOD( TRUNC(NVL(LM."GraceDate", 0) / 100 ),100) - 
                    MOD( TRUNC(NVL(LM."DrawdownDate", 0) / 100 ),100)   )      
           END                                       AS "GracePeriod"       -- 初貸時約定還本寬限期:寬限到期日至撥款日計算月差
         , ROUND(NVL(M."ApproveRate", 0) / 100, 6)   AS "ApproveRate"       -- 核准利率
         , CASE
             WHEN NVL(M."AmortizedCode",'0') = '1' THEN '1'  -- 1.按月繳息(按期繳息到期還本)
             WHEN NVL(M."AmortizedCode",'0') = '2' THEN '4'  -- 2.到期取息(到期繳息還本)
             WHEN NVL(M."AmortizedCode",'0') = '3' THEN '2'  -- 3.本息平均法(期金)
             WHEN NVL(M."AmortizedCode",'0') = '4' THEN '3'  -- 4.本金平均法
             WHEN NVL(M."AmortizedCode",'0') = '5' THEN '4'  -- 5.按月撥款收息(逆向貸款)  --???
             ELSE '3'
           END                                       AS "AmortizedCode"     -- 契約當時還款方式
         , CASE
             WHEN NVL(F."Ifrs9StepProdCode",' ') = 'B' THEN 4  -- 浮動階梯
             WHEN NVL(F."Ifrs9StepProdCode",' ') = 'A' THEN 3  -- 固定階梯
             WHEN NVL(M."RateCode", '0')  IN ('1','3')    THEN 1  -- 機動
             WHEN NVL(M."RateCode", '0')  = '2'           THEN 2  -- 固定
             ELSE to_number(NVL(M."RateCode", '0'))
           END                                       AS "RateCode"          -- 契約當時利率調整方式（1=機動；2=固定；3=固定階梯；4=浮動階梯）
         , CASE WHEN NVL(M."AmortizedCode", '0') IN ('1', '2') THEN 0  -- 到期還本
                ELSE NVL(M."RepayFreq", 0)
           END                                       AS "RepayFreq"         -- 契約約定當時還本週期
         , CASE WHEN NVL(M."AmortizedCode",'0') = '2' THEN 0  -- 到期繳息還本
                ELSE NVL(M."PayIntFreq",0)
           END                                       AS "PayIntFreq"        -- 契約約定當時繳息週期
         , NVL(F."IndustryCode", ' ')                AS "IndustryCode"      -- 授信行業別
         , NVL(F."ClTypeJCIC", ' ')                  AS "ClTypeJCIC"        -- 擔保品類別
         , NVL(F."Zip3", ' ')                        AS "Zip3"              -- 擔保品地區別 (郵遞區號)
         , NVL(F."ProdNo", ' ')                      AS "ProdNo"            -- 商品利率代碼
         , CASE
             WHEN F."EntCode" IN ('1') THEN 1  -- 企金不含企金自然人BY舜雯
             ELSE 2
           END                                       AS "CustKind"          -- 企業戶/個人戶 (1=企業戶 2=個人戶)
         , NVL(F."AssetClass", 0)                    AS "AssetClass"        -- 五類資產分類
         , NVL(F."Ifrs9ProdCode", ' ')               AS "Ifrs9ProdCode"     -- 產品別
         , NVL(F."EvaAmt", 0)                        AS "EvaAmt"            -- 原始鑑價金額
         , NVL(M."FirstDueDate", 0)                  AS "FirstDueDate"      -- 首次應繳日
         , NVL(M."TotalPeriod", 0)                   AS "TotalPeriod"       -- 總期數
         , NVL(M."AgreeBefFacmNo", 0)                AS "AgreeBefFacmNo"    -- 協議前之額度編號
         , NVL(M."AgreeBefBormNo", 0)                AS "AgreeBefBormNo"    -- 協議前之撥款序號
         , JOB_START_TIME                            AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                     AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                            AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                     AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Ifrs9LoanData" M
      LEFT JOIN "Ifrs9FacData" F  ON F."DataYM"  =  M."DataYM"
                                 AND F."CustNo"  =  M."CustNo"
                                 AND F."FacmNo"  =  M."FacmNo"
      LEFT JOIN "Ias39Loss" LOS   ON LOS."CustNo"  =  M."CustNo"
                                 AND LOS."FacmNo"  =  M."FacmNo"
                                 AND TRUNC(NVL(LOS."StartDate",0) / 100) <= YYYYMM
                                 AND TRUNC(NVL(LOS."EndDate",99991231) / 100) >= YYYYMM
      LEFT JoIN "LoanBorMain" LM  ON LM."CustNo"     =  M."CustNo"
                                 AND LM."FacmNo"     =  M."FacmNo"
                                 AND LM."BormNo"     =  M."BormNo"
      LEFT JOIN AvgFeeDataFinal AF ON AF."DataYM" = M."DataYM"
                                  AND AF."CustNo" = M."CustNo"
                                  AND AF."FacmNo" = M."FacmNo"
                                  AND AF."BormNo" = M."BormNo"
      LEFT JOIN AcctFeeData ACF ON ACF."CustNo" = LPAD(M."CustNo",7,'0')
                               AND ACF."FacmNo" = LPAD(M."FacmNo",3,'0')
                               AND ACF."RvNo"   = LPAD(M."BormNo",3,'0')
      LEFT JOIN shareFacData sfd ON sfd."CustNo" = M."CustNo"
                                AND sfd."FacmNo" = M."FacmNo"
    WHERE  M."DataYM"          =  YYYYMM
      AND  M."Status" IN (0, 2, 7)   -- 正常件, 催收, 部分轉呆
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Ap END');
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Ap INS_CNT=' || INS_CNT);


-- 更新 DerCode 符合減損客觀證據之條件   (ref: LNSP65A-LN6511)
--    DBMS_OUTPUT.PUT_LINE('UPDATE DerCode 符合減損客觀證據之條件');
--    UPD_CNT := 0;
--
--    UPDATE "Ias34Ap" M
--    SET   M."DerCode" =
--            CASE
--              WHEN M."ProdNo" IN ('60','61','62')  AND M."CustKind" IN (2) THEN 4    -- 協議件 自然人
--              WHEN M."ProdNo" IN ('60','61','62')  AND M."CustKind" IN (1) THEN 3    -- 協議件 法人
--              WHEN M."Status" NOT IN (1) AND M."CustKind" IN (2) THEN 4    -- 非正常戶 自然人
--              WHEN M."Status" NOT IN (1) AND M."CustKind" IN (1) THEN 3    -- 非正常戶 法人
--              WHEN M."OvduDays" >= 90    AND M."CustKind" IN (2) THEN 2    -- 逾期>=90 自然人
--              WHEN M."OvduDays" >= 90    AND M."CustKind" IN (1) THEN 1    -- 逾期>=90 法人
--              ELSE 0
--            END
--    WHERE M."DataYM"          =  YYYYMM
--      AND M."DerCode"         =  0
--      ;
--
--    UPD_CNT := UPD_CNT + sql%rowcount;
--    DBMS_OUTPUT.PUT_LINE('UPDATE DerCode 符合減損客觀證據之條件 END');
--    DBMS_OUTPUT.PUT_LINE('UPDATE DerCode UPD_CNT=' || UPD_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;