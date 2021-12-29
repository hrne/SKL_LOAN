
CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB201_Upd"
(
-- 程式功能：維護 JcicB201 每月聯徵授信餘額月報資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB201_Upd"(20210531,'System');
--

    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AUTHID CURRENT_USER
AS
BEGIN
	"Usp_L8_JcicB201_Upd_Prear"();
  DECLARE
    INS_CNT        INT;         -- 新增筆數
    UPD_CNT        INT;         -- 更新筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間
    YYYYMM         INT;         -- 本月年月
    LYYYYMM        INT;         -- 上月年月
    MM             INT;         -- 本月月份
    YYYY           INT;         -- 本月年度
    YYYYMMDD       INT;         -- 本日
    OccursNum      NUMBER;
    TMNDYF         INT;         -- 本月月底日
  BEGIN
    INS_CNT := 0;
    UPD_CNT := 0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    --　本月年月
    YYYYMMDD := TBSDYF;
    YYYYMM   := TBSDYF / 100;
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

    -- 寫入資料 "Work_B201_Guarantor" 保證人/所有權人
    INSERT INTO "Work_B201_Guarantor"
    SELECT YYYYMM                              AS "DataYM"
         , G."ApplNo"                          AS "ApplNo"        -- 核准號碼
         , NVL("CustMain"."CustId",' ')        AS "CustId"        -- 保證人身份統一編號
         , ROW_NUMBER()
             Over (Partition By G."ApplNo"
                   Order     By G."ApplNo", NVL("CustMain"."CustId",' '), G."Source" )
                                               AS "ROW_NUM"       -- 序號（同一核准號碼編列流水號）
         , 0                                   AS "ApplNoCount"   -- 筆數（同一核准號碼）
         , G."Source"                          AS "Source"        -- 資料來源(1=保證人檔 2=所有權人檔)
         , CASE
             WHEN G."Source" = 1 THEN NVL(GU."GuaRelCode",' ')
             WHEN G."Source" = 2 THEN NVL(O."OwnerRelCode",' ')
             ELSE ' '
           END                                 AS "GuaRelCode"    -- 保證人關係代碼
         , CASE
             WHEN G."Source" = 1 THEN NVL(CD1."GuaRelJcic",' ')
             WHEN G."Source" = 2 THEN NVL(CD2."GuaRelJcic",' ')
             ELSE ' '
           END                                 AS "GuaRelJcic"    -- 保證人關係ＪＣＩＣ代碼
         , CASE
             WHEN G."Source" = 1 THEN NVL(GU."GuaTypeCode",' ')
             WHEN G."Source" = 2 THEN '05' -- 擔保品提供人
             ELSE ' '
           END                                 AS "GuaTypeCode"   -- 保證類別代碼
         , ' '                                 AS "GuaTypeJcic"   -- 保證類別ＪＣＩＣ代碼
    FROM (
           SELECT A."ApplNo"         AS "ApplNo"
                , A."UKey"           AS "UKey"
                , MIN(A."Source")    AS "Source"  -- (1=保證人檔 優先)
           FROM (
             -- 保證人檔
                  SELECT G."GuaUKey"                AS "UKey"
                       , NVL(G."ApproveNo",0)       AS "ApplNo"
                       , 1                          AS "Source"  -- 1=保證人檔
                  FROM   "Guarantor" G
                  WHERE  G."GuaStatCode" IN ('1')  -- 保證狀況碼 1=設定
             -- 擔保品所有權人與授信戶關係檔
                UNION
                  SELECT Owner."OwnerCustUKey"      AS "UKey"
                       , Owner."CreditSysNo"        AS "ApplNo"
                       , 2                          AS "Source"  -- 2=所有權人檔
                  FROM   "ClOwnerRelation" Owner
                ) A
           GROUP BY A."ApplNo", A."UKey"
         ) G
      LEFT JOIN "CustMain"     ON "CustMain"."CustUKey"      = G."UKey"
      LEFT JOIN "Guarantor" GU ON G."Source"     =  1
                              AND GU."ApproveNo" =  G."ApplNo"
                              AND GU."GuaUKey"   =  G."UKey"
      LEFT JOIN "CdGuarantor" CD1 ON CD1."GuaRelCode" = GU."GuaRelCode"
      LEFT JOIN "ClOwnerRelation" O
                               ON G."Source"        =  2
                              AND O."CreditSysNo"   =  G."ApplNo"
                              AND O."OwnerCustUKey" =  G."UKey"
      LEFT JOIN "CdGuarantor" CD2 ON CD2."GuaRelCode" = O."OwnerRelCode"
    ORDER BY G."ApplNo", NVL("CustMain"."CustId",' ')
      ;

    -- "Work_B201_Guarantor" ApplNoCount 統計筆數（同一核准號碼）, GuaTypeJcic 
    MERGE INTO "Work_B201_Guarantor" M
    USING ( SELECT WK."DataYM"  AS "DataYM"
                 , WK."ApplNo"  AS "ApplNo"
                 , COUNT(*)     AS "ApplNoCount"
            FROM "Work_B201_Guarantor" WK
            WHERE WK."DataYM" = YYYYMM
            GROUP BY WK."DataYM", WK."ApplNo"
          ) WK
    ON (    WK."DataYM"    = YYYYMM
        AND WK."ApplNo"    = M."ApplNo" )
    WHEN MATCHED THEN UPDATE
      SET M."ApplNoCount" = WK."ApplNoCount"
        , M."GuaTypeJcic" = CASE WHEN M."GuaTypeCode" IN ('01','02')      THEN 'G' 
                                 WHEN M."GuaTypeCode" IN ('03','04')      THEN 'N'
                                 WHEN M."GuaTypeCode" IN ('05')           THEN 'S'
                                 WHEN M."GuaTypeCode" IN ('06','07')      THEN 'C'
                                 WHEN M."GuaTypeCode" IN ('08')           THEN 'E'
                                 WHEN M."GuaTypeCode" IN ('09','10','11') THEN 'L'
                                 ELSE ' '
                            END
     ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB201');

    DELETE FROM "JcicB201"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB201');

    FOR OccursNum IN 1 .. 5
    LOOP
      INSERT INTO "JcicB201"
      SELECT
             YYYYMM                                AS "DataYM"            -- 資料年月
           , '458'                                 AS "BankItem"          -- 總行代號
           , '0001'                                AS "BranchItem"        -- 分行代號
           , 'A'                                   AS "TranCode"          -- 交易代碼 A:新增 C:異動 D:刪除
           , CASE
               WHEN FLOOR(NVL(M."DrawdownDate",0) / 100) <> YYYYMM THEN 'X'
               ELSE
                 CASE
                   WHEN R."CustNo" IS NULL THEN 'A'
                   ELSE 'C'
                 END
             END                                   AS "SubTranCode"       -- 帳號屬性註記 A:本月新增帳號 C:本月轉換帳號 X:舊有帳號
           , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."FacmNo",'000')) || TRIM(to_char(M."BormNo",'000'))
                                                   AS "AcctNo"            -- 本筆撥款帳號
           , OccursNum                             AS "SeqNo"             -- 本筆撥款帳號序號
           , 0                                     AS "TotalAmt"          -- 金額合計  (後面更新處理)
           , M."CustId"                            AS "CustId"            -- 授信戶IDN/BAN
           , ' '                                   AS "CustIdErr"         -- 上欄IDN或BAN錯誤註記
           , M."SuvId"                             AS "SuvId"             -- 負責人IDN/負責之事業體BAN
           , ' '                                   AS "SuvIdErr"          -- 上欄IDN或BAN錯誤註記
           , M."OverseasId"                        AS "OverseasId"        -- 外僑兼具中華民國國籍IDN
           , SUBSTR('000000' || TRIM(NVL(M."IndustryCode",' ')), -6)
                                                   AS "IndustryCode"      -- 授信戶行業別
           , ' '                                   AS "Filler12"          -- 空白
           , CASE
               WHEN M."Status"   IN (2, 7)         THEN 'A' -- 催收款項
               WHEN M."Status"   IN (6)            THEN 'B' -- 呆帳
               WHEN M."AcctCode" IN ('310')        THEN 'E' -- 其他短期放款
               WHEN M."AcctCode" IN ('320')        THEN 'H' -- 中期放款
               WHEN M."AcctCode" IN ('330', '340') THEN 'I' -- 長期放款
               ELSE  'I'                                    -- 長期放款
             END                                   AS "AcctCode"          -- 科目別
           , CASE
               WHEN FLOOR(NVL(M."DispDate",0) / 100) > 0
                AND FLOOR(NVL(M."DispDate",0) / 100) <= YYYYMM THEN 'X'
               ELSE 'S'
             END                                   AS "SubAcctCode"       -- 科目別註記 S:有十足擔保 X:無前述情形者
           , CASE
               WHEN M."Status" NOT IN (2, 6, 7) THEN ' '
               ELSE
                 CASE
                   WHEN M."OrigAcctCode" IN ('310')        THEN 'E' -- 其他短期放款
                   WHEN M."OrigAcctCode" IN ('320')        THEN 'H' -- 中期放款
                   WHEN M."OrigAcctCode" IN ('330', '340') THEN 'I' -- 長期放款
                   ELSE  'I'                                        -- 長期放款
                 END
             END                                   AS "OrigAcctCode"      -- 轉催收款(或呆帳)前原科目別
           , CASE
               WHEN M."ClCode1" IN (9) AND M."ClCode2" IN (1) THEN 'Y' -- 車貸
               WHEN M."EntCode" IN ('1') THEN 'X'  -- 企金
               ELSE 'N'
             END                                   AS "ConsumeFg"         -- 個人消費性貸款註記  Y:個人消費性貸款 N:個人非消費性貸款 X:非屬個人貸款
           , M."FinCode"                           AS "FinCode"           -- 融資分類  法人:K非屬前述特殊融資之其他一般法人金融貸款
                                                                          --           個人:M購買住宅貸款(自用) 1個人投資理財貸款
           , NVL(M."ProjCode",' ')                 AS "ProjCode"          -- 政府專案補助貸款分類
           , NVL(M."NonCreditCode",' ')            AS "NonCreditCode"     -- 不計入授信項目
           , CASE
               WHEN M."UsageCode" IN ('02','03','04','09') THEN '1'  --2: 購置不動產 3: 營業用資產 4: 固定資產
               WHEN M."UsageCode" IN ('06') THEN '2'                 --6: 購置動產
               WHEN M."UsageCode" IN ('05') THEN '3'                 --5: 企業投資
               WHEN M."UsageCode" IN ('01') THEN '4'                 --1: 週轉金
               ELSE '1'
             END                                   AS "UsageCode"         -- 用途別 1:購置不動產 2:購置動產 3:企業投資 4:週轉金
           , NVL(M."StoreRate",0)                  AS "ApproveRate"       -- 本筆撥款利率
--         , CASE WHEN NVL(R."StoreRate",0) = 0 THEN L."StoreRate"
--                ELSE NVL(R."StoreRate",0)
--           END                                   AS "ApproveRate"       -- 本筆撥款利率
           , CASE
               WHEN FLOOR(NVL(M."DrawdownDate",0) / 100) < 191100 THEN FLOOR(NVL(M."DrawdownDate",0) / 100)
               ELSE FLOOR(NVL(M."DrawdownDate",0) / 100) - 191100
             END                                   AS "DrawdownDate"      -- 本筆撥款開始年月
           , CASE
               WHEN FLOOR(NVL(M."MaturityDate",0) / 100) < 191100 THEN FLOOR(NVL(M."MaturityDate",0) / 100)
               ELSE FLOOR(NVL(M."MaturityDate",0) / 100) - 191100
             END                                   AS "MaturityDate"      -- 本筆撥款約定清償年月
           , M."CurrencyCode"                      AS "CurrencyCode"      -- 授信餘額幣別
           , 0                                     AS "DrawdownAmt"       -- 訂約金額(台幣)   -- 共用額度填報0
           , 0                                     AS "DrawdownAmtFx"     -- 訂約金額(外幣)
           , CASE
               WHEN M."RecycleCode" IN ('1') THEN 'Y'
               WHEN M."RecycleCode" IN ('0') THEN 'N'
               ELSE ' '
             END                                   AS "RecycleCode"       -- 循環信用註記 'Y':是 'N':否
           , CASE
               WHEN M."IrrevocableFlag" IN ('N') THEN 'Y'
               WHEN M."IrrevocableFlag" IN ('Y') THEN 'N'
               ELSE 'Y'
             END                                   AS "IrrevocableFlag"   -- 額度可否撤銷 'Y':可撤銷 'N':不可撤銷
           , CASE
               WHEN M."Status" IN (6)  THEN LPAD('9', 50, '9')  -- 呆帳
               ELSE TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."FacmNo",'000'))
             END                                   AS "FacmNo"            -- 上階共用額度控制編碼
           , CASE
               WHEN M."Status" IN (2, 6, 7) THEN 0   -- 催呆
               WHEN NVL(M."LoanBal",0) = 0  THEN 0
               WHEN ROUND( NVL(M."LoanBal",0) / 1000, 0) = 0 THEN 1
               ELSE ROUND( NVL(M."LoanBal",0) / 1000, 0)
             END                                   AS "UnDelayBal"        -- 未逾期/乙類逾期/應予觀察授信餘額(台幣千元)
           , 0                                     AS "UnDelayBalFx"      -- 未逾期/乙類逾期/應予觀察授信餘額(外幣)
           , CASE
               WHEN M."Status" NOT IN (2, 7) THEN 0   -- 非催收
               ELSE                                   -- 催收 (催收餘額)
                 CASE WHEN NVL(M."LoanBal",0) = 0                   THEN 0
                      WHEN TRUNC( NVL(M."LoanBal",0) / 1000, 0) = 0 THEN 1
                      ELSE TRUNC( NVL(M."LoanBal",0) / 1000, 0)
                 END
             END                                   AS "DelayBal"          -- 逾期未還餘額（台幣千元）  (後面再更新)
           , 0                                     AS "DelayBalFx"        -- 逾期未還餘額（外幣）
           , '9'                                   AS "DelayPeriodCode"   -- 逾期期限  (先視為未逾期，後面再判斷更新)
           , ' '                                   AS "RepayCode"         -- 本月還款紀錄  (後面再更新)
--         , ROUND((NVL(M."PrevAmt",0) + NVL(M."IntAmt",0)) / 1000, 3)
--                                                 AS "PayAmt"            -- 本月（累計）應繳金額
           , CASE WHEN M."Status" IN (3, 5, 9) THEN 0   -- 結案:0
                  WHEN M."Status" IN (6)       THEN 0   -- 呆帳:0
                  WHEN M."Status" IN (2, 7)    THEN 0   -- 催收:轉催收本金+利息 (後面再更新)
                  ELSE ROUND(NVL(L."DueAmt",0) / 1000, 3)
             END                                   AS "PayAmt"            -- 本月（累計）應繳金額  (ref:M3102 4)
           , ROUND(NVL(M."PrevAmtRcv",0) / 1000,3) AS "Principal"         -- 本月收回本金
           , ROUND(NVL(M."IntAmtRcv",0) / 1000,3)  AS "Interest"          -- 本月收取利息
           , ROUND(NVL(M."FeeAmtRcv",0) / 1000,3)  AS "Fee"               -- 本月收取其他費用
           , 'X'                                   AS "FirstDelayCode"    -- 甲類逾期放款分類 (催收後面再更新)
           , CASE
               WHEN M."Status"   IN (0, 4) AND M."RepayDelayMon" IN (1,2,3) THEN '1' -- 正常戶逾期未滿3個月
               WHEN M."Status"   IN (0, 4) AND M."RepayDelayMon" IN (4,5,6) THEN '2' -- 正常戶逾期未滿6個月
               ELSE 'X'
             END                                    AS "SecondDelayCode"  -- 乙類逾期放款分類 (催收後面再更新)
           , CASE
               WHEN M."Status"   IN (2, 7)         THEN 'C' -- 催收款項
               WHEN M."Status"   IN (6)            THEN 'B' -- 呆帳
               WHEN M."Status"   IN (3, 5, 9)  AND NVL(Tx."CaseCloseCode",0) = '4'  THEN 'P'  -- 結案戶
               WHEN M."Status"   IN (3, 5, 9)  AND NVL(Tx."CaseCloseCode",0) = '5'  THEN 'Q'
               WHEN M."Status"   IN (3, 5, 9)  AND NVL(Tx."CaseCloseCode",0) = '6'  THEN 'D'
               ELSE ' '
             END                                   AS "BadDebtCode"       -- 不良債權處理註記 (B:轉銷呆帳 C:轉催收款) (ref:M3102,51)
           , CASE
               WHEN M."Status" NOT IN (3, 5, 9) THEN ' '  -- 非結案
               ELSE
                 CASE                                     -- 結案
                   WHEN M."RecycleCode" IN ('1')  AND  NVL(M."RecycleDeadline",0) <  YYYYMMDD THEN '3'  -- 先判斷循環額度及循環動用期限
                   WHEN M."RecycleCode" IN ('1')  AND  NVL(M."RecycleDeadline",0) >= YYYYMMDD THEN ' '  -- 先判斷循環額度及循環動用期限
                   WHEN NVL(Tx."CaseCloseCode",0) = '0'  THEN '3'
                   WHEN NVL(Tx."CaseCloseCode",0) = '4'  THEN 'P'
                   WHEN NVL(Tx."CaseCloseCode",0) = '5'  THEN 'Q'
                   WHEN NVL(Tx."CaseCloseCode",0) = '6'  THEN 'D'
                   ELSE  '3'
                 END
             END                                   AS "NegStatus"         -- 債權結束註記  (ref:M3102,54)
           , ' '            AS "NegCreditor"       -- 債權處理後新債權人ID/債權轉讓後前手債權人ID/信保基金退理賠信用保證機構BAN
           , ' '            AS "NegNo"             -- 債權處理案號
           , ' '            AS "NegTransYM"        -- 債權轉讓年月/債權轉讓後原債權機構買回年月
           , ' '                                   AS "Filler443"         -- 空白
           , M."ClType"                            AS "ClType"            -- 擔保品組合型態
           , ROUND(NVL(M."EvaAmt",0) / 1000, 0)    AS "ClEvaAmt"          -- 擔保品(合計)鑑估值
           , NVL(M."ClTypeCode",' ')               AS "ClTypeCode"        -- 擔保品類別(JCIC)
           , CASE
               WHEN M."SyndNo" > 0 THEN 'A'
               ELSE ' '
             END                                   AS "SyndKind"          -- 國內或國際聯貸 A:國內 B:國際; 如非屬聯貸案填空白，勿填0
           , CASE
               WHEN M."SyndNo" > 0 THEN TRIM(to_char(M."SyndNo",'00000000'))
               ELSE ' '
             END                                   AS "SyndContractDate"  -- 聯貸合約訂定日期
           , CASE
               WHEN NVL(M."SyndAmt",0) = 0 THEN 0
               ELSE ROUND( NVL(M."PartAmt",0) / NVL(M."SyndAmt",0) * 100, 2)
             END                                   AS "SyndRatio"         -- 聯貸參貸比例
           , ' '                                   AS "Filler51"          -- 空白
           , ' '                                   AS "Filler52"          -- 空白
           , 'N'                                   AS "PayablesFg"        -- 代放款註記
           , CASE
               WHEN N."CustNo" IS NOT NULL THEN 'Y'
               ELSE 'N'
             END                                   AS "NegFg"             -- 債務協商註記
           , 'N'                                   AS "Filler533"         -- (109年新增)無擔保貸款......
           , NVL(WK1."GuaTypeJcic",' ')            AS "GuaTypeCode1"      -- 共同債務人或債務關係人身份代號1
           , NVL(WK1."CustId",' ')                 AS "GuaId1"            -- 共同債務人或債務關係人身份統一編號1
           , ' '                                   AS "GuaIdErr1"         -- 上欄IDN或BAN錯誤註記
           , NVL(WK1."GuaRelJcic",' ')             AS "GuaRelCode1"       -- 與主債務人關係1
           , NVL(WK2."GuaTypeJcic",' ')            AS "GuaTypeCode2"      -- 共同債務人或債務關係人身份代號2
           , NVL(WK2."CustId",' ')                 AS "GuaId2"            -- 共同債務人或債務關係人身份統一編號2
           , ' '                                   AS "GuaIdErr2"         -- 上欄IDN或BAN錯誤註記
           , NVL(WK2."GuaRelJcic",' ')             AS "GuaRelCode2"       -- 與主債務人關係2
           , NVL(WK3."GuaTypeJcic",' ')            AS "GuaTypeCode3"      -- 共同債務人或債務關係人身份代號3
           , NVL(WK3."CustId",' ')                 AS "GuaId3"            -- 共同債務人或債務關係人身份統一編號3
           , ' '                                   AS "GuaIdErr3"         -- 上欄IDN或BAN錯誤註記
           , NVL(WK3."GuaRelJcic",' ')             AS "GuaRelCode3"       -- 與主債務人關係3
           , NVL(WK4."GuaTypeJcic",' ')            AS "GuaTypeCode4"      -- 共同債務人或債務關係人身份代號4
           , NVL(WK4."CustId",' ')                 AS "GuaId4"            -- 共同債務人或債務關係人身份統一編號4
           , ' '                                   AS "GuaIdErr4"         -- 上欄IDN或BAN錯誤註記
           , NVL(WK4."GuaRelJcic",' ')             AS "GuaRelCode4"       -- 與主債務人關係4
           , NVL(WK5."GuaTypeJcic",' ')            AS "GuaTypeCode5"      -- 共同債務人或債務關係人身份代號5
           , NVL(WK5."CustId",' ')                 AS "GuaId5"            -- 共同債務人或債務關係人身份統一編號5
           , ' '                                   AS "GuaIdErr5"         -- 上欄IDN或BAN錯誤註記
           , NVL(WK5."GuaRelJcic",' ')             AS "GuaRelCode5"       -- 與主債務人關係5
           , ' '                                   AS "Filler741"         -- 空白
           , ' '                                   AS "Filler742"         -- 空白
           , CASE
               WHEN M."CustNo" IN (0269376) AND M."FacmNo" IN (003) AND M."BormNo" IN (001) THEN 09912  -- 特例 (ref: LN15H1)
               WHEN TRUNC(NVL(M."BadDebtDate",0) / 100) < 191100 THEN TRUNC(NVL(M."BadDebtDate",0) / 100)
               ELSE TRUNC(NVL(M."BadDebtDate",0) / 100) - 191100
             END                                   AS "BadDebtDate"       -- 呆帳轉銷年月
           , 'X'                                   AS "SyndCode"          -- 聯貸主辦(管理)行註記
           , 0                                     AS "BankruptDate"      -- 破產宣告日(或法院裁定開始清算日)  --???
           , 'N'                                   AS "BdLoanFg"          -- 建築貸款註記
           , 0                                     AS "SmallAmt"          -- 授信餘額列報1（千元）之原始金額（元） --（後續更新處理）
           , ' '                                   AS "ExtraAttrCode"     -- 補充揭露案件註記－案件屬性
           , ' '                                   AS "ExtraStatusCode"   -- 補充揭露案件註記－案件情形
           , ' '                                   AS "Filler74A"         -- 空白
           , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月
           , CASE WHEN WK5."ApplNo" IS NULL THEN 'Y'
                  WHEN WK5."ApplNoCount" <= OccursNum * 5 THEN 'Y'
                  ELSE 'N'
             END                                   AS "DataEnd"           -- 資料結束註記
           , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
           , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
           , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
           , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
      FROM   "JcicMonthlyLoanData" M
          LEFT JOIN "FacMain" F   ON F."CustNo"   = M."CustNo"
                                 AND F."FacmNo"   = M."FacmNo"
          LEFT JOIN "LoanBorMain" L     ON L."CustNo"   = M."CustNo"
                                       AND L."FacmNo"   = M."FacmNo"
                                       AND L."BormNo"   = M."BormNo"
          LEFT JOIN ( SELECT  R."CustNo"
                           ,  R."NewFacmNo"
                           ,  R."NewBormNo"
                           ,  ROW_NUMBER() Over (Partition By R."CustNo", R."NewFacmNo",  R."NewBormNo"
                                                     Order By R."CustNo", R."NewFacmNo",  R."NewBormNo")
                                   AS ROW_NO
                      FROM  "AcLoanRenew" R
                    ) R            ON R."CustNo"    = M."CustNo"
                                  AND R."NewFacmNo" = M."FacmNo"
                                  AND R."NewBormNo" = M."BormNo"
                                  AND R.ROW_NO      = 1
          LEFT JOIN ( SELECT  Tx."CustNo"
                           ,  Tx."FacmNo"
                           ,  Tx."BormNo"
                           ,  Tx."TitaTxCd"
                           ,  Tx."TitaHCode"
                           ,  Tx."AcDate"
                           ,  JSON_VALUE  (Tx."OtherFields", '$.CaseCloseCode') AS "CaseCloseCode"
                           ,  ROW_NUMBER() Over (Partition By Tx."CustNo", Tx."FacmNo", Tx."BormNo"
                                                     Order By Tx."CustNo", Tx."FacmNo", Tx."BormNo", Tx."BorxNo" DESC)
                                   AS ROW_NO
                      FROM  "LoanBorTx" Tx                                   -- 結案戶
                      WHERE Tx."TitaTxCd"  IN ('L3410', 'L3420')
                        AND Tx."TitaHCode" IN ('0')
                        AND TRUNC( NVL(Tx."AcDate",0) / 100) = YYYYMM
                        AND JSON_VALUE  (Tx."OtherFields", '$.CaseCloseCode') IN (0, 4, 5, 6)
                    ) Tx           ON Tx."CustNo"   = M."CustNo"
                                  AND Tx."FacmNo"   = M."FacmNo"
                                  AND Tx."BormNo"   = M."BormNo"
                                  AND Tx.ROW_NO     = 1   -- 撈最後一筆
          LEFT JOIN ( SELECT  N."CustNo"
                           ,  N."CaseSeq"
                           ,  ROW_NUMBER() Over (Partition By "CustNo"
                                                 Order By "CaseSeq" DESC)
                                              AS ROW_NO
                      FROM  "NegMain" N
                     WHERE  N."CaseKindCode" = '1'    --案件種類-債協
                       AND  N."Status"    = '0'       --戶況正常
                    ) N            ON N."CustNo"    = M."CustNo"
                                  AND N.ROW_NO      = 1
          LEFT JOIN "Work_B201_Guarantor" WK1  ON WK1."DataYM"   = M."DataYM"
                                              AND WK1."ROW_NUM"  = OccursNum * 5 - 4
                                              AND WK1."ApplNo"   = F."ApplNo"
          LEFT JOIN "Work_B201_Guarantor" WK2  ON WK2."DataYM"   = M."DataYM"
                                              AND WK2."ROW_NUM"  = OccursNum * 5 - 3
                                              AND WK2."ApplNo"   = F."ApplNo"
          LEFT JOIN "Work_B201_Guarantor" WK3  ON WK3."DataYM"   = M."DataYM"
                                              AND WK3."ROW_NUM"  = OccursNum * 5 - 2
                                              AND WK3."ApplNo"   = F."ApplNo"
          LEFT JOIN "Work_B201_Guarantor" WK4  ON WK4."DataYM"   = M."DataYM"
                                              AND WK4."ROW_NUM"  = OccursNum * 5 - 1
                                              AND WK4."ApplNo"   = F."ApplNo"
          LEFT JOIN "Work_B201_Guarantor" WK5  ON WK5."DataYM"   = M."DataYM"
                                              AND WK5."ROW_NUM"  = OccursNum * 5 - 0
                                              AND WK5."ApplNo"   = F."ApplNo"
      WHERE  M."DataYM"     =  YYYYMM
        AND  ( ( OccursNum = 1 ) OR
               ( OccursNum > 1  AND WK1."ApplNo" IS NOT NULL )
             )
        AND  NVL(M."BadDebtSkipFg",' ') NOT IN ('Y')  -- 呆帳不報送記號
--        AND  ( ( M."Status" IN (0, 1, 2, 4, 6, 7) ) OR
--               ( M."Status" IN (3, 5, 9)  AND  M."UtilAmt" = 0  AND Tx."CustNo" IS NOT NULL  )
        AND  ( ( M."Status" IN (0, 2, 6 , 7) ) OR
               ( M."Status" IN (3, 5)  AND  M."UtilAmt" = 0  AND Tx."CustNo" IS NOT NULL  )
             )
        ;
    END LOOP;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB201 END: INS_CNT=' || INS_CNT);


-- memo: WHEN greatest(B."IntDelayMon", B."RepayDelayMon", B."RepaidEndMon") =   0 THEN ...

-- 更新 DelayPeriodCode 逾期期限
    DBMS_OUTPUT.PUT_LINE('UPDATE DelayPeriodCode 逾期期限');

    MERGE INTO "JcicB201" M
    USING ( SELECT M."DataYM"
                 , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."FacmNo",'000')) || TRIM(to_char(M."BormNo",'000')) AS "AcctNo"
                 , M."Status"               AS "Status"
                 , NVL(M."IntDelayMon",0)   AS "IntDelayMon"
                 , NVL(M."RepayDelayMon",0) AS "RepayDelayMon"
                 , NVL(M."RepaidEndMon",0)  AS "RepaidEndMon"
                 , CASE WHEN NVL(M."OvduDate", 0) <  19110000
                          OR NVL(M."OvduDate", 0) >= TMNDYF THEN 0
                        ELSE TRUNC(months_between(TO_DATE(TMNDYF,'yyyy-mm-dd'), TO_DATE(M."OvduDate",'yyyy-mm-dd')))
                   END                      AS "OvduMon"       -- 催收月數
                 , CASE WHEN NVL(M."BadDebtDate", 0) <  19110000
                          OR NVL(M."BadDebtDate", 0) >= TMNDYF THEN 0
                        ELSE TRUNC(months_between(TO_DATE(TMNDYF,'yyyy-mm-dd'), TO_DATE(M."BadDebtDate",'yyyy-mm-dd')))
                   END                      AS "BadDebtMon"    -- 呆帳月數
            FROM "JcicMonthlyLoanData" M
            WHERE M."DataYM" = YYYYMM
          ) B
    ON (    M."DataYM"    =  YYYYMM
        AND M."AcctNo"    =  B."AcctNo"
        AND B."Status"    IN (2, 6, 7)   -- 催收, 呆帳
       )
    WHEN MATCHED THEN UPDATE     -- 皆以催收日計算
     SET M."DelayPeriodCode" = CASE WHEN B."OvduMon" >= 18                     THEN '4'  -- 催收日>18月 : 逾期兩年以上
                                    WHEN B."OvduMon" >= 6 AND B."OvduMon" < 18 THEN '3'  -- 催收6~18月  : 逾期1~2年
                                    ELSE '2'                                             -- 逾期6月~1年
                               END
--   SET M."DelayPeriodCode" = CASE WHEN B."IntDelayMon" =   0 THEN '9'   -- 以下次繳息日判斷
--                                  WHEN B."IntDelayMon" <   3 THEN '0'
--                                  WHEN B."IntDelayMon" <   6 THEN '1'
--                                  WHEN B."IntDelayMon" <  12 THEN '2'
--                                  WHEN B."IntDelayMon" <  24 THEN '3'
--                                  WHEN B."IntDelayMon" >= 24 THEN '4'
--                                  ELSE '9'
--                             END
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE DelayPeriodCode 逾期期限 END');


-- 本月還款紀錄
    DBMS_OUTPUT.PUT_LINE('UPDATE RepayCode 本月還款紀錄');

    MERGE INTO "JcicB201" M
    USING ( SELECT M."DataYM"
                 , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."FacmNo",'000')) || TRIM(to_char(M."BormNo",'000')) AS "AcctNo"
                 , NVL(M."PrevPayIntDate",0)   AS "PrevPayIntDate"
                 , NVL(M."PrevRepaidDate",0)   AS "PrevRepaidDate"
                 , NVL(M."NextPayIntDate",0)   AS "NextPayIntDate"
                 , NVL(M."NextRepayDate",0)    AS "NextRepayDate"
                 , NVL(M."IntDelayMon",0)      AS "IntDelayMon"
                 , NVL(M."RepayDelayMon",0)    AS "RepayDelayMon"
            FROM "JcicMonthlyLoanData" M
            WHERE M."DataYM" = YYYYMM ) B
    ON (    M."DataYM"    = YYYYMM
        AND M."AcctNo"    = B."AcctNo"
       )
    WHEN MATCHED THEN UPDATE
      SET M."RepayCode" = CASE
                            WHEN ( B."PrevPayIntDate" / 100 ) < YYYYMM AND ( B."PrevRepaidDate" / 100 ) < YYYYMM
                             AND ( B."NextPayIntDate" / 100 ) > YYYYMM AND ( B."NextRepayDate"  / 100 ) > YYYYMM  THEN '0'
                            WHEN B."IntDelayMon" =   0 THEN '0'
                            WHEN B."IntDelayMon" =   1 THEN '1'
                            WHEN B."IntDelayMon" =   2 THEN '2'
                            WHEN B."IntDelayMon" =   3 THEN '3'
                            WHEN B."IntDelayMon" =   4 THEN '4'
                            WHEN B."IntDelayMon" =   5 THEN '5'
                            WHEN B."IntDelayMon" >=  6 THEN '6'
                            ELSE '0'
                          END
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE RepayCode 本月還款紀錄 END');


-- 催收：更新 FirstDelayCode 甲類逾期放款分類 , SecondDelayCode 乙類逾期放款分類 (ref: LNSP15A-LN15G1 (#3102 47 1) (#3102 50 1))
    DBMS_OUTPUT.PUT_LINE('UPDATE 催收 FirstDelayCode SecondDelayCode');

    MERGE INTO "JcicB201" M
    USING ( SELECT M."DataYM"
                 , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."FacmNo",'000')) || TRIM(to_char(M."BormNo",'000')) AS "AcctNo"
                 , M."Status"               AS "Status"
                 , M."MaturityDate"         AS "MaturityDate"    -- 本筆撥款約定清償年月
                 , O."OvduDate"             AS "OvduDate"        -- 轉催收日期
                 , NVL(M."IntDelayMon",0)   AS "IntDelayMon"
                 , NVL(M."RepayDelayMon",0) AS "RepayDelayMon"
                 , NVL(M."RepaidEndMon",0)  AS "RepaidEndMon"
                 , L1."LegalProg"           AS "LegalProg1"
                 , L2."LegalProg"           AS "LegalProg2"
                 , NVL(O."OvduAmt",0)       AS "OvduAmt"
            FROM "JcicMonthlyLoanData" M
              LEFT JOIN "LoanOverdue" O  ON O."CustNo"   =  M."CustNo"
                                        AND O."FacmNo"   =  M."FacmNo"
                                        AND O."BormNo"   =  M."BormNo"
                                        AND O."Status"   IN (1, 2)  -- 催收, 部分轉呆
              LEFT JOIN ( SELECT L."CustNo"
                               , L."FacmNo"
                               , L."AcDate"
                               , L."LegalProg"
                               , ROW_NUMBER() Over (Partition By L."CaseCode", L."CustNo", L."FacmNo"
                                              Order By L."CaseCode", L."CustNo", L."FacmNo", L."AcDate" DESC )  AS ROW_NO
                          FROM   "CollLaw" L
                          WHERE  L."CaseCode"   =  '1'
                            AND  TRUNC(L."AcDate" / 100 ) <= YYYYMM
                            AND  L."LegalProg"  IN ('56', '58')       -- 催繳情形代碼56,58
                        ) L1     ON L1."CustNo"     =  M."CustNo"
                                AND L1."FacmNo"     =  M."FacmNo"
                                AND L1.ROW_NO       =  1
              LEFT JOIN ( SELECT L."CustNo"
                               , L."FacmNo"
                               , L."AcDate"
                               , L."LegalProg"
                               , ROW_NUMBER() Over (Partition By L."CaseCode", L."CustNo", L."FacmNo"
                                              Order By L."CaseCode", L."CustNo", L."FacmNo", L."AcDate" DESC )  AS ROW_NO
                          FROM   "CollLaw" L
                          WHERE  L."CaseCode"   =  '1'
                            AND  TRUNC(L."AcDate" / 100 ) <= YYYYMM
                            AND  L."LegalProg"  IN ('60')             -- 催繳情形代碼60
                        ) L2     ON L2."CustNo"     =  M."CustNo"
                                AND L2."FacmNo"     =  M."FacmNo"
                                AND L2.ROW_NO       =  1
            WHERE M."DataYM"    =  YYYYMM
              AND M."Status"    IN (2, 7)   -- 催收, 部分轉呆
          ) B
    ON (    M."DataYM"    =  YYYYMM
        AND M."AcctNo"    =  B."AcctNo"
       )
    WHEN MATCHED THEN UPDATE
      SET M."PayAmt"         =  ROUND(NVL(B."OvduAmt",0) / 1000, 3)      -- 本月（累計）應繳金額 (催收)
        , M."FirstDelayCode" =  CASE WHEN B."LegalProg2" IS NOT NULL THEN '3'
                                     WHEN B."LegalProg1" IS NOT NULL THEN 'X'
                                     WHEN TRUNC( NVL(B."OvduDate",0) / 100 ) <= YYYYMM THEN '3'  -- 催收日年月份 <= 年月份
                                     WHEN TRUNC( to_char(add_months( TO_DATE(B."MaturityDate",'yyyy-mm-dd'), 3), 'YYYYMMDD') / 100 ) <= YYYYMM THEN '1'  -- 到期日+3個月 <= 年月份
                                     --WHEN TRUNC( NVL(B."OvduDate",0) / 100 ) <= YYYYMM THEN '3'  -- 催收日年月份 <= 年月份
                                     ELSE 'X'
                                END
        , M."SecondDelayCode" = CASE WHEN B."LegalProg2" IS NOT NULL THEN 'X'
                                     WHEN B."LegalProg1" IS NOT NULL THEN '5'
                                     WHEN TRUNC( NVL(B."OvduDate",0) / 100 ) <= YYYYMM THEN 'X'  -- 催收日年月份 <= 年月份
                                     WHEN TRUNC( to_char(add_months( TO_DATE(B."MaturityDate",'yyyy-mm-dd'), 3), 'YYYYMMDD') / 100 ) <= YYYYMM THEN 'X'  -- 到期日+3個月 <= 年月份
                                     --WHEN TRUNC( NVL(B."OvduDate",0) / 100 ) <= YYYYMM THEN 'X'  -- 催收日年月份 <= 年月份
                                     ELSE 'X'
                                END
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE 催收 FirstDelayCode SecondDelayCode END');

-- 催收：更新 SecondDelayCode 乙類逾期放款分類＝其他 (ref: LNSP15A-LN15G1 (#3102 50 1))
    DBMS_OUTPUT.PUT_LINE('UPDATE 催收 SecondDelayCode=其他');

    UPDATE "JcicB201" M
    SET   M."SecondDelayCode" = '7'
    WHERE M."DataYM"          = YYYYMM
      AND M."AcctCode"        = 'A'
      AND M."FirstDelayCode"  = 'X'
      AND M."SecondDelayCode" = 'X'
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE 催收 SecondDelayCode=其他 END');


-- 催收：更新 UnDelayBal DelayBal (ref: LNSP15A-LN15G1 (#3101 213 10) (#3101 233 10))
    DBMS_OUTPUT.PUT_LINE('UPDATE 催收 SecondDelayCode=其他');

    UPDATE "JcicB201" M
    SET   M."UnDelayBal" = M."DelayBal"
        , M."DelayBal"   = 0
    WHERE M."DataYM"          =  YYYYMM
      AND M."AcctCode"        =  'A'
      AND M."SecondDelayCode" IN ('1','2','3','4','5','6','7')
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('催收：更新 UnDelayBal DelayBal END');


-- 呆帳：更新 DelayBal 逾期未還餘額（台幣） -- 比照 LN15H1 撈交易明細檔
    DBMS_OUTPUT.PUT_LINE('UPDATE 呆帳：更新 DelayBal 逾期未還餘額 ');

    MERGE INTO "JcicB201" M
    USING ( SELECT Tx."CustNo"
                 , Tx."FacmNo"
                 , Tx."BormNo"
                 , TRIM(to_char(Tx."CustNo",'0000000')) || TRIM(to_char(Tx."FacmNo",'000')) || TRIM(to_char(Tx."BormNo",'000')) AS "AcctNo"
                 , Tx."TitaTxCd"
                 , Tx."TitaHCode"
                 , Tx."AcDate"
                 , JSON_VALUE  (Tx."OtherFields", '$.CaseCloseCode') AS "CaseCloseCode"
                 , Tx."TxAmt"
                 , Tx."Principal"
                 , Tx."Interest"
                 , ROW_NUMBER() Over (Partition By Tx."CustNo", Tx."FacmNo", Tx."BormNo"
                                          Order By Tx."CustNo", Tx."FacmNo", Tx."BormNo", Tx."BorxNo" DESC)
                                AS ROW_NO
            FROM  "LoanBorTx" Tx
            WHERE Tx."TitaTxCd"  IN ('L3420')
              AND Tx."TitaHCode" IN ('0')
              AND JSON_VALUE  (Tx."OtherFields", '$.CaseCloseCode') IN (7)   -- 呆帳
          ) B
    ON (    M."DataYM"    =  YYYYMM
        AND M."AcctNo"    =  B."AcctNo"
        AND M."AcctCode"  =  'B' -- 呆帳
        AND B.ROW_NO      =  1   -- 撈最後一筆
       )
    WHEN MATCHED THEN UPDATE
      SET M."DelayBal" =  CASE
                            WHEN NVL(B."Principal",0) = 0  THEN 0
                            WHEN TRUNC( NVL(B."Principal",0) / 1000, 0) = 0 THEN 1
                            ELSE TRUNC( NVL(B."Principal",0) / 1000, 0)
                          END
        , M."SmallAmt" =  CASE
                            WHEN M."AcctNo" IN ('0234349001002') THEN 1   -- 特例 (ref: LN15H1)
                            WHEN NVL(B."Principal",0) <= 1499   THEN NVL(B."Principal",0)
                            ELSE 0
                          END
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE 呆帳：更新 DelayBal 逾期未還餘額 END');

---- 呆帳：更新 DelayBal 逾期未還餘額（台幣）
--    DBMS_OUTPUT.PUT_LINE('UPDATE 呆帳：更新 DelayBal 逾期未還餘額 ');
--
--    MERGE INTO "JcicB201" M
--    USING ( SELECT M."DataYM"
--                 , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."FacmNo",'000')) || TRIM(to_char(M."BormNo",'000')) AS "AcctNo"
--                 , SUM( NVL(O."BadDebtBal",0) )  AS "BadDebtBal"
--            FROM "JcicMonthlyLoanData" M
--              LEFT JOIN "LoanOverdue" O  ON O."CustNo"   =  M."CustNo"
--                                        AND O."FacmNo"   =  M."FacmNo"
--                                        AND O."BormNo"   =  M."BormNo"
--                                        AND O."Status"   IN (3)  -- 呆帳
--            WHERE M."DataYM"    =  YYYYMM
--              AND M."Status"    IN (6)   -- 呆帳
--            GROUP BY M."DataYM", M."CustNo", M."FacmNo", M."BormNo"
--          ) B
--    ON (    M."DataYM"    =  YYYYMM
--        AND M."AcctNo"    =  B."AcctNo"
--       )
--    WHEN MATCHED THEN UPDATE
--      SET M."DelayBal" =  CASE
--                            WHEN NVL(B."BadDebtBal",0) = 0  THEN 0
--                            WHEN TRUNC( NVL(B."BadDebtBal",0) / 1000, 0) = 0 THEN 1
--                            ELSE TRUNC( NVL(B."BadDebtBal",0) / 1000, 0)
--                          END
--        , M."SmallAmt" =  CASE
--                            WHEN M."AcctNo" IN ('0234349001002') THEN 1   -- 特例 (ref: LN15H1)
--                            WHEN NVL(B."BadDebtBal",0) <= 1499   THEN NVL(B."BadDebtBal",0)
--                            ELSE 0
--                          END
--      ;
--
--    UPD_CNT := UPD_CNT + sql%rowcount;
--    DBMS_OUTPUT.PUT_LINE('UPDATE 呆帳：更新 DelayBal 逾期未還餘額 END');


-- 更新 SmallAmt 授信餘額列報1（千元）之原始金額（元）
    DBMS_OUTPUT.PUT_LINE('UPDATE SmallAmt ');

    MERGE INTO "JcicB201" M
    USING ( SELECT M."DataYM"
                 , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."FacmNo",'000')) || TRIM(to_char(M."BormNo",'000')) AS "AcctNo"
                 , M."LoanBal"
            FROM "JcicMonthlyLoanData" M
            WHERE M."DataYM" = YYYYMM
              AND M."Status" NOT IN (6)   -- 非呆帳 (呆帳撈轉呆金額，已經提前處理)
          ) B
    ON (    M."DataYM"    = YYYYMM
        AND M."AcctNo"    = B."AcctNo" )
    WHEN MATCHED THEN UPDATE
      SET M."SmallAmt" = CASE
                           WHEN M."UnDelayBal" = 1 OR M."DelayBal" = 1 THEN B."LoanBal"
                           ELSE 0
                         END
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE SmallAmt END');


-- 更新 TotalAmt 金額合計
    DBMS_OUTPUT.PUT_LINE('UPDATE TotalAmt ');

    UPDATE "JcicB201" M
    SET   M."TotalAmt" = M."DrawdownAmt" + M."DrawdownAmtFx"
                       + M."UnDelayBal"  + M."UnDelayBalFx"
                       + M."DelayBal"    + M."DelayBalFx"
    WHERE M."DataYM" = YYYYMM
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE TotalAmt END');


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;
