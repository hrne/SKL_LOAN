CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L7_Ias39Loan34Data_Upd"
(
-- 程式功能：維護 Ias39Loan34Data 每月IAS39放款34號公報資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias39Loan34Data_Upd"(20200420,'');
--

    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
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
    TMNDYF         INT;         -- 本月月底日
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
    -- 抓本月月底日
    SELECT "TmnDyf"
    INTO TMNDYF
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE Ias39Loan34Data');

    DELETE FROM "Ias39Loan34Data"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Ias39Loan34Data');

    INSERT INTO "Ias39Loan34Data"
    SELECT
           YYYYMM                               AS "DataYM"            -- 資料年月
         , M."CustNo"                           AS "CustNo"            -- 戶號
         , M."FacmNo"                           AS "FacmNo"            -- 額度編號
         , NVL("FacMain"."ApplNo",0)            AS "ApplNo"            -- 核准號碼
         , M."BormNo"                           AS "BormNo"            -- 撥款序號
         , "CustMain"."CustId"                  AS "CustId"            -- 借款人ID / 統編
         , 1                                    AS "DrawdownFg"        -- 已核撥記號    0: 未核撥  1: 已核撥
         , M."AcctCode"                         AS "AcctCode"          -- 業務科目代號  CdAcCode會計科子細目設定檔，出帳時更新；未核撥: 額度核准科目
         , NVL("LoanBorMain"."Status",0)        AS "Status"            -- 戶況
         , NVL("FacMain"."FirstDrawdownDate",0) AS "FirstDrawdownDate" -- 初貸日期 (額度初貸日)
         , NVL("LoanBorMain"."DrawdownDate",0)  AS "DrawdownDate"      -- 撥款日期
         , NVL("FacMain"."MaturityDate",0)      AS "FacLineDate"       -- 到期日(額度)
         , NVL("LoanBorMain"."MaturityDate",0)  AS "MaturityDate"      -- 到期日(撥款)  -- 已撥款案件到期日；未撥款案件值為0
         , NVL("FacMain"."LineAmt",0)           AS "LineAmt"           -- 核准金額      -- 每額度編號項下之放款帳號皆同核准額度金額
         , NVL("LoanBorMain"."DrawdownAmt",0)   AS "DrawdownAmt"       -- 撥款金額
         , NVL("FacMain"."AcctFee",0)           AS "AcctFee"           -- 帳管費
         , NVL("LoanBorMain"."LoanBal",0)       AS "LoanBal"           -- 本金餘額(撥款)
/*???*/  , NVL(M."IntAmtAcc",0)                 AS "IntAmt"            -- 應收利息      --??? 提存利息  -- 計算至每月月底之撥款應收利息
/*L*/    , 0                                    AS "Fee"               -- 法拍及火險費用
         , NVL(M."StoreRate",0)                 AS "Rate"              -- 利率(撥款)    -- 抓取月底時適用利率
         , CASE
             WHEN "LoanBorMain"."Status" IN (3,5,6,8,9)      THEN 0
             WHEN NVL("LoanBorMain"."NextPayIntDate",0) = 0  THEN 0
             WHEN "LoanBorMain"."NextPayIntDate" >= TMNDYF   THEN 0
             --WHEN (TO_DATE(TBSDYF,'YYYYMMDD') - TO_DATE("LoanBorMain"."NextPayIntDate",'YYYYMMDD')) > 999 THEN 999
             ELSE TO_DATE(TMNDYF,'YYYYMMDD') - TO_DATE("LoanBorMain"."NextPayIntDate",'YYYYMMDD')
           END                                  AS "OvduDays"          -- 逾期繳款天數  -- 抓取月底日資料，並以天數表示  ref:Usp_L5_CollList_Upd
         , NVL(OD1."LastOvduDate",0)            AS "OvduDate"          -- 轉催收款日期  -- 抓取最近一次的轉催收日期
         , NVL(OD2."FirstBadDebtDate",0)        AS "BadDebtDate"       -- 轉銷呆帳日期  -- 最早之轉銷呆帳日期
         , NVL(OD2."BadDebtAmt",0)              AS "BadDebtAmt"        -- 轉銷呆帳金額  -- 無論轉呆次數，計算全部轉銷呆帳之金額
/*???*/  , 0                                    AS "DerCode"           -- 符合減損客觀證據之條件    --??? 待 CollList
         , NVL("LoanBorMain"."GracePeriod",0)   AS "GracePeriod"       -- 初貸時約定還本寬限期(以月為單位)
         , NVL("LoanBorMain"."ApproveRate", 0)  AS "ApproveRate"       -- 核准利率
/*???*/  , CASE
             WHEN "LoanBorMain"."AmortizedCode" = '1' THEN 1  -- 1.按月繳息(按期繳息到期還本)
             WHEN "LoanBorMain"."AmortizedCode" = '2' THEN 4  -- 2.到期取息(到期繳息還本)
             WHEN "LoanBorMain"."AmortizedCode" = '3' THEN 2  -- 3.本息平均法(期金)
             WHEN "LoanBorMain"."AmortizedCode" = '4' THEN 3  -- 4.本金平均法
             WHEN "LoanBorMain"."AmortizedCode" = '5' THEN 4  -- 5.按月撥款收息(逆向貸款)  --???
             ELSE 0
           END                                  AS "AmortizedCode"     -- 契約當時還款方式      -- 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本
/*???*/  , CASE
             WHEN "LoanBorMain"."RateCode" = '1' THEN 1  -- 1: 機動
             WHEN "LoanBorMain"."RateCode" = '2' THEN 2  -- 2: 固定
             WHEN "LoanBorMain"."RateCode" = '3' THEN 3  -- 3: 定期機動
             ELSE 4
           END                                  AS "RateCode"          -- 契約當時利率調整方式    --??? 1=機動；2=固定；3=固定階梯；4=浮動階梯
         , CASE
             WHEN "FacMain"."AmortizedCode" = '1' THEN 0
             WHEN "FacMain"."AmortizedCode" = '2' THEN 0
             WHEN "FacMain"."AmortizedCode" = '5' THEN 0
             ELSE NVL("FacMain"."RepayFreq",0)
           END                                  AS "RepayFreq"         -- 契約約定當時還本週期    --若為到期還本，則填入0
         , CASE
             WHEN "FacMain"."AmortizedCode" = '2' THEN 0
             WHEN "FacMain"."AmortizedCode" = '5' THEN 0
             ELSE NVL("FacMain"."PayIntFreq",0)
           END                                  AS "PayIntFreq"        -- 契約約定當時繳息週期    --若為到期繳息，則填入0
         , NVL("CustMain"."IndustryCode",' ')   AS "IndustryCode"      -- 授信行業別
         , NVL("CdCl"."ClTypeJCIC",' ')         AS "ClTypeJCIC"        -- 擔保品類別
         , NVL("ClMain"."CityCode",' ')         AS "CityCode"          -- 擔保品地區別
         , NVL("ClMain"."AreaCode",' ')         AS "AreaCode"          -- 擔保品鄉鎮區
         , NVL("CdArea"."Zip3",' ')             AS "Zip3"              -- 擔保品郵遞區號
         , NVL("FacMain"."BaseRateCode",' ')    AS "BaseRateCode"      -- 商品利率代碼
         , CASE
             WHEN M."EntCode" = 0 THEN 2
             WHEN M."EntCode" = 1 THEN 1
             WHEN M."EntCode" = 2 THEN 2    --> ??? 2:企金自然人
             ELSE 2
           END                                  AS "CustKind"          -- 企業戶/個人戶  -- 1=企業戶；2=個人戶
/*???*/  , 0                                    AS "AssetKind"         -- 五類資產分類
/*???*/  , ' '                                  AS "ProdNo"            -- 產品別         --??? 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc
         , NVL("ClMain"."EvaAmt",0)             AS "EvaAmt"            -- 原始鑑價金額
         , NVL("LoanBorMain"."FirstDueDate",0)  AS "FirstDueDate"      -- 首次應繳日
         , NVL("LoanBorMain"."TotalPeriod",0)   AS "TotalPeriod"       -- 總期數
         , NVL(Renew."OldFacmNo",0)             AS "AgreeBefFacmNo"    -- 協議前之額度編號
         , NVL(Renew."OldBormNo",0)             AS "AgreeBefBormNo"    -- 協議前之撥款序號
         , NVL("FacMain"."UtilAmt",0)           AS "UtilAmt"           -- 累計撥款金額(額度)
         , NVL("FacMain"."UtilBal",0)           AS "UtilBal"           -- 已動用餘額(額度)
         , NVL(Tav."TempAmt",0)                 AS "TempAmt"           -- 暫收款金額(台幣)
         , CASE
             WHEN NVL("FacMain"."RecycleCode",0) = 1 AND
                  NVL("FacMain"."RecycleDeadline",0) / 100 >= YYYYMM AND
                  NVL("FacMain"."LineAmt",0) >= NVL("LoanBorMain"."LoanBal",0) THEN  NVL("FacMain"."LineAmt",0) - NVL("LoanBorMain"."LoanBal",0)
             WHEN NVL("FacMain"."RecycleCode",0) = 0 AND
                  NVL("FacMain"."UtilDeadline",0) / 100 >= YYYYMM AND
                  NVL("FacMain"."LineAmt",0) >= NVL("FacMain"."UtilBal",0) THEN  NVL("FacMain"."LineAmt",0) - NVL("FacMain"."UtilBal",0)
             ELSE 0
           END                                  AS "AvblBal"           -- 可動用餘額
                                                                          --> 當【可循環動用】=1且【循環動用期限】>=月底日→【可動用餘額】=【核准額度】-【放款餘額】
                                                                          --> 當【可循環動用】=0且【動支期限】>=月底日→【可動用餘額】=【核准額度】-【已用額度】
         , M."CurrencyCode"                     AS "CurrencyCode"      -- 記帳幣別
         , 1                                    AS "ExchangeRate"      -- 報導日匯率
         , CASE
             WHEN "FacMain"."GuaranteeDate" IS NULL OR "FacMain"."GuaranteeDate" = 0 THEN NVL("FacCaseAppl"."ApproveDate",0)
             ELSE "FacMain"."GuaranteeDate"
           END                                  AS "ApproveDate"       -- 核准日期(額度)    --1.優先取用對保日期 2.無對保日採用准駁日
         , 0
                                                AS "LoanTerm"          -- 貸款期間  -- 貸款年數(2)+貸款月數(2)+貸款日數(2)
         , CASE
             WHEN NVL("FacMain"."RecycleCode",0) = 0 THEN 0
             ELSE NVL("FacMain"."RecycleDeadline",0)
           END                                  AS "RecycleDeadline"   -- 循環動用期限  -- 循環:額度循環動用期限；非循環:0
         , NVL("FacMain"."RecycleCode",0)       AS "RecycleCode"       -- 該筆額度是否可循環動用  -- 0: 非循環動用 1: 循環動用
         , CASE
             WHEN "FacMain"."IrrevocableFlag" = 'Y' THEN 1
             ELSE 0
           END                                  AS "IrrevocableFlag"   -- 該筆額度是否為不可徹銷  -- 0: 可徹銷     1: 不可徹銷
         , NVL(M."AcBookCode",' ')              AS "AcBookCode"        -- 帳冊別
         , NVL("CdAcCode"."AcNoCode",' ')       AS "AcNoCode"          -- 會計科目(8碼)
/*???*/  , CASE
             WHEN "FacMain"."AmortizedCode" = '1' THEN 1  -- 1.按月繳息(按期繳息到期還本)
             WHEN "FacMain"."AmortizedCode" = '2' THEN 4  -- 2.到期取息(到期繳息還本)
             WHEN "FacMain"."AmortizedCode" = '3' THEN 2  -- 3.本息平均法(期金)
             WHEN "FacMain"."AmortizedCode" = '4' THEN 3  -- 4.本金平均法
             WHEN "FacMain"."AmortizedCode" = '5' THEN 4  -- 5.按月撥款收息(逆向貸款)  --???
             ELSE 0
           END                                  AS "FacAmortizedCode"  -- 還款方式(額度) -- 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本
         , JOB_START_TIME                       AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                       AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "MonthlyLoanBal" M
      LEFT JOIN "CustMain"    ON "CustMain"."CustNo"      = M."CustNo"
      LEFT JOIN "FacMain"     ON "FacMain"."CustNo"       = M."CustNo"
                             AND "FacMain"."FacmNo"       = M."FacmNo"
      LEFT JOIN "FacCaseAppl" ON "FacCaseAppl"."ApplNo"   = "FacMain"."ApplNo"
      LEFT JOIN "LoanBorMain" ON "LoanBorMain"."CustNo"   = M."CustNo"
                             AND "LoanBorMain"."FacmNo"   = M."FacmNo"
                             AND "LoanBorMain"."BormNo"   = M."BormNo"
      LEFT JOIN "ClMain"      ON "ClMain"."ClCode1"       = M."ClCode1"
                             AND "ClMain"."ClCode2"       = M."ClCode2"
                             AND "ClMain"."ClNo"          = M."ClNo"
      LEFT JOIN "CdCl"        ON "CdCl"."ClCode1"         = M."ClCode1"
                             AND "CdCl"."ClCode2"         = M."ClCode2"
      LEFT JOIN "CdArea"      ON "CdArea"."CityCode"      = "ClMain"."CityCode"
                             AND "CdArea"."AreaCode"      = "ClMain"."AreaCode"
      LEFT JOIN "CdAcCode"    ON "CdAcCode"."AcctCode"    = M."AcctCode"
      LEFT JOIN ( SELECT
                      L1."CustNo"         AS  "CustNo"
                    , L1."FacmNo"         AS  "FacmNo"
                    , L1."BormNo"         AS  "BormNo"
                    , MAX(L1."OvduDate")  AS  "LastOvduDate"
                  FROM "LoanOverdue" L1
                  GROUP BY L1."CustNo", L1."FacmNo", L1."BormNo"
                ) OD1   ON OD1."CustNo"     = "LoanBorMain"."CustNo"
                       AND OD1."FacmNo"     = "LoanBorMain"."FacmNo"
                       AND OD1."BormNo"     = "LoanBorMain"."BormNo"
      LEFT JOIN ( SELECT L2."CustNo"            AS  "CustNo"
                       , L2."FacmNo"            AS  "FacmNo"
                       , L2."BormNo"            AS  "BormNo"
                       , MIN(L2."BadDebtDate")  AS  "FirstBadDebtDate"
                       , SUM(L2."BadDebtAmt")   AS  "BadDebtAmt"
                  FROM "LoanOverdue" L2
                  WHERE L2."BadDebtDate" > 0
                  GROUP BY L2."CustNo", L2."FacmNo", L2."BormNo"
                ) OD2   ON OD2."CustNo"     = "LoanBorMain"."CustNo"
                       AND OD2."FacmNo"     = "LoanBorMain"."FacmNo"
                       AND OD2."BormNo"     = "LoanBorMain"."BormNo"
      --協議
      LEFT JOIN ( SELECT A.*
                  FROM   "AcLoanRenew" A
                  WHERE  A."RenewCode" = '2'    --1.一般 2.協議
                    AND  A."MainFlag"  = 'Y'    --Y:新撥款對應舊撥款的第一筆
                ) Renew  ON Renew."CustNo"      = M."CustNo"
                        AND Renew."NewFacmNo"   = M."FacmNo"
                        AND Renew."NewBormNo"   = M."BormNo"
      --暫收款
      LEFT JOIN ( SELECT A."CustNo", A."FacmNo", SUM(A."AcBal") AS "TempAmt"
                  FROM   "AcReceivable" A
                  WHERE  A."AcctCode" = 'TAV'
                    AND  A."ClsFlag"  = '0'     --未銷
                  GROUP BY A."CustNo", A."FacmNo"
                ) Tav    ON Tav."CustNo"        = M."CustNo"
                        AND Tav."FacmNo"        = M."FacmNo"
    WHERE  M."YearMonth"  =  YYYYMM
      ;

    INS_CNT := INS_CNT + sql%rowcount;


--   更新 Fee	法拍及火險費用

    DBMS_OUTPUT.PUT_LINE('UPDATE Fee ');

    MERGE INTO "Ias39Loan34Data" A
    USING (  SELECT M."CustNo"          AS  "CustNo"
                  , M."FacmNo"          AS  "FacmNo"
                  , M."BormNo"          AS  "BormNo"
                  , SUM(NVL(Ac1."RvAmt",0))    AS  "Fee"
             FROM   "MonthlyLoanBal"  M
               LEFT JOIN "AcReceivable" Ac1  ON Ac1."CustNo"   = M."CustNo"
                                            AND Ac1."FacmNo"   = M."FacmNo"
                                            AND TO_CHAR(Ac1."RvNo") = TO_CHAR(M."ClNo")
             WHERE Ac1."AcctCode" IN ('F07','F09','F24','F25')
               AND M."YearMonth"  =  YYYYMM
             GROUP BY M."CustNo", M."FacmNo", M."BormNo"
          )  F
     ON (   A."DataYM"    = YYYYMM
        AND A."CustNo"    = F."CustNo"
        AND A."FacmNo"    = F."FacmNo"
        AND A."BormNo"    = F."BormNo" )
    WHEN MATCHED THEN UPDATE SET A."Fee" = NVL(F."Fee", 0)
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE Fee END');


    --
    DBMS_OUTPUT.PUT_LINE('INSERT Ias39Loan34Data END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L7_Ias39Loan34Data_Upd' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
      , JobTxSeq -- 啟動批次的交易序號
    );
    COMMIT;
    RAISE;
  END;
END;