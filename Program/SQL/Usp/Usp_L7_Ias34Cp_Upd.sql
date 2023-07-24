CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L7_Ias34Cp_Upd"
(
-- 程式功能：維護 Ias34Cp 每月IAS34資料欄位清單C檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias34Cp_Upd"(20200420,'System');
--

    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2,   -- 經辦
    JobTxSeq       IN  VARCHAR2    -- 啟動批次的交易序號
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
    DBMS_OUTPUT.PUT_LINE('DELETE Ias34Cp');

    DELETE FROM "Ias34Cp"
    WHERE "DataYM" = YYYYMM
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Cp');

    INSERT INTO "Ias34Cp"
    SELECT
           YYYYMM                                      AS "DataYM"            -- 資料年月
         , NVL(M."CustNo",0)                           AS "CustNo"            -- 戶號
         , NVL(M."CustId",' ')                         AS "CustId"            -- 借款人ID / 統編
         , NVL(M."FacmNo",0)                           AS "FacmNo"            -- 額度編號
         , NVL(M."BormNo",0)                           AS "BormNo"            -- 撥款序號
--         , to_number(NVL(M."AmortizedCode",0))         AS "AmortizedCode"     -- 約定還款方式
         , CASE
             WHEN NVL(M."AmortizedCode",'0') = '1' THEN '1'  -- 1.按月繳息(按期繳息到期還本)
             WHEN NVL(M."AmortizedCode",'0') = '2' THEN '4'  -- 2.到期取息(到期繳息還本)
             WHEN NVL(M."AmortizedCode",'0') = '3' THEN '2'  -- 3.本息平均法(期金)
             WHEN NVL(M."AmortizedCode",'0') = '4' THEN '3'  -- 4.本金平均法
             WHEN NVL(M."AmortizedCode",'0') = '5' THEN '4'  -- 5.按月撥款收息(逆向貸款)  --???
             ELSE '3'
           END                                         AS "AmortizedCode"     -- 約定還款方式
--         , NVL(M."PayIntFreq",0)                       AS "PayIntFreq"        -- 繳息週期
         , CASE WHEN NVL(M."AmortizedCode",'0') = '2' THEN 0  -- 到期繳息還本
                ELSE NVL(M."PayIntFreq",0)
           END                                         AS "PayIntFreq"        -- 繳息週期
--         , NVL(M."RepayFreq",0)                        AS "RepayFreq"         -- 還本週期
         , CASE WHEN NVL(M."AmortizedCode", '0') IN ('1', '2') THEN 0  -- 到期還本
                ELSE NVL(M."RepayFreq", 0)
           END                                         AS "RepayFreq"         -- 還本週期
         , NVL(M."DrawdownDate", 0)                    AS "EffectDate"        -- 生效日期
         , JOB_START_TIME                              AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                       AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                              AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                       AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Ifrs9LoanData" M
    WHERE  M."DataYM"          =  YYYYMM
      AND  M."Status" IN (0, 2, 7)   -- 正常件, 催收, 部分轉呆
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Cp END: INS_CNT=' || INS_CNT);

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
        'Usp_L7_Ias34Cp_Upd' -- UspName 預存程序名稱
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
