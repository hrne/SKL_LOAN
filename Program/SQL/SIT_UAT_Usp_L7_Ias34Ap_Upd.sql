-- 程式功能：維護 Ias34Ap 每月IAS34資料欄位清單A檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias34Ap_Upd"(20201231,'System',0);
--

CREATE OR REPLACE PROCEDURE "Usp_L7_Ias34Ap_Upd"
(
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
         , NVL(F."LineAmt",0)                        AS "LineAmt"           -- 核准金額
         , NVL(M."DrawdownAmt", 0)                   AS "DrawdownAmt"       -- 撥款金額
         , NVL(F."AcctFee", 0)                       AS "AcctFee"           -- 帳管費
         , NVL(M."LoanBal", 0)                       AS "LoanBal"           -- 本金餘額(撥款)
         , CASE WHEN M."Status" IN (0) THEN NVL(M."IntAmt", 0)
                ELSE 0
           END                                       AS "IntAmt"            -- 應收利息          --計算至每月月底之撥款應收利息
         , CASE WHEN (NVL(F."LawFee", 0) + NVL(F."FireFee", 0)) = 0 THEN 0
                WHEN NVL(M."LoanBal", 0) = 0                        THEN 0
                WHEN NVL(F."TotalLoanBal", 0) = 0                   THEN 0
                ELSE ROUND((NVL(F."LawFee", 0) + NVL(F."FireFee", 0)) * (NVL(M."LoanBal", 0) / NVL(F."TotalLoanBal", 0)), 0)
           END                                       AS "Fee"               -- 法拍及火險費用
         , ROUND(NVL(M."Rate", 0) / 100, 6)          AS "Rate"              -- 利率(撥款)        --抓取月底時適用利率
         , NVL(M."OvduDays", 0)                      AS "OvduDays"          -- 逾期繳款天數      --抓取月底日資料，並以天數表示
         , NVL(M."OvduDate", 0)                      AS "OvduDate"          -- 轉催收款日期
         , NVL(M."BadDebtDate", 0)                   AS "BadDebtDate"       -- 轉銷呆帳日期
         , NVL(M."BadDebtAmt", 0)                    AS "BadDebtAmt"        -- 轉銷呆帳金額
         , CASE WHEN LOS."MarkCode" IS NOT NULL  THEN LOS."MarkCode"                     -- 符合特殊客觀減損狀況檔
                WHEN NVL(F."AgreementFg",' ') = 'Y' AND F."EntCode" IN ('1')     THEN 3  -- 協議件 法人
                WHEN NVL(F."AgreementFg",' ') = 'Y' AND F."EntCode" NOT IN ('1') THEN 4  -- 協議件 自然人
                WHEN M."Status" NOT IN (0)          AND F."EntCode" IN ('1')     THEN 3  -- 非正常戶 法人
                WHEN M."Status" NOT IN (0)          AND F."EntCode" NOT IN ('1') THEN 4  -- 非正常戶 自然人
                WHEN NVL(M."OvduDays", 0) >= 90     AND F."EntCode" IN ('1')     THEN 1  -- 逾期>=90 法人
                WHEN NVL(M."OvduDays", 0) >= 90     AND F."EntCode" NOT IN ('1') THEN 2  -- 逾期>=90 自然人
                ELSE 0
           END                                       AS "DerCode"           -- 符合減損客觀證據之條件
         , NVL(F."GracePeriod", 0)                   AS "GracePeriod"       -- 初貸時約定還本寬限期
         , ROUND(NVL(M."ApproveRate", 0) / 100, 6)   AS "ApproveRate"       -- 核准利率
         , NVL(M."AmortizedCode", 0)                 AS "AmortizedCode"     -- 契約當時還款方式  -- 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本
         , CASE
             WHEN NVL(F."Ifrs9StepProdCode",' ') = 'B' THEN 4  -- 浮動階梯
             WHEN NVL(F."Ifrs9StepProdCode",' ') = 'A' THEN 3  -- 固定階梯
             WHEN NVL(M."RateCode", '0')  IN ('1','3')    THEN 1  -- 機動
             WHEN NVL(M."RateCode", '0')  = '2'           THEN 2  -- 固定
             ELSE to_number(NVL(M."RateCode", '0'))
           END                                       AS "RateCode"          -- 契約當時利率調整方式（1=機動；2=固定；3=固定階梯；4=浮動階梯）
         , NVL(M."RepayFreq", 0)                     AS "RepayFreq"         -- 契約約定當時還本週期
         , NVL(M."PayIntFreq", 0)                    AS "PayIntFreq"        -- 契約約定當時繳息週期
         , NVL(F."IndustryCode", ' ')                AS "IndustryCode"      -- 授信行業別
         , NVL(F."ClTypeJCIC", ' ')                  AS "ClTypeJCIC"        -- 擔保品類別
         , NVL(F."Zip3", ' ')                        AS "Zip3"              -- 擔保品地區別 (郵遞區號)
         , NVL(F."ProdNo", ' ')                      AS "ProdNo"            -- 商品利率代碼
         , CASE
             WHEN F."EntCode" IN ('1') THEN 1  -- 企金
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


        --  , ' '          AS "CustId"            -- 借款人ID / 統編
        --  , 0            AS "ApplNo"            -- 核准號碼
        --  , ' '          AS "AcCode"            -- 會計科目(8碼)
        --  , 0            AS "Status"            -- 戶況 (1=正常 2=催收)
        --  , 0            AS "FirstDrawdownDate" -- 初貸日期
        --  , 0            AS "DrawdownDate"      -- 撥款日期
        --  , 0            AS "FacLineDate"       -- 到期日(額度)
        --  , 0            AS "MaturityDate"      -- 到期日(撥款)
        --  , 0            AS "LineAmt"           -- 核准金額
        --  , 0            AS "DrawdownAmt"       -- 撥款金額
        --  , 0            AS "AcctFee"           -- 帳管費
        --  , 0            AS "LoanBal"           -- 本金餘額(撥款)
        --  , 0            AS "IntAmt"            -- 應收利息          --計算至每月月底之撥款應收利息
        --  , 0            AS "Fee"               -- 法拍及火險費用
        --  , 0            AS "Rate"              -- 利率(撥款)        --抓取月底時適用利率
        --  , 0            AS "OvduDays"          -- 逾期繳款天數      --抓取月底日資料，並以天數表示
        --  , 0            AS "OvduDate"          -- 轉催收款日期
        --  , 0            AS "BadDebtDate"       -- 轉銷呆帳日期
        --  , 0            AS "BadDebtAmt"        -- 轉銷呆帳金額
        --  , 0            AS "DerCode"           -- 符合減損客觀證據之條件 (後面再處理)
        --  , 0            AS "GracePeriod"       -- 初貸時約定還本寬限期
        --  , 0            AS "ApproveRate"       -- 核准利率
        --  , 0            AS "AmortizedCode"     -- 契約當時還款方式      -- 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本
        --  , 0            AS "RateCode"          -- 契約當時利率調整方式
        --  , 0            AS "RepayFreq"         -- 契約約定當時還本週期
        --  , 0            AS "PayIntFreq"        -- 契約約定當時繳息週期
        --  , ' '          AS "IndustryCode"      -- 授信行業別
        --  , ' '          AS "ClTypeJCIC"        -- 擔保品類別
        --  , ' '          AS "Zip3"              -- 擔保品郵遞區號
        --  , ' '          AS "ProdNo"            -- 商品利率代碼
        --  , 0            AS "CustKind"          -- 企業戶/個人戶
        --  , 0            AS "AssetClass"        -- 五類資產分類
        --  , ' '          AS "Ifrs9ProdCode"     -- 產品別
        --  , 0            AS "EvaAmt"            -- 原始鑑價金額
        --  , 0            AS "FirstDueDate"      -- 首次應繳日
        --  , 0            AS "TotalPeriod"       -- 總期數
        --  , 0            AS "AgreeBefFacmNo"    -- 協議前之額度編號
        --  , 0            AS "AgreeBefBormNo"    -- 協議前之撥款序號
