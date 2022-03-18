create or replace PROCEDURE "Usp_L7_Ias34Gp_Upd" 
(
-- 程式功能：維護 Ias34Gp 每月IAS34資料欄位清單G檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L7_Ias34Gp_Upd"(20200420,'System');
--

    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AUTHID CURRENT_USER
AS
BEGIN
	"Usp_L7_Ias34Gp_Upd_Prear"(); -- Work_GP 資料清檔

  DECLARE
    INS_CNT        INT;         -- 新增筆數
    UPD_CNT        INT;         -- 更新筆數
    JOB_START_TIME TIMESTAMP;   -- 記錄程式起始時間
    JOB_END_TIME   TIMESTAMP;   -- 記錄程式結束時間
    YYYYMM         INT;         -- 本月年月
    LYYYYMM        INT;         -- 上月年月
    MM             INT;         -- 本月月份
    YYYY           INT;         -- 本月年度
    OccursNum      NUMBER;
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


    -- 撈符合條件最新一筆協議前後資料，寫入 "Work_GP" 暫存檔
    INSERT INTO "Work_GP"
    WITH RawData AS (
        SELECT DISTINCT
               "CustNo"
             , "NewFacmNo"
             , "NewBormNo"
             , "OldFacmNo"
             , "OldBormNo"
        FROM "AcLoanRenew"
        WHERE "RenewCode" = '2'
    )
    , OrderData AS (
        SELECT "CustNo"
             , "NewFacmNo"
             , "NewBormNo"
             , "OldFacmNo"
             , "OldBormNo"
             , DENSE_RANK()
               OVER (
                   PARTITION BY "CustNo"
                   ORDER BY "NewFacmNo"
                          , "NewBormNo"
               ) AS "AgreeSeq"
        FROM RawData
    )
    -- 協議後
    SELECT "CustNo"                             AS "CustNo"             -- 戶號
         , 'A'                                  AS "AgreeFg"            -- 協議前後 B=協議前; A=協議後
         , "NewFacmNo"                          AS "FacmNo"             -- 額度編號
         , "NewBormNo"                          AS "BormNo"             -- 撥款序號
         , "AgreeSeq"                           AS "AgreeSeq"           -- 本筆協議序號
         , 0                                    AS "RenewYM"            -- 協議年月
    FROM OrderData
    UNION
    -- 協議前
    SELECT "CustNo"                             AS "CustNo"             -- 戶號
         , 'B'                                  AS "AgreeFg"            -- 協議前後 B=協議前; A=協議後
         , "OldFacmNo"                          AS "FacmNo"             -- 額度編號
         , "OldBormNo"                          AS "BormNo"             -- 撥款序號
         , "AgreeSeq"                           AS "AgreeSeq"           -- 本筆協議序號
         , 0                                    AS "RenewYM"            -- 協議年月
    FROM OrderData
    ;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE Ias34Gp');

    DELETE FROM "Ias34Gp"
    WHERE "DataYM" = YYYYMM
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Gp');

    -- ** 按 "戶號,會計日" 產生 "協議編號" (ref: 2020/12 LNM34GP CustNo=1201366)
    INSERT INTO "Ias34Gp"
    -- 協議後
    SELECT
           YYYYMM                               AS "DataYM"             -- 年月份
         , GP."CustNo"                          AS "CustNo"             -- 戶號
         , NVL("CustMain"."CustId", ' ')        AS "CustId"             -- 借款人ID / 統編
         , GP."AgreeSeq"                        AS "AgreeNo"            -- 協議編號
         , GP."AgreeFg"                         AS "AgreeFg"            -- 協議前後 B=協議前; A=協議後
         , GP."FacmNo"                          AS "FacmNo"             -- 額度編號
         , GP."BormNo"                          AS "BormNo"             -- 撥款序號
         , JOB_START_TIME                       AS "CreateDate"         -- 建檔日期時間
         , EmpNo                                AS "CreateEmpNo"        -- 建檔人員
         , JOB_START_TIME                       AS "LastUpdate"         -- 最後更新日期時間
         , EmpNo                                AS "LastUpdateEmpNo"    -- 最後更新人員
    FROM "Work_GP" GP
    LEFT JOIN "CustMain"     ON  "CustMain"."CustNo"    =  GP."CustNo"
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Ias34Gp END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;