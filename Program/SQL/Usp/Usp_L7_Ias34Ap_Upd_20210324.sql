-- 程式功能：維護 Ias34Ap 每月IAS34資料欄位清單A檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias34Ap_Upd"(20200423,'999999');
--

CREATE OR REPLACE PROCEDURE "Usp_L7_Ias34Ap_Upd"
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
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

    INSERT INTO "Ias34Ap"
    SELECT
           YYYYMM                                    AS "DataYM"            -- 資料年月
         , M."CustNo"                                AS "CustNo"            -- 戶號
         , M."CustId"                                AS "CustId"            -- 借款人ID / 統編
         , M."FacmNo"                                AS "FacmNo"            -- 額度編號
         , NVL(F."ApplNo",0)                         AS "ApplNo"            -- 核准號碼
         , M."BormNo"                                AS "BormNo"            -- 撥款序號
         , NVL(M."AcNoCode",' ')                     AS "AcCode"            -- 會計科目(8碼)
         , CASE WHEN M."Status" IN (0) THEN '1'      
         	      ELSE '2'                             
         	 END                                       AS "Status"            -- 戶況 (1=正常 2=催收)     
         , NVL(F."FirstDrawdownDate", 0)             AS "FirstDrawdownDate" -- 初貸日期
         , NVL(M."DrawdownDate", 0)                  AS "DrawdownDate"      -- 撥款日期
         , NVL(F."MaturityDate", 0)                  AS "FacLineDate"       -- 到期日(額度)
         , NVL(M."MaturityDate", 0)                  AS "MaturityDate"      -- 到期日(撥款)
         , NVL(F."LineAmt", 0)                       AS "LineAmt"           -- 核准金額
         , NVL(M."DrawdownAmt", 0)                   AS "DrawdownAmt"       -- 撥款金額
         , NVL(F."AcctFee", 0)                       AS "AcctFee"           -- 帳管費
         , NVL(M."LoanBal", 0)                       AS "LoanBal"           -- 本金餘額(撥款)
         , CASE WHEN M."Status" IN (0) THEN TRUNC(NVL(M1."IntAmtAcc", 0) / 100, 4)  
         	      ELSE 0
         	 END                                       AS "IntAmt"            -- 應收利息          --計算至每月月底之撥款應收利息
         , 0       AS "Fee"               -- 法拍及火險費用  ???
         , NVL(M."StoreRate", 0)                     AS "Rate"              -- 利率(撥款)        --抓取月底時適用利率
         , CASE WHEN M."Status" IN (2, 7) 
                     THEN TO_DATE(TBSDYF,'yyyy-mm-dd') - TO_DATE(M."MaturityDate",'yyyy-mm-dd')    -- 先用轉催收日期
         	      ELSE 
         	        CASE WHEN M."NextPayIntDate" >= TBSDYF THEN 0
         	        	   ELSE TO_DATE(TBSDYF,'yyyy-mm-dd') - TO_DATE(M."NextPayIntDate",'yyyy-mm-dd')
         	        END
         	 END                                       AS "OvduDays"          -- 逾期繳款天數      --抓取月底日資料，並以天數表示
         , NVL(M."OvduDate", 0)                      AS "OvduDate"          -- 轉催收款日期
         , NVL(M."BadDebtDate", 0)                   AS "BadDebtDate"       -- 轉銷呆帳日期 
         , NVL(OD."BadDebtAmt", 0)                   AS "BadDebtAmt"        -- 轉銷呆帳金額 
         , 0                                         AS "DerCode"           -- 符合減損客觀證據之條件 (後面再處理)
         , NVL(L."GracePeriod", 0)                   AS "GracePeriod"       -- 初貸時約定還本寬限期
         , TRUNC(NVL(L."ApproveRate", 0) / 100, 4)   AS "ApproveRate"       -- 核准利率
         , CASE
             WHEN L."AmortizedCode" = '1' THEN '1'  -- 1.按月繳息(按期繳息到期還本)
             WHEN L."AmortizedCode" = '2' THEN '4'  -- 2.到期取息(到期繳息還本)
             WHEN L."AmortizedCode" = '3' THEN '2'  -- 3.本息平均法(期金)
             WHEN L."AmortizedCode" = '4' THEN '3'  -- 4.本金平均法
             WHEN L."AmortizedCode" = '5' THEN '4'  -- 5.按月撥款收息(逆向貸款)  --???
             ELSE '3'
           END                                       AS "AmortizedCode"     -- 契約當時還款方式      -- 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本
         , NVL(to_char(M."RateCode"),' ')            AS "RateCode"          -- 契約當時利率調整方式
         , NVL(M."RepayFreq", 0)                     AS "RepayFreq"         -- 契約約定當時還本週期
         , NVL(M."PayIntFreq", 0)                    AS "PayIntFreq"        -- 契約約定當時繳息週期
         , NVL(M."IndustryCode", ' ')                AS "IndustryCode"      -- 授信行業別
         , NVL(M."ClTypeJCIC",' ')                   AS "ClTypeJCIC"        -- 擔保品類別
         , NVL("ClMain"."CityCode",' ')              AS "CityCode"          -- 擔保品地區別
         , NVL("ClMain"."AreaCode",' ')              AS "AreaCode"          -- 擔保品鄉鎮區
         , NVL("CdArea"."Zip3",' ')                  AS "Zip3"              -- 擔保品郵遞區號
         , NVL(F."ProdNo",' ')                       AS "BaseRateCode"      -- 商品利率代碼
         , CASE
             WHEN M."EntCode" IN ('1') THEN 1  -- 企金
             ELSE 2
           END                                       AS "CustKind"          -- 企業戶/個人戶
         , ' '          AS "AssetKind"         -- 五類資產分類   ???
--       , NVL(M."ProdNo"           ,' ')            AS "ProdNo"            -- 產品別
--       , NVL(M."EvaAmt"           ,0)              AS "EvaAmt"            -- 原始鑑價金額
--       , NVL(M."FirstDueDate"     ,0)              AS "FirstDueDate"      -- 首次應繳日
--       , NVL(M."TotalPeriod"      ,0)              AS "TotalPeriod"       -- 總期數
--       , NVL(M."AgreeBefFacmNo"   ,0)              AS "AgreeBefFacmNo"    -- 協議前之額度編號
--       , NVL(M."AgreeBefBormNo"   ,0)              AS "AgreeBefBormNo"    -- 協議前之撥款序號
--       , NVL(M."UtilAmt"          ,0)              AS "UtilAmt"           -- 累計撥款金額(額度)
--       , NVL(M."UtilBal"          ,0)              AS "UtilBal"           -- 已動用餘額(額度)
--       , NVL(to_char(M."RecycleCode"),' ')         AS "RecycleCode"       -- 該筆額度是否可循環動用  -- 0: 非循環動用  1: 循環動用
--       , NVL(to_char(M."IrrevocableFlag"),' ')     AS "IrrevocableFlag"   -- 該筆額度是否為不可徹銷  -- 1=是 0=否
--       , NVL(M."TempAmt"          ,0)              AS "TempAmt"           -- 暫收款金額(台幣)
--       , 1                                         AS "AcCurcd"           -- 記帳幣別                -- 1=台幣 2=美元 3=澳幣 4=人民幣 5=歐元
--       , CASE                                      
--           WHEN M."AcBookCode" = '201' THEN '3'    
--           ELSE '1'                                
--         END                                       AS "AcBookCode"        -- 會計帳冊                -- 1=一般 2=分紅 3=利變 4=OIU
--       , NVL(M."CurrencyCode"     ,' ')            AS "CurrencyCode"      -- 交易幣別                -- TWD
--       , NVL(M."ExchangeRate"     ,0)              AS "ExchangeRate"      -- 報導日匯率
         , JOB_START_TIME                            AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                     AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                            AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                     AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "JcicMonthlyLoanData" M
      LEFT JOIN "FacMain" F     ON F."CustNo"       = M."CustNo"
                               AND F."FacmNo"       = M."FacmNo"
      LEFT JOIN "CdAcCode"      ON "CdAcCode"."AcctCode"  = M."AcctCode"
      LEFT JOIN "LoanBorMain" L  ON L."CustNo"   = M."CustNo"
                                AND L."FacmNo"   = M."FacmNo"
                                AND L."BormNo"   = M."BormNo"
      LEFT JOIN "MonthlyLoanData"  M1   ON M1."CustNo"   = M."CustNo"
                                       AND M1."FacmNo"   = M."FacmNo"
                                       AND M1."BormNo"   = M."BormNo"
      LEFT JOIN "LoanOverdue"  OD       ON OD."CustNo"   = M."CustNo"
                                       AND OD."FacmNo"   = M."FacmNo"
                                       AND OD."BormNo"   = M."BormNo"
      LEFT JOIN "ClMain"      ON "ClMain"."ClCode1"       = M."ClCode1"
                             AND "ClMain"."ClCode2"       = M."ClCode2"
                             AND "ClMain"."ClNo"          = M."ClNo"
      LEFT JOIN "CdCl"        ON "CdCl"."ClCode1"         = M."ClCode1"
                             AND "CdCl"."ClCode2"         = M."ClCode2"
      LEFT JOIN "CdArea"      ON "CdArea"."CityCode"      = "ClMain"."CityCode"
                             AND "CdArea"."AreaCode"      = "ClMain"."AreaCode"
    WHERE  M."DataYM"          =  YYYYMM
      AND  M."Status" IN (0, 2, 7)   -- 正常件, 催收, 部分轉呆




--  INSERT INTO "Ias34Ap"
--  SELECT
--         YYYYMM                                AS "DataYM"            -- 資料年月
--       , NVL(M."CustNo",0)                     AS "CustNo"            -- 戶號
--       , NVL(M."CustId",' ')                   AS "CustId"            -- 借款人ID / 統編
--       , NVL(M."FacmNo",0)                     AS "FacmNo"            -- 額度編號
--       , NVL(M."ApplNo",0)                     AS "ApplNo"            -- 核准號碼
--       , NVL(M."BormNo",0)                     AS "BormNo"            -- 撥款序號
--       , NVL(M."AcNoCode"         ,' ')        AS "AcCode"            -- 會計科目(8碼)
--       , NVL(M."Status"           ,0)          AS "Status"            -- 戶況 (1=正常 2=催收)
--       , NVL(M."FirstDrawdownDate",0)          AS "FirstDrawdownDate" -- 初貸日期
--       , NVL(M."DrawdownDate"     ,0)          AS "DrawdownDate"      -- 撥款日期
--       , NVL(M."FacLineDate"      ,0)          AS "FacLineDate"       -- 到期日(額度)
--       , NVL(M."MaturityDate"     ,0)          AS "MaturityDate"      -- 到期日(撥款)
--       , NVL(M."LineAmt"          ,0)          AS "LineAmt"           -- 核准金額
--       , NVL(M."DrawdownAmt"      ,0)          AS "DrawdownAmt"       -- 撥款金額
--       , NVL(M."AcctFee"          ,0)          AS "AcctFee"           -- 帳管費
--       , NVL(M."LoanBal"          ,0)          AS "LoanBal"           -- 本金餘額(撥款)
--       , NVL(M."IntAmt"           ,0)          AS "IntAmt"            -- 應收利息          --計算至每月月底之撥款應收利息
--       , NVL(M."Fee"              ,0)          AS "Fee"               -- 法拍及火險費用
--       , NVL(M."Rate"             ,0)          AS "Rate"              -- 利率(撥款)        --抓取月底時適用利率
--       , NVL(M."OvduDays"         ,0)          AS "OvduDays"          -- 逾期繳款天數      --抓取月底日資料，並以天數表示
--       , NVL(M."OvduDate"         ,0)          AS "OvduDate"          -- 轉催收款日期      --抓取最近一次的轉催收日期
--       , NVL(M."BadDebtDate"      ,0)          AS "BadDebtDate"       -- 轉銷呆帳日期      --最早之轉銷呆帳日期
--       , NVL(M."BadDebtAmt"       ,0)          AS "BadDebtAmt"        -- 轉銷呆帳金額      --無論轉呆次數，計算全部轉銷呆帳之金額
--       , NVL(M."DerCode"          ,0)          AS "DerCode"           -- 符合減損客觀證據之條件
--       , NVL(M."GracePeriod"      ,0)          AS "GracePeriod"       -- 初貸時約定還本寬限期
--       , NVL(M."ApproveRate"      ,0)          AS "ApproveRate"       -- 核准利率
--       , NVL(to_char(M."AmortizedCode"),' ')   AS "AmortizedCode"     -- 契約當時還款方式
--       , NVL(to_char(M."RateCode"),' ')        AS "RateCode"          -- 契約當時利率調整方式
--       , NVL(M."RepayFreq"        ,0)          AS "RepayFreq"         -- 契約約定當時還本週期
--       , NVL(M."PayIntFreq"       ,0)          AS "PayIntFreq"        -- 契約約定當時繳息週期
--       , NVL(M."IndustryCode"     ,' ')        AS "IndustryCode"      -- 授信行業別
--       , NVL(M."ClTypeJCIC"       ,' ')        AS "ClTypeJCIC"        -- 擔保品類別
--       , NVL(M."CityCode"         ,' ')        AS "CityCode"          -- 擔保品地區別
--       , NVL(M."AreaCode"         ,' ')        AS "AreaCode"          -- 擔保品鄉鎮區
--       , NVL(M."Zip3"             ,' ')        AS "Zip3"              -- 擔保品郵遞區號
--       , NVL(M."BaseRateCode"     ,' ')        AS "BaseRateCode"      -- 商品利率代碼
--       , NVL(M."CustKind"         ,0)          AS "CustKind"          -- 企業戶/個人戶
--       , NVL(M."AssetKind"        ,0)          AS "AssetKind"         -- 五類資產分類
--       , NVL(M."ProdNo"           ,' ')        AS "ProdNo"            -- 產品別
--       , NVL(M."EvaAmt"           ,0)          AS "EvaAmt"            -- 原始鑑價金額
--       , NVL(M."FirstDueDate"     ,0)          AS "FirstDueDate"      -- 首次應繳日
--       , NVL(M."TotalPeriod"      ,0)          AS "TotalPeriod"       -- 總期數
--       , NVL(M."AgreeBefFacmNo"   ,0)          AS "AgreeBefFacmNo"    -- 協議前之額度編號
--       , NVL(M."AgreeBefBormNo"   ,0)          AS "AgreeBefBormNo"    -- 協議前之撥款序號
--       , NVL(M."UtilAmt"          ,0)          AS "UtilAmt"           -- 累計撥款金額(額度)
--       , NVL(M."UtilBal"          ,0)          AS "UtilBal"           -- 已動用餘額(額度)
--       , NVL(to_char(M."RecycleCode"),' ')     AS "RecycleCode"       -- 該筆額度是否可循環動用  -- 0: 非循環動用  1: 循環動用
--       , NVL(to_char(M."IrrevocableFlag"),' ') AS "IrrevocableFlag"   -- 該筆額度是否為不可徹銷  -- 1=是 0=否
--       , NVL(M."TempAmt"          ,0)          AS "TempAmt"           -- 暫收款金額(台幣)
--       , 1                                     AS "AcCurcd"           -- 記帳幣別                -- 1=台幣 2=美元 3=澳幣 4=人民幣 5=歐元
--       , CASE
--           WHEN M."AcBookCode" = '201' THEN '3'
--           ELSE '1'
--         END                                   AS "AcBookCode"        -- 會計帳冊                -- 1=一般 2=分紅 3=利變 4=OIU
--       , NVL(M."CurrencyCode"     ,' ')        AS "CurrencyCode"      -- 交易幣別                -- TWD
--       , NVL(M."ExchangeRate"     ,0)          AS "ExchangeRate"      -- 報導日匯率
--       , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
--       , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
--       , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
--       , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
--  FROM   "Ias39Loan34Data" M
--  WHERE  M."DataYM"          =  YYYYMM
--    AND  M."Status" NOT IN (3,5,6,8,9)   -- (不含結清、銷戶與呆帳案件)
--    ;

    INS_CNT := INS_CNT + sql%rowcount;

    --
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Ap END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MonthlyLoanBa_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
