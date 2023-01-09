CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_YearlyHouseLoanInt_Upd"
(
-- 程式功能：維護 YearlyHouseLoanInt 每年房屋擔保借款繳息工作檔 
-- 執行時機：每年年底日終批次(換日前)
-- 執行方式：EXEC "Usp_L9_YearlyHouseLoanInt_Upd"(20201230,'999999',0,0,0,'');
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
      AND CASE
            WHEN StartMonth != 0  
                 AND JSON_VALUE("JsonFields",  '$.StartMonth') = StartMonth THEN 1
            WHEN StartMonth = 0
                 AND NVL(JSON_VALUE("JsonFields",  '$.StartMonth'),0) = 0 THEN 1
            ELSE 0 END = 1          
      AND CASE
            WHEN EndMonth != 0  
                 AND JSON_VALUE("JsonFields",  '$.EndMonth') = EndMonth THEN 1
            WHEN EndMonth = 0
                 AND NVL(JSON_VALUE("JsonFields",  '$.EndMonth'),0) = EndMonth THEN 1
            ELSE 0 END = 1          
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
      WHERE CASE
              WHEN EndMonth != 0
                   AND "YearMonth" = EndMonth
              THEN 1
              WHEN EndMonth = 0
                   AND "YearMonth" = YYYYMM
              THEN 1
            ELSE 0 END = 1
        AND NVL(JSON_VALUE("JsonFields",  '$.EndMonth'),0) != 0
    )
    , A AS (
      SELECT A."CustNo" 
           , A."FacmNo"
           , SUM( CASE WHEN A."TitaHCode" = '3'
                           THEN 0 - A."Interest" - A."DelayInt"
                       ELSE A."Interest" + A."DelayInt"
                  END ) AS "YearlyInt" -- 修改:累計實收利息
      FROM "LoanBorTx" A
      LEFT JOIN "FacMain" F ON F."CustNo" = A."CustNo"
                           AND F."FacmNo" = A."FacmNo"
      LEFT JOIN existedData ON existedData."CustNo" = A."CustNo"
      WHERE NVL(existedData."CustNo",0) = 0 -- 必須尚未寫入
        AND NVL(F."AcctCode",' ') != ' '
        AND A."TitaHCode" IN ('0','3','4')  -- 新增:交易明細的訂正別
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
                   AND F."AcctCode" IN ('310','320','330','340')
                   AND F."AcctCode" = AcctCode
              THEN 1
              WHEN NVL(AcctCode,' ') = ' '
                   AND F."AcctCode" IN ('310','320','330','340')
              THEN 1
            ELSE 0 END = 1 -- 若有輸入科目時，篩選科目
      GROUP BY A."CustNo"
             , A."FacmNo"
    )
    , LBMTMP AS (
      SELECT "CustNo"
           , "FacmNo"
           , MAX(CASE WHEN "Status" NOT IN ('0') THEN "BormNo"
                    ELSE 0 END) AS "LastBormNo" 
      FROM "LoanBorMain"
      GROUP BY "CustNo"
             , "FacmNo"
    )
    , LBM AS (
      SELECT LB."CustNo"
           , LB."FacmNo"
           , SUM( CASE WHEN LB."RenewFlag" IN ('0','1') THEN LB."DrawdownAmt"
                       ELSE 0 
                  END       )  AS "LoanAmt"
           , MAX( CASE WHEN LB."Status" = '0' THEN NVL(LB."MaturityDate",0)
                      ELSE 0 END ) AS "MaturityDate1" 
           , MAX( CASE WHEN LB."Status" NOT IN ('0') AND LB."BormNo" = TMP."LastBormNo" THEN NVL(LB."MaturityDate",0)
                      ELSE 0 END) AS "MaturityDate2" 
      FROM "LoanBorMain" LB
      LEFT JOIN LBMTMP TMP ON TMP."CustNo" = LB."CustNo"
                          AND TMP."FacmNo" = LB."FacmNo"
      GROUP BY LB."CustNo"
             , LB."FacmNo"
    )
    , MFB AS (
      SELECT A."CustNo"
           , A."FacmNo"
           , CASE WHEN MF."AcctCode" = '990' THEN MF."UnpaidPrincipal" -- 催收戶
                  ELSE NVL(MF."PrinBalance",0)
             END                      AS "LoanBal" -- 月底餘額
      FROM A 
      LEFT JOIN "MonthlyFacBal" MF ON MF."CustNo" = A."CustNo"
                                  AND MF."FacmNo" = A."FacmNo"
                                  AND CASE WHEN EndMonth != 0 
                                                AND "YearMonth" =  EndMonth THEN 1
                                           WHEN EndMonth = 0
                                                AND "YearMonth" =  YYYYMM THEN 1
                                           ELSE 0 END = 1
    )  

    SELECT CASE
             WHEN EndMonth != 0
             THEN EndMonth
           ELSE YYYYMM END            AS "YearMonth"           -- 資料年月
          ,A."CustNo"                 AS "CustNo"              -- 戶號 
          ,A."FacmNo"                 AS "FacmNo"              -- 額度 
          ,CASE
             WHEN F."UsageCode" = '02'
             THEN '02'  
           ELSE '00' END              AS "UsageCode"           -- 資金用途別 
          ,F."AcctCode"               AS "AcctCode"            -- 業務科目代號  
          ,F."RepayCode"              AS "RepayCode"           -- 繳款方式 
          ,NVL(LBM."LoanAmt",0)       AS "LoanAmt"             -- 撥款金額
          ,NVL(MFB."LoanBal",0)       AS "LoanBal"             -- 放款餘額
          ,F."FirstDrawdownDate"      AS "FirstDrawdownDate"   -- 初貸日
          , CASE WHEN LBM."MaturityDate1" > 0 THEN LBM."MaturityDate1"
                 ELSE LBM."MaturityDate2"
            END                       AS "MaturityDate"        -- 到期日     
          ,A."YearlyInt"              AS "YearlyInt"           -- 年度繳息金額  
          ,NVL(CB."HouseBuyDate",0)   AS "HouseBuyDate"        -- 房屋取得日期
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
          ,JOB_START_TIME             AS "CreateDate"          -- 建檔日期時間  
          ,EmpNo                      AS "CreateEmpNo"         -- 建檔人員 
          ,JOB_START_TIME             AS "LastUpdate"          -- 最後更新日期時間  
          ,EmpNo                      AS "LastUpdateEmpNo"     -- 最後更新人員 
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
    LEFT JOIN  MFB ON MFB."CustNo" = A."CustNo"
                  AND MFB."FacmNo" = A."FacmNo"

    WHERE NVL(F."AcctCode",' ') != ' '
      --AND A."YearlyInt" > 0 -- 利息為0不需寫入,改由JAVA產媒體檔時剔除
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