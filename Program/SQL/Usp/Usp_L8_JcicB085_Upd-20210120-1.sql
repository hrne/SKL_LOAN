-- 程式功能：維護 JcicB085 每月聯徵帳號轉換資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB085_Upd"(20200331,'CSCHEN');
--

CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB085_Upd"
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
             WHEN FLOOR( NVL(L."DrawdownDate", 0) / 100 ) < 191100 THEN FLOOR( NVL(L."DrawdownDate", 0) / 100 )
             ELSE FLOOR( NVL(L."DrawdownDate", 19110000) / 100 ) - 191100
           END                                   AS "RenewYM"           -- 轉換帳號年月
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
        LEFT JOIN "CustMain"        ON "CustMain"."CustNo"  = M."CustNo"
        LEFT JOIN "LoanBorMain"  L  ON L."CustNo"    = M."CustNo"
                                   AND L."FacmNo"    = M."NewFacmNo"
                                   AND L."BormNo"    = M."NewBormNo"
    WHERE  "JcicB201"."DataYM" =  YYYYMM
      AND  M."CustNo" IS NOT NULL
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB085 END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB085_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
