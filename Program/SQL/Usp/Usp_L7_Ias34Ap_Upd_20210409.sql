-- 程式功能：維護 Ias34Ap 每月IAS34資料欄位清單A檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias34Ap_Upd"(20200423,'999999');
--

-- 法務費 (戶號彙總資料)
DROP TABLE "Work_Ias34Ap_Law" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_Ias34Ap_Law"
    (  "CustNo"          decimal(7, 0)   default 0 not null    -- 戶號
     , "TotalLoanBal"    decimal(16, 0)  default 0 not null    -- 放款餘額(戶號加總)
     , "TotalLawFee"     decimal(16, 0)  default 0 not null    -- 法務費(戶號加總)
    )
    ON COMMIT DELETE ROWS;

-- 火險費 (額度彙總資料)
DROP TABLE "Work_Ias34Ap_Fire" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_Ias34Ap_Fire"
    (  "CustNo"          decimal(7, 0)   default 0 not null    -- 戶號
     , "FacmNo"          decimal(3, 0)   default 0 not null    -- 額度
     , "TotalFacBal"     decimal(16, 0)  default 0 not null    -- 放款餘額(額度加總)
     , "TotalFireFee"    decimal(16, 0)  default 0 not null    -- 火險費(額度加總)
    )
    ON COMMIT DELETE ROWS;


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
         , NVL("CdAcCode"."AcNoCode",' ')            AS "AcCode"            -- 會計科目(8碼)
         , CASE WHEN M."Status" IN (0) THEN 1
                ELSE 2
           END                                       AS "Status"            -- 戶況 (1=正常 2=催收)
         , NVL(F."FirstDrawdownDate", 0)             AS "FirstDrawdownDate" -- 初貸日期
         , NVL(M."DrawdownDate", 0)                  AS "DrawdownDate"      -- 撥款日期
         , NVL(F."MaturityDate", 0)                  AS "FacLineDate"       -- 到期日(額度)
         , NVL(M."MaturityDate", 0)                  AS "MaturityDate"      -- 到期日(撥款)
         , NVL(F."LineAmt", 0)                       AS "LineAmt"           -- 核准金額
         , NVL(M."DrawdownAmt", 0)                   AS "DrawdownAmt"       -- 撥款金額
         , NVL(F."AcctFee", 0)                       AS "AcctFee"           -- 帳管費
         , NVL(M."LoanBal", 0)                       AS "LoanBal"           -- 本金餘額(撥款)
         , CASE WHEN M."Status" IN (0) THEN NVL(M1."IntAmtAcc", 0)
                ELSE 0
           END                                       AS "IntAmt"            -- 應收利息          --計算至每月月底之撥款應收利息
         , 0                                         AS "Fee"               -- 法拍及火險費用
         , NVL(M."StoreRate", 0)                     AS "Rate"              -- 利率(撥款)        --抓取月底時適用利率
         , CASE WHEN M."Status" IN (2, 7) THEN  -- 用 "下次繳息日"
                  CASE
                    WHEN NVL(M."NextPayIntDate",0) = 0 THEN 0
                    WHEN M."NextPayIntDate" >= TBSDYF  THEN 0
                    WHEN ( TO_DATE(TBSDYF,'yyyy-mm-dd') - TO_DATE(NVL(M."NextPayIntDate",0),'yyyy-mm-dd') ) > 999 THEN 999
                    ELSE ( TO_DATE(TBSDYF,'yyyy-mm-dd') - TO_DATE(M."NextPayIntDate",'yyyy-mm-dd') )
                  END
                WHEN NVL(M."NextPayIntDate",0) = 0 THEN 0
                WHEN M."NextPayIntDate" >= TBSDYF  THEN 0
                WHEN ( TO_DATE(TBSDYF,'yyyy-mm-dd') - TO_DATE(M."NextPayIntDate",'yyyy-mm-dd') ) > 999 THEN 999
                ELSE ( TO_DATE(TBSDYF,'yyyy-mm-dd') - TO_DATE(M."NextPayIntDate",'yyyy-mm-dd') )
           END                                       AS "OvduDays"          -- 逾期繳款天數      --抓取月底日資料，並以天數表示
         , NVL(M."OvduDate", 0)                      AS "OvduDate"          -- 轉催收款日期
         , NVL(M."BadDebtDate", 0)                   AS "BadDebtDate"       -- 轉銷呆帳日期
         , NVL(OD."BadDebtAmt", 0)                   AS "BadDebtAmt"        -- 轉銷呆帳金額
         , 0                                         AS "DerCode"           -- 符合減損客觀證據之條件 (後面再處理)
         , NVL(F."GracePeriod", 0)                   AS "GracePeriod"       -- 初貸時約定還本寬限期  -- 使用額度資料
         , NVL(L."ApproveRate", 0)                   AS "ApproveRate"       -- 核准利率
         , CASE
             WHEN L."AmortizedCode" = '1' THEN '1'  -- 1.按月繳息(按期繳息到期還本)
             WHEN L."AmortizedCode" = '2' THEN '4'  -- 2.到期取息(到期繳息還本)
             WHEN L."AmortizedCode" = '3' THEN '2'  -- 3.本息平均法(期金)
             WHEN L."AmortizedCode" = '4' THEN '3'  -- 4.本金平均法
             WHEN L."AmortizedCode" = '5' THEN '4'  -- 5.按月撥款收息(逆向貸款)  --???
             ELSE '3'
           END                                       AS "AmortizedCode"     -- 契約當時還款方式      -- 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本
         , CASE
             WHEN NVL(P."IfrsStepProdCode",' ') = 'B' THEN '4'  -- 浮動階梯
             WHEN NVL(P."IfrsStepProdCode",' ') = 'A' THEN '3'  -- 固定階梯
             WHEN to_number(NVL(F."RateCode",'0')) IN (1, 3) THEN '1'  -- 機動
             WHEN to_number(NVL(F."RateCode",'0')) = 2 THEN '2'  -- 固定
             ELSE NVL(F."RateCode",' ')
           END                                       AS "RateCode"          -- 契約當時利率調整方式
         , CASE WHEN L."AmortizedCode" = '1' THEN 0   -- 到期還本
                ELSE NVL(F."RepayFreq", 0)
           END                                       AS "RepayFreq"         -- 契約約定當時還本週期
         , NVL(F."PayIntFreq", 0)                    AS "PayIntFreq"        -- 契約約定當時繳息週期
         , CASE WHEN TRIM(NVL(M."IndustryCode", ' ')) = '' THEN ' '
                ELSE SUBSTR('000000' || M."IndustryCode", -6)
           END                                       AS "IndustryCode"      -- 授信行業別
         , NVL("CdCl"."ClTypeJCIC",' ')              AS "ClTypeJCIC"        -- 擔保品類別
         , NVL("ClMain"."CityCode",' ')              AS "CityCode"          -- 擔保品地區別
         , NVL("ClMain"."AreaCode",' ')              AS "AreaCode"          -- 擔保品鄉鎮區
         , NVL("CdArea"."Zip3",'000')                AS "Zip3"              -- 擔保品郵遞區號
         , NVL(F."ProdNo",' ')                       AS "ProdNo"            -- 商品利率代碼
         , CASE
             WHEN M."EntCode" IN ('1') THEN 1  -- 企金
             ELSE 2
           END                                       AS "CustKind"          -- 企業戶/個人戶
         , CASE WHEN NVL(F1."LawAmount",0) > 0               THEN 5
                WHEN TRIM(NVL(F1."AssetClass",'')) IS NULL   THEN 0
                ELSE to_number(SUBSTR(F1."AssetClass", 1, 1))
           END                                       AS "AssetKind"         -- 五類資產分類
         , NVL(P."IfrsProdCode",' ')                 AS "IfrsProdCode"      -- 產品別
         , CASE WHEN M."ClCode1" in (1)    THEN NVL(Eva1."EvaAmt",0)
                WHEN M."ClCode1" in (2)    THEN NVL(Eva2."EvaAmt",0)
                WHEN M."ClCode1" in (3, 4) THEN NVL(Eva3."EvaAmt",0)
                WHEN M."ClCode1" in (5)    THEN NVL(Eva5."EvaAmt",0)
                WHEN M."ClCode1" in (9)    THEN NVL(Eva9."EvaAmt",0)
                ELSE 0
           END                                       AS "EvaAmt"            -- 原始鑑價金額
         , NVL(L."FirstDueDate",0)                   AS "FirstDueDate"      -- 首次應繳日
         , NVL(L."TotalPeriod",0)                    AS "TotalPeriod"       -- 總期數
         , NVL(Renew."OldFacmNo",0)                  AS "AgreeBefFacmNo"    -- 協議前之額度編號
         , NVL(Renew."OldBormNo",0)                  AS "AgreeBefBormNo"    -- 協議前之撥款序號
         , NVL(M."UtilAmt", 0)                       AS "UtilAmt"           -- 累計撥款金額(額度)
         , NVL(M."UtilBal", 0)                       AS "UtilBal"           -- 已動用餘額(額度)
         , NVL(to_char(M."RecycleCode"),' ')         AS "RecycleCode"       -- 該筆額度是否可循環動用  -- 0: 非循環動用  1: 循環動用
         , NVL(to_char(M."IrrevocableFlag"),' ')     AS "IrrevocableFlag"   -- 該筆額度是否為不可徹銷  -- 1=是 0=否
         , NVL(Tav."TempAmt",0)                      AS "TempAmt"           -- 暫收款金額(台幣) ???
         , 1                                         AS "AcCurcd"           -- 記帳幣別                -- 1=台幣 2=美元 3=澳幣 4=人民幣 5=歐元
         , CASE WHEN M."AcBookCode" IS NULL THEN '1'
                WHEN M."AcBookCode" IN ('000', '10H') THEN '1'
                WHEN M."AcBookCode" IN ('201') THEN '3'
                ELSE '1'
           END                                       AS "AcBookCode"        -- 會計帳冊                -- 1=一般 2=分紅 3=利變 4=OIU
         , NVL(M."CurrencyCode",'TWD')               AS "CurrencyCode"      -- 交易幣別                -- TWD
         , 1                                         AS "ExchangeRate"      -- 報導日匯率
         , JOB_START_TIME                            AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                     AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                            AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                     AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "JcicMonthlyLoanData" M
      LEFT JOIN "FacMain" F     ON F."CustNo"       = M."CustNo"
                               AND F."FacmNo"       = M."FacmNo"
      LEFT JOIN "FacProd" P     ON P."ProdNo"       = F."ProdNo"
      LEFT JOIN "CdAcCode"      ON "CdAcCode"."AcctCode"  = M."AcctCode"
      LEFT JOIN "LoanBorMain" L  ON L."CustNo"    = M."CustNo"
                                AND L."FacmNo"    = M."FacmNo"
                                AND L."BormNo"    = M."BormNo"
      LEFT JOIN "MonthlyFacBal"   F1    ON F1."YearMonth" = YYYYMM
                                       AND F1."CustNo"    = M."CustNo"
                                       AND F1."FacmNo"    = M."FacmNo"
      LEFT JOIN "MonthlyLoanBal"  M1    ON M1."YearMonth" = YYYYMM
                                       AND M1."CustNo"    = M."CustNo"
                                       AND M1."FacmNo"    = M."FacmNo"
                                       AND M1."BormNo"    = M."BormNo"
      LEFT JOIN ( SELECT OD."CustNo"            AS  "CustNo"
                       , OD."FacmNo"            AS  "FacmNo"
                       , OD."BormNo"            AS  "BormNo"
                       , OD."BadDebtAmt"        AS  "BadDebtAmt"
                       , ROW_NUMBER() Over (Partition By OD."CustNo", OD."FacmNo", OD."BormNo"
                                      Order By OD."CustNo", OD."FacmNo", OD."BormNo", OD."OvduNo" DESC )
                                                AS ROW_NO
                  FROM "LoanOverdue"  OD
                  WHERE OD."Status" IN (1, 2)
                ) OD    ON OD."CustNo"  = M."CustNo"
                       AND OD."FacmNo"  = M."FacmNo"
                       AND OD."BormNo"  = M."BormNo"
                       AND OD.ROW_NO    = 1
      LEFT JOIN "ClMain"      ON "ClMain"."ClCode1"       = M."ClCode1"
                             AND "ClMain"."ClCode2"       = M."ClCode2"
                             AND "ClMain"."ClNo"          = M."ClNo"
      LEFT JOIN "CdCl"        ON "CdCl"."ClCode1"         = M."ClCode1"
                             AND "CdCl"."ClCode2"         = M."ClCode2"
      LEFT JOIN "CdArea"      ON "CdArea"."CityCode"      = "ClMain"."CityCode"
                             AND "CdArea"."AreaCode"      = "ClMain"."AreaCode"
      LEFT JOIN ( SELECT F."CustNo"
                       , F."FacmNo"
                       , F."ApplNo"
                       , SUM( NVL(B."EvaUnitPrice",0) *
                              ( NVL(B."FloorArea",0) + NVL(P."Area",0) + NVL(C."Area",0) )
                            ) AS "EvaAmt"
                  FROM   "FacMain" F
                    LEFT JOIN "ClFac" CL  ON CL."ApproveNo" = F."ApplNo"
                    LEFT JOIN "ClBuilding" B  ON B."ClCode1"   = CL."ClCode1"
                                             AND B."ClCode2"   = CL."ClCode2"
                                             AND B."ClNo"      = CL."ClNo"
                    LEFT JOIN "ClBuildingPublic" P  ON p."ClCode1"   = CL."ClCode1"
                                                   AND P."ClCode2"   = CL."ClCode2"
                                                   AND P."ClNo"      = CL."ClNo"
                    LEFT JOIN "ClBuildingParking" C  ON C."ClCode1"  = CL."ClCode1"
                                                    AND C."ClCode2"  = CL."ClCode2"
                                                    AND C."ClNo"     = CL."ClNo"
                  WHERE  CL."ClCode1"  IN (1)
                  GROUP BY F."CustNo", F."FacmNo", F."ApplNo"
                ) Eva1   ON Eva1."CustNo"  =  F."CustNo"
                        AND Eva1."FacmNo"  =  F."FacmNo"       -- 建物鑑價金額
      LEFT JOIN ( SELECT F."CustNo"
                       , F."FacmNo"
                       , F."ApplNo"
                       , SUM( NVL(B."EvaUnitPrice",0) * NVL(B."Area",0) ) AS "EvaAmt"
                  FROM   "FacMain" F
                    LEFT JOIN "ClFac" CL  ON CL."ApproveNo" = F."ApplNo"
                    LEFT JOIN "ClLand" B      ON B."ClCode1"   = CL."ClCode1"
                                             AND B."ClCode2"   = CL."ClCode2"
                                             AND B."ClNo"      = CL."ClNo"
                  WHERE  CL."ClCode1"  IN (2)
                  GROUP BY F."CustNo", F."FacmNo", F."ApplNo"
                ) Eva2   ON Eva2."CustNo"  =  F."CustNo"
                        AND Eva2."FacmNo"  =  F."FacmNo"       -- 土地鑑價金額
      LEFT JOIN ( SELECT F."CustNo"
                       , F."FacmNo"
                       , F."ApplNo"
                       , SUM( NVL(B."EvaUnitPrice",0) * NVL(B."SettingBalance",0) ) AS "EvaAmt"
                  FROM   "FacMain" F
                    LEFT JOIN "ClFac" CL  ON CL."ApproveNo" = F."ApplNo"
                    LEFT JOIN "ClStock" B     ON B."ClCode1"   = CL."ClCode1"
                                             AND B."ClCode2"   = CL."ClCode2"
                                             AND B."ClNo"      = CL."ClNo"
                  WHERE  CL."ClCode1"  IN (3, 4)
                  GROUP BY F."CustNo", F."FacmNo", F."ApplNo"
                ) Eva3   ON Eva3."CustNo"  =  F."CustNo"
                        AND Eva3."FacmNo"  =  F."FacmNo"       -- 股票鑑價金額
      LEFT JOIN ( SELECT F."CustNo"
                       , F."FacmNo"
                       , F."ApplNo"
                       , SUM( NVL(B."SettingAmt",0) ) AS "EvaAmt"
                  FROM   "FacMain" F
                    LEFT JOIN "ClFac" CL  ON CL."ApproveNo" = F."ApplNo"
                    LEFT JOIN "ClOther" B     ON B."ClCode1"   = CL."ClCode1"
                                             AND B."ClCode2"   = CL."ClCode2"
                                             AND B."ClNo"      = CL."ClNo"
                  WHERE  CL."ClCode1"  IN (5)
                  GROUP BY F."CustNo", F."FacmNo", F."ApplNo"
                ) Eva5   ON Eva5."CustNo"  =  F."CustNo"
                        AND Eva5."FacmNo"  =  F."FacmNo"       -- 其他鑑價金額 (保證)
      LEFT JOIN ( SELECT F."CustNo"
                       , F."FacmNo"
                       , F."ApplNo"
                       , SUM( NVL(B."SettingAmt",0) ) AS "EvaAmt"
                  FROM   "FacMain" F
                    LEFT JOIN "ClFac" CL  ON CL."ApproveNo" = F."ApplNo"
                    LEFT JOIN "ClMovables" B  ON B."ClCode1"   = CL."ClCode1"
                                             AND B."ClCode2"   = CL."ClCode2"
                                             AND B."ClNo"      = CL."ClNo"
                  WHERE  CL."ClCode1"  IN (9)
                  GROUP BY F."CustNo", F."FacmNo", F."ApplNo"
                ) Eva9   ON Eva9."CustNo"  =  F."CustNo"
                        AND Eva9."FacmNo"  =  F."FacmNo"       -- 動產鑑價金額
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
    WHERE  M."DataYM"          =  YYYYMM
      AND  M."Status" IN (0, 2, 7)   -- 正常件, 催收, 部分轉呆
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Ap END: INS_CNT=' || INS_CNT);


--         , 0                                         AS "DerCode"           -- 符合減損客觀證據之條件 (後面再處理)
-- 更新 DerCode (ref: LNSP65A-LN6511) 符合減損客觀證據之條件
    DBMS_OUTPUT.PUT_LINE('UPDATE DerCode 符合減損客觀證據之條件');
    UPD_CNT := 0;

    UPDATE "Ias34Ap" M
    SET   M."DerCode" =
            CASE
           -- WHEN      -- 特殊減損  ???
              WHEN M."ProdNo" IN ('60','61','62')  AND M."CustKind" IN (2) THEN 4    -- 協議件 自然人
              WHEN M."ProdNo" IN ('60','61','62')  AND M."CustKind" IN (1) THEN 3    -- 協議件 法人
              WHEN M."Status" NOT IN (1) AND M."CustKind" IN (2) THEN 4    -- 非正常戶 自然人
              WHEN M."Status" NOT IN (1) AND M."CustKind" IN (1) THEN 3    -- 非正常戶 法人
              WHEN M."OvduDays" >= 90    AND M."CustKind" IN (2) THEN 2    -- 逾期>=90 自然人
              WHEN M."OvduDays" >= 90    AND M."CustKind" IN (1) THEN 1    -- 逾期>=90 法人
              ELSE 0
            END
    WHERE M."DataYM"          =  YYYYMM
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE DerCode 符合減損客觀證據之條件 END');


    -- 寫入 法務費 (戶號彙總資料)
    DBMS_OUTPUT.PUT_LINE('INSERT Work_Ias34Ap_Law');

    INSERT INTO "Work_Ias34Ap_Law"
    SELECT M."CustNo"              AS "CustNo"        -- 戶號
         , M."LoanBal"             AS "TotalLoanBal"  -- 放款餘額(戶號加總)
         , SUM( M."LawFee")        AS "TotalLawFee"   -- 法務費(戶號加總)
    FROM  ( SELECT M1."CustNo"
                 , M2."LoanBal"
                 , M1."LawFee"
            FROM ( SELECT M."CustNo"
                        , M."FacmNo"
                        , NVL(F."LawFee",0)   AS "LawFee"
                   FROM   "Ias34Ap" M
                     LEFT JOIN "MonthlyFacBal" F  ON F."YearMonth" =  M."DataYM"
                                                 AND F."CustNo"    =  M."CustNo"
                                                 AND F."FacmNo"    =  M."FacmNo"
                   WHERE  M."DataYM"  = YYYYMM
                   GROUP BY M."CustNo", M."FacmNo", NVL(F."LawFee",0)
                 ) M1                                       -- 法務費(額度層級加總)
              LEFT JOIN
                 ( SELECT M."CustNo"
                        , SUM( M."LoanBal" )  AS "LoanBal"  -- 放款餘額(戶號層級加總)
                   FROM   "Ias34Ap" M
                   WHERE  M."DataYM"  = YYYYMM
                   GROUP BY M."CustNo"
                 ) M2    ON M2."CustNo" = M1."CustNo"
          )  M
    GROUP BY M."CustNo", M."LoanBal"
      ;

    DBMS_OUTPUT.PUT_LINE('INSERT Work_Ias34Ap_Law END');


    -- 寫入 火險費 (額度彙總資料)
    DBMS_OUTPUT.PUT_LINE('INSERT Work_Ias34Ap_Fire');

    INSERT INTO "Work_Ias34Ap_Fire"
    SELECT M."CustNo"           AS "CustNo"        -- 戶號
         , M."FacmNo"           AS "FacmNo"        -- 額度
         , SUM (M."LoanBal")    AS "TotalFacBal"   -- 放款餘額(額度加總)
         , NVL(F."FireFee",0)   AS "TotalFireFee"  -- 火險費(額度加總)
    FROM   "Ias34Ap" M
      LEFT JOIN "MonthlyFacBal" F  ON F."YearMonth" =  M."DataYM"
                                  AND F."CustNo"    =  M."CustNo"
                                  AND F."FacmNo"    =  M."FacmNo"
    WHERE  M."DataYM"  = YYYYMM
    GROUP BY M."CustNo", M."FacmNo", NVL(F."FireFee",0)
      ;

    DBMS_OUTPUT.PUT_LINE('INSERT Work_Ias34Ap_Fire END');


-- 更新 Fee 法拍及火險費用 (法務費)
    DBMS_OUTPUT.PUT_LINE('UPDATE Fee 法務費');

    MERGE INTO "Ias34Ap" M
    USING ( SELECT L."CustNo"
                 , L."TotalLoanBal"
                 , L."TotalLawFee"
            FROM "Work_Ias34Ap_Law" L
            WHERE L."TotalLoanBal" > 0 AND L."TotalLawFee" > 0
          ) L
    ON (    M."DataYM"    =  YYYYMM
        AND M."CustNo"    =  L."CustNo"
       )
    WHEN MATCHED THEN UPDATE
      SET M."Fee" = M."Fee" + L."TotalLawFee" * ( M."LoanBal" / L."TotalLoanBal" )
    ;

    DBMS_OUTPUT.PUT_LINE('UPDATE Fee 法務費 END');

-- 更新 Fee 法拍及火險費用 (火險費)
    DBMS_OUTPUT.PUT_LINE('UPDATE Fee 火險費');

    MERGE INTO "Ias34Ap" M
    USING ( SELECT L."CustNo"
                 , L."FacmNo"
                 , L."TotalFacBal"
                 , L."TotalFireFee"
            FROM "Work_Ias34Ap_Fire" L
            WHERE L."TotalFacBal" > 0 AND L."TotalFireFee" > 0
          ) L
    ON (    M."DataYM"    =  YYYYMM
        AND M."CustNo"    =  L."CustNo"
        AND M."FacmNo"    =  L."FacmNo"
       )
    WHEN MATCHED THEN UPDATE
      SET M."Fee" = M."Fee" + L."TotalFireFee" * ( M."LoanBal" / L."TotalFacBal" )
    ;

    DBMS_OUTPUT.PUT_LINE('UPDATE Fee 火險費 END');


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
--       , NVL(M."BaseRateCode"     ,' ')        AS "ProdNo"            -- 商品利率代碼
--       , NVL(M."CustKind"         ,0)          AS "CustKind"          -- 企業戶/個人戶
--       , NVL(M."AssetKind"        ,0)          AS "AssetKind"         -- 五類資產分類
--       , NVL(M."ProdNo"           ,' ')        AS "IfrsProdCode"      -- 產品別
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


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

-- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MonthlyLoanBa_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;


        --  , 0            AS "FirstDrawdownDate" -- 初貸日期
        --  , 0            AS "DrawdownDate"      -- 撥款日期
        --  , 0            AS "FacLineDate"       -- 到期日(額度)
        --  , 0            AS "MaturityDate"      -- 到期日(撥款)
        --  , 0            AS "LineAmt"           -- 核准金額
        --  , 0            AS "DrawdownAmt"       -- 撥款金額
        --  , 0            AS "AcctFee"           -- 帳管費
        --  , 0            AS "LoanBal"           -- 本金餘額(撥款)
        --  , 0            AS "IntAmt"            -- 應收利息          --計算至每月月底之撥款應收利息
        --  , 0            AS "Fee"               -- 法拍及火險費用  ???
        --  , 0            AS "Rate"              -- 利率(撥款)        --抓取月底時適用利率
        --  , 0            AS "OvduDays"          -- 逾期繳款天數      --抓取月底日資料，並以天數表示
        --  , 0            AS "OvduDate"          -- 轉催收款日期
        --  , 0            AS "BadDebtDate"       -- 轉銷呆帳日期
        --  , 0            AS "BadDebtAmt"        -- 轉銷呆帳金額
        --  , 0            AS "DerCode"           -- 符合減損客觀證據之條件 (後面再處理)
        --  , 0            AS "GracePeriod"       -- 初貸時約定還本寬限期
        --  , 0            AS "ApproveRate"       -- 核准利率
        --  ' '            AS "AmortizedCode"     -- 契約當時還款方式      -- 1=按期繳息(到期還本)；2=平均攤還本息；3=平均攤還本金；4=到期繳息還本
        --  , ' '          AS "RateCode"          -- 契約當時利率調整方式
        --  , 0            AS "RepayFreq"         -- 契約約定當時還本週期
        --  , 0            AS "PayIntFreq"        -- 契約約定當時繳息週期
        --  , ' '          AS "IndustryCode"      -- 授信行業別
        --  , ' '          AS "ClTypeJCIC"        -- 擔保品類別
        --  , ' '          AS "CityCode"          -- 擔保品地區別
        --  , ' '          AS "AreaCode"          -- 擔保品鄉鎮區
        --  , ' '          AS "Zip3"              -- 擔保品郵遞區號
        --  , ' '          AS "ProdNo"            -- 商品利率代碼
        --  , 0            AS "CustKind"          -- 企業戶/個人戶
        --  , ' '          AS "AssetKind"         -- 五類資產分類   ???
        --  , ' '          AS "IfrsProdCode"      -- 產品別
        --  , 0            AS "EvaAmt"            -- 原始鑑價金額
        --  , 0            AS "FirstDueDate"      -- 首次應繳日
        --  , 0            AS "TotalPeriod"       -- 總期數
        --  , 0            AS "AgreeBefFacmNo"    -- 協議前之額度編號
        --  , 0            AS "AgreeBefBormNo"    -- 協議前之撥款序號
        --  , 0            AS "UtilAmt"           -- 累計撥款金額(額度)
        --  , 0            AS "UtilBal"           -- 已動用餘額(額度)
        --  , ' '          AS "RecycleCode"       -- 該筆額度是否可循環動用  -- 0: 非循環動用  1: 循環動用
        --  , ' '          AS "IrrevocableFlag"   -- 該筆額度是否為不可徹銷  -- 1=是 0=否
        --  , 0            AS "TempAmt"           -- 暫收款金額(台幣)
        --  , 1            AS "AcCurcd"           -- 記帳幣別                -- 1=台幣 2=美元 3=澳幣 4=人民幣 5=歐元
        --  , ' '          AS "AcBookCode"        -- 會計帳冊                -- 1=一般 2=分紅 3=利變 4=OIU
        --  , ' '          AS "CurrencyCode"      -- 交易幣別                -- TWD
        --  , 0            AS "ExchangeRate"      -- 報導日匯率
