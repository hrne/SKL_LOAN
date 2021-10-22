--------------------------------------------------------
--  DDL for Function Fn_IsBankRelated
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "Fn_IsBankRelated" 
(
    "InputCustId" IN VARCHAR2 -- input, custid
) RETURN VARCHAR2 IS "Result" VARCHAR2(1); -- 回傳值為Y/N, 如果發生錯誤時回傳NULL
BEGIN
 SELECT CASE WHEN COUNT(*) > 0
             THEN 'Y'
        ELSE 'N' END AS "Result"
    INTO "Result"
    FROM (SELECT 1
          FROM "BankRelationSelf"    BRS
          WHERE BRS."CustId" = "InputCustId"

          UNION ALL

          SELECT 1
          FROM "BankRelationFamily"  BRF
          WHERE BRF."RelationId" = "InputCustId"

          UNION ALL

          SELECT 1
          FROM "BankRelationCompany" BRC
          WHERE BRC."CompanyId" = "InputCustId"
         ) "TMP"
    ;

    RETURN "Result";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN NULL;
END;

/
