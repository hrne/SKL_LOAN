-- 程式功能：維護 MonthlyLM032 月報LM032工作檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L9_MonthlyLM032_Upd"(20200930,'AB0101',20200831);

CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L9_MonthlyLM032_Upd"
(
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    LMBSDYF        IN  INT,        -- 上月底營業日(西元)
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;        -- 新增筆數
    UPD_CNT        INT;        -- 更新筆數
    JOB_START_TIME TIMESTAMP;  -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間
    THIS_MONTH     INT;         -- 本月年月
    LAST_MONTH     INT;         -- 前月年月
  BEGIN
    INS_CNT :=0;
    UPD_CNT :=0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 本月年月
    THIS_MONTH := TRUNC(TBSDYF / 100);
    -- 前月年月
    LAST_MONTH := TRUNC(LMBSDYF / 100);

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE MonthlyLM032');

    DELETE FROM "MonthlyLM032"
    WHERE ADTYMT = LAST_MONTH
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT MonthlyLM032');

    INSERT INTO "MonthlyLM032"
    SELECT L."YearMonth"          AS "ADTYMT"   -- 前期資料年月
          ,L."ClCode1"            AS "GDRID1"   -- 前期擔保品代號1
          ,L."OvduTerm"           AS "W08PPR"   -- 前期逾期期數
          ,L."CustNo"             AS "LMSACN"   -- 前期戶號
          ,L."FacmNo"             AS "LMSAPN"   -- 前期額度號碼
          ,L."PrinBalance"        AS "W08LBL"   -- 前期本金餘額
          ,L."OvduDays"           AS "W08DLY"   -- 前期逾期天數
          ,CASE
             WHEN M."Status" = 2 THEN '轉催' 
             WHEN M."Status" IN (3, 8, 9) THEN '結案'
             WHEN M."OvduDays" = 0 THEN '正常'
           ELSE ' ' END           AS "STATUS"   -- 當期戶況
          ,NVL(M."YearMonth",0)   AS "ADTYMT01" -- 當期資料年月
          ,NVL(M."ClCode1",0)     AS "GDRID101" -- 當期擔保品代號1
          ,NVL(M."OvduTerm",0)    AS "W08PPR01" -- 當期逾期期數
          ,NVL(M."CustNo",0)      AS "LMSACN01" -- 當期戶號
          ,NVL(M."FacmNo",0)      AS "LMSAPN01" -- 當期額度號碼
          ,NVL(M."PrinBalance",0) AS "W08LBL01" -- 當期本金餘額
          ,NVL(M."OvduDays",0)    AS "W08DLY01" -- 當期逾期天數
          ,NVL(M."AcctCode",'0')  AS "ACTACT"   -- 當期業務科目
          ,JOB_START_TIME         AS "CreateDate"          -- 建檔日期時間
          ,EmpNo                  AS "CreateEmpNo"         -- 建檔人員
          ,JOB_START_TIME         AS "LastUpdate"          -- 最後更新日期時間
          ,EmpNo                  AS "LastUpdateEmpNo"     -- 最後更新人員
    FROM "MonthlyFacBal" L
    LEFT JOIN "MonthlyFacBal" M ON M."YearMonth" = THIS_MONTH -- 取當月月底日資料
                               AND M."CustNo"    =  L."CustNo"
                               AND M."FacmNo"    =  L."FacmNo"
    WHERE L."YearMonth" = LAST_MONTH -- 取前月月底日資料
   		AND L."Status" IN (0, 4)
      AND L."OvduDays" > 0
    ;

    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_MonthlyLM032_Upd' -- UspName 預存程序名稱
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
