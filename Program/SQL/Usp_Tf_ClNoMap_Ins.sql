CREATE OR REPLACE PROCEDURE "Usp_Tf_ClNoMap_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClNoMap" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClNoMap" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClNoMap" ENABLE PRIMARY KEY';

    -- 寫入資料 (擔保品編號新舊對照檔))
    INSERT INTO "ClNoMap"
    WITH cData AS (
      SELECT "GroupNo"
           , "SecGroupNo"
           , COUNT(*) AS "Cnt"
      FROM "ClBuildingUnique"
      GROUP BY "GroupNo"
             , "SecGroupNo"
    )
    SELECT S0."GDRID1"                           AS "GdrId1"
          ,S0."GDRID2"                           AS "GdrId2"
          ,S0."GDRNUM"                           AS "GdrNum"
          ,S0."LGTSEQ"                           AS "LgtSeq"
          ,NVL(S1."MainGDRID1",0)                AS "MainGdrId1"
          ,NVL(S1."MainGDRID2",0)                AS "MainGdrId2"
          ,NVL(S1."MainGDRNUM",0)                AS "MainGdrNum"
          ,NVL(S1."MainLGTSEQ",0)                AS "MainLgtSeq"
          ,CASE 
             WHEN S1."MainGDRID1" IS NOT NULL
             THEN S3."ClCode1"
             WHEN S2."ClCode1" IS NOT NULL
             THEN S2."ClCode1" 
           ELSE 0 END                            AS "ClCode1"
          ,CASE
             WHEN S1."MainGDRID2" IS NOT NULL
             THEN S3."ClCode2"
             WHEN S2."ClCode2" IS NOT NULL
             THEN S2."ClCode2" 
           ELSE 0 END                            AS "ClCode2"
          ,CASE
             WHEN S1."MainGDRNUM" IS NOT NULL
             THEN S3."ClNo"
             WHEN S2."ClNo" IS NOT NULL
             THEN S2."ClNo"
          ELSE 0 END                             AS "ClNo"
          ,CASE
             WHEN S1."MainGDRNUM" IS NOT NULL 
                  AND TO_NUMBER(S1."MainGDRID1") = TO_NUMBER(S1."SubGDRID1")
                  AND TO_NUMBER(S1."MainGDRID2") = TO_NUMBER(S1."SubGDRID2")
                  AND TO_NUMBER(S1."MainGDRNUM") = TO_NUMBER(S1."SubGDRNUM")
                  AND TO_NUMBER(S1."MainLGTSEQ") = TO_NUMBER(S1."SubLGTSEQ")
                  AND cData."Cnt" > 1
             THEN 1 -- 1 唯一性處理後,作為最新擔保品轉入
             WHEN S1."MainGDRNUM" IS NOT NULL 
                  AND cData."Cnt" > 1
             THEN 2 -- 2 唯一性處理後,此筆不轉入,僅由最新擔保品轉入
             WHEN S2."ClNo" IS NOT NULL 
             THEN 3 -- 3 單筆擔保品直接轉入
             WHEN S0."LGTADR" IS NULL
             THEN 5 -- 5 '未轉入,資料不完整(門牌地址)'
             WHEN S0."HGTMHN" = 0
             THEN 6 -- 6 '未轉入,資料不完整(主建號)'
           ELSE 0 END AS "TfStatus" -- 0 未轉入 其他原因
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 
    FROM "LA$HGTP" S0
    LEFT JOIN (SELECT DISTINCT
                      M."GroupNo"
                     ,M."SecGroupNo"
                     ,M."GDRID1" AS "MainGDRID1"
                     ,M."GDRID2" AS "MainGDRID2"
                     ,M."GDRNUM" AS "MainGDRNUM"
                     ,M."LGTSEQ" AS "MainLGTSEQ"
                     ,S."GDRID1" AS "SubGDRID1"
                     ,S."GDRID2" AS "SubGDRID2"
                     ,S."GDRNUM" AS "SubGDRNUM"
                     ,S."LGTSEQ" AS "SubLGTSEQ"
               FROM "ClBuildingUnique" M --Main
               LEFT JOIN "ClBuildingUnique" S ON S."GroupNo" = M."GroupNo" --Sub
                                             AND S."SecGroupNo" = M."SecGroupNo"
               WHERE M."TfFg" = 'Y'
              ) S1 ON S1."SubGDRID1" = S0."GDRID1"
                  AND S1."SubGDRID2" = S0."GDRID2"
                  AND S1."SubGDRNUM" = S0."GDRNUM"
                  AND S1."SubLGTSEQ" = S0."LGTSEQ"
    -- S2 : 此擔保品在 新系統的擔保品編號
    LEFT JOIN "ClNoMapping" S2 ON S2."GDRID1" = S0."GDRID1"
                              AND S2."GDRID2" = S0."GDRID2"
                              AND S2."GDRNUM" = S0."GDRNUM"
                              AND S2."LGTSEQ" = S0."LGTSEQ"
    -- S3 : 主要擔保品在 新系統的擔保品編號
    LEFT JOIN "ClNoMapping" S3 ON S3."GDRID1" = S1."MainGDRID1"
                              AND S3."GDRID2" = S1."MainGDRID2"
                              AND S3."GDRNUM" = S1."MainGDRNUM"
                              AND S3."LGTSEQ" = S1."MainLGTSEQ"
    LEFT JOIN cData ON cData."GroupNo" = S1."GroupNo"
                   AND cData."SecGroupNo" = S1."SecGroupNo"
    WHERE S0."GDRID1" = 1 -- 只抓擔保品代號1 = 1 房地 的擔保品
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    INSERT INTO "ClNoMap"
    SELECT S0."GDRID1"                           AS "GdrId1"
          ,S0."GDRID2"                           AS "GdrId2"
          ,S0."GDRNUM"                           AS "GdrNum"
          ,S0."LGTSEQ"                           AS "LgtSeq"
          ,NVL(S1."MainGDRID1",0)                AS "MainGdrId1"
          ,NVL(S1."MainGDRID2",0)                AS "MainGdrId2"
          ,NVL(S1."MainGDRNUM",0)                AS "MainGdrNum"
          ,NVL(S1."MainLGTSEQ",0)                AS "MainLgtSeq"
          ,CASE 
             WHEN S1."MainGDRID1" IS NOT NULL
             THEN S3."ClCode1"
             WHEN S2."ClCode1" IS NOT NULL
             THEN S2."ClCode1" 
           ELSE 0 END                            AS "ClCode1"
          ,CASE
             WHEN S1."MainGDRID2" IS NOT NULL
             THEN S3."ClCode2"
             WHEN S2."ClCode2" IS NOT NULL
             THEN S2."ClCode2" 
           ELSE 0 END                            AS "ClCode2"
          ,CASE
             WHEN S1."MainGDRNUM" IS NOT NULL
             THEN S3."ClNo"
             WHEN S2."ClNo" IS NOT NULL
             THEN S2."ClNo"
          ELSE 0 END                             AS "ClNo"
          ,CASE
             WHEN S1."MainGDRNUM" IS NOT NULL 
                  AND TO_NUMBER(S1."MainGDRID1") = TO_NUMBER(S1."SubGDRID1")
                  AND TO_NUMBER(S1."MainGDRID2") = TO_NUMBER(S1."SubGDRID2")
                  AND TO_NUMBER(S1."MainGDRNUM") = TO_NUMBER(S1."SubGDRNUM")
                  AND TO_NUMBER(S1."MainLGTSEQ") = TO_NUMBER(S1."SubLGTSEQ")
             THEN 1 -- 1 唯一性處理後,作為最新擔保品轉入
             WHEN S1."MainGDRNUM" IS NOT NULL 
             THEN 2 -- 2 唯一性處理後,此筆不轉入,僅由最新擔保品轉入
             WHEN S2."ClNo" IS NOT NULL 
             THEN 3 -- 3 單筆擔保品直接轉入
             WHEN S0."LGTNM1" = 0
             THEN 7 -- 7 '未轉入,資料不完整(地號)'
           ELSE 0 END AS "TfStatus" -- 0 未轉入
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 
    FROM "LA$LGTP" S0
    LEFT JOIN (SELECT DISTINCT
                      M."GroupNo"
                     ,M."NewGroupNo"
                     ,M."GDRID1" AS "MainGDRID1"
                     ,M."GDRID2" AS "MainGDRID2"
                     ,M."GDRNUM" AS "MainGDRNUM"
                     ,M."LGTSEQ" AS "MainLGTSEQ"
                     ,S."GDRID1" AS "SubGDRID1"
                     ,S."GDRID2" AS "SubGDRID2"
                     ,S."GDRNUM" AS "SubGDRNUM"
                     ,S."LGTSEQ" AS "SubLGTSEQ"
               FROM "ClLandUnique" M --Main
               LEFT JOIN "ClLandUnique" S ON S."GroupNo" = M."GroupNo" --Sub
                                         AND S."NewGroupNo" = M."NewGroupNo"
               WHERE M."TfFg" = 'Y'
              ) S1 ON S1."SubGDRID1" = S0."GDRID1"
                  AND S1."SubGDRID2" = S0."GDRID2"
                  AND S1."SubGDRNUM" = S0."GDRNUM"
                  AND S1."SubLGTSEQ" = S0."LGTSEQ"
    -- S2 : 此擔保品在 新系統的擔保品編號
    LEFT JOIN "ClNoMapping" S2 ON S2."GDRID1" = S0."GDRID1"
                              AND S2."GDRID2" = S0."GDRID2"
                              AND S2."GDRNUM" = S0."GDRNUM"
                              AND S2."LGTSEQ" = S0."LGTSEQ"
    -- S3 : 主要擔保品在 新系統的擔保品編號
    LEFT JOIN "ClNoMapping" S3 ON S3."GDRID1" = S1."MainGDRID1"
                              AND S3."GDRID2" = S1."MainGDRID2"
                              AND S3."GDRNUM" = S1."MainGDRNUM"
                              AND S3."LGTSEQ" = S1."MainLGTSEQ"
    WHERE S0."GDRID1" = 2 -- 只抓擔保品代號1 = 2 土地 的擔保品
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    INSERT INTO "ClNoMap"
    SELECT S0."GDRID1"                           AS "GdrId1"
          ,S0."GDRID2"                           AS "GdrId2"
          ,S0."GDRNUM"                           AS "GdrNum"
          ,0                                     AS "LgtSeq"
          ,0                                     AS "MainGdrId1"
          ,0                                     AS "MainGdrId2"
          ,0                                     AS "MainGdrNum"
          ,0                                     AS "MainLgtSeq"
          ,CASE 
             WHEN S2."ClCode1" IS NOT NULL
             THEN S2."ClCode1" 
           ELSE 0 END                            AS "ClCode1"
          ,CASE
             WHEN S2."ClCode2" IS NOT NULL
             THEN S2."ClCode2" 
           ELSE 0 END                            AS "ClCode2"
          ,CASE
             WHEN S2."ClNo" IS NOT NULL
             THEN S2."ClNo"
          ELSE 0 END                             AS "ClNo"
          ,CASE
             WHEN S2."ClNo" IS NOT NULL 
             THEN 3 -- 3 單筆擔保品直接轉入
           ELSE 0 END AS "TfStatus" -- 0 未轉入
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 
    FROM "LN$CGTP" S0
    -- S2 : 此擔保品在 新系統的擔保品編號
    LEFT JOIN "ClNoMapping" S2 ON S2."GDRID1" = S0."GDRID1"
                              AND S2."GDRID2" = S0."GDRID2"
                              AND S2."GDRNUM" = S0."GDRNUM"
    WHERE S0."GDRID1" = 9 -- 只抓擔保品代號1 = 9 動產 的擔保品
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    INSERT INTO "ClNoMap"
    SELECT S0."GDRID1"                           AS "GdrId1"
          ,S0."GDRID2"                           AS "GdrId2"
          ,S0."GDRNUM"                           AS "GdrNum"
          ,0                                     AS "LgtSeq"
          ,0                                     AS "MainGdrId1"
          ,0                                     AS "MainGdrId2"
          ,0                                     AS "MainGdrNum"
          ,0                                     AS "MainLgtSeq"
          ,CASE 
             WHEN S2."ClCode1" IS NOT NULL
             THEN S2."ClCode1" 
           ELSE 0 END                            AS "ClCode1"
          ,CASE
             WHEN S2."ClCode2" IS NOT NULL
             THEN S2."ClCode2" 
           ELSE 0 END                            AS "ClCode2"
          ,CASE
             WHEN S2."ClNo" IS NOT NULL
             THEN S2."ClNo"
          ELSE 0 END                             AS "ClNo"
          ,CASE
             WHEN S2."ClNo" IS NOT NULL 
             THEN 3 -- 3 單筆擔保品直接轉入
           ELSE 0 END AS "TfStatus" -- 0 未轉入
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 
    FROM "LA$SGTP" S0
    -- S2 : 此擔保品在 新系統的擔保品編號
    LEFT JOIN "ClNoMapping" S2 ON S2."GDRID1" = S0."GDRID1"
                              AND S2."GDRID2" = S0."GDRID2"
                              AND S2."GDRNUM" = S0."GDRNUM"
    WHERE S0."GDRID1" IN (3,4) -- 只抓擔保品代號1 = 3 or 4 股票 的擔保品
    ;

    -- 記錄寫入筆數
    INS_CNT := INS_CNT + sql%rowcount;

    INSERT INTO "ClNoMap"
    SELECT S0."GDRID1"                           AS "GdrId1"
          ,S0."GDRID2"                           AS "GdrId2"
          ,S0."GDRNUM"                           AS "GdrNum"
          ,0                                     AS "LgtSeq"
          ,0                                     AS "MainGdrId1"
          ,0                                     AS "MainGdrId2"
          ,0                                     AS "MainGdrNum"
          ,0                                     AS "MainLgtSeq"
          ,CASE 
             WHEN S2."ClCode1" IS NOT NULL
             THEN S2."ClCode1" 
           ELSE 0 END                            AS "ClCode1"
          ,CASE
             WHEN S2."ClCode2" IS NOT NULL
             THEN S2."ClCode2" 
           ELSE 0 END                            AS "ClCode2"
          ,CASE
             WHEN S2."ClNo" IS NOT NULL
             THEN S2."ClNo"
          ELSE 0 END                             AS "ClNo"
          ,CASE
             WHEN S2."ClNo" IS NOT NULL 
             THEN 3 -- 3 單筆擔保品直接轉入
           ELSE 0 END AS "TfStatus" -- 0 未轉入
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE  
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE  
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 
    FROM "LA$BGTP" S0
    -- S2 : 此擔保品在 新系統的擔保品編號
    LEFT JOIN "ClNoMapping" S2 ON S2."GDRID1" = S0."GDRID1"
                              AND S2."GDRID2" = S0."GDRID2"
                              AND S2."GDRNUM" = S0."GDRNUM"
    WHERE S0."GDRID1" = 5-- 只抓擔保品代號1 = 5 其他 的擔保品
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClNoMap_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;





/
