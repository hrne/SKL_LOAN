-- 程式功能：維護 JcicB680 每月聯徵「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB680_Upd"(20200420,'999999');
--

DROP TABLE "Work_B680_Loan" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B680_Loan"
    (  "CustNo"      decimal(7, 0)   default 0 not null
     , "FacmNo"      decimal(3, 0)   default 0 not null
     , "CustId"      varchar2(10)
     , "LoanBal"     decimal(16, 2)  default 0 not null
    )
    ON COMMIT DELETE ROWS;


DROP TABLE "Work_B680_Cl" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B680_Cl"
    (  "CustNo"      decimal(7, 0)   default 0 not null
     , "ClCode1"     decimal(1, 0)   default 0 not null
     , "ClCode2"     decimal(2, 0)   default 0 not null
     , "ClNo"        decimal(7, 0)   default 0 not null
     , "EvaAmt"      decimal(16, 2)  default 0 not null
    )
    ON COMMIT DELETE ROWS;


CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB680_Upd"
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


    -- 寫入資料 Work_B680_Loan
    INSERT INTO "Work_B680_Loan"
    SELECT M."CustNo", M."FacmNo", M."CustId", SUM(M."LoanBal") AS "LoanBal"
    FROM "JcicMonthlyLoanData" M
    WHERE M."DataYM" = YYYYMM
      AND M."Status" IN ('0', '1', '2', '4', '6', '7', '8')
    GROUP BY M."CustNo", M."FacmNo", M."CustId"
      ;

    -- 寫入資料 Work_B680_Cl
    INSERT INTO "Work_B680_Cl"
    SELECT L."CustNo"
         , NVL(CL."ClCode1",0)
         , NVL(CL."ClCode2",0)
         , NVL(CL."ClNo",0)
         , NVL(CL."EvaAmt",0)
    FROM  "Work_B680_Loan" L
      LEFT JOIN "ClFac"      ON "ClFac"."CustNo"   = L."CustNo"
                            AND "ClFac"."FacmNo"   = L."FacmNo"
      LEFT JOIN "ClMain" CL  ON CL."ClCode1" = "ClFac"."ClCode1"
                            AND CL."ClCode2" = "ClFac"."ClCode2"
                            AND CL."ClNo"    = "ClFac"."ClNo"
    GROUP BY L."CustNo", CL."ClCode1", CL."ClCode2", CL."ClNo", CL."EvaAmt"
      ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB680');

    DELETE FROM "JcicB680"
    WHERE "DataYM" = YYYYMM
      ;


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB680');

    INSERT INTO "JcicB680"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , 'A'                                   AS "TranCode"          -- 交易代碼  A新增 C異動 D刪除
         , WK."CustId"                           AS "CustId"            -- 授信戶IDN/BAN
         , ' '                                   AS "CustIdErr"         -- 上欄IDN或BAN錯誤註記
         , ' '                                   AS "Filler6"           -- 空白
         , CASE
             WHEN SUM(WK."LoanBal") > SUM(CL."EvaAmt") THEN
               CASE
                 WHEN ROUND( ( SUM(WK."LoanBal") - SUM(CL."EvaAmt") ) / 1000, 0 ) = 0 THEN 1
                 ELSE ROUND( ( SUM(WK."LoanBal") - SUM(CL."EvaAmt") ) / 1000, 0 )
               END
             ELSE 0
           END                                   AS "Amt"               -- 貸款餘額扣除擔保品鑑估值之金額
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月
         , ' '                                   AS "Filler9"           -- 空白
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Work_B680_Loan" WK
        LEFT JOIN "Work_B680_Cl" CL  ON CL."CustNo"    = WK."CustNo"
    GROUP BY WK."CustId"
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB680 END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB680_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
