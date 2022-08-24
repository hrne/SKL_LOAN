CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_Split" 
--------------------------------------------------------
--  DDL for Function Fn_Split
--  Example 1:
--  SELECT "Fn_Split"('切-割-我','-',1)
--  FROM DUAL
--
--  WILL RETURN:
--  '切'
--
--  Example 2:
--  SELECT "Fn_Split"('切-割-我','-',4)
--  FROM DUAL
--
--  WILL RETURN:
--  null
--------------------------------------------------------
(
    "SourceString" IN VARCHAR2,  -- 欲切割的字串
    "SplitChar"    IN VARCHAR2,  -- 以此字符切割
    "ReturnSeq"    IN int        -- 回傳第幾個,從1開始
) RETURN NVARCHAR2 IS "ResultString" NVARCHAR2(1000);
BEGIN
    SELECT TO_NCHAR(REGEXP_SUBSTR("SourceString", '[^' || "SplitChar" || ']+', 1, "ReturnSeq"))
    INTO "ResultString"
    FROM DUAL
    ;

    RETURN "ResultString";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN N'';
END;

/
