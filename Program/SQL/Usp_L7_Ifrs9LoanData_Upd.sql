CREATE OR REPLACE PROCEDURE "Usp_L7_Ifrs9LoanData_Upd"
(
-- 程式功能：維護 Ifrs9LoanData 每月IFRS9撥款資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ifrs9LoanData_Upd"(20201231,'System');
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
    TMNDYF         INT;         -- 本月月底日
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

    -- 抓本月月底日
    SELECT "TmnDyf"
    INTO TMNDYF
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE Ifrs9LoanData');

    DELETE FROM "Ifrs9LoanData"
    WHERE "DataYM" = YYYYMM
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Ifrs9LoanData');

    INSERT INTO "Ifrs9LoanData"
    SELECT YYYYMM                               AS "DataYM"            -- 資料年月
         , M."CustNo"                           AS "CustNo"            -- 戶號
         , M."FacmNo"                           AS "FacmNo"            -- 額度編號
         , M."BormNo"                           AS "BormNo"            -- 撥款序號
         , NVL(M."CustId", ' ')                 AS "CustId"            -- 借款人ID / 統編
         , NVL(M."AcctCode", ' ')               AS "AcctCode"          -- 業務科目代號
         , RPAD(NVL("CdAcCode"."AcNoCode",' '),11,' ')
                                                AS "AcCode"            -- 新會計科目(11碼)
         , RPAD(NVL("CdAcCode"."AcNoCodeOld",' '),8,' ')
                                                AS "AcCodeOld"         -- 舊會計科目(8碼)
         , NVL(M."Status", 0)                   AS "Status"            -- 戶況
         , NVL(M."DrawdownDate", 0)             AS "DrawdownDate"      -- 撥款日期
         , NVL(M."MaturityDate", 0)             AS "MaturityDate"      -- 到期日(撥款)
         , NVL(M."DrawdownAmt", 0)              AS "DrawdownAmt"       -- 撥款金額
         , NVL(M."LoanBal", 0)                  AS "LoanBal"           -- 本金餘額(撥款)
         , NVL(M1."IntAmtAcc", 0)               AS "IntAmt"            -- 應收利息
         , NVL(M."StoreRate", 0)                AS "Rate"              -- 利率(撥款)
         , CASE WHEN M."Status" IN (2, 7) THEN  -- 用 "下次繳息日"
                  CASE
                    WHEN NVL(M."NextPayIntDate",0) = 0 THEN 0
                    WHEN M."NextPayIntDate" >= TMNDYF  THEN 0
                    ELSE ( TO_DATE(TMNDYF,'yyyy-mm-dd') - TO_DATE(M."NextPayIntDate",'yyyy-mm-dd') )
                  END
                WHEN NVL(M."NextPayIntDate",0) = 0 THEN 0
                WHEN M."NextPayIntDate" >= TMNDYF  THEN 0
                WHEN M."NextRepayDate"  >  TMNDYF  THEN
                     ( TO_DATE(TMNDYF,'yyyy-mm-dd') - TO_DATE(M."NextPayIntDate",'yyyy-mm-dd') )
                ELSE ( TO_DATE(TMNDYF,'yyyy-mm-dd') - TO_DATE(M."NextRepayDate",'yyyy-mm-dd') )
           END                                  AS "OvduDays"          -- 逾期繳款天數
         , NVL(M."OvduDate", 0)                 AS "OvduDate"          -- 轉催收款日期
         , NVL(M."BadDebtDate", 0)              AS "BadDebtDate"       -- 轉銷呆帳日期
         , NVL(OD."BadDebtAmt", 0)              AS "BadDebtAmt"        -- 轉銷呆帳金額
         , NVL(L."ApproveRate", 0)              AS "ApproveRate"       -- 核准利率
         , NVL(L."AmortizedCode", '0')          AS "AmortizedCode"     -- 契約當時還款方式(月底日)
         , NVL(L."RateCode", '0')               AS "RateCode"          -- 契約當時利率調整方式(月底日)
         , NVL(L."RepayFreq", 0)                AS "RepayFreq"         -- 契約約定當時還本週期(月底日)
         , NVL(L."PayIntFreq", 0)               AS "PayIntFreq"        -- 契約約定當時繳息週期(月底日)
         , NVL(L."FirstDueDate", 0)             AS "FirstDueDate"      -- 首次應繳日
         , NVL(L."TotalPeriod", 0)              AS "TotalPeriod"       -- 總期數
         , NVL(Renew."OldFacmNo", 0)            AS "AgreeBefFacmNo"    -- 協議前之額度編號
         , NVL(Renew."OldBormNo", 0)            AS "AgreeBefBormNo"    -- 協議前之撥款序號
--         , CASE WHEN M."AcBookCode" IS NULL THEN '1'
--                WHEN M."AcBookCode" IN ('000', '10H') THEN '1'
--                WHEN M."AcBookCode" IN ('201') THEN '3'
         , CASE WHEN M."AcSubBookCode" IN ('201') THEN '3'
                ELSE '1'
           END                                  AS "AcBookCode"        -- 帳冊別
         , NVL(M."PrevPayIntDate", 0)           AS "PrevPayIntDate"    -- 上次繳息日(繳息迄日)
         , JOB_START_TIME                       AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                       AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "JcicMonthlyLoanData" M
      LEFT JOIN "CdAcCode"        ON "CdAcCode"."AcctCode"  = M."AcctCode"
      LEFT JOIN "FacMain" F       ON F."CustNo"       = M."CustNo"
                                 AND F."FacmNo"       = M."FacmNo"
      LEFT JOIN "LoanBorMain" L  ON L."CustNo"    = M."CustNo"
                                AND L."FacmNo"    = M."FacmNo"
                                AND L."BormNo"    = M."BormNo"
      LEFT JOIN "MonthlyLoanBal"  M1    ON M1."YearMonth" = YYYYMM
                                       AND M1."CustNo"    = M."CustNo"
                                       AND M1."FacmNo"    = M."FacmNo"
                                       AND M1."BormNo"    = M."BormNo"
      LEFT JOIN ( SELECT OD."CustNo"            AS  "CustNo"
                       , OD."FacmNo"            AS  "FacmNo"
                       , OD."BormNo"            AS  "BormNo"
                       , OD."BadDebtAmt"        AS  "BadDebtAmt"
                       , ROW_NUMBER() Over (Partition By OD."CustNo", OD."FacmNo", OD."BormNo"
                                      Order By OD."CustNo", OD."FacmNo", OD."BormNo", OD."OvduNo" DESC )
                                                AS ROW_NO
                  FROM "LoanOverdue"  OD
                  WHERE OD."Status" IN (1, 2)
                ) OD    ON OD."CustNo"  = M."CustNo"
                       AND OD."FacmNo"  = M."FacmNo"
                       AND OD."BormNo"  = M."BormNo"
                       AND OD.ROW_NO    = 1
      --協議
      LEFT JOIN ( SELECT A.*
                  FROM   "AcLoanRenew" A
                  WHERE  A."RenewCode" = '2'    --1.一般 2.協議
                    AND  A."MainFlag"  = 'Y'    --Y:新撥款對應舊撥款的第一筆
                ) Renew  ON Renew."CustNo"      = M."CustNo"
                        AND Renew."NewFacmNo"   = M."FacmNo"
                        AND Renew."NewBormNo"   = M."BormNo"
    WHERE  M."DataYM"          =  YYYYMM
      AND  ( ( M."Status" IN (0, 2, 7) ) OR           -- 正常件, 催收, 部分轉呆
             ( M."Status" NOT IN (0, 2, 7) AND M."LoanBal" = 0 AND F."RecycleCode" = 1 AND
               TRUNC(NVL(F."RecycleDeadline",0) / 100) > YYYYMM   -- 放款餘額=0 且 為循環動用 且 未逾循環動用期限
             )
           )
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Ifrs9LoanData END: INS_CNT=' || INS_CNT);

--    UPD_CNT := UPD_CNT + sql%rowcount;
--    DBMS_OUTPUT.PUT_LINE('UPDATE Fee END');


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;

--       , ' '                                  AS "CustId"            -- 借款人ID / 統編
--       , ' '                                  AS "AcctCode"          -- 業務科目代號
--       , ' '                                  AS "AcCode"            -- 新會計科目(11碼)
--       , ' '                                  AS "AcCodeOld"         -- 舊會計科目(8碼)
--       , 0                                    AS "Status"            -- 戶況
--       , 0                                    AS "DrawdownDate"      -- 撥款日期
--       , 0                                    AS "MaturityDate"      -- 到期日(撥款)
--       , 0                                    AS "DrawdownAmt"       -- 撥款金額
--       , 0                                    AS "LoanBal"           -- 本金餘額(撥款)
--       , 0                                    AS "IntAmt"            -- 應收利息
--       , 0                                    AS "Rate"              -- 利率(撥款)
--       , 0                                    AS "OvduDays"          -- 逾期繳款天數
--       , 0                                    AS "OvduDate"          -- 轉催收款日期
--       , 0                                    AS "BadDebtDate"       -- 轉銷呆帳日期
--       , 0                                    AS "BadDebtAmt"        -- 轉銷呆帳金額
--       , 0                                    AS "ApproveRate"       -- 核准利率
--       , 0                                    AS "AmortizedCode"     -- 契約當時還款方式(月底日)
--       , 0                                    AS "RateCode"          -- 契約當時利率調整方式(月底日)
--       , 0                                    AS "RepayFreq"         -- 契約約定當時還本週期(月底日)
--       , 0                                    AS "PayIntFreq"        -- 契約約定當時繳息週期(月底日)
--       , 0                                    AS "FirstDueDate"      -- 首次應繳日
--       , 0                                    AS "TotalPeriod"       -- 總期數
--       , 0                                    AS "AgreeBefFacmNo"    -- 協議前之額度編號
--       , 0                                    AS "AgreeBefBormNo"    -- 協議前之撥款序號
--       , ' '                                  AS "AcBookCode"        -- 帳冊別
--       , 0                                    AS "PrevPayIntDate"    -- 上次繳息日(繳息迄日)
