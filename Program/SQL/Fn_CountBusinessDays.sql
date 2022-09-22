--------------------------------------------------------
--  DDL for Function Fn_CountBusinessDays
--  Example:
--  SELECT "Fn_CountBusinessDays"(20210105,20210106) -- RETURN 1
--  FROM DUAL;
--  SELECT "Fn_CountBusinessDays"(20210106,20210105) -- RETURN -1
--  FROM DUAL;
--------------------------------------------------------

CREATE OR REPLACE FUNCTION "Fn_CountBusinessDays" 
(
    "InputDateA" IN NUMBER -- 傳入日期A(yyyymmdd)
    ,"InputDateB" IN NUMBER -- 傳入日期B(yyyymmdd)
) RETURN VARCHAR2 IS "ResultDays" NUMBER; -- 回傳天數(日期B-日期A)
BEGIN
    DECLARE
        checkTxHoliday NUMBER;
        loopCounts NUMBER;
        dateCursor NUMBER;
        dateFinalTarget NUMBER;
        baseDay NUMBER; -- 增加或減少
    BEGIN
        -- 防呆
        -- 1:TxHoliday不能為空
        -- 2:傳入日期及修改天數的方向有資料
        SELECT MAX(CASE
                     WHEN "Holiday" > "InputDateA"
                     THEN 1
                     WHEN "Holiday" < "InputDateA"
                     THEN 1
                     WHEN "Holiday" > "InputDateB"
                     THEN 1
                     WHEN "Holiday" < "InputDateB"
                     THEN 1
                   ELSE 0 END)
        INTO checkTxHoliday
        FROM "TxHoliday"
        ;

        IF checkTxHoliday = 0 THEN
            RETURN 0;
        END IF;

        -- 若欲修改的天數為0,直接回傳傳入日期
        IF "InputDateA" = "InputDateB" THEN
            RETURN 0;
        END IF;

        loopCounts := 0;

        IF "InputDateA" < "InputDateB" THEN
            dateCursor := "InputDateA";
            dateFinalTarget := "InputDateB";
        ELSE dateCursor := "InputDateB";
             dateFinalTarget := "InputDateA";
        END IF;
        
        WHILE dateCursor != dateFinalTarget
        LOOP
            IF "Fn_IsHoliday"(dateCursor) = 0 THEN
                -- 找到營業日,迴圈次數+1
                loopCounts := loopCounts + 1;
            END IF;
            dateCursor := TO_NUMBER(TO_CHAR(TO_DATE(dateCursor,'yyyymmdd') + 1,'yyyymmdd'));
        END LOOP;
        "ResultDays" := loopCounts;
    END;

    IF "InputDateA" < "InputDateB" THEN
        RETURN "ResultDays";
    ELSE RETURN 0 - "ResultDays";
    END IF;
    
    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN 0;
END;

/
