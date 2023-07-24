CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L7_LoanIfrs9Fp_Upd" 
(
-- 程式功能：維護 LoanIfrs9Fp 每月IFRS9欄位清單F檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_LoanIfrs9Fp_Upd"(20201231,'999999');
--

    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
)
AUTHID CURRENT_USER
AS
BEGIN
	"Usp_L7_LoanIfrs9Fp_Upd_Prear"();
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


    -- 撈符合條件最新一筆協議前後資料，寫入 "Work_FP" 暫存檔
    INSERT INTO "Work_FP"
    WITH RawData AS (
        SELECT DISTINCT
               "CustNo"
             , "NewFacmNo"
             , "NewBormNo"
             , "OldFacmNo"
             , "OldBormNo"
             , NVL( JSON_VALUE ("OtherFields", '$.NegNo'),0) AS "AgreeSeq"
        FROM "AcLoanRenew"
        WHERE "RenewCode" = '2'
    )
--    , OrderData AS (
--        SELECT "CustNo"
--             , "NewFacmNo"
--             , "NewBormNo"
--             , "OldFacmNo"
--             , "OldBormNo"
--             , DENSE_RANK()
--               OVER (
--                   PARTITION BY "CustNo"
--                   ORDER BY "NewFacmNo"
--                          , "NewBormNo"
--               ) AS "AgreeSeq"
--        FROM RawData
--    )
    -- 協議後
    SELECT "CustNo"                             AS "CustNo"             -- 戶號
         , 'A'                                  AS "AgreeFg"            -- 協議前後 B=協議前; A=協議後
         , "NewFacmNo"                          AS "FacmNo"             -- 額度編號
         , "NewBormNo"                          AS "BormNo"             -- 撥款序號
         , "AgreeSeq"                           AS "AgreeSeq"           -- 本筆協議序號
         , 0                                    AS "RenewYM"            -- 協議年月
    FROM RawData
    UNION
    -- 協議前
    SELECT "CustNo"                             AS "CustNo"             -- 戶號
         , 'B'                                  AS "AgreeFg"            -- 協議前後 B=協議前; A=協議後
         , "OldFacmNo"                          AS "FacmNo"             -- 額度編號
         , "OldBormNo"                          AS "BormNo"             -- 撥款序號
         , "AgreeSeq"                           AS "AgreeSeq"           -- 本筆協議序號
         , 0                                    AS "RenewYM"            -- 協議年月
    FROM RawData
    ;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE LoanIfrs9Fp');

    DELETE FROM "LoanIfrs9Fp"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Fp');

    INSERT INTO "LoanIfrs9Fp"
    SELECT
           YYYYMM                               AS "DataYM"             -- 年月份
         , FP."CustNo"                          AS "CustNo"             -- 戶號
         , NVL("CustMain"."CustId", ' ')        AS "CustId"             -- 借款人ID / 統編
         , FP."AgreeSeq"                        AS "AgreeNo"            -- 協議編號
         , FP."AgreeFg"                         AS "AgreeFg"            -- 協議前後 B=協議前; A=協議後
         , FP."FacmNo"                          AS "FacmNo"             -- 額度編號
         , FP."BormNo"                          AS "BormNo"             -- 撥款序號
         , JOB_START_TIME                       AS "CreateDate"         -- 建檔日期時間
         , EmpNo                                AS "CreateEmpNo"        -- 建檔人員
         , JOB_START_TIME                       AS "LastUpdate"         -- 最後更新日期時間
         , EmpNo                                AS "LastUpdateEmpNo"    -- 最後更新人員
    FROM "Work_FP" FP
    LEFT JOIN "CustMain"     ON  "CustMain"."CustNo"    =  FP."CustNo"
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Fp END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L7_LoanIfrs9Fp_Upd' -- UspName 預存程序名稱
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