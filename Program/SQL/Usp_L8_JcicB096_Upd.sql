create or replace NONEDITIONABLE PROCEDURE "Usp_L8_JcicB096_Upd"  
-- create by ExecSqlApp at 2022-07-19 14:09:13 
( 
-- 程式功能：維護 JcicB096 每月聯徵不動產擔保品明細-地號附加檔 
-- 執行時機：每月底日終批次(換日前) 
-- 執行方式：EXEC "Usp_L8_JcicB096_Upd"(20200430,'999999'); 
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
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB096'); 

    DELETE FROM "JcicB096" 
    WHERE "DataYM" = YYYYMM 
      ; 

    -- 寫入資料 
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB096'); 

    INSERT INTO "JcicB096" 
    WITH "rawData" AS ( 
      SELECT DISTINCT 
             M."ClActNo" 
           , M."LandNo1" 
           , M."LandNo2" 
      FROM "JcicB092" M 
      WHERE M."DataYM" =  YYYYMM 
    ) 
    , "sumData" AS ( 
      SELECT T."MainClActNo"
           , T."LandNo1"
           , T."LandNo2"
           , SUM(T."Area") AS "Area"
      FROM (
        -- 根據擔保品關聯檔(CF1 & CF2)
        -- 取得每一筆土地(PK:擔保品號碼+土地明細序號+地號)的面積
        SELECT DISTINCT
               M."ClActNo"                           AS "MainClActNo"       -- 主要擔保品控制編碼 
             , CI."ClCode1"
             , CI."ClCode2"
             , CI."ClNo"
             , L."LandSeq"
             , M."LandNo1"                           AS "LandNo1"           -- 地號-前四碼 
             , M."LandNo2"                           AS "LandNo2"           -- 地號-後四碼 
             , L."Area"                              AS "Area"              -- 土地面積 
        FROM "rawData" M 
        LEFT JOIN "ClFac" CF1 ON CF1."ClCode1" = to_number(SUBSTR(M."ClActNo",1,1)) 
                             AND CF1."ClCode2" = to_number(SUBSTR(M."ClActNo",2,2)) 
                             AND CF1."ClNo" = to_number(SUBSTR(M."ClActNo",4,7)) 
        LEFT JOIN "ClFac" CF2 ON CF2."CustNo" = CF1."CustNo" 
                             AND CF2."FacmNo" = CF1."FacmNo" 
        LEFT JOIN "ClImm" CI ON CI."ClCode1" = CF2."ClCode1"
                            AND CI."ClCode2" = CF2."ClCode2"
                            AND CI."ClNo" = CF2."ClNo"
        LEFT JOIN "ClLand" L ON L."ClCode1" = CI."ClCode1"
                            AND L."ClCode2" = CI."ClCode2"
                            AND L."ClNo" = CI."ClNo"
                            AND L."LandNo1" = M."LandNo1" 
                            AND L."LandNo2" = M."LandNo2" 
        WHERE NVL(CI."SettingDate",0) >= 20070701 -- 押品設定日期在９６０７０１之後才要報送 (ref:AS400 LN15M1) 
          AND M."LandNo1" + M."LandNo2" != 0
      ) T
      GROUP BY T."MainClActNo"
             , T."LandNo1"
             , T."LandNo2"
    ) 
    , "landData" AS ( 
      SELECT M."ClActNo"                           AS "MainClActNo"       -- 主要擔保品控制編碼 
           , M."LandNo1"                           AS "LandNo1"           -- 地號-前四碼 
           , M."LandNo2"                           AS "LandNo2"           -- 地號-後四碼 
           , MIN(L."LandSeq")                      AS "LandSeq" 
           , MAX(L."LandCode")                     AS "LandCode" 
           , MAX(L."LandZoningCode")               AS "LandZoningCode" 
           , MAX(L."PostedLandValue")              AS "PostedLandValue"  
           , MAX(L."PostedLandValueYearMonth")     AS "PostedLandValueYearMonth"  
      FROM "rawData" M 
      LEFT JOIN "ClImm" CI ON CI."ClCode1" = to_number(SUBSTR(M."ClActNo",1,1)) 
                          AND CI."ClCode2" = to_number(SUBSTR(M."ClActNo",2,2)) 
                          AND CI."ClNo" = to_number(SUBSTR(M."ClActNo",4,7)) 
      LEFT JOIN "ClFac" CF1 ON CF1."ClCode1" = to_number(SUBSTR(M."ClActNo",1,1)) 
                           AND CF1."ClCode2" = to_number(SUBSTR(M."ClActNo",2,2)) 
                           AND CF1."ClNo" = to_number(SUBSTR(M."ClActNo",4,7)) 
      LEFT JOIN "ClFac" CF2 ON CF2."CustNo" = CF1."CustNo" 
                           AND CF2."FacmNo" = CF1."FacmNo" 
                           AND CF2."MainFlag" = 'Y' 
      LEFT JOIN "ClLand" L ON L."ClCode1" = CASE 
                                              WHEN to_number(SUBSTR(M."ClActNo",1,1)) = 1 -- 房地擔保品 
                                              THEN CF2."ClCode1" 
                                            ELSE to_number(SUBSTR(M."ClActNo",1,1)) END 
                          AND L."ClCode2" = CASE 
                                              WHEN to_number(SUBSTR(M."ClActNo",1,1)) = 1 -- 房地擔保品 
                                              THEN CF2."ClCode2" 
                                            ELSE to_number(SUBSTR(M."ClActNo",2,2)) END 
                          AND L."ClNo" = CASE 
                                           WHEN to_number(SUBSTR(M."ClActNo",1,1)) = 1 -- 房地擔保品 
                                           THEN CF2."ClNo" 
                                         ELSE to_number(SUBSTR(M."ClActNo",4,7)) END 
                          AND L."LandNo1"     = M."LandNo1" 
                          AND L."LandNo2"     = M."LandNo2" 
      WHERE NVL(CI."SettingDate",0) >= 20070701        -- 押品設定日期在９６０７０１之後才要報送 (ref:AS400 LN15M1) 
        AND M."LandNo1" + M."LandNo2" > 0
      GROUP BY M."ClActNo"
             , M."LandNo1"
             , M."LandNo2"
    )
    , OnlyOneLandOwner AS (
      SELECT CF."ClCode1"
           , CF."ClCode2"
           , CF."ClNo"
           , L."LandNo1"
           , L."LandNo2"
           , C."CustId"  AS "OwnerId" 
           , ROW_NUMBER() 
             OVER ( 
              PARTITION BY CF."ClCode1"
                         , CF."ClCode2"
                         , CF."ClNo"
                         , L."LandNo1"
                         , L."LandNo2"
              ORDER BY C."CustId" -- 統編排序取第一個 
             ) AS "OwnerSeq" 
      FROM "ClFac" CF
      LEFT JOIN "ClFac" CF_ALL ON CF_ALL."CustNo" = CF."CustNo" 
                              AND CF_ALL."FacmNo" = CF."FacmNo" 
      LEFT JOIN "ClLand" L ON L."ClCode1" = CF_ALL."ClCode1"
                          AND L."ClCode2" = CF_ALL."ClCode2"
                          AND L."ClNo" = CF_ALL."ClNo"
      LEFT JOIN "ClLandOwner" O ON O."ClCode1" = CF_ALL."ClCode1"
                               AND O."ClCode2" = CF_ALL."ClCode2"
                               AND O."ClNo" = CF_ALL."ClNo"
      LEFT JOIN "CustMain" C  ON  C."CustUKey"  = O."OwnerCustUKey" 
      WHERE NVL(C."CustId",' ') != ' '
      -- 一個擔保品 && 一個地號 只取一個擔保品提供人 
    )
    , "Work_B096" AS ( 
      SELECT M."ClActNo"                    AS "MainClActNo"       -- 主要擔保品控制編碼 
           , NVL(OnlyOneLandOwner."OwnerId",' ')
                                            AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN 
           , M."CityJCICCode"               AS "CityCode"          -- 縣市別 
           , M."AreaJCICCode"               AS "AreaCode"          -- 鄉鎮市區別 
           , M."IrCode"                     AS "IrCode"            -- 段、小段號 
           , M."LandNo1"                    AS "LandNo1"           -- 地號-前四碼 
           , M."LandNo2"                    AS "LandNo2"           -- 地號-後四碼 
           , NVL(sumData."Area",0)          AS "Area"              -- 面積 
           , landData."LandSeq"             AS "LandSeq" 
           , landData."LandCode"            AS "LandCode" 
           , landData."LandZoningCode"      AS "LandZoningCode" 
           , landData."PostedLandValue"     AS "PostedLandValue" 
           , landData."PostedLandValueYearMonth"
                                            AS "PostedLandValueYearMonth" 
      FROM "JcicB092" M   
      LEFT JOIN "ClImm" CI ON CI."ClCode1"    = to_number(SUBSTR(M."ClActNo",1,1)) 
                          AND CI."ClCode2"    = to_number(SUBSTR(M."ClActNo",2,2)) 
                          AND CI."ClNo"       = to_number(SUBSTR(M."ClActNo",4,7)) 
      LEFT JOIN OnlyOneLandOwner ON OnlyOneLandOwner."ClCode1" = to_number(SUBSTR(M."ClActNo",1,1)) 
                                AND OnlyOneLandOwner."ClCode2" = to_number(SUBSTR(M."ClActNo",2,2)) 
                                AND OnlyOneLandOwner."ClNo"    = to_number(SUBSTR(M."ClActNo",4,7)) 
                                AND OnlyOneLandOwner."LandNo1" = M."LandNo1" 
                                AND OnlyOneLandOwner."LandNo2" = M."LandNo2"
                                AND OnlyOneLandOwner."OwnerSeq" = 1 
      LEFT JOIN "sumData" sumData ON sumData."MainClActNo" = M."ClActNo" 
                                 AND sumData."LandNo1" = M."LandNo1" 
                                 AND sumData."LandNo2" = M."LandNo2" 
      LEFT JOIN "landData" landData ON landData."MainClActNo" = M."ClActNo" 
                                   AND landData."LandNo1" = M."LandNo1" 
                                   AND landData."LandNo2" = M."LandNo2" 
      WHERE M."DataYM" = YYYYMM 
        AND NVL(CI."SettingDate",0) >= 20070701 -- 押品設定日期在９６０７０１之後才要報送 (ref:AS400 LN15M1) 
        AND M."LandNo1" + M."LandNo2" > 0
    ) 
    SELECT DISTINCT 
           YYYYMM                                AS "DataYM"            -- 資料年月                      DECIMAL   6 
         , '96'                                  AS "DataType"          -- 資料別                        VARCHAR2  2 
         , '458'                                 AS "BankItem"          -- 總行代號                      VARCHAR2  3 
         , '0001'                                AS "BranchItem"        -- 分行代號                      VARCHAR2  4 
         , ' '                                   AS "Filler4"           -- 空白                          VARCHAR2  2 
         , WK."MainClActNo"                      AS "ClActNo"           -- 擔保品控制編碼 (主要擔保品)   VARCHAR2  50 
         , 1                                     AS "LandSeq"           -- 土地序號                      DECIMAL   3 
         , UPPER(WK."OwnerId")                   AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN 
         , WK."CityCode"                         AS "CityCode"          -- 縣市別 
         , WK."AreaCode"                         AS "AreaCode"          -- 鄉鎮市區別 
         , WK."IrCode"                           AS "IrCode"            -- 段、小段號 
         , WK."LandNo1"                          AS "LandNo1"           -- 地號-前四碼 
         , WK."LandNo2"                          AS "LandNo2"           -- 地號-後四碼 
         , CASE 
             WHEN TRIM(NVL(WK."LandCode",'')) = '' THEN 'E'  -- 空白 
             WHEN WK."LandCode" IN ('01')          THEN 'C'  -- 建 
             WHEN WK."LandCode" IN ('02')          THEN 'A'  -- 田 
             WHEN WK."LandCode" IN ('03')          THEN 'B'  -- 旱 
             WHEN WK."LandCode" IN ('05')          THEN 'D'  -- 道 
             WHEN WK."LandCode" IN ('04','06','07','08','09','10') THEN 'Z'  -- 其他 
             ELSE 'E'                                                       -- 空白 
           END                                   AS "LandCode"          -- 地目  (ref:AS400 LN15M1)      VARCHAR2  1 
         , CASE 
             WHEN NVL(WK."Area",0) = 0 THEN 0.01 
             ELSE TRUNC(NVL(WK."Area",0)  / 0.3025, 2) 
           END                                   AS "Area"              -- 面積                          DECIMAL   10 
--L."LandZoningCode" , 土地使用區分 , VARCHAR2 , 2 , 共用代碼檔 
-- 01: 特定農業區02: 一般農業區03: 鄉村區04: 工業區05: 森林區06: 山坡地保育區07: 風景區08: 特定專用區09: 國家公園區 
-- 10: 住宅區11: 商業區12: 行政區13: 工業區14: 文教區15: 農業區16: 風景區17: 保護區18: 水岸發展區19: 漁業區 
-- 20: 倉儲區21: 保存區22: 葬儀業區23: 特定專用區24: 其他分區25: 道路26: 公園27: 綠地28: 廣場29: 兒童遊樂場 
-- 30: 民用航空站31: 停車場32: 河道33: 港埠34: 學校35: 社教機構36: 體育場37: 市場38: 醫療衛生機構39: 機關 
-- 40: 公用事業41: 綠帶42: 加油站43: 其他公共設施44: 道路保留地45: 公園保留地46: 綠地保留地47: 廣場保留地48: 兒童樂園場保留地49: 民用航空站保留地 
-- 50: 停車場保留地51: 河道保留地52: 港埠保留地53: 學校保留地54: 社教機構保留地55: 體育場保留地56: 市場保留地57: 醫療衛生機構保留地58: 機關保留地59: 公用事業保留地 
-- 60: 加油站保留地61: 其他保留地 
         , CASE 
             WHEN WK."LandZoningCode" IS NULL                  THEN 'Z' --其他 
--             WHEN WK."LandZoningCode" IN ('1')                 THEN 'A' --住宅區 
--             WHEN WK."LandZoningCode" IN ('2')                 THEN 'B' --商業區 
--             WHEN WK."LandZoningCode" IN ('4')                 THEN 'Z' --其他 
             WHEN WK."LandZoningCode" IN ('5','6','7','8')     THEN 'R' --鄉村區 
             WHEN WK."LandZoningCode" IN ('10')                THEN 'A' --住宅區 
             WHEN WK."LandZoningCode" IN ('11')                THEN 'B' --商業區 
             WHEN WK."LandZoningCode" IN ('24')                THEN 'Z' --其他 
             WHEN WK."LandZoningCode" IN ('03')                THEN 'R' --鄉村區 
             ELSE 'Z'   --其他 
           END                                   AS "LandZoningCode"    -- 使用分區   VARCHAR2  1   -- (ref:LN15M1 (#M3760 98)) 
         , CASE 
             WHEN WK."LandZoningCode" IS NULL             THEN 'Z' 
--             WHEN WK."LandZoningCode" IN ('1')            THEN 'AZ' 
--             WHEN WK."LandZoningCode" IN ('2')            THEN 'BZ' 
             WHEN WK."LandZoningCode" IN ('10')           THEN 'AZ' 
             WHEN WK."LandZoningCode" IN ('11')           THEN 'BZ' 
             ELSE 'Z' 
           END                                   AS "LandUsageType"     -- 使用地類別  都市土地 VARCHAR2  2  -- (ref:LN15M1 (#M3760 99)) 
         , CASE 
             WHEN ROUND( NVL(WK."PostedLandValue",0) / 1000, 0) = 0 THEN 10 -- (ref:AS400 LN15M1) 
             ELSE ROUND( NVL(WK."PostedLandValue",0) / 1000, 0) 
           END                                   AS "PostedLandValue"   -- 公告土地現值                  DECIMAL   10 
         , CASE 
             WHEN NVL(WK."PostedLandValueYearMonth",0) = 0 THEN 9607   -- (ref:AS400 LN15M1) 
             WHEN NVL(WK."PostedLandValueYearMonth",0) < 191100 THEN NVL(WK."PostedLandValueYearMonth",0) 
             ELSE WK."PostedLandValueYearMonth" - 191100 
           END                                   AS "PostedLandValueYearMonth" -- 公告土地現值年月              DECIMAL   5 
         , ' '                                   AS "Filler18"           -- 空白                          VARCHAR2  30 
         , YYYYMM - 191100                       AS "JcicDataYM"         -- 資料所屬年月                  DECIMAL   5 
         , JOB_START_TIME                        AS "CreateDate"         -- 建檔日期時間 
         , EmpNo                                 AS "CreateEmpNo"        -- 建檔人員 
         , JOB_START_TIME                        AS "LastUpdate"         -- 最後更新日期時間 
         , EmpNo                                 AS "LastUpdateEmpNo"    -- 最後更新人員 
    FROM "Work_B096" WK
    ;

    INS_CNT := INS_CNT + sql%rowcount; 
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB096 END: INS_CNT=' || INS_CNT); 

    -- 記錄程式結束時間 
    JOB_END_TIME := SYSTIMESTAMP; 
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' || 
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss') 
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss') 
                  ) * 86400       || ' Secs' ); 

    commit; 

  END; 
END;