create or replace NONEDITIONABLE PROCEDURE "Usp_L9_MonthlyFacBal_Upd" 
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
    "ThisMonthEndDate" INT;     -- 本月月底日
  BEGIN
    INS_CNT :=0;
    UPD_CNT :=0;

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

    SELECT "TmnDyf"
    INTO "ThisMonthEndDate"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE MonthlyFacBal');

    DELETE FROM "MonthlyFacBal"
    WHERE "YearMonth" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT MonthlyFacBal');

    INSERT INTO "MonthlyFacBal"
    SELECT
           YYYYMM                     AS "YearMonth"           -- 資料年月
          ,L."CustNo"                 AS "CustNo"              -- 戶號
          ,L."FacmNo"                 AS "FacmNo"              -- 額度
          ,L."PrevIntDate"            AS "PrevIntDate"         -- 繳息迄日
          ,L."NextIntDate"            AS "NextIntDate"         -- 應繳息日
          -- 最近應繳日:已到期又逾期時,使用月底日曆日(SKL待查)
          ,0                          AS "DueDate"             -- 最近應繳日
          -- 若 應繳息日 < 系統營業日("ThisMonthEndDate")
          -- 則 計算逾期期數
          -- 否則 擺零
          ,CASE
             WHEN NVL(B."CustNo",0) = 0
             THEN L."OvduTerm" 
             WHEN B."MaturityDate" < "ThisMonthEndDate"
              AND B."MaturityDate" < B."NextPayIntDate" -- 應繳日>到期日時用到期日計算
              AND L."PrinBalance" + L."BadDebtBal" <> 0
             THEN TRUNC(MONTHS_BETWEEN(TO_DATE("ThisMonthEndDate",'YYYY-MM-DD'), TO_DATE(B."MaturityDate",'YYYY-MM-DD')))
             WHEN B."NextPayIntDate" < "ThisMonthEndDate" AND B."NextPayIntDate" > 0
              AND L."PrinBalance" + L."BadDebtBal" <> 0
             THEN TRUNC(MONTHS_BETWEEN(TO_DATE("ThisMonthEndDate",'YYYY-MM-DD'), TO_DATE(B."NextPayIntDate",'YYYY-MM-DD')))
           ELSE 0 END                 AS "OvduTerm"            -- '逾期期數';
          -- 若 應繳息日 <= 系統營業日("ThisMonthEndDate")
          -- 則 計算逾期天數
          -- 否則 擺零
          ,CASE
             WHEN NVL(B."CustNo",0) = 0
             THEN L."OvduDays" 
             WHEN B."MaturityDate" < "ThisMonthEndDate"
              AND B."MaturityDate" < B."NextPayIntDate" -- 應繳日>到期日時用到期日計算
              AND L."PrinBalance" + L."BadDebtBal" <> 0
             THEN TO_DATE("ThisMonthEndDate",'YYYY-MM-DD')  - TO_DATE(B."MaturityDate",'YYYY-MM-DD') 
             WHEN B."NextPayIntDate" <= "ThisMonthEndDate" AND B."NextPayIntDate" > 0
              AND L."PrinBalance" + L."BadDebtBal" <> 0
             THEN TO_DATE("ThisMonthEndDate",'YYYY-MM-DD')  - TO_DATE(B."NextPayIntDate",'YYYY-MM-DD') 
           ELSE 0 END                 AS "OvduDays"            -- '逾期天數';
          ,L."CurrencyCode"           AS "CurrencyCode"        -- 幣別
          ,L."PrinBalance"            AS "PrinBalance"         -- 本金餘額
          ,L."BadDebtBal"             AS "BadDebtBal"          -- 呆帳餘額
          ,L."AccCollPsn"             AS "AccCollPsn"          -- 催收員
          ,L."LegalPsn"               AS "LegalPsn"            -- 法務人員
          ,DECODE(L."Status", 4, 0, L."Status")
                                      AS "Status"              -- 戶況
          ,L."AcctCode"               AS "AcctCode"            -- 業務科目代號
          ,L."FacAcctCode"            AS "FacAcctCode"         -- 額度業務科目
          ,L."ClCustNo"               AS "ClCustNo"            -- 同擔保品戶號
          ,L."ClFacmNo"               AS "ClFacmNo"            -- 同擔保品額度
          ,L."ClRowNo"                AS "ClRowNo"             -- 同擔保品序列號
          ,L."RenewCode"              AS "RenewCode"           -- 展期記號
          ,FAC."ProdNo"               AS "ProdNo"              -- 商品代碼
          ,'000'                      AS "AcBookCode"          -- 帳冊別
          , NULL                      AS "EntCode"             -- 企金別
          , NULL                      AS "RelsCode"            -- (準)利害關係人職稱
          , NULL                      AS "DepartmentCode"      -- 案件隸屬單位
          , 0                         AS "UnpaidPrincipal"     -- 已到期回收本金
          , 0                         AS "UnpaidInterest"      -- 已到期利息
          , 0                         AS "UnpaidBreachAmt"     -- 已到期違約金
          , 0                         AS "UnpaidDelayInt"      -- 已到期延滯息
          , 0                         AS "AcdrPrincipal"       -- 未到期回收本金
          , 0                         AS "AcdrInterest"        -- 未到期利息
          , 0                         AS "AcdrBreachAmt"       -- 未到期違約金
          , 0                         AS "AcdrDelayInt"        -- 未到期延滯息
          , 0                         AS "FireFee"             -- 火險費用 DECIMAL 16 2
          , 0                         AS "LawFee"              -- 法務費用 DECIMAL 16 2
          , 0                         AS "ModifyFee"           -- 契變手續費 DECIMAL 16 2
          , 0                         AS "AcctFee"             -- 帳管費用 DECIMAL 16 2
          , 0                         AS "ShortfallPrin"       -- 短繳本金 DECIMAL 16 2
          , 0                         AS "ShortfallInt"        -- 短繳利息 DECIMAL 16 2
          , 0                         AS "TempAmt"             -- 暫收金額 DECIMAL 16 2
          , 0                         AS "ClCode1"             -- 主要擔保品代號1
          , 0                         AS "ClCode2"             -- 主要擔保品代號2
          , 0                         AS "ClNo"                -- 主要擔保品編號
          , NULL                      AS "CityCode"            -- 主要擔保品地區別 
          , 0                         as "OvduDate"            -- 轉催收日期
          , 0                         AS "OvduPrinBal"         -- 催收本金餘額           
          , 0                         AS "OvduIntBal"          -- 催收利息餘額           
          , 0                         AS "OvduBreachBal"       -- 催收違約金餘額          
          , 0                         AS "OvduBal"             -- 催收餘額(呆帳餘額)   
          , 0                         AS "LawAmount"           -- 無擔保債權設定金額(法務進度:901)
          , CASE WHEN L."OvduTerm" > 12                        -- 三	逾繳超過清償期12月者                   
                      THEN '3'
                 WHEN L."OvduTerm" >= 7                        -- 二之3 逾繳超過清償期7-12月者
                      THEN '23'
                 WHEN L."OvduTerm" >= 1                        -- 二之2 逾繳超過清償期1-6月者 
                      THEN '22'
                 WHEN FAC."ProdNo" IN('60','61','62')          -- 二之1 債信已不良者（有擔保分期協議且正常還款者） 
                      THEN '2'                                 --  60:協議還款-定期機動 61:協議還款-機動 62:協議還款-固定
                                                               -- 20220512 改由後續程式判斷
            END                       AS "AssetClass"          -- 資產五分類代號
          , 0                         AS "StoreRate"           -- 計息利率
          ,JOB_START_TIME             AS "CreateDate"          -- 建檔日期時間
          ,EmpNo                      AS "CreateEmpNo"         -- 建檔人員
          ,JOB_START_TIME             AS "LastUpdate"          -- 最後更新日期時間
          ,EmpNo                      AS "LastUpdateEmpNo"     -- 最後更新人員
          ,'00A'                      AS "AcSubBookCode"       -- 區隔帳冊
    FROM "CollList" L
    LEFT JOIN "FacMain" FAC ON FAC."CustNo" = L."CustNo"
                           AND FAC."FacmNo" = L."FacmNo"
    LEFT JOIN (
      SELECT "CustNo" -- '戶號'
           , "FacmNo" -- '額度'
           , MIN(CASE
                   WHEN "Status" IN (3,5,6,8,9)
                   THEN  99999999
                 ELSE "NextPayIntDate" END
                ) AS "NextPayIntDate" -- '應繳息日'
           , MAX("MaturityDate") AS "MaturityDate"
      FROM "LoanBorMain"
      WHERE "Status" in (0,2,3,4,5,6,7,8,9)
        AND "DrawdownDate" <= TBSDYF
      GROUP BY "CustNo"
             , "FacmNo"
    ) B ON B."CustNo" = L."CustNo"
       AND B."FacmNo" = L."FacmNo"
    WHERE L."CaseCode" = 1
    ;

    INS_CNT := INS_CNT + sql%rowcount;

--   更新 AcBookCode 帳冊別

    DBMS_OUTPUT.PUT_LINE('UPDATE AcSubBookCode ');

    MERGE INTO "MonthlyFacBal" M
    USING (SELECT M."CustNo"
                , M."FacmNo"
                , MAX(NVL(A."AcSubBookCode",'00A')) AS "AcSubBookCode" 
            FROM "MonthlyFacBal" M
           LEFT JOIN "AcReceivable" A
            ON  A."AcctCode" = M."AcctCode"
            AND A."CustNo"   = M."CustNo"
            AND A."FacmNo"   = M."FacmNo"
           WHERE M."YearMonth" = YYYYMM
           GROUP BY M."CustNo", M."FacmNo") B
    ON (    M."YearMonth" = YYYYMM
        AND M."CustNo"    = B."CustNo"
        AND M."FacmNo"    = B."FacmNo")
    WHEN MATCHED THEN UPDATE SET M."AcSubBookCode" = B."AcSubBookCode";

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE AcBookCode END');

--   更新 EntCode  企金別, RelsCode (準)利害關係人職稱

    DBMS_OUTPUT.PUT_LINE('UPDATE EntCode, RelsCode ');

    MERGE INTO "MonthlyFacBal" M
    USING (SELECT M."CustNo"
                , M."FacmNo"
                , C."EntCode"
                -- , R."RelsCode"
            FROM "MonthlyFacBal" M
            LEFT JOIN "CustMain" C ON C."CustNo" = M."CustNo"
            -- LEFT JOIN "RelsMain" R ON R."RelsId" = C."CustId"
            WHERE M."YearMonth" = YYYYMM
          ) C
     ON (   M."YearMonth" = YYYYMM
        AND M."CustNo"    = C."CustNo"
        AND M."FacmNo"    = C."FacmNo")
    WHEN MATCHED THEN UPDATE 
    SET M."EntCode" = C."EntCode"
      -- , M."RelsCode" = C."RelsCode"
    ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE EntCode, RelsCode END');

--   更新 DepartmentCode	案件隸屬單位

    DBMS_OUTPUT.PUT_LINE('UPDATE DepartmentCode ');

    MERGE INTO "MonthlyFacBal" M
    USING (SELECT "CustNo"
                , "FacmNo"
                , "DepartmentCode"
           FROM "FacMain"
          ) F
     ON (   M."YearMonth" = YYYYMM
         AND M."CustNo"    = F."CustNo"
         AND M."FacmNo"    = F."FacmNo"
         AND F."DepartmentCode" IS NOT NULL
        )
    WHEN MATCHED THEN UPDATE
    SET M."DepartmentCode" = F."DepartmentCode";

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE DepartmentCode END');


--   更新 UnpaidPrincipal 已到期回收本金

    DBMS_OUTPUT.PUT_LINE('UPDATE UnpaidPrincipal ');

    MERGE INTO "MonthlyFacBal" M
    USING ( SELECT "CustNo", "FacmNo"
                 , NVL(SUM(F1), 0) F1
                 , NVL(SUM(F2), 0) F2
                 , NVL(SUM(F3), 0) F3
                 , NVL(SUM(F4), 0) F4
                 , NVL(SUM(F5), 0) F5
                 , NVL(SUM(F6), 0) F6
                 , NVL(SUM(F7), 0) F7
                 , NVL(SUM(F8), 0) F8 
            FROM (
                    SELECT M."CustNo", M."FacmNo",
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN I."Principal"
                          ELSE 0 END  F1,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN I."Interest"
                          ELSE 0 END  F2,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN I."BreachAmt"
                          ELSE 0 END  F3,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN I."DelayInt"
                          ELSE 0 END  F4,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN 0
                          ELSE I."Principal" END  F5,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN 0
                          ELSE I."Interest" END  F6,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN 0
                          ELSE I."BreachAmt" END  F7,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN 0
                          ELSE I."DelayInt" END  F8
                        FROM "MonthlyFacBal" M
                        LEFT JOIN "AcLoanInt" I
                          ON  I."YearMonth" = YYYYMM
                          AND I."CustNo"    = M."CustNo"
                          AND I."FacmNo"    = M."FacmNo"
                        WHERE M."YearMonth" = YYYYMM
                 )
            GROUP BY "CustNo", "FacmNo" ) I
     ON (   M."YearMonth" = YYYYMM
        AND M."CustNo"    = I."CustNo"
        AND M."FacmNo"    = I."FacmNo")
    WHEN MATCHED THEN UPDATE SET M."UnpaidPrincipal" = I."F1",
                                 M."UnpaidInterest"  = I."F2",
                                 M."UnpaidBreachAmt" = I."F3",
                                 M."UnpaidDelayInt"  = I."F4",
                                 M."AcdrPrincipal"   = I."F5",
                                 M."AcdrInterest"    = I."F6",
                                 M."AcdrBreachAmt"   = I."F7",
                                 M."AcdrDelayInt"    = I."F8";

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE UnpaidPrincipal END');

--   更新 FeeAmt 費用

    DBMS_OUTPUT.PUT_LINE('UPDATE FeeAmt ');

    -- 2021-02-19 更新 須配合新增欄位
    MERGE INTO "MonthlyFacBal" M
    USING ( SELECT "CustNo"
                  ,"FacmNo"
                  ,NVL(SUM("FireFee"), 0)       AS "FireFee"
                  ,NVL(SUM("LawFee"), 0)        AS "LawFee"
                  ,NVL(SUM("ModifyFee"), 0)     AS "ModifyFee"
                  ,NVL(SUM("AcctFee"), 0)       AS "AcctFee"
                  ,NVL(SUM("ShortfallPrin"), 0) AS "ShortfallPrin"
                  ,NVL(SUM("ShortfallInt"), 0)  AS "ShortfallInt"
                  ,NVL(SUM("TempAmt"), 0)       AS "TempAmt"
            FROM ( SELECT M."CustNo"
                         ,M."FacmNo"
                         ,CASE
                            WHEN A."AcctCode" IN ('TMI')
                            THEN A."RvBal"
                            WHEN A."AcctCode" IN ('F09', 'F25')
                            THEN A."RvBal"
                            WHEN IR."CustNo" IS NOT NULL
                            THEN IR."TotInsuPrem"
                          ELSE 0 END AS "FireFee" -- 火險費用
                         ,CASE
                            WHEN A."AcctCode" IN ('F07', 'F24')
                            THEN A."RvBal"
                          ELSE 0 END AS "LawFee" -- 法務費用
                         ,CASE
                            WHEN A."AcctCode" IN ('F29')
                            THEN A."RvBal"
                          ELSE 0 END AS "ModifyFee" -- 契變手續費
                         ,CASE
                            WHEN A."AcctCode" IN ('F10')
                            THEN A."RvBal"
                          ELSE 0 END AS "AcctFee" -- 帳管費用
                         ,CASE
                            WHEN SUBSTR(A."AcctCode",0,1) IN ('Z')
                            THEN A."RvBal"
                          ELSE 0 END AS "ShortfallPrin" -- 短繳本金
                         ,CASE
                            WHEN SUBSTR(A."AcctCode",0,1) IN ('I')
                            THEN A."RvBal"
                          ELSE 0 END AS "ShortfallInt" -- 短繳利息
                         ,CASE
                            WHEN A."AcctCode" = 'TAV'
                            THEN A."RvBal"
                          ELSE 0 END AS "TempAmt" -- 暫收金額
                   FROM "MonthlyFacBal" M
                   LEFT JOIN "AcReceivable" A ON A."CustNo" = M."CustNo"
                                             AND A."FacmNo" = M."FacmNo"
                   LEFT JOIN "InsuRenew" IR ON IR."CustNo" = A."CustNo"
                                           AND IR."FacmNo" = A."FacmNo"
                                           AND IR."PrevInsuNo" = A."RvNo"                
                   WHERE M."YearMonth" = YYYYMM
                     AND (A."AcctCode" IN ('F10','F29','TMI', 'F09', 'F25', 'F07', 'F24','TAV')
                          OR SUBSTR(A."AcctCode",0,1) IN ('I','Z'))
            )
            GROUP BY "CustNo", "FacmNo" 
          ) D
     ON (   M."YearMonth" = YYYYMM
        AND M."CustNo"    = D."CustNo"
        AND M."FacmNo"    = D."FacmNo")
    WHEN MATCHED THEN UPDATE
    SET M."FireFee"       = D."FireFee"
       ,M."LawFee"        = D."LawFee"
       ,M."ModifyFee"     = D."ModifyFee"
       ,M."AcctFee"       = D."AcctFee"
       ,M."ShortfallPrin" = D."ShortfallPrin"
       ,M."ShortfallInt"  = D."ShortfallInt"
       ,M."TempAmt"       = D."TempAmt"
    ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE FeeAmt END');

--   更新 ClCode1	擔保品代號1

    DBMS_OUTPUT.PUT_LINE('UPDATE ClCode1 ');

    MERGE INTO "MonthlyFacBal" M1
    USING (SELECT M."YearMonth"
                 ,M."CustNo"
                 ,M."FacmNo"
                 ,NVL(F."ClCode1",0)     AS "ClCode1"
                 ,NVL(F."ClCode2",0)     AS "ClCode2"
                 ,NVL(F."ClNo",0)        AS "ClNo"
                 ,NVL(Cl."CityCode",' ') AS "CityCode"
            FROM "MonthlyFacBal" M
            LEFT JOIN "ClFac" F ON F."CustNo" = M."CustNo"
                               AND F."FacmNo" = M."FacmNo"
                               AND F."MainFlag" = 'Y'
            LEFT JOIN "ClMain" Cl ON Cl."ClCode1" = F."ClCode1"
                                 AND Cl."ClCode2" = F."ClCode2"
                                 AND Cl."ClNo"    = F."ClNo"
                                 AND NVL(F."ClNo",0) > 0 
            WHERE M."YearMonth" = YYYYMM
              AND (F."ClCode1" IN (3,4,5) OR NVL(Cl."CityCode",' ') <> ' ')
           ) F1
     ON (   M1."YearMonth" = F1."YearMonth"
        AND M1."CustNo"    = F1."CustNo"
        AND M1."FacmNo"    = F1."FacmNo")
    WHEN MATCHED THEN UPDATE SET M1."ClCode1"  = F1."ClCode1"
                                ,M1."ClCode2"  = F1."ClCode2"
                                ,M1."ClNo"     = F1."ClNo"
                                ,M1."CityCode" = F1."CityCode";

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE ClCode1 END');

--   更新 OvduPrinBal	催收本金餘額

    DBMS_OUTPUT.PUT_LINE('UPDATE OvduPrinBal ');

    MERGE INTO "MonthlyFacBal" M
    USING ( SELECT M."YearMonth"
                  ,M."CustNo"
                  ,M."FacmNo"
                  ,MIN(NVL(O."OvduDate",99991231)) AS "OvduDate"
                  ,SUM(NVL(O."OvduPrinAmt",0))   AS "UnpaidPrincipal" 
                  ,SUM(NVL(O."OvduIntAmt",0))    AS "UnpaidInterest"
                  ,SUM(NVL(O."OvduBreachAmt",0)) AS "UnpaidBreachAmt"
                  ,SUM(NVL(O."OvduPrinBal",0))   AS "OvduPrinBal" 
                  ,SUM(NVL(O."OvduIntBal",0))    AS "OvduIntBal"
                  ,SUM(NVL(O."OvduBreachBal",0)) AS "OvduBreachBal"
                  ,SUM(NVL(O."OvduBal",0))       AS "OvduBal"
              FROM "MonthlyFacBal" M  
             LEFT JOIN "LoanBorMain" L ON L."CustNo" = M."CustNo" 
                                      AND L."FacmNo" = M."FacmNo" 
             LEFT JOIN "LoanOverdue" O ON  O."CustNo" = L."CustNo" 
                                      AND O."FacmNo" = L."FacmNo"
                                      AND O."BormNo" = L."BormNo" 
                                      AND O."OvduNo" = L."LastOvduNo"
             WHERE M."YearMonth" = YYYYMM 
               AND L."Status" IN (2, 7) 
               AND O."Status" IN (1, 2) 
             GROUP BY M."YearMonth",M."CustNo", M."FacmNo"
          ) O
     ON (   M."YearMonth" = O."YearMonth"
        AND M."CustNo"    = O."CustNo"
        AND M."FacmNo"    = O."FacmNo")
    WHEN MATCHED THEN UPDATE SET M."UnpaidPrincipal" = O."UnpaidPrincipal",
                                 M."UnpaidInterest"  = O."UnpaidInterest",
                                 M."UnpaidBreachAmt" = O."UnpaidBreachAmt",
                                 M."OvduPrinBal"     = O."OvduPrinBal",
                                 M."OvduIntBal"      = O."OvduIntBal",
                                 M."OvduBreachBal"   = O."OvduBreachBal",
                                 M."OvduBal"         = O."OvduBal",
                                 M."OvduDate"        = CASE
                                                         WHEN O."OvduDate" = 99991231
                                                         THEN M."OvduDate"
                                                       ELSE O."OvduDate" END
                                 ;

    UPD_CNT := UPD_CNT + sql%rowcount;    

--   更新 StoreRate 計息利率

    DBMS_OUTPUT.PUT_LINE('UPDATE StoreRate ');

    MERGE INTO "MonthlyFacBal" M
    USING (SELECT M."CustNo", M."FacmNo"
                , NVL(MIN(L."StoreRate"), 0) "StoreRate" 
            FROM "MonthlyFacBal" M
           LEFT JOIN "MonthlyLoanBal" L
            ON  L."YearMonth" = YYYYMM 
            AND L."CustNo"   = M."CustNo"
            AND L."FacmNo"   = M."FacmNo"
           WHERE M."YearMonth" = YYYYMM
           GROUP BY M."CustNo", M."FacmNo") B
    ON (    M."YearMonth" = YYYYMM
        AND M."CustNo"    = B."CustNo"
        AND M."FacmNo"    = B."FacmNo")
    WHEN MATCHED THEN UPDATE SET M."StoreRate" = B."StoreRate";

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE StoreRate END');

--  更新  無擔保債權設定金額(法務進度:901)
    DBMS_OUTPUT.PUT_LINE('UPDATE LawAmount');

    MERGE INTO "MonthlyFacBal" M
    USING (SELECT L."CustNo"
                 ,L."FacmNo"
                 ,L."Amount"
           FROM (
                 SELECT "CustNo"
                       ,"FacmNo"
                       ,"LegalProg"
                       ,"Amount"
                       ,ROW_NUMBER() OVER (PARTITION BY "CustNo", "FacmNo" 
                                           ORDER BY "RecordDate" DESC, "LastUpdate" DESC
                                          ) AS SEQ
                 FROM "CollLaw"  
                 WHERE "CaseCode"  = '1'   
                   AND "LegalProg" = '901' 
                 ) L
           WHERE L.SEQ   =  1) D
     ON (   M."CustNo"    =  D."CustNo"
        AND M."FacmNo"    =  D."FacmNo")
    WHEN MATCHED THEN UPDATE 
      SET M."LawAmount"   =  D."Amount"
    ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE LawAmount END');

--  更新  資產五分類(非1類)
    DBMS_OUTPUT.PUT_LINE('UPDATE AssetClass1');

    MERGE INTO "MonthlyFacBal" M
    USING (
      SELECT M."YearMonth"
           , M."CustNo"
           , M."FacmNo" 
           , CASE
               WHEN M."PrinBalance" = 1
                AND M."AcctCode" = 990
               THEN '5'        --(5)第五類-收回無望(應為法務進度901，現暫以餘額掛1為第五類)
                               --   無擔保部分--超過清償期12月者
                               --   或拍訂貨拍賣無實益之損失者
                               --   或放款資產經評估無法回收者   
              --將2之X的判斷由程式自行
               WHEN M."AcctCode" = 990
                AND M."ProdNo" IN ('60','61','62')
               THEN '2'       --(23)第二類-應予注意：
                               --    有足無擔保--逾繳超過清償期7-12月者
                               --    或無擔保部分--超過清償期1-3月者         
               WHEN M."OvduTerm" >= 7
                AND M."OvduTerm" <= 12
               THEN '2'       --(23)第二類-應予注意：
                               --    有足無擔保--逾繳超過清償期7-12月者
                               --    或無擔保部分--超過清償期1-3月者    
               WHEN M."AcctCode" = 990
                AND M."OvduTerm" <= 12
               THEN '2'       --(23)第二類-應予注意：
                               --    有足無擔保--逾繳超過清償期7-12月者
                               --    或無擔保部分--超過清償期1-3月者    
               WHEN M."AcctCode" <> 990
                AND M."ProdNo" IN ('60','61','62')
                AND M."OvduTerm" = 0
               THEN '2'       --(21)第二類-應予注意：
                               --    有足額擔保--但債信以不良者
                               --    (有擔保分期協議且正常還款者)
               WHEN M."AcctCode" <> 990
                AND M."OvduTerm" >= 1
                AND M."OvduTerm" <= 6
               THEN '2'       --(22)第二類-應予注意：
                               --    有足無擔保--逾繳超過清償期1-6月者
               WHEN M."AcctCode" = 990
                AND M."OvduTerm" > 12
               THEN '3'        --(3)第三類-可望收回：
                               --   有足無擔保--逾繳超過清償期12月者
                               --   或無擔保部分--超過清償期3-6月者                         
               ELSE '1'       -- 正常繳息
             END                  AS "AssetClass"	--放款資產項目	  
      FROM "MonthlyFacBal" M
      LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                            AND F."FacmNo" = M."FacmNo"
      LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
      LEFT JOIN ( SELECT DISTINCT SUBSTR("IndustryCode",3,4) AS "IndustryCode"
                        ,"IndustryItem"
                  FROM "CdIndustry" ) CDI ON CDI."IndustryCode" = SUBSTR(CM."IndustryCode",3,4)
      WHERE M."PrinBalance" > 0 
        AND M."YearMonth" = YYYYMM
    ) TMP
    ON (
      TMP."YearMonth" = M."YearMonth"
      AND TMP."CustNo" = M."CustNo"
      AND TMP."FacmNo" = M."FacmNo"
    )
    WHEN MATCHED THEN UPDATE SET
    "AssetClass" = TMP."AssetClass"
    ;
    
    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE AssetClass1 END');



    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_MonthlyFacBal_Upd' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;

