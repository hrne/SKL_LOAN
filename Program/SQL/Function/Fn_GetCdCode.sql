--------------------------------------------------------
--  DDL for Function Fn_GetCdCode
--  Example:
--  SELECT "Fn_GetCdCode"('CustTypeCode',CM."CustTypeCode") AS "CustTypeItem"
--  FROM "CustMain" CM
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetCdCode" 
(
    "ColumnName" IN VARCHAR2, -- 欄位名稱
    "Value"      IN VARCHAR2  -- 欄位值
) RETURN VARCHAR2 IS "Result" NVARCHAR2(50);
BEGIN
    SELECT NVL("Item",N' ') AS "Item"
    INTO "Result"
    FROM "CdCode"
    WHERE "DefCode" = "ColumnName"
      AND "Code" = "Value"
      AND "Enable" = 'Y' -- 啟用
    ;

    RETURN "Result";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN N' ';
END;

/
