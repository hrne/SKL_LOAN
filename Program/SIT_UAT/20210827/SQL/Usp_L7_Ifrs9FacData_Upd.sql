-- 程式功能：維護 Ifrs9FacData 每月IFRS9額度資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ifrs9FacData_Upd"(20200430,'System');
--

-- 額度資料
DROP TABLE "Work_Ifrs9FacData" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_Ifrs9FacData"
    (  "CustNo"          decimal(7, 0)   default 0 not null    -- 戶號
     , "FacmNo"          decimal(3, 0)   default 0 not null    -- 額度編號
     , "DrawdownFg"      decimal(1, 0)   default 0 not null    -- 已核撥記號 (0: 未核撥 1: 已核撥)
     , "TotalLoanBal"    decimal(16, 2)  default 0 not null    -- 本金餘額(撥款)合計
    )
    ON COMMIT DELETE ROWS;



CREATE OR REPLACE PROCEDURE "Usp_L7_Ifrs9FacData_Upd"
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


    -- 寫入已動撥額度
    DBMS_OUTPUT.PUT_LINE('INSERT Work_Ifrs9FacData DrawdownFg=1');
    INS_CNT := 0;

    INSERT INTO "Work_Ifrs9FacData"
    SELECT M."CustNo"              AS "CustNo"        -- 戶號
         , M."FacmNo"              AS "FacmNo"        -- 額度編號
         , 1                       AS "DrawdownFg"    -- 已核撥
         , SUM(M."LoanBal")        AS "TotalLoanBal"  -- 本金餘額(撥款)合計
    FROM     "Ifrs9LoanData" M
    WHERE    M."DataYM"          =  YYYYMM
    GROUP BY M."CustNo", M."FacmNo"
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Work_Ifrs9FacData DrawdownFg=1 END: INS_CNT=' || INS_CNT);


    -- 寫入未動撥額度
    DBMS_OUTPUT.PUT_LINE('INSERT Work_Ifrs9FacData DrawdownFg=0 未動撥');
    INS_CNT := 0;

    INSERT INTO "Work_Ifrs9FacData"
    SELECT M."CustNo"              AS "CustNo"        -- 戶號
         , M."FacmNo"              AS "FacmNo"        -- 額度編號
         , 0                       AS "DrawdownFg"    -- 已核撥
         , 0                       AS "TotalLoanBal"  -- 本金餘額(撥款)合計
    FROM   "FacMain" M
      LEFT JOIN "Work_Ifrs9FacData" WK ON WK."CustNo" = M."CustNo"
                                      AND WK."FacmNo" = M."FacmNo"
    WHERE  M."LastBormNo" = 0
      AND  TRUNC(NVL(M."UtilDeadline",0) / 100 ) >= YYYYMM   -- 已核貸未曾動撥且仍可動撥之放款額度編號
      AND  WK."CustNo" IS NULL
    GROUP BY M."CustNo", M."FacmNo"
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Work_Ifrs9FacData DrawdownFg=0 未動撥 END: INS_CNT=' || INS_CNT);


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE Ifrs9FacData');

    DELETE FROM "Ifrs9FacData"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Ifrs9FacData');
    INS_CNT := 0;

    INSERT INTO "Ifrs9FacData"
    SELECT
           YYYYMM                               AS "DataYM"            -- 資料年月
         , M."CustNo"                           AS "CustNo"            -- 戶號
         , M."FacmNo"                           AS "FacmNo"            -- 額度編號
         , NVL(F."ApplNo",0)                    AS "ApplNo"            -- 核准號碼
         , NVL("CustMain"."CustId",' ')         AS "CustId"            -- 借款人ID / 統編
         , M."DrawdownFg"                       AS "DrawdownFg"        -- 已核撥記號 (0:未核撥 1:已核撥)
         , CASE
             WHEN NVL(F."GuaranteeDate",0) = 0 THEN NVL("FacCaseAppl"."ApproveDate",0)
             ELSE F."GuaranteeDate"
           END                                  AS "ApproveDate"       -- 核准日期(額度)
                                                                          -- 1.優先取用對保日期
                                                                          -- 2.無對保日採用准駁日
         , NVL(F."UtilDeadline",0)              AS "UtilDeadline"      -- 動支期限
         , NVL(F."FirstDrawdownDate",0)         AS "FirstDrawdownDate" -- 初貸日期
         , NVL(F."MaturityDate",0)              AS "MaturityDate"      -- 到期日(額度)
         , NVL(F."LineAmt",0)                   AS "LineAmt"           -- 核准金額
         , NVL(F."AcctFee",0)                   AS "AcctFee"           -- 帳管費
         , NVL(MF."LawFee",0)                   AS "LawFee"            -- 法務費
         , NVL(MF."FireFee",0)                  AS "FireFee"           -- 火險費
         , NVL(F."GracePeriod",0)               AS "GracePeriod"       -- 初貸時約定還本寬限期
         , NVL(F."AmortizedCode",'0')           AS "AmortizedCode"     -- 契約當時還款方式(月底日)
         , NVL(F."RateCode",'0')                AS "RateCode"          -- 契約當時利率調整方式(月底日)
         , NVL(F."RepayFreq",0)                 AS "RepayFreq"         -- 契約約定當時還本週期(月底日)
         , NVL(F."PayIntFreq",0)                AS "PayIntFreq"        -- 契約約定當時繳息週期(月底日)
         , NVL("FacProd"."IfrsStepProdCode", ' ')
                                                AS "IfrsStepProdCode"  -- IFRS階梯商品別
         , CASE WHEN TRIM(NVL("CustMain"."IndustryCode", ' ')) = '' THEN ' '
                ELSE SUBSTR('000000' || TRIM("CustMain"."IndustryCode"), -6)
           END                                  AS "IndustryCode"      -- 授信行業別
         , NVL("CdCl"."ClTypeJCIC", ' ')        AS "ClTypeJCIC"        -- 擔保品類別
         , NVL("ClMain"."CityCode", ' ')        AS "CityCode"          -- 擔保品地區別
         , NVL("ClMain"."AreaCode", ' ')        AS "AreaCode"          -- 擔保品鄉鎮區
         , NVL("CdArea"."Zip3", ' ')            AS "Zip3"              -- 擔保品郵遞區號
         , NVL(F."ProdNo", ' ')                 AS "ProdNo"            -- 商品利率代碼
         , NVL("FacProd"."AgreementFg", 'N')    AS "AgreementFg"       -- 是否為協議商品 (Y:是 N:否)
         , NVL("CustMain"."EntCode", ' ')       AS "EntCode"           -- 企金別  (0:個金 1:企金 2:企金自然人)
         , CASE WHEN NVL(MF."LawAmount", 0) > 0 THEN 5  -- 無擔保債權設定金額，資產分類一律為五
                WHEN TRIM(NVL(MF."AssetClass", ' ')) = ''  THEN 0
                WHEN SUBSTR(MF."AssetClass", 1, 1)   = '1' THEN 1
                WHEN SUBSTR(MF."AssetClass", 1, 1)   = '2' THEN 2
                WHEN SUBSTR(MF."AssetClass", 1, 1)   = '3' THEN 3
                WHEN SUBSTR(MF."AssetClass", 1, 1)   = '4' THEN 4
                WHEN SUBSTR(MF."AssetClass", 1, 1)   = '5' THEN 5
                ELSE 0
           END                                  AS "AssetClass"        -- 資產五分類代號
         , NVL("FacProd"."IfrsProdCode", ' ')   AS "IfrsProdCode"      -- 產品別
         , CASE WHEN Cl."ClCode1" in (1)    THEN NVL(Eva1."EvaAmt",0)
                WHEN Cl."ClCode1" in (2)    THEN NVL(Eva2."EvaAmt",0)
                WHEN Cl."ClCode1" in (3, 4) THEN NVL(Eva3."EvaAmt",0)
                WHEN Cl."ClCode1" in (5)    THEN NVL(Eva5."EvaAmt",0)
                WHEN Cl."ClCode1" in (9)    THEN NVL(Eva9."EvaAmt",0)
                ELSE 0
           END                                  AS "EvaAmt"            -- 原始鑑價金額
         , NVL(F."UtilAmt",0)                   AS "UtilAmt"           -- 累計撥款金額(額度層)
         , NVL(F."UtilBal",0)                   AS "UtilBal"           -- 已動用餘額(額度層)
         , NVL(M."TotalLoanBal",0)              AS "TotalLoanBal"      -- 本金餘額(額度層)合計
         , CASE WHEN F."RecycleCode" IS NULL  THEN 0
                WHEN F."RecycleCode" IN ('1') THEN 1
                ELSE 0
           END                                  AS "RecycleCode"       -- 該筆額度是否可循環動用
         , CASE WHEN F."IrrevocableFlag" IS NULL  THEN 1
                WHEN F."IrrevocableFlag" IN ('N') THEN 0
                ELSE 1
           END                                  AS "IrrevocableFlag"   -- 該筆額度是否為不可徹銷
         , NVL(Tav."TempAmt",0)                 AS "TempAmt"           -- 暫收款金額(台幣)
         , MF."AcBookCode"                      AS "AcBookCode"        -- 帳冊別
         , MF."AcSubBookCode"                   AS "AcSubBookCode"     -- 區隔帳冊
         , JOB_START_TIME                       AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                       AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Work_Ifrs9FacData" M
      LEFT JOIN "FacMain" F     ON F."CustNo"       = M."CustNo"
                               AND F."FacmNo"       = M."FacmNo"
      LEFT JOIN "FacCaseAppl" ON "FacCaseAppl"."ApplNo"   =  F."ApplNo"
      LEFT JOIN "MonthlyFacBal" MF  ON MF."YearMonth" =  YYYYMM
                                   AND MF."CustNo"    =  M."CustNo"
                                   AND MF."FacmNo"    =  M."FacmNo"
      LEFT JOIN "FacProd"     ON "FacProd"."ProdNo"   =  F."ProdNo"
      LEFT JOIN "CustMain"    ON "CustMain"."CustNo"  =  M."CustNo"
      LEFT JOIN "ClFac" Cl    ON Cl."CustNo"       = F."CustNo"
                             AND Cl."FacmNo"       = F."FacmNo"
                             AND Cl."ApproveNo"    = F."ApplNo"
                             AND Cl."MainFlag"     = 'Y'  -- 主要擔保品"
      LEFT JOIN "ClMain"      ON "ClMain"."ClCode1"  = Cl."ClCode1"
                             AND "ClMain"."ClCode2"  = Cl."ClCode2"
                             AND "ClMain"."ClNo"     = Cl."ClNo"
      LEFT JOIN "CdCl"        ON "CdCl"."ClCode1"    = Cl."ClCode1"
                             AND "CdCl"."ClCode2"    = Cl."ClCode2"
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
                       AND Eva1."FacmNo"  =  F."FacmNo"       -- 建物鑑價金額"
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
                       AND Eva2."FacmNo"  =  F."FacmNo"       -- 土地鑑價金額"
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
                       AND Eva3."FacmNo"  =  F."FacmNo"       -- 股票鑑價金額"
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
                       AND Eva5."FacmNo"  =  F."FacmNo"       -- 其他鑑價金額 (保證)"
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
                       AND Eva9."FacmNo"  =  F."FacmNo"       -- 動產鑑價金額"
      LEFT JOIN ( SELECT A."CustNo", A."FacmNo", SUM(A."AcBal") AS "TempAmt"
                 FROM   "AcReceivable" A
                 WHERE  A."AcctCode" = 'TAV'
                   AND  A."ClsFlag"  = '0'     --未銷
                 GROUP BY A."CustNo", A."FacmNo"
               ) Tav    ON Tav."CustNo"        = F."CustNo"
                       AND Tav."FacmNo"        = F."FacmNo"   --暫收款"
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Ifrs9FacData END: INS_CNT=' || INS_CNT);

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;

--       , 0                                    AS "Rate"              -- 利率(撥款)
--       , 0                                    AS "ApplNo"            -- 核准號碼
--       , ' '                                  AS "CustId"            -- 借款人ID / 統編
--       , 0                                    AS "DrawdownFg"        -- 已核撥記號
--       , 0                                    AS "ApproveDate"       -- 核准日期(額度)
--       , 0                                    AS "UtilDeadline"      -- 動支期限
--       , 0                                    AS "FirstDrawdownDate" -- 初貸日期
--       , 0                                    AS "MaturityDate"      -- 到期日(額度)
--       , 0                                    AS "LineAmt"           -- 核准金額
--       , 0                                    AS "AcctFee"           -- 帳管費
--       , 0                                    AS "LawFee"            -- 法務費
--       , 0                                    AS "FireFee"           -- 火險費
--       , 0                                    AS "GracePeriod"       -- 初貸時約定還本寬限期
--       , 0                                    AS "AmortizedCode"     -- 契約當時還款方式(月底日)
--       , 0                                    AS "RateCode"          -- 契約當時利率調整方式(月底日)
--       , 0                                    AS "RepayFreq"         -- 契約約定當時還本週期(月底日)
--       , 0                                    AS "PayIntFreq"        -- 契約約定當時繳息週期(月底日)
--       , ' '                                  AS "IfrsStepProdCode"  -- IFRS階梯商品別
--       , ' '                                  AS "IndustryCode"      -- 授信行業別
--       , ' '                                  AS "ClTypeJCIC"        -- 擔保品類別
--       , ' '                                  AS "CityCode"          -- 擔保品地區別
--       , ' '                                  AS "AreaCode"          -- 擔保品鄉鎮區
--       , ' '                                  AS "Zip3"              -- 擔保品郵遞區號
--       , ' '                                  AS "ProdNo"            -- 商品利率代碼
--       , ' '                                  AS "AgreementFg"       -- 是否為協議商品 (Y:是 N:否)
--       , ' '                                  AS "EntCode"           -- 企金別
--       , 0                                    AS "AssetClass"        -- 資產五分類代號
--       , ' '                                  AS "IfrsProdCode"      -- 產品別
--       , 0                                    AS "EvaAmt"            -- 原始鑑價金額
--       , 0                                    AS "UtilAmt"           -- 累計撥款金額(額度層)
--       , 0                                    AS "UtilBal"           -- 已動用餘額(額度層)
--       , 0                                    AS "TotalLoanBal"      -- 本金餘額(額度層)合計
--       , 0                                    AS "RecycleCode"       -- 該筆額度是否可循環動用
--       , 0                                    AS "IrrevocableFlag"   -- 該筆額度是否為不可徹銷
--       , 0                                    AS "TempAmt"           -- 暫收款金額(台幣)
--       , ' '                                  AS "AcBookCode"        -- 帳冊別
--       , ' '                                  AS "AcSubBookCode"     -- 區隔帳冊
