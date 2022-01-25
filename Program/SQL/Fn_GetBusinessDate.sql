--------------------------------------------------------
--  DDL for Function Fn_GetBusinessDate
--  Example:
--  SELECT "Fn_GetBusinessDate"(20210105,-2) -- 找前二營業日
--  FROM DUAL;
--  SELECT "Fn_GetBusinessDate"(20210105,1) -- 找下一營業日
--  FROM DUAL;
--------------------------------------------------------

CREATE OR REPLACE FUNCTION "Fn_GetBusinessDate" 
(
    "InputBaseDate" IN NUMBER -- 傳入日期(yyyymmdd)
    ,"InputChangedDays" IN NUMBER -- 傳入欲修改的天數
) RETURN VARCHAR2 IS "ResultDate" NUMBER; -- 回傳日期(yyyymmdd)
BEGIN
    DECLARE
        checkTxHoliday NUMBER;
        loopCounts NUMBER;
        dateCursor NUMBER;
        baseDay NUMBER; -- 增加或減少
    BEGIN
        -- 防呆
        -- 1:TxHoliday不能為空
        -- 2:傳入日期及修改天數的方向有資料
        SELECT MAX(CASE
                     WHEN "InputChangedDays" > 0 AND "Holiday" > "InputBaseDate"
                     THEN 1
                     WHEN "InputChangedDays" < 0 AND "Holiday" < "InputBaseDate"
                     THEN 1
                   ELSE 0 END)
        INTO checkTxHoliday
        FROM "TxHoliday"
        ;

        IF checkTxHoliday = 0 THEN
            RETURN 0;
        END IF;

        -- 若欲修改的天數為0,直接回傳傳入日期
        IF "InputChangedDays" = 0 THEN
            RETURN "InputBaseDate";
        END IF;

        loopCounts := "InputChangedDays";

        IF "InputChangedDays" > 0 THEN
            baseDay := 1;
        ELSE baseDay := -1;
        END IF;
        
        dateCursor := "InputBaseDate";

        WHILE loopCounts != 0
        LOOP
            dateCursor := TO_NUMBER(TO_CHAR(TO_DATE(dateCursor,'yyyymmdd') + baseDay,'yyyymmdd'));
            
            IF "Fn_IsHoliday"(dateCursor) = 0 THEN
                -- 找到營業日,減少迴圈次數
                loopCounts := loopCounts - baseDay;
            END IF;
        END LOOP;
        "ResultDate" := dateCursor;
    END;

    RETURN "ResultDate";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN 0;
END;

/
