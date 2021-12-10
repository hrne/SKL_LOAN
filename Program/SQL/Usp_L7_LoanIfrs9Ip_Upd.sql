
CREATE OR REPLACE PROCEDURE "Usp_L7_LoanIfrs9Ip_Upd"
(
-- 程式功能：維護 LoanIfrs9Ip 每月IFRS9欄位清單9
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_LoanIfrs9Ip_Upd"(20201231,'System',0);
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
    DBMS_OUTPUT.PUT_LINE('DELETE LoanIfrs9Ip');

    DELETE FROM "LoanIfrs9Ip"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Ip');
    INS_CNT := 0;

    INSERT INTO "LoanIfrs9Ip"
    SELECT
           YYYYMM                                    AS "DataYM"            -- 資料年月
         , M."CustNo"                                AS "CustNo"            -- 戶號
         , NVL(M."CustId",' ')                       AS "CustId"            -- 借款人ID / 統編
         , M."FacmNo"                                AS "FacmNo"            -- 額度編號
         , NVL(M."ApplNo",0)                         AS "ApplNo"            -- 核准號碼
         , NVL(M."DrawdownFg",0)                     AS "DrawdownFg"        -- 已核撥記號 (0:未核撥 1:已核撥)
         , NVL(M."ApproveDate",0)                    AS "ApproveDate"       -- 核准日期(額度)
         , NVL(M."FirstDrawdownDate",0)              AS "FirstDrawdownDate" -- 初貸日期
         , NVL(M."LineAmt",0)                        AS "LineAmt"           -- 核准金額
         --, NVL(M."AcctFee",0)                        AS "AcctFee"           -- 帳管費
         , CASE 
             WHEN TRUNC(NVL(M."FirstDrawdownDate",0) / 100) > YYYYMM   
                  THEN  NVL(M."AcctFee",0)
             ELSE 0
           END                                       AS "AcctFee"           -- 帳管費
         , NVL(M."LawFee",0) + NVL(M."FireFee",0)    AS "Fee"               -- 法拍及火險費用
         , NVL(F."ApproveRate", 0) / 100             AS "ApproveRate"       -- 核准利率
         , NVL(M."GracePeriod",0)                    AS "GracePeriod"       -- 初貸時約定還本寬限期
         --, NVL(M."AmortizedCode",'0')              AS "AmortizedCode"     -- 契約當時還款方式(月底日)
         , CASE
             WHEN NVL(M."AmortizedCode",'0') ='1'    THEN '1'    
             WHEN NVL(M."AmortizedCode",'0') ='2'    THEN '4'    
             WHEN NVL(M."AmortizedCode",'0') ='3'    THEN '2'    
             WHEN NVL(M."AmortizedCode",'0') ='4'    THEN '3' 
             ELSE NVL(M."AmortizedCode",'0')   
           END                                       AS "AmortizedCode"     -- 契約當時還款方式(月底日)
         , CASE
             WHEN NVL(M."Ifrs9StepProdCode",' ') = 'B' THEN '4'  -- 浮動階梯
             WHEN NVL(M."Ifrs9StepProdCode",' ') = 'A' THEN '3'  -- 固定階梯
             WHEN NVL(M."RateCode", '0')  IN ('1','3')    THEN '1'  -- 機動
             WHEN NVL(M."RateCode", '0')  = '2'           THEN '2'  -- 固定
             ELSE NVL(M."RateCode", '0')
           END                                       AS "RateCode"          -- 契約當時利率調整方式(月底日)
                                                                               --(1=機動 2=固定 3=固定階梯 4=浮動階梯)
         , NVL(M."RepayFreq",0)                      AS "RepayFreq"         -- 契約約定當時還本週期(月底日)
         , NVL(M."PayIntFreq",0)                     AS "PayIntFreq"        -- 契約約定當時繳息週期(月底日)
         , CASE WHEN TRIM(NVL(M."IndustryCode", ' ')) = '' THEN ' '
                ELSE SUBSTR('000000' || TRIM(M."IndustryCode"), -6)
           END                                       AS "IndustryCode"      -- 授信行業別
         , NVL(M."ClTypeJCIC", ' ')                  AS "ClTypeJCIC"        -- 擔保品類別
         , CASE WHEN to_number(NVL(trim(NVL(M."CityCode",' ')),0)) =  5 THEN  'A'  -- A=台北市
                WHEN to_number(NVL(trim(NVL(M."CityCode",' ')),0)) = 10 THEN  'B'  -- B=新北市
                WHEN to_number(NVL(trim(NVL(M."CityCode",' ')),0)) = 15 THEN  'C'  -- C=桃園市
                WHEN to_number(NVL(trim(NVL(M."CityCode",' ')),0)) = 35 THEN  'D'  -- D=台中市
                WHEN to_number(NVL(trim(NVL(M."CityCode",' ')),0)) = 65 THEN  'E'  -- E=台南市
                WHEN to_number(NVL(trim(NVL(M."CityCode",' ')),0)) = 70 THEN  'F'  -- F=高雄市
                ELSE 'G'                                            -- G=其他
           END                                       AS "CityCode"          -- 擔保品地區別
         , NVL(M."ProdNo", ' ')                      AS "ProdNo"            -- 商品利率代碼
         , CASE WHEN NVL(M."EntCode",' ') IN ('1','2') THEN 1
                ELSE 2
           END                                       AS "CustKind"          -- 1=企業戶(含企金自然人)/2=個人戶
         , NVL(M."Ifrs9ProdCode", ' ')               AS "Ifrs9ProdCode"     -- 產品別
         , NVL(M."EvaAmt", 0)                        AS "EvaAmt"            -- 原始鑑價金額
         , NVL(M."LineAmt",0)                        AS "AvblBal"           -- 可動用餘額(台幣) = 核准金額(台幣) ???
         , to_char(NVL(M."RecycleCode",0))           AS "RecycleCode"       -- 該筆額度是否可循環動用  -- 0:非循環 1:循環
         , to_char(NVL(M."IrrevocableFlag",0))       AS "IrrevocableFlag"   -- 該筆額度是否為不可撤銷  -- 1=是 0=否
         , '00' || trim(to_char(NVL(F."LoanTermYy",0),'00'))
                || trim(to_char(NVL(F."LoanTermMm",0),'00'))
                || trim(to_char(NVL(F."LoanTermDd",0),'00'))
                                                     AS "LoanTer"           -- 合約期限
         --, CASE WHEN NewAcFg = 0 THEN RPAD(NVL("CdAcCode"."AcNoCodeOld",' '),8,' ')
         , CASE WHEN NewAcFg = 0 AND NVL(M."IrrevocableFlag",0) = 1 THEN '91300000'
                WHEN NewAcFg = 0 AND NVL(M."IrrevocableFlag",0) = 0 THEN '91500000'
                ELSE                  RPAD(NVL("CdAcCode"."AcNoCode",' '),11,' ')
           END                                       AS "AcCode"            -- 備忘分錄會計科目
         , 1                                         AS "AcCurcd"           -- 記帳幣別 1=台幣 2=美元 3=澳幣 4=人民幣 5=歐元
         , CASE WHEN M."AcSubBookCode" IS NULL THEN '1'
                WHEN M."AcSubBookCode" = '201' THEN '3'
                ELSE '1'
           END                                       AS "AcBookCode"        -- 會計帳冊 (1=一般 2=分紅 3=利變 4=OIU)
         , 'NTD'                                     AS "CurrencyCode"      -- 交易幣別 NTD
         , 1                                         AS "ExchangeRate"      -- 報導日匯率
         , 0                                         AS "LineAmtCurr"       -- 核准金額(交易幣) (後面再更新)
         , 0                                         AS "AcctFeeCurr"       -- 帳管費(交易幣) (後面再更新)
         , 0                                         AS "FeeCurr"           -- 法拍及火險費用(交易幣) (後面再更新)
         , JOB_START_TIME                            AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                     AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                            AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                     AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Ifrs9FacData" M
      LEFT JOIN "FacMain"  F   ON  F."CustNo" = M."CustNo"
                              AND  F."FacmNo" = M."FacmNo"
      LEFT JOIN "CdAcCode"     ON  "CdAcCode"."AcctCode"  = F."AcctCode"
    WHERE  TRUNC(NVL(M."ApproveDate",0) / 100) <= YYYYMM     -- 核准日期＞月底日時，不選入
      AND  (    ( M."DrawdownFg" = 0 AND TRUNC(NVL(M."UtilDeadline",0) / 100 ) >= YYYYMM )
             OR ( M."DrawdownFg" = 1 AND TRUNC(NVL(M."FirstDrawdownDate",0) / 100) > YYYYMM AND
                  TRUNC(NVL(M."UtilDeadline",0) / 100) >= YYYYMM
                )
           )    -- 已核貸未曾動撥且仍可動撥之放款額度編號
                -- 已動撥 且 首次撥款日＞月底日 且 動支期限＞＝月底日 (視為未動撥，金額要加回可動用額度)
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Ip END');
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Ip INS_CNT=' || INS_CNT);


-- 更新 Curr [交易幣]金額
    DBMS_OUTPUT.PUT_LINE('UPDATE Curr [交易幣]金額');
    UPD_CNT := 0;

    UPDATE "LoanIfrs9Ip" M
    SET   M."LineAmtCurr"      =  M."LineAmt"        -- 核准金額(交易幣)
        , M."AcctFeeCurr"      =  M."AcctFee"        -- 帳管費(交易幣)
        , M."FeeCurr"          =  M."Fee"            -- 法拍及火險費用(交易幣)
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


--       , 0      AS "ApplNo"            -- 核准號碼
--       , 0      AS "DrawdownFg"        -- 已核撥記號 (0:未核撥 1:已核撥)
--       , 0      AS "ApproveDate"       -- 核准日期(額度)
--       , 0      AS "FirstDrawdownDate" -- 初貸日期
--       , 0      AS "LineAmt"           -- 核准金額
--       , 0      AS "AcctFee"           -- 帳管費
--       , 0      AS "Fee"               -- 法拍及火險費用
--       , 0      AS "ApproveRate"       -- 核准利率
--       , 0      AS "GracePeriod"       -- 初貸時約定還本寬限期
--       , ' '    AS "AmortizedCode"     -- 契約當時還款方式(月底日)
--       , ' '    AS "RateCode"          -- 契約當時利率調整方式(月底日)
--       , 0      AS "RepayFreq"         -- 契約約定當時還本週期(月底日)
--       , 0      AS "PayIntFreq"        -- 契約約定當時繳息週期(月底日)
--       , ' '    AS "IndustryCode"      -- 授信行業別
--       , ' '    AS "ClTypeJCIC"        -- 擔保品類別
--       , ' '    AS "CityCode"          -- 擔保品地區別
--       , ' '    AS "ProdNo"            -- 商品利率代碼
--       , 0      AS "CustKind"          -- 1=企業戶(含企金自然人)/2=個人戶
--       , ' '    AS "Ifrs9ProdCode"     -- 產品別
--       , 0      AS "EvaAmt"            -- 原始鑑價金額
--       , 0      AS "AvblBal"           -- 可動用餘額(台幣) = 核准金額(台幣) ???
--       , ' '    AS "RecycleCode"       -- 該筆額度是否可循環動用  -- 0:非循環 1:循環
--       , ' '    AS "IrrevocableFlag"   -- 該筆額度是否為不可撤銷  -- 1=是 0=否
--       , ' '    AS "LoanTer"           -- 合約期限
--       , ' '    AS "AcCode"            -- 備忘分錄會計科目(8碼)
--       , 0      AS "AcCurcd"           -- 記帳幣別 1=台幣 2=美元 3=澳幣 4=人民幣 5=歐元
--       , ' '    AS "AcBookCode"        -- 會計帳冊 (1=一般 2=分紅 3=利變 4=OIU)
--       , ' '    AS "CurrencyCode"      -- 交易幣別 NTD
--       , 0      AS "ExchangeRate"      -- 報導日匯率
--       , 0      AS "LineAmtCurr"       -- 核准金額(交易幣) (後面再更新)
--       , 0      AS "AcctFeeCurr"       -- 帳管費(交易幣) (後面再更新)
--       , 0      AS "FeeCurr"           -- 法拍及火險費用(交易幣) (後面再更新)

