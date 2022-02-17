--------------------------------------------------------
--  DDL for Function Fn_GetLastMonth
--  Example:
--  SELECT "Fn_GetLastMonth"("YearMonth")
--  FROM "MonthlyFacBal"
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetLastMonth" 
(
    "InputYearMonth" IN NUMBER -- 傳入月份
) RETURN VARCHAR2 IS "Result" VARCHAR2(14);
BEGIN
    SELECT CASE
             WHEN MOD("InputYearMonth", 100) = 1
             THEN (TRUNC("InputYearMonth" / 100 ) - 1 ) * 100 + 12
           ELSE "InputYearMonth" - 1 END
    INTO "Result"
    FROM DUAL
    ;

    RETURN "Result";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN '';
END;

/
