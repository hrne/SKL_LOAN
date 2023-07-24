-- 程式功能：維護 MonthlyLM003 月報LM003工作檔 
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L9_MonthlyLM003_Upd"(20200131,'AB0101');
--

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_MonthlyLM003_Upd"
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元) 
    EmpNo          IN  VARCHAR2,   -- 經辦
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;        -- 新增筆數 
    UPD_CNT        INT;        -- 更新筆數 
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間  
    YYYYMM         INT;         -- 本月年月
    MM             INT;         -- 本月月份
    YYYY           INT;         -- 本月年度
  BEGIN   
    INS_CNT :=0;       
    UPD_CNT :=0;   
    
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    --　本月年月
    YYYYMM := TBSDYF / 100;     
    -- 
    MM := MOD(YYYYMM, 100);
    YYYY := TRUNC(YYYYMM / 100);
    
    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE MonthlyLM003');
    
    DELETE FROM "MonthlyLM003"
    WHERE "EntType" = 0
     AND  "DataYear" = YYYY
     AND  "DataMonth" = MM; 

    DELETE FROM "MonthlyLM003"
    WHERE "EntType" = 1
     AND  "DataYear" = YYYY
     AND  "DataMonth" = MM; 
     
    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT MonthlyLM003');

    INSERT INTO "MonthlyLM003"
    SELECT 
         D."F1"                     AS "EntType"             -- 企金別         
        ,YYYY                       AS "DataYear"            -- 資料年份        
        ,MM                         AS "DataMonth"           -- 資料月份        
        ,D."F2"                     AS "DrawdownAmt"         -- 撥款金額        
        ,D."F3"                     AS "CloseLoan"           -- 結清-利率高轉貸    
        ,D."F4"                     AS "CloseSale"           -- 結清-買賣       
        ,D."F5"                     AS "CloseSelfRepay"      -- 結清-自行還款     
        ,D."F6"                     AS "ExtraRepay"          -- 非結清-部份還款    
        ,D."F7"                     AS "PrincipalAmortize"   -- 非結清-本金攤提     
        ,D."F8"                     AS "Collection"          -- 非結清-轉催收     
        ,D."F9"                     AS "LoanBalance"         -- 月底餘額        
        ,JOB_START_TIME             AS "CreateDate"          -- 建檔日期時間  
        ,EmpNo                      AS "CreateEmpNo"         -- 建檔人員 
        ,JOB_START_TIME             AS "LastUpdate"          -- 最後更新日期時間  
        ,EmpNo                      AS "LastUpdateEmpNo"     -- 最後更新人員 
    FROM (SELECT F1
                ,SUM(F2) F2
                ,SUM(F3) F3
                ,SUM(F4) F4
                ,SUM(F5) F5
                ,SUM(F6) F6
                ,SUM(F7) F7
                ,SUM(F8) F8
                ,SUM(F9) F9
          FROM (SELECT DECODE(M."EntCode", '0', 0, 1) F1
                      ,0 F2
                      ,0 F3
                      ,0 F4
                      ,0 F5
                      ,0 F6
                      ,0 F7
                      ,0 F8
                      ,M."LoanBalance" F9
                FROM  "MonthlyLoanBal" M
                WHERE M."AcctCode" LIKE '3%'
                  AND M."YearMonth" = YYYYMM
                UNION ALL
                SELECT DECODE(C."EntCode", '0', 0, 1) F1
                      ,T."Principal" F2
                      ,0 F3
                      ,0 F4
                      ,0 F5
                      ,0 F6
                      ,0 F7
                      ,0 F8
                      ,0 F9
                 FROM  "LoanBorTx" T
                 LEFT JOIN "CustMain" C ON  C."CustNo" = T."CustNo"
                 WHERE T."TitaTxCd" IN ('L3100', 'L3440')
                   AND T."TitaHCode" = 0
                   AND T."Principal" > 0
                   AND TRUNC(T."AcDate" / 100) = YYYYMM
                UNION ALL
                SELECT DECODE(C."EntCode", '0', 0, 1) F1
                      ,DECODE(JSON_VALUE(T."OtherFields",'$.AdvanceCloseCode'),'04',T."Principal", 0) F2
                      ,DECODE(JSON_VALUE(T."OtherFields",'$.AdvanceCloseCode'),'01',T."Principal", 0) F3
                      ,DECODE(JSON_VALUE(T."OtherFields",'$.AdvanceCloseCode'),'01',0,'04',0,T."Principal") F4
                      ,0 F5
                      ,0 F6
                      ,0 F7
                      ,0 F8
                      ,0 F9
                 FROM  "LoanBorTx" T
                 LEFT JOIN "CustMain" C
                   ON  C."CustNo" = T."CustNo"
                 WHERE T."TitaTxCd" IN ('L3410', 'L3420')
                   AND T."TitaHCode" = 0
                   AND T."Principal" > 0
                   AND TRUNC(T."AcDate" / 100) = YYYYMM
                   AND JSON_VALUE(T."OtherFields",'$.CaseCloseCode') IN ('0','1','2') 
                UNION ALL
                SELECT DECODE(C."EntCode", '0', 0, 1) F1
                      ,0 F2
                      ,0 F3
                      ,0 F4
                      ,0 F5
                      ,T."ExtraRepay" F6
                      ,0 F7
                      ,0 F8
                      ,0 F9
                 FROM  "LoanBorTx" T
                 LEFT JOIN "CustMain" C
                   ON  C."CustNo" = T."CustNo"
                 WHERE T."TitaTxCd" = 'L3200'
                   AND T."TitaHCode" = 0
                   AND T."ExtraRepay" > 0
                   AND TRUNC(T."AcDate" / 100) = YYYYMM
                   AND JSON_VALUE(T."OtherFields",'$.RepayKindCode') = 1
                UNION ALL
                SELECT DECODE(C."EntCode", '0', 0, 1) F1
                      ,0 F2
                      ,0 F3
                      ,0 F4
                      ,0 F5
                      ,0 F6
                      ,T."Principal" F7
                      ,0 F8
                      ,0 F9
                 FROM  "LoanBorTx" T
                 LEFT JOIN "CustMain" C
                   ON  C."CustNo" = T."CustNo"
                 WHERE T."TitaTxCd" = 'L3200'
                   AND T."TitaHCode" = 0
                   AND T."Principal" > 0
                   AND TRUNC(T."AcDate" / 100) = YYYYMM
                   AND JSON_VALUE(T."OtherFields",'$.RepayKindCode') IN (2, 3)
                UNION ALL
                SELECT DECODE(C."EntCode", '0', 0, 1) F1
                      ,0 F2
                      ,0 F3
                      ,0 F4
                      ,0 F5
                      ,0 F6
                      ,0 F7
                      ,T."Principal" F8
                      ,0 F9
                 FROM  "LoanBorTx" T
                 LEFT JOIN "CustMain" C
                   ON  C."CustNo" = T."CustNo"
                 WHERE T."TitaTxCd" IN ('L3410', 'L3420')
                   AND T."TitaHCode" = 0
                   AND T."Principal" > 0
                   AND TRUNC(T."AcDate" / 100) = YYYYMM
                   AND JSON_VALUE(T."OtherFields",'$.CaseCloseCode') = '3'
                )
          GROUP BY "F1"
         ) D
    ;
    
    INS_CNT := INS_CNT + sql%rowcount;

--  更新 IntAmt	本月利息收入= 本月實收利息+本月提存利息-上月提存利息
    DBMS_OUTPUT.PUT_LINE('UPDATE IntAmt');


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_MonthlyLM003_Upd' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
      , JobTxSeq -- 啟動批次的交易序號
    );
    COMMIT;
    RAISE;
  END;
END;
