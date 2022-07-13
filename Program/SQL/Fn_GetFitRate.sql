CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetFitRate" 
(
    "InputCustNo"         IN NUMBER,  -- 戶號
    "InputFacmNo"         IN NUMBER,  -- 額度號碼
    "InputBormNo"         IN NUMBER,  -- 撥款序號
    "InputDate"           IN NUMBER   -- 日期
) RETURN NUMBER IS "FitRate" DECIMAL(6,4);
BEGIN
     --------------------------------------------------------
     --  DDL for Function Fn_GetFitRate
     --  Example:
     --  SELECT "Fn_GetFitRate"(M."CustNo",M."FacmNo",M."BormNo",M."PrevPayIntDate")
     --  FROM "LoanBorMain" M
     --------------------------------------------------------
     -- 2022-7-13 Wei 開發
     -- 目的: 供 Fn_CalculateDerogationInterest 取得某客戶在任一日期的適用利率

     DECLARE
          haveRateChange NUMBER;
     BEGIN
          SELECT COUNT(1)
          INTO haveRateChange
          FROM "LoanRateChange" LRC
          WHERE LRC."CustNo" = "InputCustNo"
            AND LRC."FacmNo" = "InputFacmNo"
            AND LRC."BormNo" = "InputBormNo"
            AND LRC."EffectDate" <= "InputDate"
          ;

          IF haveRateChange = 0 THEN
              RETURN 0;
          END IF;
     END;

     WITH orderedData AS (
          SELECT LRC."EffectDate"
               , LRC."FitRate"
               , ROW_NUMBER()
                 OVER (
                    ORDER BY "EffectDate" DESC
                 )                  AS "EffectDateSeq"
          FROM "LoanRateChange" LRC
          WHERE LRC."CustNo" = "InputCustNo"
            AND LRC."FacmNo" = "InputFacmNo"
            AND LRC."BormNo" = "InputBormNo"
            AND LRC."EffectDate" <= "InputDate"
     )
     SELECT "FitRate"
     INTO "FitRate"
     FROM orderedData
     WHERE "EffectDateSeq" = 1
     ;

     RETURN "FitRate";

     -- 例外處理
     Exception
     WHEN OTHERS THEN
     RETURN 0;
END;

/
