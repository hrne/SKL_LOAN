-- 程式功能：維護 JcicB096 每月聯徵不動產擔保品明細-地號附加檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB096_Upd"(20200420,'999999');
--

DROP TABLE "Work_B096" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B096"
    (  "ClActNo"       varchar2(50)
     , "ClCode1"       decimal(1, 0)   default 0 not null
     , "ClCode2"       decimal(2, 0)   default 0 not null
     , "ClNo"          decimal(7, 0)   default 0 not null
     , "CityJCICCode"  varchar2(1)
     , "AreaJCICCode"  decimal(2, 0)   default 0 not null
     , "IrCode"        varchar2(4)
     , "LandNo1"       decimal(4, 0)   default 0 not null
     , "LandNo2"       decimal(4, 0)   default 0 not null
     , "ClTypeJCIC"    varchar2(2)
    )
    ON COMMIT DELETE ROWS;


CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB096_Upd"
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


    -- 寫入資料 Work_B096    -- 從 JcicB090 撈不動產擔保品明細-地號 擔保品編號
    INSERT INTO "Work_B096"
    SELECT M."ClActNo"                           AS "ClActNo"           -- 擔保品控制編碼
         , to_number(SUBSTR(M."ClActNo",1,1))    AS "ClCode1"           -- 擔保品代號1
         , to_number(SUBSTR(M."ClActNo",2,2))    AS "ClCode2"           -- 擔保品代號2
         , to_number(SUBSTR(M."ClActNo",4,7))    AS "ClNo"              -- 擔保品編號
         , NVL(M."CityJCICCode", ' ')            AS "CityJCICCode"      -- 縣市別
         , NVL(M."AreaJCICCode", '0')            AS "AreaJCICCode"      -- 鄉鎮市區別
         , NVL(M."IrCode", '0000')               AS "IrCode"            -- 段、小段號
         , M."LandNo1"                           AS "LandNo1"           -- 地號-前四碼
         , M."LandNo2"                           AS "LandNo2"           -- 地號-後四碼
         , M."ClTypeJCIC"                        AS "ClTypeJCIC"        -- 擔保品類別
    FROM   "JcicB092" M
    WHERE  M."DataYM" =  YYYYMM
      AND  M."SettingDate" >= 9607  -- 押品設定日期在９６０７０１之後才要報送 (ref:AS400 LN15M1)
      AND  (M."LandNo1" > 0 OR M."LandNo2" > 0 )
    GROUP BY M."ClActNo", M."CityJCICCode", M."AreaJCICCode", M."IrCode", M."LandNo1", M."LandNo2", M."ClTypeJCIC"
      ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB096');

    DELETE FROM "JcicB096"
    WHERE "DataYM" = YYYYMM
      ;


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB096');

    INSERT INTO "JcicB096"
    SELECT
           YYYYMM                                AS "DataYM"                   -- 資料年月                      DECIMAL   6
         , '96'                                  AS "DataType"                 -- 資料別                        VARCHAR2  2
         , '458'                                 AS "BankItem"                 -- 總行代號                      VARCHAR2  3
         , '0001'                                AS "BranchItem"               -- 分行代號                      VARCHAR2  4
         , ' '                                   AS "Filler4"                  -- 空白                          VARCHAR2  2
         , WK."ClActNo"                          AS "ClActNo"                  -- 擔保品控制編碼                VARCHAR2  50
         , NVL(LandOwner."OwnerId",' ')          AS "OwnerId"                  -- 擔保品所有權人或代表人IDN/BAN VARCHAR2  10
         , NVL(WK."CityJCICCode", ' ')           AS "CityJCICCode"             -- 縣市別                        VARCHAR2  1
         , NVL(WK."AreaJCICCode", 0)             AS "AreaJCICCode"             -- 鄉鎮市區別                    DECIMAL   2
--       , CASE
--           WHEN "ClLand"."IrCode" IS NOT NULL THEN SUBSTR("ClLand"."IrCode", 2, 4)
--           ELSE ' '
--         END                                   AS "IrCode"                   -- 段、小段號                    VARCHAR2  4
         , NVL(WK."IrCode", '0000')              AS "IrCode"                   -- 段、小段號                    VARCHAR2  4
--       , WK."LandNo1"                          AS "LandNo1"                  -- 地號-前四碼                   DECIMAL   4
         , CASE
             WHEN WK."LandNo1" = 0  THEN 1  -- (ref:AS400 LN15M1)
             ELSE WK."LandNo1"
           END                                   AS "LandNo1"                  -- 地號-前四碼                   DECIMAL   4
         , WK."LandNo2"                          AS "LandNo2"                  -- 地號-後四碼                   DECIMAL   4
         , CASE
             WHEN TRIM(NVL("ClLand"."LandCode",'')) = '' THEN ' '  -- 空白
             WHEN "ClLand"."LandCode" IN ('01')          THEN 'C'  -- 建
             WHEN "ClLand"."LandCode" IN ('02')          THEN 'A'  -- 田
             WHEN "ClLand"."LandCode" IN ('03')          THEN 'B'  -- 旱
             WHEN "ClLand"."LandCode" IN ('05')          THEN 'D'  -- 道
             WHEN "ClLand"."LandCode" IN ('04','06','07','08','09','10') THEN 'Z'  -- 其他
             ELSE ' '                                                              -- 空白
           END                                   AS "LandCode"                 -- 地目  (ref:AS400 LN15M1)      VARCHAR2  1
         , CASE
             WHEN NVL("ClLand"."Area",0) = 0 THEN 0.01
             ELSE TRUNC(NVL("ClLand"."Area",0)  / 0.3025, 2)
           END                                   AS "Area"                     -- 面積                          DECIMAL   10
--"ClLand"."LandZoningCode" , 土地使用區分 , VARCHAR2 , 2 , 共用代碼檔
-- 01: 特定農業區02: 一般農業區03: 鄉村區04: 工業區05: 森林區06: 山坡地保育區07: 風景區08: 特定專用區09: 國家公園區
-- 10: 住宅區11: 商業區12: 行政區13: 工業區14: 文教區15: 農業區16: 風景區17: 保護區18: 水岸發展區19: 漁業區
-- 20: 倉儲區21: 保存區22: 葬儀業區23: 特定專用區24: 其他分區25: 道路26: 公園27: 綠地28: 廣場29: 兒童遊樂場
-- 30: 民用航空站31: 停車場32: 河道33: 港埠34: 學校35: 社教機構36: 體育場37: 市場38: 醫療衛生機構39: 機關
-- 40: 公用事業41: 綠帶42: 加油站43: 其他公共設施44: 道路保留地45: 公園保留地46: 綠地保留地47: 廣場保留地48: 兒童樂園場保留地49: 民用航空站保留地
-- 50: 停車場保留地51: 河道保留地52: 港埠保留地53: 學校保留地54: 社教機構保留地55: 體育場保留地56: 市場保留地57: 醫療衛生機構保留地58: 機關保留地59: 公用事業保留地
-- 60: 加油站保留地61: 其他保留地
         , CASE
             WHEN "ClLand"."LandZoningCode" IS NULL                  THEN 'Z' --其他
             WHEN "ClLand"."LandZoningCode" IN ('1')                 THEN 'A' --住宅區
             WHEN "ClLand"."LandZoningCode" IN ('2')                 THEN 'B' --商業區
             WHEN "ClLand"."LandZoningCode" IN ('4')                 THEN 'Z' --其他
             WHEN "ClLand"."LandZoningCode" IN ('3','5','6','7','8') THEN 'R' --鄉村區
             ELSE 'Z'   --其他
           END                                   AS "LandZoningCode"           -- 使用分區   VARCHAR2  1   -- (ref:LN15M1 (#M3760 98))

--       , CASE
--           WHEN "ClLand"."LandZoningCode" IS NULL             THEN 'Z' --其他
--           ELSE
--             CASE
--               WHEN "ClLand"."LandZoningCode" IN ('01')       THEN 'P' --特定農業區
--               WHEN "ClLand"."LandZoningCode" IN ('02')       THEN 'Q' --一般農業區
--               WHEN "ClLand"."LandZoningCode" IN ('03')       THEN 'R' --鄉村區
--               WHEN "ClLand"."LandZoningCode" IN ('04')       THEN 'S' --工業區
--               WHEN "ClLand"."LandZoningCode" IN ('05')       THEN 'T' --森林區
--               WHEN "ClLand"."LandZoningCode" IN ('06')       THEN 'U' --山坡地保育區
--               WHEN "ClLand"."LandZoningCode" IN ('07')       THEN 'V' --風景區
--               WHEN "ClLand"."LandZoningCode" IN ('09')       THEN 'W' --國家公園區
--           --  WHEN "ClLand"."LandZoningCode" IN ('')         THEN 'X' --河川區
--               WHEN "ClLand"."LandZoningCode" IN ('08')       THEN 'Y' --特定專用區
--           --  WHEN "ClLand"."LandZoningCode" IN ('')         THEN 'Z' --其他
--
--               WHEN WK."ClTypeJCIC" NOT IN ('20','21')        THEN 'O' --都市土地 擔保品類別非20,21
--
--               WHEN "ClLand"."LandZoningCode" IN ('10')       THEN 'A' --住宅區
--               WHEN "ClLand"."LandZoningCode" IN ('11')       THEN 'B' --商業區
--               WHEN "ClLand"."LandZoningCode" IN ('13')       THEN 'C' --工業區
--               WHEN "ClLand"."LandZoningCode" IN ('12')       THEN 'D' --行政區
--               WHEN "ClLand"."LandZoningCode" IN ('14')       THEN 'E' --文教區
--               WHEN "ClLand"."LandZoningCode" IN ('16')       THEN 'F' --風景區
--               WHEN "ClLand"."LandZoningCode" IN ('15')       THEN 'G' --農業區
--               WHEN "ClLand"."LandZoningCode" IN ('17')       THEN 'H' --保護區
--               WHEN "ClLand"."LandZoningCode" IN ('20')       THEN 'I' --倉庫區
--               WHEN "ClLand"."LandZoningCode" IN ('18')       THEN 'J' --行水區
--               WHEN "ClLand"."LandZoningCode" IN ('21')       THEN 'K' --保存區
--               WHEN "ClLand"."LandZoningCode" IN ('22','23')  THEN 'L' --特定專用區
--               WHEN "ClLand"."LandZoningCode" >= '25' AND "ClLand"."LandZoningCode" <= '43'
--                                                              THEN 'M' --公共設施用地
--               ELSE                                                'N' --其他
--             END
--         END                                   AS "LandZoningCode"           -- 使用分區   --???              VARCHAR2  1

         , CASE
             WHEN "ClLand"."LandZoningCode" IS NULL             THEN 'Z'
             WHEN "ClLand"."LandZoningCode" IN ('1')            THEN 'AZ'
             WHEN "ClLand"."LandZoningCode" IN ('2')            THEN 'BZ'
             ELSE 'Z'
           END                                   AS "LandUsageType"            -- 使用地類別  都市土地 VARCHAR2  2  -- (ref:LN15M1 (#M3760 99))

--       , CASE
--           WHEN "ClLand"."LandUsageType" IS NULL            THEN 'Z '
--           WHEN "ClLand"."LandUsageType" IN ('01')          THEN 'G '
--           WHEN "ClLand"."LandUsageType" IN ('02')          THEN 'H '
--           WHEN "ClLand"."LandUsageType" IN ('03')          THEN 'I '
--           WHEN "ClLand"."LandUsageType" IN ('04')          THEN 'J '
--           WHEN "ClLand"."LandUsageType" IN ('05')          THEN 'K '
--           WHEN "ClLand"."LandUsageType" IN ('17')          THEN 'L '
--           WHEN "ClLand"."LandUsageType" IN ('18')          THEN 'M '
--           WHEN "ClLand"."LandUsageType" IN ('15')          THEN 'N '
--           WHEN "ClLand"."LandUsageType" IN ('06')          THEN 'O '
--           WHEN "ClLand"."LandUsageType" IN ('16')          THEN 'P '
--           WHEN "ClLand"."LandUsageType" IN ('07')          THEN 'Q '
--           WHEN "ClLand"."LandUsageType" IN ('08')          THEN 'R '
--           WHEN "ClLand"."LandUsageType" IN ('09')          THEN 'S '
--           WHEN "ClLand"."LandUsageType" IN ('10')          THEN 'T '
--           WHEN "ClLand"."LandUsageType" IN ('11')          THEN 'U '
--           WHEN "ClLand"."LandUsageType" IN ('12')          THEN 'V '
--           WHEN "ClLand"."LandUsageType" IN ('13')          THEN 'W '
--           WHEN "ClLand"."LandUsageType" IN ('14')          THEN 'X '
--           ELSE 'Z '
--         END                                   AS "LandUsageType"            -- 使用地類別  --???都市土地     VARCHAR2  2
         , CASE
             WHEN ROUND( NVL("ClLand"."PostedLandValue",0) / 1000, 0) = 0 THEN 10    -- (ref:AS400 LN15M1)
             ELSE ROUND( NVL("ClLand"."PostedLandValue",0) / 1000, 0)
           END                                   AS "PostedLandValue"          -- 公告土地現值                  DECIMAL   10
         , CASE
             WHEN NVL("ClLand"."PostedLandValueYearMonth",0) = 0 THEN 9607   -- (ref:AS400 LN15M1)
             WHEN NVL("ClLand"."PostedLandValueYearMonth",0) < 191100 THEN NVL("ClLand"."PostedLandValueYearMonth",0)
             ELSE "ClLand"."PostedLandValueYearMonth" - 191100
           END                                   AS "PostedLandValueYearMonth" -- 公告土地現值年月              DECIMAL   5
         , ' '                                   AS "Filler18"                 -- 空白                          VARCHAR2  30
         , YYYYMM - 191100                       AS "JcicDataYM"               -- 資料所屬年月                  DECIMAL   5
         , JOB_START_TIME                        AS "CreateDate"               -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"              -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"               -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"          -- 最後更新人員
    FROM   "Work_B096" WK
        LEFT JOIN "ClLand"           ON "ClLand"."ClCode1"  = WK."ClCode1"
                                    AND "ClLand"."ClCode2"  = WK."ClCode2"
                                    AND "ClLand"."ClNo"     = WK."ClNo"
                                    AND "ClLand"."LandNo1"  = WK."LandNo1"
                                    AND "ClLand"."LandNo2"  = WK."LandNo2"
        LEFT JOIN "CdCity"           ON to_number("CdCity"."CityCode")  = to_number( NVL(TRIM("ClLand"."CityCode"),'0') )
        LEFT JOIN ( SELECT DISTINCT
                           O."ClCode1"
                         , O."ClCode2"
                         , O."ClNo"
--                       , FIRST_VALUE(O."OwnerId") OVER ( PARTITION BY O."ClCode1", O."ClCode2", O."ClNo"
--                                                         ORDER BY  O."OwnerRelCode" )  AS "OwnerId"
                         , O."OwnerId"     AS  "OwnerId"
                    FROM "ClLandOwner" O
                  ) LandOwner        ON LandOwner."ClCode1" = WK."ClCode1"
                                    AND LandOwner."ClCode2" = WK."ClCode2"
                                    AND LandOwner."ClNo"    = WK."ClNo"
      ;


    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB096 END: INS_CNT=' || INS_CNT);


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB096_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
