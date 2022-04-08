--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ClBuildingPublic_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_Tf_ClBuildingPublic_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClBuildingPublic" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClBuildingPublic" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClBuildingPublic" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClBuildingPublic"
    WITH TMP_PHGP AS (
      SELECT "GDRID1"
           , "GDRID2"
           , "GDRNUM"
           , "LGTSEQ"
           , "HGTPNM"
           , "HGTPNM2"
           , ROW_NUMBER()
             OVER (
               PARTITION BY "GDRID1"
                          , "GDRID2"
                          , "GDRNUM"
                          , "LGTSEQ"
               ORDER BY "HGTPNM"
                      , "HGTPNM2"
             ) AS "Seq"
      FROM "LA$PHGP" PH
    )
    SELECT CB."ClCode1"                   AS "ClCode1"             -- 擔保品-代號1 DECIMAL 1 0
          ,CB."ClCode2"                   AS "ClCode2"             -- 擔保品-代號2 DECIMAL 2 0
          ,CB."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 0
          ,ROW_NUMBER()
           OVER (
             PARTITION BY CB."ClCode1"
                        , CB."ClCode2"
                        , CB."ClNo"
             ORDER BY NVL(PH."HGTPNM",0)
                    , NVL(PH."HGTPNM2",0)
                    , CNM."GDRID1"
                    , CNM."GDRID2"
                    , CNM."GDRNUM"
                    , CNM."LGTSEQ"
           )                              AS "PublicSeq"           -- 公設序號 DECIMAL 7 0
          ,NVL(PH."HGTPNM",0)             AS "PublicBdNo1"         -- 公設建號 DECIMAL 5 0
          ,NVL(PH."HGTPNM2",0)            AS "PublicBdNo2"         -- 公設建號(子號) DECIMAL 3 0
          ,NVL(HG."HGTPSM",0)             AS "Area"                -- 登記面積(坪) DECIMAL 16 2
        --   ,SUM(HG."HGTPSM")               AS "Area"                -- 登記面積(坪) DECIMAL 16 2
          ,''                             AS "OwnerId"             -- 所有權人統編 VARCHAR2 10 0
          ,u''                            AS "OwnerName"           -- 所有權人姓名 NVARCHAR2 100 0
          ,JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0
          ,'999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
          ,JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
          ,'999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "ClBuilding" CB
    LEFT JOIN "ClNoMapping" CNM ON CNM."ClCode1" = CB."ClCode1"
                               AND CNM."ClCode2" = CB."ClCode2"
                               AND CNM."ClNo"    = CB."ClNo"
    LEFT JOIN TMP_PHGP PH ON PH."GDRID1" = CNM."GDRID1"
                         AND PH."GDRID2" = CNM."GDRID2"
                         AND PH."GDRNUM" = CNM."GDRNUM"
                         AND PH."LGTSEQ" = CNM."LGTSEQ"
    LEFT JOIN "LA$HGTP" HG ON HG."GDRID1" = CNM."GDRID1"
                          AND HG."GDRID2" = CNM."GDRID2"
                          AND HG."GDRNUM" = CNM."GDRNUM"
                          AND HG."LGTSEQ" = CNM."LGTSEQ"
                          AND NVL(PH."Seq",1) = 1
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClBuildingPublic_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;


/
