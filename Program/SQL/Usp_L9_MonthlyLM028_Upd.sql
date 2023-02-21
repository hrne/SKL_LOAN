CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_MonthlyLM028_Upd" 
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AS
BEGIN
  -- EXEC "Usp_L9_MonthlyLM028_Upd"(20211130,'999999');
DECLARE
    INS_CNT        INT;        -- 新增筆數
    UPD_CNT        INT;        -- 更新筆數
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;  -- 記錄程式結束時間
    YYYYMM         INT;        -- 本月年月
    YYYY           INT;        -- 西元年
    MM             INT;        -- 前月
    LYYYYMMDD      INT;        -- 前月月初
  BEGIN
    INS_CNT :=0;
    UPD_CNT :=0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

 --　本月年月
    YYYYMM := TBSDYF / 100;
    -- 西元年
    YYYY := TBSDYF / 10000;
    -- 前月
    MM := MOD(TBSDYF / 100,100) - 1;

    IF MM = 0 THEN  MM := 12;
    ELSE  MM := MM ;
    END IF;
    --前月月初
    LYYYYMMDD := YYYY * 10000 + MM * 100 + 1;

    -- 刪除舊資料
    DELETE FROM "MonthlyLM028"
    WHERE "DataMonth" = YYYYMM
    ;

    -- 寫入資料
    INSERT INTO "MonthlyLM028"
    SELECT L."YearMonth"                AS "DataMonth"        -- 資料年月
         , M."Status"                   AS "Status"           -- 戶況
         , DECODE(L."EntCode", '1', 1, 0)
                                        AS "EntCode"          -- 企金別
         , M."BranchNo"                 AS "BranchNo"         -- 營業單位別
         , L."CustNo"                   AS "CustNo"           -- 借款人戶號
         , L."FacmNo"                   AS "FacmNo"           -- 額度編號
         , L."BormNo"                   AS "BormNo"           -- 撥款序號
         , L."StoreRate"                AS "StoreRate"        -- 利率
         , M."PayIntFreq"               AS "PayIntFreq"       -- 繳息週期
         -- 改抓額度檔到期日
         , NVL(SUBSTR(TO_CHAR(F."MaturityDate"),0,4),'0')
                                        AS "MaturityYear"     -- 額度主檔到期日-年
         , NVL(SUBSTR(TO_CHAR(F."MaturityDate"),5,2),'0')
                                        AS "MaturityMonth"    -- 額度主檔到期日-月
         , NVL(SUBSTR(TO_CHAR(F."MaturityDate"),7,2),'0')
                                        AS "MaturityDay"      -- 貸款期間－日
         , L."LoanBalance"              AS "LoanBal"          -- 放款餘額
         , M."RateCode"                 AS "RateCode"         -- 利率區分
         , NVL(BAA."PostDepCode",' ')   AS "PostDepCode"      -- 郵局存款別
         , M."SpecificDd"               AS "SpecificDd"       -- 應繳日
         , NVL(F."FirstRateAdjFreq",0)  AS "FirstRateAdjFreq" -- 首次調整週期
         , F."ProdNo"                   AS "ProdNo"           -- 商品代碼
         , 0                            AS "FitRate1"         -- 利率1
         , 0                            AS "FitRate2"         -- 利率2
         , 0                            AS "FitRate3"         -- 利率3
         , 0                            AS "FitRate4"         -- 利率4
         , 0                            AS "FitRate5"         -- 利率5
         , L."ClCode1"                  AS "ClCode1"          -- 押品別１
         , L."ClCode2"                  AS "ClCod2"           -- 押品別２
         , TRUNC(M."DrawdownDate" / 10000)
                                        AS "DrawdownYear"     -- 撥款日-年
         , MOD(TRUNC(M."DrawdownDate" / 100), 100)
                                        AS "DrawdownMonth"    -- 撥款日-月
         , MOD(M."DrawdownDate", 100)   AS "DrawdownDay"      -- 撥款日-日
         -- 餘額 > 0則”0”，再接續判斷【業務科目】是否為340，若是，則”1”。
         , CASE
             WHEN L."LoanBalance" = 0  THEN 0
             WHEN F."AcctCode" = '340' THEN 1
           ELSE 0 END                   AS "W08Code"          -- 到期日碼
         , CASE
             WHEN REL."RelId" IS NOT NULL
             THEN '1'
           ELSE ' ' END                 AS "IsRelation"       -- 是否為關係人
         , NVL(E."AgType1", ' ')        AS "AgType1"          -- 制度別
         , NVL(B."AcctSource", ' ')     AS "AcctSource"       -- 資金來源
         , 0                            AS "LastestRate"      -- 最後生效利率
         , 0                            AS "LastestRateStartDate" -- 最後利率生效日
         , JOB_START_TIME               AS "CreateDate"  -- 建檔日期時間
         , EmpNo                        AS "CreateEmpNo" -- 建檔人員
         , JOB_START_TIME               AS "LastUpdate"  -- 最後更新日期時間
         , EmpNo                        AS "LastUpdateEmpNo" -- 最後更新人員
      FROM "MonthlyLoanBal" L
      LEFT JOIN "LoanBorMain" M ON M."CustNo" = L."CustNo"
                               AND M."FacmNo" = L."FacmNo"
                               AND M."BormNo" = L."BormNo"
      LEFT JOIN "CustMain" C ON  C."CustNo" =  L."CustNo"
      LEFT JOIN "FacMain" F ON  F."CustNo" =  L."CustNo"
                           AND F."FacmNo" =  L."FacmNo"
      LEFT JOIN "CdEmp" E ON  E."EmployeeNo" = C."EmpNo"
      LEFT JOIN "CdAcBook" B ON B."AcBookCode" = L."AcBookCode"
                            AND B."AcSubBookCode" = L."AcSubBookCode"
      LEFT JOIN (SELECT "CustNo"
                      , "FacmNo"
                      , "RepayBank"
                      , "PostDepCode"
                      , ROW_NUMBER() OVER (PARTITION BY "CustNo"
                                                      , "FacmNo"
                                           ORDER BY "AuthType"
                                          ) AS "Seq"
                 FROM "BankAuthAct"
                 WHERE "Status" = '0'
                ) BAA ON BAA."CustNo" = L."CustNo"
                     AND BAA."FacmNo" = L."FacmNo"
                     AND BAA."Seq" = 1
      LEFT JOIN ( 
                  -- SELECT "CusId" AS "RelId"
                  -- FROM "RptRelationSelf"
                  -- WHERE "CusSCD" = '2'
                  -- UNION
                  -- SELECT "RlbID" AS "RelId"
                  -- FROM "RptRelationFamily"
                  -- WHERE "CusSCD" = '2'
                  SELECT "CustId" AS "RelId"
                  FROM "BankRelationSelf"
                  WHERE "CustName" like '新%壽%'
                  UNION
                  SELECT "RelationId" AS "RelId"
                  FROM "BankRelationFamily"
                  WHERE "CustName" like '新%壽%'

                ) REL ON REL."RelId" = C."CustId"
      WHERE L."YearMonth" = YYYYMM
        AND L."LoanBalance" > 0
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    MERGE INTO "MonthlyLM028" M
    USING (
      SELECT R."CustNo"
           , R."FacmNo"
           , R."BormNo"
           , SUM(DECODE(R."SEQ", 1, R."FitRate", 0)) "FitRate1"
           , SUM(DECODE(R."SEQ", 2, R."FitRate", 0)) "FitRate2"
           , SUM(DECODE(R."SEQ", 3, R."FitRate", 0)) "FitRate3"
           , SUM(DECODE(R."SEQ", 4, R."FitRate", 0)) "FitRate4"
           , SUM(DECODE(R."SEQ", 5, R."FitRate", 0)) "FitRate5"
           , SUM(DECODE(R."SEQ2", 1, R."FitRate", 0)) "LFitRate"
           , SUM(DECODE(R."SEQ2", 1, R."EffectDate", 0)) "LIRTDAY"
      FROM (
        SELECT M."CustNo"
             , M."FacmNo"
             , M."BormNo"
             , R."FitRate"
             , R."EffectDate"
             , ROW_NUMBER()
               OVER (
                 PARTITION BY M."CustNo", M."FacmNo", M."BormNo"
                 ORDER BY R."EffectDate"
               )                         AS SEQ
             , ROW_NUMBER()
               OVER (
                 PARTITION BY M."CustNo", M."FacmNo", M."BormNo"
                 ORDER BY R."EffectDate" DESC
               )                         AS SEQ2
        FROM "LoanBorMain" M
        LEFT JOIN "LoanRateChange" R ON R."CustNo" = M."CustNo"
                                    AND R."FacmNo" = M."FacmNo"
                                    AND R."BormNo" = M."BormNo"
        WHERE R."EffectDate" <= M."FirstAdjRateDate"
      ) R
      GROUP BY R."CustNo"
             , R."FacmNo"
             , R."BormNo"
    ) R
    ON (  
      M."DataMonth" = YYYYMM 
      AND M."CustNo" = R."CustNo"
      AND M."FacmNo" = R."FacmNo"
      AND M."BormNo" = R."BormNo"
    )
    WHEN MATCHED THEN UPDATE
      SET M."FitRate1" = R."FitRate1"
         ,M."FitRate2" = R."FitRate2"
         ,M."FitRate3" = R."FitRate3"
         ,M."FitRate4" = R."FitRate4"
         ,M."FitRate5" = R."FitRate5"
         ,M."LastestRate" = R."LFitRate"
         ,M."LastestRateStartDate"   = R."LIRTDAY";

    UPD_CNT := UPD_CNT + sql%rowcount;

    MERGE INTO "MonthlyLM028" M
    USING (
      SELECT  M."DataMonth"
            , M."CustNo"
            , M."FacmNo"
            , M."BormNo" 
            , M."StoreRate"
            , DECODE(L."FirstAdjRateDate", 0, L."DrawdownDate", L."FirstAdjRateDate") AS "LIRTDAY" 
      FROM "MonthlyLM028" M
      LEFT JOIN "LoanBorMain" L ON L."CustNo" = M."CustNo"
                               AND L."FacmNo" = M."FacmNo"
                               AND L."BormNo" = M."BormNo"
      WHERE M."DataMonth" = YYYYMM
        AND M."LastestRate" = 0
    ) D
    ON (   
      M."DataMonth" = D."DataMonth"
      AND M."CustNo" = D."CustNo"
      AND M."FacmNo" = D."FacmNo"
      AND M."BormNo" = D."BormNo"
    )
    WHEN MATCHED THEN UPDATE SET
      M."LastestRate" = D."StoreRate"
    , M."LastestRateStartDate" = D."LIRTDAY";

    UPD_CNT := UPD_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_MonthlyLM028_Upd' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;