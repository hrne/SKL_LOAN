-- 程式功能：維護 JcicMonthlyLoanData 聯徵放款月報資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicMonthlyLoanData_Upd"(20200420,'999999');
--

-- 保證人檔
DROP TABLE "Work_Guar" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_Guar"
    (  "ApproveNo"   decimal(7, 0)   default 0 not null    -- 核准號碼
     , "ROW_NUM"     decimal(2, 0)   default 0 not null    -- 序號
     , "GuaUKey"     varchar2(32)                          -- 保證人客戶識別碼
     , "CustId"      varchar2(10)                          -- 保證人身份統一編號
     , "GuaRelCode"  varchar2(2)                           -- 保證人關係代碼
     , "GuaRelJcic"  varchar2(2)                           -- 保證人關係ＪＣＩＣ代碼
     , "GuaTypeCode" varchar2(2)                           -- 保證類別代碼
     , "GuaTypeJcic" varchar2(1)                           -- 保證類別代碼ＪＣＩＣ身份代號
     , "GuaStatCode" varchar2(1)                           -- 保證狀況碼 0:解除 1:設定 2:全部解除 3:向後解除
     , "GuaDate"     decimal(8, 0)   default 0 not null    -- 對保日期
     , "CancelDate"  decimal(8, 0)   default 0 not null    -- 解除日期
    )
    ON COMMIT DELETE ROWS;


CREATE OR REPLACE PROCEDURE "Usp_L8_JcicMonthlyLoanData_Upd"
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


    -- 寫入資料 Work_Guar    -- 保證人檔
    INSERT INTO "Work_Guar"
    SELECT G."ApproveNo"                       AS "ApproveNo"     -- 核准號碼
         , ROW_NUMBER() Over (Partition By G."ApproveNo" Order By G."ApproveNo", G."GuaUKey" )
                                               AS "ROW_NUM"       -- 序號（同一核准號碼編列流水號）
         , G."GuaUKey"                         AS "GuaUKey"       -- 保證人客戶識別碼
         , NVL("CustMain"."CustId",' ')        AS "CustId"        -- 保證人身份統一編號
         , NVL(G."GuaRelCode",' ')             AS "GuaRelCode"    -- 保證人關係代碼
         , NVL("CdGuarantor"."GuaRelJcic",' ') AS "GuaRelJcic"    -- 保證人關係ＪＣＩＣ代碼
         , NVL(G."GuaTypeCode",' ')            AS "GuaTypeCode"   -- 保證類別代碼
         , CASE
--           WHEN NVL(G."GuaTypeCode",' ') IN ('01')           THEN 'G'
--           WHEN NVL(G."GuaTypeCode",' ') IN ('02','03','04') THEN 'N'
--           WHEN NVL(G."GuaTypeCode",' ') IN ('05')           THEN 'S'
--           WHEN NVL(G."GuaTypeCode",' ') IN ('06','07')      THEN 'C'
--           WHEN NVL(G."GuaTypeCode",' ') IN ('08')           THEN 'E'
--           WHEN NVL(G."GuaTypeCode",' ') IN ('09','10','11') THEN 'L'
---- (ref:AS400 LN15F1 (@CHKGRTCLS))
             WHEN NVL(G."GuaTypeCode",' ') IN ('01')           THEN 'G'
             WHEN NVL(G."GuaTypeCode",' ') IN ('02')           THEN 'N'
             WHEN NVL(G."GuaTypeCode",' ') IN ('03','05')      THEN 'C'
             WHEN NVL(G."GuaTypeCode",' ') IN ('04')           THEN 'S'
             WHEN NVL(G."GuaTypeCode",' ') IN ('06')           THEN 'E'
             WHEN NVL(G."GuaTypeCode",' ') IN ('07')           THEN 'L'
----
             ELSE ' '
           END                                 AS "GuaTypeJcic"   -- 保證類別代碼ＪＣＩＣ身份代號
         , NVL(G."GuaStatCode",' ')            AS "GuaStatCode"   -- 保證狀況碼 0:解除 1:設定 2:全部解除 3:向後解除
         , NVL(G."GuaDate",0)                  AS "GuaDate"       -- 對保日期
         , NVL(G."CancelDate",0)               AS "CancelDate"    -- 解除日期
    FROM   "Guarantor" G
      LEFT JOIN "CustMain"    ON "CustMain"."CustUKey"      = G."GuaUKey"
      LEFT JOIN "CdGuarantor" ON "CdGuarantor"."GuaRelCode" = G."GuaRelCode"
    WHERE  G."GuaStatCode" IN ('1')  -- 保證狀況碼 1=設定
      ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicMonthlyLoanData');

    DELETE FROM "JcicMonthlyLoanData"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicMonthlyLoanData');

    INSERT INTO "JcicMonthlyLoanData"
    SELECT
           YYYYMM                                 AS "DataYM"            -- 資料年月
         , M."CustNo"                             AS "CustNo"            -- 戶號
         , M."FacmNo"                             AS "FacmNo"            -- 額度編號
         , M."BormNo"                             AS "BormNo"            -- 撥款序號
         , "CustMain"."CustId"                    AS "CustId"            -- 借款人ID / 統編
         , NVL("LoanBorMain"."Status",0)          AS "Status"            -- 戶況 0:正常戶 1:展期 2: 催收戶 3: 結案戶 4: 逾期戶 5: 催收結案戶 6: 呆帳戶 7: 部分轉呆戶 8: 債權轉讓戶 9: 呆帳結案戶
         , M."EntCode"                            AS "EntCode"           -- 企金別  0:個金 1:企金 2:企金自然人
         , CASE
             WHEN M."EntCode" IN ('1') THEN NVL("CustMain"."SpouseId",' ')
             ELSE NVL(C2."CorpId",' ')
           END                                    AS "SuvId"             -- 負責人IDN/負責之事業體BAN
         , ' '                                    AS "OverseasId"        -- 外僑兼具中華民國國籍IDN
         , "CustMain"."IndustryCode"              AS "IndustryCode"      -- 授信戶行業別
         , M."AcctCode"                           AS "AcctCode"          -- 科目別
         , 'S'                                    AS "SubAcctCode"       -- 科目別註記
         , M."FacAcctCode"                        AS "OrigAcctCode"      -- 轉催收款(或呆帳)前原科目別
         , NVL("FacMain"."UtilAmt",0)             AS "UtilAmt"           -- (額度)貸出金額(放款餘額)
         , NVL("FacMain"."UtilBal",0)             AS "UtilBal"           -- (額度)已動用額度餘額(循環動用還款時會減少,非循環動用還款時不會減少)
         , NVL("FacMain"."RecycleCode",'0')       AS "RecycleCode"       -- 循環動用 0: 非循環動用 1: 循環動用
         , NVL("FacMain"."RecycleDeadline",0)     AS "RecycleDeadline"   -- 循環動用期限
         , CASE
             WHEN "FacMain"."IrrevocableFlag" IS null OR TRIM("FacMain"."IrrevocableFlag") = '' THEN 'N'
             ELSE "FacMain"."IrrevocableFlag"
           END                                    AS "IrrevocableFlag"   -- 不可撤銷 Y:是  N:否
         , CASE
             WHEN M."EntCode" IN ('1') THEN 'K'
             WHEN NVL("CdCl"."ClTypeJCIC",' ') IN ('25') AND TRIM(NVL("LoanBorMain"."UsageCode", ' ')) IN ('02','03','04') THEN 'M'
             ELSE '1'
           END                                    AS "FinCode"           -- 融資分類  -- 法人:K其他
                                                                                      -- 個人:M購買住宅貸款(自用) 1個人投資理財貸款
         , CASE
             WHEN NVL(M."ProdNo",' ') IN ('81','82','83')    THEN '07'  -- 921受災戶
             WHEN NVL(M."ProdNo",' ') IN ('88','89')         THEN '24'  -- 莫拉克受災戶
             WHEN NVL(M."ProdNo",' ') IN ('IA')              THEN '09'
             WHEN SUBSTR(NVL(M."ProdNo",' '),1,1) IN ('I')   THEN '10'
             WHEN M."AcctCode" IN ('340')                    THEN '11'
             ELSE 'XX'
           END                                    AS "ProjCode"          -- 政府專業補助貸款分類   (ref:AS400 LN15F1)
         , CASE
             WHEN TRIM(NVL(M."ProdNo",' ')) IN ('8')         THEN '1'
             WHEN NVL(M."ProdNo",' ') IN ('81','82','83')    THEN '1'
             WHEN SUBSTR(NVL(M."ProdNo",' '),1,1) IN ('I')   THEN '1'
             WHEN M."AcctCode" IN ('340')                    THEN '1'
             ELSE ' '
           END                                    AS "NonCreditCode"     -- 不計入授信項目   (ref:AS400 LN15F1)
         , "LoanBorMain"."UsageCode"              AS "UsageCode"         -- 用途別
         , NVL("LoanBorMain"."ApproveRate",0)     AS "ApproveRate"       -- 本筆撥款利率
         , NVL(M."StoreRate",0)                   AS "StoreRate"         -- 計息利率
         , NVL("LoanBorMain"."DrawdownDate",0)    AS "DrawdownDate"      -- 撥款日期
         , NVL("LoanBorMain"."MaturityDate",0)    AS "MaturityDate"      -- 到期日
         , "LoanBorMain"."AmortizedCode"          AS "AmortizedCode"     -- 攤還方式 --1.按月繳息(按期繳息到期還本) 2.到期取息(到期繳息還本)
                                                                         --            3.本息平均法(期金)           4.本金平均法
         , M."CurrencyCode"                       AS "CurrencyCode"      -- 幣別
         , NVL("LoanBorMain"."DrawdownAmt",0)     AS "DrawdownAmt"       -- 撥款金額
         , NVL(M."LoanBalance",0)                 AS "LoanBal"           -- 放款餘額
         , NVL(AC."Principal",0)                  AS "PrevAmt"           -- 本月應收本金
         , NVL(AC."Interest",0)                   AS "IntAmt"            -- 本月應收利息
         , 0                                      AS "PrevAmtRcv"        -- 本月實收本金   -- 後面才更新值
         , 0                                      AS "IntAmtRcv"         -- 本月實收利息   -- 後面才更新值
         , 0                                      AS "FeeAmtRcv"         -- 本月收取費用   -- 後面才更新值
         , NVL("LoanBorMain"."PrevPayIntDate", 0) AS "PrevPayIntDate"    -- 上次繳息日
         , NVL("LoanBorMain"."PrevRepaidDate", 0) AS "PrevRepaidDate"    -- 上次還本日
         , NVL("LoanBorMain"."NextPayIntDate", 0) AS "NextPayIntDate"    -- 下次繳息日
         , NVL("LoanBorMain"."NextRepayDate", 0)  AS "NextRepayDate"     -- 下次還本日
         , CASE
--           WHEN  NVL("LoanBorMain"."NextPayIntDate", 0) = 0
             WHEN  NVL("LoanBorMain"."NextPayIntDate", 0) <  19110000
                OR NVL("LoanBorMain"."NextPayIntDate", 0) >= TBSDYF THEN 0
             ELSE  TRUNC(months_between(TO_DATE(TBSDYF,'yyyy-mm-dd'), TO_DATE("LoanBorMain"."NextPayIntDate",'yyyy-mm-dd')))
           END                                    AS "IntDelayMon"       -- 利息逾期月數
         , CASE
--           WHEN  NVL("LoanBorMain"."NextRepayDate", 0) = 0
             WHEN  NVL("LoanBorMain"."NextRepayDate", 0) <  19110000
                OR NVL("LoanBorMain"."NextRepayDate", 0) >= TBSDYF THEN 0
             ELSE  TRUNC(months_between(TO_DATE(TBSDYF,'yyyy-mm-dd'), TO_DATE("LoanBorMain"."NextRepayDate",'yyyy-mm-dd')))
           END                                     AS "RepayDelayMon"     -- 本金逾期月數
         , CASE
--           WHEN  NVL("LoanBorMain"."MaturityDate", 0) =  0
             WHEN  NVL("LoanBorMain"."MaturityDate", 0) <  19110000
                OR NVL("LoanBorMain"."MaturityDate", 0) >= TBSDYF THEN 0
             ELSE  TRUNC(months_between(TO_DATE(TBSDYF,'yyyy-mm-dd'), TO_DATE("LoanBorMain"."MaturityDate",'yyyy-mm-dd')))
           END                                    AS "RepaidEndMon"      -- 本金逾到期日(清償期)月數
         , NVL(C."ClCode1", 0)                    AS "ClCode1"           -- 主要擔保品代號1
         , NVL(C."ClCode2", 0)                    AS "ClCode2"           -- 主要擔保品代號2
         , NVL(C."ClNo", 0)                       AS "ClNo"              -- 主要擔保品編號
         , NVL("CdCl"."ClTypeJCIC",' ')           AS "ClTypeCode"        -- 主要擔保品類別代碼(JCIC)
---      , CASE WHEN Cl."CNT" > 1 THEN '3'
---             ELSE '1'
---        END                                    AS "ClType"            -- 擔保品組合型態  0:純信用 1:單一擔保品(或保證) 2:多種擔保品含股票 3:多種擔保品不含股票
---                                                                      --                 若為多種擔保品，後面再判斷是否含股票
         , '1'                                    AS "ClType"            -- 擔保品組合型態  1:單一擔保品(或保證)    (ref:AS400 LN15F1 (#M3102 92 01))
         , CASE WHEN NVL(C."ClCode1", 0) = 1 THEN 0                            -- 建物 (後面再處理)
                WHEN NVL(C."ClCode1", 0) = 2 THEN 0                            -- 土地 (後面再處理)
                WHEN NVL(C."ClCode1", 0) = 3 THEN NVL("ClMain"."EvaAmt", 0)    -- 股票
                WHEN NVL(C."ClCode1", 0) = 4 THEN 0                            -- 有價證券
                WHEN NVL(C."ClCode1", 0) = 5 THEN 0                            -- 銀行保證
                WHEN NVL(C."ClCode1", 0) = 9 THEN NVL("ClMovables"."SettingAmt", 0) -- 動產(車貸)
                ELSE 0
           END                                    AS "EvaAmt"            -- 鑑估總值  (若為0, 後面再處理搬 "核准額度") (ref:AS400 LN15F1 (#M3102 93 10))
         , NVL("ClMain"."DispDate", 0)            AS "DispDate"          -- 擔保品處分日期
         , NVL("LoanBorMain"."SyndNo",0)          AS "SyndNo"            -- 聯貸案序號
         , "ClMain"."SyndCode"                    AS "SyndCode"          -- 聯貸案類型 1:主辦行 2:參貸行
         , NVL("LoanSynd"."SigningDate",0)        AS "SigningDate"       -- 聯貸合約訂定日期
         , NVL("LoanSynd"."SyndAmt",0)            AS "SyndAmt"           -- 聯貸總金額
         , NVL("LoanSynd"."PartAmt",0)            AS "PartAmt"           -- 參貸金額
         , NVL(WK_Guar1."GuaTypeJcic",' ')        AS "GuaTypeCode1"      -- 共同債務人或債務關係人身份代號1
         , NVL(WK_Guar1."CustId",' ')             AS "GuaId1"            -- 共同債務人或債務關係人身份統一編號1
         , NVL(WK_Guar1."GuaRelJcic",' ')         AS "GuaRelCode1"       -- 與主債務人關係1
         , NVL(WK_Guar2."GuaTypeJcic",' ')        AS "GuaTypeCode2"      -- 共同債務人或債務關係人身份代號2
         , NVL(WK_Guar2."CustId",' ')             AS "GuaId2"            -- 共同債務人或債務關係人身份統一編號2
         , NVL(WK_Guar2."GuaRelJcic",' ')         AS "GuaRelCode2"       -- 與主債務人關係2
         , NVL(WK_Guar3."GuaTypeJcic",' ')        AS "GuaTypeCode3"      -- 共同債務人或債務關係人身份代號3
         , NVL(WK_Guar3."CustId",' ')             AS "GuaId3"            -- 共同債務人或債務關係人身份統一編號3
         , NVL(WK_Guar3."GuaRelJcic",' ')         AS "GuaRelCode3"       -- 與主債務人關係3
         , NVL(WK_Guar4."GuaTypeJcic",' ')        AS "GuaTypeCode4"      -- 共同債務人或債務關係人身份代號4
         , NVL(WK_Guar4."CustId",' ')             AS "GuaId4"            -- 共同債務人或債務關係人身份統一編號4
         , NVL(WK_Guar4."GuaRelJcic",' ')         AS "GuaRelCode4"       -- 與主債務人關係4
         , NVL(WK_Guar5."GuaTypeJcic",' ')        AS "GuaTypeCode5"      -- 共同債務人或債務關係人身份代號5
         , NVL(WK_Guar5."CustId",' ')             AS "GuaId5"            -- 共同債務人或債務關係人身份統一編號5
         , NVL(WK_Guar5."GuaRelJcic",' ')         AS "GuaRelCode5"       -- 與主債務人關係5
         , NVL(WK_Guar6."GuaTypeJcic",' ')        AS "GuaTypeCode6"      -- 共同債務人或債務關係人身份代號6
         , NVL(WK_Guar6."CustId",' ')             AS "GuaId6"            -- 共同債務人或債務關係人身份統一編號6
         , NVL(WK_Guar6."GuaRelJcic",' ')         AS "GuaRelCode6"       -- 與主債務人關係6
         , NVL(WK_Guar7."GuaTypeJcic",' ')        AS "GuaTypeCode7"      -- 共同債務人或債務關係人身份代號7
         , NVL(WK_Guar7."CustId",' ')             AS "GuaId7"            -- 共同債務人或債務關係人身份統一編號7
         , NVL(WK_Guar7."GuaRelJcic",' ')         AS "GuaRelCode7"       -- 與主債務人關係7
         , NVL(WK_Guar8."GuaTypeJcic",' ')        AS "GuaTypeCode8"      -- 共同債務人或債務關係人身份代號8
         , NVL(WK_Guar8."CustId",' ')             AS "GuaId8"            -- 共同債務人或債務關係人身份統一編號8
         , NVL(WK_Guar8."GuaRelJcic",' ')         AS "GuaRelCode8"       -- 與主債務人關係8
         , NVL(WK_Guar9."GuaTypeJcic",' ')        AS "GuaTypeCode9"      -- 共同債務人或債務關係人身份代號9
         , NVL(WK_Guar9."CustId",' ')             AS "GuaId9"            -- 共同債務人或債務關係人身份統一編號9
         , NVL(WK_Guar9."GuaRelJcic",' ')         AS "GuaRelCode9"       -- 與主債務人關係9
         , NVL(WK_Guar10."GuaTypeJcic",' ')       AS "GuaTypeCode10"     -- 共同債務人或債務關係人身份代號10
         , NVL(WK_Guar10."CustId",' ')            AS "GuaId10"           -- 共同債務人或債務關係人身份統一編號10
         , NVL(WK_Guar10."GuaRelJcic",' ')        AS "GuaRelCode10"      -- 與主債務人關係10
         , NVL(OD1."OvduDate",0)                  AS "OvduDate"          -- 轉催收日期
         , NVL(OD2."FirstBadDebtDate",0)          AS "BadDebtDate"       -- 轉呆帳日期 (最早之轉銷呆帳日期)
         , ' '                                    AS "BadDebtSkipFg"     -- 不報送呆帳記號 (Y=呆帳不報送聯徵) (後面更新)
         , JOB_START_TIME                         AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                  AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                         AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                  AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM "MonthlyLoanBal" M
      LEFT JOIN "FacMain"     ON "FacMain"."CustNo"       = M."CustNo"
                             AND "FacMain"."FacmNo"       = M."FacmNo"
      LEFT JOIN "CustMain"    ON "CustMain"."CustNo"      = M."CustNo"
      LEFT JOIN "LoanBorMain" ON "LoanBorMain"."CustNo"   = M."CustNo"
                             AND "LoanBorMain"."FacmNo"   = M."FacmNo"
                             AND "LoanBorMain"."BormNo"   = M."BormNo"
   -- LEFT JOIN "ClFac"       ON "ClFac"."CustNo"         = "FacMain"."CustNo"
   --                        AND "ClFac"."FacmNo"         = "FacMain"."FacmNo"
   --                        AND "ClFac"."MainFlag"       = 'Y'
      LEFT JOIN ( SELECT C."ClCode1"
                       , C."ClCode2"
                       , C."ClNo"
                       , C."ApproveNo"
                       , C."CustNo"
                       , C."FacmNo"
                       , ROW_NUMBER() Over (Partition By C."CustNo", C."FacmNo", C."ApproveNo"
                                      Order By C."ClCode1", C."ClCode2", C."ClNo" )  AS ROW_NO
                  FROM   "ClFac" C
                  WHERE  C."MainFlag"       = 'Y'
                ) C           ON C."CustNo"           = "FacMain"."CustNo"
                             AND C."FacmNo"           = "FacMain"."FacmNo"
                             AND C."ApproveNo"        = "FacMain"."ApplNo"
                             AND C.ROW_NO             =  1
      LEFT JOIN "ClMain"      ON "ClMain"."ClCode1"       = C."ClCode1"
                             AND "ClMain"."ClCode2"       = C."ClCode2"
                             AND "ClMain"."ClNo"          = C."ClNo"
      LEFT JOIN "CdCl"        ON "CdCl"."ClCode1"         = C."ClCode1"
                             AND "CdCl"."ClCode2"         = C."ClCode2"
      LEFT JOIN "ClMovables"  ON "ClMovables"."ClCode1"   = C."ClCode1"
                             AND "ClMovables"."ClCode2"   = C."ClCode2"
                             AND "ClMovables"."ClNo"      = C."ClNo"
                             AND NVL(C."ClCode1",0)       = 9
--    LEFT JOIN ( SELECT "ClFac"."CustNo"
--                     , "ClFac"."FacmNo"
--                     , COUNT(*)   AS  "CNT"
--                FROM   "ClFac"
--                GROUP BY "ClFac"."CustNo", "ClFac"."FacmNo"
--              ) Cl          ON M."CustNo"    = Cl."CustNo"
--                           AND M."FacmNo"    = Cl."FacmNo"
      LEFT JOIN "LoanSynd"    ON "LoanSynd"."CustNo"      = "LoanBorMain"."CustNo"
                             AND "LoanSynd"."SyndNo"      = "LoanBorMain"."SyndNo"
      LEFT JOIN ( SELECT L2."CustNo"            AS  "CustNo"
                       , L2."FacmNo"            AS  "FacmNo"
                       , L2."BormNo"            AS  "BormNo"
                       , MIN(L2."OvduDate")     AS  "OvduDate"
                  FROM "LoanOverdue" L2
                  WHERE L2."Status" IN (1,2)
                    AND L2."OvduDate" > 0
                  GROUP BY L2."CustNo", L2."FacmNo", L2."BormNo"
                ) OD1   ON OD1."CustNo"     = M."CustNo"
                       AND OD1."FacmNo"     = M."FacmNo"
                       AND OD1."BormNo"     = M."BormNo"
      LEFT JOIN ( SELECT L2."CustNo"            AS  "CustNo"
                      , L2."FacmNo"            AS  "FacmNo"
                       , L2."BormNo"            AS  "BormNo"
                       , MIN(L2."BadDebtDate")  AS  "FirstBadDebtDate"
                  FROM "LoanOverdue" L2
                  WHERE L2."Status" IN (3)
                    AND L2."BadDebtDate" > 0
                  GROUP BY L2."CustNo", L2."FacmNo", L2."BormNo"
                ) OD2   ON OD2."CustNo"     = M."CustNo"
                       AND OD2."FacmNo"     = M."FacmNo"
                       AND OD2."BormNo"     = M."BormNo"
      -- 提息明細檔 回收本金,利息
      LEFT JOIN ( SELECT "AcLoanInt"."CustNo"                 AS  "CustNo"
                       , "AcLoanInt"."FacmNo"                 AS  "FacmNo"
                       , "AcLoanInt"."BormNo"                 AS  "BormNo"
                       , SUM(NVL("AcLoanInt"."Principal",0))  AS  "Principal"
                       , SUM(NVL("AcLoanInt"."Interest",0))   AS  "Interest"
                  FROM "AcLoanInt"
                  WHERE "AcLoanInt"."YearMonth"  = YYYYMM
                    AND TRUNC(NVL("AcLoanInt"."PayIntDate",0) / 100 ) <= YYYYMM
                  GROUP BY "AcLoanInt"."YearMonth", "AcLoanInt"."CustNo", "AcLoanInt"."FacmNo", "AcLoanInt"."BormNo"
                ) AC    ON AC."CustNo"     = M."CustNo"
                       AND AC."FacmNo"     = M."FacmNo"
                       AND AC."BormNo"     = M."BormNo"
      -- 尋找 "授信戶為負責人" 的公司統編
      LEFT JOIN ( SELECT C."SpouseId"
                       , MIN(C."CustId")    AS "CorpId"
                    FROM  "CustMain" C
                    WHERE LENGTH( TRIM(C."CustId") ) = 8
                    GROUP BY C."SpouseId"
                ) C2    ON C2."SpouseId"    = "CustMain"."CustId"
      -- 共同債務人或債務關係人
      LEFT JOIN "Work_Guar" WK_Guar1   ON  WK_Guar1."ApproveNo" = "FacMain"."ApplNo"
                                      AND  WK_Guar1."ROW_NUM"   = 1
      LEFT JOIN "Work_Guar" WK_Guar2   ON  WK_Guar2."ApproveNo" = "FacMain"."ApplNo"
                                      AND  WK_Guar2."ROW_NUM"   = 2
      LEFT JOIN "Work_Guar" WK_Guar3   ON  WK_Guar3."ApproveNo" = "FacMain"."ApplNo"
                                      AND  WK_Guar3."ROW_NUM"   = 3
      LEFT JOIN "Work_Guar" WK_Guar4   ON  WK_Guar4."ApproveNo" = "FacMain"."ApplNo"
                                      AND  WK_Guar4."ROW_NUM"   = 4
      LEFT JOIN "Work_Guar" WK_Guar5   ON  WK_Guar5."ApproveNo" = "FacMain"."ApplNo"
                                      AND  WK_Guar5."ROW_NUM"   = 5
      LEFT JOIN "Work_Guar" WK_Guar6   ON  WK_Guar6."ApproveNo" = "FacMain"."ApplNo"
                                      AND  WK_Guar6."ROW_NUM"   = 6
      LEFT JOIN "Work_Guar" WK_Guar7   ON  WK_Guar7."ApproveNo" = "FacMain"."ApplNo"
                                      AND  WK_Guar7."ROW_NUM"   = 7
      LEFT JOIN "Work_Guar" WK_Guar8   ON  WK_Guar8."ApproveNo" = "FacMain"."ApplNo"
                                      AND  WK_Guar8."ROW_NUM"   = 8
      LEFT JOIN "Work_Guar" WK_Guar9   ON  WK_Guar9."ApproveNo" = "FacMain"."ApplNo"
                                      AND  WK_Guar9."ROW_NUM"   = 9
      LEFT JOIN "Work_Guar" WK_Guar10  ON  WK_Guar10."ApproveNo" = "FacMain"."ApplNo"
                                      AND  WK_Guar10."ROW_NUM"   = 10
     WHERE  M."YearMonth"  =  YYYYMM
       AND  "LoanBorMain"."CustNo" IS NOT NULL
      ;

    INS_CNT := INS_CNT + sql%rowcount;


    DBMS_OUTPUT.PUT_LINE('INSERT JcicMonthlyLoanData END: INS_CNT=' || INS_CNT);


-- 更新 BadDebtSkipFg 不報送呆帳記號
-- 不報送呆帳檔
    DBMS_OUTPUT.PUT_LINE('UPDATE BadDebtSkipFg 不報送呆帳記號');
    UPD_CNT := 0;

    MERGE INTO "JcicMonthlyLoanData" M
    USING ( SELECT DISTINCT A."CustNo"   AS  "CustNo"
                          , A."FacmNo"   AS  "FacmNo"
            FROM  "RptJcic" A
          ) T
    ON (    M."DataYM"   = YYYYMM
        AND M."CustNo"   = T."CustNo"
        AND M."FacmNo"   = T."FacmNo"
        AND M."Status" IN ('6')
       )
    WHEN MATCHED THEN UPDATE SET M."BadDebtSkipFg" = 'Y'
    ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE BadDebtSkipFg 不報送呆帳記號 End');


-- 更新 ClType  擔保品組合型態   -- 判斷 多種擔保品是否含股票 2:多種擔保品含股票 3:多種擔保品不含股票
--  MERGE INTO "JcicMonthlyLoanData" M
--  USING ( SELECT "ClFac"."CustNo"
--               , "ClFac"."FacmNo"
--          FROM   "ClFac"
--            LEFT JOIN "ClStock" ON "ClFac"."ClCode1" = "ClStock"."ClCode1"
--                               AND "ClFac"."ClCode2" = "ClStock"."ClCode2"
--                               AND "ClFac"."ClNo"    = "ClStock"."ClNo"
--          WHERE  "ClStock"."SettingStat" IN ('1')            -- 1: 設定 2: 解除
--            AND  "ClStock"."ClStat"      IN ('0', '2', '3')  -- 0: 正常 1: 塗銷 2: 處分 3: 抵押權確定 ???
--          GROUP BY "ClFac"."CustNo", "ClFac"."FacmNo"
--        ) T
--  ON (    M."DataYM"    = YYYYMM
--      AND M."CustNo"    = T."CustNo"
--      AND M."FacmNo"    = T."FacmNo"
--     )
--  WHEN MATCHED THEN UPDATE SET M."ClType" = CASE WHEN M."ClType" = '3' THEN '2'  -- 2:多種擔保品含股票
--                                                 ELSE M."ClType"
--                                            END
--   ;
--
--  UPD_CNT := UPD_CNT + sql%rowcount;


-- 更新 EvaAmt 鑑估總值 (建物 & 土地)
    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt 不動產');

    MERGE INTO "JcicMonthlyLoanData" M
    USING ( SELECT "ClFac"."CustNo"
                 , "ClFac"."FacmNo"
                 , "ClFac"."ClCode1"
                 , "ClFac"."ClCode2"
                 , SUM(NVL("ClImm"."SettingAmt",0))  AS "SettingAmt"
            FROM  "ClFac"
              LEFT JOIN "ClImm"   ON "ClImm"."ClCode1"   =  "ClFac"."ClCode1"
                                 AND "ClImm"."ClCode2"   =  "ClFac"."ClCode2"
                                 AND "ClImm"."ClNo"      =  "ClFac"."ClNo"
            WHERE NVL("ClFac"."ClCode1", 0) IN (1, 2)
            GROUP BY "ClFac"."CustNo", "ClFac"."FacmNo", "ClFac"."ClCode1", "ClFac"."ClCode2"
          ) T
    ON (    M."DataYM"   = YYYYMM
        AND NVL(M."ClCode1", 0) IN (1, 2)
        AND M."CustNo"   = T."CustNo"
        AND M."FacmNo"   = T."FacmNo"
        AND M."ClCode1"  = T."ClCode1"
        AND M."ClCode2"  = T."ClCode2"
       )
    WHEN MATCHED THEN UPDATE SET M."EvaAmt" = T."SettingAmt"
    ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt 不動產 End');


-- 更新 EvaAmt 鑑估總值 (核准額度)
    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt = 核准額度 IF = 0');

    MERGE INTO "JcicMonthlyLoanData" M
    USING ( SELECT M."CustNo"
                 , M."FacmNo"
                 , M."BormNo"
                 , NVL("FacMain"."LineAmt",0)  AS "LineAmt"
            FROM  "JcicMonthlyLoanData" M
              LEFT JOIN "FacMain"   ON "FacMain"."CustNo"   =  M."CustNo"
                                   AND "FacMain"."FacmNo"   =  M."FacmNo"
            WHERE M."DataYM"   = YYYYMM
              AND NVL(M."EvaAmt", 0) = 0
          ) T
    ON (    M."DataYM"   = YYYYMM
        AND M."CustNo"   = T."CustNo"
        AND M."FacmNo"   = T."FacmNo"
        AND M."BormNo"   = T."BormNo"
       )
    WHEN MATCHED THEN UPDATE SET M."EvaAmt" = T."LineAmt"
    ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt = 核准額度 End');


-- JOIN "LoanBorTx"
-- 更新 PrevAmtRcv, IntAmtRcv, FeeAmtRcv   本月實收  (ref: LN15F1 (費用含逾期息,違約金:#M3102 36 7))
    DBMS_OUTPUT.PUT_LINE('UPDATE Rcv 本月實收金額 End');
    UPD_CNT := 0;

    MERGE INTO "JcicMonthlyLoanData" M
    USING ( SELECT M."CustNo"
                 , M."FacmNo"
                 , M."BormNo"
                 , Tx."Principal"      AS  "Principal"
                 , Tx."Interest"       AS  "Interest"
                 , Tx."DelayInt"       AS  "DelayInt"
                 , Tx."BreachAmt"      AS  "BreachAmt"
                 , Tx."CloseBreachAmt" AS  "CloseBreachAmt"
            FROM "JcicMonthlyLoanData" M
              LEFT JOIN ( SELECT Tx."CustNo"
                               , Tx."FacmNo"
                               , Tx."BormNo"
                               , SUM ( NVL(Tx."Principal", 0) )      AS "Principal"
                               , SUM ( NVL(Tx."Interest", 0) )       AS "Interest"
                               , SUM ( NVL(Tx."DelayInt", 0) )       AS "DelayInt"
                               , SUM ( NVL(Tx."BreachAmt", 0) )      AS "BreachAmt"
                               , SUM ( NVL(Tx."CloseBreachAmt", 0) ) AS "CloseBreachAmt"
                          FROM   "LoanBorTx" Tx
                          WHERE  TRUNC(Tx."EntryDate" / 100)  = YYYYMM
                            AND  SUBSTR(NVL(Tx."TitaTxCd",' '), 1, 2) = 'L3'
                          GROUP  BY  Tx."CustNo", Tx."FacmNo", Tx."BormNo"
                        ) Tx   ON Tx."CustNo"  =  M."CustNo"
                              AND Tx."FacmNo"  =  M."FacmNo"
                              AND Tx."BormNo"  =  M."BormNo"
            WHERE  M."DataYM"   =  YYYYMM
              AND  M."Status"   IN (0, 1, 4)   -- 非結案,催收,呆帳
          ) T
    ON (    M."DataYM"   = YYYYMM
        AND M."CustNo"   = T."CustNo"
        AND M."FacmNo"   = T."FacmNo"
        AND M."BormNo"   = T."BormNo"
       )
    WHEN MATCHED THEN UPDATE SET M."PrevAmtRcv" = NVL(T."Principal",0)
                               , M."IntAmtRcv"  = NVL(T."Interest",0)
                               , M."FeeAmtRcv"  = NVL(T."DelayInt",0)
                                                + NVL(T."BreachAmt",0)
                                                + NVL(T."CloseBreachAmt",0)
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE Rcv 本月實收金額 END: UPD_CNT=' || UPD_CNT);


-- -- JOIN "AcDetail"
-- -- 更新 PrevAmtRcv, IntAmtRcv, FeeAmtRcv   本月實收  (ref: LN15F1 (費用含逾期息,違約金:#M3102 36 7))
--     DBMS_OUTPUT.PUT_LINE('UPDATE Rcv 本月實收金額 End');
--     UPD_CNT := 0;

--     MERGE INTO "JcicMonthlyLoanData" M
--     USING ( SELECT M."CustNo"
--                  , M."FacmNo"
--                  , M."BormNo"
--                  , Ac."PrevAmtRcv"    AS  "PrevAmtRcv"
--                  , Ac."IntAmtRcv"     AS  "IntAmtRcv"
--                  , Ac."FeeAmtRcv"     AS  "FeeAmtRcv"
--             FROM "JcicMonthlyLoanData" M
--               LEFT JOIN ( SELECT Ac."CustNo"
--                                , Ac."FacmNo"
--                                , Ac."BormNo"
--                                , SUM ( CASE WHEN Ac."AcctCode" IN ('310', '320', '330', '340') THEN
--                                               CASE WHEN Ac."DbCr" = 'D' THEN 0 - Ac."TxAmt"
--                                                    ELSE Ac."TxAmt"
--                                               END
--                                             ELSE  0
--                                        END )       AS "PrevAmtRcv"
--                                , SUM ( CASE WHEN Ac."AcctCode" IN ('IC1', 'IC2', 'IC3', 'IC4') THEN
--                                               CASE WHEN Ac."DbCr" = 'D' THEN 0 - Ac."TxAmt"
--                                                    ELSE Ac."TxAmt"
--                                               END
--                                             ELSE  0
--                                        END )       AS "IntAmtRcv"
--                                , SUM ( CASE WHEN Ac."AcctCode" IN ('IOV', 'IOP') THEN
--                                               CASE WHEN Ac."DbCr" = 'D' THEN 0 - Ac."TxAmt"
--                                                    ELSE Ac."TxAmt"
--                                               END
--                                             ELSE  0
--                                        END )       AS "FeeAmtRcv"
--                           FROM   "AcDetail" Ac            -- 會計帳務明細檔
--                           WHERE  TRUNC(Ac."AcDate" / 100)  = YYYYMM
--                             AND  SUBSTR(NVL(Ac."TitaTxCd",' '), 1, 2) = 'L3'
--                             AND  NVL(Ac."AcctCode",' ') IN ('310', '320', '330', '340',  -- 本金
--                                                             'IC1', 'IC2', 'IC3', 'IC4',  -- 利息
--                                                             'IOV', 'IOP' )               -- 逾期息,違約金 (費用)  (ref: LN15F1 (#M3102 36 7))
--                           GROUP  BY  Ac."CustNo", Ac."FacmNo", Ac."BormNo"
--                         ) Ac   ON Ac."CustNo"  =  M."CustNo"
--                               AND Ac."FacmNo"  =  M."FacmNo"
--                               AND Ac."BormNo"  =  M."BormNo"
--             WHERE  M."DataYM"   =  YYYYMM
--               AND  M."Status"   IN (0, 1, 4)   -- 非結案,催收,呆帳
--           ) T
--     ON (    M."DataYM"   = YYYYMM
--         AND M."CustNo"   = T."CustNo"
--         AND M."FacmNo"   = T."FacmNo"
--         AND M."BormNo"   = T."BormNo"
--        )
--     WHEN MATCHED THEN UPDATE SET M."PrevAmtRcv" = NVL(T."PrevAmtRcv",0)
--                                , M."IntAmtRcv"  = NVL(T."IntAmtRcv",0)
--                                , M."FeeAmtRcv"  = NVL(T."FeeAmtRcv",0)
--       ;

--     UPD_CNT := UPD_CNT + sql%rowcount;
--     DBMS_OUTPUT.PUT_LINE('UPDATE Rcv 本月實收金額 END: UPD_CNT=' || UPD_CNT);


-- 更新 FeeAmtRcv 本月收取費用
---- "AcctCode" ref: Project\common\BaTxCom.java  F10+...+F24
    DBMS_OUTPUT.PUT_LINE('UPDATE FeeAmtRcv 本月收取費用');
    UPD_CNT := 0;

    MERGE INTO "JcicMonthlyLoanData" M
    USING ( SELECT M."CustNo"
                 , M."FacmNo"
                 , MIN(M."BormNo")   AS  "BormNo"  -- 費用只到額度層，併入撥款序號最小的
                 , Ac."FeeAmtRcv"    AS  "FeeAmtRcv"
            FROM "JcicMonthlyLoanData" M
              LEFT JOIN ( SELECT Ac."CustNo"
                               , Ac."FacmNo"
                               , SUM ( CASE WHEN NVL(Ac."DbCr",' ') = 'D' THEN 0 - Ac."TxAmt"
                                            ELSE Ac."TxAmt"
                                       END )     AS "FeeAmtRcv"
                          FROM   "AcDetail" Ac            -- 會計帳務明細檔
                          WHERE  TRUNC(Ac."AcDate" / 100)  = YYYYMM
                            AND  SUBSTR(NVL(Ac."TitaTxCd",' '), 1, 2) = 'L3'
                            AND  NVL(Ac."AcctCode",' ') IN ('F10', 'F29', 'TMI', 'F09', 'F25', 'F07', 'F24')    -- 費用
                          GROUP  BY  Ac."CustNo", Ac."FacmNo"
                        ) Ac   ON Ac."CustNo"  =  M."CustNo"
                              AND Ac."FacmNo"  =  M."FacmNo"
            WHERE  M."DataYM"   =  YYYYMM
              AND  M."Status"   IN (0, 1, 4)   -- 非結案,催收,呆帳
            GROUP  BY  M."CustNo", M."FacmNo", Ac."FeeAmtRcv"
          ) T
    ON (    M."DataYM"   = YYYYMM
        AND M."CustNo"   = T."CustNo"
        AND M."FacmNo"   = T."FacmNo"
        AND M."BormNo"   = T."BormNo"
        AND NVL(T."FeeAmtRcv",0) > 0
       )
    WHEN MATCHED THEN UPDATE SET M."FeeAmtRcv"  = M."FeeAmtRcv" + T."FeeAmtRcv"
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE FeeAmtRcv 本月收取費用 END: UPD_CNT=' || UPD_CNT);


-- 更新 PrevAmtRcv, IntAmtRcv, FeeAmtRcv   本月實收
---- "AcctCode" ref: Project\common\BaTxCom.java  F10+...+F24
----                 Project\common\AcDetailCom.java
--    DBMS_OUTPUT.PUT_LINE('UPDATE Rcv 本月實收金額');
--
--    MERGE INTO "JcicMonthlyLoanData" M
--    USING (SELECT "CustNo"
--                , "FacmNo"
--                , "BormNo"
--                , SUM ( CASE
--                          WHEN "AcctCode" IN ('310', '320', '330', '340') THEN
--                            CASE WHEN "DbCr" = 'C' THEN "TxAmt"
--                                 ELSE 0 - "TxAmt"
--                            END
--                          ELSE  0
--                        END )       AS "PrevAmtRcv"
--                , SUM ( CASE
--                          WHEN "AcctCode" IN ('IC1', 'IC2', 'IC3', 'IC4') THEN
--                            CASE WHEN "DbCr" = 'C' THEN "TxAmt"
--                                 ELSE 0 - "TxAmt"
--                            END
--                          ELSE  0
--                        END )       AS "IntAmtRcv"
--                , SUM ( CASE
--                          WHEN "AcctCode" IN ('F10', 'F29', 'TMI', 'F09', 'F25', 'F07', 'F24', 'IOV', 'IOP') THEN
--                            CASE WHEN "DbCr" = 'C' THEN "TxAmt"
--                                 ELSE 0 - "TxAmt"
--                            END
--                          ELSE  0
--                        END )       AS "FeeAmtRcv"
--            FROM "AcDetail"             -- 會計帳務明細檔
--            WHERE "CustNo"  > 0
--             AND  TRUNC("AcDate" / 100)  = YYYYMM
--             AND  SUBSTR("TitaTxCd",1,2) = 'L3'
--             AND  "AcctCode" IN ('310', '320', '330', '340',                                      -- 本金
--                                 'IC1', 'IC2', 'IC3', 'IC4',                                      -- 利息
--                                 'F10', 'F29', 'TMI', 'F09', 'F25', 'F07', 'F24', 'IOV', 'IOP'    -- 費用 (含逾期息,違約金)  (ref: LN15F1 (#M3102 36 7))
--                                )
--            GROUP BY "CustNo", "FacmNo", "BormNo"
--           ) T
--    ON (    M."DataYM"   = YYYYMM
--        AND M."CustNo"   = T."CustNo"
--        AND M."FacmNo"   = T."FacmNo"
--        AND M."BormNo"   = T."BormNo"
--       )
--    WHEN MATCHED THEN UPDATE SET M."PrevAmtRcv" = T."PrevAmtRcv"
--                               , M."IntAmtRcv"  = T."IntAmtRcv"
--                               , M."FeeAmtRcv"  = T."FeeAmtRcv"
--    ;
--
--    UPD_CNT := UPD_CNT + sql%rowcount;
--    DBMS_OUTPUT.PUT_LINE('UPDATE Rcv 本月實收金額 End');



-- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

-- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MonthlyLoanBa_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;

--       , ' '   AS "CustId"            -- 借款人ID / 統編
--       , 0     AS "Status"            -- 戶況 0:正常戶 1:展期 2: 催收戶 3: 結案戶 4: 逾期戶 5: 催收結案戶 6: 呆帳戶 7: 部分轉呆戶 8: 債權轉讓戶 9: 呆帳結案戶
--       , ' '   AS "EntCode"           -- 企金別  0:個金 1:企金 2:企金自然人
--       , ' '   AS "SuvId"             -- 負責人IDN/負責之事業體BAN
--       , ' '   AS "OverseasId"        -- 外僑兼具中華民國國籍IDN
--       , ' '   AS "IndustryCode"      -- 授信戶行業別
--       , ' '   AS "AcctCode"          -- 科目別
--       , ' '   AS "SubAcctCode"       -- 科目別註記  --???
--       , ' '   AS "OrigAcctCode"      -- 轉催收款(或呆帳)前原科目別
--       , 0     AS "UtilAmt"           -- (額度)貸出金額(放款餘額)
--       , 0     AS "UtilBal"           -- (額度)已動用額度餘額(循環動用還款時會減少,非循環動用還款時不會減少)
--       , ' '   AS "RecycleCode"       -- 循環動用 0: 非循環動用 1: 循環動用
--       , 0     AS "RecycleDeadline"   -- 循環動用期限
--       , ' '   AS "IrrevocableFlag"   -- 不可撤銷 Y:是  N:否
--       , ' '   AS "FinCode"           -- 融資分類  -- 法人:K其他
--       , ' '   AS "ProjCode"          -- 政府專業補助貸款分類   (ref:AS400 LN15F1)
--       , ' '   AS "NonCreditCode"     -- 不計入授信項目   (ref:AS400 LN15F1)
--       , ' '   AS "UsageCode"         -- 用途別
--       , 0     AS "ApproveRate"       -- 本筆撥款利率
--       , 0     AS "StoreRate"         -- 計息利率
--       , 0     AS "DrawdownDate"      -- 撥款日期
--       , 0     AS "MaturityDate"      -- 到期日
--       , ' '   AS "AmortizedCode"     -- 攤還方式 --1.按月繳息(按期繳息到期還本) 2.到期取息(到期繳息還本)
--       , ' '   AS "CurrencyCode"      -- 幣別
--       , 0     AS "DrawdownAmt"       -- 撥款金額
--       , 0     AS "LoanBal"           -- 放款餘額
--       , 0     AS "PrevAmt"           -- 本月應收本金
--       , 0     AS "IntAmt"            -- 本月應收利息
--       , 0     AS "PrevAmtRcv"        -- 本月實收本金   -- 後面才更新值
--       , 0     AS "IntAmtRcv"         -- 本月實收利息
--       , 0     AS "FeeAmtRcv"         -- 本月收取費用   -- 後面才更新值
--       , 0     AS "PrevPayIntDate"    -- 上次繳息日
--       , 0     AS "PrevRepaidDate"    -- 上次還本日
--       , 0     AS "NextPayIntDate"    -- 下次繳息日
--       , 0     AS "NextRepayDate"     -- 下次還本日
--       , 0     AS "IntDelayMon"       -- 利息逾期月數
--       , 0     AS "RepayDelayMon"     -- 本金逾期月數
--       , 0     AS "RepaidEndMon"      -- 本金逾到期日(清償期)月數
--       , 0     AS "ClCode1"           -- 主要擔保品代號1
--       , 0     AS "ClCode2"           -- 主要擔保品代號2
--       , 0     AS "ClNo"              -- 主要擔保品編號
--       , ' '   AS "ClTypeCode"        -- 主要擔保品類別代碼(JCIC)
--       , ' '   AS "ClType"            -- 擔保品組合型態  1:單一擔保品(或保證)    (ref:AS400 LN15F1 (#M3102 92 01))
--       , 0     AS "EvaAmt"            -- 鑑估總值  (若為0, 後面再處理搬 "核准額度") (ref:AS400 LN15F1 (#M3102 93 10))
--       , 0     AS "DispDate"          -- 擔保品處分日期
--       , 0     AS "SyndNo"            -- 聯貸案序號
--       , ' '   AS "SyndCode"          -- 聯貸案類型 1:主辦行 2:參貸行
--       , 0     AS "SigningDate"       -- 聯貸合約訂定日期
--       , 0     AS "SyndAmt"           -- 聯貸總金額
--       , 0     AS "PartAmt"           -- 參貸金額
--       , ' '   AS "GuaTypeCode1"      -- 共同債務人或債務關係人身份代號1
--       , ' '   AS "GuaId1"            -- 共同債務人或債務關係人身份統一編號1
--       , ' '   AS "GuaRelCode1"       -- 與主債務人關係1
--       , ' '   AS "GuaTypeCode2"      -- 共同債務人或債務關係人身份代號2
--       , ' '   AS "GuaId2"            -- 共同債務人或債務關係人身份統一編號2
--       , ' '   AS "GuaRelCode2"       -- 與主債務人關係2
--       , ' '   AS "GuaTypeCode3"      -- 共同債務人或債務關係人身份代號3
--       , ' '   AS "GuaId3"            -- 共同債務人或債務關係人身份統一編號3
--       , ' '   AS "GuaRelCode3"       -- 與主債務人關係3
--       , ' '   AS "GuaTypeCode4"      -- 共同債務人或債務關係人身份代號4
--       , ' '   AS "GuaId4"            -- 共同債務人或債務關係人身份統一編號4
--       , ' '   AS "GuaRelCode4"       -- 與主債務人關係4
--       , ' '   AS "GuaTypeCode5"      -- 共同債務人或債務關係人身份代號5
--       , ' '   AS "GuaId5"            -- 共同債務人或債務關係人身份統一編號5
--       , ' '   AS "GuaRelCode5"       -- 與主債務人關係5
--       , ' '   AS "GuaTypeCode6"      -- 共同債務人或債務關係人身份代號6
--       , ' '   AS "GuaId6"            -- 共同債務人或債務關係人身份統一編號6
--       , ' '   AS "GuaRelCode6"       -- 與主債務人關係6
--       , ' '   AS "GuaTypeCode7"      -- 共同債務人或債務關係人身份代號7
--       , ' '   AS "GuaId7"            -- 共同債務人或債務關係人身份統一編號7
--       , ' '   AS "GuaRelCode7"       -- 與主債務人關係7
--       , ' '   AS "GuaTypeCode8"      -- 共同債務人或債務關係人身份代號8
--       , ' '   AS "GuaId8"            -- 共同債務人或債務關係人身份統一編號8
--       , ' '   AS "GuaRelCode8"       -- 與主債務人關係8
--       , ' '   AS "GuaTypeCode9"      -- 共同債務人或債務關係人身份代號9
--       , ' '   AS "GuaId9"            -- 共同債務人或債務關係人身份統一編號9
--       , ' '   AS "GuaRelCode9"       -- 與主債務人關係9
--       , ' '   AS "GuaTypeCode10"     -- 共同債務人或債務關係人身份代號10
--       , ' '   AS "GuaId10"           -- 共同債務人或債務關係人身份統一編號10
--       , ' '   AS "GuaRelCode10"      -- 與主債務人關係10
--       , 0     AS "OvduDate"          -- 轉催收日期
--       , 0     AS "BadDebtDate"       -- 轉呆帳日期 (最早之轉銷呆帳日期)
--       , ' '   AS "BadDebtSkipFg"     -- 不報送呆帳記號 (Y=呆帳不報送聯徵)
