CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetCdCityIntRateIncr" 
(
    "InputCityCode"       IN VARCHAR2, -- 縣市別
    "InputDateStart"      IN NUMBER,   -- 起日
    "InputDateEnd"        IN NUMBER    -- 迄日
) RETURN NUMBER IS "IntRateIncr" DECIMAL(6,4);
BEGIN
     --------------------------------------------------------
     --  DDL for Function Fn_GetCdCityIntRateIncr
     --  Example:
     --  SELECT "Fn_GetCdCityIntRateIncr"(CM."CityCode",R."EffectDate",:inputEffectDateE)
     --  FROM "ClFac" CF
     --  LEFT JOIN "ClMain" CM ON CM."ClCode1" = CF."ClCode1"
     --                       AND CM."ClCode2" = CF."ClCode2"
     --                       AND CM."ClNo" = CF."ClNo"
     --  LEFT JOIN "LoanRateChange" R ON R."CustNo" = CF."CustNo"
     --                              AND R."FacmNo" = CF."FacmNo"
     --  WHERE CF."MainFlag" = 'Y'
     --------------------------------------------------------
     -- 2022-10-11 Wei 開發
     -- 目的: 供 L4320ServiceImpl 取得某客戶在起日至迄日範圍的地區別利率加減碼加總

     WITH rawData AS (
          SELECT CCR."IntRateIncr"
          FROM "CdCityRate" CCR
          WHERE CCR."EffectYYMM" > TRUNC("InputDateStart" / 100)
            AND CCR."EffectYYMM" <= TRUNC("InputDateEnd" / 100)
            AND CCR."CityCode" = "InputCityCode"
     )
     SELECT SUM(NVL("IntRateIncr",0))
     INTO "IntRateIncr"
     FROM rawData
     ;

     RETURN "IntRateIncr";

     -- 例外處理
     Exception
     WHEN OTHERS THEN
     RETURN 0;
END;

/
