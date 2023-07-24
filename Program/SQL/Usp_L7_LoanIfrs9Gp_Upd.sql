CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L7_LoanIfrs9Gp_Upd"
(
-- 程式功能：維護 LoanIfrs9Gp 每月IFRS9欄位清單7
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_LoanIfrs9Gp_Upd"(20201231,'System');
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
    DBMS_OUTPUT.PUT_LINE('DELETE LoanIfrs9Gp');

    DELETE FROM "LoanIfrs9Gp"
    WHERE "DataYM" = YYYYMM
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Gp');
    INS_CNT := 0;

    INSERT INTO "LoanIfrs9Gp"
    SELECT
           YYYYMM                                      AS "DataYM"            -- 資料年月
         , M."CustNo"                                  AS "CustNo"            -- 戶號
         , NVL(M."CustId",' ')                         AS "CustId"            -- 借款人ID / 統編
         , M."FacmNo"                                  AS "FacmNo"            -- 額度編號
         , NVL(F."ApplNo",0)                           AS "ApplNo"            -- 核准號碼
         , M."BormNo"                                  AS "BormNo"            -- 撥款序號
         , CASE WHEN NVL(F."EntCode",' ') IN ('1','2') THEN 1  --企金
                ELSE 2
           END                                         AS "CustKind"          -- 企業戶/個人戶
         , CASE WHEN NVL(M."Status",0) IN (0) THEN 1
                ELSE 2
           END                                         AS "Status"            -- 戶況
         , NVL(M."OvduDate",0)                         AS "OvduDate"          -- 轉催收款日期
         , ' '                                         AS "OriRating"         -- 原始認列時時信用評等     (空白)
         , ' '                                         AS "OriModel"          -- 原始認列時信用評等模型   (空白)
         , ' '                                         AS "Rating"            -- 財務報導日時信用評等     (空白)
         , ' '                                         AS "Model"             -- 財務報導日時信用評等模型 (空白)
         , NVL(M."OvduDays",0)                         AS "OvduDays"          -- 逾期繳款天數

         , CASE   -- 債務人屬企業戶，且其歸戶下任一債務逾期90天(含)以上
             WHEN NVL(M."OvduDays",0) >= 90 AND NVL(F."EntCode",' ') IN ('1','2') THEN 1
             ELSE 2
           END                                         AS "Stage1"            -- IAS39減損客觀條件1 (1=是 2=否)

         , CASE   -- 個人消費性放款逾期超逾90天(含)以上
             WHEN NVL(M."OvduDays",0) >= 90 AND NVL(F."EntCode",' ') NOT IN ('1','2') THEN 1
             ELSE 2
           END                                         AS "Stage2"            -- IAS39減損客觀條件2 (1=是 2=否)

         , CASE   -- 債務人屬企業戶，且其歸戶任一債務已經本行轉催收款、或符合列報逾期放款條件、或本行對該債務讓步(如協議)
         --  WHEN F."ProdNo" IN ('60','61','62')   OR NVL(M."OvduDate",0) > 0 THEN  
             WHEN NVL(F."AgreementFg", ' ') = 'Y'  OR NVL(M."OvduDate",0) > 0 THEN  -- 協議商品 or 轉催收款日期 > 0
               CASE WHEN NVL(F."EntCode", ' ') IN ('1','2') THEN 1
                    ELSE 2
               END
             ELSE 2
           END                                         AS "Stage3"            -- IAS39減損客觀條件3 (1=是 2=否)

         , CASE  -- 個人消費性放款已經本行轉催收、或符合列報逾期放款條件、或本行對該債務讓步(如協議)
         --  WHEN F."ProdNo" IN ('60','61','62')   OR NVL(M."OvduDate",0) > 0 THEN  
             WHEN NVL(F."AgreementFg", ' ') = 'Y'  OR NVL(M."OvduDate",0) > 0 THEN  -- 協議商品 or 轉催收款日期 > 0
               CASE WHEN NVL(F."EntCode", ' ') NOT IN ('1','2') THEN 1
                    ELSE 2
               END
             ELSE 2
           END                                         AS "Stage4"            -- IAS39減損客觀條件4 (1=是 2=否)

         , CASE  -- 債務人申請重組、破產或其他等程序，而進行該等程序可能使債務人免除或延遲償還債務
             WHEN I."CustNo" IS NOT NULL THEN 1  -- 特殊客觀減損狀況檔
             ELSE 2
           END                                         AS "Stage5"            -- IAS39減損客觀條件5 (1=是 2=否)

         , ' '                                         AS "PdFlagToD"         -- 內部違約機率降至D評等 (空白)
         , JOB_START_TIME                              AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                       AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                              AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                       AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Ifrs9LoanData" M
      LEFT JOIN "Ifrs9FacData" F  ON F."DataYM"       =  M."DataYM"
                                 AND F."CustNo"       =  M."CustNo"
                                 AND F."FacmNo"       =  M."FacmNo"
      LEFT JOIN "Ias39Loss" I     ON I."CustNo"       =  M."CustNo"
                                 AND I."FacmNo"       =  M."FacmNo"
                                 AND TRUNC(I."StartDate" / 100) >=  YYYYMM
                                 AND TRUNC(I."EndDate" / 100)   <=  YYYYMM
    WHERE  M."DataYM" =  YYYYMM
      AND  M."Status" IN (0, 2, 7)  -- 正常件, 催收, 部分轉呆"
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Gp END');
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Gp INS_CNT=' || INS_CNT);


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
        'Usp_L7_LoanIfrs9Gp_Upd' -- UspName 預存程序名稱
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