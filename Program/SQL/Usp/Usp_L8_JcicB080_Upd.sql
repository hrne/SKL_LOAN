CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L8_JcicB080_Upd"
(
-- 程式功能：維護 JcicB080 每月聯徵授信額度資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB080_Upd"(20200430,'999999');
--

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
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB080');

    DELETE FROM "JcicB080"
    WHERE "DataYM" = YYYYMM
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB080');

    INSERT INTO "JcicB080"
    WITH "Work_B080" AS (

    -- 寫入資料 Work_B080    -- 撈應申報之額度（B201 有上階共用額度）
    SELECT YYYYMM                                AS "DataYM"
         , TO_number(SUBSTR(M."FacmNo", 1, 7))   AS "CustNo"            -- 戶號
         , TO_number(SUBSTR(M."FacmNo", 8, 3))   AS "FacmNo"            -- 額度編號
         , M."FacmNo"                            AS "B201FacmNo"        -- B201 上階額度
    FROM   "JcicB201" M
    WHERE  M."DataYM" =  YYYYMM
      AND  M."FacmNo" <> LPAD('9', 50, '9')
    GROUP BY M."FacmNo"
    )  
    , "Work_B080_1" AS (
    SELECT "DataYM"
         , "CustNo"            -- 戶號
         , "FacmNo"            -- 額度編號
         , "B201FacmNo"        -- B201 上階額度
    FROM "Work_B080"
    UNION
    -- 寫入資料 Work_B080    -- 撈預約（未申報 B201 ）
    SELECT YYYYMM                                AS "DataYM"
         , M."CustNo"                            AS "CustNo"            -- 戶號
         , M."FacmNo"                            AS "FacmNo"            -- 額度編號
         , LPAD(M."CustNo", 7, '0') || LPAD(M."FacmNo", 3, '0')
                                                 AS "B201FacmNo"        -- B201 上階額度
    FROM   "LoanBorMain" M
      LEFT JOIN "Work_B080" WK ON WK."DataYM" =  YYYYMM
                              AND M."CustNo"  =  WK."CustNo"
                              AND M."FacmNo"  =  WK."FacmNo"
    WHERE  TRUNC(M."DrawdownDate" / 100) >  YYYYMM    -- 撥款日大於本月
      AND  WK."CustNo" IS NULL
    GROUP BY M."CustNo", M."FacmNo"
    )

    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '80'                                  AS "DataType"          -- 資料別
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , 'A'                                   AS "TranCode"          -- 交易代碼 A:新增 C:異動 D:刪除
         , ' '                                   AS "Filler4"           -- 空白
         , CASE WHEN NVL(WK."CustNo",0) = 398695 THEN 'E100696154'  -- 特例報送 -- (ref:LN15E1 (#M3601 15 10))
                ELSE NVL(C."CustId", ' ')
           END                                   AS "CustId"            -- 授信戶IDN/BAN
         , WK."B201FacmNo"                       AS "FacmNo"            -- 本階共用額度控制編碼
         , F."CurrencyCode"                      AS "CurrencyCode"      -- 授信幣別
         , CASE WHEN NVL( F."LineAmt",0) <= 1000 THEN 1
                ELSE TRUNC( NVL( F."LineAmt",0) / 1000, 0)
           END                                   AS "DrawdownAmt"       -- 本階訂約金額(台幣)  -- (ref:LN15E1 (#M3601 80 8))
         , 0                                     AS "DrawdownAmtFx"     -- 本階訂約金額(外幣)
         , TRUNC(F."SettingDate" / 100) - 191100 AS "DrawdownDate"      -- 本階額度開始年月
         , CASE
             WHEN NVL(F."MaturityDate",0) = 0 THEN 0
             ELSE TRUNC(F."MaturityDate" / 100) - 191100
           END                                   AS "MaturityDate"      -- 本階額度約定截止年月
         , CASE WHEN NVL(F."RecycleCode",'0') IN ('0') THEN 'N'
                WHEN F."RecycleCode" IN ('1')          THEN 'Y'
                ELSE ' '
           END                                   AS "RecycleCode"       -- 循環信用註記 'Y':是，'N':否
         , 'Y'                                   AS "IrrevocableFlag"   -- 額度可否撤銷,固定值= 'Y':可撤銷 
         , LPAD('9', 50, '9')                    AS "UpFacmNo"          -- 上階共用額度控制編碼
         , CASE
         --  WHEN MF."Status"      IN (2, 7)         THEN 'A' -- 催收款項   -- (ref:LN15E1 (#M3601 160 2))
         --  WHEN MF."Status"      IN (6)            THEN 'B' -- 呆帳
             WHEN MF."FacAcctCode" IN ('310')        THEN 'E' -- 其他短期放款
             WHEN MF."FacAcctCode" IN ('320')        THEN 'H' -- 中期放款
             WHEN MF."FacAcctCode" IN ('330', '340') THEN 'I' -- 長期放款
             ELSE  'I'                                        -- 長期放款
           END                                   AS "AcctCode"          -- 科目別
         , 'S'                                   AS "SubAcctCode"       -- 科目別註記 S:有十足擔保 X:無前述情形者
                                                                           -- 一律為S (ref:LN15E1 (#M3601 160 2))
         , NVL("CdCl"."ClTypeJCIC", ' ')         AS "ClTypeJCIC"        -- 擔保品類別(JCIC)
      -- , CASE WHEN "ClMain"."ClCode1" IS NULL THEN '  '
      --        WHEN "ClMain"."ClCode1" = '1' THEN '25'
      --        WHEN "ClMain"."ClCode1" = '2' THEN '20'
      --        WHEN "ClMain"."ClCode1" = '3' THEN '14'
      --        WHEN "ClMain"."ClCode1" = '4' THEN '11'
      --        WHEN "ClMain"."ClCode1" = '5' THEN '02'
      --        WHEN "ClMain"."ClCode1" = '9' AND  NVL("ClMain"."ClCode2",'0') = '1' THEN '31'
      --        WHEN "ClMain"."ClCode1" = '9' THEN '30'
      --        ELSE '  '
      --   END                                   AS "ClTypeCode"        -- 擔保品類別(JCIC) (ref: LNSP15A LN15E1)
         , ' '                                   AS "Filler18"          -- 空白
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Work_B080_1" WK
      LEFT JOIN "CustMain" C      ON C."CustNo"    = WK."CustNo"
      LEFT JOIN "FacMain"  F      ON F."CustNo"    = WK."CustNo"
                                 AND F."FacmNo"    = WK."FacmNo"
      LEFT JOIN "FacCaseAppl" ON "FacCaseAppl"."ApplNo"   =  F."ApplNo"
      LEFT JOIN "ClFac" Cl    ON Cl."CustNo"       = F."CustNo"
                             AND Cl."FacmNo"       = F."FacmNo"
                             AND Cl."ApproveNo"    = F."ApplNo"
                             AND Cl."MainFlag"     = 'Y'  -- 主要擔保品"
      LEFT JOIN "ClMain"      ON "ClMain"."ClCode1"  = Cl."ClCode1"
                             AND "ClMain"."ClCode2"  = Cl."ClCode2"
                             AND "ClMain"."ClNo"     = Cl."ClNo"
      LEFT JOIN "CdCl"        ON "CdCl"."ClCode1"    = Cl."ClCode1"
                             AND "CdCl"."ClCode2"    = Cl."ClCode2"
      LEFT JOIN "MonthlyFacBal"  MF    ON MF."YearMonth" = WK."DataYM"
                                      AND MF."CustNo"    = WK."CustNo"
                                      AND MF."FacmNo"    = WK."FacmNo"
                                      AND (MF."PrinBalance" > 0  
                                         OR (MF."PrinBalance" = 0 AND 
                                             MF."Status" IN (3, 5, 8 , 9)))
      ; 

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB080 END: INS_CNT=' || INS_CNT);


-- 更新 MaturityDate 本階額度約定截止年月

    DBMS_OUTPUT.PUT_LINE('UPDATE MaturityDate ');
    UPD_CNT := 0;

    MERGE INTO "JcicB080" M
    USING ( SELECT F."CustNo"  AS "CustNo"
                 , F."FacmNo"  AS "FacmNo"
                 , L."BormNo"  AS "BormNo"
                 , NVL(L."MaturityDate",0)  AS "MaturityDate"
            FROM "JcicB080" M
              LEFT JOIN "FacMain" F       ON SUBSTR(M."FacmNo",1,7) = F."CustNo"
                                         AND SUBSTR(M."FacmNo",8,3) = F."FacmNo"
              LEFT JOIN "LoanBorMain" L   ON L."CustNo"  = F."CustNo"
                                         AND L."FacmNo"  = F."FacmNo"
                                         AND L."BormNo"  = F."LastBormNo"
            WHERE M."DataYM"       = YYYYMM
              AND M."MaturityDate" = 0
          ) L
     ON (     M."DataYM"   =   YYYYMM
          AND SUBSTR(M."FacmNo",1,7) = L."CustNo"
          AND SUBSTR(M."FacmNo",8,3) = L."FacmNo"
        )
     WHEN MATCHED THEN UPDATE
       SET M."MaturityDate" = CASE WHEN L."MaturityDate" = 0 THEN 0
                                   ELSE FLOOR( L."MaturityDate" / 100 ) - 191100
                              END
       ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE MaturityDate END');


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;
