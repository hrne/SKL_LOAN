--------------------------------------------------------
--  DDL for Procedure Usp_L8_JcicB092_Upd
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L8_JcicB092_Upd" 
(
-- 程式功能：維護 JcicB092 每月聯徵不動產擔保品明細檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB092_Upd"(20211230,'999999');
--

    -- 參數
    TBSDYF         IN  INT,        -- 系統營業日(西元)
    EmpNo          IN  VARCHAR2    -- 經辦
)
AUTHID CURRENT_USER
AS
BEGIN
 "Usp_L8_JcicB092_Upd_Prear"();
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


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT Work_B092');
    INS_CNT := 0;

    INSERT INTO "Work_B092"
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
                          , NVL(CB."CityCode", ' ')
                          , NVL(CB."AreaCode", ' ')
                          , NVL(CB."BdNo1", '00000')
                          , NVL(CB."BdNo2", '000')
               ORDER BY CASE
                          WHEN F."MainFlag" = 'Y'
                          THEN 0
                        ELSE 1 END -- 主要擔保品排在第一筆
                      , F."ClCode1"
                      , F."ClCode2"
                      , F."ClNo"
             ) AS "ClBdNoSeq"
      FROM   "JcicB080" M
          LEFT JOIN "ClFac" F    ON F."CustNo"   = to_number(SUBSTR(M."FacmNo",1,7))
                                AND F."FacmNo"   = to_number(SUBSTR(M."FacmNo",8,3))
          LEFT JOIN "ClMain" CM  ON CM."ClCode1"  = F."ClCode1"
                                AND CM."ClCode2"  = F."ClCode2"
                                AND CM."ClNo"     = F."ClNo"
          LEFT JOIN "ClBuilding" CB ON CB."ClCode1"  = F."ClCode1"
                                   AND CB."ClCode2"  = F."ClCode2"
                                   AND CB."ClNo"     = F."ClNo"
                                   AND F."ClCode1" = 1
      WHERE  M."DataYM"   =   YYYYMM
        AND  M."FacmNo"   IS  NOT NULL
        AND  F."ClNo"     IS  NOT NULL
      ORDER BY M."CustId", M."FacmNo", F."ClCode1", F."ClCode2", F."ClNo"
    )
    , ClcountData AS ( --含未抵押建號資料
      SELECT r."FacmNo"                          AS "FacmNo"            -- 額度控制編碼
           , COUNT(*)                            AS "ClCount"           -- 擔保品筆數
      FROM rawData r
      WHERE CASE
--              WHEN r."ClCode1" != 1 -- 非房地擔保品
--              THEN 1
              WHEN r."ClCode1" = 1 -- 房地擔保品
                   AND r."ClBdNoSeq" = 1 -- 建號相同時只取一筆
              THEN 1
              ELSE 0 END = 1
      GROUP BY r."FacmNo"
    )
    , ClLandRawData AS (
      SELECT r."FacmNo" -- 額度控制編碼
           , TO_NUMBER(CL."LandNo1") AS "LandNo1" -- 地號
           , TO_NUMBER(CL."LandNo2") AS "LandNo2"-- 地號(子號)
           , CL."Area" -- 土地面積加總
           , CL."LVITax" -- 土地增值稅加總
           , CL."CityCode" -- 縣市別
           , CL."AreaCode" -- 鄉鎮市區別
           , CL."LVITaxYearMonth" -- 應計土地增值稅之預估年月
           , CL."IrCode" -- 段小段
      FROM rawData r
      LEFT JOIN "ClLand" CL ON CL."ClCode1" = r."ClCode1" 
                           AND CL."ClCode2" = r."ClCode2"
                           AND CL."ClNo" = r."ClNo"
      WHERE CASE WHEN NVL(TO_NUMBER(CL."LandNo1"), 0) > 0 THEN 1
                 WHEN NVL(TO_NUMBER(CL."LandNo2"), 0) > 0 THEN 1
                 ELSE 0 END = 1
    )
    , ClLandData AS (
      SELECT r."FacmNo"                          AS "FacmNo"            -- 額度控制編碼
           , r."LandNo1"                         AS "LandNo1"           -- 地號
           , r."LandNo2"                         AS "LandNo2"           -- 地號(子號)
           , SUM(r."Area")                       AS "ClAreaCount"       -- 土地面積加總
           , SUM(r."LVITax")                     AS "LVITax"            -- 土地增值稅加總
           , MAX(r."CityCode")                   AS "CityCode"          -- 縣市別
           , MAX(r."AreaCode")                   AS "AreaCode"          -- 鄉鎮市區別
           , MAX(r."LVITaxYearMonth")            AS "LVITaxYearMonth"   -- 應計土地增值稅之預估年月
           , MAX(r."IrCode")                     AS "IrCode"            -- 段小段
      FROM ClLandRawData r
      GROUP BY r."FacmNo" 
             , r."LandNo1"
             , r."LandNo2"
    )
    , LineAmtData AS (
      SELECT CF."ClCode1"
            ,CF."ClCode2"
            ,CF."ClNo"
            ,CF."CustNo"
            ,F."LineAmt"
            ,ROW_NUMBER()
             OVER (
               PARTITION BY CF."ClCode1"
                          , CF."ClCode2"
                          , CF."ClNo"
                          , CF."CustNo"
               ORDER BY CF."FacmNo" desc
             ) AS "FacmNoSeq"
      FROM "ClFac" CF
      LEFT JOIN "FacMain" F ON F."CustNo" = CF."CustNo"
                           AND F."FacmNo" = CF."FacmNo"
      WHERE CF."CustNo" != 0
--        AND CF."MainFlag" = 'Y'
    )
    -- 2022-05-24 Wei 新增此段 目的: 該戶號同擔保品的額度加總
    -- 先找出有同擔保品的額度資料
    , sameClRawData AS (
        SELECT "CustNo"
             , "ClCode1"
             , "ClCode2"
             , "ClNo"
        FROM "ClFac"
        GROUP BY "CustNo"
               , "ClCode1"
               , "ClCode2"
               , "ClNo"
        HAVING COUNT(*) >= 2
    )
    , sameClFacmNoData AS (
        SELECT DISTINCT
               S."CustNo"
             , CF."FacmNo"
        FROM sameClRawData S
        LEFT JOIN "ClFac" CF ON CF."CustNo" = S."CustNo"
                            AND CF."ClCode1" = S."ClCode1"
                            AND CF."ClCode2" = S."ClCode2"
                            AND CF."ClNo" = S."ClNo"
        LEFT JOIN "JcicB090" J ON to_number(SUBSTR(J."FacmNo",1,7)) = S."CustNo"
                              AND to_number(SUBSTR(J."FacmNo",8,3)) = CF."FacmNo"
        WHERE J."FacmNo" IS NOT NULL --2022/05/26 加總時只考慮B090有的資料
    )
    , sameClLineAmtData AS (
      SELECT S."CustNo"
           , SUM(F."LineAmt") AS "SameClLineAmt"
      FROM sameClFacmNoData S
      LEFT JOIN "FacMain" F ON F."CustNo" = S."CustNo"
                           AND F."FacmNo" = S."FacmNo"
      GROUP BY S."CustNo"
    )
    SELECT  -- count(*) as "Count"
           M."FacmNo"                            AS "FacmNo"
         , M."ClActNo"                           AS "MainClActNo"    -- 主要擔保品控制編碼
         , CM."ClCode1"                          AS "ClCode1"        -- 擔保品代號1
         , CM."ClCode2"                          AS "ClCode2"        -- 擔保品代號2
         , CM."ClNo"                             AS "ClNo"           -- 擔保品編號
         , NVL(CD2."ClTypeJCIC",' ')             AS "ClTypeJCIC"     -- 擔保品類別
         , CASE
             WHEN BuildingOwner."OwnerId"  IS NOT NULL THEN BuildingOwner."OwnerId"
             WHEN LandOwner."OwnerId"      IS NOT NULL THEN LandOwner."OwnerId"
             WHEN BuPublicOwner."OwnerId"  IS NOT NULL THEN BuPublicOwner."OwnerId"
             ELSE ' '
           END                                   AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN
         , SUBSTR('00000000' || TRUNC(CASE
                                        WHEN NVL(CM."EvaAmt",0) = 0 
                                        THEN NVL(SCLAD."SameClLineAmt",0)
                                      ELSE NVL(CM."EvaAmt",0) END / 1000), -8)
                                                 AS "EvaAmt"            -- 鑑估(總市)值
         , CASE
             WHEN TRUNC(NVL(CM."EvaDate",0) / 100) < 191100 THEN TRUNC(NVL(CM."EvaDate",0) / 100)
             ELSE TRUNC(NVL(CM."EvaDate",0) / 100) - 191100
           END                                   AS "EvaDate"           -- 鑑估日期
         , CASE
             WHEN TRUNC(NVL(CI."SettingDate",0) / 100) < 191100 THEN TRUNC(NVL(CI."SettingDate",0) / 100)
             ELSE TRUNC(NVL(CI."SettingDate",0) / 100) - 191100
           END                                   AS "SettingDate"       -- 設定日期
         , CASE
             WHEN TRUNC(NVL(CI."SettingDate",0) / 100) = YYYYMM
               THEN SUBSTR('00000000' || TRUNC(NVL(CI."SettingAmt",0) / 1000), -8)
             ELSE '00000000'
           END                                   AS "MonthSettingAmt"   -- 本行本月設定金額
         , CASE
             WHEN to_number(NVL(CI."SettingSeq",0)) = 0 THEN 1
             ELSE to_number(NVL(CI."SettingSeq",0))
           END                                   AS "SettingSeq"        -- 本月設定抵押順位
         , SUBSTR('00000000' || TRUNC(NVL(CI."OtherOwnerTotal",0) / 1000), -8)
                                                 AS "PreSettingAmt"     -- 其他債權人已設定金額
         , SUBSTR('00000000' || TRUNC(NVL(CM."DispPrice",0) / 1000), -8)
                                                 AS "DispPrice"         -- 處分價格
         , CASE
             WHEN NVL(CI."ClaimDate",0) > 29000000 THEN 99912
             WHEN TRUNC(NVL(CI."ClaimDate",0) / 100) < 191100 THEN TRUNC(NVL(CI."ClaimDate",0) / 100)
             ELSE TRUNC(NVL(CI."ClaimDate",0) / 100) - 191100
           END                                   AS "IssueEndDate"      -- 權利到期年月
--         , NVL("CdCity"."JcicCityCode", ' ')     AS "CityCode"
         , NVL("CdArea"."JcicCityCode", ' ')     AS "CityCode"
--         , NVL(TRIM( NVL(L."AreaCode",CLL."AreaCode")), '00')         AS "AreaCode"
         , NVL("CdArea"."JcicAreaCode", '00')     AS "AreaCode"
         , CASE
--           WHEN "ClBuilding"."IrCode" IS NOT NULL THEN SUBSTR('0000' || "ClBuilding"."IrCode", -4)
--             WHEN L."IrCode" IS NOT NULL THEN SUBSTR('0000' || L."IrCode", -4)
--             WHEN NVL(CLL."LandNo1", 0) = NVL(L."LandNo1", 0) 
--                  AND NVL(CLL."LandNo2", 0) = NVL(L."LandNo2", 0)
--             THEN NVL(TRIM( NVL(L."IrCode",CLL."IrCode")), '0001') 
--             WHEN  NVL(L."LandNo1", 0)  = 0 AND NVL(L."LandNo2", 0) = 0
--             THEN NVL(TRIM( NVL(L."IrCode",CLL."IrCode")), '0001')
             WHEN NVL(CLL."LandNo1", 0)
                  + NVL(CLL."LandNo2", 0) != 0
             THEN NVL(TRIM(CLL."IrCode"), '0001')
             ELSE '0001'
           END                                     AS "IrCode"            -- 段、小段號
--         , NVL(L."LandNo1", 0)                   AS "LandNo1"           -- 地號-前四碼
--         , NVL(L."LandNo2", 0)                   AS "LandNo2"           -- 地號-後四碼
         , NVL(CLL."LandNo1", 0)        AS "LandNo1"           -- 地號-前四碼
         , NVL(CLL."LandNo2", 0)        AS "LandNo2"           -- 地號-後四碼
         , NVL(B."BdNo1", 0)            AS "BdNo1"             -- 建號-前五碼
         , NVL(B."BdNo2", 0)            AS "BdNo2"             -- 建號-後三碼
         , NVL("CdArea"."Zip3",' ')                AS "Zip"               -- 郵遞區號
--         , TRUNC(NVL(L."LVITax",'0') / 1000)     AS "LVITax"            -- 擔保品預估應計土地增值稅合計
         , CASE  
--             WHEN NVL(CLL."LandNo1", 0) = NVL(L."LandNo1", 0) 
--                  AND NVL(CLL."LandNo2", 0) = NVL(L."LandNo2", 0)
--             THEN TRUNC( NVL(CLL."LVITax", 0)  / 1000  )
--             WHEN  NVL(L."LandNo1", 0)  = 0
--                  AND NVL(L."LandNo2", 0) = 0
--             THEN TRUNC( NVL(CLL."LVITax", 0)  / 1000  )
             WHEN NVL(CLL."LandNo1", 0)
                  + NVL(CLL."LandNo2", 0) != 0
             THEN TRUNC( NVL(CLL."LVITax", 0)  / 1000  )
             ELSE 0     
           END                                   AS "LVITax"            -- 擔保品預估應計土地增值稅合計
         , CASE
             WHEN NVL(CLL."LVITaxYearMonth",0) < 191100 THEN NVL(CLL."LVITaxYearMonth",0)
             ELSE NVL(CLL."LVITaxYearMonth",0) - 191100
           END                                   AS "LVITaxYearMonth"   -- 應計土地增值稅之預估年月  (後面再額外判斷 '日期')
         , CASE
             WHEN TRUNC(NVL(B."ContractPrice",0) / 1000, 0) = 0 THEN '        '
             ELSE SUBSTR('00000000' || TRUNC(NVL(B."ContractPrice",0) / 1000), -8)
           END                                   AS "ContractPrice"     -- 買賣契約價格
         , CASE
             WHEN NVL(B."ContractDate",0) = 0 THEN '       '
             WHEN B."ContractDate" < 19110000 THEN SUBSTR('0000000' || B."ContractDate" , -7)
             ELSE SUBSTR('0000000' || (B."ContractDate" - 19110000) , -7)
           END                                   AS "ContractDate"      -- 買賣契約日期
         , CASE
             WHEN  NVL(CD2."ClTypeJCIC",' ') IN ('20', '21', '22', '23', '24', '2X') THEN 'X'
             WHEN  NVL(B."ParkingTypeCode",' ')    IN (' ') THEN '0'
             WHEN  SUBSTR(B."ParkingTypeCode",1,1) IN ('2') THEN '3'
             WHEN  SUBSTR(B."ParkingTypeCode",1,1) IN ('3') THEN '2'
             WHEN  SUBSTR(B."ParkingTypeCode",1,1) IN ('5') THEN '0'
             ELSE  SUBSTR(B."ParkingTypeCode",1,1)
           END                                   AS "ParkingTypeCode"   -- 停車位形式   -- (ref:AS400 LN15M1)
         , CASE
             WHEN  NVL(CD2."ClTypeJCIC",' ') IN ('20', '21', '22', '23', '24', '2X') THEN '000000000'  -- "ParkingTypeCode" = 'X'
             WHEN  NVL(B."ParkingTypeCode",'1') IN ('1')        THEN '000000000'  -- "ParkingTypeCode" = '0' 無車位
             WHEN  NVL(CP."ParkingArea",0) = 0        THEN '000000000'
             WHEN  NVL(B."ParkingProperty",'N') = 'Y'
--              THEN SUBSTR('000000000' || TRUNC(NVL("ClBuildingParking"."Area",0) / 0.3025, 2), -9)
                THEN to_char(TRUNC(NVL(CP."ParkingArea",0) / 0.3025, 2), 'FM000000.00')
             ELSE '000000000'
           END                                   AS "Area"              -- 車位單獨登記面積 (1平方公尺=0.3025坪)
         , CASE
--             WHEN NVL(LandOwner."OwnerTotal",0) = 0 THEN TRUNC( NVL(L."Area", 0) / 0.3025 , 2 )
--             ELSE TRUNC( NVL(L."Area", 0) * NVL(LandOwner."OwnerPart", 0) / NVL(LandOwner."OwnerTotal", 0) / 0.3025 , 2 )
--             WHEN NVL(CLL."LandNo1", 0) = NVL(L."LandNo1", 0) 
--                  AND NVL(CLL."LandNo2", 0) = NVL(L."LandNo2", 0)
--             THEN TRUNC( NVL(CLL."ClAreaCount", 0) / NVL(CLC."ClCount",1) / 0.3025 , 2 )
--             WHEN  NVL(L."LandNo1", 0)  = 0
--                  AND NVL(L."LandNo2", 0) = 0
--             THEN TRUNC( NVL(CLL."ClAreaCount", 0) / NVL(CLC."ClCount",1) / 0.3025 , 2 )
             WHEN NVL(CLL."LandNo1", 0)
                  + NVL(CLL."LandNo2", 0) != 0
             THEN TRUNC( NVL(CLL."ClAreaCount", 0) / NVL(CLC."ClCount",1) / 0.3025 , 2 )
             ELSE 0     
           END                                   AS "LandOwnedArea"     -- 土地持份面積 (1平方公尺=0.3025坪)
         , TRUNC(CASE
                   WHEN NVL(SCLAD."SameClLineAmt",0) != 0
                   THEN NVL(SCLAD."SameClLineAmt",0)
                 ELSE NVL(LAD."LineAmt",0) END)  AS "LineAmt"           -- 核准額度
    FROM   "JcicB090" M
      LEFT JOIN "CdCl"         ON "CdCl"."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                              AND "CdCl"."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))
      LEFT JOIN "FacMain" F    ON F."CustNo"   =  to_number(SUBSTR(M."FacmNo",1,7))
                              AND F."FacmNo"   =  to_number(SUBSTR(M."FacmNo",8,3))
      --LEFT JOIN "ClFac"  CF    ON CF."CustNo"   =  to_number(SUBSTR(M."FacmNo",1,7))
      --                        AND CF."FacmNo"   =  to_number(SUBSTR(M."FacmNo",8,3))  -- 關聯所有擔保品編號(含主要擔保品)
      LEFT JOIN "CdCl" CD2     ON CD2."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                              AND CD2."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))           -- 關聯擔保品類別
      LEFT JOIN "ClMain" CM    ON CM."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                              AND CM."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))
                              AND CM."ClNo"     = to_number(SUBSTR(M."ClActNo",4,7))
      LEFT JOIN "ClImm" CI    ON CI."ClCode1"   = CM."ClCode1"
                              AND CI."ClCode2"  = CM."ClCode2"
                              AND CI."ClNo"     = CM."ClNo"
      LEFT JOIN "ClBuilding"  B     ON B."ClCode1" = CM."ClCode1"
                                   AND B."ClCode2" = CM."ClCode2"
                                   AND B."ClNo"    = CM."ClNo"
      LEFT JOIN "ClLand" L ON L."ClCode1" = CM."ClCode1"
                          AND L."ClCode2" = CM."ClCode2"
                          AND L."ClNo"    = CM."ClNo"
                          AND CM."ClCode1" = 2 -- 土地的時候才自己串土地檔
      LEFT JOIN (
        SELECT "ClCode1"
             , "ClCode2"
             , "ClNo"
             , SUM("ParkingArea") AS "ParkingArea"
        FROM "ClParking"
        GROUP BY  "ClCode1"
               , "ClCode2"
               , "ClNo"
      ) CP ON CP."ClCode1"      = B."ClCode1"
          AND CP."ClCode2"      = B."ClCode2"
          AND CP."ClNo"         = B."ClNo"
      LEFT JOIN ( SELECT DISTINCT
                         O."ClCode1", O."ClCode2", O."ClNo", O."OwnerPart", O."OwnerTotal"
                       , C."CustId"  AS "OwnerId"
                  FROM "ClBuildingOwner" O
                    LEFT JOIN "CustMain" C  ON  C."CustUKey"  = O."OwnerCustUKey"
                ) BuildingOwner    ON BuildingOwner."ClCode1" = CM."ClCode1"
                                  AND BuildingOwner."ClCode2" = CM."ClCode2"
                                  AND BuildingOwner."ClNo"    = CM."ClNo"
      LEFT JOIN ( SELECT DISTINCT
                         O."ClCode1"
                       , O."ClCode2"
                       , O."ClNo"
                       , NVL(O."OwnerPart",O2."OwnerPart") AS "OwnerPart"
                       , NVL(O."OwnerTotal",O2."OwnerTotal") AS "OwnerTotal"
                       , C."CustId"  AS "OwnerId"
                  FROM "ClLandOwner" O
                  LEFT JOIN (
                    SELECT CF1."ClCode1"
                         , CF1."ClCode2"
                         , CF1."ClNo"
                         , CLO."OwnerPart"
                         , CLO."OwnerTotal"
                         , CLO."OwnerCustUKey"
                    FROM "ClFac" CF1
                    LEFT JOIN "ClFac" CF2 ON CF2."CustNo" = CF1."CustNo"
                                         AND CF2."FacmNo" = CF2."FacmNo"
                                         AND CF2."MainFlag" = 'Y'
                    LEFT JOIN "ClLandOwner" CLO ON CLO."ClCode1" = CF2."ClCode1"
                                               AND CLO."ClCode2" = CF2."ClCode2"
                                               AND CLO."ClNo" = CF2."ClNo"
                    WHERE CF1."MainFlag" != 'Y'
                      AND CLO."OwnerPart" IS NOT NULL
                  ) O2 ON O2."ClCode1" = O."ClCode1"
                      AND O2."ClCode2" = O."ClCode2"
                      AND O2."ClNo" = O."ClNo"
                    LEFT JOIN "CustMain" C  ON  C."CustUKey"  = O."OwnerCustUKey"
                ) LandOwner        ON LandOwner."ClCode1" = CM."ClCode1"
                                  AND LandOwner."ClCode2" = CM."ClCode2"
                                  AND LandOwner."ClNo"    = CM."ClNo"
                                  AND BuildingOwner."OwnerId" IS NULL
      LEFT JOIN ( SELECT DISTINCT
                         O."ClCode1", O."ClCode2", O."ClNo", O."OwnerId"
                  FROM "ClBuildingPublic" O
                ) BuPublicOwner    ON BuPublicOwner."ClCode1" = CM."ClCode1"
                                  AND BuPublicOwner."ClCode2" = CM."ClCode2"
                                  AND BuPublicOwner."ClNo"    = CM."ClNo"
                                  AND LandOwner."OwnerId"     IS NULL
                                  AND BuildingOwner."OwnerId" IS NULL
      LEFT JOIN ClcountData CLC ON CLC."FacmNo" = M."FacmNo"
      LEFT JOIN CllandData  CLL ON CLL."FacmNo" = M."FacmNo"
                               AND CASE
                                     WHEN CM."ClCode1" = 2
                                          AND L."LandNo1" = CLL."LandNo1"
                                          AND L."LandNo2" = CLL."LandNo2"
                                     THEN 1
                                     WHEN CM."ClCode1" != 2
                                     THEN 1
                                   ELSE 0 END = 1
      LEFT JOIN "CdCity"       ON "CdCity"."CityCode"  = NVL(CLL."CityCode",CM."CityCode")
      LEFT JOIN "CdArea"       ON "CdArea"."CityCode"   = NVL(CLL."CityCode",CM."CityCode")
                              AND "CdArea"."AreaCode"   = NVL(CLL."AreaCode",CM."AreaCode")
      LEFT JOIN LineAmtData LAD ON LAD."ClCode1" = to_number(SUBSTR(M."ClActNo",1,1))
                               AND LAD."ClCode2" = to_number(SUBSTR(M."ClActNo",2,2))
                               AND LAD."ClNo" = to_number(SUBSTR(M."ClActNo",4,7))
                               AND LAD."CustNo" = to_number(SUBSTR(M."FacmNo",1,7))
                               AND LAD."FacmNoSeq" = 1
      LEFT JOIN sameClLineAmtData SCLAD ON SCLAD."CustNo" = to_number(SUBSTR(M."FacmNo",1,7))
    WHERE  M."DataYM"  =  YYYYMM
      AND  M."ClActNo" IS NOT NULL
      AND  SUBSTR("CdCl"."ClTypeJCIC",1,1) IN ('2')         -- 主要擔保品為不動產
      AND  SUBSTR(CD2."ClTypeJCIC",1,1) IN ('2')            -- 擔保品為不動產
--    AND  L."ClNo" IS NOT NULL
      --AND  NVL(L."LandNo1", 0) > 0
      AND  ( NVL(CLL."LandNo1", 0)   > 0 OR NVL(CLL."LandNo2", 0)   > 0 OR
             NVL(B."BdNo1", 0) > 0 OR NVL(B."BdNo2", 0) > 0 )
      AND ( ( CM."ClCode1" NOT IN (1) ) OR
            ( CM."ClCode1" IN (1) AND ( NVL(B."BdNo1", 0) > 0 OR
                                        NVL(B."BdNo2", 0) > 0 ) )
          )
--and M."FacmNo" = '1285192004'
--order by L."CityCode", L."AreaCode", L."IrCode",
--         L."LandNo1", L."LandNo2", B."BdNo1", B."BdNo2"
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('INSERT Work_B092 END: INS_CNT=' || INS_CNT);


-- 擔保品類別更新為 '20' (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE Work_B092 ClTypeJCIC');

    UPDATE "Work_B092" M
    SET   M."ClTypeJCIC" = '20'
    WHERE to_number(NVL(M."LandOwnedArea",'0')) >= 300
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE Work_B092 ClTypeJCIC INS_CNT=' || INS_CNT);
    DBMS_OUTPUT.PUT_LINE('UPDATE Work_B092 ClTypeJCIC END');

-- BdNo1-BdNo2 建號 再處理 (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE Work_B092 BdNo1 BdNo2');

    UPDATE "Work_B092" M
    SET  M."BdNo1" = CASE WHEN SUBSTR(M."ClTypeJCIC",1,1) IN ('0', '1')         THEN 0
                          WHEN M."ClTypeJCIC" IN ('20', '21', '22', '23', '24') THEN 0
                          WHEN M."BdNo1" = 0                                    THEN 1
                          ELSE M."BdNo1"
                     END
       , M."BdNo2" = CASE WHEN SUBSTR(M."ClTypeJCIC",1,1) IN ('0', '1')         THEN 0
                          WHEN M."ClTypeJCIC" IN ('20', '21', '22', '23', '24') THEN 0
                          ELSE M."BdNo2"
                     END
      ;

    DBMS_OUTPUT.PUT_LINE('UPDATE Work_B092 BdNo1 BdNo2 END');


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB092');

    DELETE FROM "JcicB092"
    WHERE "DataYM" = YYYYMM
      ;

    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB092');
    INS_CNT := 0;

    INSERT INTO "JcicB092"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '92'                                  AS "DataType"          -- 資料別
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , ' '                                   AS "Filler4"           -- 空白
         , WK."MainClActNo"                      AS "ClActNo"           -- 擔保品控制編碼
         , WK."ClTypeJCIC"                       AS "ClTypeJCIC"        -- 擔保品類別
         , WK."OwnerId"                          AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN
         , MAX(WK."EvaAmt")                      AS "EvaAmt"            -- 鑑估(總市)值 (後面會再更新)
         , MAX(WK."EvaDate")                     AS "EvaDate"           -- 鑑估日期
         , TRUNC(MAX(WK."LineAmt") / 1000)       AS "LoanLimitAmt"      -- 可放款值 = 核准額度 (ref:AS400 LN15M1)
         , MAX(WK."SettingDate")                 AS "SettingDate"       -- 設定日期
         , MAX(WK."MonthSettingAmt")             AS "MonthSettingAmt"   -- 本行本月設定金額
         , MAX(WK."SettingSeq")                  AS "SettingSeq"        -- 本月設定抵押順位
         , TRUNC(MAX(WK."LineAmt") * 1.2 / 1000) AS "SettingAmt"        -- 本行累計已設定總金額 = 核准額度 * 1.2 (ref:AS400 LN15M1)
         , MAX(WK."PreSettingAmt")               AS "PreSettingAmt"     -- 其他債權人已設定金額
         , MAX(WK."DispPrice")                   AS "DispPrice"         -- 處分價格
         , MAX(WK."IssueEndDate")                AS "IssueEndDate"      -- 權利到期年月
         , WK."CityCode"                         AS "CityJCICCode"      -- 縣市別
         , TO_NUMBER(WK."AreaCode")              AS "AreaJCICCode"      -- 鄉鎮市區別
         , WK."IrCode"                           AS "IrCode"            -- 段、小段號
         , WK."LandNo1"                          AS "LandNo1"           -- 地號-前四碼
         , WK."LandNo2"                          AS "LandNo2"           -- 地號-後四碼
         , WK."BdNo1"                            AS "BdNo1"             -- 建號-前五碼
         , WK."BdNo2"                            AS "BdNo2"             -- 建號-後三碼
         , MAX(WK."Zip")                         AS "Zip"               -- 郵遞區號
         , CASE
             WHEN SUBSTR(WK."MainClActNo",1,1) IN ('2') THEN 'N'        -- 土地資料沒有保險
             ELSE 'Y'
           END                                   AS "InsuFg"            -- 是否有保險 (ref:AS400 LN15M1)
         , MAX(WK."LVITax")                      AS "LVITax"            -- 預估應計土地增值稅  (後面再額外判斷 '日期')
         , MAX(WK."LVITaxYearMonth")             AS "LVITaxYearMonth"   -- 應計土地增值稅之預估年月  (後面再額外判斷 '日期')
         , MAX(WK."ContractPrice")               AS "ContractPrice"     -- 買賣契約價格
         , MAX(WK."ContractDate")                AS "ContractDate"      -- 買賣契約日期
         , CASE
             WHEN  NVL(WK."ClTypeJCIC",' ') IN ('20', '21', '22', '23', '24', '2X') THEN 'X'
             ELSE  MAX(WK."ParkingTypeCode")
           END                                   AS "ParkingTypeCode"   -- 停車位形式   -- (ref:AS400 LN15M1)
         , CASE
             WHEN  NVL(WK."ClTypeJCIC",' ') IN ('20', '21', '22', '23', '24', '2X') THEN '000000000'  -- "ParkingTypeCode" = 'X'
             ELSE  MAX(WK."Area")
           END                                   AS "Area"              -- 車位單獨登記面積 (1平方公尺=0.3025坪)
         , to_char(MAX(WK."LandOwnedArea"),'FM0000000.00')
                                                 AS "LandOwnedArea"     -- 土地持份面積 (1平方公尺=0.3025坪)
         , CASE
--           WHEN  WK."ClCode1" IN ('1') THEN 'R1'
             WHEN  WK."ClTypeJCIC" IN ('25') THEN 'R1'
             ELSE  'XX'
           END                                   AS "BdTypeCode"        -- 建物類別 (ref:AS400 LN15M1) 後面會再更新
         , ' '                                   AS "Filler33"          -- 空白
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Work_B092" WK
    GROUP BY WK."MainClActNo",  WK."ClTypeJCIC", WK."OwnerId"
           , WK."CityCode", WK."AreaCode", WK."IrCode"
           , WK."LandNo1", WK."LandNo2", WK."BdNo1", WK."BdNo2"
      ;

    INS_CNT := INS_CNT + sql%rowcount;
    DBMS_OUTPUT.PUT_LINE('UPDATE JcicB092 INS_CNT=' || INS_CNT);
    DBMS_OUTPUT.PUT_LINE('UPDATE JcicB092 END');


-- 更新 "EvaAmt" 鑑估(總市)值 (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt');
    UPD_CNT := 0;

    UPDATE "JcicB092" M
    SET   M."EvaAmt" = SUBSTR('00000000' || TRUNC(to_number(M."LoanLimitAmt") * 1.2), -8)
    WHERE M."DataYM" =  YYYYMM
      AND to_number(M."EvaAmt") <  to_number(M."LoanLimitAmt")
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;
--  DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt UPD_CNT=' || UPD_CNT);
    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt END');


-- 更新 "LVITax" 預估應計土地增值稅, "LVITaxYearMonth" 應計土地增值稅之預估年月
    DBMS_OUTPUT.PUT_LINE('UPDATE LVITax, LVITaxYearMonth');
    UPD_CNT := 0;

    UPDATE "JcicB092" M
    SET   M."LVITax" = 'X'
        , M."LVITaxYearMonth" = 'X'
    WHERE M."DataYM"      =  YYYYMM
      AND M."SettingDate" <  9601
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;
--  DBMS_OUTPUT.PUT_LINE('UPDATE LVITax, LVITaxYearMonth UPD_CNT=' || UPD_CNT);
    DBMS_OUTPUT.PUT_LINE('UPDATE LVITax, LVITaxYearMonth END');


-- 鑑估值＜土增稅時，兩者對調 (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt, LVITax');
    UPD_CNT := 0;

    UPDATE "JcicB092" M
     SET  M."EvaAmt" = LPAD(M."LVITax",8,'0')
         ,M."LVITax" = LPAD(M."EvaAmt",8,'0')
    WHERE M."DataYM" =  YYYYMM
      AND M."LVITax" <> 'X'
      AND CASE
            WHEN M."LVITax" <> 'X'
                 AND to_number(M."EvaAmt") < to_number(M."LVITax")
            THEN 1
          ELSE 0 END = 1
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;
--  DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt, LVITax UPD_CNT=' || UPD_CNT);
    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt, LVITax END');


-- 擔保品類別為 '20'~'24'之處理 (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE for ClTypeJCIC=20~24');
    UPD_CNT := 0;

    UPDATE "JcicB092" M
    SET   M."ParkingTypeCode" = 'X'
        , M."BdTypeCode"      = 'XX'
    WHERE M."DataYM"      =  YYYYMM
      AND M."ClTypeJCIC"  IN ('20', '21', '22', '23', '24')
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;
--  DBMS_OUTPUT.PUT_LINE('UPDATE for ClTypeJCIC=20~24 UPD_CNT=' || UPD_CNT);
    DBMS_OUTPUT.PUT_LINE('UPDATE for ClTypeJCIC=20~24 END');


-- Area 車位單獨登記面積之處理 (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE Area');
    UPD_CNT := 0;

    UPDATE "JcicB092" M
    SET   M."Area" = '000010.00'
    WHERE M."DataYM"      =  YYYYMM
      AND M."ParkingTypeCode"  IN ('1', '2', '3', '4', '5', '6')
      AND M."Area"        =  '000000000'
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;
--  DBMS_OUTPUT.PUT_LINE('UPDATE Area UPD_CNT=' || UPD_CNT);
    DBMS_OUTPUT.PUT_LINE('UPDATE Area END');

-- 土地持分面積之處理 - 有地號時面積不可為0 FOR檢核
    DBMS_OUTPUT.PUT_LINE('UPDATE LandOwnedArea');
    UPD_CNT := 0;

    UPDATE "JcicB092" M
    SET   M."LandOwnedArea" = '0000000.01'
    WHERE M."DataYM"      =  YYYYMM
      AND ( M."LandNo1" > 0 OR  M."LandNo2" > 0 )  
      AND to_number(M."LandOwnedArea") =  0
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;
--  DBMS_OUTPUT.PUT_LINE('UPDATE Area UPD_CNT=' || UPD_CNT);
    DBMS_OUTPUT.PUT_LINE('UPDATE Area END');


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
