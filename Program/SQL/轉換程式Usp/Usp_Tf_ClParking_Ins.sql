--------------------------------------------------------
--  DDL for Procedure Usp_Tf_ClParking_Ins
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_ClParking_Ins" 
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
    EXECUTE IMMEDIATE 'ALTER TABLE "ClParking" DISABLE PRIMARY KEY CASCADE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE "ClParking" DROP STORAGE';
    EXECUTE IMMEDIATE 'ALTER TABLE "ClParking" ENABLE PRIMARY KEY';

    -- 寫入資料
    INSERT INTO "ClParking"
    SELECT CB."ClCode1"                   AS "ClCode1"             -- 擔保品-代號1 DECIMAL 1 0
         , CB."ClCode2"                   AS "ClCode2"             -- 擔保品-代號2 DECIMAL 2 0
         , CB."ClNo"                      AS "ClNo"                -- 擔保品編號 DECIMAL 7 0
         , ROW_NUMBER() OVER (PARTITION BY CB."ClCode1"
                                         , CB."ClCode2"
                                         , CB."ClNo"
                              ORDER BY CNM."LGTSEQ"
                             )            AS "ParkingSeqNo"        -- 車位資料序號 DECIMAL 5 0
         , '.'                            AS "ParkingNo"           -- 車位編號 NVARCHAR2 20
         , 1                              AS "ParkingQty"          -- 車位數量 DECIMAL 5 0
         , HG."HGTCAP"                    AS "ParkingTypeCode"     -- 停車位形式 VARCHAR2 1
         , 1                              AS "OwnerPart"           -- 持份比率(分子) DECIMAL 10 0
         , 1                              AS "OwnerTotal"          -- 持份比率(分母) DECIMAL 10 0
         , NVL(CB."CityCode",'')          AS "CityCode"            -- 縣市 VARCHAR2 2 
         , NVL(CB."AreaCode",'')          AS "AreaCode"            -- 鄉鎮市區 VARCHAR2 3 
         , ''                             AS "IrCode"              -- 段小段代碼 VARCHAR2 5
         , ''                             AS "BdNo1"               -- 建號 VARCHAR2 4 
         , ''                             AS "BdNo2"               -- 建號(子號) VARCHAR2 4 
         , ''                             AS "LandNo1"             -- 地號 VARCHAR2 4 
         , ''                             AS "LandNo2"             -- 地號(子號) VARCHAR2 4 
         , HG."HGTCAM"                    AS "ParkingArea"         -- 車位面積(坪) DECIMAL 16 2
         , 0                              AS "Amount"              -- 價格(元) decimal 16 2;
         , JOB_START_TIME                 AS "CreateDate"          -- 建檔日期時間 DATE 0 0
         , '999999'                       AS "CreateEmpNo"         -- 建檔人員 VARCHAR2 6 0
         , JOB_START_TIME                 AS "LastUpdate"          -- 最後更新日期時間 DATE 0 0
         , '999999'                       AS "LastUpdateEmpNo"     -- 最後更新人員 VARCHAR2 6 0
    FROM "ClBuilding" CB
    LEFT JOIN "ClNoMapping" CNM ON CNM."ClCode1" = CB."ClCode1"
                               AND CNM."ClCode2" = CB."ClCode2"
                               AND CNM."ClNo"    = CB."ClNo"
    LEFT JOIN "LA$HGTP" HG ON HG."GDRID1" = CNM."GDRID1"
                          AND HG."GDRID2" = CNM."GDRID2"
                          AND HG."GDRNUM" = CNM."GDRNUM"
                          AND HG."LGTSEQ" = CNM."LGTSEQ"
    WHERE NVL(HG."HGTCAM",0) > 0
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
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_ClParking_Ins',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;




/
