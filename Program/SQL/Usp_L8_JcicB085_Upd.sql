CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L8_JcicB085_Upd"
(
-- 程式功能：維護 JcicB085 聯徵帳號轉換資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB085_Upd"(20200430,'System');
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
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB085');

    DELETE FROM "JcicB085"
    WHERE "DataYM" = YYYYMM
      ;


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB085');

    INSERT INTO "JcicB085"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '85'                                  AS "DataType"          -- 資料別
         , CASE
             WHEN FLOOR( NVL(M."AcDate", 0) / 100 ) < 191100 THEN FLOOR( NVL(M."AcDate", 0) / 100 )
             ELSE FLOOR( NVL(M."AcDate", 19110000) / 100 ) - 191100
           END                                   AS "RenewYM"           -- 轉換帳號年月
--         , CASE
--             WHEN FLOOR( NVL(L."DrawdownDate", 0) / 100 ) < 191100 THEN FLOOR( NVL(L."DrawdownDate", 0) / 100 )
--             ELSE FLOOR( NVL(L."DrawdownDate", 19110000) / 100 ) - 191100
--           END                                   AS "RenewYM"           -- 轉換帳號年月
         , "CustMain"."CustId"                   AS "CustId"            -- 授信戶IDN/BAN
         , '458'                                 AS "BefBankItem"       -- 轉換前總行代號
         , '0001'                                AS "BefBranchItem"     -- 轉換前分行代號
         , ' '                                   AS "Filler6"           -- 空白
         , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."OldFacmNo",'000')) || TRIM(to_char(M."OldBormNo",'000'))
                                                 AS "BefAcctNo"         -- 轉換前帳號
         , '458'                                 AS "AftBankItem"       -- 轉換後總行代號
         , '0001'                                AS "AftBranchItem"     -- 轉換後分行代號
         , ' '                                   AS "Filler10"          -- 空白
         , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."NewFacmNo",'000')) || TRIM(to_char(M."NewBormNo",'000'))
                                                 AS "AftAcctNo"         -- 轉換後帳號
         , ' '                                   AS "Filler12"          -- 空白
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "JcicB201"
        LEFT JOIN "AcLoanRenew"  M  ON M."CustNo"    = SUBSTR("JcicB201"."AcctNo", 1, 7)
                                   AND M."NewFacmNo" = SUBSTR("JcicB201"."AcctNo", 8, 3)
                                   AND M."NewBormNo" = SUBSTR("JcicB201"."AcctNo",11, 3)
                                   AND FLOOR( NVL(M."AcDate", 0) / 100 ) = YYYYMM   -- 調整
        LEFT JOIN "CustMain"        ON "CustMain"."CustNo"  = M."CustNo"
        LEFT JOIN "LoanBorMain"  L  ON L."CustNo"    = M."CustNo"
                                   AND L."FacmNo"    = M."NewFacmNo"
                                   AND L."BormNo"    = M."NewBormNo"
    WHERE  "JcicB201"."DataYM"  =  YYYYMM
      AND  "JcicB201"."DataEnd" =  'Y'
      --AND  FLOOR( NVL(L."DrawdownDate", 0) / 100 ) = YYYYMM -- 本月轉換
      AND  M."CustNo" IS NOT NULL
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB085 END: INS_CNT=' || INS_CNT);


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
        'Usp_L8_JcicB085_Upd' -- UspName 預存程序名稱
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
