CREATE OR REPLACE FUNCTION "Fn_GetNextRepayDate" 
(
    "InputPrevPayIntDate" IN DECIMAL , -- 傳入上次應繳日(8碼西元年月日yyyymmdd)
    "InputSpecificDd"     IN DECIMAL , -- 傳入指定應繳日(2碼)
    "InputFreqBase"       IN DECIMAL , -- 傳入周期基準 1:日 2:月 3:週
    "InputPayIntFreq"     IN DECIMAL , -- 傳入繳息周期
    "InputAddTerms"       IN DECIMAL   -- 傳入往後推幾個週期 
) RETURN DECIMAL IS "Result" DECIMAL(8);
BEGIN
--------------------------------------------------------
--  DDL for Function Fn_GetNextRepayDate
--  Example:
--  SELECT "Fn_GetNextRepayDate"(LB."PrevPayIntDate",LB."SpecificDd",LB."FreqBase",LB."PayIntFreq",1) AS "NextRepayDate"
--       , LB."PrevPayIntDate"
--       , LB."SpecificDd"
--       , LB."FreqBase"
--       , LB."PayIntFreq"
--  FROM "LoanBorMain" Lb
--------------------------------------------------------
    SELECT CASE
             WHEN "InputFreqBase" = 2 -- 1:日 2:月 3:週
             THEN
               CASE
                 -- 只有週期基準為月才會參考指定應繳日
                 -- 若計算結果的最後兩碼 大於 指定應繳日(2碼)
                 WHEN SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE("InputPrevPayIntDate",'yyyymmdd'),"InputAddTerms" * "InputPayIntFreq"),'yyyymmdd'),-2) > LPAD("InputSpecificDd",2,'0')
                 -- 重組下次繳息日
                 THEN TO_NUMBER(SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE("InputPrevPayIntDate",'yyyymmdd'),"InputAddTerms" * "InputPayIntFreq"),'yyyymmdd'),0,6) || LPAD("InputSpecificDd",2,'0'))
               ELSE TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE("InputPrevPayIntDate",'yyyymmdd'),"InputAddTerms" * "InputPayIntFreq"),'yyyymmdd'))
               END
             WHEN "InputFreqBase" = 1 -- 日
             THEN TO_NUMBER(TO_CHAR(TO_DATE("InputPrevPayIntDate",'yyyymmdd') + "InputAddTerms" * "InputPayIntFreq",'yyyymmdd'))
             WHEN "InputFreqBase" = 3 -- 周
             THEN TO_NUMBER(TO_CHAR(TO_DATE("InputPrevPayIntDate",'yyyymmdd') + "InputAddTerms" * "InputPayIntFreq" * 7,'yyyymmdd'))
           ELSE 0 END
    INTO "Result"
    FROM DUAL
    ;

    RETURN "Result";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN 0;
END;

/
