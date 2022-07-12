CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_CalculateDerogationInterest" 
(
    "InputCustNo"         IN NUMBER,  -- 戶號
    "InputFacmNo"         IN NUMBER,  -- 額度號碼
    "InputBormNo"         IN NUMBER,  -- 撥款序號
    "InputLoanBal"        IN NUMBER,  -- 放款餘額
    "InputFitRate"        IN NUMBER,  -- 放款利率
    "InputPrevPayIntDate" IN NUMBER,  -- 繳息迄日
    "InputDerogationDate" IN NUMBER   -- 減損發生日
) RETURN NUMBER IS "Interest" DECIMAL(16,2);
BEGIN
     --------------------------------------------------------
     --  DDL for Function Fn_CalculateDerogationInterest
     --  Example:
     --  SELECT "Fn_CalculateDerogationInterest"(M."CustNo",M."FacmNo",M."BormNo",NVL(ML."LoanBalance",0),NVL(LR."FitRate",0),JML."PrevPayIntDate",M."DerDate")
     --  FROM "LoanIfrs9Dp" M
     --  LEFT JOIN "JcicMonthlyLoanData" JML ON JML."DataYM" = YYYYMM
     --                                      AND JML."CustNo" = M."CustNo"
     --                                      AND JML."FacmNo" = M."FacmNo"
     --                                      AND JML."BormNo" = M."BormNo"
     --------------------------------------------------------
     -- 2022-7-12 Wei from Linda
     -- 上述發生日期時之應收利息(台幣)=繳息迄日~發生日期間的利息
     -- 若此區間無新利率則利息=
     -- 發生日時餘額 * 發生日時利率 * 天數(固定120) / 360 / 100
     -- 若此區間有不同利率則以分段計算
     -- 發生日時餘額 * (利率生效日、發生日)時利率 * 天數(繳息迄日~利率生效日~發生日 分段) / 360 / 100

     DECLARE
          haveRateChange NUMBER;
     BEGIN
          SELECT COUNT(1)
          INTO haveRateChange
          FROM "LoanRateChange" LRC
          WHERE LRC."CustNo" = "InputCustNo"
            AND LRC."FacmNo" = "InputFacmNo"
            AND LRC."BormNo" = "InputBormNo"
            AND LRC."EffectDate" >= "InputPrevPayIntDate"
            AND LRC."EffectDate" < "InputDerogationDate"
          ;

          IF haveRateChange = 0 THEN
              RETURN "InputLoanBal" * "InputFitRate" * 120 / 360 / 100;
          END IF;
     END;

     WITH rawData AS (
          SELECT LRC."EffectDate"
               , LRC."FitRate"
          FROM "LoanRateChange" LRC
          WHERE LRC."CustNo" = "InputCustNo"
          AND LRC."FacmNo" = "InputFacmNo"
          AND LRC."BormNo" = "InputBormNo"
          AND LRC."EffectDate" >= "InputPrevPayIntDate"
          AND LRC."EffectDate" < "InputDerogationDate"
          UNION
          SELECT "InputPrevPayIntDate" AS "EffectDate"
               , "InputFitRate"        AS "FitRate"
          FROM DUAL
          UNION
          SELECT "InputDerogationDate"
               , 0                     AS "FitRate"
          FROM DUAL
     )
     , orderedData AS (
          SELECT ROW_NUMBER()
                 OVER (
                    ORDER BY "EffectDate"
                 )                     AS "EffectDateSeq"
               , "EffectDate"
               , "FitRate" 
          FROM rawData
     )
     SELECT ROUND(
               "InputLoanBal"
               * nowRow."FitRate"
               * (TO_DATE(nextRow."EffectDate",'yyyymmdd') - TO_DATE(nowRow."EffectDate",'yyyymmdd'))
               / 360
               / 100
               , 0)
     INTO "Interest"
     FROM orderedData nowRow
     LEFT JOIN orderedData nextRow ON nextRow."EffectDateSeq" = nowRow."EffectDateSeq" + 1
     WHERE nextRow."EffectDateSeq" IS NOT NULL
     ;

     RETURN "Interest";

     -- 例外處理
     Exception
     WHEN OTHERS THEN
     RETURN 0;
END;

/
