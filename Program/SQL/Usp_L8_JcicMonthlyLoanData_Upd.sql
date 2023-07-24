CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L8_JcicMonthlyLoanData_Upd"
(
-- 程式功能：維護 JcicMonthlyLoanData 聯徵放款月報資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicMonthlyLoanData_Upd"(20210531,'999999');
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
         , NVL(LM."Status",0)                     AS "Status"            -- 戶況 0:正常戶 1:展期 2: 催收戶 3: 結案戶 4: 逾期戶 5: 催收結案戶 6: 呆帳戶 7: 部分轉呆戶 8: 債權轉讓戶 9: 呆帳結案戶
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
--         , CASE
--             WHEN "FacMain"."IrrevocableFlag" IS null OR TRIM("FacMain"."IrrevocableFlag") = '' THEN 'N'
--             ELSE "FacMain"."IrrevocableFlag"
--           END                                    AS "IrrevocableFlag"   -- 不可撤銷 Y:是  N:否
-- 2023/1/5 配合聯徵申報,目前並無可撤銷資料,故統一放"不可撤銷"
         , 'Y'                                    AS "IrrevocableFlag"   -- 不可撤銷 Y:是  N:否
         , CASE
             WHEN M."EntCode" IN ('1') THEN 'K'
             WHEN NVL(C."ClCode1", 0) = 9 AND NVL(C."ClCode2", 0) = 1 THEN 'O'  -- 車貸
             --WHEN NVL("CdCl"."ClTypeJCIC",' ') IN ('25') AND TRIM(NVL(LM."UsageCode", ' ')) IN ('02','03','04') THEN 'M'
             WHEN TRIM(NVL(LM."UsageCode", ' ')) IN ('02','03','04') THEN 'M'
             ELSE '1'
           END                                    AS "FinCode"           -- 融資分類  -- 法人:K其他
                                                                                      -- 個人:M購買住宅貸款(自用) 1個人投資理財貸款
         , CASE
             WHEN NVL(M."ProdNo",' ') IN ('81','82','83')    THEN '07'  -- 921受災戶
             WHEN NVL(M."ProdNo",' ') IN ('88','89')         THEN '24'  -- 莫拉克受災戶
             WHEN NVL(M."ProdNo",' ') IN ('IA')              THEN '09'
             WHEN SUBSTR(NVL(M."ProdNo",' '),1,1) IN ('I')   THEN '10'
             WHEN M."FacAcctCode" IN ('340')                 THEN '11'
             ELSE 'XX'
           END                                    AS "ProjCode"          -- 政府專業補助貸款分類   (ref:AS400 LN15F1)
         , CASE
             WHEN TRIM(NVL(M."ProdNo",' ')) IN ('8')         THEN '1'
             WHEN NVL(M."ProdNo",' ') IN ('81','82','83')    THEN '1'
             WHEN SUBSTR(NVL(M."ProdNo",' '),1,1) IN ('I')   THEN '1'
             WHEN M."FacAcctCode" IN ('340')                 THEN '1'
             ELSE ' '
           END                                    AS "NonCreditCode"     -- 不計入授信項目   (ref:AS400 LN15F1)
         , LM."UsageCode"                         AS "UsageCode"         -- 用途別
         , NVL(LM."ApproveRate",0)                AS "ApproveRate"       -- 本筆撥款利率
         , NVL(M."StoreRate",0)                   AS "StoreRate"         -- 計息利率
         , NVL(LM."DrawdownDate",0)               AS "DrawdownDate"      -- 撥款日期
         , NVL(LM."MaturityDate",0)               AS "MaturityDate"      -- 到期日
         , LM."AmortizedCode"                     AS "AmortizedCode"     -- 攤還方式 --1.按月繳息(按期繳息到期還本) 2.到期取息(到期繳息還本)
                                                                         --            3.本息平均法(期金)           4.本金平均法
         , M."CurrencyCode"                       AS "CurrencyCode"      -- 幣別
         , NVL(LM."DrawdownAmt",0)                AS "DrawdownAmt"       -- 撥款金額
         , NVL(M."LoanBalance",0)                 AS "LoanBal"           -- 放款餘額
         , NVL(AC."Principal",0)                  AS "PrevAmt"           -- 本月應收本金
         , NVL(M."IntAmtAcc",0)                   AS "IntAmt"            -- 本月應收利息
         , 0                                      AS "PrevAmtRcv"        -- 本月實收本金   -- 後面才更新值
         , 0                                      AS "IntAmtRcv"         -- 本月實收利息   -- 後面才更新值
         , 0                                      AS "FeeAmtRcv"         -- 本月收取費用   -- 後面才更新值
         , NVL(LM."PrevPayIntDate", 0)            AS "PrevPayIntDate"    -- 上次繳息日
         , NVL(LM."PrevRepaidDate", 0)            AS "PrevRepaidDate"    -- 上次還本日
         , NVL(LM."NextPayIntDate", 0)            AS "NextPayIntDate"    -- 下次繳息日
         , NVL(LM."NextRepayDate", 0)             AS "NextRepayDate"     -- 下次還本日
         , CASE
             WHEN  LM."Status"  IN (6)          THEN 6
             WHEN  NVL(LM."LoanBal", 0)         =    0    THEN 0
             WHEN  NVL(LM."NextPayIntDate", 0)  > NVL(LM."MaturityDate", 0)  THEN  
              CASE     
                WHEN  NVL(LM."MaturityDate", 0) <  19110000
                   OR NVL(LM."MaturityDate", 0) >= TMNDYF THEN 0
                ELSE  TRUNC(months_between(TO_DATE(TMNDYF,'yyyy-mm-dd'), TO_DATE(LM."MaturityDate",'yyyy-mm-dd')))
              END 
             ELSE
              CASE    
                WHEN  NVL(LM."NextPayIntDate", 0) <  19110000
                   OR NVL(LM."NextPayIntDate", 0) >= TMNDYF THEN 0
                ELSE  TRUNC(months_between(TO_DATE(TMNDYF,'yyyy-mm-dd'), TO_DATE(LM."NextPayIntDate",'yyyy-mm-dd')))
              END 
           END                                    AS "IntDelayMon"       -- 利息逾期月數
         , CASE
             WHEN  NVL(LM."NextRepayDate", 0) <  19110000
                OR NVL(LM."NextRepayDate", 0) >= TMNDYF THEN 0
             ELSE  TRUNC(months_between(TO_DATE(TMNDYF,'yyyy-mm-dd'), TO_DATE(LM."NextRepayDate",'yyyy-mm-dd')))
           END                                     AS "RepayDelayMon"     -- 本金逾期月數
         , CASE
             WHEN  NVL(LM."MaturityDate", 0) <  19110000
                OR NVL(LM."MaturityDate", 0) >= TMNDYF THEN 0
             ELSE  TRUNC(months_between(TO_DATE(TMNDYF,'yyyy-mm-dd'), TO_DATE(LM."MaturityDate",'yyyy-mm-dd')))
           END                                    AS "RepaidEndMon"      -- 本金逾到期日(清償期)月數
         , NVL(C."ClCode1", 0)                    AS "ClCode1"           -- 主要擔保品代號1
         , NVL(C."ClCode2", 0)                    AS "ClCode2"           -- 主要擔保品代號2
         , NVL(C."ClNo", 0)                       AS "ClNo"              -- 主要擔保品編號
         , NVL("CdCl"."ClTypeJCIC",' ')           AS "ClTypeCode"        -- 主要擔保品類別代碼(JCIC)
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
         , NVL("LoanSynd"."SyndNo",0)             AS "SyndNo"            -- 聯貸案序號
         , NVL("LoanSynd"."SyndTypeCodeFlag",' ') AS "SyndCode"          -- 聯貸案類型 A:國內 B:國際
         , NVL("LoanSynd"."SigningDate",0)        AS "SigningDate"       -- 聯貸合約訂定日期
         , NVL("LoanSynd"."SyndAmt",0)            AS "SyndAmt"           -- 聯貸總金額
         , NVL("LoanSynd"."PartAmt",0)            AS "PartAmt"           -- 參貸金額
         , CASE WHEN NVL(OD2."OvduDate",0) > 0 THEN OD2."OvduDate"
                ELSE NVL(OD1."OvduDate",0)
           END                                    AS "OvduDate"          -- 轉催收日期
         , NVL(OD2."FirstBadDebtDate",0)          AS "BadDebtDate"       -- 轉呆帳日期 (最早之轉銷呆帳日期)
         , ' '                                    AS "BadDebtSkipFg"     -- 不報送呆帳記號 (Y=呆帳不報送聯徵) (後面更新)
         , M."AcBookCode"                         AS "AcBookCode"        -- 帳冊別   (000：全公司)
         , M."AcSubBookCode"                      AS "AcSubBookCode"     -- 區隔帳冊 (00A:傳統帳冊 201:利變年金帳冊)
         , JOB_START_TIME                         AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                  AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                         AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                  AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM "MonthlyLoanBal" M
      LEFT JOIN "FacMain"     ON "FacMain"."CustNo"       = M."CustNo"
                             AND "FacMain"."FacmNo"       = M."FacmNo"
      LEFT JOIN "CustMain"    ON "CustMain"."CustNo"      = M."CustNo"
      LEFT JOIN "LoanBorMain"  LM ON LM."CustNo"   = M."CustNo"
                                 AND LM."FacmNo"   = M."FacmNo"
                                 AND LM."BormNo"   = M."BormNo"
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
      LEFT JOIN "FacCaseAppl" FCA ON FCA."ApplNo" = "FacMain"."ApplNo"
      LEFT JOIN "LoanSynd"    ON "LoanSynd"."SyndNo"      = FCA."SyndNo"
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
      LEFT JOIN ( SELECT L2."CustNo"                   AS  "CustNo"
                       , L2."FacmNo"                   AS  "FacmNo"
                       , L2."BormNo"                   AS  "BormNo"
                       , NVL(L2."OvduDate",0)          AS  "OvduDate"
                       , NVL(L2."BadDebtDate",0)       AS  "FirstBadDebtDate"
                  FROM "LoanOverdue" L2
                  WHERE L2."Status" IN (3)
                    AND L2."BadDebtDate" > 0
                ) OD2   ON OD2."CustNo"     = M."CustNo"
                       AND OD2."FacmNo"     = M."FacmNo"
                       AND OD2."BormNo"     = M."BormNo"
      -- 提息明細檔 回收本金
      LEFT JOIN ( SELECT "AcLoanInt"."CustNo"                 AS  "CustNo"
                       , "AcLoanInt"."FacmNo"                 AS  "FacmNo"
                       , "AcLoanInt"."BormNo"                 AS  "BormNo"
                       , SUM(NVL("AcLoanInt"."Principal",0))  AS  "Principal"
                       , SUM(NVL("AcLoanInt"."Interest",0))   AS  "Interest"
                  FROM "AcLoanInt"
                  WHERE "AcLoanInt"."YearMonth"  = YYYYMM
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
     WHERE  M."YearMonth"  =  YYYYMM
       AND  LM."CustNo" IS NOT NULL
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



-- 更新 EvaAmt 鑑估總值 (建物 & 土地)
    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt 不動產');

    MERGE INTO "JcicMonthlyLoanData" M
    USING ( SELECT "ClFac"."CustNo"
                 , "ClFac"."FacmNo"
                 , "ClFac"."ClCode1"
                 , "ClFac"."ClCode2"
--                 , SUM(NVL("ClImm"."SettingAmt",0))  AS "SettingAmt"
                 , SUM(NVL("ClFac"."OriSettingAmt",0))  AS "SettingAmt"  -- 使用原始鑑估值
            FROM  "ClFac"
--              LEFT JOIN "ClImm"   ON "ClImm"."ClCode1"   =  "ClFac"."ClCode1"
--                                 AND "ClImm"."ClCode2"   =  "ClFac"."ClCode2"
--                                 AND "ClImm"."ClNo"      =  "ClFac"."ClNo"
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
                               , SUM ( CASE 
                                         WHEN NVL(Tx."TitaHCode",' ') IN ('1','3') THEN  --減掉金額
                                              NVL(Tx."Principal", 0)  * (-1)  
                                         ELSE NVL(Tx."Principal", 0) 
                                       END )                           AS "Principal"
                               , SUM ( CASE 
                                         WHEN NVL(Tx."TitaHCode",' ') IN ('1','3') THEN  --減掉金額
                                              NVL(Tx."Interest", 0)  * (-1)  
                                         ELSE NVL(Tx."Interest", 0) 
                                       END )                           AS "Interest"
                               , SUM ( CASE 
                                         WHEN NVL(Tx."TitaHCode",' ') IN ('1','3') THEN  --減掉金額
                                              NVL(Tx."DelayInt", 0)  * (-1)  
                                         ELSE NVL(Tx."DelayInt", 0) 
                                       END )                           AS "DelayInt"
                               , SUM ( CASE 
                                         WHEN NVL(Tx."TitaHCode",' ') IN ('1','3') THEN  --減掉金額
                                              NVL(Tx."BreachAmt", 0)  * (-1)  
                                         ELSE NVL(Tx."BreachAmt", 0) 
                                       END )                           AS "BreachAmt"
                               , SUM ( CASE 
                                         WHEN NVL(Tx."TitaHCode",' ') IN ('1','3') THEN  --減掉金額
                                              NVL(Tx."CloseBreachAmt", 0)  * (-1)  
                                         ELSE NVL(Tx."CloseBreachAmt", 0) 
                                       END )                           AS "CloseBreachAmt"
                          FROM   "LoanBorTx" Tx
                          WHERE  TRUNC(Tx."AcDate" / 100)  = YYYYMM
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
                            AND  Ac."EntAc" > 0
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

-- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

-- 例外處理
   Exception
   WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L8_JcicMonthlyLoanData_Upd' -- UspName 預存程序名稱
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
