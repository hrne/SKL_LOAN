-- 程式功能：維護 Ias34Bp 每月IAS34資料欄位清單B檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias34Bp_Upd"(20200420,'999999');
--

CREATE OR REPLACE PROCEDURE "Usp_L7_Ias34Bp_Upd"
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
    DBMS_OUTPUT.PUT_LINE('DELETE Ias34Bp');

    DELETE FROM "Ias34Bp"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Bp LoanRateChange');

    INSERT INTO "Ias34Bp"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , NVL(M."CustNo",0)                     AS "CustNo"            -- 戶號
         , NVL(M."CustId",' ')                   AS "CustId"            -- 借款人ID / 統編
         , NVL(M."FacmNo",0)                     AS "FacmNo"            -- 額度編號
         , NVL(M."BormNo",0)                     AS "BormNo"            -- 撥款序號
         , NVL(C."FitRate",0)                    AS "LoanRate"          -- 貸放利率
         -- 貸放利率：
         --     至小數點後第6位。例如，利率為2.12%，則本欄位值表示0.021200
         --     ex:五月底給資料，則機動/階梯型利率需拋出契約約定之多段加碼幅度後利率(浮動利率i以現在的i代入)；
         --        但固定則需六月之後的利率都要給(用以估算未來的現金流量)，浮動階梯以尚未生效的利率加碼值，算出利率後拋出
         --
         , M."RateCode"                          AS "RateCode"          -- 利率調整方式（1=機動；2=固定；3=固定階梯；4=浮動階梯）
         , NVL(C."EffectDate",0)                 AS "EffectDate"        -- 利率欄位生效日（西元年）
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Ias34Ap" M
      LEFT JOIN "LoanRateChange" C     ON C."CustNo"  = M."CustNo"
                                      AND C."FacmNo"  = M."FacmNo"
                                      AND C."BormNo"  = M."BormNo"
    WHERE  M."DataYM"  =  YYYYMM
      AND  ( ( M."RateCode" NOT IN ('4') ) OR
             ( M."RateCode" IN ('4') AND NVL(C."EffectDate",0) / 100 <= YYYYMM )  -- 非浮動階梯, skip 尚未生效者
           )
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Bp LoanRateChange END');


    -- 寫入資料
    -- 浮動階梯未生效加碼
--    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Bp 浮動階梯未生效加碼');
--
--    INSERT INTO "Ias34Bp"
--    SELECT
--           YYYYMM                                AS "DataYM"            -- 資料年月
--         , NVL(M."CustNo",0)                     AS "CustNo"            -- 戶號
--         , NVL(M."CustId",' ')                   AS "CustId"            -- 借款人ID / 統編
--         , NVL(M."FacmNo",0)                     AS "FacmNo"            -- 額度編號
--         , NVL(M."BormNo",0)                     AS "BormNo"            -- 撥款序號
--         , NVL(C."FitRate",0)                    AS "LoanRate"          -- 貸放利率
--         -- 貸放利率：
--         --     至小數點後第6位。例如，利率為2.12%，則本欄位值表示0.021200
--         --     ex:五月底給資料，則機動/階梯型利率需拋出契約約定之多段加碼幅度後利率(浮動利率i以現在的i代入)；
--         --        但固定則需六月之後的利率都要給(用以估算未來的現金流量)，浮動階梯以尚未生效的利率加碼值，算出利率後拋出
--         --
--         , M."RateCode"                          AS "RateCode"          -- 利率調整方式（1=機動；2=固定；3=固定階梯；4=浮動階梯）
--         , NVL(C."EffectDate",0)                 AS "EffectDate"        -- 利率欄位生效日（西元年）
--         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
--         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
--         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
--         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
--    FROM   "Ias34Ap" M
--      LEFT JOIN "LoanRateChange" C     ON C."CustNo"  = M."CustNo"
--                                      AND C."FacmNo"  = M."FacmNo"
--                                      AND C."BormNo"  = M."BormNo"
--    WHERE  M."DataYM"  =  YYYYMM
--      ;
--
--    INS_CNT := INS_CNT + sql%rowcount;
--    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Bp 浮動階梯未生效加碼 END');


    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Bp INS_CNT=' || INS_CNT);


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
