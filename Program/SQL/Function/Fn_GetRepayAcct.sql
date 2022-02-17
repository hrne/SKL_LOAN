--------------------------------------------------------
--  DDL for Function Fn_GetRepayAcct
--  Example:
--  SELECT "Fn_GetRepayAcct"(FAC."CustNo",FAC."FacmNo",'0')
--  FROM "FacMain" FAC
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetRepayAcct" 
(
    "InputCustNo"   IN NUMBER,    -- 戶號
    "InputFacmNo"   IN NUMBER,    -- 額度
    "ReturnType"    IN VARCHAR2   -- 回傳種類(0:扣款銀行(代號);1:扣款帳號)
) RETURN VARCHAR2 IS "Result" VARCHAR2(14);
BEGIN
    SELECT CASE
             WHEN "ReturnType" = 0 THEN "RepayBank" -- 0:扣款銀行
             WHEN "ReturnType" = 1 THEN "RepayAcct" -- 1:取得扣款帳號
           ELSE '' END AS "TempResult"
    INTO "Result"
    FROM (SELECT "RepayBank"
               , "RepayAcct"
               , ROW_NUMBER() OVER (PARTITION BY "CustNo"
                                               , "FacmNo"
                                    ORDER BY "AuthType") AS "Seq"
          FROM "BankAuthAct"
          WHERE "CustNo" = "InputCustNo"
            AND "FacmNo" = "InputFacmNo"
            AND "Status" = '0' -- 授權成功
         ) BAA
    WHERE "Seq" = 1
    ;

    RETURN "Result";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN '';
END;

/
