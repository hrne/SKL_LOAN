--------------------------------------------------------
--  DDL for Function GetAdviseFireInsuCovrg
--  Example:
--  SELECT "Fn_GetAdviseFireInsuCovrg"(IR."ClCode1",IR."ClCode2",IR."ClNo",1)  AS "AdviseFireInsuCovrg"
--  FROM "InsuRenew" IR
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetAdviseFireInsuCovrg" 
(
    "InputClCode1" IN DECIMAL, -- 擔保品代號1
    "InputClCode2" IN DECIMAL, -- 擔保品代號2
    "InputClNo"  IN DECIMAL,  -- 擔保品號碼
    "ReturnType" IN DECIMAL -- 回傳種類:1=總坪數,2=建築估價
) RETURN DECIMAL IS "Result" DECIMAL(16,2);
BEGIN
    WITH "PublicArea" AS (
        SELECT SUM("Area") "PublicArea"
        FROM "ClBuildingPublic"
        WHERE "ClCode1" = "InputClCode1"
        AND "ClCode2" = "InputClCode2"
        AND "ClNo" = "InputClNo"
    )
    , "BuildingCost" AS (
        SELECT NVL(BC."Cost",BC2."Cost") AS "Cost"
             , ROW_NUMBER() OVER (PARTITION BY B."ClCode1"
                                             , B."ClCode2"
                                             , B."ClNo"
                                  ORDER BY NVL(BC."FloorLowerLimit",BC2."FloorLowerLimit") DESC
                                 ) AS "Seq"
        FROM "ClBuilding" B
        LEFT JOIN "CdBuildingCost" BC ON BC."CityCode" = B."CityCode"
                                     AND BC."FloorLowerLimit" <= B."TotalFloor"
        LEFT JOIN "CdBuildingCost" BC2 ON BC2."CityCode" = '10'
                                      AND BC2."FloorLowerLimit" <= B."TotalFloor"
        WHERE B."ClCode1" = "InputClCode1"
          AND B."ClCode2" = "InputClCode2"
          AND B."ClNo" = "InputClNo"
        
    )
    SELECT (  NVL(B."FloorArea",0)
            + NVL(B."ParkingArea",0)
            + NVL(P."PublicArea",0)
           ) * 
           CASE
             WHEN "ReturnType" = 1
             THEN 1
             WHEN "ReturnType" = 2
             THEN NVL(C."Cost",0)
           ELSE 0 END AS "TmpResult"
    INTO "Result"
    FROM "ClBuilding" B
       , "PublicArea" P 
       , "BuildingCost" C
    WHERE B."ClCode1" = "InputClCode1"
      AND B."ClCode2" = "InputClCode2"
      AND B."ClNo" = "InputClNo"
      AND C."Seq" = 1
    ;

    RETURN "Result";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN N' ';
END;

/
