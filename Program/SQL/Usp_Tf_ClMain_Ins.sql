CREATE OR REPLACE PROCEDURE "Usp_Tf_ClMain_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClMain" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClMain" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClMain" ENABLE PRIMARY KEY';

    -- 寫入資料 (不動產擔保品)
    INSERT INTO "ClMain" (
        "ClCode1"             -- 擔保品代號1 DECIMAL 1 
      , "ClCode2"             -- 擔保品代號2 DECIMAL 2 
      , "ClNo"                -- 擔保品編號 DECIMAL 7 
      , "CustUKey"            -- 客戶識別碼 VARCHAR2 32
      , "ClTypeCode"          -- 擔保品類別代碼 VARCHAR2 3 
      , "CityCode"            -- 地區別 VARCHAR2 2 
      , "AreaCode"            -- 鄉鎮區 VARCHAR2 3
      , "ClStatus"            -- 擔保品狀況碼 VARCHAR2 1 
      , "EvaDate"             -- 鑑估日期 DecimalD 8 
      , "EvaAmt"              -- 鑑估總值 DECIMAL 16 2
      , "ShareTotal"          -- 可分配金額 DECIMAL 16 2
      , "Synd"                -- 是否為聯貸案 VARCHAR2 1 
      , "SyndCode"            -- 聯貸案類型 VARCHAR2 1 
      , "DispPrice"           -- 處分價格 DECIMAL 16 2
      , "DispDate"            -- 處分日期 Decimald 8 
      , "NewNote"             -- 最新註記 VARCHAR2 1
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
      , "LastClOtherSeq"
    )
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,CASE
             WHEN NVL(CM3."CustUKey",' ') != ' '
             THEN NVL(CM3."CustUKey",' ')
             WHEN S1."ClCode1" = 1 AND NVL(CM."CustUKey",' ') != ' '
             THEN CM."CustUKey"
             WHEN S1."ClCode1" = 2 AND NVL(CM2."CustUKey",' ') != ' '
             THEN CM2."CustUKey"
           ELSE ' '
           END                            AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32
          ,CASE 
             WHEN S1."ClCode1" = '1' AND S1."ClCode2" = '1'  THEN '250' -- 土地及建物-住宅用
             WHEN S1."ClCode1" = '1' AND S1."ClCode2" = '2'  THEN '250' -- 土地及建物-住宅用
             WHEN S1."ClCode1" = '1' AND S1."ClCode2" = '3'  THEN '2A0' -- 土地及建物商業用
             WHEN S1."ClCode1" = '1' AND S1."ClCode2" = '4'  THEN '260' -- 土地及廠房
             WHEN S1."ClCode1" = '1' AND S1."ClCode2" = '5'  THEN '2X0' -- 其他不動產
             WHEN S1."ClCode1" = '1' AND S1."ClCode2" = '99' THEN '2X0' -- 其他不動產
             WHEN S1."ClCode1" = '2' AND S1."ClCode2" = '1'  THEN '200' -- 房地建地（不含建物）
             WHEN S1."ClCode1" = '2' AND S1."ClCode2" = '2'  THEN '200' -- 房地建地（不含建物）
             WHEN S1."ClCode1" = '2' AND S1."ClCode2" = '3'  THEN '200' -- 房地建地（不含建物）
             WHEN S1."ClCode1" = '2' AND S1."ClCode2" = '4'  THEN '200' -- 房地建地（不含建物）
             WHEN S1."ClCode1" = '2' AND S1."ClCode2" = '9'  THEN '220' -- 農地
             WHEN S1."ClCode1" = '2' AND S1."ClCode2" = '10' THEN '200' -- 房地建地（不含建物）
             WHEN S1."ClCode1" = '2' AND S1."ClCode2" = '11' THEN '200' -- 房地建地（不含建物）
             WHEN S1."ClCode1" = '2' AND S1."ClCode2" = '12' THEN '200' -- 房地建地（不含建物）
             WHEN S1."ClCode1" = '2' AND S1."ClCode2" = '13' THEN '200' -- 房地建地（不含建物）
             WHEN S1."ClCode1" = '2' AND S1."ClCode2" = '19' THEN '220' -- 農地
           ELSE '' END                    AS "ClTypeCode"          -- 擔保品類別代碼 VARCHAR2 3 
          ,CASE
             WHEN S1."ClCode1" = 1 AND NVL(CITY."CityCode",' ') != ' '
             THEN CITY."CityCode"
             WHEN S1."ClCode1" = 1 AND NVL(CITY2."CityCode",' ') != ' '
             THEN CITY2."CityCode"
             WHEN S1."ClCode1" = 2 AND NVL(CITY3."CityCode",' ') != ' '
             THEN CITY3."CityCode"
           ELSE ' '
           END                            AS "CityCode"            -- 地區別 VARCHAR2 2 
          ,CASE
             WHEN S1."ClCode1" = 1 AND NVL(AREA."AreaCode",' ') != ' '
             THEN AREA."AreaCode"
             WHEN S1."ClCode1" = 2 AND NVL(AREA3."AreaCode",' ') != ' '
             THEN AREA3."AreaCode"
           ELSE ' '
           END                            AS "AreaCode"            -- 鄉鎮區 VARCHAR2 3
          ,CASE
             WHEN S1."ClCode1" = 1 AND NVL(HGTP."GRTSTS",'0') != '0'
             THEN TO_CHAR(HGTP."GRTSTS")
             WHEN S1."ClCode1" = 2 AND NVL(LGTP."GRTSTS",'0') != '0'
             THEN TO_CHAR(LGTP."GRTSTS")
           ELSE '0'
           END                            AS "ClStatus"            -- 擔保品狀況碼 VARCHAR2 1 
          ,NVL(GDTP."GDTIDT",0)           AS "EvaDate"             -- 鑑估日期 DecimalD 8 
          ,NVL(GDTP."ETTVAL",0)           AS "EvaAmt"              -- 鑑估總值 DECIMAL 16 2
          ,0                              AS "ShareTotal"          -- 可分配金額 DECIMAL 16 2
          ,'N'                            AS "Synd"                -- 是否為聯貸案 VARCHAR2 1 
          ,''                             AS "SyndCode"            -- 聯貸案類型 VARCHAR2 1 
          ,0                              AS "DispPrice"           -- 處分價格 DECIMAL 16 2
          ,0                              AS "DispDate"            -- 處分日期 Decimald 8 
          ,'Y'                            AS "NewNote"             -- 最新註記 VARCHAR2 1
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,0                              AS "LastClOtherSeq"
    FROM "ClNoMap" S1
    LEFT JOIN "LA$HGTP" HGTP ON HGTP."GDRID1" = S1."GdrId1"
                            AND HGTP."GDRID2" = S1."GdrId2"
                            AND HGTP."GDRNUM" = S1."GdrNum"
                            AND HGTP."LGTSEQ" = S1."LgtSeq"
    LEFT JOIN "LA$LGTP" LGTP ON LGTP."GDRID1" = S1."GdrId1"
                            AND LGTP."GDRID2" = S1."GdrId2"
                            AND LGTP."GDRNUM" = S1."GdrNum"
                            AND LGTP."LGTSEQ" = S1."LgtSeq"
    LEFT JOIN "LA$GDTP" GDTP ON GDTP."GDRID1" = S1."GdrId1"
                            AND GDTP."GDRID2" = S1."GdrId2"
                            AND GDTP."GDRNUM" = S1."GdrNum"
    LEFT JOIN "LA$APLP" APLP ON APLP."GDRID1" = S1."GdrId1"
                            AND APLP."GDRID2" = S1."GdrId2"
                            AND APLP."GDRNUM" = S1."GdrNum"
    LEFT JOIN "CU$CUSP" CUSP ON CUSP."CUSCIF" = HGTP."LGTCIF" -- 建物擔保品提供人識別碼
                            AND NVL(HGTP."LGTCIF",0) > 0
    LEFT JOIN "CU$CUSP" CUSP2 ON CUSP2."CUSCIF" = LGTP."LGTCIF" -- 土地擔保品提供人識別碼
                             AND NVL(LGTP."LGTCIF",0) > 0
    LEFT JOIN "CustMain" CM ON CM."CustNo" = CUSP."LMSACN" -- 建物擔保品提供人戶號
                           AND NVL(CUSP."LMSACN",0) > 0
    LEFT JOIN "CustMain" CM2 ON CM2."CustNo" = CUSP2."LMSACN" -- 土地擔保品提供人戶號
                            AND NVL(CUSP2."LMSACN",0) > 0
    LEFT JOIN "CustMain" CM3 ON CM3."CustNo" = APLP."LMSACN" -- 借戶戶號
                            AND NVL(APLP."LMSACN",0) > 0
    LEFT JOIN "CdCity" CITY ON NVL(HGTP."HGTAD1",' ') <> ' ' -- 建物擔保品地區別
                           AND CITY."CityItem" = NVL(HGTP."HGTAD1",' ')
    LEFT JOIN "CdArea" AREA ON AREA."CityCode" = CITY."CityCode" -- 建物擔保品鄉鎮區
                           AND AREA."AreaItem" = NVL(HGTP."HGTAD2",' ')
                           AND NVL(CITY."CityCode",' ') <> ' '
    LEFT JOIN "CdCity" CITY2 ON NVL(HGTP."HGTAD1",' ') = ' '
                           AND NVL(GDTP."LOCLID",0) > 0
                           AND CITY."CityCode" = NVL(GDTP."LOCLID",0)
    LEFT JOIN "CdCity" CITY3 ON NVL(LGTP."LGTCTY",' ') <> ' ' -- 土地擔保品地區別
                            AND CITY3."CityItem" = NVL(LGTP."LGTCTY",' ')
    LEFT JOIN "CdArea" AREA3 ON AREA3."CityCode" = CITY3."CityCode" -- 土地擔保品鄉鎮區
                            AND AREA3."AreaItem" = NVL(LGTP."LGTTWN",' ')
                            AND NVL(CITY3."CityCode",' ') <> ' '
    WHERE S1."ClCode1" IN (1,2)
      AND S1."TfStatus" IN (1,3)
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料 (動產擔保品)
    INSERT INTO "ClMain" (
        "ClCode1"             -- 擔保品代號1 DECIMAL 1 
      , "ClCode2"             -- 擔保品代號2 DECIMAL 2 
      , "ClNo"                -- 擔保品編號 DECIMAL 7 
      , "CustUKey"            -- 客戶識別碼 VARCHAR2 32
      , "ClTypeCode"          -- 擔保品類別代碼 VARCHAR2 3 
      , "CityCode"            -- 地區別 VARCHAR2 2 
      , "AreaCode"            -- 鄉鎮區 VARCHAR2 3
      , "ClStatus"            -- 擔保品狀況碼 VARCHAR2 1 
      , "EvaDate"             -- 鑑估日期 DecimalD 8 
      , "EvaAmt"              -- 鑑估總值 DECIMAL 16 2
      , "ShareTotal"          -- 可分配金額 DECIMAL 16 2
      , "Synd"                -- 是否為聯貸案 VARCHAR2 1 
      , "SyndCode"            -- 聯貸案類型 VARCHAR2 1 
      , "DispPrice"           -- 處分價格 DECIMAL 16 2
      , "DispDate"            -- 處分日期 Decimald 8 
      , "NewNote"             -- 最新註記 VARCHAR2 1
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
      , "LastClOtherSeq"
    )
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,CM."CustUKey"                  AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32
          ,CASE
             WHEN S1."ClCode2" = 1  THEN '310' -- 車輛
             WHEN S1."ClCode2" = 2  THEN '300' -- 機器設備
           ELSE '' END                    AS "ClTypeCode"          -- 擔保品類別代碼 VARCHAR2 3 
          ,CITY."CityCode"                AS "CityCode"            -- 地區別 VARCHAR2 2 
          ,''                             AS "AreaCode"            -- 鄉鎮區 VARCHAR2 3
          ,S2."GRTSTS"                    AS "ClStatus"            -- 擔保品狀況碼 VARCHAR2 1 
          ,0                              AS "EvaDate"             -- 鑑估日期 DecimalD 8 
          ,S2."CGT018"                    AS "EvaAmt"              -- 鑑估總值 DECIMAL 16 2
          ,0                              AS "ShareTotal"          -- 可分配金額 DECIMAL 16 2
          ,'N'                            AS "Synd"                -- 是否為聯貸案 VARCHAR2 1 
          ,''                             AS "SyndCode"            -- 聯貸案類型 VARCHAR2 1 
          ,0                              AS "DispPrice"           -- 處分價格 DECIMAL 16 2
          ,0                              AS "DispDate"            -- 處分日期 Decimald 8 
          ,'Y'                            AS "NewNote"             -- 最新註記 VARCHAR2 1
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,0                              AS "LastClOtherSeq"
    FROM "ClNoMap" S1
    LEFT JOIN "LN$CGTP" S2 ON S2."GDRID1" = S1."GdrId1"
                          AND S2."GDRID2" = S1."GdrId2"
                          AND S2."GDRNUM" = S1."GdrNum"
    LEFT JOIN "CU$CUSP" CU ON CU."CUSID1" = S2."CUSID1"
    LEFT JOIN "CustMain" CM ON CM."CustId" = CU."CUSID1"
    LEFT JOIN "CdCity" CITY ON CITY."CityCode" = NVL(S2."LOCLID",0)
    WHERE S1."ClCode1" = 9
      AND S1."TfStatus" IN (1,3)
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料 (股票擔保品)
    INSERT INTO "ClMain" (
        "ClCode1"             -- 擔保品代號1 DECIMAL 1 
      , "ClCode2"             -- 擔保品代號2 DECIMAL 2 
      , "ClNo"                -- 擔保品編號 DECIMAL 7 
      , "CustUKey"            -- 客戶識別碼 VARCHAR2 32
      , "ClTypeCode"          -- 擔保品類別代碼 VARCHAR2 3 
      , "CityCode"            -- 地區別 VARCHAR2 2 
      , "AreaCode"            -- 鄉鎮區 VARCHAR2 3
      , "ClStatus"            -- 擔保品狀況碼 VARCHAR2 1 
      , "EvaDate"             -- 鑑估日期 DecimalD 8 
      , "EvaAmt"              -- 鑑估總值 DECIMAL 16 2
      , "ShareTotal"          -- 可分配金額 DECIMAL 16 2
      , "Synd"                -- 是否為聯貸案 VARCHAR2 1 
      , "SyndCode"            -- 聯貸案類型 VARCHAR2 1 
      , "DispPrice"           -- 處分價格 DECIMAL 16 2
      , "DispDate"            -- 處分日期 Decimald 8 
      , "NewNote"             -- 最新註記 VARCHAR2 1
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
      , "LastClOtherSeq"
    )
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,S3."CustUKey"                  AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32
          ,CASE
             WHEN S1."ClCode1" = 3 THEN '145' -- 股權憑證
             WHEN S1."ClCode1" = 4 THEN '1X0' -- 其他有價證券
           ELSE '' END                    AS "ClTypeCode"          -- 擔保品類別代碼 VARCHAR2 3 
          ,CITY."CityCode"                AS "CityCode"            -- 地區別 VARCHAR2 2 
          ,''                             AS "AreaCode"            -- 鄉鎮區 VARCHAR2 3
          ,S2."GRTSTS"                    AS "ClStatus"            -- 擔保品狀況碼 VARCHAR2 1 
          ,CASE
             WHEN NVL(S2."GDRNUM",0) > 0 THEN S2."GDTIDT"
           ELSE 0 END                     AS "EvaDate"             -- 鑑估日期 DecimalD 8 
          ,CASE
             WHEN NVL(S2."GDRNUM",0) > 0 THEN S2."SGTTOT"
           ELSE 0 END                     AS "EvaAmt"              -- 鑑估總值 DECIMAL 16 2
          -- 2021-08-10 智偉 新增 可分配金額 轉入邏輯
          -- 步驟1: 評估淨值有值時取評估淨值，無值時取鑑估總值，乘上貸放成數 (四捨五入至個位數)
          -- 步驟2: 將步驟1算出的結果與設定金額相比，較低者則為可分配金額
          -- *** 股票沒有設定金額及評估淨值，直接用鑑估總值*貸放成數
          ,ROUND(NVL(S2."SGTTOT",0) * NVL(S2."SGTPER",0) / 100,0)
                                          AS "ShareTotal"          -- 可分配金額 DECIMAL 16 2
          ,'N'                            AS "Synd"                -- 是否為聯貸案 VARCHAR2 1 
          ,''                             AS "SyndCode"            -- 聯貸案類型 VARCHAR2 1 
          ,0                              AS "DispPrice"           -- 處分價格 DECIMAL 16 2
          ,0                              AS "DispDate"            -- 處分日期 Decimald 8 
          ,'Y'                            AS "NewNote"             -- 最新註記 VARCHAR2 1
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,0                              AS "LastClOtherSeq"
    FROM "ClNoMap" S1
    LEFT JOIN "LA$SGTP" S2 ON S2."GDRID1" = S1."GdrId1"
                          AND S2."GDRID2" = S1."GdrId2"
                          AND S2."GDRNUM" = S1."GdrNum"
    LEFT JOIN (SELECT SG."GDRID1"
                    , SG."GDRID2"
                    , SG."GDRNUM"
                    , CM."CustUKey"
                    , ROW_NUMBER() OVER (PARTITION BY SG."GDRID1"
                                                    , SG."GDRID2"
                                                    , SG."GDRNUM"
                                         ORDER BY SG."SGDQTY"
                                        ) AS "Seq"
               FROM "LA$SGDP" SG
               LEFT JOIN "CU$CUSP" CU ON CU."CUSCIF" = SG."LGTCIF"
               LEFT JOIN "CustMain" CM ON CM."CustId" = CU."CUSID1"
               WHERE NVL(CM."CustUKey",' ') <> ' '
              ) S3 ON S3."GDRID1" = S1."GdrId1"
                  AND S3."GDRID2" = S1."GdrId2"
                  AND S3."GDRNUM" = S1."GdrNum"
                  AND S3."Seq" = 1
    LEFT JOIN "CdCity" CITY ON CITY."CityCode" = NVL(S2."LOCLID",0)
    WHERE S1."ClCode1" >= 3
      AND S1."ClCode1" <= 4
      AND S1."TfStatus" IN (1,3)
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 寫入資料 (其他擔保品)
    INSERT INTO "ClMain" (
        "ClCode1"             -- 擔保品代號1 DECIMAL 1 
      , "ClCode2"             -- 擔保品代號2 DECIMAL 2 
      , "ClNo"                -- 擔保品編號 DECIMAL 7 
      , "CustUKey"            -- 客戶識別碼 VARCHAR2 32
      , "ClTypeCode"          -- 擔保品類別代碼 VARCHAR2 3 
      , "CityCode"            -- 地區別 VARCHAR2 2 
      , "AreaCode"            -- 鄉鎮區 VARCHAR2 3
      , "ClStatus"            -- 擔保品狀況碼 VARCHAR2 1 
      , "EvaDate"             -- 鑑估日期 DecimalD 8 
      , "EvaAmt"              -- 鑑估總值 DECIMAL 16 2
      , "ShareTotal"          -- 可分配金額 DECIMAL 16 2
      , "Synd"                -- 是否為聯貸案 VARCHAR2 1 
      , "SyndCode"            -- 聯貸案類型 VARCHAR2 1 
      , "DispPrice"           -- 處分價格 DECIMAL 16 2
      , "DispDate"            -- 處分日期 Decimald 8 
      , "NewNote"             -- 最新註記 VARCHAR2 1
      , "CreateDate"          -- 建檔日期時間 DATE  
      , "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
      , "LastUpdate"          -- 最後更新日期時間 DATE  
      , "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
      , "LastClOtherSeq"
    )
    SELECT S1."ClCode1"                   AS "ClCode1"             -- 擔保品代號1 DECIMAL 1 
          ,S1."ClCode2"                   AS "ClCode2"             -- 擔保品代號2 DECIMAL 2 
          ,S1."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 
          ,''                             AS "CustUKey"            -- 客戶識別碼 VARCHAR2 32
          ,'998'                          AS "ClTypeCode"          -- 擔保品類別代碼 VARCHAR2 3 
          ,CITY."CityCode"                AS "CityCode"            -- 地區別 VARCHAR2 2 
          ,''                             AS "AreaCode"            -- 鄉鎮區 VARCHAR2 3 
          ,S2."GRTSTS"                    AS "ClStatus"            -- 擔保品狀況碼 VARCHAR2 1 
          ,0                              AS "EvaDate"             -- 鑑估日期 DecimalD 8 
          ,S2."BGTAMT"                    AS "EvaAmt"              -- 鑑估總值 DECIMAL 16 2
          ,0                              AS "ShareTotal"          -- 可分配金額 DECIMAL 16 2
          ,'N'                            AS "Synd"                -- 是否為聯貸案 VARCHAR2 1 
          ,''                             AS "SyndCode"            -- 聯貸案類型 VARCHAR2 1 
          ,0                              AS "DispPrice"           -- 處分價格 DECIMAL 16 2
          ,0                              AS "DispDate"            -- 處分日期 Decimald 8 
          ,'Y'                            AS "NewNote"             -- 最新註記 VARCHAR2 1
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 
          ,0                              AS "LastClOtherSeq"
    FROM "ClNoMap" S1
    LEFT JOIN "LA$BGTP" S2 ON S2."GDRID1" = S1."GdrId1"
                          AND S2."GDRID2" = S1."GdrId2"
                          AND S2."GDRNUM" = S1."GdrNum"
    LEFT JOIN "CdCity" CITY ON CITY."CityCode" = NVL(S2."LOCLID",0)
    WHERE S1."ClCode1" = 5
      AND S1."TfStatus" IN (1,3)
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClMain_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
