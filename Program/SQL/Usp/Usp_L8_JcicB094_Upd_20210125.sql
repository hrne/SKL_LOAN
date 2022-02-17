-- 程式功能：維護 JcicB094 每月聯徵股票擔保品明細檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB094_Upd"(20200420,'999999');
--

DROP TABLE "Work_B094" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B094"
    (  "ClActNo"     varchar2(50)
     , "ClCode1"     decimal(1, 0)   default 0 not null
     , "ClCode2"     decimal(2, 0)   default 0 not null
     , "ClNo"        decimal(7, 0)   default 0 not null
    )
    ON COMMIT DELETE ROWS;


CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB094_Upd"
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


    -- 寫入資料 Work_B094    -- 從 JcicB090 撈股票擔保品編號    (需確認擔保品擷取位置)
    INSERT INTO "Work_B094"
    SELECT M."ClActNo"                           AS "ClActNo"           -- 擔保品控制編碼
         , to_number(SUBSTR(M."ClActNo",1,1))    AS "ClCode1"           -- 擔保品代號1
         , to_number(SUBSTR(M."ClActNo",2,2))    AS "ClCode2"           -- 擔保品代號2
         , to_number(SUBSTR(M."ClActNo",4,7))    AS "ClNo"              -- 擔保品編號
    FROM   "JcicB090" M
    WHERE  M."DataYM"  =  YYYYMM
      AND  M."ClActNo" IS NOT NULL
      AND  to_number(SUBSTR(M."ClActNo",1,1)) IN (3)     -- 股票
    GROUP BY M."ClActNo"
      ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB094');

    DELETE FROM "JcicB094"
    WHERE "DataYM" = YYYYMM
      ;


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB094');

    INSERT INTO "JcicB094"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '94'                                  AS "DataType"          -- 資料別
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , ' '                                   AS "Filler4"           -- 空白
         , WK."ClActNo"                          AS "ClActNo"           -- 擔保品控制編碼
         , '14'                                  AS "ClTypeJCIC"        -- 擔保品類別                     VARCHAR2  2
         , NVL(C."OwnerName",' ')                AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN  VARCHAR2  10
         , ROUND( NVL("ClMain"."EvaAmt",0) / 1000, 0)
                                                 AS "EvaAmt"            -- 鑑估值                         DECIMAL   8
         , CASE
             WHEN NVL("ClMain"."EvaDate",0) < 19110000 THEN NVL("ClMain"."EvaDate",0)
             ELSE NVL("ClMain"."EvaDate",0) - 19110000
           END                                   AS "EvaDate"           -- 鑑估日期                       DECIMAL   7
         , ROUND( NVL("ClMain"."EvaAmt",0) * NVL(C."LoanToValue",0) / 100 / 1000, 0)
                                                 AS "LoanLimitAmt"      -- 可放款值                       DECIMAL   10
         , CASE
             WHEN C."SettingStat" = '1' THEN
               CASE WHEN NVL(C."SettingDate",0) < 19110000 THEN NVL(C."SettingDate",0)
                    ELSE NVL(C."SettingDate",0) - 19110000
               END
             ELSE   0
           END                                   AS "SettingDate"       -- 設質日期                       DECIMAL   7
         , NVL(C."CompanyId",' ')                AS "CompanyId"         -- 發行機構 BAN                   VARCHAR2  8
         , 'TW'                                  AS "CompanyCountry"    -- 發行機構所在國別               VARCHAR2  2
         , NVL(C."StockCode",' ')                AS "StockCode"         -- 股票代號                       VARCHAR2  10
         , CASE
             WHEN NVL(C."StockType",' ') IN ('3') THEN 2
             ELSE 1
           END                                   AS "StockType"         -- 股票種類  1=普通股 2=特別股    DECIMAL   1
         , 'TWD'                                 AS "Currency"          -- 幣別                           VARCHAR2  3
         , NVL(C."SettingBalance",0)             AS "SettingBalance"    -- 設定股數餘額                   DECIMAL   14
         , ROUND(NVL(M."LoanBal",0) / 1000, 0)   AS "LoanBal"           -- 股票質押授信餘額               DECIMAL   10
         , CASE
             WHEN C."InsiderJobTitle" IS NULL THEN ' '
             WHEN C."InsiderJobTitle" = '01' THEN 'A'
             WHEN C."InsiderJobTitle" = '02' THEN 'B'
             WHEN C."InsiderJobTitle" = '03' THEN 'C'
             WHEN C."InsiderJobTitle" = '04' THEN 'D'
             WHEN C."InsiderJobTitle" = '05' THEN 'E'
             WHEN C."InsiderJobTitle" = '06' THEN 'G'
             WHEN C."InsiderJobTitle" = '07' THEN 'H'
             WHEN C."InsiderJobTitle" = '08' THEN 'I'
             WHEN C."InsiderJobTitle" = '09' THEN 'J'
             WHEN C."InsiderJobTitle" = '10' THEN 'L'
             WHEN C."InsiderJobTitle" = '11' THEN 'Z'
             ELSE ' '
           END                                   AS "InsiderJobTitle"   -- 公司內部人職稱                 VARCHAR2  1
         , CASE
             WHEN C."InsiderPosition" IS NULL THEN ' '
             WHEN C."InsiderPosition" = '01' THEN '0'
             WHEN C."InsiderPosition" = '02' THEN '1'
             WHEN C."InsiderPosition" = '03' THEN '2'
             WHEN C."InsiderPosition" = '04' THEN '3'
             WHEN C."InsiderPosition" = '05' THEN '4'
             WHEN C."InsiderPosition" = '06' THEN '5'
             WHEN C."InsiderPosition" = '07' THEN '6'
             WHEN C."InsiderPosition" = '08' THEN '8'  -- ???
             WHEN C."InsiderPosition" = '09' THEN '8'  -- ???
             ELSE ' '
           END                                   AS "InsiderPosition"   -- 公司內部人身分註記             VARCHAR2  1
         , NVL(C."LegalPersonId",' ')            AS "LegalPersonId"     -- 公司內部人法定關係人           VARCHAR2  10
         , ROUND(NVL("ClMain"."DispPrice",0) / 1000, 0)
                                                 AS "DispPrice"         -- 處分價格                       DECIMAL   8
         , ' '                                   AS "Filler19"          -- 空白                           VARCHAR2  14
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月                   DECIMAL   5
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Work_B094" WK
        LEFT JOIN "ClStock" C        ON C."ClCode1"   = WK."ClCode1"
                                    AND C."ClCode2"   = WK."ClCode2"
                                    AND C."ClNo"      = WK."ClNo"
        LEFT JOIN "ClMain"           ON "ClMain"."ClCode1"  = WK."ClCode1"
                                    AND "ClMain"."ClCode2"  = WK."ClCode2"
                                    AND "ClMain"."ClNo"     = WK."ClNo"
        LEFT JOIN ( SELECT M."ClCode1", M."ClCode2", M."ClNo", SUM(NVL(M."LoanBal",0)) AS "LoanBal"
                    FROM   "JcicMonthlyLoanData" M
                    WHERE  M."ClNo" > 0
                    GROUP BY M."ClCode1", M."ClCode2", M."ClNo"
                    ORDER BY M."ClCode1", M."ClCode2", M."ClNo"
                  ) M     ON M."ClCode1"  = WK."ClCode1"
                         AND M."ClCode2"  = WK."ClCode2"
                         AND M."ClNo"     = WK."ClNo"      -- 做為撥款序號之主要擔保品
      ;


    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB094 END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB094_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
