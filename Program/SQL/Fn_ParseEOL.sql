--------------------------------------------------------
--  DDL for Function Fn_ParseEOL
--  Example:
--  SELECT "Fn_ParseEOL"(CM."CustName", 0) AS "CustName"
--  FROM "CustMain" CM
--------------------------------------------------------

CREATE OR REPLACE FUNCTION "Fn_ParseEOL" 
(
    "InputString" IN VARCHAR2 , -- 傳入欄位值
    "ReturnType"    IN DECIMAL   -- 回傳型態 0:無換行符 1:有換行符(CHR(13)) 2:有換行符($n) 
) RETURN VARCHAR2 IS "Result" VARCHAR2(100);
BEGIN
    SELECT CASE
             WHEN "ReturnType" = 0
             THEN REPLACE(REPLACE("InputString", '$n', ''), CHR(13), '')
             WHEN "ReturnType" = 1
             THEN REPLACE("InputString", '$n', CHR(13))
             WHEN "ReturnType" = 2 
             THEN REPLACE("InputString", CHR(13), '$n')
           ELSE "InputString"
           END               AS "TempString"
    INTO "Result"
    FROM DUAL
    ;

    RETURN "Result";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN TO_NCHAR(' ');
END;

/
