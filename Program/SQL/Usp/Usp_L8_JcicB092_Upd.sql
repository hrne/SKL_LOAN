create or replace NONEDITIONABLE PROCEDURE "Usp_L8_JcicB092_Upd"
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
    SELECT  -- count(*) as "Count"
           M."FacmNo"                            AS "FacmNo"
         , M."ClActNo"                           AS "MainClActNo"    -- 主要擔保品控制編碼
         , CF."ClCode1"                          AS "ClCode1"        -- 擔保品代號1
         , CF."ClCode2"                          AS "ClCode2"        -- 擔保品代號2
         , CF."ClNo"                             AS "ClNo"           -- 擔保品編號
         , NVL(CD2."ClTypeJCIC",' ')             AS "ClTypeJCIC"     -- 擔保品類別
         , CASE
             WHEN BuildingOwner."OwnerId"  IS NOT NULL THEN BuildingOwner."OwnerId"
             WHEN LandOwner."OwnerId"      IS NOT NULL THEN LandOwner."OwnerId"
             WHEN BuPublicOwner."OwnerId"  IS NOT NULL THEN BuPublicOwner."OwnerId"
             ELSE ' '
           END                                   AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN
         , SUBSTR('00000000' || TRUNC(NVL(CM."EvaAmt",0) / 1000), -8)
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
         , NVL(TRIM(L."AreaCode"), '00')         AS "AreaCode"
         , CASE
--           WHEN "ClBuilding"."IrCode" IS NOT NULL THEN SUBSTR('0000' || "ClBuilding"."IrCode", -4)
             WHEN L."IrCode" IS NOT NULL THEN SUBSTR('0000' || L."IrCode", -4)
             ELSE '0001'
           END                                   AS "IrCode"            -- 段、小段號
         , NVL(L."LandNo1", 0)                   AS "LandNo1"           -- 地號-前四碼
         , NVL(L."LandNo2", 0)                   AS "LandNo2"           -- 地號-後四碼
         , NVL(B."BdNo1", 0)                     AS "BdNo1"             -- 建號-前五碼
         , NVL(B."BdNo2", 0)                     AS "BdNo2"             -- 建號-後三碼
         , NVL("CdArea"."Zip3",' ')              AS "Zip"               -- 郵遞區號
         , TRUNC(NVL(L."LVITax",'0') / 1000)     AS "LVITax"            -- 擔保品預估應計土地增值稅合計
         , CASE
             WHEN NVL(L."LVITaxYearMonth",0) < 191100 THEN NVL(L."LVITaxYearMonth",0)
             ELSE NVL(L."LVITaxYearMonth",0) - 191100
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
             WHEN NVL(LandOwner."OwnerTotal",0) = 0 THEN TRUNC( NVL(L."Area", 0) / 0.3025 , 2 )
             ELSE TRUNC( NVL(L."Area", 0) * NVL(LandOwner."OwnerPart", 0) / NVL(LandOwner."OwnerTotal", 0) / 0.3025 , 2 )
           END                                   AS "LandOwnedArea"     -- 土地持份面積 (1平方公尺=0.3025坪)
         , TRUNC(NVL(F."LineAmt",0))             AS "LineAmt"           -- 核准額度
    FROM   "JcicB090" M
      LEFT JOIN "CdCl"         ON "CdCl"."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                              AND "CdCl"."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))
      LEFT JOIN "FacMain" F    ON F."CustNo"   =  to_number(SUBSTR(M."FacmNo",1,7))
                              AND F."FacmNo"   =  to_number(SUBSTR(M."FacmNo",8,3))
      LEFT JOIN "ClFac"  CF    ON CF."CustNo"   =  to_number(SUBSTR(M."FacmNo",1,7))
                              AND CF."FacmNo"   =  to_number(SUBSTR(M."FacmNo",8,3))  -- 關聯所有擔保品編號(含主要擔保品)
      LEFT JOIN "CdCl" CD2     ON CD2."ClCode1"  = CF."ClCode1"
                              AND CD2."ClCode2"  = CF."ClCode2"           -- 關聯擔保品類別
      LEFT JOIN "ClMain" CM    ON CM."ClCode1"  = CF."ClCode1"
                              AND CM."ClCode2"  = CF."ClCode2"
                              AND CM."ClNo"     = CF."ClNo"
      LEFT JOIN "ClImm" CI    ON CI."ClCode1"  = CF."ClCode1"
                              AND CI."ClCode2"  = CF."ClCode2"
                              AND CI."ClNo"     = CF."ClNo"
      LEFT JOIN "ClBuilding"  B     ON B."ClCode1" = CM."ClCode1"
                                   AND B."ClCode2" = CM."ClCode2"
                                   AND B."ClNo"    = CM."ClNo"
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
      LEFT JOIN "ClLand"  L    ON L."ClCode1" = CM."ClCode1"
                              AND L."ClCode2" = CM."ClCode2"
                              AND L."ClNo"    = CM."ClNo"
      LEFT JOIN "CdCity"       ON to_number("CdCity"."CityCode")  = to_number(NVL(trim(L."CityCode"), 0))
      LEFT JOIN "CdArea"       ON "CdArea"."CityCode"   = L."CityCode"
                              AND "CdArea"."AreaCode"   = L."AreaCode"
      LEFT JOIN ( SELECT DISTINCT
                         O."ClCode1", O."ClCode2", O."ClNo", O."OwnerPart", O."OwnerTotal"
                       , C."CustId"  AS "OwnerId"
                  FROM "ClBuildingOwner" O
                    LEFT JOIN "CustMain" C  ON  C."CustUKey"  = O."OwnerCustUKey"
                ) BuildingOwner    ON BuildingOwner."ClCode1" = CF."ClCode1"
                                  AND BuildingOwner."ClCode2" = CF."ClCode2"
                                  AND BuildingOwner."ClNo"    = CF."ClNo"
      LEFT JOIN ( SELECT DISTINCT
                         O."ClCode1", O."ClCode2", O."ClNo", O."OwnerPart", O."OwnerTotal"
                       , C."CustId"  AS "OwnerId"
                  FROM "ClLandOwner" O
                    LEFT JOIN "CustMain" C  ON  C."CustUKey"  = O."OwnerCustUKey"
                ) LandOwner        ON LandOwner."ClCode1" = CF."ClCode1"
                                  AND LandOwner."ClCode2" = CF."ClCode2"
                                  AND LandOwner."ClNo"    = CF."ClNo"
                                  AND BuildingOwner."OwnerId" IS NULL
      LEFT JOIN ( SELECT DISTINCT
                         O."ClCode1", O."ClCode2", O."ClNo", O."OwnerId"
                  FROM "ClBuildingPublic" O
                ) BuPublicOwner    ON BuPublicOwner."ClCode1" = CF."ClCode1"
                                  AND BuPublicOwner."ClCode2" = CF."ClCode2"
                                  AND BuPublicOwner."ClNo"    = CF."ClNo"
                                  AND LandOwner."OwnerId"     IS NULL
                                  AND BuildingOwner."OwnerId" IS NULL
    WHERE  M."DataYM"  =  YYYYMM
      AND  M."ClActNo" IS NOT NULL
      AND  SUBSTR("CdCl"."ClTypeJCIC",1,1) IN ('2')         -- 主要擔保品為不動產
      AND  SUBSTR(CD2."ClTypeJCIC",1,1) IN ('2')            -- 擔保品為不動產
--    AND  L."ClNo" IS NOT NULL
      AND  NVL(L."LandNo1", 0) > 0
      AND  ( NVL(L."LandNo1", 0)   > 0 OR NVL(L."LandNo2", 0)   > 0 OR
             NVL(B."BdNo1", 0) > 0 OR NVL(B."BdNo2", 0) > 0 )
      AND ( ( CF."ClCode1" NOT IN (1) ) OR
            ( CF."ClCode1" IN (1) AND ( NVL(B."BdNo1", 0) > 0 OR
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
    WHERE to_number(NVL(M."LandOwnedArea",'0')) >= 3
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
         , WK."AreaCode"                         AS "AreaJCICCode"      -- 鄉鎮市區別
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
--    SET   M."EvaAmt" = SUBSTR('00000000' || TRUNC(to_number(M."LVITax")), -8)
--        , M."LVITax" = SUBSTR('00000000' || TRUNC(to_number(M."EvaAmt")), -8)
     SET  M."EvaAmt" = LPAD(M."LVITax",8,'0')
         ,M."LVITax" = LPAD(M."EvaAmt",8,'0')
    WHERE M."DataYM" =  YYYYMM
      AND M."LVITax" <> 'X'
      AND M."EvaAmt" < M."LVITax"
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


    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;
    DBMS_OUTPUT.PUT_LINE('Spend Time: ' ||
        to_number(  to_date(to_char(JOB_END_TIME,'  yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  - to_date(to_char(JOB_START_TIME,'yyyy-mm-dd hh24:mi:ss'), 'yyyy-mm-dd hh24:mi:ss')
                  ) * 86400       || ' Secs' );

    commit;

  END;
END;

--"Work_B092"
--     , ' '      as "ClTypeJCIC"
--     , ' '      as "OwnerId"
--     , ' '      as "EvaAmt"
--     , 0        as "EvaDate"
--     , 0        as "SettingDate"
--     , ' '      as "MonthSettingAmt"     -- varchar2(8)
--     , 0        as "SettingSeq"          -- decimal(1, 0)
--     , ' '      as "PreSettingAmt"       -- varchar2(8)
--     , ' '      as "DispPrice"           -- varchar2(8)
--     , 0        as "IssueEndDate"        -- decimal(5, 0)
--     , ' '      as "CityCode"            -- varchar2(2)
--     , ' '      as "AreaCode"            -- varchar2(2)
--     , ' '      as "IrCode"              -- varchar2(4)
--     , 0        as "LandNo1"             -- decimal(4, 0)
--     , 0        as "LandNo2"             -- decimal(4, 0)
--     , 0        as "BdNo1"               -- decimal(5, 0)
--     , 0        as "BdNo2"               -- decimal(3, 0)
--     , ' '      as "Zip"                 -- varchar2(5)
--     , 0        as "LVITax"              -- decimal(14,2)
--     , 0        as "LVITaxYearMonth"     -- decimal(5, 0)
--     , ' '      as "ContractPrice"       -- varchar2(8)
--     , ' '      as "ContractDate"        -- varchar2(8)
--     , ' '      as "ParkingTypeCode"     -- varchar2(1)
--     , ' '      as "Area"                -- varchar2(9)
--     , 0        as "LandOwnedArea"       -- decimal(14, 2)
--     , 0        as "LineAmt"             -- decimal(14, 0)