--------------------------------------------------------
--  DDL for Procedure Usp_Tf_MonthlyLM052Ovdu_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_MonthlyLM052Ovdu_Ins" 
(
    -- 參數
    TYYMM           IN  INT,        -- 本月資料年月(西元)
    EmpNo          IN  VARCHAR2    -- 經辦

)
AS
BEGIN
  DECLARE
    INS_CNT        INT;         -- 新增筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間

  BEGIN
    INS_CNT :=0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE MonthlyLM052Ovdu');

    DELETE FROM "MonthlyLM052Ovdu"
    WHERE "YearMonth" = TYYMM 
    ;

    -- 筆數預設0
    INS_CNT:=0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    DBMS_OUTPUT.PUT_LINE('INSERT MonthlyLM052Ovdu');

    -- 寫入資料
    INSERT INTO "MonthlyLM052Ovdu"
    SELECT "YearMonth"                            AS "YearMonth"  --資料年月
          ,CASE
             WHEN "OvduTerm" = 1
             THEN '1'             -- (1)逾清償1期                   
             WHEN "OvduTerm" = 2
             THEN '2'             -- (2)逾清償2期   
             WHEN "OvduTerm" IN (3,4,5,6)
             THEN '3'             -- (3)逾清償3-6期   
             ELSE 'N'             -- (N)其它期數 
           END                                    AS "OvduNo"   --逾期數
          ,DECODE("AcctCode",310,310,320,320,330) AS "AcctCode"   --310：短期放款
                                                                  --320：中期放款
                                                                  --330：長期放款
          ,SUM("PrinBalance")                     AS "LoanBal"  --放款餘額
          ,JOB_START_TIME                         AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,EmpNo                                  AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                         AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,EmpNo                                  AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "MonthlyFacBal"
    WHERE "YearMonth" = TYYMM
      AND "AcctCode" <> 990
      AND "PrinBalance" > 0
    GROUP BY "YearMonth"
            ,CASE
               WHEN "OvduTerm" = 1
               THEN '1'                     
               WHEN "OvduTerm" = 2
               THEN '2'            
               WHEN "OvduTerm" IN (3,4,5,6)
               THEN '3'             
               ELSE 'N'             
             END       
            ,DECODE("AcctCode",310,310,320,320,330)
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_MonthlyLM052Ovdu_Ins' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;


