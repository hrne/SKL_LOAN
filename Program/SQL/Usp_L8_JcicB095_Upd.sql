--------------------------------------------------------
--  DDL for Procedure Usp_L8_JcicB095_Upd
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L8_JcicB095_Upd" 
(
-- 程式功能：維護 JcicB095 每月聯徵不動產擔保品明細-建號附加檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB095_Upd"(20200430,'999999');
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
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB095');

    DELETE FROM "JcicB095"
    WHERE "DataYM" = YYYYMM
      ;


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB095');
    INS_CNT := 0;

    INSERT INTO "JcicB095"
    WITH rawData AS (
    SELECT DISTINCT
           M."ClActNo"                           AS "MainClActNo"       -- 主要擔保品控制編碼
         , M."BdNo1"                             AS "BdNo1"             -- 建號-前五碼
         , M."BdNo2"                             AS "BdNo2"             -- 建號-後三碼
    FROM   "JcicB092" M
    WHERE  M."DataYM" =  YYYYMM
      AND  ( M."BdNo1" > 0 OR M."BdNo2" > 0 )
      GROUP BY M."ClActNo" , M."BdNo1" , M."BdNo2"            
    )
    , "Work_B095_Sum" AS (
    SELECT 
           M."MainClActNo"                           AS "MainClActNo"       -- 主要擔保品控制編碼
         , M."BdNo1"                             AS "BdNo1"             -- 建號-前五碼
         , M."BdNo2"                             AS "BdNo2"             -- 建號-後三碼
         , SUM(CB."Area")                        AS "Area"              -- 公設面積
    FROM  rawData M
      LEFT JOIN "ClImm"  CI    ON CI."ClCode1"    = to_number(SUBSTR(M."MainClActNo",1,1))
                              AND CI."ClCode2"    = to_number(SUBSTR(M."MainClActNo",2,2))
                              AND CI."ClNo"       = to_number(SUBSTR(M."MainClActNo",4,7))
      LEFT JOIN "ClBuildingPublic" CB ON CB."ClCode1" = CI."ClCode1"
                                     AND CB."ClCode2" = CI."ClCode2"
                                     AND CB."ClNo"    = CI."ClNo"
    WHERE  NVL(CI."SettingDate",0) >= 20070701        -- 押品設定日期在９６０７０１之後才要報送 (ref:AS400 LN15M1)
      GROUP BY M."MainClActNo" , M."BdNo1" , M."BdNo2"            
    )
    , "Work_B095" AS (
    SELECT 
           M."ClActNo"                           AS "MainClActNo"       -- 主要擔保品控制編碼
         , M."OwnerId"                           AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN
         , M."CityJCICCode"                      AS "CityCode"          -- 縣市別
         , M."AreaJCICCode"                      AS "AreaCode"          -- 鄉鎮市區別
         , M."IrCode"                            AS "IrCode"            -- 段、小段號
         , M."BdNo1"                             AS "BdNo1"             -- 建號-前五碼
         , M."BdNo2"                             AS "BdNo2"             -- 建號-後三碼
         , WK."Area"                             AS "Area"              -- 公設面積
    FROM   "JcicB092" M
      LEFT JOIN "ClImm"  CI    ON CI."ClCode1"    = to_number(SUBSTR(M."ClActNo",1,1))
                              AND CI."ClCode2"    = to_number(SUBSTR(M."ClActNo",2,2))
                              AND CI."ClNo"       = to_number(SUBSTR(M."ClActNo",4,7))
      LEFT JOIN "Work_B095_Sum" WK ON WK."MainClActNo" = M."ClActNo"
                                     AND WK."BdNo1" = M."BdNo1"
                                     AND WK."BdNo2" = M."BdNo2"
    WHERE  M."DataYM" =  YYYYMM
      AND  NVL(CI."SettingDate",0) >= 20070701        -- 押品設定日期在９６０７０１之後才要報送 (ref:AS400 LN15M1)
      AND  ( M."BdNo1" > 0 OR M."BdNo2" > 0 )

    )
    SELECT DISTINCT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '95'                                  AS "DataType"          -- 資料別
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , ' '                                   AS "Filler4"           -- 空白
         , WK."MainClActNo"                      AS "ClActNo"           -- 擔保品控制編碼 (主要擔保品)   VARCHAR2 50
         , WK."OwnerId"                          AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN
         , WK."CityCode"                         AS "CityCode"           -- 縣市別
         , WK."AreaCode"                         AS "AreaCode"           -- 鄉鎮市區別
         , WK."IrCode"                           AS "IrCode"            -- 段、小段號
         , WK."BdNo1"                            AS "BdNo1"             -- 建號-前五碼
         , WK."BdNo2"                            AS "BdNo2"             -- 建號-後三碼
         , NVL("CdCity"."CityItem", ' ')         AS "CityName"          -- 縣市名稱
         , NVL("CdArea"."AreaItem", ' ')         AS "AreaName"          -- 鄉鎮市區名稱
         , SUBSTRB(NVL(
             TRIM(NVL2(B."Road",      B."Road",              ''))  ||
             TRIM(NVL2(B."Section",   B."Section" || '段',   ''))  ||
             TRIM(NVL2(B."Alley",     B."Alley" || '巷',     ''))  ||
             TRIM(NVL2(B."Lane",      B."Lane" || '弄',      ''))  ||
             TRIM(NVL(B."Num", ''))  ||
             ( CASE
                 WHEN TRIM(NVL(B."NumDash", '')) IS NOT NULL THEN TRIM(NVL2(B."NumDash", '之' || B."NumDash", ''))
                 ELSE u''
               END) ||
             ( CASE
                 WHEN TRIM(NVL(B."Num", '')) IS NULL AND TRIM(NVL(B."NumDash", '')) IS NULL THEN ''
                 ELSE '號'
               END) ||
             TRIM(NVL2(B."Floor",     B."Floor" || '樓',     ''))  ||
             TRIM(NVL2(B."FloorDash", '之' || B."FloorDash", ''))
           , ' '), 1, 228)
                                                 AS "Addr"              -- 村里/街路/段/巷/弄/號/樓      VARCHAR2 76
         , CASE
             WHEN B."BdMainUseCode" IS NULL         THEN 'Z'    -- 其他      -- 本欄參考 AS400 LN15M1 對照規則 及 新系統轉檔規則
             WHEN B."BdMainUseCode" IN ('01')       THEN 'A'    -- 住家用
             WHEN B."BdMainUseCode" IN ('02')       THEN 'B'    -- 商業用
             WHEN B."BdMainUseCode" IN ('03')       THEN 'C'    -- 工業用
             WHEN B."BdMainUseCode" IN ('05')       THEN 'G'    -- 農舍
             WHEN B."BdMainUseCode" IN ('06')       THEN 'D'    -- 住商用
             WHEN B."BdMainUseCode" IN ('07')       THEN 'E'    -- 住工用
             WHEN B."BdMainUseCode" IN ('08')       THEN 'F'    -- 工商用
             ELSE 'Z'                                           -- 其他
           END                                   AS "BdMainUseCode"     -- 主要用途                      VARCHAR2 1
         , CASE
             WHEN B."BdMtrlCode" IS NULL              THEN 'Z'    -- 本欄參考 AS400 LN15M1 對照規則
             WHEN B."BdMtrlCode" IN ('01','04','18')  THEN 'D'
             WHEN B."BdMtrlCode" IN ('02')            THEN 'E'
             WHEN B."BdMtrlCode" IN ('03')            THEN 'H'
             WHEN B."BdMtrlCode" IN ('09','12')       THEN 'C'
             WHEN B."BdMtrlCode" IN ('11')            THEN 'A'
             WHEN B."BdMtrlCode" IN ('99')            THEN 'Z'
             ELSE 'Z'
           END                                   AS "BdMtrlCode"        -- 主要建材(結構體)              VARCHAR2 1
--       , CASE
--           WHEN B."BdSubUsageCode" IS NULL         THEN 'Z'     -- 本欄參考 AS400 LN15M1 對照規則
--           WHEN B."BdSubUsageCode" IN ('01')       THEN 'A'
--           WHEN B."BdSubUsageCode" IN ('02')       THEN 'A'
--           WHEN B."BdSubUsageCode" IN ('03')       THEN 'B'
--           WHEN B."BdSubUsageCode" IN ('04')       THEN 'Z'
--           ELSE 'Z'
--         END                                   AS "BdSubUsageCode"    -- 附屬建物用途 (後面再處理)     VARCHAR2 6
         , ' '                                   AS "BdSubUsageCode"    -- 附屬建物用途 (後面再處理)     VARCHAR2 6
         , CASE
             WHEN NVL(B."TotalFloor", 0) = 0 THEN 999
             ELSE B."TotalFloor"
           END                                   AS "TotalFloor"        -- 層數(標的所在樓高)            DECIMAL  3
         , CASE
             WHEN NVL(B."TotalFloor", 0) = 0 AND trim(NVL(B."FloorNo",' ')) IS NULL THEN '999    ' --透天厝
             WHEN NVL(B."TotalFloor", 0) = 0 AND trim(NVL(B."FloorNo",' '))  = '0'  THEN '999    ' --透天厝
             WHEN trim(NVL(B."FloorNo",' ')) IS NULL  THEN '1      '
             ELSE B."FloorNo"
           END                                   AS "FloorNo"           -- 層次(標的所在樓層)            VARCHAR2 7
         , CASE
             WHEN B."BdDate" IS NULL    THEN '0880109'  -- (erf:AS400 LN15M1)
             WHEN B."BdDate" = 0        THEN '0880109'
             WHEN B."BdDate" < 19810101 THEN '070' || LPAD(LTRIM(TO_CHAR(MOD( B."BdDate" , 10000))),4,'0') -- 小於70/01/01則報70年+原建物完成月日
             WHEN B."BdDate" < 19110000 THEN LPAD(B."BdDate",7,'0')
             ELSE LPAD(B."BdDate" - 19110000, 7, '0')
           END                                   AS "BdDate"            -- 建築完成日期(屋齡)            VARCHAR2 7
         , 0                                     AS "TotalArea"         -- 建物總面積 (後面再處理)       DECIMAL  10,2
         , TRUNC(NVL(B."FloorArea",0)  / 0.3025, 2)
                                                 AS "FloorArea"         -- 主建物(層次)面積 (後面再處理) DECIMAL  10,2
         , TRUNC(NVL(B."BdSubArea",0)  / 0.3025, 2)
                                                 AS "BdSubArea"         -- 附屬建物面積 (後面再處理)     DECIMAL  10,2
         , TRUNC(NVL(WK."Area",0) / 0.3025, 2)
                                                 AS "PublicArea"        -- 共同部份持分面積 (後面再處理) DECIMAL  10,2
         , ' '                                   AS "Filler33"          -- 空白                          VARCHAR2 44
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月                  DECIMAL  5
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Work_B095" WK
      LEFT JOIN "CdArea"       ON "CdArea"."JcicCityCode"  = WK."CityCode"
                              AND "CdArea"."JcicAreaCode"  = WK."AreaCode"
      LEFT JOIN "CdCity"       ON "CdCity"."CityCode"  =  "CdArea"."CityCode"  
      LEFT JOIN "ClBuilding"  B     ON B."ClCode1" = to_number(SUBSTR(WK."MainClActNo",1,1))
                                   AND B."ClCode2" = to_number(SUBSTR(WK."MainClActNo",2,2))
                                   AND B."ClNo"    = to_number(SUBSTR(WK."MainClActNo",4,7))
    ;


    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB095 END: INS_CNT=' || INS_CNT);


-- 轉全形
    DBMS_OUTPUT.PUT_LINE('UPDATE 轉全形 ');

    UPDATE "JcicB095" M
    SET   M."Addr"   = SUBSTRB(TO_MULTI_BYTE(TRIM(M."Addr")), 1, 228)
    WHERE M."DataYM" = YYYYMM
      ;

    DBMS_OUTPUT.PUT_LINE('UPDATE END 轉全形 ');


-- if BdSubArea < PublicArea then set BdSubArea = PublicArea
    DBMS_OUTPUT.PUT_LINE('UPDATE BdSubArea < PublicArea ');

    UPDATE "JcicB095" M
    SET   M."BdSubArea"   =  M."PublicArea"
    WHERE M."DataYM" = YYYYMM
      AND M."BdSubArea" < M."PublicArea"
      ;

    DBMS_OUTPUT.PUT_LINE('UPDATE BdSubArea < PublicArea END ');


-- if FloorArea < BdSubArea then set FloorArea = BdSubArea, BdSubArea = FloorArea
    DBMS_OUTPUT.PUT_LINE('UPDATE FloorArea < BdSubArea ');

    UPDATE "JcicB095" M
    SET   M."FloorArea"   =  M."BdSubArea"
        , M."BdSubArea"   =  M."FloorArea"
    WHERE M."DataYM" = YYYYMM
      AND M."FloorArea" < M."BdSubArea"
      ;

    DBMS_OUTPUT.PUT_LINE('UPDATE FloorArea < BdSubArea END ');


-- BdSubUsageCode -- 附屬建物用途  (ref:AS400 LN15M1 (#M375A 189))
    DBMS_OUTPUT.PUT_LINE('UPDATE BdSubUsageCode ');

    UPDATE "JcicB095" M
    SET  M."BdSubUsageCode" = CASE WHEN NVL(M."BdSubArea",0) = 0 THEN 'Z'
                                   ELSE 'A'
                              END
    WHERE M."DataYM" = YYYYMM
      ;

    DBMS_OUTPUT.PUT_LINE('UPDATE BdSubUsageCode END ');


-- TotalArea & FloorArea = 0 -- 建物總面積, 主建物(層次)面積=0 的處理  (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE TotalArea, FloorArea = 0');

    UPDATE "JcicB095" M
    SET  M."TotalArea" = CASE WHEN ( M."FloorArea" + M."BdSubArea" + M."PublicArea" ) = 0 THEN 0.01
                              ELSE ( M."FloorArea" + M."BdSubArea" + M."PublicArea" )
                         END
       , M."FloorArea" = CASE WHEN M."FloorArea" = 0 THEN 0.01
                              ELSE M."FloorArea"
                         END
    WHERE M."DataYM" = YYYYMM
      ;

    DBMS_OUTPUT.PUT_LINE('UPDATE TotalArea, FloorArea = 0  END ');


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
