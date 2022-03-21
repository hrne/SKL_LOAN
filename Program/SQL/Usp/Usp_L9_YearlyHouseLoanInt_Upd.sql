-- 程式功能：維護 YearlyHouseLoanInt 每年房屋擔保借款繳息工作檔 
-- 執行時機：每年年底日終批次(換日前)
-- 執行方式：EXEC "Usp_L9_YearlyHouseLoanInt_Upd"(20201231,'999999',0,0,0,'');
create or replace PROCEDURE "Usp_L9_YearlyHouseLoanInt_Upd"
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
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

    IF StartMonth != 0
    THEN SDATE := StartMonth * 100 + 01;
    END IF
    ;

    IF EndMonth != 0
    THEN EDATE := EndMonth * 100 + 31;
    END IF
    ;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE YearlyHouseLoanInt');

    DELETE FROM "YearlyHouseLoanInt"
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

    -- 新增 資金用途別=0
    DBMS_OUTPUT.PUT_LINE('INSERT YearlyHouseLoanInt');

    INSERT INTO "YearlyHouseLoanInt"
    WITH existedData AS (
      SELECT DISTINCT "CustNo"
      FROM "YearlyHouseLoanInt"
      WHERE "YearMonth" = YYYYMM
        AND JSON_VALUE("JsonFields",  '$.EndMonth') != 0
    )
    , A AS (
      SELECT A."CustNo" 
           , A."FacmNo"
           , SUM(
                CASE
                  WHEN A."DbCr" = 'C' THEN A."TxAmt"              
                ELSE 0 - A."TxAmt" END
             ) AS "YearlyInt"           
      FROM "AcDetail" A
      LEFT JOIN "FacMain" F ON F."CustNo" = A."CustNo"
                           AND F."FacmNo" = A."FacmNo"
      LEFT JOIN existedData ON existedData."CustNo" = A."CustNo"
      WHERE NVL(existedData."CustNo",0) = 0 -- 必須尚未寫入
        AND NVL(F."AcctCode",' ') != ' '
        AND A."AcDate" >= SDATE
        AND A."AcDate" <= EDATE
        AND CASE
              WHEN CustNo != 0
                   AND A."CustNo" = CustNo
              THEN 1
              WHEN CustNo != 0
              THEN 0
            ELSE 1 END = 1 -- 若有輸入戶號時，篩選戶號
        AND CASE
              WHEN NVL(AcctCode,' ') != ' '
                   AND A."AcctCode" IN ('IC1','IC2','IC3','IC4')
                   AND F."AcctCode" = AcctCode
              THEN 1
              WHEN NVL(AcctCode,' ') = ' '
                   AND A."AcctCode" IN ('IC1','IC2','IC3','IC4')
              THEN 1
            ELSE 0 END = 1 -- 若有輸入科目時，篩選科目
      GROUP BY A."CustNo"
             , A."FacmNo"
    )
    , LBM AS (
      SELECT "CustNo"
           , "FacmNo"
           , SUM("DrawdownAmt")  AS "LoanAmt"
           , SUM("LoanBal")      AS "LoanBal" 
           , MAX("MaturityDate") AS "MaturityDate" 
      FROM "LoanBorMain"
      GROUP BY "CustNo"
             , "FacmNo"
    )
    SELECT YYYYMM                     AS "YearMonth"           -- 資料年月
          ,A."CustNo"                 AS "CustNo"              -- 戶號 
          ,A."FacmNo"                 AS "FacmNo"              -- 額度 
          ,CASE
             WHEN F."UsageCode" = '02'
             THEN '02'  
           ELSE '00' END              AS "UsageCode"           -- 資金用途別 
          ,F."AcctCode"               AS "AcctCode"            -- 業務科目代號  
          ,F."RepayCode"              AS "RepayCode"           -- 繳款方式 
          ,NVL(LBM."LoanAmt",0)       AS "LoanAmt"             -- 撥款金額
          ,NVL(LBM."LoanBal",0)       AS "LoanBal"             -- 放款餘額
          ,F."FirstDrawdownDate"      AS "FirstDrawdownDate"   -- 初貸日
          ,NVL(LBM."MaturityDate",0)  AS "MaturityDate"        -- 到期日     
          ,A."YearlyInt"              AS "YearlyInt"          -- 年度繳息金額  
          ,NVL(CB."HouseBuyDate",0)   AS "HouseBuyDate"        -- 房屋取得日期
          ,JOB_START_TIME             AS "CreateDate"          -- 建檔日期時間  
          ,EmpNo                      AS "CreateEmpNo"         -- 建檔人員 
          ,JOB_START_TIME             AS "LastUpdate"          -- 最後更新日期時間  
          ,EmpNo                      AS "LastUpdateEmpNo"     -- 最後更新人員 
          ,'{"BdLoacation":"'
           || CASE
                WHEN NVL(TO_CHAR(CB."BdLocation"),' ') != ' '
                THEN TO_CHAR(CB."BdLocation")
              ELSE ' ' END
           || '"' -- BdLoacation 建物地址
           || ',' 
           || '"StartMonth":"' 
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
    FROM A 
    LEFT JOIN LBM ON LBM."CustNo" = A."CustNo"
                 AND LBM."FacmNo" = A."FacmNo"
    LEFT JOIN "FacMain" F ON F."CustNo" = A."CustNo"
                         AND F."FacmNo" = A."FacmNo"
    LEFT JOIN "ClFac" CF ON CF."CustNo" = A."CustNo"
                        AND CF."FacmNo" = A."FacmNo"
                        AND CF."MainFlag" = 'Y'
    LEFT JOIN "ClBuilding" CB ON CB."ClCode1" = CF."ClCode1"
                             AND CB."ClCode2" = CF."ClCode2"
                             AND Cb."ClNo" = CF."ClNo"
    WHERE NVL(F."AcctCode",' ') != ' '
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