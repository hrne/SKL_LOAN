CREATE OR REPLACE PROCEDURE "Usp_Tf_TfClOtherRightsMap_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "TfClOtherRightsMap" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "TfClOtherRightsMap" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "TfClOtherRightsMap" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "TfClOtherRightsMap" (
          "ClCode1"
        , "ClCode2"
        , "ClNo"
        , GDTRDT
        , "Seq"
        , GDRID1
        , GDRID2
        , GDRNUM
        , LMSACN
        , LMSAPN
    )
    WITH rawData AS (
        SELECT DISTINCT
               CNM."ClCode1"
             , CNM."ClCode2"
             , CNM."ClNo"
             , NVL(G.GDTRDT,0)      AS GDTRDT
             , G.GDRID1
             , G.GDRID2
             , G.GDRNUM
             , NVL(A.LMSACN,0)      AS LMSACN
             , NVL(A.LMSAPN,0)      AS LMSAPN
        FROM "ClNoMap" CNM
        LEFT JOIN LA$GDTP G ON G.GDRID1 = CNM."GdrId1"
                           AND G.GDRID2 = CNM."GdrId2"
                           AND G.GDRNUM = CNM."GdrNum"
        LEFT JOIN LA$APLP A ON A.GDRID1 = G.GDRID1
                           AND A.GDRID2 = G.GDRID2
                           AND A.GDRNUM = G.GDRNUM
        WHERE CNM."ClCode1" IN (1,2)
          AND CNM."TfStatus" IN ('1','3')
          AND NVL(A.LMSACN,0) != 0
          AND NVL(A.LMSAPN,0) != 0
    )
    SELECT "ClCode1"
         , "ClCode2"
         , "ClNo"
         , GDTRDT
         , '9999-'||
           LPAD(
             ROW_NUMBER()
             OVER (
               PARTITION BY "ClCode1"
                          , "ClCode2"
                          , "ClNo"
               ORDER BY GDTRDT
             )
             ,3,'0')            AS "Seq"
         , GDRID1
         , GDRID2
         , GDRNUM
         , LMSACN
         , LMSAPN
    FROM rawData
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_TfClOtherRightsMap_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;
/