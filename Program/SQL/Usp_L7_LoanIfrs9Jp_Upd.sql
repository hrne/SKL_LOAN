CREATE OR REPLACE PROCEDURE "Usp_L7_LoanIfrs9Jp_Upd"
(
-- 程式功能：維護 LoanIfrs9Jp 每月IFRS9欄位清單10
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_LoanIfrs9Jp_Upd"(20201231,'System');
--
    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;         -- 新增筆數
    UPD_CNT        INT;         -- 更新筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間
    YYYYMM         INT;         -- 本月年月
    LYYYYMM        INT;         -- 上月年月
    MM             INT;         -- 本月月份
    YYYY           INT;         -- 本月年度
  BEGIN
    INS_CNT := 0;
    UPD_CNT := 0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    --　本月年月
    YYYYMM := TBSDYF / 100;
    --
    MM := MOD(YYYYMM, 100);
    YYYY := TRUNC(YYYYMM / 100);
    IF MM = 1 THEN
       LYYYYMM := (YYYY - 1) * 100 + 12;
    ELSE
       LYYYYMM := YYYYMM - 1;
    END IF;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE LoanIfrs9Jp');

    DELETE FROM "LoanIfrs9Jp"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Jp');
    INS_CNT := 0;

    INSERT INTO "LoanIfrs9Jp"
    SELECT
           YYYYMM                                      AS "DataYM"            -- 資料年月
         , TRUNC(NVL(M."AcDate",0) / 100)              AS "AcDateYM"          -- 發生時會計日期年月
         , M."CustNo"                                  AS "CustNo"            -- 戶號
         , M."NewFacmNo"                               AS "NewFacmNo"         -- 新額度編號
         , M."NewBormNo"                               AS "NewBormNo"         -- 新撥款序號
         , M."OldFacmNo"                               AS "OldFacmNo"         -- 新額度編號
         , M."OldBormNo"                               AS "OldBormNo"         -- 新撥款序號
         , JOB_START_TIME                              AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                       AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                              AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                       AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "AcLoanRenew" M
    WHERE  M."AcDate" > 20120831      -- 借新還舊之舊撥款序號資料檔資料從201209開始
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Jp END');
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Jp INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;
