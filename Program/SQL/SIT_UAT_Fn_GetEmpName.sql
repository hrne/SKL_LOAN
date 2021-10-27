--------------------------------------------------------
--  DDL for Function Fn_GetEmpName
--  Example:
--  SELECT "Fn_GetEmpName"(FAC."Coorgnizer",1) AS "CoName"
--  FROM "FacMain" FAC
--------------------------------------------------------

CREATE OR REPLACE FUNCTION "Fn_GetEmpName" 
(
    "InputEmployeeNo" IN VARCHAR2, -- 欄位名稱
    "ReturnFlag"      IN DECIMAL  -- 查不到時是否以員編回傳 0:傳回空白 1:傳回員編
) RETURN NVARCHAR2 IS "Result" NVARCHAR2(40);
BEGIN
    SELECT CASE
             WHEN "ReturnFlag" = 0 
             THEN NVL("Fullname",N' ')
           ELSE NVL("Fullname",TO_NCHAR("InputEmployeeNo"))
           END               AS "Item"
    INTO "Result"
    FROM "CdEmp"
    WHERE "EmployeeNo" = "InputEmployeeNo"
    ;

    RETURN "Result";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN TO_NCHAR(' ');
END;

/
