-- 程式功能：維護 JcicB680 每月聯徵「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB680_Upd"(20200420,'999999');
--

DROP TABLE "Work_B680" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B680"
    (  "CustId"      varchar2(10)
     , "CustNo"      decimal(7, 0)   default 0 not null
     , "FacmNo"      decimal(3, 0)   default 0 not null
     , "BormNo"      decimal(3, 0)   default 0 not null
     , "LoanBal"     decimal(16, 2)  default 0 not null
     , "LineAmt"     decimal(16, 2)  default 0 not null
    )
    ON COMMIT DELETE ROWS;


DROP TABLE "Work_B680_F" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B680_F"
    (  "CustId"      varchar2(10)
--   , "CustNo"      decimal(7, 0)   default 0 not null
--   , "FacmNo"      decimal(3, 0)   default 0 not null
     , "LineAmt"     decimal(16, 2)  default 0 not null
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

    -- 本月年月
    YYYYMM := TBSDYF / 100;
    --
    MM := MOD(YYYYMM, 100);
    YYYY := TRUNC(YYYYMM / 100);
    IF MM = 1 THEN
       LYYYYMM := (YYYY - 1) * 100 + 12;
    ELSE
       LYYYYMM := YYYYMM - 1;
    END IF;


    -- 寫入資料 Work_B680
    INSERT INTO "Work_B680"
    SELECT
           B."CustId"            AS "CustId"            -- 授信戶IDN/BAN
         , M."CustNo"            AS "CustNo"
         , M."FacmNo"            AS "FacmNo"
         , M."BormNo"            AS "BormNo"
         , NVL(M."LoanBal",0)    AS "LoanBal"
         , NVL(F."LineAmt",0)    AS "LineAmt"
    FROM  "JcicB201" B
      LEFT JOIN "JcicMonthlyLoanData"  M  ON M."DataYM" = YYYYMM
                                         AND M."CustNo" = to_number(SUBSTR(B."AcctNo",1, 7))
                                         AND M."FacmNo" = to_number(SUBSTR(B."AcctNo",8, 3))
                                         AND M."BormNo" = to_number(SUBSTR(B."AcctNo",11,3))
      LEFT JOIN "FacMain" F    ON F."CustNo"   =  M."CustNo"
                              AND F."FacmNo"   =  M."FacmNo"
    WHERE  B."DataYM"      =  YYYYMM
      AND  B."DataEnd"     =  'Y'              -- Y:該筆授信記錄結束
      AND  M."EntCode"     IN ('0', '2')       -- 自然人
      AND  ( ( B."AcctCode"  NOT IN ('B') ) OR                          -- 正常戶，催收戶，本月結案戶
             ( B."AcctCode"  IN ('B')  AND  B."BadDebtDate" = YYYYMM )  -- 本月轉呆才納入
           )
      ;


    -- 寫入資料 Work_B680_F
    INSERT INTO "Work_B680_F"
    SELECT F."CustId"        AS "CustId"
         , SUM(F."LineAmt")  AS "LineAmt"
    FROM ( SELECT DISTINCT WK."CustId"            AS "CustId"
                         , WK."CustNo"            AS "CustNo"
                         , WK."FacmNo"            AS "FacmNo"
                         , WK."LineAmt"           AS "LineAmt"
           FROM  "Work_B680" WK
         ) F
    GROUP BY F."CustId"
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
             WHEN SUM(WK."LoanBal") <= (F."LineAmt" * 1.2)  THEN 0
             ELSE CEIL( ( SUM(WK."LoanBal") - (F."LineAmt" * 1.2) ) / 1000 )
           END                                   AS "Amt"               -- 貸款餘額扣除擔保品鑑估值之金額
                                                                           -- (ref:LNSP15A-LN15N2 (#M15N01 60 10))
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月
         , ' '                                   AS "Filler9"           -- 空白
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Work_B680" WK
      LEFT JOIN "Work_B680_F" F  ON F."CustId" = WK."CustId"
    GROUP BY WK."CustId", F."LineAmt"    -- 因為 "Work_B680_F" 已經 GROUP 過, 所以每個統編的 F."LineAmt" 都一樣
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
