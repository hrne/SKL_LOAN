-- 程式功能：維護 YearlyHouseLoanInt 每年房屋擔保借款繳息工作檔 
-- 執行時機：每年年底日終批次(換日前)
-- 執行方式：EXEC "Usp_L9_YearlyHouseLoanInt_Upd"(20201231,'AB0101');
--

create or replace PROCEDURE "Usp_L9_YearlyHouseLoanInt_Upd"
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;        -- 新增筆數 
    UPD_CNT        INT;        -- 更新筆數 
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間 
    YYYYMM         INT;         -- 本月年月
    SDATE          INT;         -- 起始日期
    EDATE          INT;         -- 結束日期
    YYYY           INT;         -- 本月年度   
  BEGIN   
    INS_CNT :=0;       
    UPD_CNT :=0;   

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    --　本月年月
    YYYYMM := TBSDYF / 100;
    --
    YYYY := TRUNC(YYYYMM / 100);
    SDATE := YYYY * 10000 + 0101;
    EDATE := YYYY * 10000 + 1231;
    --
    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE YearlyHouseLoanInt');

    DELETE FROM "YearlyHouseLoanInt"
    WHERE "YearMonth" = YYYYMM
    ; 

    -- 新增 資金用途別=0
    DBMS_OUTPUT.PUT_LINE('INSERT YearlyHouseLoanInt');

    INSERT INTO "YearlyHouseLoanInt"
    SELECT YYYYMM                     AS "YearMonth"           -- 資料年月
          ,A."CustNo"                 AS "CustNo"              -- 戶號 
          ,A."FacmNo"                 AS "FacmNo"              -- 額度 
          ,CASE WHEN F."UsageCode" = '02' THEN '02'                        
                ELSE '00'          
           END                        AS "UsageCode"           -- 資金用途別 
          ,F."AcctCode"               AS "AcctCode"            -- 業務科目代號  
          ,F."RepayCode"              AS "RepayCode"           -- 繳款方式 
          ,0                          AS "LoanAmt"             -- 撥款金額
          ,0                          AS "LoanBal"             -- 放款餘額
          ,F."FirstDrawdownDate"      AS "FirstDrawdownDate"   -- 初貸日
          ,0                          AS "MaturityDate"        -- 到期日     
          ,A."YearlyInt"               AS "YearlyInt"          -- 年度繳息金額  
          , 0                         AS "HouseBuyDate"        -- 房屋取得日期
          ,JOB_START_TIME             AS "CreateDate"          -- 建檔日期時間  
          ,EmpNo                      AS "CreateEmpNo"         -- 建檔人員 
          ,JOB_START_TIME             AS "LastUpdate"          -- 最後更新日期時間  
          ,EmpNo                      AS "LastUpdateEmpNo"     -- 最後更新人員 
          ,null                       AS "JsonFields"          -- jason格式紀錄欄 NVARCHAR2 300
    FROM ( SELECT 
            "CustNo" 
           ,"FacmNo"
           ,SUM(CASE
                  WHEN "DbCr" = 'C' THEN "TxAmt"              
                ELSE 0 - "TxAmt" END) AS "YearlyInt"           
           FROM "AcDetail" 
           WHERE "AcDate" >= SDATE
             AND "AcDate" <= EDATE
             AND "AcctCode" IN ('IC1','IC2','IC3','IC4')
           GROUP BY "CustNo", "FacmNo"
          ) A 
    LEFT JOIN "FacMain" F ON F."CustNo" = A."CustNo"
                         AND F."FacmNo" = A."FacmNo" 
    WHERE F."AcctCode" IS NOT NULL         
    ;

    INS_CNT := INS_CNT + sql%rowcount;

--  更新 撥款金額、放款餘額、到期日
    DBMS_OUTPUT.PUT_LINE('UPDATE LoanBalance');

    MERGE INTO "YearlyHouseLoanInt" M
    USING (SELECT Y."CustNo"
                , Y."FacmNo"
                , SUM(L."DrawdownAmt")    AS  "LoanAmt"
                , SUM(L."LoanBal")        AS  "LoanBal" 
                , MAX(L."MaturityDate")   AS  "MaturityDate" 
           FROM "YearlyHouseLoanInt" Y
           LEFT JOIN "LoanBorMain" L  
                  ON L."CustNo"   = Y."CustNo"
                 AND L."FacmNo"   = Y."FacmNo"          
           WHERE  Y."YearMonth" = YYYYMM
           GROUP BY Y."CustNo", Y."FacmNo"
          ) D
    ON (    M."YearMonth" = YYYYMM
        AND M."CustNo"    = D."CustNo"
        AND M."FacmNo"    = D."FacmNo"
       )
    WHEN MATCHED THEN UPDATE
    SET M."LoanAmt"      = D."LoanAmt"
       ,M."LoanBal"      = D."LoanBal"
       ,M."MaturityDate" = D."MaturityDate" 
    ;

    UPD_CNT := UPD_CNT + sql%rowcount;

--   更新 房屋取得日期

    DBMS_OUTPUT.PUT_LINE('UPDATE HouseBuyDate ');

    MERGE INTO "YearlyHouseLoanInt" M
    USING (SELECT Y."CustNo"
                , Y."FacmNo"
                , B."HouseBuyDate" 
           FROM "YearlyHouseLoanInt" Y
           LEFT JOIN "ClFac" C                           -- 擔保品與額度關聯檔
                  ON C."CustNo"      = Y."CustNo" 
                 AND C."FacmNo"      = Y."FacmNo"       
                 AND C."MainFlag"    = 'Y'               -- 主要擔保品
           LEFT JOIN "ClBuilding" B                      -- 擔保品不動產建物檔
                  ON B."ClCode1" = C."ClCode1"
                 AND B."ClCode2" = C."ClCode2"
                 AND B."ClNo"    = C."ClNo"
           WHERE  Y."YearMonth" = YYYYMM 
             AND  NVL(B."HouseBuyDate",0) > 0
          ) D
    ON (    M."YearMonth" = YYYYMM
        AND M."CustNo"    = D."CustNo"
        AND M."FacmNo"    = D."FacmNo"
       )
    WHEN MATCHED THEN UPDATE SET M."HouseBuyDate"      = D."HouseBuyDate"
    ;

    UPD_CNT := UPD_CNT + sql%rowcount;
    
    DBMS_OUTPUT.PUT_LINE('INSERT YearlyHouseLoanInt UsageCode 2');
        
    INSERT INTO "YearlyHouseLoanInt"
    SELECT YYYYMM                     AS "YearMonth"           -- 資料年月
          ,Y."CustNo"                 AS "CustNo"              -- 戶號 
          ,Y."FacmNo"                 AS "FacmNo"              -- 額度 
          ,'00'                       AS "UsageCode"           -- 資金用途別 
          ,Y."AcctCode"               AS "AcctCode"            -- 業務科目代號  
          ,Y."RepayCode"              AS "RepayCode"           -- 繳款方式 
          ,Y."LoanAmt"                AS "LoanAmt"             -- 撥款金額
          ,Y."LoanBal"                AS "LoanBal"             -- 放款餘額
          ,Y."FirstDrawdownDate"      AS "FirstDrawdownDate"   -- 初貸日
          ,Y."MaturityDate"           AS "MaturityDate"        -- 到期日     
          ,Y."YearlyInt"              AS "YearlyInt"           -- 年度繳息金額  
          ,"HouseBuyDate"             AS "HouseBuyDate"        -- 房屋取得日期
          ,JOB_START_TIME             AS "CreateDate"          -- 建檔日期時間  
          ,EmpNo                      AS "CreateEmpNo"         -- 建檔人員 
          ,JOB_START_TIME             AS "LastUpdate"          -- 最後更新日期時間  
          ,EmpNo                      AS "LastUpdateEmpNo"     -- 最後更新人員 
          ,null                       AS "JsonFields"          -- jason格式紀錄欄 NVARCHAR2 300
    FROM "YearlyHouseLoanInt" Y
    WHERE Y."YearMonth" = YYYYMM 
      AND Y."UsageCode" = '02'
    ;

    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_YearlyHouseLoanInt_Upd' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;