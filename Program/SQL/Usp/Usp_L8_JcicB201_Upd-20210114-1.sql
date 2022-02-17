-- 程式功能：維護 JcicB201 每月聯徵授信餘額月報資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB201_Upd"(20200930,'999999');
--

CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB201_Upd"
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
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB201');

    DELETE FROM "JcicB201"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB201');

    INSERT INTO "JcicB201"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '260'                                 AS "BankItem"          -- 總行代號
         , '2458'                                AS "BranchItem"        -- 分行代號
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
                                                 AS "AcctNo"            -- 本筆撥款帳號  --??? 須比照 SKL 現行編碼
         , 0                                     AS "TotalAmt"          -- 金額合計  (後面更新處理)
         , M."CustId"                            AS "CustId"            -- 授信戶IDN/BAN
         , ' '                                   AS "CustIdErr"         -- 上欄IDN或BAN錯誤註記
         , M."SuvId"                             AS "SuvId"             -- 負責人IDN/負責之事業體BAN
         , ' '                                   AS "SuvIdErr"          -- 上欄IDN或BAN錯誤註記
         , M."OverseasId"                        AS "OverseasId"        -- 外僑兼具中華民國國籍IDN
         , M."IndustryCode"                      AS "IndustryCode"      -- 授信戶行業別
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
             WHEN M."EntCode" IN ('1', '2') THEN 'X'
             ELSE 'N'
           END                                   AS "ConsumeFg"         -- 個人消費性貸款註記  Y:個人消費性貸款 N:個人非消費性貸款 X:非屬個人貸款
         , CASE
             WHEN M."EntCode" IN ('1', '2') THEN 'K'
             ELSE 'M'    -- ???
           END                                   AS "FinCode"           -- 融資分類  法人:K 非屬前述特殊融資之其他一般法人金融貸款
                                                                        --           個人:L 購買住宅貸款(非自用)    M 購買住宅貸款(自用)
                                                                        --                N 一般房屋抵押貸款(房屋修繕貸款)
                                                                        --                Z 其他個人金融貸款        1 個人投資理財貸款
         , 'XX'                                  AS "ProjCode"          -- 政府專案補助貸款分類  --???
         , ' '                                   AS "NonCreditCode"     -- 不計入授信項目        --???
         , CASE
             WHEN M."UsageCode" IN ('2','3','4') THEN '1'  --2: 購置不動產 3: 營業用資產 4: 固定資產
             WHEN M."UsageCode" IN ('6') THEN '2'          --6: 購置動產
             WHEN M."UsageCode" IN ('5') THEN '3'          --5: 企業投資
             WHEN M."UsageCode" IN ('1') THEN '4'          --1: 週轉金
             ELSE '1'
           END                                   AS "UsageCode"         -- 用途別 1:購置不動產 2:購置動產 3:企業投資 4:週轉金
         , NVL(M."StoreRate",0)                  AS "ApproveRate"       -- 本筆撥款利率
         , CASE
             WHEN FLOOR(NVL(M."DrawdownDate",0) / 100) < 191100 THEN FLOOR(NVL(M."DrawdownDate",0) / 100)
             ELSE FLOOR(NVL(M."DrawdownDate",0) / 100) - 191100
           END                                   AS "DrawdownDate"      -- 本筆撥款開始年月
         , CASE
             WHEN FLOOR(NVL(M."MaturityDate",0) / 100) < 191100 THEN FLOOR(NVL(M."MaturityDate",0) / 100)
             ELSE FLOOR(NVL(M."MaturityDate",0) / 100) - 191100
           END                                   AS "MaturityDate"      -- 本筆撥款約定清償年月
         , M."CurrencyCode"                      AS "CurrencyCode"      -- 授信餘額幣別
         , ROUND(NVL(M."DrawdownAmt",0)/1000, 0) AS "DrawdownAmt"       -- 訂約金額(台幣)
         , 0                                     AS "DrawdownAmtFx"     -- 訂約金額(外幣)
         , CASE
             WHEN M."RecycleCode" IN ('1') THEN 'Y'
             WHEN M."RecycleCode" IN ('0') THEN 'N'
             ELSE ' '
           END                                   AS "RecycleCode"       -- 循環信用註記 'Y':是 'N':否
         , CASE
             WHEN M."IrrevocableFlag" IN ('N') THEN 'Y'
             WHEN M."IrrevocableFlag" IN ('Y') THEN 'N'
             ELSE ' '
           END                                   AS "IrrevocableFlag"   -- 額度可否撤銷 'Y':可撤銷 'N':不可撤銷
         , CASE
             WHEN NVL(M."BormNo",0) = 0 THEN LPAD('9', 50, '9')
             ELSE TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."FacmNo",'000'))
           END                                   AS "FacmNo"            -- 上階共用額度控制編碼
         , CASE
             WHEN ROUND( NVL(M."LoanBal",0) / 1000, 0) = 0 THEN 1
             ELSE ROUND( NVL(M."LoanBal",0) / 1000, 0)
           END                                   AS "UnDelayBal"        -- 未逾期/乙類逾期/應予觀察授信餘額(台幣千元)  (先視為未逾期，後面再判斷若為甲類逾期，再更新)
         , 0                                     AS "UnDelayBalFx"      -- 未逾期/乙類逾期/應予觀察授信餘額(外幣)
         , 0                                     AS "DelayBal"          -- 逾期未還餘額（台幣千元）  (後面再更新)
         , 0                                     AS "DelayBalFx"        -- 逾期未還餘額（外幣）
         , '9'                                   AS "DelayPeriodCode"   -- 逾期期限  (先視為未逾期，後面再判斷若為甲類逾期，再更新)
         , ' '                                   AS "RepayCode"         -- 本月還款紀錄  (後面再更新)
         , ROUND((NVL(M."PrevAmt",0) + NVL(M."IntAmt",0)) / 1000, 3)
                                                 AS "PayAmt"            -- 本月（累計）應繳金額
         , ROUND(NVL(M."PrevAmtRcv",0) / 1000,3) AS "Principal"         -- 本月收回本金
         , ROUND(NVL(M."IntAmtRcv",0) / 1000,3)  AS "Interest"          -- 本月收取利息
         , ROUND(NVL(M."FeeAmtRcv",0) / 1000,3)  AS "Fee"               -- 本月收取其他費用
         , CASE
             WHEN NVL(M."RepaidEndMon",0) > 3                             THEN '1'
             WHEN NVL(M."RepaidEndMon",0) > 0 AND NVL(M."DispDate",0) > 0 THEN '1'  --1 放款本金超過清償期三個月而未獲清償，或雖未屆滿三個月，但已向主、從債務人訴追獲處分擔保品者
             WHEN NVL(M."IntDelayMon",0)  > 6                             THEN '2'  --2 放款本金未超過清償期三個月，而利息未按期繳納超過六個月者
             WHEN NVL(M."IntDelayMon",0)  > 0 AND NVL(M."DispDate",0) > 0 THEN '3'  --3 分期償還放款未按期攤還超過六個月，或雖未屆滿六個月，但已向主、從債務人訴追獲處分擔保品者
          -- WHEN ???                                                     THEN '4'  --4 協議分期償還放款符合一定條件而曾經免列報逾期放款案件，於免列報期間再發生未依約清償超過三個月者
             ELSE 'X'
           END                                   AS "FirstDelayCode"    -- 甲類逾期放款分類
         , CASE
             WHEN  NVL(M."RepaidEndMon",0) >  0
               AND NVL(M."RepaidEndMon",0) <= 3
               AND NVL(M."IntDelayMon",0)  >  3 THEN '1'
             WHEN  NVL(M."IntDelayMon",0)  >  3
               AND NVL(M."IntDelayMon",0)  <= 6 THEN '1'  --1 放款本金未超過清償期三個月，惟利息未按期繳納超過清償期三個月，分期償還放款未按期攤還超過三個月至六個月者
           --WHEN ???  --3 協議分期償還放款，協議條件符合規定，且借款戶依協議條件按期履約未滿六個月者
           --WHEN ???  --4 已獲信用保證基金同意理賠款項或有足額存單或存款備償，而約定待其他債務人財產處分後再予沖償者
           --WHEN ???  --5 已確定分配之債權，惟尚未接獲分配款者
           --WHEN ???  --6 債務人兼擔保品提供人死亡，於辦理繼承期間，借期而為清償之放款，其繳息正常且有十足擔保者
           --WHEN ???  --7 其他(如921震災展延本息之放款)
             ELSE 'X'
          END                                    AS "SecondDelayCode"   -- 乙類逾期放款分類
         , ' '            AS "BadDebtCode"       -- 不良債權處理註記
         , ' '            AS "NegStatus"         -- 債權結束註記
         , ' '            AS "NegCreditor"       -- 債權處理後新債權人ID/債權轉讓後前手債權人ID/信保基金退理賠信用保證機構BAN
         , ' '            AS "NegNo"             -- 債權處理案號
         , ' '            AS "NegTransYM"        -- 債權轉讓年月/債權轉讓後原債權機構買回年月
         , ' '                                   AS "Filler443"         -- 空白
         , M."ClType"                            AS "ClType"            -- 擔保品組合型態
         , ROUND(NVL(M."EvaAmt",0) / 1000, 0)    AS "ClEvaAmt"          -- 擔保品(合計)鑑估值
         , CASE
             WHEN M."ClTypeCode" IS NULL THEN ' '
             WHEN LENGTH(M."ClTypeCode") < 3 THEN M."ClTypeCode"
             ELSE SUBSTR(M."ClTypeCode",1,2)
           END                                   AS "ClTypeCode"        -- 擔保品類別  (取前兩碼對照 JCIC 擔保品類別)
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
         , ' '                                   AS "Filler533"         -- 空白
         , NVL(M."GuaTypeCode1",' ')             AS "GuaTypeCode1"      -- 共同債務人或債務關係人身份代號1
         , NVL(M."GuaId1",' ')                   AS "GuaId1"            -- 共同債務人或債務關係人身份統一編號1
         , ' '                                   AS "GuaIdErr1"         -- 上欄IDN或BAN錯誤註記
         , NVL(M."GuaRelCode1",' ')              AS "GuaRelCode1"       -- 與主債務人關係1
         , NVL(M."GuaTypeCode2",' ')             AS "GuaTypeCode2"      -- 共同債務人或債務關係人身份代號2
         , NVL(M."GuaId2",' ')                   AS "GuaId2"            -- 共同債務人或債務關係人身份統一編號2
         , ' '                                   AS "GuaIdErr2"         -- 上欄IDN或BAN錯誤註記
         , NVL(M."GuaRelCode2",' ')              AS "GuaRelCode2"       -- 與主債務人關係2
         , NVL(M."GuaTypeCode3",' ')             AS "GuaTypeCode3"      -- 共同債務人或債務關係人身份代號3
         , NVL(M."GuaId3",' ')                   AS "GuaId3"            -- 共同債務人或債務關係人身份統一編號3
         , ' '                                   AS "GuaIdErr3"         -- 上欄IDN或BAN錯誤註記
         , NVL(M."GuaRelCode3",' ')              AS "GuaRelCode3"       -- 與主債務人關係3
         , NVL(M."GuaTypeCode4",' ')             AS "GuaTypeCode4"      -- 共同債務人或債務關係人身份代號4
         , NVL(M."GuaId4",' ')                   AS "GuaId4"            -- 共同債務人或債務關係人身份統一編號4
         , ' '                                   AS "GuaIdErr4"         -- 上欄IDN或BAN錯誤註記
         , NVL(M."GuaRelCode4",' ')              AS "GuaRelCode4"       -- 與主債務人關係4
         , NVL(M."GuaTypeCode5",' ')             AS "GuaTypeCode5"      -- 共同債務人或債務關係人身份代號5
         , NVL(M."GuaId5",' ')                   AS "GuaId5"            -- 共同債務人或債務關係人身份統一編號5
         , ' '                                   AS "GuaIdErr5"         -- 上欄IDN或BAN錯誤註記
         , NVL(M."GuaRelCode5",' ')              AS "GuaRelCode5"       -- 與主債務人關係5
         , ' '                                   AS "Filler741"         -- 空白
         , ' '                                   AS "Filler742"         -- 空白
         , CASE
             WHEN FLOOR(NVL(M."BadDebtDate",0) / 100) < 191100 THEN FLOOR(NVL(M."BadDebtDate",0) / 100)
             ELSE FLOOR(NVL(M."BadDebtDate",0) / 100) - 191100
           END                                   AS "BadDebtDate"       -- 呆帳轉銷年月
         , ' '                                   AS "SyndCode"          -- 聯貸主辦(管理)行註記  --???
         , 0                                     AS "BankruptDate"      -- 破產宣告日(或法院裁定開始清算日)  --???
         , 'N'                                   AS "BdLoanFg"          -- 建築貸款註記
         , 0                                     AS "SmallAmt"          -- 授信餘額列報1（千元）之原始金額（元） --（後續更新處理）
         , ' '                                   AS "ExtraAttrCode"     -- 補充揭露案件註記－案件屬性
         , ' '                                   AS "ExtraStatusCode"   -- 補充揭露案件註記－案件情形
         , ' '                                   AS "Filler74A"         -- 空白
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月
         , ' '                                   AS "DataEnd"           -- 資料結束註記
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "JcicMonthlyLoanData" M
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
        LEFT JOIN ( SELECT  N."CustNo"
                         ,  N."CaseSeq"
                         ,  ROW_NUMBER() Over (Partition By "CustNo"
                                               Order By "CaseSeq" DESC)
                                            AS ROW_NO
                    FROM  "NegMain" N
                  ) N            ON N."CustNo"    = M."CustNo"
                                AND N.ROW_NO      = 1
     WHERE  M."DataYM"     =  YYYYMM
       AND  M."Status" IN (0, 2, 4, 6, 7)
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB201 END: INS_CNT=' || INS_CNT);


--   更新 SecondDelayCode 乙類逾期放款分類 (與 '甲類逾期放款分類' 則一填報)

    DBMS_OUTPUT.PUT_LINE('UPDATE SecondDelayCode ');

    UPDATE "JcicB201" M
    SET   M."SecondDelayCode" = 'X'
    WHERE M."DataYM"         =  YYYYMM
      AND M."FirstDelayCode" <> 'X' ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE SecondDelayCode END');


--  甲類逾期：更新 DelayPeriodCode 逾期期限, DelayBal 逾期未還餘額（台幣）

    DBMS_OUTPUT.PUT_LINE('UPDATE DelayPeriodCode, DelayBal ');

    MERGE INTO "JcicB201" M
    USING ( SELECT M."DataYM"
                 , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."FacmNo",'000')) || TRIM(to_char(M."BormNo",'000')) AS "AcctNo"
                 , M."LoanBal"
                 , NVL(M."IntDelayMon",0)   AS "IntDelayMon"
                 , NVL(M."RepayDelayMon",0) AS "RepayDelayMon"
                 , NVL(M."RepaidEndMon",0)  AS "RepaidEndMon"
            FROM "JcicMonthlyLoanData" M
            WHERE M."DataYM" = YYYYMM ) B
    ON (    M."DataYM"    = YYYYMM
        AND M."AcctNo"    = B."AcctNo"
        AND ( M."FirstDelayCode" IN ('1','2','3','4','A','B') OR M."AcctCode" IN ('A','B')) )
    WHEN MATCHED THEN UPDATE
      SET M."DelayBal" = CASE
                           WHEN ROUND( NVL(B."LoanBal",0) / 1000, 0) = 0 THEN 1
                           ELSE ROUND( NVL(B."LoanBal",0) / 1000, 0)
                         END
        , M."UnDelayBal" = 0
        , M."DelayPeriodCode" = CASE
                                  WHEN greatest(B."IntDelayMon", B."RepayDelayMon", B."RepaidEndMon") =   0 THEN '9'
                                  WHEN greatest(B."IntDelayMon", B."RepayDelayMon", B."RepaidEndMon") <   3 THEN '0'
                                  WHEN greatest(B."IntDelayMon", B."RepayDelayMon", B."RepaidEndMon") <   6 THEN '1'
                                  WHEN greatest(B."IntDelayMon", B."RepayDelayMon", B."RepaidEndMon") <  12 THEN '2'
                                  WHEN greatest(B."IntDelayMon", B."RepayDelayMon", B."RepaidEndMon") <  24 THEN '3'
                                  WHEN greatest(B."IntDelayMon", B."RepayDelayMon", B."RepaidEndMon") >= 24 THEN '4'
                                  ELSE '9'
                                END
      ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE DelayPeriodCode, DelayBal END');


--   更新 SmallAmt 授信餘額列報1（千元）之原始金額（元）

    DBMS_OUTPUT.PUT_LINE('UPDATE SmallAmt ');

    MERGE INTO "JcicB201" M
    USING ( SELECT M."DataYM"
                 , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."FacmNo",'000')) || TRIM(to_char(M."BormNo",'000')) AS "AcctNo"
                 , M."LoanBal"
            FROM "JcicMonthlyLoanData" M
            WHERE M."DataYM" = YYYYMM ) B
    ON (    M."DataYM"    = YYYYMM
        AND M."AcctNo"    = B."AcctNo" )
    WHEN MATCHED THEN UPDATE
      SET M."SmallAmt" = CASE
                           WHEN M."UnDelayBal" = 1 OR M."UnDelayBalFx" = 1 OR M."DelayBal" = 1 OR M."DelayBalFx" = 1 THEN B."LoanBal"
                           ELSE 0
                         END
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE SmallAmt END');


--   更新 TotalAmt 金額合計

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

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB201_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
