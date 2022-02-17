--------------------------------------------------------
--  DDL for Function Fn_IsHoliday
--  Example:
--  SELECT "Fn_IsHoliday"(20210105) -- 回傳值0:營業日 1:假日
--  FROM DUAL;
--------------------------------------------------------

CREATE OR REPLACE FUNCTION "Fn_IsHoliday" 
(
    "InputBaseDate" IN NUMBER -- 傳入日期(yyyymmdd)
) RETURN VARCHAR2 IS isHoliday NUMBER; -- 0:營業日 1:假日
BEGIN
    DECLARE
        checkTxHoliday NUMBER;
    BEGIN
        -- 防呆
        -- 1:TxHoliday不能為空
        SELECT COUNT(1)
        INTO checkTxHoliday
        FROM "TxHoliday"
        ;

        IF checkTxHoliday = 0 THEN
            RETURN 0;
        END IF;

        SELECT COUNT(1)
        INTO isHoliday
        FROM "TxHoliday"
        WHERE "Holiday" = "InputBaseDate"
        ;
    END;
    
    RETURN isHoliday;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN 0;
END;

/
