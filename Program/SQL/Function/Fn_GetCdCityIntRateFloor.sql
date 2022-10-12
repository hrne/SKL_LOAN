CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetCdCityIntRateFloor" 
(
    "InputCityCode"       IN VARCHAR2, -- 縣市別
    "InputDate"           IN NUMBER    -- 日期
) RETURN NUMBER IS "IntRateFloor" DECIMAL(6,4);
BEGIN
     --------------------------------------------------------
     --  DDL for Function Fn_GetCdCityIntRateFloor
     --  Example:
     --  SELECT "Fn_GetCdCityIntRateFloor"(CM."CityCode",R."EffectDate")
     --  FROM "ClFac" CF
     --  LEFT JOIN "ClMain" CM ON CM."ClCode1" = CF."ClCode1"
     --                       AND CM."ClCode2" = CF."ClCode2"
     --                       AND CM."ClNo" = CF."ClNo"
     --  LEFT JOIN "LoanRateChange" R ON R."CustNo" = CF."CustNo"
     --                              AND R."FacmNo" = CF."FacmNo"
     --  WHERE CF."MainFlag" = 'Y'
     --------------------------------------------------------
     -- 2022-10-11 Wei 開發
     -- 目的: 供 L4320ServiceImpl 取得某客戶在任一日期的最新地區別利率下限

     WITH orderedData AS (
          SELECT CCR."IntRateFloor"
               , ROW_NUMBER()
                 OVER (
                    ORDER BY "EffectYYMM" DESC
                 )                  AS "Seq"
          FROM "CdCityRate" CCR
          WHERE CCR."EffectYYMM" >= TRUNC("InputDate" / 100)
            AND CCR."CityCode" = "InputCityCode"
     )
     SELECT NVL("IntRateFloor",0)
     INTO "IntRateFloor"
     FROM orderedData
     WHERE "Seq" = 1
     ;

     RETURN "IntRateFloor";

     -- 例外處理
     Exception
     WHEN OTHERS THEN
     RETURN 0;
END;

/
