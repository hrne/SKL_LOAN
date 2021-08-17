--------------------------------------------------------
--  DDL for Function Fn_GetTelNo
--  Example:
--  SELECT "Fn_GetTelNo"(CM."CustUKey",'01')
--  FROM "CustMain" CM
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetTelNo" 
(
    "InputCustUKey"    IN VARCHAR2,  -- 客戶識別碼
    "InputTelTypeCode" IN VARCHAR2   -- 電話種類(01:公司;02:住家;03:手機;04:傳真;05:簡訊;06:催收聯絡;09:其他)
) RETURN VARCHAR2 IS "Result" VARCHAR2(20);
BEGIN
    SELECT TMP."TelNo" AS "TelNo"
    INTO "Result"
    FROM (SELECT CASE
                   WHEN "TelArea" IS NOT NULL THEN "TelArea" || '-' -- 電話區碼
                 ELSE '' END
                 || CASE
                      WHEN "TelNo" IS NOT NULL THEN "TelNo"-- 電話號碼
                    ELSE '' END
                 || CASE
                      WHEN "TelExt" IS NOT NULL THEN '-' || "TelExt"  -- 分機號碼
                    ELSE '' END AS "TelNo"
               , ROW_NUMBER() OVER (PARTITION BY "CustUKey"
                                               , "TelTypeCode"
                                    ORDER BY "RelationCode") AS "Seq"
          FROM "CustTelNo"
          WHERE "CustUKey" = "InputCustUKey"
            AND "TelTypeCode" = "InputTelTypeCode"
            AND "Enable" = 'Y'
         ) TMP
    WHERE TMP."Seq" = 1
    ;

    RETURN "Result";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN '';
END;

/
