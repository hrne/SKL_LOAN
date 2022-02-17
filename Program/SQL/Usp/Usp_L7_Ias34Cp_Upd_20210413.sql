-- 程式功能：維護 Ias34Cp 每月IAS34資料欄位清單C檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias34Cp_Upd"(20200420,'System');
--

CREATE OR REPLACE PROCEDURE "Usp_L7_Ias34Cp_Upd"
(
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
    DBMS_OUTPUT.PUT_LINE('DELETE Ias34Cp');

    DELETE FROM "Ias34Cp"
    WHERE "DataYM" = YYYYMM
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Cp');

    -- 先撈訂約時資料
    INSERT INTO "Ias34Cp"
    SELECT
           YYYYMM                                      AS "DataYM"            -- 資料年月
         , NVL(M."CustNo",0)                           AS "CustNo"            -- 戶號
         , NVL(M."CustId",' ')                         AS "CustId"            -- 借款人ID / 統編
         , NVL(M."FacmNo",0)                           AS "FacmNo"            -- 額度編號
         , NVL(M."BormNo",0)                           AS "BormNo"            -- 撥款序號
         , NVL(M."AmortizedCode",0)                    AS "AmortizedCode"     -- 約定還款方式
         , NVL(M."PayIntFreq",0)                       AS "PayIntFreq"        -- 繳息週期
         , NVL(M."RepayFreq",0)                        AS "RepayFreq"         -- 還本週期
        --  , CASE
        --      WHEN NVL(F."GuaranteeDate", 0) = 0 THEN NVL(A."ApproveDate",0)
        --      ELSE F."GuaranteeDate"
        --    END                                         AS "EffectDate"        -- 生效日期
         , NVL(J."DrawdownDate", 0)                    AS "EffectDate"        -- 生效日期
         , JOB_START_TIME                              AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                       AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                              AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                       AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Ias34Ap" M
      LEFT JOIN "JcicMonthlyLoanData"  J    ON  J."DataYM"   =  M."DataYM"
                                           AND  J."CustNo"   =  M."CustNo"
                                           AND  J."FacmNo"   =  M."FacmNo"
                                           AND  J."BormNo"   =  M."BormNo"
      -- LEFT JOIN "FacMain"  F  ON F."CustNo"       = M."CustNo"
      --                        AND F."FacmNo"       = M."FacmNo"
      -- LEFT JOIN "FacCaseAppl" A ON A."ApplNo"   = F."ApplNo"
    WHERE  M."DataYM"          =  YYYYMM
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
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_MonthlyLoanBa_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;

--  , NVL("Ias39Loan34Data"."FacAmortizedCode",0) AS "AmortizedCode"     -- 約定還款方式
--  , NVL("Ias39Loan34Data"."PayIntFreq",0)       AS "PayIntFreq"        -- 繳息週期
--  , NVL("Ias39Loan34Data"."RepayFreq",0)        AS "RepayFreq"         -- 還本週期
--  , NVL("Ias39Loan34Data"."ApproveDate",0)      AS "EffectDate"        -- 生效日期
-- LEFT JOIN "Ias39Loan34Data" ON "Ias39Loan34Data"."DataYM"   = M."DataYM"
--                            AND "Ias39Loan34Data"."CustNo"   = M."CustNo"
--                            AND "Ias39Loan34Data"."FacmNo"   = M."FacmNo"
--                            AND "Ias39Loan34Data"."BormNo"   = M."BormNo"
