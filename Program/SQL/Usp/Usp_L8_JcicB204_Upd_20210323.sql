-- 程式功能：維護 JcicB204 每日聯徵授信日報資料檔
-- 執行時機：每日日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB204_Upd"(20200316,'CSCHEN');
--

DROP TABLE "Work_B204" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B204"
    ( "AcDate"      decimal(8, 0) default 0 not null,
      "CustNo"      decimal(7, 0) default 0 not null,
      "FacmNo"      decimal(3, 0) default 0 not null,
      "BormNo"      decimal(3, 0) default 0 not null,
      "SubTranCode" varchar2(1),
      "Amt"         decimal(16, 0) default 0 not null
    )
    ON COMMIT DELETE ROWS;


CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB204_Upd"
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


    -- 寫入資料 Work_B204

    -- 當日清償
    INSERT INTO "Work_B204"
    SELECT Tx."AcDate"         AS "AcDate"
         , Tx."CustNo"         AS "CustNo"
         , Tx."FacmNo"         AS "FacmNo"
         , Tx."BormNo"         AS "BormNo"
         , 'P'                 AS "SubTranCode"       -- 交易別  -- P:每筆撥款清償後額度未解約(第10欄清償金額需大於0)
         , SUM(Tx."TxAmt")     AS "Amt"
    FROM   "LoanBorTx" Tx
      LEFT JOIN "FacMain" F  ON F."CustNo"  =  Tx."CustNo"
                            AND F."FacmNo"  =  Tx."FacmNo"
    WHERE  Tx."AcDate"                =  TBSDYF
      AND  Tx."CustNo"                >  0
      AND  NVL(Tx."TitaHCode", ' ')   IN ('0')    -- 正常
      AND  NVL(Tx."TitaTxCd", ' ')    IN ('L3200', 'L3410', 'L3420')
      AND  NVL(JSON_VALUE(Tx."OtherFields", '$.CaseCloseCode'),' ') IN ('0', '4', '5')   -- 結案區分: 正常, 催收戶本人清償, 催收戶保證人代償
      AND  ( F."AdvanceCloseCode" <> 0 AND F."UtilAmt" = 0 )
    GROUP BY  Tx."AcDate", Tx."CustNo", Tx."FacmNo", Tx."BormNo"
      ;

    -- 當日授信
    INSERT INTO "Work_B204"
    SELECT Tx."AcDate"         AS "AcDate"
         , Tx."CustNo"         AS "CustNo"
         , Tx."FacmNo"         AS "FacmNo"
         , Tx."BormNo"         AS "BormNo"
         , 'L'                 AS "SubTranCode"       -- 交易別  -- L:新增授信額度
         , SUM(Tx."TxAmt")     AS "Amt"
    FROM   "LoanBorTx" Tx
      LEFT JOIN "LoanBorMain" M  ON M."CustNo"  =  Tx."CustNo"
                                AND M."FacmNo"  =  Tx."FacmNo"
                                AND M."BormNo"  =  Tx."BormNo"
    WHERE  Tx."AcDate"       =  TBSDYF
      AND  Tx."CustNo"       >  0
      AND  NVL(Tx."TitaHCode", ' ') IN ('0')    -- 正常
      AND  NVL(Tx."TitaTxCd", ' ')  IN ('L3100', 'L3110', 'L3120')
      AND  NVL(M."RenewFlag", ' ')  NOT IN ('Y', '1')
    GROUP BY  Tx."AcDate", Tx."CustNo", Tx."FacmNo", Tx."BormNo"
      ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB204');

    DELETE FROM "JcicB204"
    WHERE "DataYMD" = TBSDYF
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB204');

    INSERT INTO "JcicB204"
    SELECT TBSDYF                                AS "DataYMD"           -- 資料日期
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , Tx."AcDate" - 19110000                AS "DataDate"          -- 新增核准額度日期／清償日期／額度到期或解約日期
         , TRIM(to_char(Tx."CustNo",'0000000')) || TRIM(to_char(Tx."FacmNo",'000')) || TRIM(to_char(Tx."BormNo",'000'))
                                                 AS "AcctNo"            -- 額度控制編碼／帳號
         , NVL("CustMain"."CustId", ' ')         AS "CustId"            -- 授信戶IDN/BAN
         , CASE WHEN F."AcctCode" IN ('310')        THEN 'E' -- 其他短期放款
                WHEN F."AcctCode" IN ('320')        THEN 'H' -- 中期放款
                WHEN F."AcctCode" IN ('330', '340') THEN 'I' -- 長期放款
                ELSE 'I'
           END                                   AS "AcctCode"          -- 科目別
         , 'S'                                   AS "SubAcctCode"       -- 科目別註記
         , CASE WHEN Tx."SubTranCode" NOT IN ('P')  THEN Tx."SubTranCode"
                ELSE
                  CASE WHEN F."ProdNo" IN ('P1') THEN  -- 判斷額度到期日前一個月
                         CASE WHEN TO_DATE(Tx."AcDate",'yyyy-mm-dd')
                                >= add_months(TO_DATE(F."MaturityDate",'yyyy-mm-dd'), -1) THEN 'A'
                              ELSE Tx."SubTranCode"
                         END
                       WHEN F."ProdNo" IN ('Z') THEN  -- 判斷 累計撥款金額(循環動用) 或 已動用餘額(非循環動用)
                         CASE WHEN    F."RecycleCode" = '1'
                                  AND F."RecycleDeadline" > 0
                                  AND F."LineAmt" <= F."UtilAmt"  THEN 'A'
                              WHEN    F."RecycleCode" <> '1'
                                  AND F."UtilDeadline" > 0
                                  AND F."LineAmt" <= F."UtilBal"  THEN 'A'
                              ELSE Tx."SubTranCode"
                         END
                       ELSE 'A'
                  END
           END                                   AS "SubTranCode"       -- 交易別
         , TRUNC(F."LineAmt" / 1000)             AS "LineAmt"           -- 訂約金額
         , CASE WHEN Tx."Amt" = 0 THEN 0
                WHEN Tx."Amt" < 1000 THEN 1
                ELSE TRUNC(Tx."Amt" / 1000)
           END                                   AS "DrawdownAmt"       -- 新增核准額度當日動撥／清償金額
         , 0                                     AS "DBR22Amt"          -- 本筆新增核准額度應計入DBR22倍規範之金額
         , ' '                                   AS "SeqNo"             -- 1~7欄資料值相同之交易序號
         , ' '                                   AS "Filler13"          -- 空白
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM "Work_B204" Tx
      LEFT JOIN "CustMain"   ON "CustMain"."CustNo"    = Tx."CustNo"
      LEFT JOIN "FacMain" F  ON F."CustNo"  =  Tx."CustNo"
                            AND F."FacmNo"  =  Tx."FacmNo"
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB204 END: INS_CNT=' || INS_CNT);

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB204_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
