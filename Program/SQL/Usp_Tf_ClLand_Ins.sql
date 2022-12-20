CREATE OR REPLACE PROCEDURE "Usp_Tf_ClLand_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClLand" DISABLE PRIMARY KEY CASCADE'; 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClLand" DROP STORAGE'; 
    EXECUTE IMMEDIATE 'ALTER TABLE "ClLand" ENABLE PRIMARY KEY'; 
 
    -- 寫入資料 
    INSERT INTO "ClLand" 
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1  
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2  
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7  
          -- 純土地固定擺0 
          ,0                              AS "LandSeq"             -- 土地序號 DECIMAL 3 
          ,NVL(C1."CityCode",' ')         AS "CityCode"            -- 縣市 VARCHAR2 2  
          ,NVL(C1."AreaCode",' ')         AS "AreaCode"            -- 鄉鎮市區 VARCHAR2 3  
          ,NVL(LS."IrCode",' ')           AS "IrCode"              -- 段小段代碼 VARCHAR2 5  
          ,LPAD(REPLACE(TRIM(S2."LGTNM1"),'-',''),4,'0') 
                                          AS "LandNo1"             -- 地號 VARCHAR2 4  
          ,LPAD(REPLACE(TRIM(S2."LGTNM2"),'-',''),4,'0') 
                                          AS "LandNo2"             -- 地號(子號) VARCHAR2 4  
          ,TRIM(u'' || NVL(C1."CityItem",'') || NVL(C1."AreaItem",'') || 
                CASE 
                  WHEN NVL(S2."LGTSGM",' ') != ' ' AND INSTR(S2."LGTSGM",'段') > 0 
                    AND S2."LGTSGM" = '奉口段' 
                  THEN '唪口段' 
                  WHEN NVL(S2."LGTSGM",' ') != ' ' 
                    AND S2."LGTSGM" = '奉口' 
                  THEN '唪口段' 
                  WHEN NVL(S2."LGTSGM",' ') != ' ' AND INSTR(S2."LGTSGM",'段') > 0 
                    AND S2."LGTSGM" = '犁和段' 
                  THEN '犂和段' 
                  WHEN NVL(S2."LGTSGM",' ') != ' ' 
                    AND S2."LGTSGM" = '犁和' 
                  THEN '犂和段' 
                  WHEN NVL(S2."LGTSGM",' ') != ' ' AND INSTR(S2."LGTSGM",'段') > 0 
                  THEN S2."LGTSGM" 
                  WHEN NVL(S2."LGTSGM",' ') != ' ' 
                  THEN S2."LGTSGM" || '段' 
                ELSE '' END || 
                CASE 
                 WHEN NVL(S2."LGTSSG",' ') != ' 'AND INSTR(S2."LGTSSG",'小段') > 0 
                 THEN S2."LGTSSG" 
                 WHEN NVL(S2."LGTSSG",' ') != ' ' 
                 THEN S2."LGTSSG" || '小段' 
                ELSE '' END  || 
                '，地號'  
                || LPAD(REPLACE(TRIM(S2."LGTNM1"),'-',''),4,'0') 
                || '-' 
                || LPAD(REPLACE(TRIM(S2."LGTNM2"),'-',''),4,'0') 
               )                          AS "LandLocation"        -- 土地座落 NVARCHAR2 150  
          ,S2."LGTORY"                    AS "LandCode"            -- 地目 VARCHAR2 2  
          ,NVL(S2."LGTSQM",0)             AS "Area"                -- 面積 DECIMAL 9 2 
          ,CASE 
             WHEN S2."LGTUSE" = '1'  THEN '10' 
             WHEN S2."LGTUSE" = '10' THEN '02' 
             WHEN S2."LGTUSE" = '11' THEN '04' 
             WHEN S2."LGTUSE" = '12' THEN '05' 
             WHEN S2."LGTUSE" = '13' THEN '06' 
             WHEN S2."LGTUSE" = '14' THEN '07' 
             WHEN S2."LGTUSE" = '15' THEN '08' 
             WHEN S2."LGTUSE" = '16' THEN '09' 
             WHEN S2."LGTUSE" = '17' THEN '12' 
             WHEN S2."LGTUSE" = '18' THEN '13' 
             WHEN S2."LGTUSE" = '19' THEN '14' 
             WHEN S2."LGTUSE" = '2'  THEN '11' 
             WHEN S2."LGTUSE" = '20' THEN '15' 
             WHEN S2."LGTUSE" = '21' THEN '16' 
             WHEN S2."LGTUSE" = '22' THEN '17' 
             WHEN S2."LGTUSE" = '23' THEN '18' 
             WHEN S2."LGTUSE" = '24' THEN '19' 
             WHEN S2."LGTUSE" = '25' THEN '20' 
             WHEN S2."LGTUSE" = '26' THEN '21' 
             WHEN S2."LGTUSE" = '27' THEN '22' 
             WHEN S2."LGTUSE" = '28' THEN '23' 
             WHEN S2."LGTUSE" = '29' THEN '25' 
             WHEN S2."LGTUSE" = '3'  THEN '03' 
             WHEN S2."LGTUSE" = '30' THEN '26' 
             WHEN S2."LGTUSE" = '31' THEN '27' 
             WHEN S2."LGTUSE" = '32' THEN '28' 
             WHEN S2."LGTUSE" = '33' THEN '29' 
             WHEN S2."LGTUSE" = '34' THEN '30' 
             WHEN S2."LGTUSE" = '35' THEN '31' 
             WHEN S2."LGTUSE" = '36' THEN '32' 
             WHEN S2."LGTUSE" = '37' THEN '33' 
             WHEN S2."LGTUSE" = '38' THEN '34' 
             WHEN S2."LGTUSE" = '39' THEN '35' 
             WHEN S2."LGTUSE" = '4'  THEN '24' 
             WHEN S2."LGTUSE" = '40' THEN '36' 
             WHEN S2."LGTUSE" = '41' THEN '37' 
             WHEN S2."LGTUSE" = '42' THEN '38' 
             WHEN S2."LGTUSE" = '43' THEN '39' 
             WHEN S2."LGTUSE" = '44' THEN '40' 
             WHEN S2."LGTUSE" = '45' THEN '41' 
             WHEN S2."LGTUSE" = '46' THEN '42' 
             WHEN S2."LGTUSE" = '47' THEN '43' 
             WHEN S2."LGTUSE" = '48' THEN '44' 
             WHEN S2."LGTUSE" = '49' THEN '45' 
             WHEN S2."LGTUSE" = '5'  THEN '03' 
             WHEN S2."LGTUSE" = '50' THEN '46' 
             WHEN S2."LGTUSE" = '51' THEN '47' 
             WHEN S2."LGTUSE" = '52' THEN '48' 
             WHEN S2."LGTUSE" = '53' THEN '49' 
             WHEN S2."LGTUSE" = '54' THEN '50' 
             WHEN S2."LGTUSE" = '55' THEN '51' 
             WHEN S2."LGTUSE" = '56' THEN '52' 
             WHEN S2."LGTUSE" = '57' THEN '53' 
             WHEN S2."LGTUSE" = '58' THEN '54' 
             WHEN S2."LGTUSE" = '59' THEN '55' 
             WHEN S2."LGTUSE" = '6'  THEN '03' 
             WHEN S2."LGTUSE" = '60' THEN '56' 
             WHEN S2."LGTUSE" = '61' THEN '57' 
             WHEN S2."LGTUSE" = '62' THEN '58' 
             WHEN S2."LGTUSE" = '63' THEN '59' 
             WHEN S2."LGTUSE" = '64' THEN '60' 
             WHEN S2."LGTUSE" = '65' THEN '61' 
             WHEN S2."LGTUSE" = '7'  THEN '03' 
             WHEN S2."LGTUSE" = '8'  THEN '03' 
             WHEN S2."LGTUSE" = '9'  THEN '01' 
           ELSE '24' END                  AS "LandZoningCode"      -- 土地使用區分 VARCHAR2 2  
          ,CASE 
             WHEN S2."LGTTYP" = 'F' THEN '19' 
             WHEN S2."LGTTYP" = 'G' THEN '01' 
             WHEN S2."LGTTYP" = 'H' THEN '02' 
             WHEN S2."LGTTYP" = 'I' THEN '03' 
             WHEN S2."LGTTYP" = 'J' THEN '04' 
             WHEN S2."LGTTYP" = 'K' THEN '05' 
             WHEN S2."LGTTYP" = 'L' THEN '17' 
             WHEN S2."LGTTYP" = 'M' THEN '18' 
             WHEN S2."LGTTYP" = 'N' THEN '15' 
             WHEN S2."LGTTYP" = 'O' THEN '06' 
             WHEN S2."LGTTYP" = 'P' THEN '16' 
             WHEN S2."LGTTYP" = 'Q' THEN '07' 
             WHEN S2."LGTTYP" = 'R' THEN '08' 
             WHEN S2."LGTTYP" = 'S' THEN '09' 
             WHEN S2."LGTTYP" = 'T' THEN '10' 
             WHEN S2."LGTTYP" = 'U' THEN '11' 
             WHEN S2."LGTTYP" = 'V' THEN '12' 
             WHEN S2."LGTTYP" = 'W' THEN '13' 
           ELSE '20' END                  AS "LandUsageType"       -- 使用地類別 VARCHAR2 2  
          ,''                             AS "LandUsageCode"       -- 土地使用別 VARCHAR2 1  
          ,NVL(S2."LGTVAL",0)             AS "PostedLandValue"     -- 公告土地現值 DECIMAL 16 2 
          ,CASE
             -- 2022-12-20 Wei 新增 舊資料有392612以上的資料
             WHEN NVL(S2."LGTVYM",0) >= (191100 * 2)
             THEN NVL(S2."LGTVYM",0) - 191100
           ELSE NVL(S2."LGTVYM",0) END    AS "PostedLandValueYearMonth" -- 公告土地現值年月 DECIMAL 6  
          ,NVL(S2."LGTTYR",0)             AS "TransferedYear"      -- 移轉年度 DECIMAL 4  
          ,NVL(S2."LGTPTA",0)             AS "LastTransferedAmt"   -- 前次移轉金額 DECIMAL 16 2 
          ,NVL(S2."LGTTAX",0)             AS "LVITax"              -- 土地增值稅 DECIMAL 16 2 
          ,NVL(S2."LGTTAY",0)             AS "LVITaxYearMonth"     -- 土地增值稅年月 DECIMAL 6  
          ,NVL(S2."LGTUNT",0)             AS "EvaUnitPrice"        -- 鑑價單價/坪 DECIMAL 16 2 
          ,0                              AS "LandRentStartDate"   -- 土地租約起日 decimald 8  
          ,0                              AS "LandRentEndDate"     -- 土地租約到期日 decimald 8  
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE   
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE   
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
    FROM "ClNoMapping" S1 
    LEFT JOIN "LA$LGTP" S2 ON S2."GDRID1" = S1."GDRID1" 
                          AND S2."GDRID2" = S1."GDRID2" 
                          AND S2."GDRNUM" = S1."GDRNUM" 
                          AND CASE 
                                WHEN S1."GDRID1" = 1 THEN S1."LGTSEQ" 
                              ELSE S2."LGTSEQ" END 
                              = S1."LGTSEQ" 
    LEFT JOIN ( SELECT CITY."CityCode" 
                      ,CITY."CityItem" 
                      ,AREA."AreaCode" 
                      ,AREA."AreaItem" 
                FROM "CdCity" CITY 
                LEFT JOIN "CdArea" AREA ON AREA."CityCode" = CITY."CityCode" 
              ) C1 ON C1."CityItem" LIKE CASE 
                                           WHEN S2."LGTCTY" = '　南投' THEN '南投縣' 
                                           WHEN S2."LGTCTY" = '　台中' THEN '台中市' 
                                           WHEN S2."LGTCTY" = '　台北' THEN '台北市' 
                                           WHEN S2."LGTCTY" = '　基隆' THEN '基隆市' 
                                           WHEN S2."LGTCTY" = '　彰化' THEN '彰化縣' 
                                           WHEN S2."LGTCTY" = '　花蓮' THEN '花蓮縣' 
                                           WHEN S2."LGTCTY" = '　雲林' THEN '雲林縣' 
                                           WHEN S2."LGTCTY" = '　高雄' THEN '高雄市' 
                                           WHEN S2."LGTCTY" = '台中縣' THEN '台中市' 
                                           WHEN S2."LGTCTY" = '台北縣' THEN '新北市' 
                                           WHEN S2."LGTCTY" = '台南縣' THEN '台南市' 
                                           WHEN S2."LGTCTY" = '嘉義嘉' THEN '嘉義市' 
                                           WHEN S2."LGTCTY" = '高雄縣' THEN '高雄市' 
                                         ELSE S2."LGTCTY" END || '%' 
                  AND NVL(S2."LGTCTY",' ') != ' ' 
                  AND CASE 
                        WHEN NVL(S2."LGTTWN",' ') != ' ' 
                             AND C1."AreaItem" LIKE CASE 
                                                      WHEN S2."LGTTWN" = '頭份鎮' THEN '頭份' 
                                                      WHEN S2."LGTCTY" = '嘉義' 
                                                           AND S2."LGTTWN" IN ('東區','西區') 
                                                      THEN '嘉義' 
                                                      WHEN S2."LGTCTY" = '新竹' 
                                                           AND S2."LGTTWN" IN ('北區','香山區') 
                                                      THEN '新竹' 
                                                    ELSE S2."LGTTWN" END || '%' 
                        THEN 1 
                        WHEN NVL(S2."LGTTWN",' ') = ' ' 
                             AND NVL(S2."LGTCTY",' ') != ' ' 
                             AND C1."AreaItem" LIKE S2."LGTCTY" || '%' 
                        THEN 1 
                      ELSE 0 END = 1 
    LEFT JOIN "CdLandSection" LS ON LS."CityCode" = C1."CityCode" 
                                AND LS."AreaCode" = C1."AreaCode" 
                                AND LS."IrItem" = CASE 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' AND INSTR(S2."LGTSGM",'段') > 0 
                                                         AND S2."LGTSGM" = '奉口段' 
                                                    THEN '唪口段' 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' 
                                                         AND S2."LGTSGM" = '奉口' 
                                                    THEN '唪口段' 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' AND INSTR(S2."LGTSGM",'段') > 0 
                                                         AND S2."LGTSGM" = '犁和段' 
                                                    THEN '犂和段' 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' 
                                                         AND S2."LGTSGM" = '犁和' 
                                                    THEN '犂和段' 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' AND INSTR(S2."LGTSGM",'段') > 0 
                                                    THEN S2."LGTSGM" 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' 
                                                    THEN S2."LGTSGM" || '段' 
                                                  ELSE '' END || 
                                                  CASE 
                                                    WHEN NVL(S2."LGTSSG",' ') != ' ' AND INSTR(S2."LGTSSG",'小段') > 0 
                                                    THEN S2."LGTSSG" 
                                                    WHEN NVL(S2."LGTSSG",' ') != ' ' 
                                                    THEN S2."LGTSSG" || '小段' 
                                                  ELSE '' END 
    WHERE S1."GDRID1" = '2' -- 撈土地 
    ; 
 
    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 
 
    -- 從建物檔將建物的土地資料寫入土地檔寫入資料 
    INSERT INTO "ClLand" 
    WITH S1 AS ( 
         SELECT DISTINCT 
                B0."ClCode1" 
              , B0."ClCode2" 
              , B0."ClNo" 
              , M."GDRID1" 
              , M."GDRID2" 
              , M."GDRNUM" 
         FROM "ClBuilding" B0  
         LEFT JOIN "ClNoMapping" M ON M."ClCode1" = B0."ClCode1" 
                                  AND M."ClCode2" = B0."ClCode2" 
                                  AND M."ClNo"    = B0."ClNo" 
         LEFT JOIN "LA$LGTP" LG ON LG."GDRID1" = M."GDRID1" 
                               AND LG."GDRID2" = M."GDRID2" 
                               AND LG."GDRNUM" = M."GDRNUM" 
         WHERE B0."ClCode1" = '1' -- 撈建物 
           AND NVL(LG."LGTNM1",0) != 0 
           AND CASE 
                 WHEN B0."ClCode1" = 5 
                 THEN 1 
                 WHEN B0."ClNo" = M."GDRNUM" 
                 THEN 1 
               ELSE 0 END = 1 
    ) 
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1  
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2  
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7  
          ,CASE 
             WHEN S1."ClCode1" = 1 THEN ROW_NUMBER() OVER (PARTITION BY S1."ClCode1" 
                                                                       ,S1."ClCode2" 
                                                                       ,S1."ClNo" 
                                                           ORDER BY S2."GDRID1" 
                                                                   ,S2."GDRID2" 
                                                                   ,S2."GDRNUM" 
                                                                   ,S2."LGTSEQ" 
                                                                   ,S2."LGTNM1" 
                                                                   ,S2."LGTNM2") 
           ELSE 0 END                     AS "LandSeq"             -- 土地序號 DECIMAL 3 
          ,NVL(C1."CityCode",' ')         AS "CityCode"            -- 縣市 VARCHAR2 2  
          ,NVL(C1."AreaCode",' ')         AS "AreaCode"            -- 鄉鎮市區 VARCHAR2 3  
          ,NVL(LS."IrCode",' ')           AS "IrCode"              -- 段小段代碼 VARCHAR2 5  
          ,LPAD(REPLACE(TRIM(S2."LGTNM1"),'-',''),4,'0') 
                                          AS "LandNo1"             -- 地號 VARCHAR2 4  
          ,LPAD(REPLACE(TRIM(S2."LGTNM2"),'-',''),4,'0') 
                                          AS "LandNo2"             -- 地號(子號) VARCHAR2 4  
          ,TRIM(u''||NVL(C1."CityItem",'') || NVL(C1."AreaItem",'') || 
                CASE 
                  WHEN NVL(S2."LGTSGM",' ') != ' ' AND INSTR(S2."LGTSGM",'段') > 0 
                  THEN S2."LGTSGM" 
                  WHEN NVL(S2."LGTSGM",' ') != ' ' 
                  THEN S2."LGTSGM" || '段' 
                ELSE '' END || 
                CASE 
                 WHEN NVL(S2."LGTSSG",' ') != ' 'AND INSTR(S2."LGTSSG",'小段') > 0 
                 THEN S2."LGTSSG" 
                 WHEN NVL(S2."LGTSSG",' ') != ' ' 
                 THEN S2."LGTSSG" || '小段' 
                ELSE '' END  || 
                '，地號'  
                || LPAD(REPLACE(TRIM(S2."LGTNM1"),'-',''),4,'0') 
                || '-' 
                || LPAD(REPLACE(TRIM(S2."LGTNM2"),'-',''),4,'0') 
                )                         AS "LandLocation"        -- 土地座落 NVARCHAR2 150  
          ,S2."LGTORY"                    AS "LandCode"            -- 地目 VARCHAR2 2  
          ,NVL(S2."LGTSQM",0)             AS "Area"                -- 面積 DECIMAL 9 2 
          ,CASE 
             WHEN S2."LGTUSE" = '1'  THEN '10' 
             WHEN S2."LGTUSE" = '10' THEN '02' 
             WHEN S2."LGTUSE" = '11' THEN '04' 
             WHEN S2."LGTUSE" = '12' THEN '05' 
             WHEN S2."LGTUSE" = '13' THEN '06' 
             WHEN S2."LGTUSE" = '14' THEN '07' 
             WHEN S2."LGTUSE" = '15' THEN '08' 
             WHEN S2."LGTUSE" = '16' THEN '09' 
             WHEN S2."LGTUSE" = '17' THEN '12' 
             WHEN S2."LGTUSE" = '18' THEN '13' 
             WHEN S2."LGTUSE" = '19' THEN '14' 
             WHEN S2."LGTUSE" = '2'  THEN '11' 
             WHEN S2."LGTUSE" = '20' THEN '15' 
             WHEN S2."LGTUSE" = '21' THEN '16' 
             WHEN S2."LGTUSE" = '22' THEN '17' 
             WHEN S2."LGTUSE" = '23' THEN '18' 
             WHEN S2."LGTUSE" = '24' THEN '19' 
             WHEN S2."LGTUSE" = '25' THEN '20' 
             WHEN S2."LGTUSE" = '26' THEN '21' 
             WHEN S2."LGTUSE" = '27' THEN '22' 
             WHEN S2."LGTUSE" = '28' THEN '23' 
             WHEN S2."LGTUSE" = '29' THEN '25' 
             WHEN S2."LGTUSE" = '3'  THEN '03' 
             WHEN S2."LGTUSE" = '30' THEN '26' 
             WHEN S2."LGTUSE" = '31' THEN '27' 
             WHEN S2."LGTUSE" = '32' THEN '28' 
             WHEN S2."LGTUSE" = '33' THEN '29' 
             WHEN S2."LGTUSE" = '34' THEN '30' 
             WHEN S2."LGTUSE" = '35' THEN '31' 
             WHEN S2."LGTUSE" = '36' THEN '32' 
             WHEN S2."LGTUSE" = '37' THEN '33' 
             WHEN S2."LGTUSE" = '38' THEN '34' 
             WHEN S2."LGTUSE" = '39' THEN '35' 
             WHEN S2."LGTUSE" = '4'  THEN '24' 
             WHEN S2."LGTUSE" = '40' THEN '36' 
             WHEN S2."LGTUSE" = '41' THEN '37' 
             WHEN S2."LGTUSE" = '42' THEN '38' 
             WHEN S2."LGTUSE" = '43' THEN '39' 
             WHEN S2."LGTUSE" = '44' THEN '40' 
             WHEN S2."LGTUSE" = '45' THEN '41' 
             WHEN S2."LGTUSE" = '46' THEN '42' 
             WHEN S2."LGTUSE" = '47' THEN '43' 
             WHEN S2."LGTUSE" = '48' THEN '44' 
             WHEN S2."LGTUSE" = '49' THEN '45' 
            --  WHEN S2."LGTUSE" = '5' THEN '10' 
             WHEN S2."LGTUSE" = '50' THEN '46' 
             WHEN S2."LGTUSE" = '51' THEN '47' 
             WHEN S2."LGTUSE" = '52' THEN '48' 
             WHEN S2."LGTUSE" = '53' THEN '49' 
             WHEN S2."LGTUSE" = '54' THEN '50' 
             WHEN S2."LGTUSE" = '55' THEN '51' 
             WHEN S2."LGTUSE" = '56' THEN '52' 
             WHEN S2."LGTUSE" = '57' THEN '53' 
             WHEN S2."LGTUSE" = '58' THEN '54' 
             WHEN S2."LGTUSE" = '59' THEN '55' 
            --  WHEN S2."LGTUSE" = '6'  THEN '10' 
             WHEN S2."LGTUSE" = '60' THEN '56' 
             WHEN S2."LGTUSE" = '61' THEN '57' 
             WHEN S2."LGTUSE" = '62' THEN '58' 
             WHEN S2."LGTUSE" = '63' THEN '59' 
             WHEN S2."LGTUSE" = '64' THEN '60' 
             WHEN S2."LGTUSE" = '65' THEN '61' 
            --  WHEN S2."LGTUSE" = '7' THEN '10' 
            --  WHEN S2."LGTUSE" = '8' THEN '10' 
             WHEN S2."LGTUSE" = '9'  THEN '01' 
           ELSE S2."LGTUSE" END           AS "LandZoningCode"      -- 土地使用區分 VARCHAR2 2  
          ,CASE 
             WHEN S2."LGTTYP" = 'F' THEN '19' 
             WHEN S2."LGTTYP" = 'G' THEN '01' 
             WHEN S2."LGTTYP" = 'H' THEN '02' 
             WHEN S2."LGTTYP" = 'I' THEN '03' 
             WHEN S2."LGTTYP" = 'J' THEN '04' 
             WHEN S2."LGTTYP" = 'K' THEN '05' 
             WHEN S2."LGTTYP" = 'L' THEN '17' 
             WHEN S2."LGTTYP" = 'M' THEN '18' 
             WHEN S2."LGTTYP" = 'N' THEN '15' 
             WHEN S2."LGTTYP" = 'O' THEN '06' 
             WHEN S2."LGTTYP" = 'P' THEN '16' 
             WHEN S2."LGTTYP" = 'Q' THEN '07' 
             WHEN S2."LGTTYP" = 'R' THEN '08' 
             WHEN S2."LGTTYP" = 'S' THEN '09' 
             WHEN S2."LGTTYP" = 'T' THEN '10' 
             WHEN S2."LGTTYP" = 'U' THEN '11' 
             WHEN S2."LGTTYP" = 'V' THEN '12' 
             WHEN S2."LGTTYP" = 'W' THEN '13' 
           ELSE '20' END                  AS "LandUsageType"       -- 使用地類別 VARCHAR2 2  
          ,''                             AS "LandUsageCode"       -- 土地使用別 VARCHAR2 1  
          ,NVL(S2."LGTVAL",0)             AS "PostedLandValue"     -- 公告土地現值 DECIMAL 16 2 
          ,NVL(S2."LGTVYM",0)             AS "PostedLandValueYearMonth" -- 公告土地現值年月 DECIMAL 6  
          ,NVL(S2."LGTTYR",0)             AS "TransferedYear"      -- 移轉年度 DECIMAL 4  
          ,NVL(S2."LGTPTA",0)             AS "LastTransferedAmt"   -- 前次移轉金額 DECIMAL 16 2 
          ,NVL(S2."LGTTAX",0)             AS "LVITax"              -- 土地增值稅 DECIMAL 16 2 
          ,NVL(S2."LGTTAY",0)             AS "LVITaxYearMonth"     -- 土地增值稅年月 DECIMAL 6  
          ,NVL(S2."LGTUNT",0)             AS "EvaUnitPrice"        -- 鑑價單價/坪 DECIMAL 16 2 
          ,0                              AS "LandRentStartDate"   -- 土地租約起日 decimald 8  
          ,0                              AS "LandRentEndDate"     -- 土地租約到期日 decimald 8  
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE   
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6  
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE   
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6  
    FROM S1 
    LEFT JOIN "LA$LGTP" S2 ON S2."GDRID1" = S1."GDRID1" 
                          AND S2."GDRID2" = S1."GDRID2" 
                          AND S2."GDRNUM" = S1."GDRNUM" 
                          -- AND S2."LGTSEQ" = S1."LGTSEQ" 
    LEFT JOIN ( SELECT CITY."CityCode" 
                      ,CITY."CityItem" 
                      ,AREA."AreaCode" 
                      ,AREA."AreaItem" 
                FROM "CdCity" CITY 
                LEFT JOIN "CdArea" AREA ON AREA."CityCode" = CITY."CityCode" 
              ) C1 ON C1."CityItem" LIKE CASE 
                                           WHEN S2."LGTCTY" = '　南投' THEN '南投縣' 
                                           WHEN S2."LGTCTY" = '　台中' THEN '台中市' 
                                           WHEN S2."LGTCTY" = '　台北' THEN '台北市' 
                                           WHEN S2."LGTCTY" = '　基隆' THEN '基隆市' 
                                           WHEN S2."LGTCTY" = '　彰化' THEN '彰化縣' 
                                           WHEN S2."LGTCTY" = '　花蓮' THEN '花蓮縣' 
                                           WHEN S2."LGTCTY" = '　雲林' THEN '雲林縣' 
                                           WHEN S2."LGTCTY" = '　高雄' THEN '高雄市' 
                                           WHEN S2."LGTCTY" = '台中縣' THEN '台中市' 
                                           WHEN S2."LGTCTY" = '台北縣' THEN '新北市' 
                                           WHEN S2."LGTCTY" = '台南縣' THEN '台南市' 
                                           WHEN S2."LGTCTY" = '嘉義嘉' THEN '嘉義市' 
                                           WHEN S2."LGTCTY" = '高雄縣' THEN '高雄市' 
                                         ELSE S2."LGTCTY" END || '%' 
                  AND NVL(S2."LGTCTY",' ') != ' ' 
                  AND CASE 
                        WHEN NVL(S2."LGTTWN",' ') != ' ' 
                             AND C1."AreaItem" LIKE CASE 
                                                      WHEN S2."LGTTWN" = '頭份鎮' THEN '頭份' 
                                                      WHEN S2."LGTCTY" = '嘉義' 
                                                           AND S2."LGTTWN" IN ('東區','西區') 
                                                      THEN '嘉義' 
                                                      WHEN S2."LGTCTY" = '新竹' 
                                                           AND S2."LGTTWN" IN ('北區','香山區') 
                                                      THEN '新竹' 
                                                    ELSE S2."LGTTWN" END || '%' 
                        THEN 1 
                        WHEN NVL(S2."LGTTWN",' ') = ' ' 
                             AND NVL(S2."LGTCTY",' ') != ' ' 
                             AND C1."AreaItem" LIKE S2."LGTCTY" || '%' 
                        THEN 1 
                      ELSE 0 END = 1 
    LEFT JOIN "CdLandSection" LS ON LS."CityCode" = C1."CityCode" 
                                AND LS."AreaCode" = C1."AreaCode" 
                                AND LS."IrItem" = CASE 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' AND INSTR(S2."LGTSGM",'段') > 0 
                                                         AND S2."LGTSGM" = '奉口段' 
                                                    THEN '唪口段' 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' 
                                                         AND S2."LGTSGM" = '奉口' 
                                                    THEN '唪口段' 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' AND INSTR(S2."LGTSGM",'段') > 0 
                                                         AND S2."LGTSGM" = '犁和段' 
                                                    THEN '犂和段' 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' 
                                                         AND S2."LGTSGM" = '犁和' 
                                                    THEN '犂和段' 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' AND INSTR(S2."LGTSGM",'段') > 0 
                                                    THEN S2."LGTSGM" 
                                                    WHEN NVL(S2."LGTSGM",' ') != ' ' 
                                                    THEN S2."LGTSGM" || '段' 
                                                  ELSE '' END || 
                                                  CASE 
                                                    WHEN NVL(S2."LGTSSG",' ') != ' ' AND INSTR(S2."LGTSSG",'小段') > 0 
                                                    THEN S2."LGTSSG" 
                                                    WHEN NVL(S2."LGTSSG",' ') != ' ' 
                                                    THEN S2."LGTSSG" || '小段' 
                                                  ELSE '' END 
    WHERE NVL(S2."GDRNUM",0) > 0  
    ; 
 
    -- 記錄寫入筆數 
    INS_CNT := INS_CNT + sql%rowcount; 
     
    -- 把土地設定金額寫回ClImm 
    MERGE INTO "ClImm" T1 
    USING (SELECT S1."ClCode1" 
                 ,S1."ClCode2" 
                 ,S1."ClNo" 
                 ,S2."LGTSAM" 
           FROM "ClNoMapping" S1 
           LEFT JOIN "LA$LGTP" S2 ON S2."GDRID1" = S1."GDRID1" 
                                 AND S2."GDRID2" = S1."GDRID2" 
                                 AND S2."GDRNUM" = S1."GDRNUM" 
                                 AND S2."LGTSEQ" = S1."LGTSEQ" 
           WHERE S1."ClCode1" = 2 
          ) SC1 
    ON (    SC1."ClCode1" = T1."ClCode1" 
        AND SC1."ClCode2" = T1."ClCode2" 
        AND SC1."ClNo"    = T1."ClNo" 
        AND SC1."LGTSAM"  > 0 
       ) 
    WHEN MATCHED THEN UPDATE SET 
    T1."SettingAmt" = NVL(SC1."LGTSAM",0) 
    ; 
 
 
    -- 把地區別更新回ClMain 
    MERGE INTO "ClMain" T1 
    USING (SELECT S1."ClCode1" 
                 ,S1."ClCode2" 
                 ,S1."ClNo" 
                 ,S1."CityCode" 
                 ,S1."AreaCode" 
                 ,ROW_NUMBER() 
                  OVER ( 
                    PARTITION BY S1."ClCode1" 
                               , S1."ClCode2" 
                               , S1."ClNo" 
                    ORDER BY S1."LandSeq" 
                  ) AS "Seq"  
           FROM "ClLand" S1 
           LEFT JOIN "ClMain" CM ON CM."ClCode1" = S1."ClCode1" 
                                AND CM."ClCode2" = S1."ClCode2" 
                                AND CM."ClNo"    = S1."ClNo" 
           WHERE CASE 
                   WHEN NVL(S1."CityCode",' ') != ' ' 
                        AND NVL(CM."CityCode",' ') = ' ' -- ClMain的CityCode為空,更新 
                   THEN 1 
                   WHEN S1."ClCode1" = 1 -- 若是建物資料 下列條件跳過 
                   THEN 0 
                   WHEN NVL(S1."CityCode",' ') != ' ' 
                        AND NVL(CM."CityCode",' ') != ' '  
                        AND NVL(S1."CityCode",' ') != NVL(CM."CityCode",' ') -- ClMain的CityCode與ClLand不一致,更新 
                   THEN 1  
                   WHEN NVL(S1."CityCode",' ') != ' ' 
                        AND NVL(CM."CityCode",' ') != ' '  
                        AND NVL(S1."CityCode",' ') = NVL(CM."CityCode",' ')  
                        AND NVL(S1."AreaCode",' ') != ' '  
                        AND NVL(S1."AreaCode",' ') != NVL(CM."AreaCode",' ') -- ClMain的AreaCode與ClLand不一致,更新 
                   THEN 1  
                 ELSE 0 
                 END = 1 
          ) SC1 
    ON (    SC1."ClCode1" = T1."ClCode1" 
        AND SC1."ClCode2" = T1."ClCode2" 
        AND SC1."ClNo"    = T1."ClNo" 
        AND SC1."Seq" = 1 -- 多筆時只取一筆 
       ) 
    WHEN MATCHED THEN UPDATE SET 
    T1."CityCode" = SC1."CityCode" 
    , T1."AreaCode" = SC1."AreaCode" 
    ; 
     
    update "ClBuilding" T 
    set "AreaCode" = '01' 
    where "CityCode" IN ('17','54') 
      and "AreaCode" is null 
    ; 
     
    merge into "ClBuilding" T 
    using ( 
    with mappingData as ( 
    select DISTINCT 
           B."ClCode1" 
         , B."ClCode2" 
         , B."ClNo" 
         , B."CityCode" 
         , B."AreaCode" 
         , B."BdLocation" 
         , L.LGTCTY 
         , L.LGTTWN 
         , L.LGTSGM 
         , L.LGTSSG 
         , LS."IrCode" 
    from "ClBuilding" B 
    left join "ClNoMap" CNM on CNM."ClCode1" = B."ClCode1" 
                           and CNM."ClCode2" = B."ClCode2" 
                           and CNM."ClNo" = B."ClNo" 
                           and CNM."TfStatus" IN ('1','3') 
    left join LA$LGTP L on L.GDRID1 = CNM."GdrId1" 
                                     AND L.GDRID2 = CNM."GdrId2" 
                                     AND L.GDRNUM = CNM."GdrNum" 
        LEFT JOIN ( SELECT CITY."CityCode" 
                          ,CITY."CityItem" 
                          ,AREA."AreaCode" 
                          ,AREA."AreaItem" 
                    FROM "CdCity" CITY 
                    LEFT JOIN "CdArea" AREA ON AREA."CityCode" = CITY."CityCode" 
                  ) C1 ON C1."CityItem" LIKE CASE 
                                               WHEN L."LGTCTY" = '　南投' THEN '南投縣' 
                                               WHEN L."LGTCTY" = '　台中' THEN '台中市' 
                                               WHEN L."LGTCTY" = '　台北' THEN '台北市' 
                                               WHEN L."LGTCTY" = '　基隆' THEN '基隆市' 
                                               WHEN L."LGTCTY" = '　彰化' THEN '彰化縣' 
                                               WHEN L."LGTCTY" = '　花蓮' THEN '花蓮縣' 
                                               WHEN L."LGTCTY" = '　雲林' THEN '雲林縣' 
                                               WHEN L."LGTCTY" = '　高雄' THEN '高雄市' 
                                               WHEN L."LGTCTY" = '台中縣' THEN '台中市' 
                                               WHEN L."LGTCTY" = '台北縣' THEN '新北市' 
                                               WHEN L."LGTCTY" = '台南縣' THEN '台南市' 
                                               WHEN L."LGTCTY" = '嘉義嘉' THEN '嘉義市' 
                                               WHEN L."LGTCTY" = '高雄縣' THEN '高雄市' 
                                             ELSE L."LGTCTY" END || '%' 
                      AND NVL(L."LGTCTY",' ') != ' ' 
                      AND CASE 
                            WHEN NVL(L."LGTTWN",' ') != ' ' 
                                 AND C1."AreaItem" LIKE CASE 
                                                          WHEN L."LGTTWN" = '頭份鎮' THEN '頭份' 
                                                          WHEN L."LGTCTY" = '嘉義' 
                                                               AND L."LGTTWN" IN ('東區','西區') 
                                                          THEN '嘉義' 
                                                          WHEN L."LGTCTY" = '新竹' 
                                                               AND L."LGTTWN" IN ('北區','香山區') 
                                                          THEN '新竹' 
                                                        ELSE L."LGTTWN" END || '%' 
                            THEN 1 
                            WHEN NVL(L."LGTTWN",' ') = ' ' 
                                 AND NVL(L."LGTCTY",' ') != ' ' 
                                 AND C1."AreaItem" LIKE L."LGTCTY" || '%' 
                            THEN 1 
                          ELSE 0 END = 1 
        LEFT JOIN "CdLandSection" LS ON LS."CityCode" = C1."CityCode" 
                                    AND LS."AreaCode" = C1."AreaCode" 
                                    AND LS."IrItem" = CASE 
                                                        WHEN NVL(L."LGTSGM",' ') != ' ' AND INSTR(L."LGTSGM",'段') > 0 
                                                             AND L."LGTSGM" = '奉口段' 
                                                        THEN '唪口段' 
                                                        WHEN NVL(L."LGTSGM",' ') != ' ' 
                                                             AND L."LGTSGM" = '奉口' 
                                                        THEN '唪口段' 
                                                        WHEN NVL(L."LGTSGM",' ') != ' ' AND INSTR(L."LGTSGM",'段') > 0 
                                                             AND L."LGTSGM" = '犁和段' 
                                                        THEN '犂和段' 
                                                        WHEN NVL(L."LGTSGM",' ') != ' ' 
                                                             AND L."LGTSGM" = '犁和' 
                                                        THEN '犂和段' 
                                                        WHEN NVL(L."LGTSGM",' ') != ' ' AND INSTR(L."LGTSGM",'段') > 0 
                                                        THEN L."LGTSGM" 
                                                        WHEN NVL(L."LGTSGM",' ') != ' ' 
                                                        THEN L."LGTSGM" || '段' 
                                                      ELSE '' END || 
                                                      CASE 
                                                        WHEN NVL(L."LGTSSG",' ') != ' 'AND INSTR(L."LGTSSG",'小段') > 0 
                                                        THEN L."LGTSSG" 
                                                        WHEN NVL(L."LGTSSG",' ') != ' ' 
                                                        THEN L."LGTSSG" || '小段' 
                                                      ELSE '' END 
    where B."IrCode" is null 
      and B."CityCode" is not null 
      and B."AreaCode" is not null 
      and L.LGTCTY is not null 
      and L.LGTTWN is not null 
      and L.LGTSGM is not null 
      and LS."IrCode" is not null 
      and B."CityCode" = LS."CityCode" 
      and B."AreaCode" = LS."AreaCode" 
    ) 
    select DISTINCT 
           "ClCode1" 
         , "ClCode2" 
         , "ClNo" 
         , "CityCode" 
         , "AreaCode" 
         , "BdLocation" 
         , LGTCTY 
         , LGTTWN 
         , LGTSGM 
         , LGTSSG 
         , "IrCode" 
         , ROW_NUMBER() 
           OVER ( 
            PARTITION BY "ClCode1" 
                       , "ClCode2" 
                       , "ClNo" 
            ORDER BY "IrCode" 
           ) AS "Seq" 
    from mappingData 
    ) S 
    on ( 
    S."ClCode1" = T."ClCode1" 
    AND S."ClCode2" = T."ClCode2" 
    AND S."ClNo" = T."ClNo" 
    AND S."Seq" = 1 
    ) 
    when matched then update 
    set "IrCode" = S."IrCode" 
    ; 
 
    -- 記錄程式結束時間 
    JOB_END_TIME := SYSTIMESTAMP; 
 
    commit; 
 
    -- 例外處理 
    Exception 
    WHEN OTHERS THEN 
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace; 
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClLand_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace); 
END;

/
