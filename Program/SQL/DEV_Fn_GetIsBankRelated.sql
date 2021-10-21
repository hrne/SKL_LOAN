--------------------------------------------------------
--  DDL for Function Fn_GetIsBankRelated
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "C##ITXADMIN"."Fn_GetIsBankRelated" 
(
    "InputCustId" IN VARCHAR2 -- input, custid
) RETURN VARCHAR2 IS "Result" VARCHAR2(1); -- 回傳值為Y/N, 如果發生錯誤時回傳NULL
BEGIN
 SELECT CASE WHEN COUNT(*) > 0
             THEN 'Y'
		ELSE 'N' END AS "Result"
	INTO "Result"
	FROM (SELECT *
		  FROM "BankRelationSelf"    BRS
		      ,"BankRelationFamily"  BRF
			  ,"BankRelationCompany" BRC
		  WHERE BRS."CustId" = "InputCustId"
		     OR BRF."CustId" = "InputCustId"
			 OR BRC."CustId" = "InputCustId"
		 ) "TMP"
	;

    RETURN "Result";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN NULL;
END;

/
