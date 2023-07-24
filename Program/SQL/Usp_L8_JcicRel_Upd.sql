CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L8_JcicRel_Upd"
(
-- 程式功能：維護 JcicRel 聯徵授信「同一關係企業及集團企業」資料報送檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicRel_Upd"(20200430,'999999');
-- 2021-11-22 智偉修改 : 改用With AS 寫法,原本使用CustRel資料,改用ReltMain資料.

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
    YYYYMMDD       INT;         -- 本日
    L7Date         DATE;        -- 前七日
    L7YMD          INT;         -- 前七日
  BEGIN
    INS_CNT := 0;
    UPD_CNT := 0;

    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    --　本月年月
    YYYYMM := TBSDYF / 100;
    YYYYMMDD := TBSDYF;
    SELECT to_date(to_char(TBSDYF), 'YYYYMMDD') - 7
           INTO L7Date
           FROM DUAL ;
    L7YMD := to_number(to_char(L7Date, 'YYYYMMDD'));
    DBMS_OUTPUT.PUT_LINE('YYYYMMDD=' || YYYYMMDD);
    DBMS_OUTPUT.PUT_LINE('L7YMD=' || L7YMD);
    --
    MM := MOD(YYYYMM, 100);
    YYYY := TRUNC(YYYYMM / 100);
    IF MM = 1 THEN
       LYYYYMM := (YYYY - 1) * 100 + 12;
    ELSE
       LYYYYMM := YYYYMM - 1;
    END IF;

    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicRel');

    DELETE FROM "JcicRel"
    WHERE "DataYMD" = YYYYMMDD
    ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicRel');
    INS_CNT := 0;

    INSERT INTO "JcicRel"
    WITH "Temp_Work_Rel_1" AS (
      -- Temp_Work_Rel_1  A:新貸 B:續貸
      SELECT C."CustId"                     AS "CustId"
           , TRUNC(M."DrawdownDate" / 100)  AS "RelYM" -- 客戶填表年月 
           , CASE
               WHEN NVL(F."FirstDrawdownDate",0) = M."DrawdownDate" 
               THEN 'A'
             ELSE 'B'
             END                            AS "TranCode" -- 報送時機  -- A:新貸 B:續貸 
      FROM "LoanBorMain" M
      LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                           AND F."FacmNo" = M."FacmNo"
      LEFT JOIN "CustMain" C ON C."CustNo" = M."CustNo"
      WHERE C."EntCode" IN (1) -- 1:企金
        AND TRUNC(NVL(M."DrawdownDate",0) / 100) = YYYYMM  -- 撥款日期:限本月資料 
    )
    , "Temp_Work_Rel_2" AS (
      -- Temp_Work_Rel_2  C:更新  
      SELECT C1."CustId"                    AS "CustId"
          , TRUNC(to_number(to_char(RM."LastUpdate", 'YYYYMMDD')) / 100)
                                            AS "RelYM" -- 客戶填表年月 
          , 'C'                             AS "TranCode" -- 報送時機  C:更新
      FROM "ReltMain" RM
      LEFT JOIN "CustMain" C1 ON C1."CustNo"  = RM."CustNo"
      LEFT JOIN "CustMain" C2 ON C2."CustUKey"  = RM."ReltUKey"
      LEFT JOIN "Temp_Work_Rel_1" WK ON WK."CustId" = C1."CustId"
      WHERE TRUNC(to_number(to_char(RM."LastUpdate", 'YYYYMMDD')) / 100) =  YYYYMM   --  異動判斷:限本月異動資料
        AND C1."EntCode" IN (1) -- 1:企金
        AND C2."EntCode" IN (1) -- 1:企金
        AND WK."CustId" IS NULL -- 需非本月撥款,避免同戶資料重複申報
    )
    SELECT YYYYMMDD                              AS "DataYMD"           -- 資料年月日
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , CASE
             WHEN NVL(WK."RelYM",0) < 191100
             THEN NVL(WK."RelYM",0)
           ELSE WK."RelYM" - 191100
           END                                   AS "RelYM"             -- 客戶填表年月
         , WK."TranCode"                         AS "TranCode"          -- 報送時機  -- A:新貸 B:續貸 C:更新
         , WK."CustId"                           AS "CustId"            -- 授信企業統編
         , ' '                                   AS "Filler6"           -- 空白
         , NVL(C2."CustId", WK."CustId")         AS "RelId"             -- 關係企業統編
         , ' '                                   AS "Filler8"           -- 空白
         , CASE
             WHEN NVL(RM."CustNo",0) = 0 THEN ' ' -- 無關係企業資料
             WHEN NVL(RM."ReltCode",' ') IN ('08') THEN 'A' -- 08 有控制與從屬關係
             WHEN NVL(RM."ReltCode",' ') IN ('09') THEN 'B' -- 09 相互投資關係
             WHEN NVL(RM."ReltCode",' ') IN ('10','11','12') THEN 'C' -- 10 董事長 11 董事 12 監察人
           ELSE ' '
           END                                   AS "RelationCode"      -- 關係企業關係代號
         , ' '                                   AS "Filler10"          -- 空白
         , 'Z'                                   AS "EndCode"           -- 結束註記碼
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM ( SELECT WK."CustId"      AS "CustId"
                , WK."TranCode"    AS "TranCode"
                , MAX(WK."RelYM")  AS "RelYM"
           FROM ( SELECT "CustId"
                       , "TranCode"
                       , "RelYM"
                  FROM "Temp_Work_Rel_1"
                  UNION
                  SELECT "CustId"
                       , "TranCode"
                       , "RelYM"
                  FROM "Temp_Work_Rel_2"
                ) WK
           GROUP BY WK."CustId", WK."TranCode"
         ) WK
    LEFT JOIN "CustMain" C1 ON C1."CustId" = WK."CustId"
    LEFT JOIN "ReltMain" RM ON RM."CustNo" = C1."CustNo"
    LEFT JOIN "CustMain" C2 ON C2."CustUKey" = RM."ReltUKey"
    WHERE CASE
            WHEN NVL(RM."CustNo",0) = 0
            THEN 1 -- 無關係企業資料直接進表
            WHEN NVL(C1."EntCode",' ') = '1' -- 1:企金
                 AND NVL(C2."EntCode",' ') = '1' -- 1:企金
            THEN 1 -- 有關係企業資料且兩邊都是企業,進表
          ELSE 0
          END = 1
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT JcicRel END: INS_CNT=' || INS_CNT);

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L8_JcicRel_Upd' -- UspName 預存程序名稱
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
