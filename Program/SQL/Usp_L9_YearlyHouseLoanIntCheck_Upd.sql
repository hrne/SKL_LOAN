CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_YearlyHouseLoanIntCheck_Upd"
(
-- 程式功能：維護 YearlyHouseLoanIntCheck 每年房屋擔保借款繳息檢核檔
-- 執行時機：執行L5811產生檢核檔時
-- 執行方式：EXEC "Usp_L9_YearlyHouseLoanIntCheck_Upd"(20201231,'999999',202012,0,0,0,'');
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    YYYYMM         IN  INT,        -- 資料年月
    StartMonth     IN  INT,        -- 起月(西元)
    EndMonth       IN  INT,        -- 迄月(西元)
    CustNo         IN  INT,        -- 戶號
    AcctCode       IN  VARCHAR2    -- 業務科目
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;        -- 新增筆數 
    UPD_CNT        INT;        -- 更新筆數 
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間  
  BEGIN   
    INS_CNT :=0;       
    UPD_CNT :=0;   

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE YearlyHouseLoanInt');

    DELETE FROM "YearlyHouseLoanIntCheck"
    WHERE CASE
            WHEN EndMonth != 0
                 AND "YearMonth" = EndMonth
            THEN 1
            WHEN EndMonth = 0
                 AND "YearMonth" = YYYYMM
            THEN 1
          ELSE 0 END = 1
      AND JSON_VALUE("JsonFields",  '$.StartMonth') = StartMonth
      AND JSON_VALUE("JsonFields",  '$.EndMonth') = EndMonth
      AND CASE
            WHEN CustNo != 0
                 AND "CustNo" = CustNo
            THEN 1
            WHEN CustNo != 0
            THEN 0
          ELSE 1 END = 1
    ; 

    INSERT INTO "YearlyHouseLoanIntCheck"
    WITH existedData AS (
      SELECT DISTINCT "CustNo"
      FROM "YearlyHouseLoanIntCheck"
      WHERE CASE
              WHEN EndMonth != 0
                   AND "YearMonth" = EndMonth
              THEN 1
              WHEN EndMonth = 0
                   AND "YearMonth" = YYYYMM
              THEN 1
            ELSE 0 END = 1
        AND JSON_VALUE("JsonFields",  '$.EndMonth') != 0
    )
    SELECT Y."YearMonth"                               AS "YearMonth"  -- 資料年月   DECIMAL 6
         , Y."CustNo"                                  AS "CustNo"     -- 戶號  DECIMAL 7
         , Y."FacmNo"                                  AS "FacmNo"     -- 額度編號  DECIMAL 3
         , Y."UsageCode"                               AS "UsageCode"  -- 資金用途別  VARCHAR2 2
         , CASE
             WHEN NVL(C."CustName",'') = ''
             THEN 'V'
           ELSE ' ' END                                AS R1 -- r1 借戶姓名空白
         , CASE
             WHEN NVL(C."CustId",'') = ''
             THEN 'V'
           ELSE ' ' END                                AS R2 -- r2 統一編號空白
         , CASE
             WHEN Y."CustNo" = 0
             THEN 'V'
             WHEN Y."FacmNo" = 0
             THEN 'V'
           ELSE ' ' END                                 AS R3 -- r3 貸款帳號空白
         , CASE
             WHEN Y."LoanAmt" = 0
             THEN 'V'
           ELSE ' ' END                                 AS R4 -- r4 最初貸款金額為０
         , CASE
             WHEN Y."LoanAmt" > F."LineAmt"
             THEN 'V'
           ELSE ' ' END                                 AS R5 -- r5 最初貸款金額＞核准額度
         , CASE
             WHEN Y."LoanAmt" < Y."LoanBal"
             THEN 'V'
           ELSE ' ' END                                 AS R6 -- r6 最初貸款金額＜放款餘額
         , CASE
             WHEN Y."FirstDrawdownDate" = 0
             THEN 'V'
           ELSE ' ' END                                 AS R7 -- r7 貸款起日空白
         , CASE
             WHEN Y."MaturityDate" = 0
             THEN 'V'
           ELSE ' ' END                                 AS R8 -- r8 貸款訖日空白
         , CASE
             WHEN Y."YearMonth" = 0
             THEN 'V'
           ELSE ' ' END                                 AS R10 -- r10 繳息所屬年月空白
         , CASE
             WHEN Y."YearlyInt" = 0
             THEN 'V'
           ELSE ' ' END                                 AS R11 -- r11 繳息金額為 0
         , CASE
             WHEN Y."AcctCode" = ' '
             THEN 'V'
           ELSE ' ' END                                 AS R12 -- r12 科子細目代號暨說明空白
         , CASE
             WHEN C."LastFacmNo" = 1
                 AND F."LastBormNo" = 1
             THEN 'V'
           ELSE ' ' END                                 AS C1 -- c1 一額度一撥款
         , CASE
             WHEN C."LastFacmNo" = 1
                 AND F."LastBormNo" > 1
             THEN 'V'
           ELSE ' ' END                                 AS C2 -- c2 一額度多撥款
         , CASE
             WHEN C."LastFacmNo" > 1
             THEN 'V'
           ELSE ' ' END                                 AS C3 -- c3 多額度多撥款
         , CASE
             WHEN NVL(R."CustNo",0) = 0
             THEN ' '
           ELSE 'V' END                                 AS C4 -- c4 借新還舊件
         , CASE
             WHEN Y."LoanBal" = 0
             THEN 'V'
           ELSE ' ' END                                 AS C5 -- c5 清償件
         ,'{"StartMonth":"' 
           || CASE
                WHEN StartMonth != 0
                THEN TO_CHAR(StartMonth)
              ELSE '0' END
           || '"' -- StartMonth 繳息所屬年月-起月	DECIMAL	西元年月yyymm
           || ',' 
           || '"EndMonth":"' 
           || CASE
                WHEN EndMonth != 0
                THEN TO_CHAR(EndMonth)
              ELSE '0' END
           || '"' -- EndMonth	繳息所屬年月-迄月	DECIMAL	西元年月yyymm
           || '}'                         AS "JsonFields"          -- jason格式紀錄欄 NVARCHAR2 300
         , JOB_START_TIME             AS "CreateDate"          -- 建檔日期時間
         , EmpNo                      AS "CreateEmpNo"         -- 建檔人員
         , JOB_START_TIME             AS "LastUpdate"          -- 最後更新日期時間
         , EmpNo                      AS "LastUpdateEmpNo"     -- 最後更新人員
    FROM "YearlyHouseLoanInt" Y
    LEFT JOIN "CustMain" C ON C."CustNo" =  Y."CustNo"
    LEFT JOIN "FacMain" F ON F."CustNo" = Y."CustNo"
                         AND F."FacmNo" = Y."FacmNo"
    LEFT JOIN (
        SELECT "CustNo"
             , "FacmNo"
        FROM "LoanBorMain"
        WHERE NVL("RenewFlag",' ') = '2' -- 借新還舊記號
        GROUP BY "CustNo"
               , "FacmNo"
    ) R ON Y."CustNo" = R."CustNo"
      AND Y."FacmNo" = R."FacmNo"
    -- 擔保品與額度關聯檔
    LEFT JOIN "ClFac" CL ON CL."CustNo" = Y."CustNo"
                        AND CL."FacmNo" = Y."FacmNo"
                        AND CL."MainFlag" = 'Y' -- 主要擔保品
    -- 擔保品不動產建物檔
    -- LEFT JOIN "ClBuilding" CB ON CB."ClCode1" = CL."ClCode1"
    --                          AND CB."ClCode2" = CL."ClCode2"
    --                          AND CB."ClNo" = CL."ClNo" 
    -- 縣市與鄉鎮區對照檔
    -- LEFT JOIN "CdArea" CA ON CA."CityCode" = CB."CityCode"
    --                      AND CA."AreaCode" = CB."AreaCode" 
    LEFT JOIN existedData ON existedData."CustNo" = Y."CustNo"
    WHERE NVL(existedData."CustNo",0) = 0
      AND Y."YearMonth" = YYYYMM
      AND CASE
            WHEN C."CuscCd" = 1
            THEN 1
            WHEN CL."ClCode1" = 1
            THEN 1
          ELSE 0 END = 1
      AND CASE -- L5810連動有值時，依值篩選
            WHEN CustNo != 0
                AND Y."CustNo" = CustNo
            THEN 1
            WHEN CustNo != 0
            THEN 0
          ELSE 1 END = 1
      AND CASE -- L5810連動有值時，依值篩選
            WHEN NVL(AcctCode,' ') != ' '
                 AND Y."AcctCode" = AcctCode
            THEN 1
            WHEN NVL(AcctCode,' ') = ' '
            THEN 1
          ELSE 0 END = 1
      AND CASE 
            WHEN NVL(JSON_VALUE(Y."JsonFields",  '$.StartMonth'),0) = StartMonth THEN 1
            WHEN StartMonth = 0 
              AND NVL(JSON_VALUE(Y."JsonFields",  '$.StartMonth'),0) = TRUNC(YYYYMM / 100) * 100 + 01 THEN 1
          ELSE 0 END = 1
      AND CASE
            WHEN NVL(JSON_VALUE(Y."JsonFields",  '$.EndMonth'),0) = EndMonth THEN 1 
            WHEN EndMonth = 0 
              AND NVL(JSON_VALUE(Y."JsonFields",  '$.EndMonth'),0) = TRUNC(YYYYMM / 100) * 100 + 12 THEN 1 
          ELSE 0 END = 1
    ;

    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_YearlyHouseLoanIntCheck_Upd' -- UspName 預存程序名稱
      , SQLCODE -- Sql Error Code (固定值)
      , SQLERRM -- Sql Error Message (固定值)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
      , EmpNo -- 發動預存程序的員工編號
    );
  END;
END;