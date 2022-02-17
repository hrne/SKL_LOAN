-- 程式功能：維護 JcicB092 每月聯徵不動產擔保品明細檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB092_Upd"(20200420,'999999');
--

DROP TABLE "Work_B092" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B092"
    (  "ClActNo"     varchar2(50)
     , "ClCode1"     decimal(1, 0)   default 0 not null
     , "ClCode2"     decimal(2, 0)   default 0 not null
     , "ClNo"        decimal(7, 0)   default 0 not null
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


    -- 寫入資料 Work_B092    -- 從 JcicB090 撈不動產擔保品編號    (需確認擔保品擷取位置)
    INSERT INTO "Work_B092"
    SELECT M."ClActNo"                           AS "ClActNo"           -- 擔保品控制編碼
         , to_number(SUBSTR(M."ClActNo",1,1))    AS "ClCode1"           -- 擔保品代號1
         , to_number(SUBSTR(M."ClActNo",2,2))    AS "ClCode2"           -- 擔保品代號2
         , to_number(SUBSTR(M."ClActNo",4,7))    AS "ClNo"              -- 擔保品編號
    FROM   "JcicB090" M
    WHERE  M."DataYM"  =  YYYYMM
      AND  M."ClActNo" IS NOT NULL
      AND  to_number(SUBSTR(M."ClActNo",1,1)) IN (1, 2)     -- 不動產
    GROUP BY M."ClActNo"
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
         , NVL("CdCl"."ClTypeJCIC",' ')          AS "ClTypeJCIC"        -- 擔保品類別
         , CASE
             WHEN BuildingOwner."OwnerId"  IS NOT NULL THEN BuildingOwner."OwnerId"
             WHEN LandOwner."OwnerId"      IS NOT NULL THEN LandOwner."OwnerId"
             WHEN BuPublicOwner."OwnerId"  IS NOT NULL THEN BuPublicOwner."OwnerId"
             ELSE ' '
           END                                   AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN
         , trim(to_char(ROUND(NVL("ClMain"."EvaAmt",0) / 1000, 0), '900000000'))
                                                 AS "EvaAmt"            -- 鑑估(總市)值
         , CASE
             WHEN FLOOR(NVL("ClMain"."EvaDate",0) / 100) < 191100 THEN FLOOR(NVL("ClMain"."EvaDate",0) / 100)
             ELSE FLOOR(NVL("ClMain"."EvaDate",0) / 100) - 191100
           END                                   AS "EvaDate"           -- 鑑估日期
         , trim(to_char(ROUND( NVL("ClMain"."EvaAmt",0) * NVL(C."LoanToValue",0) / 100 / 1000, 0), '900000000'))
                                                 AS "LoanLimitAmt"      -- 可放款值
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
             WHEN FLOOR(NVL(C."SettingDate",0) / 100) = YYYYMM THEN to_number(NVL(C."SettingSeq",0))
             ELSE 0
           END                                   AS "SettingSeq"        -- 本月設定抵押順位
         , trim(to_char(ROUND(NVL(C."SettingAmt",0) / 1000, 0), '900000000'))
                                                 AS "SettingAmt"        -- 本行累計已設定總金額
         , trim(to_char(ROUND(NVL(C."OtherOwnerTotal",0) / 1000, 0), '900000000'))
                                                 AS "PreSettingAmt"     -- 其他債權人已設定金額
         , trim(to_char(ROUND(NVL("ClMain"."DispPrice",0) / 1000, 0), '900000000'))
                                                 AS "DispPrice"         -- 處分價格
         , 0                                     AS "IssueEndDate"      -- 權利到期年月
         , NVL("CdCity"."JcicCityCode", ' ')     AS "CityJCICCode"      -- 縣市別
         , CASE
             WHEN "ClMain"."AreaCode" IS NULL   THEN 0
             WHEN "ClMain"."AreaCode" IN (' ')  THEN 0
             ELSE to_number("ClMain"."AreaCode")
           END                                   AS "AreaJCICCode"      -- 鄉鎮市區別
         , CASE
             WHEN "ClBuilding"."IrCode" IS NOT NULL THEN NVL(SUBSTR("ClBuilding"."IrCode", 2, 4), ' ')
             WHEN "ClLand"."IrCode"     IS NOT NULL THEN NVL(SUBSTR("ClLand"."IrCode", 2, 4), ' ')
             ELSE ' '
           END                                   AS "IrCode"            -- 段、小段號
         , NVL("ClLand"."LandNo1", 0)            AS "LandNo1"           -- 地號-前四碼
         , NVL("ClLand"."LandNo2", 0)            AS "LandNo2"           -- 地號-後四碼
         , NVL("ClBuilding"."BdNo1", 0)          AS "BdNo1"             -- 建號-前五碼
         , NVL("ClBuilding"."BdNo2", 0)          AS "BdNo2"             -- 建號-後三碼
         , NVL("CdArea"."Zip3",' ')              AS "Zip"               -- 郵遞區號
         , CASE
             WHEN InsuOrg."ClNo" IS NULL AND InsuRenew."ClNo" IS NULL THEN 'N'
             ELSE 'Y'
           END                                   AS "InsuFg"            -- 是否有保險
         , trim(to_char(ROUND(NVL("ClLand"."LVITax",0) / 1000, 0), '900000000'))
                                                 AS "LVITax"            -- 預估應計土地增值稅  (後面再額外判斷 '日期')
         , CASE
             WHEN NVL("ClLand"."LVITaxYearMonth",0) < 191100 THEN NVL("ClLand"."LVITaxYearMonth",0)
             ELSE NVL("ClLand"."LVITaxYearMonth",0) - 191100
           END                                   AS "LVITaxYearMonth"   -- 應計土地增值稅之預估年月  (後面再額外判斷 '日期')
         , CASE
             WHEN ROUND(NVL("ClBuilding"."ContractPrice",0) / 1000, 0) = 0 THEN ' '
             ELSE trim(to_char(ROUND(NVL("ClBuilding"."ContractPrice",0) / 1000, 0), '900000000'))
           END                                   AS "ContractPrice"     -- 買賣契約價格
         , CASE
             WHEN FLOOR(NVL("ClBuilding"."ContractDate",0) / 100) = 0 THEN '     '
             WHEN FLOOR("ClBuilding"."ContractDate" / 100) < 191100
                  THEN SUBSTR('00000' || to_char(FLOOR("ClBuilding"."ContractDate" / 100)) , -5, 5)
             ELSE SUBSTR('00000' || to_char(FLOOR("ClBuilding"."ContractDate" / 100) - 191100) , -5, 5)
           END                                   AS "ContractDate"      -- 買賣契約日期
         , CASE
             WHEN  NVL("CdCl"."ClTypeJCIC",' ') IN ('20', '21', '22', '23', '24', '2X') THEN 'X'
          -- WHEN  NVL("CdCl"."ClTypeJCIC",' ') IN ('25', '27', '2A', '2B') AND ...     THEN '5'
             WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('1') THEN '0'
             WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('2') THEN '1'
             WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('3') THEN '2'
             WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('4') THEN '3'
             WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('5') THEN '4'
             WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('6') THEN '5'
             ELSE  '0'
           END                                   AS "ParkingTypeCode"   -- 停車位形式
         , CASE
             WHEN  NVL("CdCl"."ClTypeJCIC",' ') IN ('20', '21', '22', '23', '24', '2X') THEN '0.00'  -- "ParkingTypeCode" = 'X'
             WHEN  NVL("ClBuilding"."ParkingTypeCode",'1') IN ('1')                     THEN '0.00'  -- "ParkingTypeCode" = '0' 無車位
             WHEN  NVL("ClBuilding"."ParkingProperty",'N') = 'Y'
                THEN trim(to_char(ROUND(NVL("ClBuildingParking"."Area",0) * 3.3058 , 2), '999990.99'))
             ELSE '0.00'
           END                                   AS "Area"              -- 車位單獨登記面積
         , CASE
             WHEN NVL(LandOwner."OwnerTotal",0) = 0
               THEN trim(to_char(ROUND( NVL("ClLand"."Area", 0) * 3.3058 , 2 ), '9999990.99'))
             ELSE trim(to_char(ROUND( NVL("ClLand"."Area", 0) * NVL(LandOwner."OwnerPart", 0) / NVL(LandOwner."OwnerTotal", 0) * 3.3058 , 2 ), '9999990.99'))
           END                                   AS "LandOwnedArea"     -- 土地持份面積
         , CASE
             WHEN  NVL("CdCl"."ClTypeJCIC",' ') IN ('20', '21', '22', '23', '24', '2X') THEN 'XX'
             WHEN  NVL("ClBuilding"."BdTypeCode",' ') IN ('01') THEN 'R1'
             WHEN  NVL("ClBuilding"."BdTypeCode",' ') IN ('02') THEN 'R2'
             WHEN  NVL("ClBuilding"."BdTypeCode",' ') IN ('03') THEN 'R3'
             WHEN  NVL("ClBuilding"."BdTypeCode",' ') IN ('04') THEN 'R4'
             WHEN  NVL("ClBuilding"."BdTypeCode",' ') IN ('05') THEN 'R5'
             WHEN  NVL("ClBuilding"."BdTypeCode",' ') IN ('06') THEN 'R6'
             WHEN  NVL("ClBuilding"."BdTypeCode",' ') IN ('07') THEN 'B1'
             WHEN  NVL("ClBuilding"."BdTypeCode",' ') IN ('08') THEN 'B2'
             WHEN  NVL("ClBuilding"."BdTypeCode",' ') IN ('09') THEN 'B3'
             WHEN  NVL("ClBuilding"."BdTypeCode",' ') IN ('10') THEN 'C1'
             WHEN  NVL("ClBuilding"."BdTypeCode",' ') IN ('11') THEN '99'
             ELSE  '99'
           END                                   AS "BdTypeCode"        -- 建物類別
         , ' '                                   AS "Filler33"          -- 空白
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Work_B092" WK
        LEFT JOIN "CdCl"             ON "CdCl"."ClCode1"  = WK."ClCode1"
                                    AND "CdCl"."ClCode2"  = WK."ClCode2"
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
        LEFT JOIN "CdCity"           ON to_number("CdCity"."CityCode")  = to_number("ClMain"."CityCode")
        LEFT JOIN "CdArea"           ON "CdArea"."CityCode"   = "ClMain"."CityCode"
                                    AND "CdArea"."AreaCode"   = "ClMain"."AreaCode"
        LEFT JOIN ( SELECT  "ClCode1", "ClCode2", "ClNo", "InsuEndDate",
                            ROW_NUMBER() OVER (PARTITION BY "ClCode1", "ClCode2", "ClNo"
                                               ORDER BY "ClCode1", "ClCode2", "ClNo", "InsuEndDate" DESC) AS "RowNum"
                    FROM  "InsuOrignal"
                    WHERE FLOOR(NVL("InsuOrignal"."InsuEndDate",0) / 100) >= YYYYMM
                    ORDER BY "ClCode1", "ClCode2", "ClNo", "RowNum"
                  ) InsuOrg          ON InsuOrg."ClCode1"  = WK."ClCode1"
                                    AND InsuOrg."ClCode2"  = WK."ClCode2"
                                    AND InsuOrg."ClNo"     = WK."ClNo"
                                    AND InsuOrg."RowNum"   = 1            -- 取第一筆
        LEFT JOIN ( SELECT  "ClCode1", "ClCode2", "ClNo", "InsuEndDate",
                            ROW_NUMBER() OVER (PARTITION BY "ClCode1", "ClCode2", "ClNo"
                                               ORDER BY "ClCode1", "ClCode2", "ClNo", "InsuEndDate" DESC) AS "RowNum"
                    FROM  "InsuRenew"
                    WHERE FLOOR(NVL("InsuRenew"."InsuEndDate",0) / 100) >= YYYYMM
                      AND NVL("InsuRenew"."AcDate",0) > 0
                    ORDER BY "ClCode1", "ClCode2", "ClNo", "RowNum"
                  ) InsuRenew        ON InsuRenew."ClCode1"  = WK."ClCode1"
                                    AND InsuRenew."ClCode2"  = WK."ClCode2"
                                    AND InsuRenew."ClNo"     = WK."ClNo"
                                    AND InsuRenew."RowNum"   = 1          -- 取第一筆
        LEFT JOIN ( SELECT DISTINCT
                           O."ClCode1", O."ClCode2", O."ClNo", O."OwnerId", O."OwnerPart", O."OwnerTotal"
                    FROM "ClBuildingOwner" O
                  ) BuildingOwner    ON BuildingOwner."ClCode1" = WK."ClCode1"
                                    AND BuildingOwner."ClCode2" = WK."ClCode2"
                                    AND BuildingOwner."ClNo"    = WK."ClNo"
        LEFT JOIN ( SELECT DISTINCT
                           O."ClCode1", O."ClCode2", O."ClNo", O."OwnerId", O."OwnerPart", O."OwnerTotal"
                    FROM "ClLandOwner" O
                  ) LandOwner        ON LandOwner."ClCode1" = WK."ClCode1"
                                    AND LandOwner."ClCode2" = WK."ClCode2"
                                    AND LandOwner."ClNo"    = WK."ClNo"
                                    AND BuildingOwner."OwnerId" IS NULL
        LEFT JOIN ( SELECT DISTINCT
                           O."ClCode1", O."ClCode2", O."ClNo", O."OwnerId"
                    FROM "ClBuildingPublic" O
                  ) BuPublicOwner    ON BuPublicOwner."ClCode1" = WK."ClCode1"
                                    AND BuPublicOwner."ClCode2" = WK."ClCode2"
                                    AND BuPublicOwner."ClNo"    = WK."ClNo"
                                    AND BuildingOwner."OwnerId" IS NULL
                                    AND LandOwner."OwnerId"     IS NULL
      ;


    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB092 END: INS_CNT=' || INS_CNT);


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


-- 更新 "Area" 車位單獨登記面積
    DBMS_OUTPUT.PUT_LINE('UPDATE Area');

    UPDATE "JcicB092" M
    SET   M."Area" = 'X'
    WHERE M."DataYM"      =  YYYYMM
      AND M."SettingDate" <  9601
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