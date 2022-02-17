CREATE OR REPLACE FUNCTION "Fn_ParseEOL" 
(
    "InputString" IN VARCHAR2 , -- 傳入欄位值
    "ReturnType"    IN DECIMAL   -- 回傳型態 0:無換行符 1:有換行符(CHR(13)) 2:有換行符($n) 
) RETURN NVARCHAR2 IS "Result" NVARCHAR2(100);
BEGIN
--------------------------------------------------------
--  DDL for Function Fn_ParseEOL
--  Example:
--  SELECT "Fn_ParseEOL"(CM."CustName", 0) AS "CustName"
--  FROM "CustMain" CM
--------------------------------------------------------
    SELECT CASE
             WHEN "ReturnType" = 0
             THEN TO_NCHAR(REPLACE(REPLACE("InputString", '$n', ''), CHR(13), ''))
             WHEN "ReturnType" = 1
             THEN TO_NCHAR(REPLACE("InputString", '$n', CHR(13)))
             WHEN "ReturnType" = 2 
             THEN TO_NCHAR(REPLACE("InputString", CHR(13), '$n'))
           ELSE TO_NCHAR("InputString")
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
