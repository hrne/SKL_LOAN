--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ClBuilding_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_ClBuilding_Ins" 
(
    -- 參數
    JOB_START_TIME OUT TIMESTAMP, --程式起始時間
    JOB_END_TIME   OUT TIMESTAMP, --程式結束時間
    INS_CNT        OUT INT,       --新增資料筆數
    ERROR_MSG      OUT VARCHAR2   --錯誤訊息
)
AS
BEGIN
    -- 筆數預設0
    INS_CNT:=0;
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 刪除舊資料
    EXECUTE IMMEDIATE 'ALTER TABLE "ClBuilding" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClBuilding" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClBuilding" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClBuilding"
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,"CdCity"."CityCode"            AS "CityCode"            -- 縣市 VARCHAR2 2 
          ,NVL("CdArea"."AreaCode",CA2."AreaCode")
                                          AS "AreaCode"            -- 鄉鎮市區 VARCHAR2 3 
          ,''                             AS "IrCode"              -- 段小段代碼 VARCHAR2 4 
          ,TRIM(TO_SINGLE_BYTE(S2."HGTAD3"))
                                          AS "Road"                -- 路名 NVARCHAR2 40 
          ,''                             AS "Section"             -- 段 VARCHAR2 5 
          ,''                             AS "Alley"               -- 巷 VARCHAR2 5 
          ,''                             AS "Lane"                -- 弄 VARCHAR2 5 
          ,''                             AS "Num"                 -- 號 VARCHAR2 5 
          ,''                             AS "NumDash"             -- 號之 VARCHAR2 5 
          ,''                             AS "Floor"               -- 樓 VARCHAR2 5 
          ,''                             AS "FloorDash"           -- 樓之 VARCHAR2 5 
          ,LPAD(S2."HGTMHN",5,'0')        AS "BdNo1"               -- 建號 VARCHAR2 5 
          ,LPAD(S2."HGTMHN2",3,'0')       AS "BdNo2"               -- 建號(子號) VARCHAR2 3 
          ,TRIM(TO_SINGLE_BYTE(S2."LGTADR"))
                                          AS "BdLocation"          -- 建物門牌 VARCHAR2 150 
          ,CASE
             WHEN S2."HGTGUS" = '1'            THEN '01'
             WHEN S2."HGTGUS" IN ('2','3','C') THEN '02'
             WHEN S2."HGTGUS" IN ('4','A')     THEN '03'
             WHEN S2."HGTGUS" = 'E'            THEN '04'
             WHEN S2."HGTGUS" = '5'            THEN '05'
             WHEN S2."HGTGUS" = '6'            THEN '06'
             WHEN S2."HGTGUS" = 'B'            THEN '07'
             WHEN S2."HGTGUS" = 'D'            THEN '08'
             WHEN S2."HGTGUS" = '7'            THEN '09'
             WHEN S2."HGTGUS" = 'F'            THEN '10'
             WHEN S2."HGTGUS" = 'G'            THEN '11'
             WHEN S2."HGTGUS" = 'H'            THEN '12'
             WHEN S2."HGTGUS" = '8'            THEN '13'
             WHEN S2."HGTGUS" = 'Y'            THEN '14'
           ELSE LPAD(S2."HGTGUS",2,'0') END
                                          AS "BdMainUseCode"       -- 建物主要用途 VARCHAR2 2 
          ,'1'                            AS "BdUsageCode"         -- 建物使用別 VARCHAR2 1 
          ,CASE
             WHEN LPAD(S2."HGTSTR",2,'0') = '01' -- 磚水泥
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '02' -- 鋼筋水泥
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '03' -- 鋼骨水泥
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '04' -- 磚造
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '05' -- 鋼骨鋼筋混
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '06' -- 鋼筋混凝土
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '07' -- 鋼造
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '08' -- 混凝土造
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '09' -- 磚鐵皮
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '10' -- 石造
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '11' -- 木造
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '12' -- 鐵皮造
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '13' -- 壁式預鑄鋼
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '14' -- 預力混凝土
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '15' -- 土造
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '16' -- 土石造
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '17' -- 土磚石混合
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '18' -- 加強磚
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '19' -- 竹造
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '20' -- 鋼筋混凝土
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '21' -- 土木造
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '22' -- 鋁架造
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '30' -- 見使用執照
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '31' -- 見其它登記
             THEN LPAD(S2."HGTSTR",2,'0')
             WHEN LPAD(S2."HGTSTR",2,'0') = '99' -- 其他
             THEN LPAD(S2."HGTSTR",2,'0')
           ELSE '99' END                  AS "BdMtrlCode"          -- 建物主要建材 VARCHAR2 2 
          ,''                             AS "BdTypeCode"          -- 建物類別 VARCHAR2 2 
          ,NVL(S2."HGTFLR",0)             AS "TotalFloor"          -- 總樓層 DECIMAL 3 
          ,TRIM(S2."HGTFOR")              AS "FloorNo"             -- 擔保品所在樓層 VARCHAR2 7
          ,NVL(S2."HGTMHS",0)             AS "FloorArea"           -- 擔保品所在樓層面積 DECIMAL 8 2
          ,NVL(S2."LGTUNT",0)             AS "EvaUnitPrice"        -- 鑑價單價/坪 DECIMAL 16 2
          ,CASE
             WHEN LPAD(S2."HGTROF",2,'0') = '01' -- 平屋頂
             THEN '01'
             WHEN LPAD(S2."HGTROF",2,'0') = '02' -- 瓦屋頂
             THEN '02'
             WHEN LPAD(S2."HGTROF",2,'0') = '03' -- 石棉板屋頂
             THEN '03'
             WHEN LPAD(S2."HGTROF",2,'0') = '04' -- 鐵皮屋頂
             THEN '04'
             WHEN LPAD(S2."HGTROF",2,'0') = '07' -- 木板屋頂
             THEN '05'
             WHEN LPAD(S2."HGTROF",2,'0') = '13' -- 石棉瓦屋頂
             THEN '06'
             WHEN LPAD(S2."HGTROF",2,'0') = '99' -- 其他
             THEN '07'
           ELSE '07' END                  AS "RoofStructureCode"   -- 屋頂結構 VARCHAR2 2 
          ,NVL(S2."HGTCPE",0)             AS "BdDate"              -- 建築完成日期 decimald 8 
          ,LPAD(S2."HGTAUS",2,'0')        AS "BdSubUsageCode"      -- 附屬建物用途 VARCHAR2 2 
          ,NVL(S2."HGTADS",0)             AS "BdSubArea"           -- 附屬建物面積 DECIMAL 8 2
          ,S2."SALID1"                    AS "SellerId"            -- 賣方統編 VARCHAR2 10 
          ,' '                            AS "SellerName"          -- 賣方姓名 VARCHAR2 10 
          ,NVL(S2."BUYAMT",0)             AS "ContractPrice"       -- 買賣契約價格 DECIMAL 16 2
          ,NVL(S2."BUYDAT",0)             AS "ContractDate"        -- 買賣契約日期 decimald 8 
          -- HGTCAP	1	平道平面位
          -- HGTCAP	2	平道機械位
          -- HGTCAP	3	械道平面位
          -- HGTCAP	4	械道機械位
          -- HGTCAP	5	無車位
          ,CASE
             WHEN S2."HGTCAP" = '1' -- 平道平面位
             THEN '1' -- 坡道平面車位
             WHEN S2."HGTCAP" = '2' -- 平道機械位
             THEN '3' -- 坡道機械車位
             WHEN S2."HGTCAP" = '3' -- 械道平面位
             THEN '2' -- 機械平面車位
             WHEN S2."HGTCAP" = '4' -- 械道機械位
             THEN '4' -- 機械機械車位
             WHEN S2."HGTCAP" = '5' -- 無車位
             THEN '0' -- 無車位
           ELSE '0' END                   AS "ParkingTypeCode"     -- 停車位形式 VARCHAR2 1 
          ,0                              AS "ParkingArea"         -- 登記面積(坪) DECIMAL 16 2
          ,CASE
             WHEN NVL(S2."HGTCIP",'0') = '1' THEN 'Y'
           ELSE 'N' END                   AS "ParkingProperty"     -- 獨立產權車位註記 VARCHAR2 1 
          ,''                             AS "HouseTaxNo"          -- 房屋稅籍號碼 VARCHAR2 12 
          ,NVL(S2."HGTGTD",0)             AS "HouseBuyDate"        -- 房屋取得日期 decimald 8 
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
    FROM "ClNoMapping" S1
    LEFT JOIN "LA$HGTP" S2 ON S2."GDRID1" = S1."GDRID1"
                          AND S2."GDRID2" = S1."GDRID2"
                          AND S2."GDRNUM" = S1."GDRNUM"
                          AND S2."LGTSEQ" = S1."LGTSEQ"
    LEFT JOIN "CdCity" ON NVL(S2."HGTAD1",' ') != ' '
                      AND "CdCity"."CityItem" = CASE
                                                  WHEN S2."HGTAD1" = '　南投' THEN '南投縣'
                                                  WHEN S2."HGTAD1" = '　台中' THEN '台中市'
                                                  WHEN S2."HGTAD1" = '　台北' THEN '台北市'
                                                  WHEN S2."HGTAD1" = '　基隆' THEN '基隆市'
                                                  WHEN S2."HGTAD1" = '　彰化' THEN '彰化縣'
                                                  WHEN S2."HGTAD1" = '　花蓮' THEN '花蓮縣'
                                                  WHEN S2."HGTAD1" = '　雲林' THEN '雲林縣'
                                                  WHEN S2."HGTAD1" = '　高雄' THEN '高雄市'
                                                  WHEN S2."HGTAD1" = '台中縣' THEN '台中市'
                                                  WHEN S2."HGTAD1" = '台北縣' THEN '新北市'
                                                  WHEN S2."HGTAD1" = '台南縣' THEN '台南市'
                                                  WHEN S2."HGTAD1" = '嘉義嘉' THEN '嘉義市'
                                                  WHEN S2."HGTAD1" = '高雄縣' THEN '高雄市'
                                                ELSE S2."HGTAD1" END
    LEFT JOIN "CdArea" ON "CdArea"."CityCode" = "CdCity"."CityCode"
                      AND "CdArea"."AreaItem" = S2."HGTAD2"
                      AND NVL("CdCity"."CityCode",' ') != ' '
    LEFT JOIN "CdArea" CA2 ON CA2."CityCode" = "CdCity"."CityCode"
                          AND NVL(S2."HGTAD2",' ') != ' '
                          AND SUBSTR(CA2."AreaItem",0,2) = SUBSTR(S2."HGTAD2",0,2) 
                          AND NVL("CdCity"."CityCode",' ') != ' '
    WHERE S1."GDRID1" = '1' -- 只撈建物
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 把地區別更新回ClMain
    MERGE INTO "ClMain" T1
    USING (SELECT S1."ClCode1"
                 ,S1."ClCode2"
                 ,S1."ClNo"
                 ,S1."CityCode"
                 ,S1."AreaCode"
           FROM "ClBuilding" S1
           LEFT JOIN "ClMain" CM ON CM."ClCode1" = S1."ClCode1"
                                AND CM."ClCode2" = S1."ClCode2"
                                AND CM."ClNo"    = S1."ClNo"
           WHERE CASE
                   WHEN NVL(S1."CityCode",' ') != ' '
                        AND NVL(CM."CityCode",' ') = ' ' -- ClMain的CityCode為空,更新
                   THEN 1
                   WHEN S1."ClCode1" = 2 -- 若是土地資料 下列條件跳過
                   THEN 0
                   WHEN NVL(S1."CityCode",' ') != ' '
                        AND NVL(CM."CityCode",' ') != ' ' 
                        AND NVL(S1."CityCode",' ') != NVL(CM."CityCode",' ') -- ClMain的CityCode與ClBuilding不一致,更新
                   THEN 1 
                   WHEN NVL(S1."CityCode",' ') != ' '
                        AND NVL(CM."CityCode",' ') != ' ' 
                        AND NVL(S1."CityCode",' ') = NVL(CM."CityCode",' ') 
                        AND NVL(S1."AreaCode",' ') != ' ' 
                        AND NVL(S1."AreaCode",' ') != NVL(CM."AreaCode",' ') -- ClMain的AreaCode與ClBuilding不一致,更新
                   THEN 1 
                 ELSE 0
                 END = 1
          ) SC1
    ON (    SC1."ClCode1" = T1."ClCode1"
        AND SC1."ClCode2" = T1."ClCode2"
        AND SC1."ClNo"    = T1."ClNo"
      )
    WHEN MATCHED THEN UPDATE SET
    T1."CityCode" = SC1."CityCode"
    , T1."AreaCode" = SC1."AreaCode"
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClBuilding_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
    -- DBMS_OUTPUT.PUT_LINE(dbms_utility.format_error_backtrace);

END;





/
