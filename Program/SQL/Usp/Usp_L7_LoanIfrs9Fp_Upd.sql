CREATE OR REPLACE PROCEDURE "Usp_L7_LoanIfrs9Fp_Upd"
(
-- 程式功能：維護 LoanIfrs9Fp 每月IFRS9欄位清單F檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_LoanIfrs9Fp_Upd"(20201231,'System');
--

    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AUTHID CURRENT_USER
AS
BEGIN
	"Usp_L7_LoanIfrs9Fp_Upd_Prear"();
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


    -- 撈符合條件最新一筆協議前後資料，寫入 "Work_FP" 暫存檔
    INSERT INTO "Work_FP"
    -- 協議後
    SELECT
           A."CustNo"                           AS "CustNo"             -- 戶號
         , 'A'                                  AS "AgreeFg"            -- 協議前後 B=協議前; A=協議後
         , A."NewFacmNo"                        AS "FacmNo"             -- 額度編號
         , A."NewBormNo"                        AS "BormNo"             -- 撥款序號
         , 1                                    AS "AgreeSeq"           -- 本筆協議序號
         -- 2021-12-20 志偉修改,AcLoanRenew同一年月可能借新還舊兩筆
         , TRUNC(A."AcDate"/100)                AS "RenewYM"            -- 協議年月
    FROM   "Ifrs9LoanData" M
         LEFT JOIN "AcLoanRenew" A   ON  A."CustNo"    =  M."CustNo"
                                    AND  A."NewFacmNo" =  M."FacmNo"
                                    AND  A."NewBormNo" =  M."BormNo"
    WHERE  M."DataYM"    =  YYYYMM
      AND  M."Status" IN (0, 2, 7)   -- 正常件, 催收, 部分轉呆
      AND  A."CustNo" IS NOT NULL
      AND  A."RenewCode" = '2' -- 2021-12-20 志偉增加:判斷2=協議件
    GROUP BY A."CustNo", A."NewFacmNo", A."NewBormNo", A."AcDate"
    -- 協議前
    UNION
    SELECT
           A."CustNo"                           AS "CustNo"             -- 戶號
         , 'B'                                  AS "AgreeFg"            -- 協議前後 B=協議前; A=協議後
         , A."OldFacmNo"                        AS "FacmNo"             -- 額度編號
         , A."OldBormNo"                        AS "BormNo"             -- 撥款序號
         , 1                                    AS "AgreeSeq"           -- 本筆協議序號
         -- 2021-12-20 志偉修改,AcLoanRenew同一年月可能借新還舊兩筆
         , TRUNC(A."AcDate"/100)                AS "RenewYM"            -- 協議年月
    FROM   "Ifrs9LoanData" M
         LEFT JOIN "AcLoanRenew" A   ON  A."CustNo"    =  M."CustNo"
                                    AND  A."NewFacmNo" =  M."FacmNo"
                                    AND  A."NewBormNo" =  M."BormNo"
    WHERE  M."DataYM"    =  YYYYMM
      AND  M."Status" IN (0, 2, 7)   -- 正常件, 催收, 部分轉呆
      AND  A."CustNo" IS NOT NULL
      AND  A."RenewCode" = '2' -- 2021-12-20 志偉增加:判斷2=協議件
    GROUP BY A."CustNo", A."OldFacmNo", A."OldBormNo", A."AcDate"
      ;


    -- 判斷是否有上一筆協議接續 (設定最多30次協議接續)，寫入 "Work_FP" 暫存檔
    FOR OccursNum IN 2 .. 30
    LOOP
      INSERT INTO "Work_FP"
      -- 協議後
      SELECT
             FP."CustNo"                        AS "CustNo"             -- 戶號
           , 'A'                                AS "AgreeFg"            -- 協議前後 B=協議前; A=協議後
           , A."NewFacmNo"                      AS "FacmNo"             -- 額度編號
           , A."NewBormNo"                      AS "BormNo"             -- 撥款序號
           , OccursNum                          AS "AgreeSeq"           -- 本筆協議序號
         -- 2021-12-20 志偉修改,AcLoanRenew同一年月可能借新還舊兩筆
         , TRUNC(A."AcDate"/100)                AS "RenewYM"            -- 協議年月
      FROM "Work_FP" FP
           LEFT JOIN "AcLoanRenew" A   ON  A."CustNo"    =  FP."CustNo"
                                      AND  A."NewFacmNo" =  FP."FacmNo"
                                      AND  A."NewBormNo" =  FP."BormNo"
      WHERE FP."AgreeSeq"  = ( OccursNum - 1 )
        AND FP."AgreeFg"   = 'B'
        AND A."NewFacmNo" IS NOT NULL
      GROUP BY FP."CustNo", A."NewFacmNo", A."NewBormNo", A."AcDate"
      -- 協議前
      UNION
      SELECT
             FP."CustNo"                        AS "CustNo"             -- 戶號
           , 'B'                                AS "AgreeFg"            -- 協議前後 B=協議前; A=協議後
           , A."OldFacmNo"                      AS "FacmNo"             -- 額度編號
           , A."OldBormNo"                      AS "BormNo"             -- 撥款序號
           , OccursNum                          AS "AgreeSeq"           -- 本筆協議序號
         -- 2021-12-20 志偉修改,AcLoanRenew同一年月可能借新還舊兩筆
         , TRUNC(A."AcDate"/100)                AS "RenewYM"            -- 協議年月
      FROM "Work_FP" FP
           LEFT JOIN "AcLoanRenew" A   ON  A."CustNo"    =  FP."CustNo"
                                      AND  A."NewFacmNo" =  FP."FacmNo"
                                      AND  A."NewBormNo" =  FP."BormNo"
      WHERE FP."AgreeSeq"  = ( OccursNum - 1 )
        AND FP."AgreeFg"   = 'B'
        AND A."NewFacmNo" IS NOT NULL
      GROUP BY FP."CustNo", A."OldFacmNo", A."OldBormNo", A."AcDate"
        ;

    END LOOP;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE LoanIfrs9Fp');

    DELETE FROM "LoanIfrs9Fp"
    WHERE "DataYM" = YYYYMM
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Fp');

    -- ** 按 "戶號,會計日" 產生 "協議編號" (ref: 2020/12 LNFFP.TXT CustNo=1201366)
    INSERT INTO "LoanIfrs9Fp"
    -- 協議後
    SELECT
           YYYYMM                               AS "DataYM"             -- 年月份
         , FP."CustNo"                          AS "CustNo"             -- 戶號
         , NVL("CustMain"."CustId", ' ')        AS "CustId"             -- 借款人ID / 統編
         , NVL(C."ROW_NO", 0)                   AS "AgreeNo"            -- 協議編號
         , FP."AgreeFg"                         AS "AgreeFg"            -- 協議前後 B=協議前; A=協議後
         , FP."FacmNo"                          AS "FacmNo"             -- 額度編號
         , FP."BormNo"                          AS "BormNo"             -- 撥款序號
         , JOB_START_TIME                       AS "CreateDate"         -- 建檔日期時間
         , EmpNo                                AS "CreateEmpNo"        -- 建檔人員
         , JOB_START_TIME                       AS "LastUpdate"         -- 最後更新日期時間
         , EmpNo                                AS "LastUpdateEmpNo"    -- 最後更新人員
    FROM "Work_FP" FP
         LEFT JOIN "CustMain"     ON  "CustMain"."CustNo"    =  FP."CustNo"
         LEFT JOIN ( SELECT A1."CustNo"    AS "CustNo"
         -- 2021-12-20 志偉修改,AcLoanRenew同一年月可能借新還舊兩筆
                          , TRUNC(A1."AcDate"/100)  AS "RenewYM"
                          , ROW_NUMBER() Over (Partition By A1."CustNo"
                                               -- 2021-12-20 志偉修改,AcLoanRenew同一年月可能借新還舊兩筆
                                               Order     By A1."CustNo", TRUNC(A1."AcDate"/100))  AS ROW_NO
                     FROM  "AcLoanRenew" A1
                     -- 2021-12-20 志偉修改,AcLoanRenew同一年月可能借新還舊兩筆
                     GROUP BY A1."CustNo", TRUNC(A1."AcDate"/100)
                   ) C     ON C."CustNo"   =  FP."CustNo"
                          AND C."RenewYM"  =  FP."RenewYM"
                          -- 2021-12-20 志偉修改,AcLoanRenew同一年月可能借新還舊兩筆 
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT LoanIfrs9Fp END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;
