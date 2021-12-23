CREATE OR REPLACE PROCEDURE "Usp_L7_LoanIfrs9Ap_Upd"
(
-- 程式功能：維護 LoanIfrs9Ap 每月IFRS9欄位清單A檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_LoanIfrs9Ap_Upd"(20201231,'System',0);
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
    DBMS_OUTPUT.PUT_LINE('DELETE LoanIfrs9Ap');

    DELETE FROM "LoanIfrs9Ap"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Ap');
    INS_CNT := 0;

    INSERT INTO "LoanIfrs9Ap"
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
         , CASE WHEN M."Status" IN (2, 7) THEN 2
                ELSE 1
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
                ELSE NVL(F."LawFee",  0) * NVL(M."LoanBal", 0) / NVL(F."TotalLoanBal", 0)
                   + NVL(F."FireFee", 0) * NVL(M."LoanBal", 0) / NVL(F."TotalLoanBal", 0)
           END                                       AS "Fee"               -- 法拍及火險費用
         , ROUND(NVL(M."Rate", 0) / 100, 6)          AS "Rate"              -- 利率(撥款)        --抓取月底時適用利率
         , NVL(M."OvduDays", 0)                      AS "OvduDays"          -- 逾期繳款天數      --抓取月底日資料，並以天數表示
         , NVL(M."OvduDate", 0)                      AS "OvduDate"          -- 轉催收款日期
         , NVL(M."BadDebtDate", 0)                   AS "BadDebtDate"       -- 轉銷呆帳日期
         , NVL(M."BadDebtAmt", 0)                    AS "BadDebtAmt"        -- 轉銷呆帳金額
         , NVL(F."GracePeriod", 0)                   AS "GracePeriod"       -- 初貸時約定還本寬限期
         , ROUND(NVL(M."ApproveRate", 0) / 100, 6)   AS "ApproveRate"       -- 核准利率
--         , to_number(NVL(M."AmortizedCode", 0))      AS "AmortizedCode"     -- 契約當時還款方式  -- 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本
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
--         , NVL(M."RepayFreq", 0)                     AS "RepayFreq"         -- 契約約定當時還本週期
         , CASE WHEN NVL(M."AmortizedCode", '0') IN ('1', '2') THEN 0  -- 到期還本
                ELSE NVL(M."RepayFreq", 0)
           END                                       AS "RepayFreq"         -- 契約約定當時還本週期
--         , NVL(M."PayIntFreq", 0)                    AS "PayIntFreq"        -- 契約約定當時繳息週期
         , CASE WHEN NVL(M."AmortizedCode",'0') = '2' THEN 0  -- 到期繳息還本
                ELSE NVL(M."PayIntFreq",0)
           END                                       AS "PayIntFreq"        -- 契約約定當時繳息週期
         , NVL(F."IndustryCode", ' ')                AS "IndustryCode"      -- 授信行業別
         , NVL(F."ClTypeJCIC", ' ')                  AS "ClTypeJCIC"        -- 擔保品類別
--         , NVL("CdCity"."JcicCityCode", ' ')         AS "CityCode"          -- 擔保品地區別
         , CASE 
             WHEN NVL(F."CityCode", ' ') = '05' THEN 'A'
             WHEN NVL(F."CityCode", ' ') = '10' THEN 'B' 
             WHEN NVL(F."CityCode", ' ') = '15' THEN 'C' 
             WHEN NVL(F."CityCode", ' ') = '35' THEN 'D' 
             WHEN NVL(F."CityCode", ' ') = '65' THEN 'E' 
             WHEN NVL(F."CityCode", ' ') = '70' THEN 'F' 
             ELSE 'G'
           END                                       AS "CityCode"          -- 擔保品地區別 A~G
         , NVL(F."ProdNo", ' ')                      AS "ProdNo"            -- 商品利率代碼
         , CASE
             WHEN F."EntCode" IN ('1','2') THEN 1  -- 企金
             ELSE 2
           END                                       AS "CustKind"          -- 企業戶/個人戶 (1=企業戶,企金自然人 2=個人戶)
         , NVL(F."AssetClass", 0)                    AS "AssetClass"        -- 五類資產分類
         , NVL(F."Ifrs9ProdCode", ' ')               AS "Ifrs9ProdCode"     -- 產品別
         , NVL(F."EvaAmt", 0)                        AS "EvaAmt"            -- 原始鑑價金額
         , NVL(M."FirstDueDate", 0)                  AS "FirstDueDate"      -- 首次應繳日
         , NVL(M."TotalPeriod", 0)                   AS "TotalPeriod"       -- 總期數
         , CASE WHEN NVL(F."LineAmt", 0) < NVL(F."UtilBal", 0) THEN 0
                ELSE NVL(F."LineAmt", 0) - NVL(F."UtilBal", 0)
           END                                       AS "AvblBal"           -- 可動用餘額(台幣)
         , NVL(F."RecycleCode", 0)                   AS "RecycleCode"       -- 該筆額度是否可循環動用 (0=非循環動用 1=循環動用)
         , NVL(F."IrrevocableFlag", 0)               AS "IrrevocableFlag"   -- 該筆額度是否為不可徹銷 (1=是 0=否)
         , NVL(F."TempAmt", 0)                       AS "TempAmt"           -- 暫收款金額(台幣)
         , 1                                         AS "AcCurcd"           -- 記帳幣別 (1=台幣)
         , NVL(M."AcBookCode", ' ')                  AS "AcBookCode"        -- 會計帳冊 (1=一般 2=分紅 3=利變 4=OIU)
         , 'NTD'                                     AS "CurrencyCode"      -- 交易幣別
         , 1                                         AS "ExchangeRate"      -- 報導日匯率
         , 0                                         AS "LineAmtCurr"       -- 核准金額(交易幣)
         , 0                                         AS "DrawdownAmtCurr"   -- 撥款金額(交易幣)
         , 0                                         AS "AcctFeeCurr"       -- 帳管費(交易幣)
         , 0                                         AS "LoanBalCurr"       -- 本金餘額(撥款)(交易幣)
         , 0                                         AS "IntAmtCurr"        -- 應收利息(交易幣)
         , 0                                         AS "FeeCurr"           -- 法拍及火險費用(交易幣)
         , 0                                         AS "AvblBalCurr"       -- 可動用餘額(交易幣)
         , 0                                         AS "TempAmtCurr"       -- 暫收款金額(交易幣)
         , JOB_START_TIME                            AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                     AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                            AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                     AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Ifrs9LoanData" M
      LEFT JOIN "Ifrs9FacData" F  ON F."DataYM"      =  M."DataYM"
                                 AND F."CustNo"      =  M."CustNo"
                                 AND F."FacmNo"      =  M."FacmNo"
      LEFT JOIN "CdCity" ON to_number("CdCity"."CityCode") = to_number(NVL(trim(F."CityCode"), 0))
    WHERE  M."DataYM"  =  YYYYMM
      AND  TRUNC(NVL(M."DrawdownDate", 0) / 100 ) <= YYYYMM
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Ap END');
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Ap INS_CNT=' || INS_CNT);


-- 更新 Curr [交易幣]金額
    DBMS_OUTPUT.PUT_LINE('UPDATE Curr [交易幣]金額');
    UPD_CNT := 0;

    UPDATE "LoanIfrs9Ap" M
    SET   M."LineAmtCurr"      =  M."LineAmt"        -- 核准金額(交易幣)
        , M."DrawdownAmtCurr"  =  M."DrawdownAmt"    -- 撥款金額(交易幣)
        , M."AcctFeeCurr"      =  M."AcctFee"        -- 帳管費(交易幣)
        , M."LoanBalCurr"      =  M."LoanBal"        -- 本金餘額(撥款)(交易幣)
        , M."IntAmtCurr"       =  M."IntAmt"         -- 應收利息(交易幣)
        , M."FeeCurr"          =  M."Fee"            -- 法拍及火險費用(交易幣)
        , M."AvblBalCurr"      =  M."AvblBal"        -- 可動用餘額(交易幣)
        , M."TempAmtCurr"      =  M."TempAmt"        -- 暫收款金額(交易幣)
    WHERE M."DataYM"  =  YYYYMM
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE Curr [交易幣]金額 END');


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;