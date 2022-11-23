--------------------------------------------------------
--  DDL for Procedure Usp_L8_JcicB090_Upd
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L8_JcicB090_Upd" 
(
-- 程式功能：維護 JcicB090 每月聯徵擔保品關聯檔資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB090_Upd"(20200430,'999999');
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
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB090');

    DELETE FROM "JcicB090"
    WHERE "DataYM" = YYYYMM
      ;


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB090');
    INS_CNT := 0;

    INSERT INTO "JcicB090"
    WITH rawData AS (
      SELECT M."CustId"
           , F."ClCode1"
           , F."ClCode2"
           , F."ClNo"
           , M."FacmNo"
           , ROW_NUMBER()
             OVER (
               PARTITION BY M."CustId"
                          , M."FacmNo"
                          , NVL(CASE
                                  WHEN F."ClCode1" = 1
                                  THEN CB."CityCode"
                                  WHEN F."ClCode1" = 2
                                  THEN CL."CityCode"
                                ELSE NULL END, ' ')
                          , NVL(CASE
                                  WHEN F."ClCode1" = 1
                                  THEN CB."AreaCode"
                                  WHEN F."ClCode1" = 2
                                  THEN CL."AreaCode"
                                ELSE NULL END, ' ')
                          , NVL(CASE
                                  WHEN F."ClCode1" = 1
                                  THEN CB."BdNo1"
                                  WHEN F."ClCode1" = 2
                                  THEN CL."LandNo1"
                                ELSE NULL END
                               , '00000')
                          , NVL(CASE
                                  WHEN F."ClCode1" = 1
                                  THEN CB."BdNo2"
                                  WHEN F."ClCode1" = 2
                                  THEN CL."LandNo2"
                                ELSE NULL END
                               , '000')
               ORDER BY CASE
                          WHEN F."MainFlag" = 'Y'
                          THEN 0
                        ELSE 1 END -- 主要擔保品排在第一筆
                      , F."ClCode1"
                      , F."ClCode2"
                      , F."ClNo"
             ) AS "ClBdNoSeq"
      FROM   "JcicB080" M
          LEFT JOIN "ClFac" F    ON F."CustNo"   = SUBSTR(M."FacmNo",1,7)
                                AND F."FacmNo"   = SUBSTR(M."FacmNo",8,3)
          LEFT JOIN "ClMain" CM  ON CM."ClCode1"  = F."ClCode1"
                                AND CM."ClCode2"  = F."ClCode2"
                                AND CM."ClNo"     = F."ClNo"
          LEFT JOIN "ClBuilding" CB ON CB."ClCode1"  = F."ClCode1"
                                   AND CB."ClCode2"  = F."ClCode2"
                                   AND CB."ClNo"     = F."ClNo"
                                   AND F."ClCode1" = 1
          LEFT JOIN "ClLand" CL ON CL."ClCode1"  = F."ClCode1"
                               AND CL."ClCode2"  = F."ClCode2"
                               AND CL."ClNo"     = F."ClNo"
                               AND F."ClCode1" = 2
          LEFT JOIN "FacCaseAppl" FCA ON FCA."ApplNo" = F."ApproveNo"
          LEFT JOIN "LoanSynd" LS ON LS."SyndNo" = FCA."SyndNo"
      WHERE  M."DataYM"   =   YYYYMM
        AND  M."FacmNo"   IS  NOT NULL
        AND  F."ClNo"     IS  NOT NULL
        AND  CM."ClStatus" IN '1'  -- 已抵押
        AND  LS."SyndNo" IS NULL   -- 剔除聯貸案件
      --GROUP BY M."CustId", M."FacmNo", F."ClCode1", F."ClCode2", F."ClNo"
      ORDER BY M."CustId", M."FacmNo", F."ClCode1", F."ClCode2", F."ClNo"
    )
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '90'                                  AS "DataType"          -- 資料別
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , ' '                                   AS "Filler4"           -- 空白
         , r."CustId"                            AS "CustId"            -- 授信戶IDN/BAN
         , TRIM(to_char(r."ClCode1",'0')) || TRIM(to_char(r."ClCode2",'00')) ||
           TRIM(to_char(r."ClNo",'0000000'))     AS "ClActNo"           -- 擔保品控制編碼
         , r."FacmNo"                            AS "FacmNo"            -- 額度控制編碼
         , ' '                                   AS "GlOverseas"        -- 海外不動產擔保品資料註記
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM rawData r
    WHERE CASE
            WHEN r."ClCode1" NOT IN (1,2) -- 非房地、土地擔保品
            THEN 1
            WHEN r."ClCode1" IN (1,2) -- 房地、土地擔保品
                 AND r."ClBdNoSeq" = 1 -- 建號或地號相同時只取一筆
            THEN 1
          ELSE 0 END = 1
    ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB090 END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;

/
