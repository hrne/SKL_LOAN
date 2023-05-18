--------------------------------------------------------
--  DDL for Function Fn_GetPfCoOfficeEffectiveDate
--  Example:
--  SELECT "Fn_GetPfCoOfficeEffectiveDate"(PR."Coorgnizer", PR."WorkMonth") AS "EffectiveDate"
--  FROM "PfReward" PR
--------------------------------------------------------

--------------------------------------------------------
--  DDL for Function Fn_GetPfCoOfficeEffectiveDate
--  Example:
--  SELECT "Fn_GetPfCoOfficeEffectiveDate"(PR."Coorgnizer", PR."WorkMonth") AS "EffectiveDate"
--  FROM "PfReward" PR
--------------------------------------------------------

CREATE OR REPLACE FUNCTION "Fn_GetPfCoOfficeEffectiveDate" 
(
    "InputEmployeeNo" IN VARCHAR2, -- 協辦人員編
    "InputWorkMonth"  IN DECIMAL   -- 工作月
) RETURN DECIMAL IS "Result" DECIMAL(8); -- 生效日期
BEGIN
    SELECT MAX(NVL(c."EffectiveDate",0))   AS "EffectiveDate"
    INTO "Result"
		FROM "CdWorkMonth" w 
		LEFT JOIN "PfCoOfficer" c ON c."EmpNo" = "InputEmployeeNo" 
		                         AND c."EffectiveDate" <= w."EndDate"  
		                         AND c."IneffectiveDate" >= w."StartDate" 	
       WHERE  w."Year" = TRUNC("InputWorkMonth" / 100) 
        and   w."Month" = MOD("InputWorkMonth",100)
    ;

    RETURN "Result";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN 0;
END;

