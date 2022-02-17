-- 程式功能：維護 JcicB095 每月聯徵不動產擔保品明細-建號附加檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：EXEC "Usp_L8_JcicB095_Upd"(20200420,'999999');
--

DROP TABLE "Work_B095" purge;
CREATE GLOBAL TEMPORARY TABLE "Work_B095"
    (  "ClActNo"       varchar2(50)
     , "ClCode1"       decimal (1, 0)   default 0 not null
     , "ClCode2"       decimal (2, 0)   default 0 not null
     , "ClNo"          decimal (7, 0)   default 0 not null
     , "CityCode"      varchar2(2)
     , "AreaCode"      varchar2(3)
     , "IrCode"        varchar2(4)
     , "BdNo1"         decimal (5, 0)   default 0 not null
     , "BdNo2"         decimal (3, 0)   default 0 not null
    )
    ON COMMIT DELETE ROWS;


CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB095_Upd"
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


    -- 寫入資料 Work_B095    -- 從 JcicB092 擔保品編號重新 JOIN "ClBuilding" 建號
    -- 因為 JcicB092 有條件清掉建號，但是原本 SKL 又有申報
    INSERT INTO "Work_B095"
    SELECT M."ClActNo"                           AS "ClActNo"           -- 擔保品控制編碼
         , to_number(SUBSTR(M."ClActNo",1,1))    AS "ClCode1"           -- 擔保品代號1
         , to_number(SUBSTR(M."ClActNo",2,2))    AS "ClCode2"           -- 擔保品代號2
         , to_number(SUBSTR(M."ClActNo",4,7))    AS "ClNo"              -- 擔保品編號
         , NVL(B."CityCode", ' ')                AS "CityJCICCode"      -- 縣市別
         , NVL(B."AreaCode", ' ')                AS "AreaCode"          -- 鄉鎮市區別
         , NVL(M."IrCode", '0000')               AS "IrCode"            -- 段、小段號  --"ClBuilding"未紀錄, 抓"JcicB092"("ClLand")
         , NVL(B."BdNo1", 0)                     AS "BdNo1"             -- 建號-前五碼
         , NVL(B."BdNo2", 0)                     AS "BdNo2"             -- 建號-後三碼
    FROM   "JcicB092" M
      LEFT JOIN "ClBuilding"  B    ON B."ClCode1" = to_number(SUBSTR(M."ClActNo",1,1))
                                  AND B."ClCode2" = to_number(SUBSTR(M."ClActNo",2,2))
                                  AND B."ClNo"    = to_number(SUBSTR(M."ClActNo",4,7))
    WHERE  M."DataYM" =  YYYYMM
      AND  M."SettingDate" >= 9607  -- 押品設定日期在９６０７０１之後才要報送 (ref:AS400 LN15M1)
      AND  ( NVL(B."BdNo1",0)  > 0 OR NVL(B."BdNo2",0) > 0 )
    GROUP BY M."ClActNo", B."CityCode", B."AreaCode", M."IrCode", B."BdNo1", B."BdNo2"
      ;


    -- 刪除舊資料
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB095');

    DELETE FROM "JcicB095"
    WHERE "DataYM" = YYYYMM
      ;


    -- 寫入資料
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB095');

    INSERT INTO "JcicB095"
    SELECT
           YYYYMM                                AS "DataYM"            -- 資料年月
         , '95'                                  AS "DataType"          -- 資料別
         , '458'                                 AS "BankItem"          -- 總行代號
         , '0001'                                AS "BranchItem"        -- 分行代號
         , ' '                                   AS "Filler4"           -- 空白
         , WK."ClActNo"                          AS "ClActNo"           -- 擔保品控制編碼                VARCHAR2 50
         , NVL(BuildingOwner."OwnerId",' ')      AS "OwnerId"           -- 擔保品所有權人或代表人IDN/BAN VARCHAR2 10
         , NVL("CdCity"."JcicCityCode", ' ')     AS "CityJCICCode"      -- 縣市別                        VARCHAR2 1
         , NVL(TRIM("ClBuilding"."AreaCode"), 0) AS "AreaJCICCode"      -- 鄉鎮市區別                    DECIMAL  2
         , WK."IrCode"                           AS "IrCode"            -- 段、小段號                    VARCHAR2 4
         , NVL(WK."BdNo1", 0)                    AS "BdNo1"             -- 建號-前五碼                   DECIMAL  5
         , NVL(WK."BdNo2", 0)                    AS "BdNo2"             -- 建號-後三碼                   DECIMAL  3
         , to_char(NVL("CdCity"."CityItem",' ')) AS "CityName"          -- 縣市名稱                      VARCHAR2 12
         , to_char(NVL("CdArea"."AreaItem",' ')) AS "AreaName"          -- 鄉鎮市區名稱                  VARCHAR2 12
         , SUBSTRB(NVL(
             TRIM(NVL2("ClBuilding"."Road",      "ClBuilding"."Road",              ''))  ||
             TRIM(NVL2("ClBuilding"."Section",   "ClBuilding"."Section" || '段',   ''))  ||
             TRIM(NVL2("ClBuilding"."Alley",     "ClBuilding"."Alley" || '巷',     ''))  ||
             TRIM(NVL2("ClBuilding"."Lane",      "ClBuilding"."Lane" || '弄',      ''))  ||
             TRIM(NVL("ClBuilding"."Num", ''))  ||
             ( CASE
                 WHEN TRIM(NVL("ClBuilding"."NumDash", '')) IS NOT NULL THEN TRIM(NVL2("ClBuilding"."NumDash", '之' || "ClBuilding"."NumDash", ''))
                 ELSE ''
               END) ||
             ( CASE
                 WHEN TRIM(NVL("ClBuilding"."Num", '')) IS NULL AND TRIM(NVL("ClBuilding"."NumDash", '')) IS NULL THEN ''
                 ELSE '號'
               END) ||
             TRIM(NVL2("ClBuilding"."Floor",     "ClBuilding"."Floor" || '樓',     ''))  ||
             TRIM(NVL2("ClBuilding"."FloorDash", '之' || "ClBuilding"."FloorDash", ''))
           , ' '), 1, 228)
                                                 AS "Addr"              -- 村里/街路/段/巷/弄/號/樓      VARCHAR2 76
         , CASE
             WHEN "ClBuilding"."BdMainUseCode" IS NULL         THEN 'Z'    -- 其他      -- 本欄參考 AS400 LN15M1 對照規則 及 新系統轉檔規則
             WHEN "ClBuilding"."BdMainUseCode" IN ('01')       THEN 'A'    -- 住家用
             WHEN "ClBuilding"."BdMainUseCode" IN ('02')       THEN 'B'    -- 商業用
             WHEN "ClBuilding"."BdMainUseCode" IN ('03')       THEN 'C'    -- 工業用
             WHEN "ClBuilding"."BdMainUseCode" IN ('05')       THEN 'G'    -- 農舍
             WHEN "ClBuilding"."BdMainUseCode" IN ('06')       THEN 'D'    -- 住商用
             WHEN "ClBuilding"."BdMainUseCode" IN ('07')       THEN 'E'    -- 住工用
             WHEN "ClBuilding"."BdMainUseCode" IN ('08')       THEN 'F'    -- 工商用
             ELSE 'Z'                                                      -- 其他
           END                                   AS "BdMainUseCode"     -- 主要用途                      VARCHAR2 1
         , CASE
             WHEN "ClBuilding"."BdMtrlCode" IS NULL              THEN 'Z'    -- 本欄參考 AS400 LN15M1 對照規則
             WHEN "ClBuilding"."BdMtrlCode" IN ('01','04','18')  THEN 'D'
             WHEN "ClBuilding"."BdMtrlCode" IN ('02')            THEN 'E'
             WHEN "ClBuilding"."BdMtrlCode" IN ('03')            THEN 'H'
             WHEN "ClBuilding"."BdMtrlCode" IN ('09','12')       THEN 'C'
             WHEN "ClBuilding"."BdMtrlCode" IN ('11')            THEN 'A'
             WHEN "ClBuilding"."BdMtrlCode" IN ('99')            THEN 'Z'
             ELSE 'Z'
           END                                   AS "BdMtrlCode"        -- 主要建材(結構體)              VARCHAR2 1
--       , CASE
--           WHEN "ClBuilding"."BdSubUsageCode" IS NULL         THEN 'Z'     -- 本欄參考 AS400 LN15M1 對照規則
--           WHEN "ClBuilding"."BdSubUsageCode" IN ('01')       THEN 'A'
--           WHEN "ClBuilding"."BdSubUsageCode" IN ('02')       THEN 'A'
--           WHEN "ClBuilding"."BdSubUsageCode" IN ('03')       THEN 'B'
--           WHEN "ClBuilding"."BdSubUsageCode" IN ('04')       THEN 'Z'
--           ELSE 'Z'
--         END                                   AS "BdSubUsageCode"    -- 附屬建物用途 (後面再處理)     VARCHAR2 6
         , ' '                                   AS "BdSubUsageCode"    -- 附屬建物用途 (後面再處理)     VARCHAR2 6
         , CASE
             WHEN NVL("ClBuilding"."TotalFloor", 0) = 0 THEN 999
             ELSE "ClBuilding"."TotalFloor"
           END                                   AS "TotalFloor"        -- 層數(標的所在樓高)            DECIMAL  3
         , CASE
             WHEN NVL("ClBuilding"."TotalFloor", 0) = 0 AND trim(NVL("ClBuilding"."FloorNo",' ')) IS NULL THEN '999    ' --透天厝
             WHEN NVL("ClBuilding"."TotalFloor", 0) = 0 AND trim(NVL("ClBuilding"."FloorNo",' '))  = '0'  THEN '999    ' --透天厝
             WHEN trim(NVL("ClBuilding"."FloorNo",' ')) IS NULL  THEN '1      '
             ELSE "ClBuilding"."FloorNo"
           END                                   AS "FloorNo"           -- 層次(標的所在樓層)            VARCHAR2 7
         , CASE
             WHEN "ClBuilding"."BdDate" IS NULL    THEN '0880109'  -- (erf:AS400 LN15M1)
             WHEN "ClBuilding"."BdDate" = 0        THEN '0880109'
             WHEN "ClBuilding"."BdDate" < 19810101 THEN '0700101'
             WHEN "ClBuilding"."BdDate" < 19110000 THEN LPAD("ClBuilding"."BdDate",7,'0')
             ELSE LPAD("ClBuilding"."BdDate" - 19110000, 7, '0')
           END                                   AS "BdDate"            -- 建築完成日期(屋齡)            VARCHAR2 7
--       , TRUNC(NVL("ClBuilding"."FloorArea",0)  / 0.3025, 2) +
--         TRUNC(NVL("ClBuilding"."BdSubArea",0)  / 0.3025, 2) +
--         TRUNC(NVL("ClBuildingPublic"."Area",0) / 0.3025, 2)
--                                               AS "TotalArea"         -- 建物總面積                    DECIMAL  10,2
         , 0                                     AS "TotalArea"         -- 建物總面積 (後面再處理)       DECIMAL  10,2
         , TRUNC(NVL("ClBuilding"."FloorArea",0)  / 0.3025, 2)
                                                 AS "FloorArea"         -- 主建物(層次)面積 (後面再處理) DECIMAL  10,2
         , TRUNC(NVL("ClBuilding"."BdSubArea",0)  / 0.3025, 2)
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
        LEFT JOIN "ClBuilding"       ON "ClBuilding"."ClCode1" = WK."ClCode1"
                                    AND "ClBuilding"."ClCode2" = WK."ClCode2"
                                    AND "ClBuilding"."ClNo"    = WK."ClNo"
                                    AND "ClBuilding"."BdNo1"   = WK."BdNo1"
                                    AND "ClBuilding"."BdNo2"   = WK."BdNo2"
        LEFT JOIN "CdCity"           ON to_number("CdCity"."CityCode")  = to_number( NVL(TRIM("ClBuilding"."CityCode"),'0') )
        LEFT JOIN "CdArea"           ON "CdArea"."CityCode"   = "ClBuilding"."CityCode"
                                    AND "CdArea"."AreaCode"   = "ClBuilding"."AreaCode"
        LEFT JOIN "ClBuildingPublic" ON "ClBuildingPublic"."ClCode1" = WK."ClCode1"
                                    AND "ClBuildingPublic"."ClCode2" = WK."ClCode2"
                                    AND "ClBuildingPublic"."ClNo"    = WK."ClNo"
                                    AND "ClBuildingPublic"."PublicBdNo1"   = WK."BdNo1"
                                    AND "ClBuildingPublic"."PublicBdNo2"   = WK."BdNo2"
        LEFT JOIN ( SELECT DISTINCT
                           O."ClCode1", O."ClCode2", O."ClNo"
--                       , FIRST_VALUE(O."OwnerId") OVER ( PARTITION BY O."ClCode1", O."ClCode2", O."ClNo"
--                                                         ORDER BY  O."OwnerRelCode" )  AS "OwnerId"
                         , O."OwnerId"  AS "OwnerId"
                    FROM "ClBuildingOwner" O
                  ) BuildingOwner    ON BuildingOwner."ClCode1" = WK."ClCode1"
                                    AND BuildingOwner."ClCode2" = WK."ClCode2"
                                    AND BuildingOwner."ClNo"    = WK."ClNo"
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

    commit;

    -- 例外處理
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB095_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
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
