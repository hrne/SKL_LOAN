CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB095_Upd"
(
-- 程式功能：維護 JcicB095 每月聯徵不動產擔保品明細-建號附加檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB095_Upd"(20200430,'System');
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
    WITH "Work_B095_All" AS (
    SELECT
           M."ClActNo"                           AS "MainClActNo"       -- 主要擔保品控制編碼
         , CF."ClCode1"                          AS "ClCode1"           -- 擔保品代號1
         , CF."ClCode2"                          AS "ClCode2"           -- 擔保品代號2
         , CF."ClNo"                             AS "ClNo"              -- 擔保品編號
         , CASE WHEN TRIM(B."CityCode") IS NOT NULL THEN B."CityCode"
                ELSE NVL(TRIM(L."CityCode"),' ')
           END                                   AS "CityCode"          -- 縣市別
         , CASE WHEN TRIM(B."AreaCode") IS NOT NULL THEN B."AreaCode"
                ELSE NVL(TRIM(L."AreaCode"),' ')
           END                                   AS "AreaCode"          -- 鄉鎮市區別
         , CASE
             WHEN TRIM(B."IrCode") IS NOT NULL THEN SUBSTR('0000' || B."IrCode", -4)
             WHEN TRIM(L."IrCode") IS NOT NULL THEN SUBSTR('0000' || L."IrCode", -4)
             ELSE '0001'
           END                                   AS "IrCode"            -- 段、小段號
         , NVL(B."BdNo1", 0)                     AS "BdNo1"             -- 建號-前五碼
         , NVL(B."BdNo2", 0)                     AS "BdNo2"             -- 建號-後三碼
    FROM   "JcicB090" M
      LEFT JOIN "CdCl"         ON "CdCl"."ClCode1"  = to_number(SUBSTR(M."ClActNo",1,1))
                              AND "CdCl"."ClCode2"  = to_number(SUBSTR(M."ClActNo",2,2))
      LEFT JOIN "ClFac"  CF    ON CF."CustNo"     = to_number(SUBSTR(M."FacmNo",1,7))
                              AND CF."FacmNo"     = to_number(SUBSTR(M."FacmNo",8,3))  -- 關聯所有擔保品編號(含主要擔保品)
      LEFT JOIN "ClImm"  CI    ON CI."ClCode1"    = CF."ClCode1"
                              AND CI."ClCode2"  = CF."ClCode2"
                              AND CI."ClNo"     = CF."ClNo"
      LEFT JOIN "ClBuilding"  B     ON B."ClCode1" = CF."ClCode1"
                                   AND B."ClCode2" = CF."ClCode2"
                                   AND B."ClNo"    = CF."ClNo"
      LEFT JOIN "ClLand"  L    ON L."ClCode1" = CF."ClCode1"
                              AND L."ClCode2" = CF."ClCode2"
                              AND L."ClNo"    = CF."ClNo"
                              AND L."LandSeq" = 1
    WHERE  M."DataYM" =  YYYYMM
      AND  SUBSTR("CdCl"."ClTypeJCIC",1,1) IN ('2')   -- 主要擔保品為不動產
      AND  NVL(CI."SettingDate",0) >= 20070701        -- 押品設定日期在９６０７０１之後才要報送 (ref:AS400 LN15M1)
      AND  ( NVL(TRIM(B."BdNo1"),0)  > 0 OR NVL(TRIM(B."BdNo2"),0) > 0 )
    )
	  , "Work_B095" AS (
    SELECT "MainClActNo"       -- 主要擔保品控制編碼
         , "ClCode1"           -- 擔保品代號1
         , "ClCode2"           -- 擔保品代號2
         , "ClNo"              -- 擔保品編號
         , "CityCode"          -- 縣市別
         , "AreaCode"          -- 鄉鎮市區別
         , "IrCode"            -- 段、小段號
         , "BdNo1"             -- 建號-前五碼
         , "BdNo2"             -- 建號-後三碼
         , ROW_NUMBER()
           OVER (
            PARTITION BY "MainClActNo"
                       , "CityCode"
                       , "AreaCode"
                       , "IrCode"
                       , "BdNo1"
                       , "BdNo2"
            ORDER BY "ClCode1" 
                   , "ClCode2"
                   , "ClNo"
           ) AS "Seq"                  
    FROM "Work_B095_All"
    )


    SELECT DISTINCT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '95'                                  AS "DataType"          -- 資料別
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , ' '                                   AS "Filler4"           -- 空白
         , WK."MainClActNo"                      AS "ClActNo"           -- 擔保品控制編碼 (主要擔保品)   VARCHAR2 50
         , CASE
             WHEN BuildingOwner."OwnerId"  IS NOT NULL THEN BuildingOwner."OwnerId"
             WHEN LandOwner."OwnerId"      IS NOT NULL THEN LandOwner."OwnerId"
             WHEN BuPublicOwner."OwnerId"  IS NOT NULL THEN BuPublicOwner."OwnerId"
             ELSE ' '
           END                                   AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN
         , NVL("CdArea"."JcicCityCode", ' ')     AS "CityCode"          -- 縣市別
--         , NVL("CdCity"."JcicCityCode", ' ')     AS "CityCode"          -- 縣市別
         , NVL(TRIM(WK."AreaCode"),0)            AS "AreaCode"          -- 鄉鎮市區別
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
                 ELSE ''
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
             WHEN B."BdDate" < 19810101 THEN '0700101'
             WHEN B."BdDate" < 19110000 THEN LPAD(B."BdDate",7,'0')
             ELSE LPAD(B."BdDate" - 19110000, 7, '0')
           END                                   AS "BdDate"            -- 建築完成日期(屋齡)            VARCHAR2 7
         , 0                                     AS "TotalArea"         -- 建物總面積 (後面再處理)       DECIMAL  10,2
         , TRUNC(NVL(B."FloorArea",0)  / 0.3025, 2)
                                                 AS "FloorArea"         -- 主建物(層次)面積 (後面再處理) DECIMAL  10,2
         , TRUNC(NVL(B."BdSubArea",0)  / 0.3025, 2)
                                                 AS "BdSubArea"         -- 附屬建物面積 (後面再處理)     DECIMAL  10,2
         , TRUNC(NVL("ClBuildingPublic"."Area",0) / 0.3025, 2)
                                                 AS "PublicArea"        -- 共同部份持分面積 (後面再處理) DECIMAL  10,2
         , ' '                                   AS "Filler33"          -- 空白                          VARCHAR2 44
         , YYYYMM - 191100                       AS "JcicDataYM"        -- 資料所屬年月                  DECIMAL  5
         , JOB_START_TIME                        AS "CreateDate"        -- 建檔日期時間
         , EmpNo                                 AS "CreateEmpNo"       -- 建檔人員
         , JOB_START_TIME                        AS "LastUpdate"        -- 最後更新日期時間
         , EmpNo                                 AS "LastUpdateEmpNo"   -- 最後更新人員
    FROM   "Work_B095" WK
      LEFT JOIN "CdCity"       ON "CdCity"."CityCode"  = WK."CityCode"
      LEFT JOIN "CdArea"       ON "CdArea"."CityCode"  = WK."CityCode"
                              AND "CdArea"."AreaCode"  = WK."AreaCode"
      LEFT JOIN "ClMain" CM    ON CM."ClCode1"  = WK."ClCode1"
                              AND CM."ClCode2"  = WK."ClCode2"
                              AND CM."ClNo"     = WK."ClNo"
      LEFT JOIN "ClImm" CI    ON  CI."ClCode1"  = CM."ClCode1"
                              AND CI."ClCode2"  = CM."ClCode2"
                              AND CI."ClNo"     = CM."ClNo"
      LEFT JOIN "ClBuilding"  B     ON B."ClCode1" = CM."ClCode1"
                                   AND B."ClCode2" = CM."ClCode2"
                                   AND B."ClNo"    = CM."ClNo"
      LEFT JOIN "ClBuildingPublic"   ON "ClBuildingPublic"."ClCode1" = B."ClCode1"
                                    AND "ClBuildingPublic"."ClCode2" = B."ClCode2"
                                    AND "ClBuildingPublic"."ClNo"    = B."ClNo"
                                    AND "ClBuildingPublic"."PublicBdNo1"   = B."BdNo1"
                                    AND "ClBuildingPublic"."PublicBdNo2"   = B."BdNo2"
      LEFT JOIN "ClLand"  L    ON L."ClCode1" = CM."ClCode1"
                              AND L."ClCode2" = CM."ClCode2"
                              AND L."ClNo"    = CM."ClNo"
                              AND L."LandSeq" = 1
      LEFT JOIN ( SELECT DISTINCT
                         O."ClCode1", O."ClCode2", O."ClNo", O."OwnerPart", O."OwnerTotal"
                       , C."CustId"  AS "OwnerId"
                  FROM "ClBuildingOwner" O
                    LEFT JOIN "CustMain" C  ON  C."CustUKey"  = O."OwnerCustUKey"
                ) BuildingOwner    ON BuildingOwner."ClCode1" = CM."ClCode1"
                                  AND BuildingOwner."ClCode2" = CM."ClCode2"
                                  AND BuildingOwner."ClNo"    = CM."ClNo"
      LEFT JOIN ( SELECT DISTINCT
                         O."ClCode1", O."ClCode2", O."ClNo", O."OwnerPart", O."OwnerTotal"
                       , C."CustId"  AS "OwnerId"
                  FROM "ClLandOwner" O
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
    WHERE WK."Seq" = 1  
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


--         , ' '     AS "IrCode"            -- 段、小段號                VARCHAR2 4
--         , 0       AS "BdNo1"             -- 建號-前五碼               DECIMAL  5
--         , 0       AS "BdNo2"             -- 建號-後三碼               DECIMAL  3
--         , ' '     AS "CityName"          -- 縣市名稱                  VARCHAR2 12
--         , ' '     AS "AreaName"          -- 鄉鎮市區名稱              VARCHAR2 12
--         , ' '     AS "Addr"              -- 村里/街路/段/巷/弄/號/樓  VARCHAR2 76
--         , ' '     AS "BdMainUseCode"     -- 主要用途                  VARCHAR2 1
--         , ' '     AS "BdMtrlCode"        -- 主要建材(結構體)          VARCHAR2 1
--         , ' '     AS "BdSubUsageCode"    -- 附屬建物用途              VARCHAR2 6
--         , 0       AS "TotalFloor"        -- 層數(標的所在樓高)        DECIMAL  3
--         , ' '     AS "FloorNo"           -- 層次(標的所在樓層)        VARCHAR2 7
--         , ' '     AS "BdDate"            -- 建築完成日期(屋齡)        VARCHAR2 7
--         , 0       AS "TotalArea"         -- 建物總面積                DECIMAL  10,2
--         , 0       AS "FloorArea"         -- 主建物(層次)面積          DECIMAL  10,2
--         , 0       AS "BdSubArea"         -- 附屬建物面積              DECIMAL  10,2
--         , 0       AS "PublicArea"        -- 共同部份持分面積 --???    DECIMAL  10,2
