-- 程式功能：維護 JcicB092 每月聯徵不動產擔保品明細檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB092_Upd"(20200420,'999999');
--

DROP TABLE "Work_B092_Dtl" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B092_Dtl"
    (  "ClActNo"        varchar2(50)
     , "ClCode1"        decimal(1, 0)   default 0 not null
     , "ClCode2"        decimal(2, 0)   default 0 not null
     , "ClNo"           decimal(7, 0)   default 0 not null
     , "SubClCode1"     decimal(1, 0)   default 0 not null
     , "SubClCode2"     decimal(2, 0)   default 0 not null
     , "SubClNo"        decimal(7, 0)   default 0 not null
     , "ClTypeJCIC"     varchar2(2)
     , "OwnerId"        varchar2(10)
     , "CityJCICCode"   varchar2(1)
     , "AreaJCICCode"   decimal(2, 0)   default 0 not null
     , "IrCode"         varchar2(4)
     , "LandNo1"        decimal(4, 0)   default 0 not null
     , "LandNo2"        decimal(4, 0)   default 0 not null
     , "BdNo1"          decimal(5, 0)   default 0 not null
     , "BdNo2"          decimal(3, 0)   default 0 not null
     , "LVITax"         decimal(14,2)   default 0 not null
     , "LandOwnedArea"  decimal(14,2)   default 0 not null
    )
    ON COMMIT DELETE ROWS;


DROP TABLE "Work_B092" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B092"
    (  "ClActNo"        varchar2(50)
     , "ClCode1"        decimal(1, 0)   default 0 not null
     , "ClCode2"        decimal(2, 0)   default 0 not null
     , "ClNo"           decimal(7, 0)   default 0 not null
     , "ClTypeJCIC"     varchar2(2)
     , "OwnerId"        varchar2(10)
     , "CityJCICCode"   varchar2(1)
     , "AreaJCICCode"   decimal(2, 0)   default 0 not null
     , "IrCode"         varchar2(4)
     , "LandNo1"        decimal(4, 0)   default 0 not null
     , "LandNo2"        decimal(4, 0)   default 0 not null
     , "BdNo1"          decimal(5, 0)   default 0 not null
     , "BdNo2"          decimal(3, 0)   default 0 not null
     , "LVITax"         decimal(14,2)   default 0 not null
     , "LandOwnedArea"  decimal(14,2)   default 0 not null
    )
    ON COMMIT DELETE ROWS;

create or replace PROCEDURE "Usp_L8_JcicB092_Upd"
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


    -- 寫入資料 Work_B092_Dtl    -- 從 JcicB090 撈不動產擔保品編號(所有擔保品編號)
    INSERT INTO "Work_B092_Dtl"
    SELECT M."ClActNo"                           AS "ClActNo"           -- 主要擔保品控制編碼
         , to_number(SUBSTR(M."ClActNo",1,1))    AS "ClCode1"           -- 主要擔保品代號1
         , to_number(SUBSTR(M."ClActNo",2,2))    AS "ClCode2"           -- 主要擔保品代號2
         , to_number(SUBSTR(M."ClActNo",4,7))    AS "ClNo"              -- 主要擔保品編號
         , CL."ClCode1"                          AS "SubClCode1"        -- 擔保品代號1
         , CL."ClCode2"                          AS "SubClCode2"        -- 擔保品代號2
         , CL."ClNo"                             AS "SubClNo"           -- 擔保品編號
         , NVL(Cd2."ClTypeJCIC",' ')             AS "ClTypeJCIC"        -- 擔保品類別
         , CASE
             WHEN BuildingOwner."OwnerId"  IS NOT NULL THEN BuildingOwner."OwnerId"
             WHEN LandOwner."OwnerId"      IS NOT NULL THEN LandOwner."OwnerId"
             WHEN BuPublicOwner."OwnerId"  IS NOT NULL THEN BuPublicOwner."OwnerId"
             ELSE ' '
           END                                   AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN
--       , NVL("CdCity"."JcicCityCode", ' ')     AS "CityJCICCode"      -- 縣市別
         , CASE
             WHEN  NVL("CdCity"."JcicCityCode", ' ') = ' ' THEN NVL(MC."JcicCityCode", ' ')
             ELSE  NVL("CdCity"."JcicCityCode", ' ')
           END                                   AS "CityJCICCode"      -- 縣市別
         , CASE
             WHEN NVL("ClMain"."AreaCode", ' ') IN (' ')  THEN to_number(NVL(trim(ML."AreaCode"), 0))
             ELSE to_number("ClMain"."AreaCode")
           END                                   AS "AreaJCICCode"      -- 鄉鎮市區別
         , CASE
             WHEN "ClBuilding"."IrCode" IS NOT NULL THEN SUBSTR('0000' || "ClBuilding"."IrCode", -4)
             WHEN "ClLand"."IrCode"     IS NOT NULL THEN SUBSTR('0000' || "ClLand"."IrCode", -4)
             ELSE '0001'
           END                                   AS "IrCode"            -- 段、小段號
         , NVL("ClLand"."LandNo1", 0)            AS "LandNo1"           -- 地號-前四碼
         , NVL("ClLand"."LandNo2", 0)            AS "LandNo2"           -- 地號-後四碼
         , NVL("ClBuilding"."BdNo1", 0)          AS "BdNo1"             -- 建號-前五碼
         , NVL("ClBuilding"."BdNo2", 0)          AS "BdNo2"             -- 建號-後三碼
         , NVL("ClLand"."LVITax",'0')            AS "LVITax"            -- 擔保品預估應計土地增值稅合計
         , CASE
             WHEN NVL(LandOwner."OwnerTotal",0) = 0 THEN TRUNC( NVL("ClLand"."Area", 0) / 0.3025 , 2 )
             ELSE TRUNC( NVL("ClLand"."Area", 0) * NVL(LandOwner."OwnerPart", 0) / NVL(LandOwner."OwnerTotal", 0) / 0.3025 , 2 )
           END                                   AS "LandOwnedArea"     -- 土地持份面積 (1平方公尺=0.3025坪)
    FROM   "JcicB090" M
      LEFT JOIN "CdCl"         ON "CdCl"."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                              AND "CdCl"."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))
      LEFT JOIN "ClLand" ML    ON ML."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                              AND ML."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))
                              AND ML."ClNo"     = to_number(SUBSTR(M."ClActNo",4,7))
                              AND ML."LandSeq"  = 1
      LEFT JOIN "CdCity" MC    ON to_number(MC."CityCode")  = to_number(NVL(trim(ML."CityCode"), 0))
      LEFT JOIN "ClFac"  CL    ON CL."CustNo"   =  SUBSTR(M."FacmNo",1,7)
                              AND CL."FacmNo"   =  SUBSTR(M."FacmNo",8,3)   -- 關聯所有擔保品編號(含主要擔保品)
        LEFT JOIN "CdCl" Cd2     ON Cd2."ClCode1"  = CL."ClCode1"
                                AND Cd2."ClCode2"  = CL."ClCode2"           -- 關聯擔保品類別
        LEFT JOIN "ClMain"       ON "ClMain"."ClCode1"  = CL."ClCode1"
                                AND "ClMain"."ClCode2"  = CL."ClCode2"
                                AND "ClMain"."ClNo"     = CL."ClNo"
        LEFT JOIN "CdCity"       ON to_number("CdCity"."CityCode")  = to_number("ClMain"."CityCode")
        LEFT JOIN "ClLand"       ON "ClLand"."ClCode1" = CL."ClCode1"
                                AND "ClLand"."ClCode2" = CL."ClCode2"
                                AND "ClLand"."ClNo"    = CL."ClNo"
        LEFT JOIN "ClBuilding"       ON "ClBuilding"."ClCode1" = CL."ClCode1"
                                    AND "ClBuilding"."ClCode2" = CL."ClCode2"
                                    AND "ClBuilding"."ClNo"    = CL."ClNo"
        LEFT JOIN ( SELECT DISTINCT
                           O."ClCode1", O."ClCode2", O."ClNo", O."OwnerId", O."OwnerPart", O."OwnerTotal"
                    FROM "ClBuildingOwner" O
                  ) BuildingOwner    ON BuildingOwner."ClCode1" = CL."ClCode1"
                                    AND BuildingOwner."ClCode2" = CL."ClCode2"
                                    AND BuildingOwner."ClNo"    = CL."ClNo"
        LEFT JOIN ( SELECT DISTINCT
                           O."ClCode1", O."ClCode2", O."ClNo", O."OwnerId", O."OwnerPart", O."OwnerTotal"
                    FROM "ClLandOwner" O
                  ) LandOwner        ON LandOwner."ClCode1" = CL."ClCode1"
                                    AND LandOwner."ClCode2" = CL."ClCode2"
                                    AND LandOwner."ClNo"    = CL."ClNo"
                                    AND BuildingOwner."OwnerId" IS NULL
        LEFT JOIN ( SELECT DISTINCT
                           O."ClCode1", O."ClCode2", O."ClNo", O."OwnerId"
                    FROM "ClBuildingPublic" O
                  ) BuPublicOwner    ON BuPublicOwner."ClCode1" = CL."ClCode1"
                                    AND BuPublicOwner."ClCode2" = CL."ClCode2"
                                    AND BuPublicOwner."ClNo"    = CL."ClNo"
                                    AND LandOwner."OwnerId"     IS NULL
                                    AND BuildingOwner."OwnerId" IS NULL
    WHERE  M."DataYM"  =  YYYYMM
      AND  M."ClActNo" IS NOT NULL
      AND  SUBSTR("CdCl"."ClTypeJCIC",1,1) IN ('2')         -- 主要擔保品為不動產
      AND  SUBSTR(Cd2."ClTypeJCIC",1,1) IN ('2')            -- 擔保品為不動產
      AND  ( NVL("ClLand"."LandNo1", 0)   > 0 OR NVL("ClLand"."LandNo2", 0)   > 0 OR
             NVL("ClBuilding"."BdNo1", 0) > 0 OR NVL("ClBuilding"."BdNo2", 0) > 0 )
      ;

-- 無地號，填入主要擔保品的地號
--  DBMS_OUTPUT.PUT_LINE('UPDATE Work_B092_Dtl LandNo');
--
--  MERGE INTO "Work_B092_Dtl" M
--  USING ( SELECT L."ClCode1"       AS "ClCode1"
--               , L."ClCode2"       AS "ClCode2"
--               , L."ClNo"          AS "ClNo"
--               , NVL(L."LandNo1",'0000')       AS "LandNo1"
--               , NVL(L."LandNo2",'0000')       AS "LandNo2"
--          FROM  "ClLand" L
--          where L."LandSeq"  =  1
--       )  B
--  ON (    M."ClCode1"   = B."ClCode1"
--      AND M."ClCode2"   = B."ClCode2"
--      AND M."ClNo"      = B."ClNo"
--     )
--  WHEN MATCHED THEN UPDATE
--    SET M."LandNo1"     = case when  M."LandNo1" = 0 and M."LandNo2" = 0 then to_number(B."LandNo1")
--                               else  M."LandNo1"
--                          end
--      , M."LandNo2"     = case when  M."LandNo1" = 0 and M."LandNo2" = 0 then to_number(B."LandNo2")
--                               else  M."LandNo2"
--                          end
--   ;
--
--  DBMS_OUTPUT.PUT_LINE('UPDATE Work_B092_Dtl LandNo END');


-- 擔保品類別更新為 '20' (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE Work_B092_Dtl ClTypeJCIC');

    UPDATE "Work_B092_Dtl" M
    SET   M."ClTypeJCIC" = '20'
    WHERE to_number(NVL(M."LandOwnedArea",'0')) >= 3
     ;

    DBMS_OUTPUT.PUT_LINE('UPDATE Work_B092_Dtl ClTypeJCIC END');


-- BdNo1-BdNo2 建號 再處理 (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE Work_B092_Dtl BdNo1 BdNo2');

    UPDATE "Work_B092_Dtl" M
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

    DBMS_OUTPUT.PUT_LINE('UPDATE Work_B092_Dtl BdNo1 BdNo2 END');


  -- 寫入資料 Work_B092    -- GROUP 從 Work_B092_Dtl
  INSERT INTO "Work_B092"
  SELECT M."ClActNo"                           AS "ClActNo"           -- 擔保品控制編碼
       , M."ClCode1"                           AS "ClCode1"           -- 擔保品代號1
       , M."ClCode2"                           AS "ClCode2"           -- 擔保品代號2
       , M."ClNo"                              AS "ClNo"              -- 擔保品編號
       , M."ClTypeJCIC"                        AS "ClTypeJCIC"        -- 擔保品類別
       , M."OwnerId"                           AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN
       , M."CityJCICCode"                      AS "CityJCICCode"      -- 縣市別
       , M."AreaJCICCode"                      AS "AreaJCICCode"      -- 鄉鎮市區別
       , M."IrCode"                            AS "IrCode"            -- 段、小段號
       , M."LandNo1"                           AS "LandNo1"           -- 地號-前四碼
       , M."LandNo2"                           AS "LandNo2"           -- 地號-後四碼
       , M."BdNo1"                             AS "BdNo1"             -- 建號-前五碼
       , M."BdNo2"                             AS "BdNo2"             -- 建號-後三碼
       , SUM(M."LVITax")                       AS "LVITax"            -- 擔保品預估應計土地增值稅合計
       , MAX(M."LandOwnedArea")                AS "LandOwnedArea"     -- 土地持份面積 (1平方公尺=0.3025坪)
  FROM   "Work_B092_Dtl" M
  WHERE  M."LandOwnedArea" > 0
  GROUP BY M."ClActNo", M."ClCode1", M."ClCode2", M."ClNo"
         , M."ClTypeJCIC", M."OwnerId"
         , M."CityJCICCode", M."AreaJCICCode", M."IrCode"
         , M."LandNo1", M."LandNo2", M."BdNo1", M."BdNo2"
    ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB092');

    DELETE FROM "JcicB092"
    WHERE "DataYM" = YYYYMM
      ;


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB092');

    INSERT INTO "JcicB092"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '92'                                  AS "DataType"          -- 資料別
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , ' '                                   AS "Filler4"           -- 空白
         , WK."ClActNo"                          AS "ClActNo"           -- 擔保品控制編碼
         , WK."ClTypeJCIC"                       AS "ClTypeJCIC"        -- 擔保品類別
         , WK."OwnerId"                          AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN
         , trim(to_char(TRUNC(NVL("ClMain"."EvaAmt",0) / 1000, 0), '900000000'))
                                                 AS "EvaAmt"            -- 鑑估(總市)值
         , CASE
             WHEN FLOOR(NVL("ClMain"."EvaDate",0) / 100) < 191100 THEN FLOOR(NVL("ClMain"."EvaDate",0) / 100)
             ELSE FLOOR(NVL("ClMain"."EvaDate",0) / 100) - 191100
           END                                   AS "EvaDate"           -- 鑑估日期
         , 0                                     AS "LoanLimitAmt"      -- 可放款值 (後面更新)
         , CASE
             WHEN FLOOR(NVL(C."SettingDate",0) / 100) < 191100 THEN FLOOR(NVL(C."SettingDate",0) / 100)
             ELSE FLOOR(NVL(C."SettingDate",0) / 100) - 191100
           END                                   AS "SettingDate"       -- 設定日期
         , CASE
             WHEN FLOOR(NVL(C."SettingDate",0) / 100) = YYYYMM
               THEN trim(to_char(ROUND(NVL(C."SettingAmt",0) / 1000, 0), '900000000'))
             ELSE trim(to_char(0, '900000000'))
           END                                   AS "MonthSettingAmt"   -- 本行本月設定金額
         , CASE
             WHEN to_number(NVL(C."SettingSeq",0)) = 0 THEN 1
             ELSE to_number(NVL(C."SettingSeq",0))
           END                                   AS "SettingSeq"        -- 本月設定抵押順位
         , 0                                     AS "SettingAmt"        -- 本行累計已設定總金額 (後面更新)
         , trim(to_char(ROUND(NVL(C."OtherOwnerTotal",0) / 1000, 0), '900000000'))
                                                 AS "PreSettingAmt"     -- 其他債權人已設定金額
         , trim(to_char(ROUND(NVL("ClMain"."DispPrice",0) / 1000, 0), '900000000'))
                                                 AS "DispPrice"         -- 處分價格
         , CASE
             WHEN NVL(C."ClaimDate",0) > 30000000 THEN 0
             WHEN FLOOR(NVL(C."ClaimDate",0) / 100) < 191100 THEN FLOOR(NVL(C."ClaimDate",0) / 100)
             ELSE FLOOR(NVL(C."ClaimDate",0) / 100) - 191100
           END                                   AS "IssueEndDate"      -- 權利到期年月
         , WK."CityJCICCode"                     AS "CityJCICCode"      -- 縣市別
         , WK."AreaJCICCode"                     AS "AreaJCICCode"      -- 鄉鎮市區別
         , WK."IrCode"                           AS "IrCode"            -- 段、小段號
         , WK."LandNo1"                          AS "LandNo1"           -- 地號-前四碼
         , WK."LandNo2"                          AS "LandNo2"           -- 地號-後四碼
         , WK."BdNo1"                            AS "BdNo1"             -- 建號-前五碼
         , WK."BdNo2"                            AS "BdNo2"             -- 建號-後三碼
         , NVL("CdArea"."Zip3",' ')              AS "Zip"               -- 郵遞區號
         , CASE
             WHEN WK."ClCode1" IN ('2') THEN 'N'   -- 土地資料沒有保險
             ELSE 'Y'
           END                                   AS "InsuFg"            -- 是否有保險 (ref:AS400 LN15M1)
         , trim(to_char(TRUNC(NVL(WK."LVITax",0) / 1000, 0), '900000000'))
                                                 AS "LVITax"            -- 預估應計土地增值稅  (後面再額外判斷 '日期')
         , CASE
             WHEN NVL("ClLand"."LVITaxYearMonth",0) < 191100 THEN NVL("ClLand"."LVITaxYearMonth",0)
             ELSE NVL("ClLand"."LVITaxYearMonth",0) - 191100
           END                                   AS "LVITaxYearMonth"   -- 應計土地增值稅之預估年月  (後面再額外判斷 '日期')
         , CASE
             WHEN TRUNC(NVL("ClBuilding"."ContractPrice",0) / 1000, 0) = 0 THEN '         '
             ELSE trim(to_char(TRUNC(NVL("ClBuilding"."ContractPrice",0) / 1000, 0), '900000000'))
           END                                   AS "ContractPrice"     -- 買賣契約價格
         , CASE
             WHEN NVL("ClBuilding"."ContractDate",0) = 0 THEN '       '
             WHEN "ClBuilding"."ContractDate" < 19110000
                  THEN SUBSTR('0000000' || to_char("ClBuilding"."ContractDate") , -7)
             ELSE SUBSTR('0000000' || to_char("ClBuilding"."ContractDate" - 19110000) , -7)
           END                                   AS "ContractDate"      -- 買賣契約日期
--       , CASE
--           WHEN  NVL("CdCl"."ClTypeJCIC",' ') IN ('20', '21', '22', '23', '24', '2X') THEN 'X'
--           WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('1') THEN '0'
--           WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('2') THEN '1'
--           WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('3') THEN '2'
--           WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('4') THEN '3'
--           WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('5') THEN '4'
--           WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('6') THEN '5'
--           ELSE  '0'
--         END                                   AS "ParkingTypeCode"   -- 停車位形式
         , CASE
             WHEN  NVL(WK."ClTypeJCIC",' ') IN ('20', '21', '22', '23', '24', '2X') THEN 'X'
             WHEN  NVL("ClBuilding"."ParkingTypeCode",' ')    IN (' ') THEN '0'
             WHEN  SUBSTR("ClBuilding"."ParkingTypeCode",1,1) IN ('2') THEN '3'
             WHEN  SUBSTR("ClBuilding"."ParkingTypeCode",1,1) IN ('3') THEN '2'
             WHEN  SUBSTR("ClBuilding"."ParkingTypeCode",1,1) IN ('5') THEN '0'
             ELSE  SUBSTR("ClBuilding"."ParkingTypeCode",1,1)
           END                                   AS "ParkingTypeCode"   -- 停車位形式   -- (ref:AS400 LN15M1)
         , CASE
             WHEN  NVL(WK."ClTypeJCIC",' ') IN ('20', '21', '22', '23', '24', '2X') THEN '000000000'  -- "ParkingTypeCode" = 'X'
             WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('1')                 THEN '000000000'  -- "ParkingTypeCode" = '0' 無車位
             WHEN  NVL("ClBuildingParking"."Area",0) = 0                            THEN '000000000'
             WHEN  NVL("ClBuilding"."ParkingProperty",'N') = 'Y'
                THEN trim(to_char(TRUNC(NVL("ClBuildingParking"."Area",0) / 0.3025 , 2), '999990.99'))
             ELSE '000000000'
           END                                   AS "Area"              -- 車位單獨登記面積 (1平方公尺=0.3025坪)
         , SUBSTR( '0000000000' || WK."LandOwnedArea", -10)
                                                 AS "LandOwnedArea"     -- 土地持份面積 (1平方公尺=0.3025坪)
         , CASE
             WHEN  WK."ClCode1" IN ('1') THEN 'R1'
             ELSE  'XX'
           END                                   AS "BdTypeCode"        -- 建物類別 (ref:AS400 LN15M1) 後面會再更新
         , ' '                                   AS "Filler33"          -- 空白
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Work_B092" WK
        LEFT JOIN "CdCity"           ON "CdCity"."JcicCityCode"  = WK."CityJCICCode"
        LEFT JOIN "CdArea"           ON "CdArea"."CityCode"   = "CdCity"."CityCode"
                                    AND "CdArea"."AreaCode"   = WK."AreaJCICCode"
        LEFT JOIN "ClMain"           ON "ClMain"."ClCode1"  = WK."ClCode1"
                                    AND "ClMain"."ClCode2"  = WK."ClCode2"
                                    AND "ClMain"."ClNo"     = WK."ClNo"
        LEFT JOIN "ClImm" C          ON C."ClCode1"       = WK."ClCode1"
                                    AND C."ClCode2"       = WK."ClCode2"
                                    AND C."ClNo"          = WK."ClNo"
        LEFT JOIN "ClBuilding"       ON "ClBuilding"."ClCode1" = WK."ClCode1"
                                    AND "ClBuilding"."ClCode2" = WK."ClCode2"
                                    AND "ClBuilding"."ClNo"    = WK."ClNo"
        LEFT JOIN "ClBuildingParking" ON "ClBuildingParking"."ClCode1"      = "ClBuilding"."ClCode1"
                                     AND "ClBuildingParking"."ClCode2"      = "ClBuilding"."ClCode2"
                                     AND "ClBuildingParking"."ClNo"         = "ClBuilding"."ClNo"
                                     AND "ClBuildingParking"."ParkingBdNo1" = "ClBuilding"."BdNo1"
                                     AND "ClBuildingParking"."ParkingBdNo2" = "ClBuilding"."BdNo2"
        LEFT JOIN "ClLand"           ON "ClLand"."ClCode1" = WK."ClCode1"
                                    AND "ClLand"."ClCode2" = WK."ClCode2"
                                    AND "ClLand"."ClNo"    = WK."ClNo"
                                    AND "ClLand"."LandNo1" = WK."LandNo1"
                                    AND "ClLand"."LandNo2" = WK."LandNo2"
      ;


    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB092 END: INS_CNT=' || INS_CNT);


-- 更新 "LoanLimitAmt" 可放款值 = 核准額度 (ref:AS400 LN15M1)
-- 更新 "SettingAmt" 本行累計已設定總金額＝核准額度*1.2 (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE LoanLimitAmt, SettingAmt');

    MERGE INTO "JcicB092" M
    USING ( SELECT M."DataYM"
                 , TRIM(  M."ClActNo")                        AS "ClActNo"
                 , TRUNC( MIN(F."LineAmt") / 1000, 0 )        AS "LineAmt"
                 , TRUNC( MIN(F."LineAmt") * 1.2 / 1000, 0 )  AS "SettingAmt"
            FROM "JcicB090" M
              LEFT JOIN "FacMain" F   ON F."CustNo" = to_number(SUBSTR(M."FacmNo",1,7))
                                     AND F."FacmNo" = to_number(SUBSTR(M."FacmNo",8,3))
            WHERE    M."DataYM" = YYYYMM
            GROUP BY M."DataYM", M."ClActNo"
         )  B
    ON (    M."DataYM"         = YYYYMM
        AND TRIM(M."ClActNo")  = TRIM(B."ClActNo")
       )
    WHEN MATCHED THEN UPDATE
      SET M."LoanLimitAmt" =  B."LineAmt"
        , M."SettingAmt"   =  B."SettingAmt"
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE LoanLimitAmt, SettingAmt END');


-- 更新 "EvaAmt" 鑑估(總市)值 (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt');

    UPDATE "JcicB092" M
    SET   M."EvaAmt" = trim(to_char(TRUNC(to_number(M."LoanLimitAmt") * 1.2, 0), '900000000'))
    WHERE M."DataYM" =  YYYYMM
      AND to_number(M."EvaAmt") <  to_number(M."LoanLimitAmt")
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt END');


-- 更新 "LVITax" 預估應計土地增值稅, "LVITaxYearMonth" 應計土地增值稅之預估年月
    DBMS_OUTPUT.PUT_LINE('UPDATE LVITax, LVITaxYearMonth');

    UPDATE "JcicB092" M
    SET   M."LVITax" = 'X'
        , M."LVITaxYearMonth" = 'X'
    WHERE M."DataYM"      =  YYYYMM
      AND M."SettingDate" <  9601
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE LVITax, LVITaxYearMonth END');


-- 鑑估值＜土增稅時，兩者對調 (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt, LVITax');

    UPDATE "JcicB092" M
    SET   M."EvaAmt" = trim(to_char(to_number(LTRIM(M."LVITax",'0')), '900000000'))
        , M."LVITax" = trim(to_char(to_number(LTRIM(M."EvaAmt",'0')), '900000000'))
    WHERE M."DataYM" =  YYYYMM
      AND M."LVITax" <> 'X'
      AND to_number(LTRIM(M."EvaAmt",'0')) < to_number(LTRIM(M."LVITax",'0'))
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE EvaAmt, LVITax END');


-- 擔保品類別為 '20'~'24'之處理 (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE for ClTypeJCIC=20~24');

    UPDATE "JcicB092" M
    SET   M."ParkingTypeCode" = 'X'
        , M."BdTypeCode"      = 'XX'
    WHERE M."DataYM"      =  YYYYMM
      AND M."ClTypeJCIC"  IN ('20', '21', '22', '23', '24')
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE for ClTypeJCIC=20~24 END');


-- Area 車位單獨登記面積之處理 (ref:AS400 LN15M1)
    DBMS_OUTPUT.PUT_LINE('UPDATE Area');

    UPDATE "JcicB092" M
    SET   M."Area" = '000010.00'
    WHERE M."DataYM"      =  YYYYMM
      AND M."ParkingTypeCode"  IN ('1', '2', '3', '4', '5', '6')
      AND M."Area"        =  '000000000'
     ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE Area END');



    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB092_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
